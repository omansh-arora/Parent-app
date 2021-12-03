package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;
import com.example.parentapp.model.Task;
import com.example.parentapp.model.TaskManager;

import org.w3c.dom.Text;

public class ViewTaskWindowActivity extends AppCompatActivity {

    private String taskName;
    private TextView taskNameTv;
    private Button historyBtn;
    private Button editBtn;
    private Button deleteBtn;
    private ImageButton coinFlipBtn;
    private TextView selectedChildNameTv;
    private ImageView selectedChildPicIV;
    private Task task;
    private TaskManager taskManager;
    String baseIMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_window);

        //setup and load popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .85), (int) (height * .85));

        Resources resources = this.getResources();
        baseIMAGE = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        //get task name via Intent
        Intent intent = getIntent();
        taskName = intent.getStringExtra("task_name");

        //get current task
        taskManager = new TaskManager();
        task = new Task(taskName);

        //display task Name, selected Child name
        taskNameTv = (TextView) findViewById(R.id.taskNameTV);
        taskNameTv.setText("Task Name: " + taskName);

        historyBtn = (Button) findViewById(R.id.historyBtn);
        editBtn = (Button) findViewById(R.id.editBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        coinFlipBtn = (ImageButton) findViewById(R.id.coinFlipBtn);

        selectedChildNameTv = (TextView) findViewById(R.id.selectedChildNameTv);
        selectedChildNameTv.setText("Next Child: " + LocalStorage.getInstance().getSelectedChild(taskName).getName());

        Uri imgPFP = LocalStorage.getInstance().getSelectedChild(taskName).getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(LocalStorage.getInstance().getSelectedChild(taskName).getPicture());
        selectedChildPicIV = (ImageView) findViewById(R.id.selectedChildPicIV);
        selectedChildPicIV.setImageURI(imgPFP);
        selectedChildPicIV.setPadding(5,2,5,2);

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TossHistoryActivity.class);
                intent.putExtra("task_name", taskName);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start Edit Task Activity
                // link to add/update child activity page when a child is clicked
                Intent intent = AddTaskActivity.makeIntent(ViewTaskWindowActivity.this, "Edit", taskName);
                startActivity(intent);
                finish();
            }

        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove the task
                taskManager.deleteTask(task);
                finish();
            }

        });

        coinFlipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start coinflip activity and load associated histories
                Intent intent = new Intent(getApplicationContext(), CoinFlipActivity.class);
                intent.putExtra("task_name", taskName);
                startActivity(intent);
            }

        });

    }
}