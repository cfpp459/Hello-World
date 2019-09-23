package com.example.zhaojing5.myapplication.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.example.zhaojing5.myapplication.R;

import java.io.InputStream;
import java.lang.reflect.Field;

public class PowerImageView extends AppCompatImageView implements View.OnClickListener{

    /**
     * 播放GIF动画的关键类
     */
    private Movie mMovie;

    /**
     * 开始播放按钮图片
     */
    private Bitmap mStartButton;

    /**
     * 记录动画开始的时间
     */
    private long mMovieStart;

    /**
     * GIF图片的宽度
     */
    private int mImageWidth;

    /**
     * GIF图片的高度
     */
    private int mImageHeight;

    /**
     * 图片是否正在播放
     */
    private boolean isPlaying;

    /**
     * 是否允许自动播放
     */
    private boolean isAutoPlay;

    public PowerImageView(Context context) {
        super(context);
        init(null);
    }

    public PowerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PowerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);//close HardWare,some phone has not support
        if(attrs == null){
        }else{
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PowerImageView);
            int resourceId = getResourcesId(a);
            if(resourceId != 0){
                InputStream is = getResources().openRawResource(resourceId);
                mMovie = Movie.decodeStream(is);
                if(mMovie != null){//it means, this is a gif
                    isAutoPlay = a.getBoolean(R.styleable.PowerImageView_auto_play,false);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
                    //only abtain the size
                    mImageWidth = options.outWidth;
                    mImageHeight = options.outHeight;
                    if( !isAutoPlay){
                        mStartButton = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                        setOnClickListener(this);
                    }
                }
            }
            if(a != null){
                a.recycle();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mMovie == null){
            //is a pure pic
            super.onDraw(canvas);
        }else{
            if(isAutoPlay){
                playMovie(canvas);
                invalidate();
            }else{
                if(isPlaying){
                    if(playMovie(canvas)){
                        isPlaying = false;
                    }
                    invalidate();
                }else{
                    mMovie.setTime(0);
                    mMovie.draw(canvas,0,0);
                    int offsetW = (mImageWidth - mStartButton.getWidth()) / 2;
                    int offsetH = (mImageHeight - mStartButton.getHeight()) / 2;
                    canvas.drawBitmap(mStartButton,offsetW,offsetH,null);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mMovie != null){
            setMeasuredDimension(mImageWidth,mImageHeight);
        }
    }

    private boolean playMovie(Canvas canvas){
        long now = SystemClock.uptimeMillis();
        if(mMovieStart == 0){
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if(duration == 0){
            duration = 1000;
        }
        int relTime = (int) ((now - mMovieStart) % duration);
        mMovie.setTime(relTime);
        mMovie.draw(canvas,0,0);
        if((now - mMovieStart) >= duration){
            mMovieStart = 0;
            return true;
        }
        return false;
    }

    private int getResourcesId(TypedArray array){
        try {
            Field field = TypedArray.class.getDeclaredField("mValue");
            field.setAccessible(true);
            TypedValue typedValue = (TypedValue) field.get(array);
            return typedValue.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == getId()){
            if(isPlaying){
                return;
            }
            isPlaying = true;
            invalidate();
        }
    }
}
