package com.stackbase.mobapp.view.adapters;

public interface IUpdateCallback {
    // return the progress
    void startProgress(SwipeListViewItem item);

    void dismiss(int position);

}
