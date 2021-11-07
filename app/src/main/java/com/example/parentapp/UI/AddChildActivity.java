package com.example.parentapp.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.google.gson.Gson;

import java.util.List;


public class AddChildActivity extends AppCompatActivity {

    private Button saveChildBtn;
    private Button deleteBtn;
    private ChildManager childManager;
    private EditText childNameEt;
    private EditText childAgeEt;
    private RadioGroup genderRBs;
    private TextView formTitleTv;
    private String childName;
    private Integer childAge;
    public static final String ACTION_NAME = "Action";
    public static final String CHILD_POSITION = "Game Index";
    private String formAction;
    private Integer childClickedIndex = 0;
    private List<Child> childrenList;
    private Child editChild;
    private String gender = "";
    private static final String PREFS_NAME = "ChildPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        childManager = ChildManager.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure A Child");

        childManager.setChildrenList(getChildManager(this).getChildrenList());

        //get input of name,age
        childNameEt = (EditText) findViewById(R.id.childNameEt);
        childAgeEt = (EditText) findViewById(R.id.childAgeEditText);
        formTitleTv = (TextView) findViewById(R.id.AddChildLabelTv);
        saveChildBtn = (Button) findViewById(R.id.addChildBtn);
        deleteBtn = (Button) findViewById(R.id.deleteChildBtn) ;
        genderRBs = (RadioGroup) findViewById(R.id.genderRBsGroup);

        // setup child choice
        genderRBs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int choiceID) {
                switch (choiceID) {
                    case R.id.genderGirlRb:
                        gender = "Girl";
                        break;
                    case R.id.genderBoyRb:
                        gender = "Boy";
                        break;
                    default:
                        break;
                }
            }
        });

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

    private void configChild(String name, Integer age, String gender) {

        //update or add a new?
        switch (formAction) {
            case "Add":
                Child child = new Child(name, age, gender);
                childManager.addNewChild(child);
                saveChildManager(childManager);
                break;
            case "Edit":
                editChild.setName(name);
                editChild.setAge(age);
                editChild.setGender(gender);
                saveChildManager(childManager);

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
                saveChildManager(childManager);
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
                gender = editChild.getGender();

                switch (gender){
                    case "Girl":
                        ((RadioButton)genderRBs.getChildAt(0)).setChecked(true);
                        break;
                    case "Boy":
                        ((RadioButton)genderRBs.getChildAt(1)).setChecked(true);
                        break;
                }

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
                if (childNameEt.getText().toString().length() == 0 || childAgeEt.getText().toString().length() == 0 || genderRBs.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(AddChildActivity.this, "You must enter all the child information! ", Toast.LENGTH_SHORT).show();
                    validate = false;
                }

                if (validate) {
                    childName = childNameEt.getText().toString();
                    childAge = Integer.parseInt(childAgeEt.getText().toString());
                    //Toast.makeText(AddChildActivity.this, "Gender: " + gender, Toast.LENGTH_SHORT).show();
                    configChild(childName, childAge, gender);
                    Intent childrenListIntent = new Intent(AddChildActivity.this, ChildrenActivity.class);
                    startActivity(childrenListIntent);
                }

            }

        });

    }
    private void saveChildManager(ChildManager cm) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cm);
        editor.putString("ChildManager", json);
        editor.commit();
    }

    static public ChildManager getChildManager(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("ChildManager", "");
        ChildManager children = gson.fromJson(json, ChildManager.class);
        return children;
    }



}