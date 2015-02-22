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
import android.widget.TextView;

import com.stackbase.mobapp.R;
import com.stackbase.mobapp.objects.Message;
import com.stackbase.mobapp.view.swipelistview.SwipeListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageViewAdapter extends BaseAdapter {

    private static final String TAG = MessageViewAdapter.class.getSimpleName();
    private List<MessageViewItem> data;
    private Context context;
    private IMessageCallback messageCallback;

    public MessageViewAdapter(Context context, List<MessageViewItem> data) {
        this.context = context;
        this.data = data;

    }

    public IMessageCallback getMessageCallback() {
        return messageCallback;
    }

    public void setMessageCallback(IMessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MessageViewItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MessageViewItem item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.message_row, null);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.messageImage);
            holder.title = (TextView) convertView.findViewById(R.id.messageTypeText);
            holder.description = (TextView) convertView.findViewById(R.id.messageContent);
            holder.time = (TextView) convertView.findViewById(R.id.messageTime);
            holder.delBtn = (Button) convertView.findViewById(R.id.delBorrowerBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ((SwipeListView) parent).recycle(convertView, position);

        holder.ivImage.setImageDrawable(item.getIcon());
        holder.title.setText(context.getString(R.string.user_message));
        if (item.getMessageType() == Message.MessageType.SYSTEM_MESSAGE) {
            holder.title.setText(context.getString(R.string.system_message));
        }
        holder.time.setText(getFriendlyTime(item.getTime()));
        holder.description.setText(item.getContent());
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageCallback.dismiss(position);
            }
        });
        return convertView;
    }

    private String getFriendlyTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.today)+ " " + timeFormatter.format(date);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.yesterday) + " " + timeFormatter.format(date);
        } else {
            return new SimpleDateFormat("yy/MM/dd HH:mm").format(date);
        }
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
//                messageCallback.setSwipeOffset(offset);
//            }
//        }
//    }

    public static class ViewHolder {
        ImageView ivImage;
        TextView title;
        TextView description;
        TextView time;
        Button delBtn;
    }

}
