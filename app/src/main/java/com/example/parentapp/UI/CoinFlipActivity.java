package com.example.parentapp.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.example.parentapp.model.ChildManager;
import com.example.parentapp.model.ChildrenQueue;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipHistory;
import com.example.parentapp.model.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;


public class CoinFlipActivity extends AppCompatActivity {

    public static final Random ranNum = new Random();
    private TextView tossResultTv, childTurnTv, currentTaskTv;
    private ImageView coin, selectedChildImgView;
    private RadioGroup coinRBsGroup;
    private Switch childPickModeSW;
    private Button tossHistoryBtn;
    int tossResultNum;
    String tossResultText;
    private MediaPlayer mediaPlayer;

    private CoinFlipHistory coinFlipHistory;
    private ChildManager childManager;
    private ChildrenQueue childrenQueue;
    String baseIMAGE;

    int childChoice;
    String taskName = TaskManager.DEFAULT_TASK.getName();

    private FloatingActionButton overrideChildFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Flip Coin");

        Resources resources = this.getResources();
        baseIMAGE = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        // get current task
        Intent intent = getIntent();
        taskName = intent.getStringExtra("task_name");
        if (taskName == null) {
            taskName = TaskManager.DEFAULT_TASK.getName();
        }
        currentTaskTv = (TextView) findViewById(R.id.currentTaskTv);
        currentTaskTv.setText("Task: " + (taskName.length() > 10 ? taskName.substring(0, 10) + "..." : taskName));


        // load override current child floating button
        overrideChildFab = (FloatingActionButton) findViewById(R.id.fabOverrideChild);
        overrideChildFab.setVisibility(View.INVISIBLE);
        overrideChildFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start intent (open popup)
                Intent intent = new Intent(getApplicationContext(), ChildQueueActivity.class);
                intent.putExtra("task_name", taskName);
                startActivity(intent);
            }
        });

        //initialize singleton instance//get shared preferences
        coinFlipHistory = new CoinFlipHistory(taskName);
        childManager = new ChildManager();

        coin = (ImageView) findViewById(R.id.coinImgView);
        tossResultTv = (TextView) findViewById(R.id.tossResultTv);
        childPickModeSW = (Switch) findViewById(R.id.tossModeSW);
        tossHistoryBtn = (Button) findViewById(R.id.viewTossHistoryBtn);

        //hide child custom pick section
        childTurnTv = (TextView) findViewById(R.id.childTurnTv);
        coinRBsGroup = (RadioGroup) findViewById(R.id.coinRBsGroup);
        selectedChildImgView = (ImageView) findViewById(R.id.selectedChildImgView);

        childTurnTv.setVisibility(View.INVISIBLE);
        coinRBsGroup.setVisibility(View.INVISIBLE);
        selectedChildImgView.setVisibility(View.INVISIBLE);

        // pick up a child
        childrenQueue = new ChildrenQueue(taskName);

        // Toast.makeText(CoinFlipActivity.this, "from onCreate() ", Toast.LENGTH_SHORT).show();
        //Toast.makeText(CoinFlipActivity.this, "Current Turn:" + childrenQueue.getSelectedChild().getName(), Toast.LENGTH_SHORT).show();

        if (childrenQueue.getSelectedChild() != childManager.DEFAULT_CHILD) {
            childTurnTv.setText(childrenQueue.getSelectedChild().getName() + "'s turn to pick");
        }

        // initialize sounds
        initSounds();

        // setup child choice
        coinRBsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int choiceID) {
                switch (choiceID) {
                    case R.id.headsRB:
                        childChoice = 0;
                        break;
                    case R.id.tailsRB:
                        childChoice = 1;
                        break;
                    default:
                        break;
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
                    if (childrenQueue.getSelectedChild() == childManager.DEFAULT_CHILD) {
                        if (childrenQueue.getNextChild() == childManager.DEFAULT_CHILD) {
                            Toast.makeText(CoinFlipActivity.this, "No child added yet.", Toast.LENGTH_SHORT).show();
                            childPickModeSW.setChecked(false);
                            return;
                        }
                    }
                    childTurnTv.setText(childrenQueue.getSelectedChild().getName() + "'s turn to pick");
                    childTurnTv.setVisibility(View.VISIBLE);
                    coinRBsGroup.setVisibility(View.VISIBLE);

                    // set selected Child picture
                    Uri imgPFP = childrenQueue.getSelectedChild().getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(childrenQueue.getSelectedChild().getPicture());
                    selectedChildImgView.setImageURI(imgPFP);
                    selectedChildImgView.setVisibility(View.VISIBLE);


                    overrideChildFab.setVisibility(View.VISIBLE);
                } else {
                    //Do something when Switch is off/unchecked
                    childrenQueue.cleanSelection();
                    childTurnTv.setVisibility(View.INVISIBLE);
                    coinRBsGroup.setVisibility(View.INVISIBLE);
                    coinRBsGroup.clearCheck();
                    selectedChildImgView.setVisibility(View.INVISIBLE);
                    overrideChildFab.setVisibility(View.INVISIBLE);
                }
            }
        });

        tossHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch history page
                Intent intent = new Intent(getApplicationContext(), TossHistoryActivity.class);
                intent.putExtra("task_name", taskName);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        childrenQueue = new ChildrenQueue(taskName);
        coinFlipHistory = new CoinFlipHistory(taskName);

        if (childrenQueue.getSelectedChild() != ChildManager.DEFAULT_CHILD) {
            childTurnTv.setText(childrenQueue.getSelectedChild().getName() + "'s turn to pick");
            Uri imgPFP = childrenQueue.getSelectedChild().getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(childrenQueue.getSelectedChild().getPicture());
            selectedChildImgView.setImageURI(imgPFP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        childrenQueue = new ChildrenQueue(taskName);
        coinFlipHistory = new CoinFlipHistory(taskName);
        if (childrenQueue.getSelectedChild() != ChildManager.DEFAULT_CHILD) {
            childTurnTv.setText(childrenQueue.getSelectedChild().getName() + "'s turn to pick");
            Uri imgPFP = childrenQueue.getSelectedChild().getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(childrenQueue.getSelectedChild().getPicture());
            selectedChildImgView.setImageURI(imgPFP);
        }

    }

    private void initSounds() {
        // sound downloaded from: https://www.soundjay.com/coin-sounds-1.html
        mediaPlayer = MediaPlayer.create(this, R.raw.coindrop);
    }

    private void flipTheCoin() {

        //when Switch widget is on
        // check current state of a Switch (true or false).
        Boolean isChildPickMode = childPickModeSW.isChecked();

        if (isChildPickMode && childrenQueue.getSelectedChild() != null && coinRBsGroup.getCheckedRadioButtonId() == -1) {
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
                if (isChildPickMode && childrenQueue.getSelectedChild() != null) {
                    assert childrenQueue.getSelectedChild() != null;
                    CoinFlip flip = new CoinFlip(childrenQueue.getSelectedChild().getName(), childChoice, tossResultNum);
                    coinFlipHistory.addFlipRecord(flip);
                }

                if (childrenQueue.getNextChild() == childManager.DEFAULT_CHILD) {
                    Toast.makeText(CoinFlipActivity.this, "No child added yet.", Toast.LENGTH_SHORT).show();
                    childPickModeSW.setChecked(false);
                } else {
                    childTurnTv.setText(childrenQueue.getSelectedChild().getName() + "'s turn to pick");

                    Uri imgPFP = childrenQueue.getSelectedChild().getPicture() == null ?  Uri.parse(baseIMAGE) : Uri.parse(childrenQueue.getSelectedChild().getPicture());
                    selectedChildImgView.setImageURI(imgPFP);
                    selectedChildImgView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        coin.startAnimation(rotateAnimation);

    }

}