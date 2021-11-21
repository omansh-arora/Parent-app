package com.example.parentapp.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parentapp.R;
import com.example.parentapp.model.ChildManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;


public class addImageActivity extends AppCompatActivity {

    private Button button_addImage;
    private Button button_return;
    private static final String PREFS_NAME = "ChildPrefs";
    private ImageView pfp;
    ChildManager cm;
    int childIndex;
    int mode = 0;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        Resources resources = this.getResources();

        button_addImage = findViewById(R.id.bt_setImageFinal);
        button_return = findViewById(R.id.bt_setImageReturn);
        pfp = findViewById(R.id.imgPFPFinal);
        pfp.setImageResource(R.drawable.ic_default);

        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("Edit")){
            button_addImage.setText("Change image");

            childIndex = intent.getIntExtra("position",0);
            cm = getChildManager(this);
            pfp.setImageURI(Uri.parse(cm.getChildPic(childIndex)));
            mode = 1;

        }

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addImageActivity.this).start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){




        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            button_addImage.setText("Change image");
            Uri fileUri= data.getData();
            pfp.setImageURI(fileUri);
            savePFPUri(fileUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode,resultCode,data);

    }

    private void savePFPUri(Uri uri) {
        SharedPreferences prefs = this.getSharedPreferences("imgURIprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String uriString = uri.toString();
        editor.putString("Image",uriString);
        editor.apply();
    }


    static public Uri getPFPUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("imgURIprefs", MODE_PRIVATE);
        String uriString = prefs.getString("Image", "");
        return Uri.parse(uriString);
    }

    private void saveChildManager(ChildManager cm) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cm);
        editor.putString("ChildManager", json);
        editor.commit();
    }

    static public ChildManager getChildManager(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("ChildManager", "");
        ChildManager children = gson.fromJson(json, ChildManager.class);
        return children;
    }
}