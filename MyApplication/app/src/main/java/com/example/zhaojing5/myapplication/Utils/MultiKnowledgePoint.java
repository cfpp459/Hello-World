package com.example.zhaojing5.myapplication.Utils;

import android.content.Context;
import android.util.Log;

import com.example.zhaojing5.myapplication.modelPattern.BookBean;
import com.example.zhaojing5.myapplication.modelPattern.JsonFormatter;
import com.example.zhaojing5.myapplication.modelPattern.XMLFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * created by zhaojing 2019/12/31 下午5:26
 */
public class MultiKnowledgePoint {

    public static final String TAG = MultiKnowledgePoint.class.getSimpleName();

    /***
     * u year
     * y year-of-era
     * Y week-based-year
     */
    public static void testWeakYear(){
        String formatString = "YYYY-MM-DD";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, Locale.CHINA);
        String formatDateString = simpleDateFormat.format(new Date());
        Log.d("Elizabeth","YYYY-MM-DD formatDateString is " + formatDateString);

        String formatString1 = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(formatString1, Locale.CHINA);
        String formatDateString1 = simpleDateFormat1.format(new Date());
        Log.d("Elizabeth","yyyy-MM-dd formatDateString1 is " + formatDateString1);

        String formatString2 = "uuuu-MM-dd";
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(formatString2, Locale.CHINA);
        String formatDateString2 = simpleDateFormat2.format(new Date());
        Log.d("Elizabeth","uuuu-MM-dd formatDateString2 is " + formatDateString2);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("Elizabeth","Calendar date string is " + year + "-" + month + "-" + day);
    }

    public static void testFileVisitor(Context context){

        if (context == null) {
            throw new RuntimeException("context should not be null...");
        }

        try {
            Files.walkFileTree(FileUtils.getPhoneRootPath(context), new FileVisitorUtil());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void testCloneable(){

        int[] randomValue = new int[]{10,2,3,32,15,76,4};
        int[] randonValueClone = randomValue.clone();

        Log.i(TAG,"randonValue is " + randomValue);
        for (int randomValueEach : randomValue) {
            Log.i(TAG,"randonValue is " + randomValueEach);
        }

        Log.i(TAG,"randonValue cloneable is " + randonValueClone.toString());
        for (int randonValueCloneEach : randonValueClone) {
            Log.i(TAG,"randonValueClone is " + randonValueCloneEach);
        }

    }

    public static void testModelPattern(){
        //modelPattern
        Log.d(TAG,"--->testModelPattern");
        BookBean bookBean = new BookBean();
        bookBean.setBookName("book1");
        bookBean.setBookId("12");
        bookBean.setPages(101);
        XMLFormatter xmlFormatter = new XMLFormatter();
        xmlFormatter.formatBook(bookBean);
        JsonFormatter jsonFormatter = new JsonFormatter();
        jsonFormatter.formatBook(bookBean);
    }


}
