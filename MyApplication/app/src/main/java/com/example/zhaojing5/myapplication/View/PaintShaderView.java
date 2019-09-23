package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.Utils.CommonUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class PaintShaderView extends View implements View.OnClickListener{

    public static final String TAG = PaintShaderView.class.getSimpleName();

    private Paint mPaint;

    private Rect mBound;

    int mCount;

    private Matrix mGradientMatrix;

    private LinearGradient mLinearGradient;

    private int mTranslateX;

    public PaintShaderView(Context context) {
        super(context);
    }

    public PaintShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PaintShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mTranslateX = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBound = new Rect();
        mGradientMatrix = new Matrix();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mCount++;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        changeLinearGradient();

//        drawLinearGradient(canvas);

//        drawBitmapShader(canvas);

//        drawSweepGradient(canvas);

//        drawRadialGradient(canvas);

        drawComposeShader(canvas);

//        changeShaderWithMatrix(canvas);

//        changeShaderWithMatrix(canvas);
    }

    private void changeLinearGradient(){
        if(mLinearGradient == null){
            mLinearGradient = new LinearGradient(
                    0, 0, getWidth(), getHeight(),
                    new int[] { 0x88ffffff, 0xffffffff, 0x88ffffff },
                    new float[] { 0, 0.5f, 1 },
                    Shader.TileMode.CLAMP);
        }
        mTranslateX += getWidth() / 5;
        if(mTranslateX > 2 * getWidth()){
            mTranslateX = -getWidth();
        }
        mGradientMatrix.setTranslate(mTranslateX,0);
        mLinearGradient.setLocalMatrix(mGradientMatrix);
        mPaint.setShader(mLinearGradient);
    }

    private void drawGradientText(Canvas canvas){
        mPaint.setShader(null);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        mPaint.setColor(Color.YELLOW);
        final float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30,getResources().getDisplayMetrics());
        mPaint.setTextSize(textSize);
        String text = String.valueOf(mCount);
        mPaint.getTextBounds(text,0,text.length(),mBound);
        final float textWidth = mBound.width();
        final float textHeight = mBound.height();
        final float startX = ( getWidth() - textWidth ) / 2;
        final float startY = ( getHeight() - textHeight) / 2;

        changeLinearGradient();

        canvas.drawText(text,startX,startY,mPaint);
    }

    private void drawLinearGradient(Canvas canvas){

        //draw gray canvas to outstanding effect
        canvas.drawColor(Color.GRAY);

        int x = 200,y = 200;

        Bitmap bitmap = decodeBitmapWithAppointedSize(R.drawable.cat2);

        Log.i(TAG,"bitmap width is " + bitmap.getWidth() +
                ",height is " + bitmap.getHeight());

        Matrix matrix = new Matrix();
        matrix.setScale(1f,-1f);
        //produce one inverted pic as the same with "sky"
        Bitmap refBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        //draw original pic
        canvas.drawBitmap(bitmap,x,y,null);

        //save whole rect(source + inverted)
        RectF saveRect = new RectF(x,y + bitmap.getHeight(),x + getWidth(),y + bitmap.getHeight() * 2);
        int sc = canvas.saveLayer(saveRect,null);

        //draw inverted pic from the bottom of source pic
        canvas.drawBitmap(refBitmap, x, y + bitmap.getHeight(),null);
        //set pait mixed mode
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setShader(new LinearGradient(x, y + bitmap.getHeight(),
                x, y + bitmap.getHeight() + bitmap.getHeight() / 4,
                Color.BLACK,Color.TRANSPARENT,Shader.TileMode.CLAMP));
        canvas.drawRect(x, y + bitmap.getHeight(),
                x + refBitmap.getWidth(), y + bitmap.getHeight() * 2,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    private final int BITMAP_WIDTH = 200,BITMAP_HEIGHT = 200;

    private Bitmap decodeBitmapWithAppointedSize(int drawableId){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),drawableId,options);

        int sampleSize = 1;

        if(options.outWidth <= options.outHeight){
            if(options.outWidth > BITMAP_WIDTH){
                double tmp = CommonUtils.div(options.outWidth,BITMAP_WIDTH,1);
                sampleSize = (int) Math.round(tmp);
            }
        }else{
            if(options.outHeight > BITMAP_HEIGHT)
            {
                double tmp = CommonUtils.div(options.outHeight,BITMAP_HEIGHT,1);
                sampleSize = (int) Math.round(tmp);
            }
        }
        Log.i(TAG,"original width and height is " + options.outWidth +
                        "," + options.outHeight + "\n sampleSime is " + sampleSize);

        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(),drawableId,options);
    }

    /**
     * BitmapShader 先画Y轴再画X轴
     * @param canvas
     */
    private void drawBitmapShader(Canvas canvas){

        LinearGradient linearGradient = new LinearGradient(0,getHeight() / 2,getWidth(),getHeight() / 2,
                Color.parseColor("#ff3366"),
                Color.parseColor("#99ffcc"),
                Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        Bitmap bitmap = decodeBitmapWithAppointedSize(R.drawable.cat3);
        BitmapShader bitmapShader = new BitmapShader(bitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
        mPaint.setShader(bitmapShader);

        RectF rect = new RectF(0,0,getWidth(),getWidth());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawCircle(centerX,
                centerY,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,getResources().getDisplayMetrics()),
                mPaint);
//        canvas.drawRoundRect(rect,centerX,centerY,mPaint);
    }

    private void drawSweepGradient(Canvas canvas){
        final int x = getWidth() / 2;
        final int y = getHeight() / 2;
        int[] colors = new int[]{Color.parseColor("#cc0066"),Color.parseColor("#ffcccc"),Color.parseColor("#33ffff")};
        float[] positions = new float[]{0.3f,0.6f,1};
        SweepGradient sweepGradient = new SweepGradient(x,y,colors,positions);
        mPaint.setShader(sweepGradient);

        RectF rect = new RectF(0,0,getWidth(),getHeight());
        canvas.drawArc(rect,-30,-240,true,mPaint);
    }

    /**
     * 径向渐变
     * @param canvas
     */
    private void drawRadialGradient(Canvas canvas){
        LinearGradient linearGradient = new LinearGradient(0,getHeight() / 2,getWidth(),getHeight() / 2,
                Color.parseColor("#ff3366"),
                Color.parseColor("#99ffcc"),
                Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        final float x = getWidth() / 2;
        final float y = getHeight() / 2;
        int[] colors = new int[]{Color.parseColor("#cc0066"),Color.parseColor("#cc66cc"),Color.parseColor("#ccccff")};
        float[] stops = new float[]{0.3f,0.6f,1};
        RadialGradient radialGradient = new RadialGradient(x,y,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,getResources().getDisplayMetrics()),
                colors,
                stops,
                Shader.TileMode.CLAMP);
        RectF rect = new RectF(0,0,getWidth(),getHeight());
        mPaint.setShader(radialGradient);
        canvas.drawRect(rect,mPaint);
    }

    /**
     * 组合渐变
     * @param canvas
     */
    private void drawComposeShader(Canvas canvas){

        canvas.drawColor(Color.parseColor("#ff6699"));

        Bitmap bitmap = decodeBitmapWithAppointedSize(R.drawable.cat4);
        Shader shader01 = new BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.MIRROR);
        Shader shader02 = new LinearGradient(0, 0, 0, 100, new int[] {
                Color.WHITE, Color.LTGRAY, Color.TRANSPARENT, Color.GREEN }, null,
                Shader.TileMode.MIRROR);
        ComposeShader composeShader = new ComposeShader(shader01, shader02, PorterDuff.Mode.MULTIPLY);
        // 设置shader
        mPaint.setShader(composeShader);

        canvas.drawCircle(240, 360, 200, mPaint);
    }

    /***
     * use Matrix to realize translate (especially BitmapShader)
     * @param canvas
     */
    private void changeShaderWithMatrix(Canvas canvas){

        canvas.drawColor(Color.parseColor("#ff6699"));

        Matrix matrix = new Matrix();
        matrix.setTranslate(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,getResources().getDisplayMetrics()));
        matrix.setRotate(20);
        /*LinearGradient linearGradient = new LinearGradient(
                0,getHeight()/2,
                getWidth(),getHeight()/2,
                Color.parseColor("#ff6699"),
                Color.parseColor("#000066"),
                Shader.TileMode.CLAMP);
        linearGradient.setLocalMatrix(matrix);
        mPaint.setShader(linearGradient);*/

        Bitmap bitmap = decodeBitmapWithAppointedSize(R.drawable.cat4);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);

        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

    }
}
