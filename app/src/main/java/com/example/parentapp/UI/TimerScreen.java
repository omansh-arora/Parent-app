package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parentapp.R;

import java.util.Locale;

public class TimerScreen extends AppCompatActivity {
    private static final long START_TIME_MILLIS = 600000;

    private TextView txt_countDown;
    private Button buttonStartPauseResume;
    private Button buttonReset;

    private Button oneMin;
    private Button twoMin;
    private Button threeMin;
    private Button fiveMin;
    private Button tenMin;

    private CountDownTimer countDownTimer;

    private boolean boolTimerRunning;

    private long timeLeftMillis = START_TIME_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_screen);

        txt_countDown = findViewById(R.id.timer_txt_timer);

        buttonStartPauseResume = findViewById(R.id.timer_bt_start_pause_reset);
        buttonReset = findViewById(R.id.timer_bt_reset);

        oneMin = findViewById(R.id.timer_bt_1min);
        twoMin = findViewById(R.id.timer_bt_2min);
        threeMin = findViewById(R.id.timer_bt_3min);
        fiveMin = findViewById(R.id.timer_bt_5min);
        tenMin = findViewById(R.id.timer_bt_10min);

        buttonStartPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(boolTimerRunning) pauseTimer();
                else startTimer();

            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        oneMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(1);
            }
        });
        twoMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(2);
            }
        });
        threeMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(3);
            }
        });
        fiveMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(5);
            }
        });
        tenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(10);
            }
        });

    }

    private void setTime(int i) {

        timeLeftMillis = (long)i*60*1000;
        updateCountDownText();

    }

    private void pauseTimer() {

        countDownTimer.cancel();
        boolTimerRunning =false;
        buttonStartPauseResume.setText("Start");
        buttonReset.setVisibility(View.VISIBLE);

    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisLeftUntilDone) {
                timeLeftMillis = millisLeftUntilDone;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                boolTimerRunning = false;
                buttonStartPauseResume.setText("Start");
                buttonStartPauseResume.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.VISIBLE);

            }
        }.start();

        boolTimerRunning = true;
        buttonStartPauseResume.setText("pause");
        buttonReset.setVisibility(View.INVISIBLE);

    }

    private void updateCountDownText() {

        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis/1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        txt_countDown.setText(timeLeft);


    }

    private void resetTimer() {

        timeLeftMillis = START_TIME_MILLIS;
        updateCountDownText();
        buttonStartPauseResume.setVisibility(View.VISIBLE);

    }
}