package com.example.zhaojing5.myapplication.modelPattern;

import android.util.Log;

public abstract class Formatter {
    private static final String TAG = "Formatter";
    public static int XML = 0;
    public static int JSON = 1;

    public String formatBook(BookBean bookBean){
        beforeFormat();
        String result = formattting(bookBean);
        afterFormat();
        return result;
    }

    private void beforeFormat(){
        Log.d(TAG,"beforeFormat...");
    }

    protected abstract String formattting(BookBean bookBean);

    private void afterFormat(){
        Log.d(TAG,"afterFormat...");
    }

}
