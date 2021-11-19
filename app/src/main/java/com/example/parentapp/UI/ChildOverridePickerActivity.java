package com.example.parentapp.UI;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.ChildrenListMaintainer;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChildOverridePickerActivity extends AppCompatActivity {

    private ChildManager childManager;
    private List<Child> childrenList;
    private List<Child> sortedChildrenList;
    private static final String PREFS_NAME = "ChildPrefs";
    Child defaultChild;
    Child newSelectedChild;
    Integer newChildPosition = 0;
    ChildrenListMaintainer childrenlstMaintainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_picker);

        //setup and load popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .85), (int) (height * .85));


        //init child manager
        childManager = ChildManager.getInstance();
        if (getChildManager(this) != null)
            childManager = getChildManager(this);

        childrenList = childManager.getChildrenList();
        //Toast.makeText(ChildOverridePickerActivity.this, "Original childrenList Size -> " + String.valueOf(childrenList.size()), Toast.LENGTH_SHORT).show();

        childrenlstMaintainer = new ChildrenListMaintainer(childrenList);

        childrenlstMaintainer.sortChildrenListByDefaultTop();
        sortedChildrenList = childrenlstMaintainer.getChildrenList();
        defaultChild = childrenlstMaintainer.getNextChild();

//        Toast.makeText(ChildOverridePickerActivity.this, "sortedChildrenList Size -> " + String.valueOf(sortedChildrenList.size()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(ChildOverridePickerActivity.this, "defaultChild(Next) is -> " + defaultChild.getName(), Toast.LENGTH_SHORT).show();

        populateListView();

    }

    protected void onStart() {

        childManager = ChildManager.getInstance();
        if (getChildManager(this) != null)
            childManager = getChildManager(this);
        // show all added children
        populateListView();

        //register an event when a child is clicked in the listView
        super.onStart();

    }

    private void populateListView() {

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listOverrideChildren);

        //build adapter
        ArrayAdapter<Child> adapter = new ChildrenListAdapter();
        list.setAdapter(adapter);

        // setup buttons
        Button cancelBtn = (Button) findViewById(R.id.cancelOverrideBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the popup without make any modification
                finish();
                Intent myIntent = new Intent(ChildOverridePickerActivity.this, CoinFlipActivity.class);
                startActivity(myIntent);
            }
        });

        Button okBtn = (Button) findViewById(R.id.confirmOverrideBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if no new child has select
                if (newSelectedChild == null) {
                    Toast.makeText(ChildOverridePickerActivity.this, "You haven't selected a new child yet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if radio button equals to default child, stay the order and exit
                if (newSelectedChild.getName().equals(defaultChild.getName())) {
                    Toast.makeText(ChildOverridePickerActivity.this, "You chose to stay with the default child! Nothing will change!", Toast.LENGTH_LONG).show();
                } else {
                    // if a new child is select, update order
                    Toast.makeText(ChildOverridePickerActivity.this, "New Child Turn -> " + newSelectedChild.getName() + " Index--> " + newChildPosition, Toast.LENGTH_LONG).show();
                    childrenlstMaintainer.setOverrideChildrenList(newChildPosition);

//                    if(getChildManager(ChildOverridePickerActivity.this)!=null){
//                        childManager.setNewChildrenList(getChildManager(ChildOverridePickerActivity.this).getOverrideChildrenList());
//                    }

                    finish();
                }

            }
        });

    }

    private class ChildrenListAdapter extends ArrayAdapter<Child> {

        RadioButton selected = null;

        public ChildrenListAdapter() {
            super(ChildOverridePickerActivity.this, R.layout.override_item_view, sortedChildrenList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // need to check if view is null or not before use it
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.override_item_view, parent, false);
            }

            //find the toss game to work with
            Child childRow = sortedChildrenList.get(position);

            //fill the view
            Integer imgResource;
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);

            imgResource = childRow.getGender().equals("Boy") ? R.drawable.ic_baseline_child_boy_35 : R.drawable.ic_baseline_child_girl_35;
            imageView.setImageResource(imgResource);
            //imageView.setPadding(5, 2, 5, 2);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String childRowName = childRow.getName();
            outputTV.setText(childRowName);


            // Setup Radio buttons
            RadioButton childRowRB = (RadioButton) itemView.findViewById(R.id.overrideChildRB);
            childRowRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected != null) {
                        selected.setChecked(false);
                    }
                    childRowRB.setChecked(true);
                    selected = childRowRB;

                    //set new selected child
                    newSelectedChild = sortedChildrenList.get(position);
                    String name = sortedChildrenList.get(position).getName();
                    newChildPosition = position;

                    //Toast.makeText(ChildOverridePickerActivity.this, "Selected -> " + name, Toast.LENGTH_SHORT).show();
                }
            });

            //get Current Turn Child
            CheckedTextView childRowCTV = (CheckedTextView) itemView.findViewById(R.id.defaultChildCtv);

            //reset
            childRowRB.setVisibility(View.VISIBLE);
            childRowCTV.setVisibility(View.INVISIBLE);

            if (defaultChild.getName().equals(childRowName)) {
                //Toast.makeText(ChildOverridePickerActivity.this,  defaultChild.getName() + " equals --> " + childRowName, Toast.LENGTH_SHORT).show();
                //childRowRB.setVisibility(View.INVISIBLE);
                childRowCTV.setText("Default");
                childRowCTV.setVisibility(View.VISIBLE);
            }

            return itemView;
        }
    }

    static public ChildManager getChildManager(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("ChildManager", "");
        return gson.fromJson(json, ChildManager.class);
    }


}