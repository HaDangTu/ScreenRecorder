package com.example.screeenrecordernative;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_REF_LIST_RESOLUTION = "list_resolution";
    public static final String KEY_REF_LIST_FPS = "list_fps";
    public static final String KEY_REF_LIST_BITRATE = "list_bit_rate";
    public static final String KEY_REF_LIST_ORIENTATION = "list_orientation";
//    public static final String KEY_REF_LIST_SPEED = "list_speed";
    public static final String KEY_REF_SWITCH_DISPLAY_TIMER = "switch_display_timer";
    public static final String KEY_REF_EDIT_STORAGE = "edit_storage";

    public SettingsFragment(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
