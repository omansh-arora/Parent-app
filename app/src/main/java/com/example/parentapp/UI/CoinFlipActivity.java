package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parentapp.R;


import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {

    public static final Random ranNum = new Random();
    private ImageView coin;
    private TextView tossResultTv, childTurnTv;
    private  RadioGroup coinRBsGroup;
    private Switch tossModeSW;
    int tossResultNum;
    String tossResultText;
    private MediaPlayer mediaPlayer;

    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    int numSoundsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        coin = (ImageView) findViewById(R.id.coinImgView);
        tossResultTv = (TextView) findViewById(R.id.tossResultTv);
        tossModeSW = (Switch) findViewById(R.id.tossModeSW);


        //hide child custom pick section
        childTurnTv = (TextView) findViewById(R.id.childTurnTv);
        coinRBsGroup = (RadioGroup) findViewById(R.id.coinRBsGroup);
        childTurnTv.setVisibility(View.INVISIBLE);
        coinRBsGroup.setVisibility(View.INVISIBLE);

        // initialize sounds
        initSounds();


        // registers click Listeners
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipTheCoin();
            }
        });

        //Set a CheckedChange Listener for Switch Button
        tossModeSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked
                    childTurnTv.setVisibility(View.VISIBLE);
                    coinRBsGroup.setVisibility(View.VISIBLE);
                }
                else
                {
                    //Do something when Switch is off/unchecked
                    childTurnTv.setVisibility(View.INVISIBLE);
                    coinRBsGroup.setVisibility(View.INVISIBLE);
                    coinRBsGroup.clearCheck();
                }
            }
        });


    }

    private void initSounds() {
        // sound downloaded from: https://www.soundjay.com/coin-sounds-1.html
        mediaPlayer = MediaPlayer.create(this, R.raw.coindrop);
    }

    private void flipTheCoin() {

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF
                , 0);
        rotateAnimation.setDuration(2500);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mediaPlayer.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                tossResultNum = ranNum.nextInt(2);
                //Toast.makeText(CoinFlipActivity.this, String.valueOf(tossResultNum), Toast.LENGTH_SHORT).show();
                if (tossResultNum == 0) {
                    tossResultText = "Head";
                } else {
                    tossResultText = "Tail";
                }

                coin.setImageResource(tossResultNum > 0 ? R.drawable.tails2 : R.drawable.heads2);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(2000);
                fadeIn.setFillAfter(true);
                coin.startAnimation(fadeIn);
                tossResultTv.setText(tossResultText);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        coin.startAnimation(rotateAnimation);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

}