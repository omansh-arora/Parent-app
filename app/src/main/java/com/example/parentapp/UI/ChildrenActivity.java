package com.example.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.CoinFlip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.parentapp.R;
import com.google.gson.Gson;

import java.util.List;

public class ChildrenActivity extends AppCompatActivity {

    private FloatingActionButton addChildActivityFab;
    private ChildManager childManager;
    private List<Child> childrenList;
    private static final String PREFS_NAME = "ChildPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure Children");

        //init child manager
        childManager = ChildManager.getInstance();
        if(getChildManager(this)!=null)
            childManager = getChildManager(this);
        childrenList = childManager.getChildrenList();

        // direct to Add a child info page
        addChildActivityFab = (FloatingActionButton) findViewById(R.id.fabAddChild);
        addChildActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddChildActivity.makeIntent(ChildrenActivity.this, "Add", 0);
                startActivity(intent);
            }
        });
        populateListView();


    }
    protected void onStart() {

        childManager = ChildManager.getInstance();
        if(getChildManager(this)!=null)
        childManager = getChildManager(this);
        // show all added children
        populateListView();

        //register an event when a child is clicked in the listView
        registerClickCallback();
        super.onStart();

    }


    /*** Setup Array Adapter and Display child in listView **/
//    private void populateListView() {
//        // create  list of items
//        List<Child> childrenList = childManager.getChildrenList();
//
//        // build adapter
//        ArrayAdapter<Child> adapter = new ArrayAdapter<Child>(
//                this,
//                R.layout.child_item, //Layout to use (create)
//                childrenList); //Items to be displayed
//
//        // configure the list view
//        ListView list = (ListView) findViewById(R.id.listAllChildren);
//        list.setAdapter(adapter);
//    }
    private void populateListView() {

        //build adapter
        ArrayAdapter<Child> adapter = new ChildrenListAdapter();

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setAdapter(adapter);

    }

    private class ChildrenListAdapter extends ArrayAdapter<Child> {
        public ChildrenListAdapter() {
            super(ChildrenActivity.this, R.layout.item_view, childrenList);
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
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            int imgResource = currentChild.getGender() == "Boy" ? R.drawable.ic_baseline_child_boy_35 : R.drawable.ic_baseline_child_girl_35;
            imageView.setImageResource(imgResource);
            imageView.setPadding(5,2,5,2);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String output = currentChild.getName() + ", Age: " + currentChild.getAge();
            outputTV.setText(output);

            return itemView;
        }
    }

    /*** register click event when a child item is clicked in the listview  **/
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                // link to add/update child activity page when a child is clicked
                Intent intent = AddChildActivity.makeIntent(ChildrenActivity.this, "Edit", position);
                startActivity(intent);

            }
        });
    }



    static public ChildManager getChildManager(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("ChildManager", "");
        return gson.fromJson(json, ChildManager.class);
    }

    private void saveChildManager(ChildManager cm) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cm);
        editor.putString("ChildManager", json);
        editor.commit();
    }


}