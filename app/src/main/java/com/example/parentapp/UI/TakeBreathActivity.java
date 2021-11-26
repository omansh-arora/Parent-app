package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TakeBreathActivity extends AppCompatActivity {

    // State Pattern's base states
    private abstract class State {
        // Empty implementations, so derived class don't need to
        // override methods they don't care about.
        void handleEnter() {}
        void handleExit() {}
        void handleClickOn() {}
        void handleClickOff() {}
    }

    public final State onState = new OnState();
    public final State offState = new OffState();
    private State currentState = new IdleState();

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private TextView takeBreathImgBtn, breathsNumTV;
    private FloatingActionButton fabEditBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        loadBreathsConfigBtn();
        loadBreathsTitle();
        registerTakeBreathEvent();


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadBreathsTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBreathsTitle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerTakeBreathEvent() {
        //Button Press effect and button images taking from: https://www.youtube.com/watch?v=ApfqiinI3c4&ab_channel=androidCODE
        //on touch even Listener code: https://www.geeksforgeeks.org/add-ontouchlistener-to-imageview-to-perform-speech-to-text-in-android/
        takeBreathImgBtn = (TextView) findViewById(R.id.breathBtn);
        takeBreathImgBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MediaPlayer mediaPlayer = MediaPlayer.create(TakeBreathActivity.this, R.raw.mixkit_serene_view);

                //Toast.makeText(TakeBreathActivity.this, "I was touched", Toast.LENGTH_SHORT).show();
                switch (motionEvent.getAction()) {

                    // ACTION_UP: when user release finger
                    case MotionEvent.ACTION_UP:
                        takeBreathImgBtn.setBackgroundResource(R.drawable.off);
                        takeBreathImgBtn.setText("Released");
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
                        break;
                    // ACTION_DOWN: when user hold/press the image button
                    case MotionEvent.ACTION_DOWN:
                        takeBreathImgBtn.setBackgroundResource(R.drawable.on);
                        takeBreathImgBtn.setText("In");
                        //mediaPlayer.start();
                        break;
                }
                return true;
            }
        });
    }

    private void loadBreathsTitle() {
        breathsNumTV = (TextView) findViewById(R.id.breathsNumTV);
        //get saved Breaths N Size from localStorage
        if(LocalStorage.getInstance().getBreaths() != null){
            String breathsNum = LocalStorage.getInstance().getBreaths();
            //Toast.makeText(TakeBreathActivity.this, "breathsNum: " + breathsNum, Toast.LENGTH_SHORT).show();
            breathsNumTV.setText("Let's take " +breathsNum+ " breaths together");
        }
    }

    private void loadBreathsConfigBtn() {
        fabEditBreaths = (FloatingActionButton) findViewById(R.id.fabEditBreaths);
        fabEditBreaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //open Breaths selection in popup
                Intent intent = new Intent(getApplicationContext(), BreathsConfigActivity.class);
                //intent.putExtra("task_name", taskName);
                startActivity(intent);
            }
        });
    }

    // ************************************************************
    // State Pattern states
    // ************************************************************
    private class OffState extends State {
        private int count = 0;
        @Override
        void handleEnter() {
            TextView tv = findViewById(R.id.txtMessage);
            tv.setText("OFF OFF OFF OFF!");
        }

        @Override
        void handleExit() {
            Log.i("OffState", "Just exited off state");
        }

        @Override
        void handleClickOn() {
            count++;
            Toast.makeText(TakeBreathActivity.this, "From off state, clicked ON: " + count, Toast.LENGTH_SHORT)
                    .show();
            setState(onState);
        }

        @Override
        void handleClickOff() {
            Toast.makeText(TakeBreathActivity.this, "From off state, clicked off", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class OnState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(offState);
//        Runnable timerRunnable = new Runnable() {
//            @Override
//            public void run() {
//                setState(offState);
//            }
//        };

        @Override
        void handleEnter() {
            TextView tv = findViewById(R.id.txtMessage);
            tv.setText("In ON state!");

            timerHandler.postDelayed(timerRunnable, 2000);
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
        }

        @Override
        void handleClickOff() {
            setState(offState);
        }

        @Override
        void handleClickOn() {
            // Reset the timer
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable, 2000);
        }
    }

    // Use "Null Object" pattern: This class, does nothing! It's like a safe null
    private class IdleState extends State {
    }

}