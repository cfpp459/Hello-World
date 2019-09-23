package com.example.zhaojing5.myapplication.modelPattern;

import android.util.Log;

public class JsonFormatter extends Formatter {
    public static final String TAG = "JsonFormatter";
    @Override
    public String formattting(BookBean bookBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{book_name:\"").append(bookBean.getBookName()).append("\",")
                .append("pages:\"").append(bookBean.getPages()).append("\",")
                .append("book_id:\"").append(bookBean.getBookId()).append("\"}");
        Log.d(TAG,stringBuilder.toString());
        return stringBuilder.toString();
    }
}
