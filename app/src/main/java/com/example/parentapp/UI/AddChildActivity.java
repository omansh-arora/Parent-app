package com.example.parentapp.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;

import java.util.List;


public class AddChildActivity extends AppCompatActivity {

    private Button saveChildBtn;
    private Button deleteBtn;
    private ChildManager childManager;
    private EditText childNameEt;
    private EditText childAgeEt;
    private TextView formTitleTv;
    private String childName;
    private Integer childAge;
    public static final String ACTION_NAME = "Action";
    public static final String CHILD_POSITION = "Game Index";
    private String formAction;
    private Integer childClickedIndex = 0;
    private List<Child> childrenList;
    private Child editChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure A Child");

        //get input of name,age
        childNameEt = (EditText) findViewById(R.id.childNameEt);
        childAgeEt = (EditText) findViewById(R.id.childAgeEditText);
        formTitleTv = (TextView) findViewById(R.id.AddChildLabelTv);
        saveChildBtn = (Button) findViewById(R.id.addChildBtn);
        deleteBtn = (Button) findViewById(R.id.deleteChildBtn) ;

        //init child manager
        childManager = ChildManager.getInstance();

        //get parameters ("Edit" or "Add") and clicked child index from ChildrenActivity
        extractDataFromIntent();

        //  This function detects whether we need "add" or "edit". reload child info if its' "Edit"
        initialInputFields();

    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        formAction = intent.getStringExtra(ACTION_NAME);
        childClickedIndex = intent.getIntExtra(CHILD_POSITION, 0);
    }

    public static Intent makeIntent(Context context, String actionName, int position) {
        Intent intent = new Intent(context, AddChildActivity.class);
        intent.putExtra(ACTION_NAME, actionName);
        intent.putExtra(CHILD_POSITION, position);
        return intent;
    }

    private void configChild(String name, Integer age) {

        //update or add a new?
        switch (formAction) {
            case "Add":
                Child child = new Child(name, age);
                childManager.addNewChild(child);
                break;
            case "Edit":
                editChild.setName(name);
                editChild.setAge(age);
                break;
            default:
                break;
        }

    }

    public void comfirmDelete(){
        /*
        AlertDialog creation code token from: https://abhiandroid.com/ui/alertdialog
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddChildActivity.this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_delete_forever_35);
        alertDialogBuilder.setMessage("Are you sure you want to delete "+ editChild.getName() + " ?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                childManager.deleteChild(childClickedIndex);
                Toast.makeText(AddChildActivity.this,"Child Deleted!",Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(AddChildActivity.this, ChildrenActivity.class);
                startActivity(myIntent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddChildActivity.this,"You cancelled delete",Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void initialInputFields() {

        switch (formAction) {
            case "Edit":
                formTitleTv.setText("Edit a child");
                saveChildBtn.setText("Update");
                //get all the children in list
                childrenList = childManager.getChildrenList();
                //get clicked child and load child info to input fields
                editChild = childrenList.get(childClickedIndex);
                childNameEt.setText(editChild.getName());
                childAgeEt.setText(Integer.toString(editChild.getAge()));
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        comfirmDelete();
                    }
                });

                break;
            case "Add":
                formTitleTv.setText("Add a child");
                deleteBtn.setVisibility(View.INVISIBLE);
                saveChildBtn.setWidth(400);
                break;
        }


        saveChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create child obj and add to list
                boolean validate = true;
                if (childNameEt.getText().toString().length() == 0 || childAgeEt.getText().toString().length() == 0) {
                    Toast.makeText(AddChildActivity.this, "Child Information can not be empty ", Toast.LENGTH_SHORT).show();
                    validate = false;
                }

                if (validate) {
                    childName = childNameEt.getText().toString();
                    childAge = Integer.parseInt(childAgeEt.getText().toString());
                    configChild(childName, childAge);
                    Intent childrenListIntent = new Intent(AddChildActivity.this, ChildrenActivity.class);
                    startActivity(childrenListIntent);
                }

            }

        });

    }

}