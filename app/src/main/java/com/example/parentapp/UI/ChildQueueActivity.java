package com.example.parentapp.UI;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildrenQueue;
import com.example.parentapp.model.CoinFlipHistory;
import com.example.parentapp.model.TaskManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ChildQueueActivity extends AppCompatActivity {

    private List<Child> children;
    ChildrenQueue childrenQueue;
    private CoinFlipHistory coinFlipHistory;
    String taskName = TaskManager.DEFAULT_TASK.getName();
    String baseIMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_picker);

        Resources resources = this.getResources();
        baseIMAGE = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        //setup and load popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .85), (int) (height * .85));

        // get current task
        Intent intent = getIntent();
        taskName = intent.getStringExtra("task_name");
        if (taskName == null) {
            taskName = TaskManager.DEFAULT_TASK.getName();
        }

        //init coin flip manager
        coinFlipHistory = new CoinFlipHistory(taskName);
        childrenQueue = new ChildrenQueue(taskName);
        children = childrenQueue.getChildren();
        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        //configure the list view
        ListView list = (ListView) findViewById(R.id.queueLv);

        //build adapter
        ArrayAdapter<Child> adapter = new ChildrenListAdapter();
        list.setAdapter(adapter);
    }

    private class ChildrenListAdapter extends ArrayAdapter<Child> {

        public ChildrenListAdapter() {
            super(ChildQueueActivity.this, R.layout.override_item_view, children);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // need to check if view is null or not before use it
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.override_item_view, parent, false);
            }

            //find the toss game to work with
            Child childRow = children.get(position);

            //fill the view
            Uri imgPFP = childRow.getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(childRow.getPicture());
            ImageView imageView = itemView.findViewById(R.id.item_icon);
            imageView.setImageURI(imgPFP);
            imageView.setPadding(5,2,5,2);


            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String childRowName = childRow.getName();
            outputTV.setText(childRowName);

            return itemView;
        }
    }

    /*** register click event when a child item is clicked in the listview  **/
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.queueLv);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                childrenQueue.setSelectedChild(position);
                finish();
            }
        });
    }
}