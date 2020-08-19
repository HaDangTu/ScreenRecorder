package com.example.screeenrecordernative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.example.screeenrecordernative.core.NotificationCustom;
import com.example.screeenrecordernative.core.SettingsApp;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final String[] permissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    private final int REQUEST_PERMISSION_CODE = 1001;
    private final int REQUEST_PERMISSION_OVERLAY_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) !=
                        PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
        else {
            initStorageFolder();
            startNotification();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults)
            if (result == PackageManager.PERMISSION_DENIED)
                finish();

        if (!Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_PERMISSION_OVERLAY_CODE);
        }
        else {
            initStorageFolder();
            startNotification();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_OVERLAY_CODE){
            initStorageFolder();
        }
    }

    private void startNotification() {
        NotificationCustom notificationCustom = NotificationCustom.getInstance(getApplication());
        notificationCustom.registerNotificationChanel();
        notificationCustom.show(notificationCustom.MAIN_LAYOUT);
        startService(new Intent(this, ControlBar.class));
        startService(new Intent(this, ForegroundService.class));
        finish();
    }

    private void initStorageFolder() {
        SettingsApp settingsApp = new SettingsApp(this);
        File folder = new File(settingsApp.getImageStoragePath());

        if (!folder.exists()){
            if (folder.mkdirs())
                System.out.println("Create storage folder successful");
            else
                System.out.println("Fail to create storage folder");
        }
    }
}
