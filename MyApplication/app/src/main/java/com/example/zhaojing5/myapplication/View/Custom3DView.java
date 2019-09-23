package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class Custom3DView extends ViewGroup {

    public static final String TAG = Custom3DView.class.getSimpleName();

    private Camera mCamera;
    private Matrix mMatrix;
    private int mStartScreen = 1;//开始时的item位置
    private float mDownY = 0;
    private static final int standerSpeed = 2000;
    private int mCurScreen = 1;//当前item的位置
    private  int mHeight = 0;//控件的高
    private VelocityTracker mTracker;
    private Scroller mScroller;
    // 旋转的角度，可以进行修改来观察效果
    private float angle = 90;
    //三种状态
    private static final int STATE_PRE = 0;
    private static final int STATE_NEXT = 1;
    private static final int STATE_NORMAL = 2;
    private int STATE = -1;
    private float resistance = 1.6f;//滑动阻力

    public Custom3DView(Context context) {
        super(context);
        init();
    }

    public Custom3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom3DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mCamera = new Camera();
        mMatrix = new Matrix();
        if(mScroller == null){
            mScroller = new Scroller(getContext(),new LinearInterpolator());
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //measure father
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth,measureHeight);

        //measure children
        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth - (params.leftMargin + params.rightMargin),MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight - (params.topMargin + params.bottomMargin),MeasureSpec.EXACTLY);
        measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);

        //scroll to first child
        mHeight = getMeasuredHeight();
        scrollTo(0,mStartScreen * mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //layout in turn.

        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();

        int childTop = 0;

        for(int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE){
                if(i == 0){
                    childTop += params.topMargin;
                }
                child.layout(params.leftMargin,childTop,
                        child.getMeasuredWidth() + params.leftMargin,
                        child.getMeasuredHeight() + childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
/*
    // FIXME: 2018/7/31 principle
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                return false;
        }
        return true;
    }*/

    //由于onDraw中调用dispatchDraw，而onDraw只有在需要绘制背景时调用，所以保险起见绘制子布局需要重写dispatchDraw
    @Override
    protected void dispatchDraw(Canvas canvas) {
        for(int i = 0; i < getChildCount(); i++){
            drawChildren(canvas, i, getDrawingTime());
        }
    }

    private void drawChildren(Canvas canvas, int screen, long drawingTime){
        final int height = getHeight();
        final int scrollHeight = screen * height;
        final int scrollY = this.getScrollY();

        if(scrollHeight > scrollY + height || scrollHeight + height < scrollY){
            return;
        }

        final View child = getChildAt(screen);
        final int faceIndex = screen;
        final float currentDegree = getScrollY() * (angle / getMeasuredHeight());
        final float faceDegree = currentDegree - faceIndex * angle;
        if(faceDegree > 90 || faceDegree < -90){
            return;
        }
        final float centerY = (scrollHeight < scrollY) ? scrollHeight + height : scrollHeight;
        final float centerX = getWidth() / 2;
        final Camera camera = mCamera;
        final Matrix matrix = mMatrix;
        canvas.save();
        camera.save();
        camera.rotateX(faceDegree);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);
        canvas.concat(matrix);
        drawChild(canvas,child,drawingTime);
        canvas.restore();
        Log.i(TAG,"dispatchDraw screen is " + screen +
                        " getScrollY() is " + scrollY +
                " faceDegree is " + faceDegree +
                " (centerX,centerY) is (" + centerX + "," + centerY + ")");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTracker(event);
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if( !mScroller.isFinished() ){
                    mScroller.setFinalY(mScroller.getCurrY());
                    mScroller.abortAnimation();
                    scrollTo(0,getScrollY());
                }
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int disY = (int) (mDownY - y);
                mDownY = y;
                if(mScroller.isFinished()){
                    recycleMove(disY);
                }
                break;
            case MotionEvent.ACTION_UP:
                mTracker.computeCurrentVelocity(1000);
                float velocitY = mTracker.getYVelocity();
                if(velocitY > standerSpeed || ( ( getScrollY() + mHeight / 2 ) / mHeight < mStartScreen) ){
                    STATE = STATE_PRE;
                }else if(velocitY < -standerSpeed  || ((getScrollY() + mHeight / 2) / mHeight > mStartScreen)){
                    //滑动的速度大于规定的速度，或者向下滑动时，下一页页面展现出的高度超过1/2。则设定状态为STATE_NEXT
                    STATE =  STATE_NEXT;
                }else{
                    STATE =  STATE_NORMAL;
                }
                //根据stat进行相应的变化
                changeByState();
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void recycleMove(int delta){
        delta = delta % mHeight;
        delta = (int) (delta / resistance);
        if(Math.abs(delta) > mHeight / 4){
            return;
        }
        if(getScrollY() <= 0 && mCurScreen <= 0 && delta <= 0){
            return;
        }
        if(mHeight * mCurScreen <= getScrollY() && mCurScreen == getChildCount() - 1 && delta >= 0){
            return;
        }
        scrollBy(0,delta);

        if(getScrollY() < 8 && mCurScreen != 0){
            setPre();
            scrollBy(0,mHeight);
        }else if(getScrollY() > (getChildCount() - 1) * mHeight - 8){
            setNext();
            scrollBy(0,-mHeight);
        }
    }

    private void setNext(){
        mCurScreen = (mCurScreen + 1) % getChildCount();

        int childCount = getChildCount();
        View view = getChildAt(0);
        removeViewAt(0);
        addView(view,childCount - 1);
    }

    private void setPre(){
        mCurScreen = ( (mCurScreen - 1) + getChildCount() ) % getChildCount();

        int childCount = getChildCount();
        View view = getChildAt(childCount - 1);
        removeViewAt(childCount - 1);
        addView(view,0);
    }

    private void initVelocityTracker(MotionEvent event){
        if(mTracker == null){
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
    }

    private void recycleVelocityTracker(){
        if(mTracker != null){
            mTracker.recycle();
            mTracker = null;
        }
    }

    private void changeByState(){
        switch (STATE){
            case STATE_NORMAL:
                toNormalAction();
                break;
            case STATE_PRE:
                toPrePager();
                break;
            case STATE_NEXT:
                toNextPager();
                break;
        }
        invalidate();
    }

    private void toNormalAction(){
        int startY;
        int delta;
        int duration;
        STATE = STATE_NORMAL;
        startY = getScrollY();

        delta = mHeight * mStartScreen - getScrollY();
        duration = (Math.abs(delta)) * 4;
        mScroller.startScroll(0, startY,0, delta, duration);
    }

    private void toPrePager(){
        int startY;
        int delta;
        int duration;
        STATE = STATE_PRE;
        setPre();
        startY = getScrollY() + mHeight;
        setScrollY(startY);
        delta = -(startY - mStartScreen * mHeight);
        duration = (Math.abs(delta)) * 2;
        mScroller.startScroll(0, startY, 0, delta, duration);
    }

    private void toNextPager(){
        int startY;
        int delta;
        int duration;
        STATE = STATE_NEXT;
        setNext();
        startY = getScrollY() - mHeight;
        setScrollY(startY);
        delta = mHeight * mStartScreen - startY;
        duration = (Math.abs(delta)) * 2;
        mScroller.startScroll(0,startY,0,delta,duration);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
