package com.stackbase.mobapp.view.adapters;

public interface IUpdateCallback {
    // return the progress
    int startProgress(int position, SwipeListViewItem item);

    void dismiss(int position);
}
