package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;


public class BreathsConfigActivity extends AppCompatActivity {

    private String[] breathsNums = {"1","2","3","4",
            "5","6","7","8","9","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breaths_config);

        //setup and load popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .85), (int) (height * .85));

        // load Breaths N Size and register click event when a item is clicked
        populateListView();
        registerClickCallback();

    }

    private void populateListView() {

        //Setup adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.breath_config_item, breathsNums);

        ListView listView = (ListView) findViewById(R.id.breathsNumlv);
        listView.setAdapter(adapter);

    }


    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.breathsNumlv);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(BreathsConfigActivity.this, "Breath N selected: " + breathsNums[position], Toast.LENGTH_SHORT).show();
                LocalStorage.getInstance().saveBreaths(breathsNums[position]);
                finish();
            }
        });
    }



}