package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;


public class AddChildActivity extends AppCompatActivity {

    private Button saveChildBtn;
    private ChildManager childManager;
    private EditText childNameEt;
    private EditText childAgeEt;
    private String childName;
    private Integer childAge;
    Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        //init child manager
        childManager = ChildManager.getInstance();

      //get input of name,age
        childNameEt = (EditText) findViewById(R.id.childNameEt);
        childAgeEt = (EditText) findViewById(R.id.childAgeEditText);


        saveChildBtn = (Button) findViewById(R.id.addChildBtn);
        saveChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create child obj and add to list
                childName = childNameEt.getText().toString();
                childAge = Integer.parseInt(childAgeEt.getText().toString());
                child = new Child(childName,childAge);
                childManager.addNewChild(child);

//                String testStr = "Child Added!!!  Name: " + childName;
//                Toast.makeText(AddChildActivity.this, testStr, Toast.LENGTH_LONG).show();
                Intent childrenListIntent = new Intent(AddChildActivity.this, ChildrenActivity.class);
                startActivity(childrenListIntent);

            }
        });


    }
}