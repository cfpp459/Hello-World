package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.zhaojing5.myapplication.R;

public class My3DView extends ViewGroup {

    public static final String TAG = My3DView.class.getSimpleName();

    private Camera mCamera;

    private Scroller mScroller;

    private Matrix mMatrix;

    private int currentIndex = 0;

    private int mHeight;

    private int ANGLE0 = 90;

    private int ANGLE1 = 270;

    private int picNum = 0;

    private int picRes[];

    private ImageView img0,img1;

    public My3DView(Context context) {
        super(context);
        init(null);
    }

    public My3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public My3DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public My3DView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attrs){

        mCamera = new Camera();
        mMatrix = new Matrix();
        mScroller = new Scroller(getContext(),new LinearInterpolator());

        if(attrs != null){
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.My3DView);
            picNum = typedArray.getInteger(R.styleable.My3DView_picNum,0);
        }

        if(picNum > 0){
            picRes = new int[picNum];
            picRes[0] = R.drawable.cat1;
            picRes[1] = R.drawable.cat2;
            picRes[2] = R.drawable.cat3;
            picRes[3] = R.drawable.cat4;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //measure itself
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthMeasure,heightMeasure);

        //measure children
        MarginLayoutParams childParams = (MarginLayoutParams) getLayoutParams();

        int childWidthMeasure = MeasureSpec.makeMeasureSpec(widthMeasureSpec - childParams.leftMargin - childParams.rightMargin,MeasureSpec.EXACTLY);
        int childHeightMeasure = MeasureSpec.makeMeasureSpec(heightMeasureSpec - childParams.topMargin - childParams.bottomMargin,MeasureSpec.EXACTLY);
        measureChildren(childWidthMeasure,childHeightMeasure);

        mHeight = getMeasuredHeight();
        //setMeasureDimension 之后才能调用getMeasuredHeight
    }

    /**
     * onLayout 重写的意义就是ViewGroup布局文件中包含了若干子view
     * 需要通过一定逻辑将他们安置在一定位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();

        int top = layoutParams.topMargin;

        int childCount = getChildCount();
        if(childCount > 0){
            for(int i = 0; i < childCount; i++){
                View child = getChildAt(i);
                if(child.getVisibility() == View.VISIBLE){
                    child.layout(
                            layoutParams.leftMargin,
                            top,
                            layoutParams.leftMargin + child.getMeasuredWidth(),
                            top + child.getMeasuredHeight());
                    top += child.getMeasuredHeight();
                }
            }
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
        for(int i = 0; i < 2; i++){
            drawChildren(canvas, i, getDrawingTime());
        }
    }

    private void drawChildren(Canvas canvas, int index, long drawingTime){

        int scrollY = getScrollY();
        int facedegree = 0;

        if(index == 0){
            facedegree  = ( scrollY / mHeight ) * 90;
        }else if(index == 1){
            facedegree =  270 + ( scrollY / mHeight ) * 90;
        }

//        final View child = getImgChild(index);
        final View child = getChildAt(index);

        int centerX = getWidth() / 2;
        int centerY = ( mHeight - scrollY ) / 2;

        final Camera camera = mCamera;
        final Matrix matrix = mMatrix;

        mCamera.save();
        canvas.save();

        camera.rotateX(facedegree);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);
        canvas.concat(matrix);
        drawChild(canvas,child,drawingTime);
        canvas.restore();
    }

    private View getImgChild(int index){

        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,MarginLayoutParams.WRAP_CONTENT);

        if(index == 0){
            if(img0 == null){
                img0 = new ImageView(getContext());
            }
            img0.setImageResource(picRes[currentIndex]);
            img0.setLayoutParams(params);
            return img0;
        }else{
            if(img1 == null){
                img1 = new ImageView(getContext());
            }
            img1.setImageResource(picRes[currentIndex + 1]);
            img1.setLayoutParams(params);
            return img1;
        }
    }
}
