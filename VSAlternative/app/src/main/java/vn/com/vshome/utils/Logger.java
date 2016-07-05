package vn.com.vshome.utils;

import android.util.Log;

/**
 * Created by anlab on 7/5/16.
 */
public class Logger {

    private static final String TAG = "dungnt";

    public static void LogD(String msg){
        if(Define.DEBUG){
            Log.d(TAG, msg);
        }
    };
}
