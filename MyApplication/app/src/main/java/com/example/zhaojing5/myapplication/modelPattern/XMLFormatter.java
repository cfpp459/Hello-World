package com.example.zhaojing5.myapplication.modelPattern;

import android.util.Log;

public class XMLFormatter extends Formatter {
    public static final String TAG = "XmlFormatter";
    @Override
    public String formattting(BookBean bookBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<book_name>").append(bookBean.getBookName()).append("</book_name>")
                .append("<pages>").append(bookBean.getPages()).append("</pages>")
                .append("<book_id>").append(bookBean.getBookId()).append("</book_id>");
        Log.d(TAG,stringBuilder.toString());
        return stringBuilder.toString();
    }
}
