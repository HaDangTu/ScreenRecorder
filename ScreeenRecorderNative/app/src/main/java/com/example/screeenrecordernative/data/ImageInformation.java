package com.example.screeenrecordernative.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;


public class ImageInformation extends Information {
//    private Bitmap imageContent;
    private Drawable imageContent;
    public ImageInformation(String pathName){
        super(pathName);
//        imageContent = BitmapFactory.decodeFile(pathName);
        imageContent = Drawable.createFromPath(pathName);
        imageContent.setBounds(0, 0, imageContent.getIntrinsicWidth() / 3,
                imageContent.getIntrinsicHeight() / 3);
    }

//    public Bitmap getImageContent(){
//        return imageContent;
//    }

    public Drawable getImageContent(){
        return imageContent;
    }
}
