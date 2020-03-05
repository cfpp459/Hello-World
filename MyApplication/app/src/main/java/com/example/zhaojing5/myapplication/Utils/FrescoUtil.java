package com.example.zhaojing5.myapplication.Utils;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.animation.drawable.AnimatedDrawable2;
import com.facebook.fresco.animation.drawable.AnimationListener;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * created by zhaojing 2020/1/10 下午5:57
 */
public class FrescoUtil {
    /**
     * SimpleDraweeView加载gif文件 播放一次后加载静态图片
     *
     * @param uri
     * @param sdv
     */
    public static void loadGifOnce(String uri, SimpleDraweeView sdv, String staticImgUrl) {
        Uri uri2 = Uri.parse(uri);
        DraweeController mDraweeController2 = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(false)
                .setUri(uri2)
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {

                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        Log.d("zhaojing","onFinalImageSet change static img...");
                        if (animatable != null && !animatable.isRunning()){
                            animatable.start();
                            if(animatable instanceof AnimatedDrawable2){
                                AnimatedDrawable2 animatedDrawable2 = (AnimatedDrawable2) animatable;
                                animatedDrawable2.setAnimationListener(new AnimationListener() {
                                    @Override
                                    public void onAnimationStart(AnimatedDrawable2 drawable) {
                                        Log.d("zhaojing","onAnimationStart change static img...");
                                    }

                                    @Override
                                    public void onAnimationStop(AnimatedDrawable2 drawable) {
                                        Log.d("zhaojing","onAnimationStop change static img...");
//                                        FrescoUtils.setImageUrl(sdv, staticImgUrl);
                                    }

                                    @Override
                                    public void onAnimationReset(AnimatedDrawable2 drawable) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(AnimatedDrawable2 drawable) {
                                        Log.d("zhaojing","onAnimationRepeat change static img...");
//                                        FrescoUtils.setImageUrl(sdv, staticImgUrl);
                                    }

                                    @Override
                                    public void onAnimationFrame(AnimatedDrawable2 drawable, int frameNumber) {

                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {

                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }

                    @Override
                    public void onRelease(String id) {

                    }
                })
                .build();
        sdv.setController(mDraweeController2);
    }
}
