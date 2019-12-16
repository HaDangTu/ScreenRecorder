package com.example.screeenrecordernative.core;

import com.example.screeenrecordernative.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimeRecord {
    private static TimeRecord instance;
    private int hour;
    private int minute;
    private int second;

    private Timer timer;

    private NotificationCustom notificationCustom;
    private TimeRecord(){
        hour = 0;
        minute = 0;
        second = 0;
        this.notificationCustom = NotificationCustom.getInstance();
    }

    public String getTime(){
        String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
        String strSecond = second < 10 ? "0" + second : String.valueOf(second);
        return strHour + ":" + strMinute + ":" + strSecond;
    }

    /**
     * start timer
     */
    public void start(){
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);
    }

    /**
     * pause timer
     */
    public void pause(){
        timer.cancel();
    }

    public void resume(){
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);
    }
    /**
     * stop timer
     */
    public void stop(){
        pause();
        hour = 0;
        minute = 0;
        second = 0;
    }

    public static TimeRecord getInstance(){
        if (instance == null) instance = new TimeRecord();
        return instance;
    }

    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            second++;
            if (second > 59){
                second = 0;
                minute++;
                if (minute > 59){
                    minute = 0;
                    hour++;
                    if (hour > 23){
                        hour = 0;
                    }
                }
            }
            notificationCustom.updateNotificationTextView(notificationCustom.PROGRESS_LAYOUT,
                    R.id.timerText, getTime());
            notificationCustom.show(notificationCustom.PROGRESS_LAYOUT);
        }
    }
}
