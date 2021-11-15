package com.example.parentapp.UI;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ChildOverridePickerActivity extends AppCompatActivity {

    private ChildManager childManager;
    private List<Child> childrenList;
    private static final String PREFS_NAME = "ChildPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_picker);

        //setup and load popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8),(int) (height*.8));

        //init child manager
        childManager = ChildManager.getInstance();
        if(getChildManager(this)!=null)
            childManager = getChildManager(this);
        childrenList = childManager.getChildrenList();

        populateListView();
    }

    protected void onStart() {

        childManager = ChildManager.getInstance();
        if(getChildManager(this)!=null)
            childManager = getChildManager(this);
        // show all added children
        populateListView();

        //register an event when a child is clicked in the listView
        super.onStart();

    }

    private void populateListView() {
        //build adapter
        ArrayAdapter<Child> adapter = new ChildrenListAdapter();

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setAdapter(adapter);

    }

    private class ChildrenListAdapter extends ArrayAdapter<Child> {
        public ChildrenListAdapter() {
            super(ChildOverridePickerActivity.this, R.layout.item_view, childrenList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // need to check if view is null or not before use it
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //find the toss game to work with
            Child currentChild = childrenList.get(position);

            //fill the view
            Integer imgResource;
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);

            imgResource = currentChild.getGender().equals("Boy") ? R.drawable.ic_baseline_child_boy_35 : R.drawable.ic_baseline_child_girl_35;
            imageView.setImageResource(imgResource);
            imageView.setPadding(5,2,5,2);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String output = currentChild.getName() + ", Age: " + currentChild.getAge();
            outputTV.setText(output);

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