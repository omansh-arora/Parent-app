package com.example.parentapp.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.Task;
import com.example.parentapp.model.TaskManager;


public class AddTaskActivity extends AppCompatActivity {

    public static final String ACTION_NAME = "Action";
    public static final String TASK_POSITION = "Task Index";
    private TaskManager taskManager;
    private String formAction;
    private Integer taskClickedIndex = 0;
    private String taskName;

    private EditText taskNameEt;
    private Button saveTaskBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Configure A Task");

        //init child manager
        taskManager = new TaskManager();

        //get name
        taskNameEt = (EditText) findViewById(R.id.taskNameET);
        saveTaskBtn = (Button) findViewById(R.id.addChildBtn);
        deleteBtn = (Button) findViewById(R.id.deleteChildBtn);

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create child obj and add to list
                boolean validate = true;
                if (taskNameEt.getText().toString().length() == 0) {
                    Toast.makeText(AddTaskActivity.this, "You must enter a task name ", Toast.LENGTH_SHORT).show();
                    validate = false;
                }

                if (validate) {
                    taskName = taskNameEt.getText().toString();
                    //Toast.makeText(AddChildActivity.this, "Gender: " + gender, Toast.LENGTH_SHORT).show();
                    configTask(taskName);
                    finish();
                }

            }

        });

        extractDataFromIntent();
    }

    private void configTask(String taskName){
        switch (formAction) {
            case "Add":
                Task task = new Task(taskName);
                taskManager.addNewTask(task);
                break;
            case "Edit":
                //to do..
                break;
            default:
                break;
        }
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        formAction = intent.getStringExtra(ACTION_NAME);
        taskClickedIndex = intent.getIntExtra(TASK_POSITION, 0);
    }

    public static Intent makeIntent(Context context, String actionName, int position) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(ACTION_NAME, actionName);
        intent.putExtra(TASK_POSITION, position);
        return intent;
    }

}