package com.stackbase.mobapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.stackbase.mobapp.utils.Constant;

import java.io.File;

public class SettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences prefs;
    private CheckBox messageNotify;
    private CheckBox messageVibrate;
    private TextView spaceUsage;

    private static final int MSG_WHAT_CALCULATE_USAGE = 1;
    private static final String MSG_KEY_TOTAL_SPACE = "MSG_KEY_TOTAL_SPACE";
    private static final String TAG = SettingsActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);

        initView();
	}

    @Override
    protected void onStop() {
        super.onStop();
        savePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalculateUsage task = new CalculateUsage(new MessageHandler());
        new Thread(task).start();
    }

    private void initView() {
        retrievePreferences();

        messageNotify = (CheckBox) findViewById(R.id.notify_me);
        messageNotify.setChecked(prefs.getBoolean(Constant.KEY_MESSAGE_NOTIFY,
                Constant.DEFAULT_MESSAGE_NOTIFY));
        messageVibrate = (CheckBox) findViewById(R.id.vibrate);
        messageVibrate.setChecked(prefs.getBoolean(Constant.KEY_MESSAGE_VIBRATE,
                Constant.DEFAULT_MESSAGE_VIBRATE));

        messageNotify.setOnCheckedChangeListener(this);
        messageVibrate.setOnCheckedChangeListener(this);

        spaceUsage = (TextView) findViewById(R.id.spaceUsage);
    }

    private void retrievePreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.camera_preferences, false);
    }

    private void savePreferences() {
        prefs.edit().putBoolean(Constant.KEY_MESSAGE_NOTIFY, messageNotify.isChecked()).apply();
        prefs.edit().putBoolean(Constant.KEY_MESSAGE_VIBRATE, messageVibrate.isChecked()).apply();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (messageNotify == buttonView) {
            if (!isChecked) {
                messageVibrate.setChecked(false);
            }
        } else if (messageVibrate == buttonView) {
            if (isChecked) {
                messageNotify.setChecked(true);
            }
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_CALCULATE_USAGE:
                    float space = msg.getData().getFloat(MSG_KEY_TOTAL_SPACE);
                    if (space == -1) {
                        spaceUsage.setText(getString(R.string.calculate_usage));
                    } else {
                        Log.d(TAG, "space in handler: " + space);
                        spaceUsage.setText(space + " MB");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }


    private class CalculateUsage implements Runnable {
        MessageHandler handler;

        public CalculateUsage(MessageHandler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            publishProgress(-1);
            String storageDir = prefs.getString(Constant.KEY_STORAGE_DIR, Constant.DEFAULT_STORAGE_DIR);
            Log.d(TAG, "storageDir: " + storageDir);
            File dir = new File(storageDir);
            long total = totalSize(dir);
            publishProgress(total/1024/1024);
        }

        private void publishProgress(float space) {
            Message msg = handler.obtainMessage();
            msg.what = MSG_WHAT_CALCULATE_USAGE;
            msg.getData().putFloat(MSG_KEY_TOTAL_SPACE, (Math.round(space*100))/100);
            msg.sendToTarget();
        }

        private long totalSize(File directory) {
            long length = 0;
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += totalSize(file);
            }
            return length;
        }
    }
}
