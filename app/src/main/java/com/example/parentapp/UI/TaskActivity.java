package com.example.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.Task;
import com.example.parentapp.model.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private FloatingActionButton addTaskActivityFab;
    private TaskManager taskManager;
    private List<Task> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setTitle("All Tasks");

        //init Task manager
        taskManager = new TaskManager();
        tasksList = taskManager.getTasks();

        // direct to Add a task page
        addTaskActivityFab = (FloatingActionButton) findViewById(R.id.fabAddTask);
        addTaskActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddTaskActivity.makeIntent(TaskActivity.this, "Add", 0);
                startActivity(intent);
            }
        });

        //load listview
        populateListView();
    }


    private void populateListView() {
        //build adapter
        ArrayAdapter<Task> adapter = new TaskAdapter();

        //configure the list view
        ListView list = (ListView) findViewById(R.id.tasksLv);
        list.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter() {
            super(TaskActivity.this, R.layout.item_view, taskManager.getTasks());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // need to check if view is null or not before use it
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_item, parent, false);
            }

            Task currentTask = taskManager.getTasks().get(position);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_task_txt);
            outputTV.setText(currentTask.getName());

            return itemView;
        }

    }

}
