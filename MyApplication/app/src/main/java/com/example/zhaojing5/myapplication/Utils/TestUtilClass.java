package com.example.zhaojing5.myapplication.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUtilClass{

    public static final String TAG = TestUtilClass.class.getSimpleName();

    /**
     * tool class don`t want to be instanced
     */
    private TestUtilClass(){
        throw new AssertionError();
    }

    private void testExecutorService(){
        /**
         * 池中线程数量固定，适用于服务端很稳定，很固定的正规并发线程
         * 使用无界的LinkedBlockingQueue
         */
        ExecutorService pool = Executors.newFixedThreadPool(4);
        /**
         * 池中线程是随着处理数据增加而增加，
         * 有空闲且未到空闲截至时间的重复利用，否则创建新线程并放入池中
         * 用于执行一些生命周期很短的异步任务，不适用于io等长延时的操作
         * 使用SynchronousQueue作为阻塞队列，如果有新任务进去队列，队列中数据必须被其他线程处理，
         * 否则会等待
         */
        ExecutorService pool1 = Executors.newCachedThreadPool();
        /**
         * 池中只有一个线程在执行，只用于有明确执行顺序但是不影响主线程的任务，压入池中的任务
         * 会按照队列顺序执行。
         * 使用无界的LinkedBlockingQueue
         */
        ExecutorService pool2 = Executors.newSingleThreadExecutor();
    }

    private void testLineNumberReader(){
        InputStream in = TestUtilClass.class.getResourceAsStream("");
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(in));
        String line;
        try{
            while( !TextUtils.isEmpty(( line = lineNumberReader.readLine() ) ) ){
                Log.i(TAG,"line number is " + lineNumberReader.getLineNumber() + " : " + line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable{
        try{
            //finalize subclass state
        }finally {
            super.finalize();
            //remember to call father`s finalize function manually
        }

    }


    public class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"onPreExecute begin AsyncTask...");
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //change progress view in UI
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //show final result in UI
        }
    }

}
