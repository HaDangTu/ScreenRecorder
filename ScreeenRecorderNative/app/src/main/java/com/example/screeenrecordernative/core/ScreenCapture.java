package com.example.screeenrecordernative.core;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.CamcorderProfile;

import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScreenCapture {
    private MediaRecorder recorder;
    private MediaProjection projection;
    private MediaProjectionManager manager;

    private VirtualDisplay display;
    private final String DISPLAY_NAME = "record display";

    private DisplayMetrics metrics;

    private boolean isPause;

    private static ScreenCapture instance;

    private String imagePath;

    private Context context;
    /**
     * This constructor is used for screen record video
     * @param activity current activity
     */
    private ScreenCapture(AppCompatActivity activity){
        isPause = false;
        manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    public void prepareRecorder(AppCompatActivity activity, int quality, int fps, int bitrate,
                                int orientation, String absolutePath) {
        CamcorderProfile profile = CamcorderProfile.get(quality);
        profile.videoFrameWidth = metrics.widthPixels;
        profile.videoFrameHeight = metrics.heightPixels;
        profile.videoFrameRate = fps;
        profile.duration = 86400000;
        profile.videoBitRate = bitrate == 0 ? autoBitRate(quality, fps) : bitrate;

        //format file name
        String pathName = generateFileName(absolutePath, ".mp4");

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setProfile(profile);

        if (orientation == 100){
            recorder.setOrientationHint(autoOrientation(activity.getWindowManager()));
        }
        else recorder.setOrientationHint(orientation);

        recorder.setOutputFile(pathName);

        try{
            recorder.prepare();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /**
     * This constructor is used for take screenshot
     *
     */
    public ScreenCapture(Context context, String imagePath){
        this.context = context;
        manager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        this.imagePath = imagePath;
    }

    private String generateFileName(String absolutePath, String fileType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_hhmmss", Locale.US);
        String fileName = dateFormat.format(new Date());
        return absolutePath + "/" + fileName + fileType;
    }

    public Intent createScreenCaptureIntent(){
        return manager.createScreenCaptureIntent();
    }

    public void startRecord(int resultCode, Intent data){
        projection = manager.getMediaProjection(resultCode, data);
        display = projection.createVirtualDisplay(DISPLAY_NAME, metrics.widthPixels,
                metrics.heightPixels, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, recorder.getSurface(),
                null, null);

        recorder.start();
    }

    public void pauseRecord(){
        recorder.pause();
    }

    public void resumeRecord(){
        recorder.resume();
    }

    public void stopRecord(){
        recorder.stop();
        stopProjection();
    }

    public boolean getIsPause() {
        return isPause;
    }

    public void setIsPause(boolean pause) {
        isPause = pause;
    }

    public static ScreenCapture getInstance(AppCompatActivity activity){
        instance = new ScreenCapture(activity);
        return instance;
    }

    public static ScreenCapture getInstance(){
        return instance;
    }

    private int autoOrientation (WindowManager windowManager){
        int angle = 0;
        switch (windowManager.getDefaultDisplay().getRotation()){
            case Surface.ROTATION_90:
                angle = 90;
                break;
            case Surface.ROTATION_180:
                angle = 180;
                break;
            case Surface.ROTATION_270:
                angle = 270;
                break;
        }
        return angle;
    }

    private int autoBitRate(int quality, int fps){
        int bitRate = 0;
        switch (quality){
            case CamcorderProfile.QUALITY_LOW:
                bitRate = 1000000;
                break;
            case CamcorderProfile.QUALITY_CIF:
                bitRate = 1500000;
                break;
            case CamcorderProfile.QUALITY_480P:
                bitRate = 2500000;
                break;
            case CamcorderProfile.QUALITY_720P:
                bitRate = fps >= 60 ? 5000000 : 4000000;
                break;
            case CamcorderProfile.QUALITY_1080P:
            case CamcorderProfile.QUALITY_HIGH:
                bitRate = fps >= 60 ? 8000000 : 5000000;
                break;
            case CamcorderProfile.QUALITY_2160P:
                bitRate = 45000000;
                break;
        }
        return bitRate;
    }

    public void takeScreenshot(int resultCode, Intent data) {
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;
        projection = manager.getMediaProjection(resultCode, data);
        ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888,
                2);

        display = projection.createVirtualDisplay(DISPLAY_NAME, width, height, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY, imageReader.getSurface(),
                null, null);

        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireLatestImage();

                if (image != null) {
                    final Image.Plane[] planes = image.getPlanes();
                    Buffer buffer = planes[0].getBuffer();

                    int imgWidth = image.getWidth();
                    int imgHeight = image.getHeight();

                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * imgWidth;

                    Bitmap bitmap = Bitmap.createBitmap(imgWidth + rowPadding / pixelStride
                            , imgHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    FileOutputStream fileOutputStream =  null;
                    try {
                        fileOutputStream = new FileOutputStream(generateFileName(
                                imagePath, ".jpg"));

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    }
                    catch (IOException fe){
                        System.err.println(fe.getMessage());
                    }
                    finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            stopProjection();
                            Toast.makeText(context, "Screenshot has been saved",
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (IOException ioe){
                            System.err.println(ioe.getMessage());
                        }
                    }
                }
            }
        }, new Handler());
    }

    public void stopProjection(){
        projection.stop();
        display.release();
    }

}
