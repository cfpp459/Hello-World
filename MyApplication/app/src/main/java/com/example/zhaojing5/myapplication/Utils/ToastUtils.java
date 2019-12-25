package com.example.zhaojing5.myapplication.Utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    private static Toast toast;
    public static synchronized void showToast(Context context,String content){
        if(toast == null){
            toast = Toast.makeText(context, content,Toast.LENGTH_SHORT);
        }else{
            toast.setText(content);
        }
        toast.show();
    }
    public static void showToast(Context context,int resId){
        showToast(context,context.getResources().getString(resId));
    }
}
