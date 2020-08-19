package com.example.screeenrecordernative;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideoActivity extends AppCompatActivity {

    public static final String VIDEO_PATH = "video file path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String filePath = bundle.getString(VIDEO_PATH);

        MediaController controller = new MediaController(this);
        VideoView videoView = findViewById(R.id.video_view);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();
    }
}
