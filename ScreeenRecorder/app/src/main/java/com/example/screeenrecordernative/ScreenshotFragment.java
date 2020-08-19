package com.example.screeenrecordernative;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.screeenrecordernative.adapter.ScreenshotRecyclerAdapter;
import com.example.screeenrecordernative.core.SettingsApp;
import com.example.screeenrecordernative.data.ImageInformation;


import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenshotFragment extends Fragment {
    public static final String IMAGE_PATH = "image path";
    private ArrayList<ImageInformation> data;
    private ScreenshotRecyclerAdapter adapter;
    private SettingsApp settingsApp;
    public ScreenshotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View screenshotView = inflater.inflate(R.layout.fragment_screenshot, container, false);
        RecyclerView imageRecyclerView = screenshotView.findViewById(R.id.image_recycler_view);
        final SwipeRefreshLayout imageRefreshLayout = screenshotView.findViewById(R.id.refresh_image);

        settingsApp = new SettingsApp(getContext());
        data = new ArrayList<>();
        String pathName = settingsApp.getImageStoragePath();

        File file = new File(pathName);
        File[] images = file.listFiles();
        if (images.length > 0) {
            for (File image : images)
                data.add(new ImageInformation(image.getAbsolutePath()));

            adapter = new ScreenshotRecyclerAdapter(data, getContext());
            adapter.setOnItemClickListener(new ScreenshotRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ImageInformation imageInformation) {
                    Intent intent = new Intent(getContext(), ViewImageActivity.class);
                    intent.putExtra(IMAGE_PATH, imageInformation.getPathName());
                    startActivity(intent);
                }
            });

            adapter.setOnItemLongClickListener(new ScreenshotRecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    adapter.setItem(position);
                    adapter.setIsVisible(true);
                    setHasOptionsMenu(true);
                }
            });
            imageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            imageRecyclerView.setAdapter(adapter);

        }

        imageRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
                imageRefreshLayout.setRefreshing(false);
            }
        });
        return screenshotView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screenshot_library, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_screenshot_item_select_all:
                adapter.setIsSelectAll(!adapter.getIsSelectAll());
                break;
            case R.id.menu_screenshot_item_delete:
                ArrayList<Integer> checkList = adapter.getCheckList();
                if (checkList.size() > 0) {
                    for (int pos : checkList) {
                        ImageInformation imageInformation = data.get(pos);
                        File file = new File(imageInformation.getPathName());
                        if (file.delete())
                            Toast.makeText(getContext(), "Image has been deleted",
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
            case R.id.menu_screenshot_item_close:
                setHasOptionsMenu(false);
                adapter.setIsSelectAll(false);
                adapter.setIsVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        reloadData();
        super.onResume();
    }

    private void reloadData(){
        data = new ArrayList<>();
        String pathName = settingsApp.getImageStoragePath();
        File file = new File(pathName);
        File[] images = file.listFiles();
        if (images.length > 0) {
            for (File image : images)
                data.add(new ImageInformation(image.getAbsolutePath()));
            adapter.setData(data);
        }
    }
}
