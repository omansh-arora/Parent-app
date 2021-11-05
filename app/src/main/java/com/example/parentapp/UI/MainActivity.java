package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentapp.R;

public class MainActivity extends AppCompatActivity {

    private Button flipCoinActivityBtn, timerActivityBtn, addChildActivityBtn, editChildActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addChildActivityBtn = (Button) findViewById(R.id.btnAddChild);
        editChildActivityBtn = (Button) findViewById(R.id.btnEditChild);
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

        editChildActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch history page
                Intent intentEditChildActivity = new Intent(MainActivity.this, ChildrenActivity.class);
                startActivity((intentEditChildActivity));
            }
        });

        //timerActivityBtn = (Button) findViewById(R.id.btnTimer);

    }
}