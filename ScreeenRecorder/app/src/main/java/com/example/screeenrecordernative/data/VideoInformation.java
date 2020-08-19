package com.example.screeenrecordernative.data;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class VideoInformation extends Information {

    private Bitmap imagePreview;
//    private String resolution;
    private String duration;
    private String size;

    public VideoInformation(String pathName){
        super(pathName);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(pathName);
        File file = new File(pathName);

        imagePreview = retriever.getFrameAtTime(0);

//        resolution = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
//                + "x" + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

        duration = convertDuration(
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//        System.out.println(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        size = convertSize(file.length());
        retriever.release();
    }

    private String convertDuration(String durationString){
        long duration = Long.parseLong(durationString);
        long second = TimeUnit.MILLISECONDS.toSeconds(duration);
        int hour = (int) second / 3600;
        second = second % 3600;
        int minute = (int) second / 60;
        second = second % 60;
        return formatNumber(hour) + ":" + formatNumber(minute) + ":" + formatNumber((int)second);
    }

    private String formatNumber(int num){
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    private String convertSize(long size){
        String postFix;
        DecimalFormat format = new DecimalFormat("#.##");
        float sizeGB = size / 1000000f;
        if (sizeGB < 1000) {
            postFix = "MB";
        }
        else {
            sizeGB /= 1000;
            postFix = "GB";
        }
        return format.format(sizeGB) + postFix;
    }

    public Bitmap getImagePreview() {
        return imagePreview;
    }

//    public String getResolution() {
//        return resolution;
//    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public String getPathName() {
        return pathName;
    }
}
