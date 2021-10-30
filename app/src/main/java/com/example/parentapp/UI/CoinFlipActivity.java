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
import android.widget.ImageView;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

import com.example.parentapp.R;


import java.util.Random;

public class CoinFlipActivity extends AppCompatActivity {

    public static final Random ranNum = new Random();
    private ImageView coin;
    private TextView tossResultTv;
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
        tossResultTv = findViewById(R.id.tossResultTv);

        // initialize sounds
        initSounds();

        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipTheCoin();
            }
        });

    }

    private void initSounds() {
        // sound downloaded from: https://www.soundjay.com/coin-sounds-1.html
        mediaPlayer = MediaPlayer.create(this, R.raw.coindrop);

    }

    private void flipTheCoin() {

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                0, RotateAnimation.RELATIVE_TO_SELF
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