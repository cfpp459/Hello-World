package com.example.zhaojing5.myapplication.apiUtils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.example.zhaojing5.myapplication.R;

public class apiUtils {

    public static String getImei(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(checkReadTelephonePermission(context)){
            return telephonyManager.getImei();
        }else{
            //request permission

        }
        return "";
    }

    public static boolean checkReadTelephonePermission(Context context){
        return (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
    }

    public void drawRoundImage(Canvas canvas,Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,displayMetrics);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,displayMetrics);
        Matrix matrix = new Matrix();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        float scale = height/bmpHeight < width/bmpWidth ? width/bmpWidth : height/bmpHeight;
        matrix.postScale(scale,scale);
        matrix.postTranslate(-(bmpWidth*scale/2 - width/2),-(bmpHeight*scale/2-height/2));
        BitmapShader shader = new BitmapShader(bmp, Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
        shader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        if(width == height){
            canvas.drawCircle(width/2,height/2,width < height ? width/2 : height/2,paint);
        }else{
            canvas.drawRoundRect(new RectF(10,10,width-10,height-10),10,10,paint);
        }
    }

    public void multiColorRoundImage(Canvas canvas,Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,displayMetrics);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,displayMetrics);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(width/2,height/2,width/2 - 20/2,paint);
        int[] arcColor = new int[]{
            0xFF09F68C,
            0xFFB0F44B,
            0xFFE8DD30,
            0xFFF1CA2E,
            0xFFFF902F,
            0xFFFF6433,
            0xFF09F68C
        };
        SweepGradient sweepGradient = new SweepGradient(width/2,height/2,arcColor,null);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        PathEffect pathEffect = new DashPathEffect(new float[]{2,4,8,16},20);
        paint.setPathEffect(pathEffect);
        paint.setShader(sweepGradient);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF(10,10,width-10,height-10),-90,270,false,paint);
    }
}
