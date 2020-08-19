package com.example.screeenrecordernative;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;


import android.os.Bundle;
import android.view.View;


import com.example.screeenrecordernative.core.NotificationCustom;
import com.example.screeenrecordernative.core.ScreenCapture;
import com.example.screeenrecordernative.core.SettingsApp;
import com.example.screeenrecordernative.core.TimeRecord;


public class RecordActivity extends AppCompatActivity {

    private final int REQUEST_CODE_RECORD = 120;
    private ScreenCapture screenCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenCapture = ScreenCapture.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(collapseNotification);

        startActivityForResult(screenCapture.createScreenCaptureIntent(), REQUEST_CODE_RECORD);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECORD) {
            if (resultCode == RESULT_OK) {
                SettingsApp settingsApp = new SettingsApp(this);
                NotificationCustom notificationCustom = NotificationCustom.getInstance(getApplication());
                notificationCustom.updateVisibilityOfTimer(settingsApp.isDisplayTimer() ?
                        View.VISIBLE : View.GONE);
                notificationCustom.show(notificationCustom.PROGRESS_LAYOUT);

                Intent intent = new Intent(this, ControlBar.class);
                stopService(intent);

                TimeRecord timeRecord = TimeRecord.getInstance();
                timeRecord.start();


                screenCapture.prepareRecorder(this, settingsApp.getQuality(),
                        settingsApp.getFps(), settingsApp.getBitrate(),
                        settingsApp.getOrientation(), settingsApp.getVideoStoragePath());

                screenCapture.startRecord(resultCode, data);

            } else
                startService(new Intent(this, ControlBar.class));
            finish();
        }
    }
}
