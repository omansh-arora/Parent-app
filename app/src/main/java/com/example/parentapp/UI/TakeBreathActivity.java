package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TakeBreathActivity extends AppCompatActivity {

    private static final long MIN_INHALE_IN_MILLISEC = 3000;
    private static final long MAX_INHALE_IN_MILLISEC = 10000;

    // State Pattern's base states
    private abstract class State {
        // Empty implementations, so derived class don't need to
        // override methods they don't care about.
        void handleEnter() {

        }

        void handleExit() {

        }

        void handleClickOn() {

        }

        void handleClickOff() {

        }
    }

    public final State inState = new InState(); // Breath In
    public final State outState = new OutState(); // Breath Out
    private State currentState = new IdleState();

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private TextView takeBreathImgBtn, breathsNumTV, tv, stateTV;
    private ImageView breathCircleIV;
    private FloatingActionButton fabEditBreaths;
    private MediaPlayer mediaPlayer;
    private long timeElapsed = 0L;
    private int breathsConfig;
    private int breathsTaken = 0;
    private Animation animZoomOut, animZoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        //init UI components
        breathsNumTV = (TextView) findViewById(R.id.breathsNumTV);
        breathCircleIV = (ImageView) findViewById(R.id.breathCircleIV);
        tv = (TextView) findViewById(R.id.txtMessage);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        takeBreathImgBtn = (TextView) findViewById(R.id.breathBtn);
        stateTV =  (TextView) findViewById(R.id.stateTV);

        loadBreathsConfigBtn();
        loadBreathsTitle();
        registerTakeBreathEvent();

        // set initial state
        //setState(inState); //default is breath in state, unless btn hold for at least 3 sec

        if (LocalStorage.getInstance().getBreaths() != null) {
            breathsConfig = Integer.parseInt(LocalStorage.getInstance().getBreaths());
        }

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

    private void loadMediaPlayer() {
        if (currentState.equals(inState)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.mixkit_my_little_star_1037);
        } else if (currentState.equals(outState)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.mixkit_discover_587);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerTakeBreathEvent() {

        //Button Press effect and button images taking from: https://www.youtube.com/watch?v=ApfqiinI3c4&ab_channel=androidCODE
        //on touch even Listener code: https://www.geeksforgeeks.org/add-ontouchlistener-to-imageview-to-perform-speech-to-text-in-android/
        // TimeElapse code: https://stackoverflow.com/questions/4410362/how-to-detect-the-period-for-which-a-button-is-press-in-android
        takeBreathImgBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP: // ACTION_UP: when user release finger
                        takeBreathImgBtn.setBackgroundResource(R.drawable.off);
                        timeElapsed = motionEvent.getEventTime() - timeElapsed;

                        // FOR TESTING, REMOVE B4 submit
                        if(currentState.equals(inState)){
                            stateTV.setText("Current State: inState ");
                        }

                        if(currentState.equals(outState)){
                            stateTV.setText("Current State: outState ");
                        }
                        // FOR TESTING, REMOVE B4 submit

                        if(currentState.equals(inState) && timeElapsed >= MIN_INHALE_IN_MILLISEC){
                            setState(outState);
                            break;
                        }

                        if(currentState.equals(inState) && timeElapsed < MIN_INHALE_IN_MILLISEC){
                            inState.handleExit();
                            setState(outState);
                        }

                        if(timeElapsed < MAX_INHALE_IN_MILLISEC){
                            Toast.makeText(TakeBreathActivity.this, "Hold: " + timeElapsed + " milliSecond", Toast.LENGTH_SHORT).show();
                            doStopSound();
                            clearAnimations();
                        }

                        break;
                    case MotionEvent.ACTION_DOWN: // ACTION_DOWN: when user hold/press the image button
                        timeElapsed = motionEvent.getDownTime();
                        takeBreathImgBtn.setBackgroundResource(R.drawable.on);
                        setState(inState);
//                        doStartSound();
//                        startBreathInAnimation();
                        break;
                }
                return true;
            }

        });
    }

    private void clearAnimations() {
        //Toast.makeText(TakeBreathActivity.this, "clear Animation,Button only hold for " + timeElapsed + " Second", Toast.LENGTH_SHORT).show();
        animZoomIn.reset();
        animZoomOut.reset();
        breathCircleIV.clearAnimation();
        breathCircleIV.setImageResource(R.drawable.circle);
    }

    private void doStopSound() {
        if(mediaPlayer == null){
            return;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

    }

    private void doStartSound() {
        loadMediaPlayer();
        if (mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.start();
    }

    private void startBreathOutAnimation() {
        breathCircleIV.startAnimation(animZoomOut);
    }

    private void startBreathInAnimation() {
        // Animation: https://www.tutlane.com/tutorial/android/android-zoom-in-out-animations-with-examples
        breathCircleIV.startAnimation(animZoomIn);
    }

    private void loadBreathsTitle() {

        //get saved Breaths N Size from localStorage
        if (LocalStorage.getInstance().getBreaths() != null) {
            breathsConfig = Integer.parseInt(LocalStorage.getInstance().getBreaths());
            //Toast.makeText(TakeBreathActivity.this, "breathsNum: " + breathsNum, Toast.LENGTH_SHORT).show();
            breathsNumTV.setText("Let's take " + String.valueOf(breathsConfig) + " breaths together");
        }
    }

    private void loadBreathsConfigBtn() {
        fabEditBreaths = (FloatingActionButton) findViewById(R.id.fabEditBreaths);
        fabEditBreaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open Breaths selection in popup
                Intent intent = new Intent(getApplicationContext(), BreathsConfigActivity.class);
                startActivity(intent);
            }
        });
    }

    // ************************************************************
    // State Pattern states
    // ************************************************************
    private class OutState extends State {
        private int count = 0;
        Handler timerHandler = new Handler();
        //Runnable timerRunnable = () -> setState(inState);
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                clearAnimations();
                doStopSound();
            }
        };

        @Override
        void handleEnter() {
            tv.setText("Out!");
            takeBreathImgBtn.setText("Out");
            startBreathOutAnimation();
            //stop any playing sound:
            doStopSound();

            //load and start breath out sound:
            doStartSound();

            timerHandler.postDelayed(timerRunnable, MAX_INHALE_IN_MILLISEC);
//            if(timeElapsed < MIN_INHALE_IN_MILLISEC){
//                clearAnimations();
//            }

        }

        @Override
        void handleExit() {
            clearAnimations();
            doStopSound();
            Toast.makeText(TakeBreathActivity.this, "OutState: Just exited off state", Toast.LENGTH_SHORT).show();
        }

        @Override
        void handleClickOn() {
            count++;
            Toast.makeText(TakeBreathActivity.this, "From out state, clicked ON: " + count, Toast.LENGTH_SHORT)
                    .show();
            setState(inState);
        }

        @Override
        void handleClickOff() {
            Toast.makeText(TakeBreathActivity.this, "From OUT state, clicked OUT", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class InState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(outState);

        @Override
        void handleEnter() {
            tv.setText("In ON state!");
            takeBreathImgBtn.setText("In");
            doStopSound();
            doStartSound();
            clearAnimations();
            startBreathInAnimation();

            //inhaled for 10s, change state to "out"
            timerHandler.postDelayed(timerRunnable, MAX_INHALE_IN_MILLISEC);
        }

        @Override
        void handleExit() {
            Toast.makeText(TakeBreathActivity.this, "INT state, handleExit", Toast.LENGTH_SHORT)
                    .show();
            clearAnimations();
            doStopSound();
            timerHandler.removeCallbacks(timerRunnable);
        }

        @Override
        void handleClickOff() {
            setState(outState);
        }

        @Override
        void handleClickOn() {
            // Reset the timer
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable, MAX_INHALE_IN_MILLISEC);
        }
    }

    // Use "Null Object" pattern: This class, does nothing! It's like a safe null
    private class IdleState extends State {
    }

}