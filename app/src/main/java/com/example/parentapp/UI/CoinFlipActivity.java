package com.example.parentapp.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;

import java.util.Random;


public class CoinFlipActivity extends AppCompatActivity {

    public static final Random ranNum = new Random();
    private ImageView coin;
    private TextView tossResultTv, childTurnTv;
    private RadioGroup coinRBsGroup;
    private Switch childPickModeSW;
    private Button tossHistoryBtn;
    int tossResultNum;
    String tossResultText;
    private MediaPlayer mediaPlayer;

    private CoinFlipManager coinFlipManager;
    private ChildManager childManager;
    int childChoice;
    Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        //
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Flip Coin");

        //initialize singleton instance
        coinFlipManager = CoinFlipManager.getInstance();
        childManager = ChildManager.getInstance();

        coin = (ImageView) findViewById(R.id.coinImgView);
        tossResultTv = (TextView) findViewById(R.id.tossResultTv);
        childPickModeSW = (Switch) findViewById(R.id.tossModeSW);
        tossHistoryBtn = (Button) findViewById(R.id.viewTossHistoryBtn);

        //hide child custom pick section
        childTurnTv = (TextView) findViewById(R.id.childTurnTv);
        coinRBsGroup = (RadioGroup) findViewById(R.id.coinRBsGroup);
        childTurnTv.setVisibility(View.INVISIBLE);
        coinRBsGroup.setVisibility(View.INVISIBLE);

        // pick up a child
        child = childManager.getNextChild();
        if (child != null) {
            childTurnTv.setText(child.getName() + "'s turn to pick");
        }

        // initialize sounds
        initSounds();

        // setup child choice
        coinRBsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int choiceID) {
                switch (choiceID) {
                    case R.id.headsRB: childChoice = 0; break;
                    case R.id.tailsRB: childChoice = 1; break;
                    default: break;
                }
            }
        });

        // Registers click Listeners
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipTheCoin();
            }
        });

        //Set a Checked Change Listener for Switch Button
        childPickModeSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    //Do something when Switch button is on/checked
                    if (child == null) {
                        Toast.makeText(CoinFlipActivity.this, "No child added yet.", Toast.LENGTH_SHORT).show();
                        childPickModeSW.setChecked(false);
                    } else {
                        childTurnTv.setVisibility(View.VISIBLE);
                        coinRBsGroup.setVisibility(View.VISIBLE);
                    }
                } else {
                    //Do something when Switch is off/unchecked
                    childTurnTv.setVisibility(View.INVISIBLE);
                    coinRBsGroup.setVisibility(View.INVISIBLE);
                    coinRBsGroup.clearCheck();
                }
            }
        });

        tossHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intentHistory = new Intent(CoinFlipActivity.this, TossHistoryActivity.class);
                startActivity(intentHistory);
            }
        });
    }

    private void initSounds() {
        // sound downloaded from: https://www.soundjay.com/coin-sounds-1.html
        mediaPlayer = MediaPlayer.create(this, R.raw.coindrop);
    }

    private void flipTheCoin() {

        //when Switch widget is on
        // check current state of a Switch (true or false).
        Boolean isChildPickMode = childPickModeSW.isChecked();

        if (isChildPickMode && child != null && coinRBsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(CoinFlipActivity.this, "Please select the side.", Toast.LENGTH_SHORT).show();
            return;
        }

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF
                , 0.5f);
        rotateAnimation.setDuration(2500);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mediaPlayer.start();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
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

                // add result to records
                if(isChildPickMode && child != null) {
                    assert child != null;
                    CoinFlip flip = new CoinFlip(child.getName(), childChoice, tossResultNum);
                    coinFlipManager.addFlipGame(flip);
                }

                child = childManager.getNextChild();
                if (child == null) {
                    Toast.makeText(CoinFlipActivity.this, "No child added yet.", Toast.LENGTH_SHORT).show();
                    childPickModeSW.setChecked(false);
                } else {
                    childTurnTv.setText(child.getName() + "'s turn to pick");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        coin.startAnimation(rotateAnimation);

    }


}