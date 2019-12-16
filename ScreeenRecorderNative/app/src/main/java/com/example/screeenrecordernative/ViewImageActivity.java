package com.example.screeenrecordernative;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.ObjectAnimator;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class ViewImageActivity extends AppCompatActivity {

    ImageButton btDelete;
    ImageButton btBack;
    LinearLayout actionBar;
    ImageView imageView;
    boolean isCollapse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        final String pathName = data.getString(ScreenshotFragment.IMAGE_PATH);
        setContentView(R.layout.activity_view_image);
        isCollapse = true;
        btDelete = findViewById(R.id.btDelete_Image);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(pathName);
                if (file.delete())
                    Toast.makeText(ViewImageActivity.this,
                            "Image has been deleted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ViewImageActivity.this,
                            "Failed to delete image", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
        btBack = findViewById(R.id.back_button);
        actionBar = findViewById(R.id.action_view_bar);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(BitmapFactory.decodeFile(pathName));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup)btBack.getRootView());
                int visibility = isCollapse ? View.VISIBLE : View.GONE;
                btBack.setVisibility(visibility);
                actionBar.setVisibility(visibility);
                isCollapse = !isCollapse;
            }
        });
    }
}
