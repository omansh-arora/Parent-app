package com.example.parentapp.UI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    private long timeMillisUnmultiplied = START_TIME_MILLIS;
    private double multiplier = 1;
    private int OGminutes;


    ProgressBar progressBar;


    private boolean speedChanged = false;

    private long endTime;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private TextView TXTspeed;

    double millisCounted = 0;

    int i = 0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TXTspeed = findViewById(R.id.timer_txt_speed);

        switch (item.getItemId()) {
            case R.id.tr_speed_25:
                if(multiplier!=4) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = 4;
                TXTspeed.setText("Speed@25%");
                return true;
            case R.id.tr_speed_50:
                if(multiplier!=2) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = 2;
                TXTspeed.setText("Speed@50%");
                return true;
            case R.id.tr_speed_75:
                if(multiplier!=1.33) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = 1.33;
                TXTspeed.setText("Speed@75%");
                return true;
            case R.id.tr_speed_100:
                if(multiplier!=1) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = 1;
                TXTspeed.setText("Speed@100%");
                return true;
            case R.id.tr_speed_200:
                if(multiplier!=.5) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = .5;
                TXTspeed.setText("Speed@200%");
                return true;
            case R.id.tr_speed_300:
                if(multiplier!=.33) speedChanged = boolTimerRunning;
                else speedChanged = false;
                multiplier = .33;
                TXTspeed.setText("Speed@300%");
                return true;
            case R.id.tr_speed_400:
                if(multiplier!=.25) speedChanged = boolTimerRunning;
                else speedChanged = false;
                TXTspeed.setText("Speed@400%");
                multiplier = .25;
                return true;
            default:
                multiplier = 1;
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_screen);

        SharedPreferences preferences = getSharedPreferences("prefs", 0);
        preferences.edit().remove("speed").commit();

        TXTspeed = findViewById(R.id.timer_txt_speed);

        TXTspeed.setText("Speed@" + (int)(100/multiplier) +"%");

        progressBar = findViewById(R.id.timer_progressBar);

        multiplier = 1;

        updateButtons();

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
        START_TIME_MILLIS = ((long) i * 60 * 1000);
        timeMillisUnmultiplied = ((long) i * 60 * 1000);
        timeLeftMillis = (long) (START_TIME_MILLIS*multiplier);
        updateCountDownText();
        updateButtons();
        OGminutes = i;
    }

    private void pauseTimer() {

        countDownTimer.cancel();
        boolTimerRunning = false;
        updateButtons();

    }

    private void startTimer() {


        timeLeftMillis = (long) (timeMillisUnmultiplied*multiplier);
        endTime = System.currentTimeMillis() + timeLeftMillis;
        setAlarm(endTime);
        double milliBar = timeLeftMillis / (double)(100-progressBar.getProgress());

        countDownTimer = new CountDownTimer(timeLeftMillis, (int)(1000*multiplier)) {
            @Override
            public void onTick(long millisLeftUntilDone) {
                if(speedChanged){
                    speedChanged = false;
                    cancelAlarm();
                    countDownTimer.cancel();
                    startTimer();
                }

                timeLeftMillis = millisLeftUntilDone;
                timeMillisUnmultiplied = (long) (timeLeftMillis/multiplier);

                millisCounted+= 1000 *multiplier;

                if(millisCounted>=milliBar) {
                    progressBar.setProgress(progressBar.getProgress() + 1);
                    millisCounted = 0;
                }

                updateCountDownText();
                updateButtons();

            }

            @Override
            public void onFinish() {
                boolTimerRunning = false;
                progressBar.setProgress(0);
                updateButtons();
                cancelAlarm();
                timeLeftMillis = START_TIME_MILLIS;
                timeMillisUnmultiplied = ((long) OGminutes * 60 * 1000);

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

            int minutes = (int) (timeMillisUnmultiplied / 1000) / 60;
            int seconds = (int) (timeMillisUnmultiplied / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        txt_countDown.setText(timeLeft);


    }

    private void resetTimer() {

        timeLeftMillis = START_TIME_MILLIS;
        timeMillisUnmultiplied = START_TIME_MILLIS;
        updateCountDownText();
        updateButtons();
        cancelAlarm();
        progressBar.setProgress(0);

    }

    private void updateButtons() {


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
            buttonStartPauseResume.setVisibility(View.VISIBLE);

            //if(START_TIME_MILLIS == 0 || timeLeftMillis == 0) buttonStartPauseResume.setVisibility(View.INVISIBLE);

            if (timeLeftMillis < 1000) {

                oneMin.setVisibility(View.VISIBLE);
                twoMin.setVisibility(View.VISIBLE);
                threeMin.setVisibility(View.VISIBLE);
                fiveMin.setVisibility(View.VISIBLE);
                tenMin.setVisibility(View.VISIBLE);
                buttonTimeSet.setVisibility(View.VISIBLE);
                editSetTime.setVisibility(View.VISIBLE);
                TXT_editSetTime.setVisibility(View.VISIBLE);
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
        editor.putFloat("speed", (float)multiplier);
        editor.putInt("progress", progressBar.getProgress());

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
        if(boolTimerRunning)
        multiplier = prefs.getFloat("speed",0);
        else multiplier = 1;
        int progress = prefs.getInt("progress",0);
        progressBar.setProgress(progress);

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