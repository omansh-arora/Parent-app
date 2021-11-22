package com.example.parentapp.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.example.parentapp.model.LocalStorage;

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
    private Button button_addImage;
    private ImageView imgPFP;
    String imageURI;
    String baseIMG;
    boolean pic_edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        Resources resources = this.getResources();
        imageURI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        baseIMG = imageURI;


        //init child manager
        childManager = new ChildManager();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure A Child");

        //get input of name,age
        childNameEt = (EditText) findViewById(R.id.childNameEt);
        childAgeEt = (EditText) findViewById(R.id.childAgeEditText);
        formTitleTv = (TextView) findViewById(R.id.AddChildLabelTv);
        saveChildBtn = (Button) findViewById(R.id.addChildBtn);
        deleteBtn = (Button) findViewById(R.id.deleteChildBtn);
        genderRBs = (RadioGroup) findViewById(R.id.genderRBsGroup);

        //Addpfp
        button_addImage = findViewById(R.id.bt_setPFP);
        imgPFP = findViewById(R.id.imgPFP);

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

        //get parameters ("Edit" or "Add") and clicked child index from ChildrenActivity
        extractDataFromIntent();

        //  This function detects whether we need "add" or "edit". reload child info if its' "Edit"
        initialInputFields();

        button_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AddChildActivity.this, "change pic clicked!!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddChildActivity.this, addImageActivity.class);
                if (formAction.equals("Edit")) {
                    i.putExtra("position", childClickedIndex);
                    i.putExtra("mode", "Edit");
                } else {
                    i.putExtra("mode", "");
                }
                startActivity(i);

            }
        });


    }

    protected void onResume() {

        if (getPFPUri(this) != Uri.parse(baseIMG)) {
            imgPFP.setImageURI(getPFPUri(this));
            imageURI = getPFPUri(this).toString();
        }
        super.onResume();
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
                child.setPicture(imageURI);
                childManager.addNewChild(child);
                break;
            case "Edit":
//                editChild.setName(name);
//                editChild.setAge(age);
//                editChild.setGender(gender);
//                editChild.setPicture(imageURI);
//                childManager.saveChildren();
                childManager.updateChild(editChild, name, age, gender, imageURI);
                //LocalStorage.getInstance().saveQueues();
                break;
            default:
                break;
        }

    }

    public void confirmDelete() {
        /*
        AlertDialog creation code token from: https://abhiandroid.com/ui/alertdialog
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddChildActivity.this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_delete_forever_35);
        alertDialogBuilder.setMessage("Are you sure you want to delete " + editChild.getName() + " ?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                childManager.deleteChild(childClickedIndex);
                Toast.makeText(AddChildActivity.this, "Child Deleted!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddChildActivity.this, "You cancelled delete", Toast.LENGTH_SHORT).show();
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
                childrenList = childManager.getChildren();
                //get clicked child and load child info to input fields
                editChild = childrenList.get(childClickedIndex);
                imgPFP.setImageURI(Uri.parse(childrenList.get(childClickedIndex).getPicture()));
                savePFPUri(Uri.parse(childrenList.get(childClickedIndex).getPicture()));
                childNameEt.setText(editChild.getName());
                childAgeEt.setText(Integer.toString(editChild.getAge()));
                gender = editChild.getGender();

                switch (gender) {
                    case "Girl":
                        ((RadioButton) genderRBs.getChildAt(0)).setChecked(true);
                        break;
                    case "Boy":
                        ((RadioButton) genderRBs.getChildAt(1)).setChecked(true);
                        break;
                }

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmDelete();
                    }
                });

                break;
            case "Add":
                savePFPUri(Uri.parse(baseIMG));
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
                    finish();
                }

            }

        });

    }

    static public Uri getPFPUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("imgURIprefs", MODE_PRIVATE);
        String uriString = prefs.getString("Image", "");
        return Uri.parse(uriString);
    }

    private void savePFPUri(Uri uri) {
        SharedPreferences prefs = this.getSharedPreferences("imgURIprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String uriString = uri.toString();
        editor.putString("Image", uriString);
        editor.apply();
    }


}