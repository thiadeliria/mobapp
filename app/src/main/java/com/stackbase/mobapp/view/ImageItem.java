package com.stackbase.mobapp.view;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ImageItem {
    private Bitmap image;
    private String title;

    private String picPath;
    private ImageView imageView;
    private TextView textView;

    public ImageItem(Bitmap image, String title, String picPath) {
        this.image = image;
        this.title = title;
        this.picPath = picPath;
    }

    public ImageItem() {
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}