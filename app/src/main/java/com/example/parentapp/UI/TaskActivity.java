package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.parentapp.R;

/**
 * I want the app to maintain a list of tasks and help me track which child get to do it next because there are always disagreements about who gets to do something first, or who's turn it is to do it. For example, "Gets to pick what music plays before dinner" or "Picks first which bowl of fruit."
 * I want to be able to add, remove, and edit the tasks.
 * I want each task to have a name, such as "First bath", or "Put pop can into can cooler".
 * The list of tasks should show the task name, and the name of the child whose turn it is next so I can quickly see the configured tasks.
 * Tapping on a task puts up a pop-up message (or screen) showing:
 * The name of the task
 * Name and photo of the child whose turn it is to do this task next
 * Convenient way to confirm that the child has had their turn (and returns to the task list)
 * Convenient way of cancelling (and returns to the task list)
 * When a child has had their turn, it automatically advances to the next child's turn (round-robin) for that task next time
 * I want all the tasks, and whose turn it is, to be saved between executions of the program so that the app can help me track whose turn it is in the future.
 */
public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setTitle("Task");
    }
}
