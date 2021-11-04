package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.parentapp.R;

import java.util.Locale;

public class TimerScreen extends AppCompatActivity {
    private static long START_TIME_MILLIS = 600000;

    private TextView txt_countDown;
    private Button buttonStartPauseResume;
    private Button buttonReset;

    private Button oneMin;
    private Button twoMin;
    private Button threeMin;
    private Button fiveMin;
    private Button tenMin;

    private Button buttonTimeSet;
    private EditText editSetTime;
    private TextView TXT_editSetTime;

    private CountDownTimer countDownTimer;

    private boolean boolTimerRunning;

    private long timeLeftMillis = START_TIME_MILLIS;

    private long endTime;

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

        buttonTimeSet = findViewById(R.id.timer_btn_setTime);
        editSetTime = findViewById(R.id.timer_inp_timeInp);
        TXT_editSetTime = findViewById(R.id.timer_txt_timeEnter);

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

        buttonTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minsStr = editSetTime.getText().toString();
                int mins = Integer.parseInt(minsStr);
                setTime(mins);
            }
        });



    }

    private void setTime(int i) {
        START_TIME_MILLIS = (long)i*60*1000;
        timeLeftMillis = (long)i*60*1000;
        updateCountDownText();
        updateButtons();
    }

    private void pauseTimer() {

        countDownTimer.cancel();
        boolTimerRunning =false;
        updateButtons();

    }

    private void startTimer() {

        endTime = System.currentTimeMillis() + timeLeftMillis;

        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisLeftUntilDone) {
                timeLeftMillis = millisLeftUntilDone;
                updateCountDownText();
                updateButtons();
            }

            @Override
            public void onFinish() {
                boolTimerRunning = false;
                updateButtons();

            }
        }.start();

        boolTimerRunning = true;
        buttonStartPauseResume.setText("pause");
        updateButtons();

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
        updateButtons();

    }

    private void updateButtons() {
        if (boolTimerRunning) {
            buttonReset.setVisibility(View.INVISIBLE);
            buttonStartPauseResume.setText("Pause");
            buttonTimeSet.setVisibility(View.INVISIBLE);
            editSetTime.setVisibility(View.INVISIBLE);
            TXT_editSetTime.setVisibility(View.INVISIBLE);
        } else {
            buttonStartPauseResume.setText("Start");
            buttonStartPauseResume.setVisibility(View.INVISIBLE);
            buttonTimeSet.setVisibility(View.VISIBLE);
            editSetTime.setVisibility(View.VISIBLE);
            TXT_editSetTime.setVisibility(View.VISIBLE);

            if (timeLeftMillis < 1000) {

            } else {
                buttonStartPauseResume.setVisibility(View.VISIBLE);
            }

            if (timeLeftMillis < START_TIME_MILLIS) {
                buttonReset.setVisibility(View.VISIBLE);
            } else {
                buttonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {

        SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", timeLeftMillis);
        editor.putBoolean("timerRunning", boolTimerRunning);
        editor.putLong("endTime", endTime);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onStop();

    }

    @Override
    protected void onStart() {


        SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);

        timeLeftMillis = prefs.getLong("millisLeft",timeLeftMillis);
        boolTimerRunning = prefs.getBoolean("timerRunning", false);


        updateCountDownText();
        updateButtons();

        if (boolTimerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftMillis = endTime - System.currentTimeMillis();

            if (timeLeftMillis < 0) {
                timeLeftMillis = 0;
                boolTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
        super.onStart();

    }
}