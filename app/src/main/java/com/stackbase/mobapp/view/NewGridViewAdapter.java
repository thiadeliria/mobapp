package com.stackbase.mobapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stackbase.mobapp.R;
import com.stackbase.mobapp.activity.ThumbnailsActivity;
import com.stackbase.mobapp.utils.BitmapUtilities;
import com.stackbase.mobapp.utils.Helper;

import java.lang.ref.WeakReference;
import java.util.List;

public class NewGridViewAdapter extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private List<String> mList = null;

    private int width = 120;
    private int height = 150;
    private static final String TAG = NewGridViewAdapter.class.getSimpleName();

    public NewGridViewAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public Object getItem(int arg0) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageItem viewHolder;
        if (convertView == null) {
            viewHolder = new ImageItem();
            convertView = mLayoutInflater.inflate(R.layout.thumbnail_row, null);
            viewHolder.setImageView((ImageView) convertView.findViewById(R.id.pictureImageView));
            viewHolder.setTextView((TextView) convertView.findViewById(R.id.pictureTexView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImageItem) convertView.getTag();
        }

        String url = mList.get(position);
        if (cancelPotentialLoad(url, viewHolder.getImageView())) {
            AsyncLoadImageTask task = new AsyncLoadImageTask(viewHolder.getImageView());
            LoadedDrawable loadedDrawable = new LoadedDrawable(task);
            viewHolder.getImageView().setImageDrawable(loadedDrawable);
            task.execute(position);
        }
        viewHolder.setPicPath(url);
        viewHolder.getTextView().setText("Image#" + position);
        return convertView;
    }


    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        bitmap = ThumbnailsActivity.getGridviewBitmapCaches().get(url);
        if (bitmap != null) {
            Log.d(TAG, "Find bitmap from Cache: " + url);
            return bitmap;
        }

        byte[] decodedData = Helper.loadFile(url);
        bitmap = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);

        bitmap = BitmapUtilities.getBitmapThumbnail(bitmap, width, height);
        return bitmap;
    }

    private class AsyncLoadImageTask extends AsyncTask<Integer, Void, Bitmap> {
        private String url = null;
        private final WeakReference<ImageView> imageViewReference;

        public AsyncLoadImageTask(ImageView imageview) {
            super();
            imageViewReference = new WeakReference<ImageView>(imageview);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bitmap;
            this.url = mList.get(params[0]);
            bitmap = getBitmapFromUrl(url);
            ThumbnailsActivity.getGridviewBitmapCaches().put(mList.get(params[0]), bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap resultBitmap) {
            if (isCancelled()) {
                resultBitmap = null;
            }
            if (imageViewReference != null) {
                ImageView imageview = imageViewReference.get();
                AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
                // Change bitmap only if this process is still associated with it
                if (this == loadImageTask) {
                    imageview.setImageBitmap(resultBitmap);
                    imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
            super.onPostExecute(resultBitmap);
        }
    }


    private boolean cancelPotentialLoad(String url, ImageView imageview) {
        AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);

        if (loadImageTask != null) {
            String bitmapUrl = loadImageTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                loadImageTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;

    }

    private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview) {
        if (imageview != null) {
            Drawable drawable = imageview.getDrawable();
            if (drawable instanceof LoadedDrawable) {
                LoadedDrawable loadedDrawable = (LoadedDrawable) drawable;
                return loadedDrawable.getLoadImageTask();
            }
        }
        return null;
    }

    public static class LoadedDrawable extends ColorDrawable {
        private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

        public LoadedDrawable(AsyncLoadImageTask loadImageTask) {
            super(Color.TRANSPARENT);
            loadImageTaskReference =
                    new WeakReference<AsyncLoadImageTask>(loadImageTask);
        }

        public AsyncLoadImageTask getLoadImageTask() {
            return loadImageTaskReference.get();
        }

    }
}
