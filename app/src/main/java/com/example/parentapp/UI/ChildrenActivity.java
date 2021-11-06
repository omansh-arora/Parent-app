package com.example.parentapp.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.parentapp.R;

import java.util.List;

public class ChildrenActivity extends AppCompatActivity {

    private FloatingActionButton addChildActivityFab;
    private ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure Children");

        //init child manager
        childManager = ChildManager.getInstance();

        // direct to Add a child info page
        addChildActivityFab = (FloatingActionButton) findViewById(R.id.fabAddChild);
        addChildActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddChildActivity.makeIntent(ChildrenActivity.this, "Add", 0);
                startActivity(intent);
            }
        });

        // show all added children
        populateListView();

        //register an event when a child is clicked in the listView
        registerClickCallback();

    }

    /*** Setup Array Adapter and Display child in listView **/
    private void populateListView() {
        // create  list of items
        List<Child> childrenList = childManager.getChildrenList();

        // build adapter
        ArrayAdapter<Child> adapter = new ArrayAdapter<Child>(
                this,
                R.layout.child_item, //Layout to use (create)
                childrenList); //Items to be displayed

        // configure the list view
        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setAdapter(adapter);
    }

    /*** register click event when a child item is clicked in the listview  **/
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;

                // link to add/update child activity page when a child is clicked
                Intent intent = AddChildActivity.makeIntent(ChildrenActivity.this, "Edit", position);
                startActivity(intent);

            }
        });
    }


}