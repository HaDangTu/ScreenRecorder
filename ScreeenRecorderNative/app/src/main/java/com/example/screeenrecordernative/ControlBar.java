package com.example.screeenrecordernative;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class ControlBar extends Service {
    private View controlBarView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private float deltaX;
    private float deltaY;
    private int lastAction;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        controlBarView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.control_bar_layout, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        controlBarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return dragView(v, event);
            }
        });

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        setListener();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(controlBarView, params);
    }

    @SuppressWarnings("ClickableViewAccessibility")
    private void setListener() {
        final ImageButton btSettings = controlBarView.findViewById(R.id.ctrl_bar_bt_settings);
        final ImageButton btRecord = controlBarView.findViewById(R.id.ctrl_bar_bt_record);
        final ImageButton btScreenshot = controlBarView.findViewById(R.id.ctrl_bar_bt_screenshot);
        final ImageButton btClose = controlBarView.findViewById(R.id.ctrl_bar_bt_exit);

        //btSettings
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                settingsIntent.putExtra(SettingsActivity.TAB_SELECTED, SettingsActivity.TAB_SETTINGS);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingsIntent);
            }
        });

//        btSettings.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return dragView(v, event);
//            }
//        });

        //btRecord
        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                Intent newActivityIntent = new Intent(getApplicationContext(), RecordActivity.class);
                newActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newActivityIntent);
            }
        });

//        btRecord.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return dragView(v, event);
//            }
//        });

        //btScreenshot
        btScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                System.out.println("Button screen shot is clicked");
                Intent takeScreenshotIntent = new Intent(getApplicationContext(),
                        TakeScreenShotActivity.class);
                takeScreenshotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(takeScreenshotIntent);
            }
        });

//        btScreenshot.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return dragView(v, event);
//            }
//        });

        //btClose
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

//        btClose.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return dragView(v, event);
//            }
//        });
    }

    private boolean dragView(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                deltaX = Math.abs(motionEvent.getRawX() - params.x);
                deltaY = Math.abs(motionEvent.getRawY() - params.y);
                lastAction = motionEvent.getAction();
                return true;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN){
                    if (view instanceof ImageButton){
                        ImageButton button = (ImageButton) view;
                        button.performClick();
                    }
                }
                windowManager.updateViewLayout(controlBarView, params);
                lastAction = motionEvent.getAction();
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = (int) (motionEvent.getRawX() - deltaX);
                params.y = (int) (motionEvent.getRawY() - deltaY);

                windowManager.updateViewLayout(controlBarView, params);
                lastAction = motionEvent.getAction();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (controlBarView != null)
            windowManager.removeView(controlBarView);
    }
}
