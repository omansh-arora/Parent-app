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
import com.example.parentapp.model.Child;
import com.example.parentapp.model.ChildManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;


public class addImageActivity extends AppCompatActivity {

    private Button button_addImage;
    private Button button_return;
    private Button button_del;
    private static final String PREFS_NAME = "ChildPrefs";
    private ImageView pfp;
    ChildManager childManager;
    int childIndex;
    int mode = 0;
    String baseIMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        childManager = new ChildManager();

        Resources resources = this.getResources();
        baseIMAGE = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        button_addImage = findViewById(R.id.bt_setImageFinal);
        button_return = findViewById(R.id.bt_setImageReturn);
        button_del = findViewById(R.id.bt_delImage);
        button_del.setVisibility(View.INVISIBLE);
        pfp = findViewById(R.id.imgPFPFinal);
        pfp.setImageResource(R.drawable.ic_default);

        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("Edit")){
            button_addImage.setText("Change image");
            childIndex = intent.getIntExtra("position",0);

            //childManager = getChildManager(this);
            Child currentChild = childManager.getChildren().get(childIndex);

            pfp.setImageURI(Uri.parse(currentChild.getPicture()));

            if (currentChild.getPicture().equals(baseIMAGE))
                button_del.setVisibility(View.INVISIBLE);
            else button_del.setVisibility(View.VISIBLE);
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
                ImagePicker.with(addImageActivity.this).
                        cropSquare().start();
            }
        });

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pfp.setImageURI(Uri.parse(baseIMAGE));
                savePFPUri(Uri.parse(baseIMAGE));
                button_del.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onResume() {

        Resources resources = this.getResources();

        baseIMAGE = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_default))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_default))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_default))
                .build().toString();

        if(getPFPUri(this)==Uri.parse(baseIMAGE)){
            button_del.setVisibility(View.INVISIBLE);
        }
        else button_del.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){


        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            button_addImage.setText("Change image");
            Uri fileUri= data.getData();
            pfp.setImageURI(fileUri);
            button_del.setVisibility(View.VISIBLE);
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