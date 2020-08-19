package com.example.screeenrecordernative.core;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.screeenrecordernative.NotificationReceiver;
import com.example.screeenrecordernative.R;
import com.example.screeenrecordernative.RecordActivity;
import com.example.screeenrecordernative.SettingsActivity;
import com.example.screeenrecordernative.TakeScreenShotActivity;

import java.util.Hashtable;

public class NotificationCustom {
    private static NotificationCustom instance;

    private final int REQUEST_CODE_PENDING_VIDEO = 100;
    private final int REQUEST_CODE_PENDING_SETTINGS = 101;
    private final int REQUEST_CODE_PENDING_SCREENSHOT = 102;
    private final int REQUEST_CODE_PENDING_LIBRARY = 103;
    private final int REQUEST_CODE_PENDING_EXIT = 104;
    private final int REQUEST_CODE_PENDING_STOP = 105;
    private final int REQUEST_CODE_PENDING_PAUSE = 106;
    private final int REQUEST_CODE_PENDING_START_CTRL_BAR = 107;

    private final String CHANEL_ID = "chanel 1";
    public final int NOTIFICATION_ID = 1001;

    private NotificationManagerCompat manager;

//    public final String ACTION_VIDEO = "VIDEO";
//    public final String ACTION_SCREENSHOT = "SCREENSHOT";
//    public final String ACTION_SETTINGS = "SETTINGS";
    public final String ACTION_EXIT = "EXIT";
//    public final String ACTION_LIBRARY = "LIBRARY";

    public final String ACTION_STOP = "STOP";
    public final String ACTION_PAUSE = "PAUSE";

    public final String ACTION_START_CONTROL_BAR = "CONTROL_BAR";
    public final String MAIN_LAYOUT = "MAIN";
    public final String PROGRESS_LAYOUT = "PROGRESS";

    private Hashtable<String, RemoteViews> layouts;

    private Application application;

    private NotificationCustom(final Application application){
        this.application = application;
        //Main notification
        RemoteViews mainRecordLayout = new RemoteViews(application.getPackageName(),
                R.layout.main_record_layout);
        Intent settingsIntent = new Intent(application, SettingsActivity.class);
        settingsIntent.putExtra(SettingsActivity.TAB_SELECTED, SettingsActivity.TAB_SETTINGS);

        Intent libraryIntent = new Intent(application, SettingsActivity.class);
        libraryIntent.putExtra(SettingsActivity.TAB_SELECTED, SettingsActivity.TAB_VIDEO_LIBRARY);

        Intent recordIntent = new Intent(application, RecordActivity.class);
//        recordIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        Intent screenshotIntent = new Intent(application, TakeScreenShotActivity.class);
        screenshotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        screenshotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainRecordLayout.setOnClickPendingIntent(R.id.btSettings,
                createPendingIntent(settingsIntent, REQUEST_CODE_PENDING_SETTINGS));

        mainRecordLayout.setOnClickPendingIntent(R.id.btVideo,
                createPendingIntent(recordIntent, REQUEST_CODE_PENDING_VIDEO));

        mainRecordLayout.setOnClickPendingIntent(R.id.btScreenshot,
                createPendingIntent(screenshotIntent, REQUEST_CODE_PENDING_SCREENSHOT));

        mainRecordLayout.setOnClickPendingIntent(R.id.btLibrary,
                createPendingIntent(libraryIntent, REQUEST_CODE_PENDING_LIBRARY));

        mainRecordLayout.setOnClickPendingIntent(R.id.btExit,
                createPendingIntent(ACTION_EXIT, REQUEST_CODE_PENDING_EXIT));

        mainRecordLayout.setOnClickPendingIntent(R.id.content_text,
                createPendingIntent(ACTION_START_CONTROL_BAR, REQUEST_CODE_PENDING_START_CTRL_BAR));


        //Progress notification
        RemoteViews inProgressLayout = new RemoteViews(application.getPackageName(),
                R.layout.in_progress_layout);

        inProgressLayout.setOnClickPendingIntent(R.id.btStop,
                createPendingIntent(ACTION_STOP, REQUEST_CODE_PENDING_STOP));
        inProgressLayout.setOnClickPendingIntent(R.id.btPause,
                createPendingIntent(ACTION_PAUSE, REQUEST_CODE_PENDING_PAUSE));

        layouts = new Hashtable<>();
        layouts.put(MAIN_LAYOUT, mainRecordLayout);
        layouts.put(PROGRESS_LAYOUT, inProgressLayout);

        manager = NotificationManagerCompat.from(application);
    }

    public void registerNotificationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel =
                    new NotificationChannel(CHANEL_ID, "Notification main",
                            NotificationManager.IMPORTANCE_LOW);
            chanel.setSound(null, null);
            manager.createNotificationChannel(chanel);
        }
    }

    /**
     * this method use to set on click listener for button on activity
     * @param action string action to distinguish buttons
     * @param requestCode request code
     * @return pending intent
     */
    private PendingIntent createPendingIntent(String action, int requestCode){
        Intent intent = new Intent(application, NotificationReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(application, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * this method use to start activity from notification
     * @param intent intent to start activity
     * @param requestCode request code to start activity
     * @return pending intent
     */
    private PendingIntent createPendingIntent(Intent intent, int requestCode){
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(application, requestCode, intent,
                0);
    }

    public Notification createNotification(String layout){
        Notification notification = new NotificationCompat.Builder(
                application.getApplicationContext(), CHANEL_ID)
                .setSmallIcon(R.drawable.ic_action_video)
                .setCustomBigContentView(layouts.get(layout))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(CHANEL_ID)
                .setOngoing(true)
                .build();
        return notification;
    }
    /**
     * show notification
     * @param layout choose type of notification (MAIN_LAYOUT/PROGRESS_LAYOUT)
     */
    public void show(String layout){
        manager.notify(NOTIFICATION_ID, createNotification(layout));
    }

    /**
     * cancel notification
     */
    public void cancel(){
        manager.cancel(NOTIFICATION_ID);
    }

    public void unregisterNotificationChanel(){
        manager.deleteNotificationChannel(CHANEL_ID);
    }
    /**
     * Create new instance of class (should use)
     * @param activity application
     * @return instance
     */
    public static NotificationCustom getInstance(Application activity){
        if (instance == null) instance = new NotificationCustom(activity);
        return instance;
    }

    /**
     * set attribute drawableTop of button, not set drawableLeft, Right, Bottom
     * @param layout layout you want to update (MainLayout/ProgressLayout)
     * @param viewId id of button which is declared in .xml layout file
     * @param resDrawableID id of drawable which is declared in drawable
     */
    public void updateNotificationDrawableTopView(String layout, int viewId,
                                                  int resDrawableID){
        RemoteViews remoteViews = layouts.get(layout);
        remoteViews.setTextViewCompoundDrawables(viewId, 0, resDrawableID, 0, 0);
    }

    /**
     * set text of button, textView, text
     * @param layout layout you want to update (MainLayout/ProgressLayout)
     * @param viewId id of button which is declared in .xml layout file
     * @param text content
     */
    public void updateNotificationTextView(String layout, int viewId, String text){
        RemoteViews remoteViews = layouts.get(layout);
        remoteViews.setTextViewText(viewId, text);
    }

    public void updateVisibilityOfTimer (int visibility) {
        RemoteViews remoteViews = layouts.get(PROGRESS_LAYOUT);
        remoteViews.setViewVisibility(R.id.timerText, visibility);
    }
    /**
     * WARNING: method này chỉ sử dụng khi đã biết instance khác null nếu chưa chắc chắn thì hãy
     * sử dụng method getInstance() phía trên
     * @return instance
     */
    public static NotificationCustom getInstance(){
        return instance;
    }

}
