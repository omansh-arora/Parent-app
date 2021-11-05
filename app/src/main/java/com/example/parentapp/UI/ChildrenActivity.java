package com.example.parentapp.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

        //init child manager
        childManager = ChildManager.getInstance();

        // direct to Add a child info page
        addChildActivityFab = (FloatingActionButton) findViewById(R.id.fabAddChild);
        addChildActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ChildrenActivity.this, "Add A child", Toast.LENGTH_SHORT).show();
                Intent intentChildActivity = new Intent(ChildrenActivity.this, AddChildActivity.class);
                startActivity(intentChildActivity);
            }
        });

        // show all added children
        populateListView();

    }


    private void populateListView() {
        //create  list of items
        List<Child> childrenList = childManager.getChildrenList();

        //build adapter
        ArrayAdapter<Child> adapter = new ArrayAdapter<Child>(
                this,
                R.layout.game_item, //Layout to use (create)
                childrenList); //Items to be displayed

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listAllChildren);
        list.setAdapter(adapter);

    }



}