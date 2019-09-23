package com.example.zhaojing5.myapplication.multiImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Image3DView extends ImageView {
    /**
     * 旋转角度基准值
     */
    private static final float BASE_DEGREE = 50f;
    /**
     * 旋转深度基准值
     */
    private static final float BASE_DEEP = 150f;
    private Camera mCamera;
    private Matrix matrix;
    private Bitmap bitmap;
    /**
     * 当前图片对应下标值
     */
    private int mIndex;
    /**
     * 在前图片在X轴方向上滚动的距离
     */
    private int mScroolx;
    /**
     * Image3DSwitchView控件的宽度
     */
    private int mLayoutWidth;
    /**
     * 当前图片的宽度
     */
    private int mWidth;
    /**
     * 当前旋转的角度
     */
    private float mRotateDegree;
    /**
     * 旋转中心点
     */
    private float mDx;
    /**
     * 旋转的深度
     */
    private float mDeep;

    public Image3DView(Context context) {
        super(context);
        init();
    }

    public Image3DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Image3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Image3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mCamera = new Camera();
        matrix = new Matrix();
    }

     protected void initImageViewBitmap(){
        if(bitmap == null){
            setDrawingCacheEnabled(true);
            buildDrawingCache();
            bitmap = getDrawingCache();
        }
        mLayoutWidth = Image3DSwitchView.mWidth;
        mWidth = getWidth() + Image3DSwitchView.IMAGE_PADDING * 2;
     }

    /**
     * 设置旋转角度
     * @param index 当前图片下标
     * @param scrollx
     */
    public void setRotateData(int index,int scrollx){
        mIndex = index;
        mScroolx = scrollx;
    }

    /**
     * 回收当前bitmao对象，以释放内存
     */
    public void recycleBitmap(){
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
    }

    public void setImageResource(int resId){
        setImageResource(resId);
        bitmap = null;
        initImageViewBitmap();
    }

    public void setImageBitmap(Bitmap bitmap){
        setImageBitmap(bitmap);
        bitmap = null;
        initImageViewBitmap();
    }

    public void setImageDrawable(Drawable drawable){
        super.setImageDrawable(drawable);
        bitmap = null;
        initImageViewBitmap();
    }

    public void setImageURI(Uri uri){
        super.setImageURI(uri);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap == null){
            super.onDraw(canvas);
        }else{
            if(isImageVisible()){
                computeRotateData();
                mCamera.save();
                mCamera.translate(0.0f,0.0f,mDeep);
                mCamera.rotateY(mRotateDegree);
                mCamera.getMatrix(matrix);
                mCamera.restore();
                matrix.preTranslate(-mDx,-getHeight() / 2);
                matrix.postTranslate(mDx,getHeight() / 2);
                canvas.drawBitmap(bitmap,matrix,null);
            }
        }
    }

    private void computeRotateData(){
        float degreePerPix = BASE_DEGREE / mWidth;
        float deepPerPix = BASE_DEEP / ((mLayoutWidth - mWidth) / 2);
        switch (mIndex){
            case 0:
                mDx = mWidth;
                mRotateDegree = 360f - (2 * mWidth + mScroolx) * degreePerPix;
                if (mScroolx < -mWidth) {
                    mDeep = 0;
                } else {
                    mDeep = (mWidth + mScroolx) * deepPerPix;
                }
                    break;
            case 1:
                if (mScroolx > 0) {
                    mDx = mWidth;
                    mRotateDegree = (360f - BASE_DEGREE) - mScroolx * degreePerPix;
                    mDeep = mScroolx * deepPerPix;
                } else {
                    if (mScroolx < -mWidth) {
                        mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                        mRotateDegree = (-mScroolx - mWidth) * degreePerPix;
                    } else {
                        mDx = mWidth;
                        mRotateDegree = 360f - (mWidth + mScroolx) * degreePerPix;
                    }
                    mDeep = 0;
                }
                break;
            case 2:
                if (mScroolx > 0) {
                    mDx = mWidth;
                    mRotateDegree = 360f - mScroolx * degreePerPix;
                    mDeep = 0;
                    if (mScroolx > mWidth) {
                        mDeep = (mScroolx - mWidth) * deepPerPix;
                    }
                } else {
                    mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                    mRotateDegree = -mScroolx * degreePerPix;
                    mDeep = 0;
                    if (mScroolx < -mWidth) {
                        mDeep = -(mWidth + mScroolx) * deepPerPix;
                    }
                }
                break;
            case 3:
                if (mScroolx < 0) {
                    mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                    mRotateDegree = BASE_DEGREE - mScroolx * degreePerPix;
                    mDeep = -mScroolx * deepPerPix;
                } else {
                    if (mScroolx > mWidth) {
                        mDx = mWidth;
                        mRotateDegree = 360f - (mScroolx - mWidth) * degreePerPix;
                    } else {
                        mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                        mRotateDegree = BASE_DEGREE - mScroolx * degreePerPix;
                    }
                    mDeep = 0;
                }
                break;
            case 4:
                mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                mRotateDegree = (2 * mWidth - mScroolx) * degreePerPix;
                if (mScroolx > mWidth) {
                    mDeep = 0;
                } else {
                    mDeep = (mWidth - mScroolx) * deepPerPix;
                }
                break;
        }
    }

    private boolean isImageVisible(){
        boolean isVisible = false;
        switch(mIndex){
            case 0:
                if(mScroolx < (mLayoutWidth - mWidth) / 2 - mWidth){
                    isVisible = true;
                }else{
                    isVisible = false;
                }
                break;
            case 1:
                if (mScroolx > (mLayoutWidth - mWidth) / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 2:
                if (mScroolx > mLayoutWidth / 2 + mWidth / 2
                        || mScroolx < -mLayoutWidth / 2 - mWidth / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 3:
                if (mScroolx < -(mLayoutWidth - mWidth) / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 4:
                if (mScroolx > mWidth - (mLayoutWidth - mWidth) / 2) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
                break;
        }
        return isVisible;
    }
}
