package com.example.screeenrecordernative.core;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.screeenrecordernative.R;
import com.example.screeenrecordernative.SettingsFragment;

public class SettingsApp {
    private int quality;
    private int fps;
    private int bitrate;
    private int orientation;
    private String videoStoragePath;
    private String imageStoragePath;
    private boolean isDisplayTimer;

    public SettingsApp(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        quality = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_REF_LIST_RESOLUTION,  ""));

        fps = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_REF_LIST_FPS,  ""));

        bitrate = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_REF_LIST_BITRATE,  ""));

        orientation = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_REF_LIST_ORIENTATION,  ""));

        videoStoragePath = sharedPref.getString(SettingsFragment.KEY_REF_EDIT_STORAGE,  "");
        imageStoragePath = videoStoragePath.concat("/Screenshot");

        isDisplayTimer = sharedPref.getBoolean(SettingsFragment.KEY_REF_SWITCH_DISPLAY_TIMER, false);
    }

    public int getQuality() {
        return quality;
    }

    public int getFps() {
        return fps;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getOrientation() {
        return orientation;
    }

    public String getVideoStoragePath() {
        return videoStoragePath;
    }

    public String getImageStoragePath() {
        return imageStoragePath;
    }

    public boolean isDisplayTimer() {
        return isDisplayTimer;
    }
}
