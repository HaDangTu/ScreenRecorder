package com.example.screeenrecordernative;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.screeenrecordernative.core.ScreenCapture;
import com.example.screeenrecordernative.core.SettingsApp;

public class TakeScreenShotActivity extends AppCompatActivity {

    public final int REQUEST_CODE_TAKE_SCREENSHOT = 1110;
    public ScreenCapture screenCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_screen_shot);
        SettingsApp settingsApp = new SettingsApp(this);
//        String imagePath = settingsApp.getImageStoragePath();
        System.out.println("On create take screeenshot activity");
        screenCapture = new ScreenCapture(this, settingsApp.getImageStoragePath());

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Start takescreenshot activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startActivityForResult(screenCapture.createScreenCaptureIntent(), REQUEST_CODE_TAKE_SCREENSHOT);
        System.out.println("Resume takescreenshot activity");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Activity result");
        if (requestCode == REQUEST_CODE_TAKE_SCREENSHOT) {
            if (resultCode == RESULT_OK) {
                stopService(new Intent(this, ControlBar.class));
                screenCapture.takeScreenshot(resultCode, data);
            }
            else {
                startService(new Intent(this, ControlBar.class));
                System.out.println("Cancel takescreenshot");
            }
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pause take screenshot activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stop take screenshot activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroy take screenshot activity");
    }
}
