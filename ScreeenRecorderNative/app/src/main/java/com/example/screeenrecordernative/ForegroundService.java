package com.example.screeenrecordernative;

import android.app.Service;;
import android.content.Context;
import android.content.Intent;

import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
;

import androidx.annotation.Nullable;

import com.example.screeenrecordernative.core.NotificationCustom;


public class ForegroundService extends Service {

    private static NotificationCustom notificationCustom;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationCustom = NotificationCustom.getInstance(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(notificationCustom.NOTIFICATION_ID,
                    notificationCustom.createNotification(notificationCustom.MAIN_LAYOUT),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
        }
        else
            startForeground(notificationCustom.NOTIFICATION_ID,
                    notificationCustom.createNotification(notificationCustom.MAIN_LAYOUT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Foreground service is destroy");
//        unregisterReceiver(notificationReceiver);
    }

    public static void startForegroundService(Context context) {
        Intent intent = new Intent(context, ForegroundService.class);
        context.startService(intent);
    }
}
