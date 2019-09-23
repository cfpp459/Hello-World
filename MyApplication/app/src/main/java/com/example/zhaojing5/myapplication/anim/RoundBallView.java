package com.example.zhaojing5.myapplication.anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.RadioGroup;

public class RoundBallView extends View {
    private Paint mPaint;
    public static final String TAG = RoundBallView.class.getSimpleName();
    private Point currentPoint;
    private static final float RADIUS = 50f;

    public RoundBallView(Context context) {
        super(context);
    }

    public RoundBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(currentPoint == null){
            currentPoint = new Point(RADIUS,RADIUS);
            drawCircle(canvas);

        }else{
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas){
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x,y,RADIUS,mPaint);
    }

    private void startAnimation(){
        Point startPoint = new Point(RADIUS,RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS,getHeight() - RADIUS);
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),startPoint,endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();

            }
        });
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(5000);
        animator.start();
    }
}
