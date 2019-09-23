package com.example.zhaojing5.myapplication.View;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.zhaojing5.myapplication.R;

import java.util.List;

import static android.view.animation.AnimationUtils.loadAnimation;

/**
 * created by zhaojing 2019/7/22 上午10:51
 */
public class LooperImageView extends FrameLayout {

    private List<String> tipList;
    private int curTipIndex = 0;
    private long lastTimeMillis ;
    private static final int ANIM_DELAYED_MILLIONS = 1 * 1000;
    private static final int ANIM_DURATION = 1 * 1000;
    private AnimationSet mAnimationSet;
    private Animation anim_out, anim_in,anim_out_alpha,anim_in_alpha,animationOut,animationIn;
    private ImageView iv_out,iv_in;
    private Context mContext;
    public LooperImageView(Context context) {
        super(context);
        initTipFrame(context);
        initAnimation();
    }

    public LooperImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTipFrame(context);
        initAnimation();
    }

    public LooperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTipFrame(context);
        initAnimation();
    }
    private void initTipFrame(Context context) {
        this.mContext = context;
        iv_out = newImageView();
        iv_in = newImageView();
        addView(iv_out);
        addView(iv_in);
    }

    private ImageView newImageView(){
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
        lp.setMargins(0,0,0,0);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        return imageView;
    }

    private Drawable loadDrawable(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() - 10, drawable.getMinimumHeight() - 10);
        return drawable;
    }
    private void initAnimation() {
        anim_out = newTranslateAnimation(0, -1);
        anim_in = newTranslateAnimation(1.5f, 0);
        anim_in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("zhaojing","onAnimationEnd...");
                updateTipAndPlayAnimationWithCheck();
            }
        });
        anim_out_alpha = newAlphaAnimation(1,0.3f);
        anim_in_alpha = newAlphaAnimation(0.7f,1);

        animationOut = AnimationUtils.loadAnimation(mContext,R.anim.loopview_animators_out);
        animationIn = AnimationUtils.loadAnimation(mContext,R.anim.loopview_animators_in);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("zhaojing","onAnimationEnd...");
                updateTipAndPlayAnimationWithCheck();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
    private Animation newTranslateAnimation(float fromYValue, float toYValue) {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_PARENT,fromYValue, Animation.RELATIVE_TO_SELF, toYValue);
        this.setBackgroundColor(Color.TRANSPARENT);
        anim.setDuration(ANIM_DURATION);
        anim.setStartOffset(ANIM_DELAYED_MILLIONS);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    private Animation newAlphaAnimation(float fromAlpha, float toAlpha){
        Animation anim = new AlphaAnimation(fromAlpha,toAlpha);
        this.setBackgroundColor(Color.TRANSPARENT);
        anim.setDuration(ANIM_DURATION);
        anim.setStartOffset(ANIM_DELAYED_MILLIONS);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    private void updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - lastTimeMillis < 1000 ) {
            return ;
        }
        lastTimeMillis = System.currentTimeMillis();
        updateTipAndPlayAnimation();
    }
    private void updateTipAndPlayAnimation() {
        if (curTipIndex % 2 == 0) {
            updateImageTip(iv_out);
//            iv_in.startAnimation(anim_out);
//            iv_out.startAnimation(anim_in);
            iv_in.startAnimation(animationOut);
            iv_out.startAnimation(animationIn);
            this.bringChildToFront(iv_in);
        } else {
            updateImageTip(iv_in);
//            iv_out.startAnimation(anim_out);
//            iv_in.startAnimation(anim_in);
            iv_out.startAnimation(animationOut);
            iv_in.startAnimation(animationIn);
            this.bringChildToFront(iv_out);
        }
    }

    private void updateImageTip(ImageView tipView) {
        String tip = getNextTip();
        if(TextUtils.isEmpty(tip)){
            return;
        }
        Drawable drawable = null;
        switch (tip){
            case "tip1":
                drawable = getResources().getDrawable(R.drawable.cat1);
                break;
            case "tip2":
                drawable = getResources().getDrawable(R.drawable.cat2);
                break;
            case "tip3":
                drawable = getResources().getDrawable(R.drawable.cat3);
                break;

                default:
        }
        tipView.setBackground(drawable);
    }

    private String getNextTip() {
        if (isListEmpty(tipList)){
            return null;
        }
        return tipList.get(curTipIndex++ % tipList.size());
    }
    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public void setImgTipList(List<String> tipList) {
        this.tipList = tipList;
        curTipIndex = 0;
        updateImageTip(iv_out);
        updateTipAndPlayAnimation();
    }

}
