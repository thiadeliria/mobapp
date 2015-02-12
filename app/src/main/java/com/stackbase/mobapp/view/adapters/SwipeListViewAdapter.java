/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stackbase.mobapp.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stackbase.mobapp.R;
import com.stackbase.mobapp.view.swipelistview.SwipeListView;

import java.util.List;

public class SwipeListViewAdapter extends BaseAdapter {

    private static final String TAG = SwipeListViewAdapter.class.getSimpleName();
    private List<SwipeListViewItem> data;
    private Context context;
    private IUpdateCallback updateCallback;
    private LayoutInflater li;

    public SwipeListViewAdapter(Context context, List<SwipeListViewItem> data) {
        this.context = context;
        this.data = data;

    }

    public IUpdateCallback getUpdateCallback() {
        return updateCallback;
    }

    public void setUpdateCallback(IUpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SwipeListViewItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SwipeListViewItem item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            li = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.swipe_row, null);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.borrowerHeadImage);
            holder.title = (TextView) convertView.findViewById(R.id.borrowerNameText);
            holder.description = (TextView) convertView.findViewById(R.id.borrowerIdText);
            holder.delBtn = (Button) convertView.findViewById(R.id.delBorrowerBtn);
            holder.uploadBtn = (Button) convertView.findViewById(R.id.uploadBorrowerBtn);
            holder.uploadPB = (ProgressBar) convertView.findViewById(R.id.uploadProgressBar);
            convertView.setTag(holder);
//            ViewTreeObserver vto = convertView.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new LayoutListener(convertView));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ((SwipeListView) parent).recycle(convertView, position);

        holder.ivImage.setImageDrawable(item.getIcon());
        holder.title.setText(item.getName());
        holder.description.setText(item.getId());
        if (item.isUploading() || item.getUploadedProgress() == 100) {
            holder.uploadBtn.setVisibility(View.GONE);
            holder.delBtn.setVisibility(View.GONE);
            holder.uploadPB.setVisibility(View.VISIBLE);
            holder.uploadPB.setProgress(item.getUploadedProgress());
        } else {
            holder.uploadBtn.setVisibility(View.VISIBLE);
            holder.delBtn.setVisibility(View.VISIBLE);
            holder.uploadPB.setVisibility(View.GONE);
        }
        holder.uploadPB.setProgress(item.getCurrentProgress());
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCallback.dismiss(position);
            }
        });

        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCallback.startProgress(item);
            }
        });
//        Log.d(TAG, String.format("test--%d--%s--%s--%s---", position, holder.title.getText(), holder.title,
//                holder.uploadPB));

        return convertView;
    }

//    private class LayoutListener implements OnGlobalLayoutListener {
//        View parentView;
//
//        public LayoutListener(View view) {
//            this.parentView = view;
//        }
//
//        @Override
//        public void onGlobalLayout() {
//            parentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            Button delBtn = (Button) parentView.findViewById(R.id.delBorrowerBtn);
//            Button uploadBtn = (Button) parentView.findViewById(R.id.uploadBorrowerBtn);
//            float offset = delBtn.getMeasuredWidth() + uploadBtn.getMeasuredWidth();
//            Log.d(TAG, "onGlobalLayout: " + offset);
//            if (offset > 0) {
//                updateCallback.setSwipeOffset(offset);
//            }
//        }
//    }

    public static class ViewHolder {
        ImageView ivImage;
        TextView title;
        TextView description;
        Button delBtn;
        Button uploadBtn;
        ProgressBar uploadPB;

        public Button getDelBtn() {
            return delBtn;
        }

        public Button getUploadBtn() {
            return uploadBtn;
        }

        public ProgressBar getUploadPB() {
            return uploadPB;
        }

        public TextView getTitle() {
            return title;
        }
    }

}
