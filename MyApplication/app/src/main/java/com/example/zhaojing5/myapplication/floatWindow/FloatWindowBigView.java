package com.example.zhaojing5.myapplication.floatWindow;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.zhaojing5.myapplication.R;

public class FloatWindowBigView extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big,this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = findViewById(R.id.close);
        Button back = findViewById(R.id.back);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MywindowManager.removeSmallWindow(context);
                MywindowManager.removeBigWindow(context);
                Intent intent = new Intent(getContext(),FloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击返回的时候，移除打悬浮窗，创建小的悬浮窗
                MywindowManager.removeBigWindow(context);
                MywindowManager.createSmallWindow(context);
            }
        });
    }

    public FloatWindowBigView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatWindowBigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FloatWindowBigView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }




}
