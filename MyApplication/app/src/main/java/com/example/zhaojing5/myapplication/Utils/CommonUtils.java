package com.example.zhaojing5.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

import java.math.BigDecimal;

public class CommonUtils {


    /**
     * Intent隐式调用Activity时防止抛出ActivityNotFoundException
     * 所以要先查一下该Activity是否存在
     * @param context
     * @param intent
     * @return
     */
    public static boolean isActivityExist(Context context, Intent intent){

        if (context != null && intent != null) {
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                return true;
            }
        }

        return false;
    }


    /***
     * 强转封装，捕获异常，并取消类型检查提示
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T cast(Object obj){
        try{
            return (T) obj;
        }catch (ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * get appointed size bitmap.
     * @param bitmap
     * @param srcWidth
     * @param srcHeight
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getAppointedBItmap(Bitmap bitmap, int srcWidth, int srcHeight, int newWidth , int newHeight){
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / srcWidth;
        float scaleHeight = ((float) newHeight) / srcHeight;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, matrix, true);
        return newbm;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
