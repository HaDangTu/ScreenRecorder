package com.example.screeenrecordernative;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.screeenrecordernative.adapter.VideoRecyclerAdapter;
import com.example.screeenrecordernative.core.SettingsApp;
import com.example.screeenrecordernative.data.VideoInformation;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private ArrayList<VideoInformation> data;
    private VideoRecyclerAdapter adapter;
    private SettingsApp settingsApp;
    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        RecyclerView recyclerVideoView = view.findViewById(R.id.recycler_view_video);
        final SwipeRefreshLayout refreshVideoLayout = view.findViewById(R.id.refresh_video);

        settingsApp = new SettingsApp(getContext());
        String filePath = settingsApp.getVideoStoragePath();
        data = new ArrayList<>();
        File file = new File(filePath);
        File[] listVideo = file.listFiles();
        //Because folder ScreenRecord has child folder is Screenshot
        // so listVideo.length must greater than 1
        if (listVideo.length > 1) {
            for (File video : listVideo)
                if (!video.isDirectory())
                    data.add(new VideoInformation(video.getAbsolutePath()));


            adapter = new VideoRecyclerAdapter(data, getContext());
            adapter.setOnItemClickListener(new VideoRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(VideoInformation videoInformation) {
                    Intent intent = new Intent(getContext(), ViewVideoActivity.class);
                    intent.putExtra(ViewVideoActivity.VIDEO_PATH, videoInformation.getPathName());
                    startActivity(intent);
                }
            });

            adapter.setOnItemLongClickListener(new VideoRecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    adapter.setItem(position);
                    adapter.setCheckboxVisible(true);
                    setHasOptionsMenu(true);
                }
            });
            recyclerVideoView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerVideoView.setAdapter(adapter);
        }

        refreshVideoLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
                refreshVideoLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_library, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_video_item_select_all:
                adapter.setIsSelectAll(!adapter.getIsSelectAll());
                break;
            case R.id.menu_video_item_delete:
                ArrayList<Integer> checkList = adapter.getCheckList();
                if (checkList.size() > 0) {
                    for (int pos : checkList) {
                        VideoInformation videoInformation = data.get(pos);
                        File file = new File(videoInformation.getPathName());
                        if (file.delete())
                            Toast.makeText(getContext(), "Video has been deleted",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(), "Delete image fail",
                                    Toast.LENGTH_LONG).show();
                    }
                    reloadData();
                    adapter.clearCheckList();
                }
                else
                    Toast.makeText(getContext(), "Nothing select",
                            Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_video_item_close:
                setHasOptionsMenu(false);
                adapter.setIsSelectAll(false);
                adapter.setCheckboxVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadData() {
        data = new ArrayList<>();
        String pathName = settingsApp.getVideoStoragePath();
        File file = new File(pathName);
        File[] videos = file.listFiles();
        if (videos.length > 0) {
            for (File video : videos)
                if (!video.isDirectory())
                    data.add(new VideoInformation(video.getAbsolutePath()));
            adapter.setData(data);
        }
    }
}
