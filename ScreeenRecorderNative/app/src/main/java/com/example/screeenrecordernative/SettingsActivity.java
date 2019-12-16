package com.example.screeenrecordernative;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.screeenrecordernative.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SettingsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int tab;

    public static final String TAB_SELECTED = "tab selected";
    public static final int TAB_SETTINGS = 0;
    public static final int TAB_VIDEO_LIBRARY = 1;
    public static final int TAB_SCREENSHOT_LIBRARY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        Intent stopServiceIntent = new Intent(this, ForegroundService.class);
//        stopService(stopServiceIntent);
        System.out.println("On create settings activity");
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(createNewTab(R.drawable.ic_action_settings, R.string.btSettingsName));
        tabLayout.addTab(createNewTab(R.drawable.ic_tab_video_library, R.string.btVideoName));
        tabLayout.addTab(createNewTab(R.drawable.ic_tab_screenshot_library, R.string.btTakeScreenshotName));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                PagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setLabelOfTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);

        Bundle bundle = getIntent().getExtras();
        tab = bundle.getInt(TAB_SELECTED);
        viewPager.setCurrentItem(tab);
        setLabelOfTab(tab);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        tab = bundle.getInt(TAB_SELECTED);
        System.out.println(tab);
        viewPager.setCurrentItem(tab);
        setLabelOfTab(tab);
        Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(collapseNotification);
    }

    private TabLayout.Tab createNewTab(int resIDIcon, int resText){
        return tabLayout.newTab()
                .setIcon(resIDIcon);
    }

    private void setLabelOfTab(int tab){
        switch (tab){
            case TAB_SETTINGS:
                setTitle("Settings");
                break;
            case TAB_VIDEO_LIBRARY:
                setTitle("Video");
                break;
            case TAB_SCREENSHOT_LIBRARY:
                setTitle("Screenshot");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Start settings activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume settings activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pause settings activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stop settings activity");
    }
}
