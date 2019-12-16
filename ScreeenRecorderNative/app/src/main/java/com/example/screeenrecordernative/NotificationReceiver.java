package com.example.screeenrecordernative;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.screeenrecordernative.core.NotificationCustom;
import com.example.screeenrecordernative.core.ScreenCapture;
import com.example.screeenrecordernative.core.TimeRecord;

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCustom notificationCustom = NotificationCustom.getInstance();
        ScreenCapture screenRecorder = ScreenCapture.getInstance();
        /*if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_VIDEO)){
            Intent newActivityIntent = new Intent(context, RecordActivity.class);
            newActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newActivityIntent);
        }
        else*/ if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_EXIT)){

            Intent stopForegroundService = new Intent(context, ForegroundService.class);
            context.stopService(stopForegroundService);

            Intent stopControlBar = new Intent(context, ControlBar.class);
            context.stopService(stopControlBar);

//            notificationCustom.unregisterNotificationChanel();
            notificationCustom.cancel();
        }
        /*else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_SETTINGS)){
            Intent mainIntent = new Intent(context, SettingsActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.putExtra(SettingsActivity.TAB_SELECTED, TAB_SETTINGS);
            ActivityCompat.startActivity(context, mainIntent, null);
        }
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_SCREENSHOT)) {
            Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(collapseNotification);
            Intent takeScreenshotIntent = new Intent(context, TakeScreenShotActivity.class);
            takeScreenshotIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(takeScreenshotIntent);
        }
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_LIBRARY)) {
            Intent mainIntent = new Intent(context, SettingsActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.putExtra(SettingsActivity.TAB_SELECTED, TAB_VIDEO_LIBRARY);
            context.startActivity(mainIntent);
        }*/
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_STOP)){
            TimeRecord timeRecord = TimeRecord.getInstance();
            timeRecord.stop();
            screenRecorder.stopRecord();
            notificationCustom.show(notificationCustom.MAIN_LAYOUT);

            Intent startControlBarIntent = new Intent(context, ControlBar.class);
            context.startService(startControlBarIntent);
            screenRecorder.setIsPause(false);
            Toast.makeText(context, "Video has been saved", Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_PAUSE)){
            TimeRecord timeRecord = TimeRecord.getInstance();
            boolean isPause = screenRecorder.getIsPause();
            int drawableId;
            String text;
            if (isPause){
                drawableId = R.drawable.ic_action_pause;
                text = "Pause";
                timeRecord.resume();
                screenRecorder.resumeRecord();
            }
            else {
                drawableId = R.drawable.ic_action_video;
                text = "Continue";
                timeRecord.pause();
                screenRecorder.pauseRecord();
            }
            screenRecorder.setIsPause(!isPause);

            notificationCustom.updateNotificationDrawableTopView(notificationCustom.PROGRESS_LAYOUT,
                    R.id.btPause, drawableId);
            notificationCustom.updateNotificationTextView(notificationCustom.PROGRESS_LAYOUT,
                    R.id.btPause, text);
            notificationCustom.show(notificationCustom.PROGRESS_LAYOUT);
        }
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_START_CONTROL_BAR)){
            Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(collapseNotification);
            Intent startCtrlBarIntent = new Intent(context, ControlBar.class);
            context.startService(startCtrlBarIntent);
        }
    }
}
