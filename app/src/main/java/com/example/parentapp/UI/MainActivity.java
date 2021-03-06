package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;


public class MainActivity extends AppCompatActivity {

    private Button flipCoinActivityBtn, timerActivityBtn, addChildActivityBtn, taskActivityBtn, helpBtn, takeBreathBtn;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localStorage = LocalStorage.getInstance(this.getApplicationContext());

        addChildActivityBtn = (Button) findViewById(R.id.btnAddChild);
        flipCoinActivityBtn = (Button) findViewById(R.id.btnFlipCoin);

        helpBtn = findViewById(R.id.btnHelp);

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(help);
            }
        });

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
                if(LocalStorage.getInstance().getChildren().size() == 0){
                    Toast.makeText(MainActivity.this, "Please add a child before playing the game! ", Toast.LENGTH_SHORT).show();
                }else {
                    //launch coinflip page
                    Intent intentFlipCoinActivity = new Intent(MainActivity.this, CoinFlipActivity.class);
                    startActivity(intentFlipCoinActivity);
                }
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
                if(LocalStorage.getInstance().getChildren().size() == 0){
                    Toast.makeText(MainActivity.this, "Please add a child before adding a task! ", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(MainActivity.this, TaskActivity.class));
                }
            }
        });

        takeBreathBtn = (Button) findViewById(R.id.btnTakeBreath);
        takeBreathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TakeBreathActivity.class));
            }
        });

    }
}