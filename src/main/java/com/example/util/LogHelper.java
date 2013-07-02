package com.example.util;

import android.util.Log;

/**
 * Created by dingdj on 13-7-2.
 */
public class LogHelper {

    static boolean DEBUG = true;

    public static void v(String TAG, String msg){
        if(DEBUG){
            Log.v(TAG, msg);
        }
    }

    public static void w(String TAG, String msg){
        if(DEBUG){
            Log.w(TAG, msg);
        }
    }

    public static void e(String TAG, String msg){
        if(DEBUG){
            Log.e(TAG, msg);
        }
    }

    public static void i(String TAG, String msg){
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }

    public static void d(String TAG, String msg){
        if(DEBUG){
            Log.d(TAG, msg);
        }
    }

}
