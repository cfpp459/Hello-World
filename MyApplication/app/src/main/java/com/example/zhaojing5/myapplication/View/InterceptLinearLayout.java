package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class InterceptLinearLayout extends LinearLayout {

    public static final String TAG = InterceptLinearLayout.class.getSimpleName();

    public InterceptLinearLayout(Context context) {
        super(context);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"Layout onInterceptTouchEvent() event id " + ev.getAction());
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"-->layout onTouchEvent event is " + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"you trigger down event!");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"you trigger move event!");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"you trigger up event!");
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"--->layout dispatchToucheevent");
        return super.dispatchTouchEvent(ev);
    }
}
