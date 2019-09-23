package com.example.zhaojing5.myapplication.floatWindow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.R;

import java.lang.reflect.Field;
import java.util.zip.Inflater;

public class FloatWindowSmallView extends LinearLayout {
    public static final String TAG = "FloatWindowSmallView";
    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small,this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView percentView = findViewById(R.id.percent);
        percentView.setText(MywindowManager.getUsedPercentValue(context));
    }

    public FloatWindowSmallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatWindowSmallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FloatWindowSmallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int getStatusBarHeight(){
        if(statusBarHeight == 0){
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer)field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"ontouchEvent is " + event.getAction());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if(xDownInScreen == xInScreen && yDownInScreen == yInScreen){
                    openBigWindow();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void updateViewPosition(){
        mParams.x = (int)(xInScreen - xInView);
        mParams.y = (int)(yInScreen - yInView);
        Log.d(TAG,"uodateViewPosition x,y is " + mParams.x + "," + mParams.y);
        windowManager.updateViewLayout(this,mParams);
    }

    private void openBigWindow(){
        MywindowManager.createBigWindow(getContext());
        MywindowManager.removeSmallWindow(getContext());
    }

    public void setParams(WindowManager.LayoutParams mParams){
        this.mParams = mParams;
    }
}
