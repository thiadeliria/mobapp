package com.stackbase.mobapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gengjh on 1/17/15.
 */
abstract public class Helper {

    private static final String TAG = Helper.class.getSimpleName();

    /**
     * Displays an error message dialog box to the user on the UI thread.
     *
     * @param title   The title for the dialog box
     * @param message The error message to be displayed
     */
    public static void showErrorMessage(Context context, String title, String message,
                                        DialogInterface.OnCancelListener cancelListener,
                                        DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setOnCancelListener(cancelListener)
                .setPositiveButton("确认", positiveListener)
                .show();
    }

    /**
     * Finds the proper location on the SD card where we can save files.
     */
    public static File getStorageDirectory(Context context, ErrorCallback callback) {
        //Log.d(TAG, "getStorageDirectory(): API level is " + Integer.valueOf(android.os.Build.VERSION.SDK_INT));

        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (RuntimeException e) {
            Log.e(TAG, "Is the SD card visible?", e);
            if (callback != null) {
                callback.onErrorTaken("错误", "需要的外部存储(例如: SD卡)不可用.");
            }
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            // We can read and write the media
            //    	if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) > 7) {
            // For Android 2.2 and above

            try {
                return context.getExternalFilesDir(Environment.MEDIA_MOUNTED);
            } catch (NullPointerException e) {
                // We get an error here if the SD card is visible, but full
                Log.e(TAG, "External storage is unavailable");
                if (callback != null) {
                    callback.onErrorTaken("错误", "需要的外部存储(例如: SD卡)不可用或者已经没有可用空间.");
                }
            }

            //        } else {
            //          // For Android 2.1 and below, explicitly give the path as, for example,
            //          // "/mnt/sdcard/Android/data/edu.sfsu.cs.orange.ocr/files/"
            //          return new File(Environment.getExternalStorageDirectory().toString() + File.separator +
            //                  "Android" + File.separator + "data" + File.separator + getPackageName() +
            //                  File.separator + "files" + File.separator);
            //        }

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.e(TAG, "External storage is read-only");
            if (callback != null) {
                callback.onErrorTaken("错误", "需要的外部存储(例如: SD卡)是只读的, 无法存储数据.");
            }
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            Log.e(TAG, "External storage is unavailable");
            if (callback != null) {
                callback.onErrorTaken("错误", "需要的外部存储(例如: SD卡)不可用或者已经损坏.");
            }
        }
        return null;
    }

    public interface ErrorCallback {
        /**
         * Called when hint error
         *
         * @param title,   error title
         * @param message, error message
         */
        void onErrorTaken(String title, String message);
    }

    public static String getMD5String(String source) {
        String result = source;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

//    public static
}
