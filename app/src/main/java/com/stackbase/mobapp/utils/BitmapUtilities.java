package com.stackbase.mobapp.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

public class BitmapUtilities {
    public static Bitmap getBitmapThumbnail(Bitmap bmp,int width,int height){
        Bitmap bitmap = null;
        if(bmp != null ){
            bitmap = ThumbnailUtils.extractThumbnail(bmp, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
