package com.example.screeenrecordernative.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.screeenrecordernative.ScreenshotFragment;
import com.example.screeenrecordernative.SettingsFragment;
import com.example.screeenrecordernative.VideoFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTab;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTab) {
        super(fm, behavior);
        this.numOfTab = numOfTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SettingsFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new ScreenshotFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
