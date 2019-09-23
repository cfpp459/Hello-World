package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;

public class DisallowInterceptButton extends AppCompatButton {

    public static final String TAG = DisallowInterceptButton.class.getSimpleName();
    public DisallowInterceptButton(Context context) {
        super(context);
    }

    public DisallowInterceptButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisallowInterceptButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"--->button dispatchTouchEvent!");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"-->button onTouchEvent event is " + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
                this.performClick();
                Log.d(TAG,"button trigger down!");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"button trigger move!");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"button trigger up!");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG,"when has been intercapted,receives action_cancel!");
                break;
        }
        return true;
    }

    @Override
    public ViewPropertyAnimator animate() {
        return super.animate();
    }
}
