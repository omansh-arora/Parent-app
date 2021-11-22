package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;


public class MainActivity extends AppCompatActivity {

    private Button flipCoinActivityBtn, timerActivityBtn, addChildActivityBtn, helpBtn;

    private Button taskActivityBtn;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localStorage = LocalStorage.getInstance(this.getApplicationContext());

        addChildActivityBtn = (Button) findViewById(R.id.btnAddChild);
        flipCoinActivityBtn = (Button) findViewById(R.id.btnFlipCoin);
        helpBtn = findViewById(R.id.bt_help);

        addChildActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intentChildrenActivity = new Intent(MainActivity.this, ChildrenActivity.class);
                startActivity(intentChildrenActivity);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intentHELP = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intentHELP);
            }
        });


        flipCoinActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intentFlipCoinActivity = new Intent(MainActivity.this, CoinFlipActivity.class);
                startActivity(intentFlipCoinActivity);
            }
        });


        timerActivityBtn = (Button) findViewById(R.id.btnTimer);
        timerActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timerIntent = new Intent(MainActivity.this,TimerScreen.class);
                timerIntent.putExtra("Type",0);
                startActivity(timerIntent);
            }
        });


        taskActivityBtn = (Button) findViewById(R.id.btnTask);
        taskActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });

    }
}