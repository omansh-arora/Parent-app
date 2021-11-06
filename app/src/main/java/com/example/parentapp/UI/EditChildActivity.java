package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;

public class EditChildActivity extends AppCompatActivity {

    private Button saveEditBtn;
    private ChildManager childManager;
    private EditText childNameEt;
    private EditText childAgeEt;
    private String childName;
    private Integer childAge;
    Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        childManager = ChildManager.getInstance();

        childNameEt = (EditText) findViewById(R.id.editChildNameEt);
        childAgeEt = (EditText) findViewById(R.id.editChildAgeEt);
        Intent intent = getIntent();
        int selectedChildIndex = intent.getIntExtra("Position",-1);

        saveEditBtn = (Button) findViewById(R.id.editChildBtn);
        saveEditBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                childName = childNameEt.getText().toString();
                childAge = Integer.parseInt(childAgeEt.getText().toString());
                child = new Child(childName, childAge);
                childManager.editChild(selectedChildIndex, child);
                Intent childrenListIntent = new Intent(EditChildActivity.this, ChildrenActivity.class);
                startActivity(childrenListIntent);
            }
        });

    }
}