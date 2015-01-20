package com.stackbase.mobapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import com.stackbase.mobapp.R;
import com.stackbase.mobapp.utils.Constant;
import com.stackbase.mobapp.view.NewGridViewAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThumbnailsActivity extends Activity implements AbsListView.OnScrollListener {

    private GridView gridView;
    private BaseAdapter customGridAdapter;
    private ImageButton takePictureBtn;
    private static final String TAG = ThumbnailsActivity.class.getSimpleName();
    private List<String> mList = null;
    private static Map<String, Bitmap> gridviewBitmapCaches = null;

    public static Map<String, Bitmap> getGridviewBitmapCaches() {
        return gridviewBitmapCaches;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnails);
        gridView = (GridView) findViewById(R.id.picturesGridView);
        gridviewBitmapCaches = new HashMap<String, Bitmap>();
        initData();
        setAdapter();

        takePictureBtn = (ImageButton) this.findViewById(R.id.takepictureBtn);
        takePictureBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ThumbnailsActivity.this, CaptureActivity.class);
                String picFolder = getIntent().getStringExtra(Constant.INTENT_KEY_PIC_FOLDER);
                intent.putExtra(Constant.INTENT_KEY_PIC_FOLDER, picFolder);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            // refresh the Gridview
            initData();
            setAdapter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thumbnails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mList = new ArrayList<String>();
        String picFolder = getIntent().getStringExtra(Constant.INTENT_KEY_PIC_FOLDER);
        File pF = new File(picFolder);
        File[] pictures = pF.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (int i = 0; i < pictures.length; i++) {
            mList.add(pictures[i].getAbsolutePath());
        }
    }

    private void setAdapter() {
        //        customGridAdapter = new GridViewAdapter(this, R.layout.thumbnail_row, getData());
        customGridAdapter = new NewGridViewAdapter(this, mList);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnScrollListener(this);
    }

//    private ArrayList<ImageItem> getData() {
//        final ArrayList imageItems = new ArrayList();
//        // retrieve String drawable array
//        String picFolder = getIntent().getStringExtra(Constant.INTENT_KEY_PIC_FOLDER);
//        File pF = new File(picFolder);
//        File[] pictures = pF.listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String filename) {
//                if (filename.endsWith(".jpg")) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        for (int i = 0; i < pictures.length; i++) {
//            try {
//                byte[] decodedData = Helper.loadFile(pictures[i].getAbsolutePath());
//                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length);
//                imageItems.add(new ImageItem(ThumbnailUtils.extractThumbnail(bitmap, 40, 40,
//                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT),
//                        "Image#" + i, pictures[i].getAbsolutePath()));
//            } catch (Exception ex) {
//                Log.d(TAG, "Fail to load File: " + ex.getMessage());
//            }
//        }
//
//        return imageItems;
//
//    }

    private void recycleBitmapCaches(int fromPosition, int toPosition) {
        Bitmap delBitmap = null;
        for (int del = fromPosition; del < toPosition; del++) {
            if (mList.get(del) != null) {
                delBitmap = gridviewBitmapCaches.get(mList.get(del));
                if (delBitmap != null) {
                    Log.d(TAG, "release position:" + del);
                    gridviewBitmapCaches.remove(mList.get(del));
                    delBitmap.recycle();
                    delBitmap = null;
                }
            }
        }
    }

    private void recycleAllBitmapCaches() {
        Bitmap delBitmap;
        for (Map.Entry<String, Bitmap> entry : gridviewBitmapCaches.entrySet()) {
            delBitmap = entry.getValue();
            if (delBitmap != null) {
                Log.d(TAG, "release bitmap:" + entry.getKey());
                delBitmap.recycle();
                delBitmap = null;
            }
        }
        gridviewBitmapCaches.clear();

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        recycleBitmapCaches(0, firstVisibleItem);
        recycleBitmapCaches(firstVisibleItem + visibleItemCount, totalItemCount);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    protected void onDestroy() {
        // Release all the bitmaps from cache
        recycleAllBitmapCaches();
        Log.d(TAG, "size: " + gridviewBitmapCaches.size());
        gridviewBitmapCaches = null;
        super.onDestroy();
    }


}
