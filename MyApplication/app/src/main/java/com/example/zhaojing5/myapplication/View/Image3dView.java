package com.example.zhaojing5.myapplication.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Image3dView extends View {

    /**
     * 原视图，用于生成图片对象
     */
    private View sourceView;

    /**
     * 根据传入的原视图生成的图片对象
     */
    private Bitmap sourceBitmap;

    /**
     * 源视图的宽度。
     */
    private float sourceWidth;

    /**
     * Matrix对象，用于对图片进行矩阵操作。
     */
    private Matrix matrix = new Matrix();

    /**
     * Camera对象，用于对图片进行三维操作。
     */
    private Camera camera = new Camera();

    public Image3dView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 提供外部接口，允许向Image3dView传入原视图
     * @param view
     */
    public void setSourceView(View view){
        sourceView = view;
        sourceWidth = sourceView.getWidth();
    }

    public void clearSourceBitmap(){
        if(sourceBitmap != null){
            sourceBitmap = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(sourceBitmap == null){
            getSourceBitmap();
        }
        float degree = 90 - (90 / sourceWidth) * getWidth();
        camera.save();
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();

        //将旋转的中心点移动到屏幕左边缘的中间位置
        matrix.preTranslate(0,- getHeight() / 2);
        matrix.postTranslate(0,getHeight() / 2);
        canvas.drawBitmap(sourceBitmap,matrix,null);


    }

    private void getSourceBitmap(){
        if(sourceView != null){
            sourceView.setDrawingCacheEnabled(true);
            sourceView.layout(0,0,sourceView.getWidth(),sourceView.getHeight());
            sourceView.buildDrawingCache();
            sourceBitmap = sourceView.getDrawingCache();

        }
    }
}
