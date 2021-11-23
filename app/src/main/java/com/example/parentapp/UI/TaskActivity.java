package com.example.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.LocalStorage;
import com.example.parentapp.model.Task;
import com.example.parentapp.model.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

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
                Intent intent = AddTaskActivity.makeIntent(TaskActivity.this, "Add", taskManager.DEFAULT_TASK.getName() );
                startActivity(intent);
            }
        });

        //load listview
        populateListView();

        registerClickCallback();
    }


    private void populateListView() {
        //build adapter
        ArrayAdapter<Task> adapter = new TaskAdapter();

        //configure the list view
        ListView list = (ListView) findViewById(R.id.tasksLv);
        list.setAdapter(adapter);
    }

    /*** register click event when a child item is clicked in the listview  **/
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.tasksLv);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //start intent (open popup)
                Intent intent = new Intent(getApplicationContext(), ViewTaskWindowActivity.class);
                intent.putExtra("task_name", taskManager.getTasks().get(position).getName());
                startActivity(intent);
            }
        });
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
            //        currentTaskTv.setText("Task: " + (taskName.length() > 10 ? taskName.substring(0, 10) + "..." : taskName));
            outputTV.setText(currentTask.getName().length() > 20 ? currentTask.getName().substring(0,20) + "..." :  currentTask.getName());

            TextView selectedChildTv = (TextView) itemView.findViewById(R.id.selected_child_Tv) ;

            selectedChildTv.setText(LocalStorage.getInstance().getSelectedChild(currentTask.getName()).getName());

            return itemView;
        }

    }

}
