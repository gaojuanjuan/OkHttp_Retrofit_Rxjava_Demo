package com.gjj.frame;

import android.util.Log;

/**
 * Created by gaojuanjuan on 2018/2/27.
 */

public class Logger {
    private static final String TAG = "gjj";
    public static void info(String content){
        Log.i("OkHttp",content);
    }
    public static void d(String content){
        Log.d(TAG,content);
    }
    public static void e(String content){
        Log.e(TAG,content);
    }


}
