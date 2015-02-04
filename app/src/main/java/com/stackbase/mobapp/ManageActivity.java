package com.stackbase.mobapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.stackbase.mobapp.activity.PreferencesActivity;
import com.stackbase.mobapp.objects.Borrower;
import com.stackbase.mobapp.utils.BitmapUtilities;
import com.stackbase.mobapp.utils.Constant;
import com.stackbase.mobapp.utils.Helper;
import com.stackbase.mobapp.view.adapters.IUpdateCallback;
import com.stackbase.mobapp.view.adapters.SwipeListViewAdapter;
import com.stackbase.mobapp.view.adapters.SwipeListViewItem;
import com.stackbase.mobapp.view.swipelistview.BaseSwipeListViewListener;
import com.stackbase.mobapp.view.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends Activity implements IUpdateCallback {
    private static final String TAG = ManageActivity.class.getSimpleName();

    private static final int REQUEST_CODE_SETTINGS = 0;
    private static final int REQUEST_ID_CHANGE = 1;
    private SwipeListViewAdapter adapter;
    private List<SwipeListViewItem> data;
    private SwipeListView swipeListView;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrower_list);
        data = new ArrayList<SwipeListViewItem>();
        adapter = new SwipeListViewAdapter(this, data);
        adapter.setUpdateCallback(this);
        swipeListView = (SwipeListView) findViewById(R.id.swipe_list_view);
        swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            swipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//                @Override
//                public void onItemCheckedStateChanged(ActionMode mode, int position,
//                                                      long id, boolean checked) {
//                    mode.setTitle("Selected (" + swipeListView.getCountSelected() + ")");
//                }
//                @Override
//                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.menu_delete:
//                            swipeListView.dismissSelected();
//                            mode.finish();
//                            return true;
//                        default:
//                            return false;
//                    }
//                }
//                @Override
//                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                    MenuInflater inflater = mode.getMenuInflater();
//                    inflater.inflate(R.menu.menu_choice_items, menu);
//                    return true;
//                }
//                @Override
//                public void onDestroyActionMode(ActionMode mode) {
//                    swipeListView.unselectedChoiceStates();
//                }
//                @Override
//                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                    return false;
//                }
//            });
//        }
        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                Log.d(TAG, String.format("onOpened %d - toRight %b", position, toRight));
                setListOffset(position, toRight);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d(TAG, String.format("onStartOpen %d - action %d", position, action));
                if (action == SwipeListView.SWIPE_ACTION_REVEAL) {
                    setListOffset(position, right);
                }
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d(TAG, String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d(TAG, String.format("onClickFrontView %d", position));
                //Show the detail
                Intent intent = new Intent();
                intent.setClass(ManageActivity.this, CollectActivity.class);
                String jsonFile = ((SwipeListViewItem) swipeListView.getAdapter().getItem(position)).getIdFileName();
                intent.putExtra(Constant.INTENT_KEY_ID_JSON_FILENAME, jsonFile);
                startActivityForResult(intent, REQUEST_ID_CHANGE);
            }

            @Override
            public void onClickBackView(int position) {
                Log.d(TAG, String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    Log.d(TAG, String.format("onDismiss %d", position));
                    data.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        swipeListView.setAdapter(adapter);
        reload();
        new ListAppTask().execute();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void setListOffset(int position, boolean toRight) {
        // Set the offset
        View item = getItemViewByPosition(position, swipeListView);
        SwipeListViewAdapter.ViewHolder holder = (SwipeListViewAdapter.ViewHolder) item.getTag();
        float offset = holder.getDelBtn().getMeasuredWidth() + holder.getUploadBtn().getMeasuredWidth();
        if (toRight) {
            swipeListView.setOffsetRight(convertDpToPixel(offset));
        } else {
            swipeListView.setOffsetLeft(convertDpToPixel(offset));
        }

    }

    private View getItemViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void reload() {
//        SettingsManager settings = SettingsManager.getInstance();
//        swipeListView.setSwipeMode(settings.getSwipeMode());
//        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
//        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
//        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
//        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
//        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
//        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_app, menu);
//        return true;
//    }
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        boolean handled = false;
//        switch (item.getItemId()) {
//            case android.R.id.home: //Actionbar home/up icon
//                finish();
//                break;
//            case R.id.menu_settings:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
//                break;
//        }
//        return handled;
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_SETTINGS:
                reload();
                break;
            case REQUEST_ID_CHANGE:
                Borrower borrower = (Borrower) intent.getSerializableExtra(Constant.INTENT_KEY_BORROWER_OBJ);
                if (borrower != null) {
                    // Do not allow to change the id and name
//                    for (SwipeListViewItem item : data) {
//                        if (item.getId().equals(borrower.getId())) {
//                            item.setName(borrower.getName());
//                            break;
//                        }
//                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }

    void updateProgressInUiThread(SwipeListViewItem model,int progress,int position){
        updateProgressPartly(progress,position);
    }

    private void updateProgressPartly(int progress,int position){
        int firstVisiblePosition = swipeListView.getFirstVisiblePosition();
        int lastVisiblePosition = swipeListView.getLastVisiblePosition();
        if(position>=firstVisiblePosition && position<=lastVisiblePosition){
            View view = swipeListView.getChildAt(position - firstVisiblePosition);
            if(view.getTag() instanceof SwipeListViewAdapter.ViewHolder){
                SwipeListViewAdapter.ViewHolder vh = (SwipeListViewAdapter.ViewHolder)view.getTag();
                vh.getUploadPB().setProgress(progress);
            }
        }
    }

    @Override
    public int startProgress(final int position, final SwipeListViewItem item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i<=100;i++){
                    updateProgressInUiThread(item, i, position);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Upload borrower error", e);
                    }
                }
                dismiss(position);
                //TODO: or send message to notification center.
                Helper.mMakeTextToast(ManageActivity.this, getString(R.string.upload_finished), true);
            }
        }).start();

        //TODO: need return the current progress when fail to upload all the borrower's info
        return 0;
    }

    @Override
    public void dismiss(int position) {
        SwipeListViewItem item = (SwipeListViewItem) swipeListView.getAdapter().getItem(position);
        Log.d(TAG, "Delete: " + item.getIdFileName());
        Helper.deleteBorrower(item.getIdFileName());
        data.remove(item);
        swipeListView.dismiss(position);
    }


    public class ListAppTask extends AsyncTask<Void, Void, List<SwipeListViewItem>> {
        protected List<SwipeListViewItem> doInBackground(Void... args) {
            List<SwipeListViewItem> data = new ArrayList();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ManageActivity.this);
            String rootDir = prefs.getString(PreferencesActivity.KEY_STORAGE_DIR,
                    Constant.DEFAULT_STORAGE_DIR);
            for (Borrower borrower : Helper.loadBorrowersInfo(rootDir)) {
                SwipeListViewItem item = new SwipeListViewItem();
                item.setName(borrower.getName());
                item.setId(borrower.getId());
                item.setIdFileName(borrower.getJsonFile());
                Bitmap icon = null;
                if (borrower.getIdPicture1() != null && !borrower.getIdPicture1().equals("")) {
                    icon = BitmapUtilities.getBitmapThumbnail(BitmapUtilities.getBitmap(
                            borrower.getIdPicture1()), 50, 50);
                }
                if (icon != null) {
                    item.setIcon(new BitmapDrawable(getResources(), icon));
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_stranger));
                }
                data.add(item);
            }
            return data;
        }

        protected void onPostExecute(List<SwipeListViewItem> result) {
            data.clear();
            data.addAll(result);
            adapter.notifyDataSetChanged();
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
