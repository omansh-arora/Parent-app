package com.example.parentapp.UI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parentapp.R;
import com.example.parentapp.ReminderBroadcast;

import java.util.Locale;

public class TimerScreen extends AppCompatActivity {

    //Initialize variables
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

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_screen);

        //See if alarm is picked from notification
        Intent intent = getIntent();
        int op = intent.getIntExtra("Type", 0);

        if (op == 1) cancelAlarm();

        //Creates notification channel//initialize variables
        createNotificationChannel();

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

        //Set onclick listeners
        buttonStartPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boolTimerRunning) {
                    pauseTimer();
                    cancelAlarm();
                } else {
                    startTimer();
                }

            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                cancelAlarm();
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
                if (editSetTime.getText().toString().equals("")) {

                    Toast.makeText(TimerScreen.this,
                            "Please enter a number(in minutes)", Toast.LENGTH_LONG).show();
                    return;
                }
                String minsStr = editSetTime.getText().toString();
                int mins = Integer.parseInt(minsStr);
                setTime(mins);
            }
        });


    }

    //Set custom time
    private void setTime(int i) {
        START_TIME_MILLIS = (long) i * 60 * 1000;
        timeLeftMillis = START_TIME_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void pauseTimer() {

        countDownTimer.cancel();
        boolTimerRunning = false;
        updateButtons();

    }

    private void startTimer() {

        endTime = System.currentTimeMillis() + timeLeftMillis;
        setAlarm(endTime);

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
                cancelAlarm();
            }
        }.start();

        boolTimerRunning = true;
        buttonStartPauseResume.setText("pause");
        updateButtons();

    }

    private void setAlarm(long millis) {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ReminderBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.set(AlarmManager.RTC, millis, pendingIntent);
        Toast.makeText(this, "Timer set", Toast.LENGTH_SHORT).show();

    }

    private void cancelAlarm() {

        Intent intent = new Intent(this, ReminderBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);


    }

    private void updateCountDownText() {

        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        txt_countDown.setText(timeLeft);


    }

    private void resetTimer() {

        timeLeftMillis = START_TIME_MILLIS;
        updateCountDownText();
        updateButtons();
        cancelAlarm();

    }

    private void updateButtons() {
        if (boolTimerRunning) {
            buttonReset.setVisibility(View.INVISIBLE);
            buttonStartPauseResume.setText("Pause");
            buttonTimeSet.setVisibility(View.INVISIBLE);
            editSetTime.setVisibility(View.INVISIBLE);
            TXT_editSetTime.setVisibility(View.INVISIBLE);
            oneMin.setVisibility(View.INVISIBLE);
            twoMin.setVisibility(View.INVISIBLE);
            threeMin.setVisibility(View.INVISIBLE);
            fiveMin.setVisibility(View.INVISIBLE);
            tenMin.setVisibility(View.INVISIBLE);
        } else {
            buttonStartPauseResume.setText("Start");
            buttonStartPauseResume.setVisibility(View.INVISIBLE);


            if (timeLeftMillis < 1000) {


            } else {
                buttonStartPauseResume.setVisibility(View.VISIBLE);
            }

            if (timeLeftMillis < START_TIME_MILLIS) {
                buttonReset.setVisibility(View.VISIBLE);
            } else {
                buttonReset.setVisibility(View.INVISIBLE);
            }
            if (timeLeftMillis == START_TIME_MILLIS) {
                oneMin.setVisibility(View.VISIBLE);
                twoMin.setVisibility(View.VISIBLE);
                threeMin.setVisibility(View.VISIBLE);
                fiveMin.setVisibility(View.VISIBLE);
                tenMin.setVisibility(View.VISIBLE);
                buttonTimeSet.setVisibility(View.VISIBLE);
                editSetTime.setVisibility(View.VISIBLE);
                TXT_editSetTime.setVisibility(View.VISIBLE);
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

        timeLeftMillis = prefs.getLong("millisLeft", timeLeftMillis);
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

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Timer noti";
            String description = "Channel for noti";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("timer", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }
}