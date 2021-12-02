package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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

    private static final long MIN_INHALE_EXHALE_IN_MILLISEC = 3000;
    private static final long MAX_INHALE_IN_MILLISEC = 10000;

    // State Pattern's base states
    private abstract class State {
        // Empty implementations, so derived class don't need to
        // override methods they don't care about.
        void handleEnter() {}
        void handleExit() {}
        void handleClickOn() {}
        void handleClickOff() {}
    }

    public final State inState = new InState(); // Breath In
    public final State outState = new OutState(); // Breath Out
    private State currentState = new IdleState();

    public void setState(State newState) {
        //currentState.handleExit();
        currentState = newState;
        //currentState.handleEnter();
    }

    private TextView takeBreathImgBtn, breathsNumTV, tv, helpTV;
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
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        takeBreathImgBtn = (TextView) findViewById(R.id.breathBtn);
        helpTV =  (TextView) findViewById(R.id.helpTV);

        loadBreathsConfigBtn();
        loadBreathsTitle();
        registerTakeBreathEvent();


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
                    case MotionEvent.ACTION_UP: // ACTION_UP: when user release finger the image button
                        takeBreathImgBtn.setBackgroundResource(R.drawable.off);
                        timeElapsed = motionEvent.getEventTime() - timeElapsed;

                        // clear Instate Time Runners
                        inState.handleExit();

                        if(timeElapsed < MIN_INHALE_EXHALE_IN_MILLISEC){
                            Toast.makeText(TakeBreathActivity.this, "That is too Short! Let's try again", Toast.LENGTH_SHORT).show();
                            outState.handleClickOff();
                        }else {
                            setState(outState);
                            outState.handleEnter();
                        }

                        break;
                    case MotionEvent.ACTION_DOWN: // ACTION_DOWN: when user hold/press the image button
                        timeElapsed = motionEvent.getDownTime();
                        takeBreathImgBtn.setBackgroundResource(R.drawable.on);

                        //clear any running timer handles
                        inState.handleExit();
                        outState.handleExit();

                        setState(inState);
                        currentState.handleEnter();
                        updateRemainingUI();

                        //reset and restart
                        doStopSound();
                        doStartSound();
                        clearAnimations("both");
                        startBreathInAnimation();

                        break;
                }
                return true;
            }

        });
    }

    private void clearAnimations(String action) {
        switch (action){
            case "In":
                animZoomIn.reset();
                break;
            case "Out":
                animZoomOut.reset();
                break;
            case "both":
                animZoomIn.reset();
                animZoomOut.reset();
        }

        breathCircleIV.clearAnimation();
        //breathCircleIV.setImageResource(R.drawable.circle);
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
        breathCircleIV.setImageResource(R.drawable.circle_out);
        breathCircleIV.startAnimation(animZoomOut);
    }

    private void startBreathInAnimation() {
        breathCircleIV.setImageResource(R.drawable.circle);
        // Animation: https://www.tutlane.com/tutorial/android/android-zoom-in-out-animations-with-examples
        breathCircleIV.startAnimation(animZoomIn);
    }

    private void loadBreathsTitle() {

        //get saved Breaths N Size from localStorage
        if (LocalStorage.getInstance().getBreaths() != null) {
            breathsConfig = Integer.parseInt(LocalStorage.getInstance().getBreaths());
            updateRemainingUI();
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

    private void updateRemainingUI() {

        if(currentState.equals(inState) && breathsTaken > 0){
            takeBreathImgBtn.setText("In");
        }

        if(currentState.equals(outState)){
            takeBreathImgBtn.setText("Out");
        }

        helpTV.setText("Hold the button and breath In");

        // once a breath has begun, we wanna disable config N
        fabEditBreaths.setVisibility(breathsTaken > 0 ? View.INVISIBLE : View.VISIBLE);

        if(breathsTaken == 0){
            breathsNumTV.setText("Let's take " + String.valueOf(breathsConfig) + " breaths together");
        }

        if(breathsConfig == breathsTaken){
            //reset back to Begin
            resetToInitialState();
        }

        if(breathsTaken > 0 && breathsTaken < breathsConfig){
            breathsNumTV.setText("Remaining Breaths: " + String.valueOf(breathsConfig - breathsTaken));
        }

    }

    private void resetToInitialState() {
        breathsTaken = 0;
        helpTV.setText("Hold the button and breath In");
        fabEditBreaths.setVisibility(View.VISIBLE);
        breathsNumTV.setText("Let's take " + String.valueOf(breathsConfig) + " breaths together");
        takeBreathImgBtn.setText("Begin");
    }

    // ************************************************************
    // State Pattern states
    // ************************************************************
    private class OutState extends State {
        private int count = 0;
        Handler doneExhaleTimerHandler = new Handler();
        Runnable doneExhaleTimerRunnable = new Runnable() {
            @Override
            public void run() {
                setState(inState); // set back to inState
                outState.handleExit(); // stop Animation and sound
                updateRemainingUI();

            }
        };

        Handler updateCounterTimerHandler = new Handler();
        Runnable updateCounterTimerRunnable = new Runnable() {
            @Override
            public void run() {
                //after 3s,update button text "Great Job" or "In", update remaining breaths
                takeBreathImgBtn.setText(breathsTaken == breathsConfig ? "Great Job" : "In");
                if(breathsConfig == breathsTaken){
                    breathsNumTV.setText("Great Job! All Breaths Completed! ");
                }

                if(breathsTaken > 0 && breathsTaken < breathsConfig){
                    breathsNumTV.setText("Remaining Breaths: " + String.valueOf(breathsConfig - breathsTaken));
                }

                //after 10s(which is 7s at this point)
                doneExhaleTimerHandler.postDelayed(doneExhaleTimerRunnable, MAX_INHALE_IN_MILLISEC - MIN_INHALE_EXHALE_IN_MILLISEC);
            }
        };

        @Override
        void handleEnter() {
            //tv.setText("Out!");

            takeBreathImgBtn.setText("Out");
            helpTV.setText("Now breath out..");

            clearAnimations("both");
            doStopSound();
            doStartSound();
            startBreathOutAnimation();

            // update count....
            breathsTaken++;

            // after 3 s, update count....
            updateCounterTimerHandler.postDelayed(updateCounterTimerRunnable, MIN_INHALE_EXHALE_IN_MILLISEC);
        }

        @Override
        void handleExit() {
            clearAnimations("both");
            doStopSound();
            updateCounterTimerHandler.removeCallbacks(updateCounterTimerRunnable);
            doneExhaleTimerHandler.removeCallbacks(doneExhaleTimerRunnable);
            updateRemainingUI();
        }

        @Override
        void handleClickOn() {
            setState(inState);
        }

        @Override
        void handleClickOff() {
            this.handleExit();

            // rollback to inState
            setState(inState);
            takeBreathImgBtn.setText("In");
        }
    }

    private class InState extends State {

        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                // don't change state until button released
                helpTV.setText("Release button and breath out");
                Toast.makeText(TakeBreathActivity.this, "held for 10s, now release button and breath out", Toast.LENGTH_SHORT).show();
                doStopSound();
            }
        };

        Handler updateBtnTimerHandler = new Handler();
        Runnable updateBtnTimerRunnable = new Runnable() {
            @Override
            public void run() {
                takeBreathImgBtn.setText("Out");
                Toast.makeText(TakeBreathActivity.this, "Nice, Inhaled for 3s", Toast.LENGTH_SHORT).show();
            }
        };

        @Override
        void handleEnter() {

            //tv.setText("handleEnter: In ON state!");
            takeBreathImgBtn.setText("In");
            helpTV.setText("Hold the button and breath In");

            //user held the button for at least 3 seconds continuously, change button message to "Out"
            updateBtnTimerHandler.postDelayed(updateBtnTimerRunnable, MIN_INHALE_EXHALE_IN_MILLISEC);

            //inhaled for 10s, update text and stop animation and sound (but don't change state until button released)
            timerHandler.postDelayed(timerRunnable, MAX_INHALE_IN_MILLISEC);

        }

        @Override
        void handleExit() {
            clearAnimations("both");
            doStopSound();
            updateBtnTimerHandler.removeCallbacks(updateBtnTimerRunnable);
            timerHandler.removeCallbacks(timerRunnable);
        }

        @Override
        void handleClickOff() {
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