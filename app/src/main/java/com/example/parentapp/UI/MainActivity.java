package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentapp.R;
import com.example.parentapp.ReminderBroadcast;
import com.example.parentapp.model.ChildManager;


public class MainActivity extends AppCompatActivity {

    private Button flipCoinActivityBtn, timerActivityBtn, addChildActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addChildActivityBtn = (Button) findViewById(R.id.btnAddChild);
        flipCoinActivityBtn = (Button) findViewById(R.id.btnFlipCoin);


        addChildActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intentChildrenActivity = new Intent(MainActivity.this, ChildrenActivity.class);
                startActivity(intentChildrenActivity);
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

    }
}