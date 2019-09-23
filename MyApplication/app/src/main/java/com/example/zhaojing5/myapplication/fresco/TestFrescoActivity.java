package com.example.zhaojing5.myapplication.fresco;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;

import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.TestApplication;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by zhaojing 2019/9/16 下午1:54
 */
public class TestFrescoActivity extends AppCompatActivity {
    private Unbinder unbinder;

    @BindView(R.id.sdv_test_image)
    SimpleDraweeView mDrawView;

    private Animatable animatable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_fresci_layout);
        unbinder = ButterKnife.bind(this);

        init();
    }

    public void init(){


        //set image with Uri
//        setImageWithUri();

        //set image with MODEL(DraweeHierarchy)
//        setWithHierarchy();

        //set progressBarImage with many images
//        setProgressBarImages();

        //set image with Controller
//        setWithController();

        //set image with controllerListener
//        setImageWithListener();

        //set image with ImageRequest
//        setImageWithImageRequest();

        //set image with the same real image
//        setImageWithFirstAvailable();

        //set image with postProcessor
        setImageWithPostProcessor();

        //set gif
//        setGif();

        //clear cache with uri
//        clearCacheWithUri();

        //isInMemoryCache
//        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");
//        ToastUtils.showToast(this, "二哈的图片缓存了吗？-" + isCache(uri));

        //isInDiskCache
//        isInDiskCache();

    }

    private void getCacheSize(){
        ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(this);


//        Log.d("zhaojing","disk cache is "  + );
    }

    /***
     * view
     */
    private void setImageWithUri(){
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523079111408&di=7783555b20885592a8034c6e729a6414&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01ea90595f5ca4a8012193a3d93648.jpeg");
        mDrawView.setImageURI(uri);
    }

    /***
     * Model
     */
    private void setWithHierarchy(){
        GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        genericDraweeHierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        //缩放类型为CENTER_CROP时，需要设置一个居中点
        //（0，0）表示坐上对齐显示，（1，1）表示右下对齐显示
        PointF pointF = new PointF(1f, 1f);
        genericDraweeHierarchyBuilder.setActualImageFocusPoint(pointF);
        //进度条，占位图消失时，加载图片展示的时间间隔
        genericDraweeHierarchyBuilder.setFadeDuration(1000);
        genericDraweeHierarchyBuilder.setFailureImage(R.drawable.cat3, ScalingUtils.ScaleType.CENTER_CROP);
        genericDraweeHierarchyBuilder.setPlaceholderImage(R.drawable.cat1, ScalingUtils.ScaleType.CENTER_CROP);
        //加载进度条图片
        genericDraweeHierarchyBuilder.setProgressBarImage(R.drawable.jpush_richpush_progressbar, ScalingUtils.ScaleType.CENTER_INSIDE);
        genericDraweeHierarchyBuilder.setRetryImage(R.mipmap.ic_launcher, ScalingUtils.ScaleType.CENTER_CROP);
        //设置背景图片
        genericDraweeHierarchyBuilder.setBackground(getResources().getDrawable(R.color.cardview_dark_background));
        //在图片上方覆盖一个图片资源
        genericDraweeHierarchyBuilder.setOverlay(getResources().getDrawable(R.drawable.cat4));
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#ff6699"));
        genericDraweeHierarchyBuilder.setPressedStateOverlay(colorDrawable);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(false);
        roundingParams.setCornersRadii(100f,0f,100f,0f);
        //圆角部分填充色
        roundingParams.setOverlayColor(Color.parseColor("#ff0000"));
        roundingParams.setBorderWidth(20f);
        roundingParams.setBorderColor(Color.parseColor("#ffffff"));
        genericDraweeHierarchyBuilder.setRoundingParams(roundingParams);
        GenericDraweeHierarchy hierarchy = genericDraweeHierarchyBuilder.build();
        mDrawView.setHierarchy(hierarchy);
    }

    private void setProgressBarImages(){
        List<Drawable> overlayList = new ArrayList<>();
        overlayList.add(new ColorDrawable(Color.parseColor("#55ff6699")));
        overlayList.add(getResources().getDrawable(R.mipmap.customer_service_gymbo_red));
        GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        genericDraweeHierarchyBuilder.setOverlays(overlayList);
        genericDraweeHierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        //缩放类型为CENTER_CROP时，需要设置一个居中点
        //（0，0）表示坐上对齐显示，（1，1）表示右下对齐显示
        PointF pointF = new PointF(1f, 1f);
        genericDraweeHierarchyBuilder.setActualImageFocusPoint(pointF);

        GenericDraweeHierarchy hierarchy = genericDraweeHierarchyBuilder.build();
        mDrawView.setHierarchy(hierarchy);

        setImageWithUri();
    }

    /***
     * controller
     */
    private void setWithController(){
        PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");
        pipelineDraweeControllerBuilder.setUri(uri);
        //需要设置重试的图片和图片缩放类型
        pipelineDraweeControllerBuilder.setTapToRetryEnabled(true);
        pipelineDraweeControllerBuilder.setOldController(mDrawView.getController());
        DraweeController controller = pipelineDraweeControllerBuilder.build();
        mDrawView.setController(controller);
    }

    private ControllerListener getListener(){
        ControllerListener listener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                Log.d("zhaojing","提交请求之前调用的方法");
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                Log.d("zhaojing","所有图片都加载成功时触发的方法");
                Log.d("zhaojing","image id is " + id + ", width is " + imageInfo.getWidth() + ", height is " + imageInfo.getHeight());
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.d("zhaoing","当中间图片下载成功的时候触发，用于多图片请求");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                Log.d("zhaojing","当中间图片下载失败的时候触发，用于多图请求");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.d("zhaojing","加载图片失败是回调的方法");
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                Log.d("zhaojing","释放图片资源时加载的方法");
            }
        };

        return listener;
    }

    /***
     * ControllerListener
     */
    private void setImageWithListener(){

        PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        pipelineDraweeControllerBuilder.setControllerListener(getListener());

        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");
        pipelineDraweeControllerBuilder.setUri(uri);
        //需要设置重试的图片和图片缩放类型
        pipelineDraweeControllerBuilder.setTapToRetryEnabled(true);
        pipelineDraweeControllerBuilder.setOldController(mDrawView.getController());
        DraweeController controller = pipelineDraweeControllerBuilder.build();
        mDrawView.setController(controller);

    }

    /***
     * ImageRequest
     * FULL_FETCH:下载或加载本地文件。调整大小和旋转（如有），解码并返回。
     */
    private void setImageWithImageRequest(){
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");
//        RotationOptions rotationOptions = new RotationOptions(RotationOptions.ROTATE_90, true);
        RotationOptions rotationOptions = RotationOptions.forceRotation(RotationOptions.ROTATE_180);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(rotationOptions)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setResizeOptions(new ResizeOptions(width,width))
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        pipelineDraweeControllerBuilder.setImageRequest(imageRequest);
        DraweeController controller = pipelineDraweeControllerBuilder.build();
        mDrawView.setController(controller);
    }

    /***
     * setFirstAvailableImageRequests
     * 多种尺寸的图片，只要第一个加载成功，就使用第一个
     */
    @SuppressWarnings("unchecked")
    private void setImageWithFirstAvailable(){
        String image1 = "http://img1.imgtn.bdimg.com/it/u=308383462,498044055&fm=26&gp=0.jpg";
        String image2 = "http://img3.duitang.com/uploads/item/201605/05/20160505231517_d2SAa.thumb.700_0.jpeg";
        ImageRequest request1 = ImageRequest.fromUri(image1);
        ImageRequest request2 = ImageRequest.fromUri(image2);

        ImageRequest[] requests = {request1,request2};
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        builder.setFirstAvailableImageRequests(requests);
        builder.setControllerListener(getListener());
        DraweeController controller = builder.build();
        mDrawView.setController(controller);
    }

    /***
     * PostProcessor
     */
    private void setImageWithPostProcessor(){
        Postprocessor postprocessor = new MyPostProcessor();
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569220663235&di=71dca05ff431b57a1aeedec0a51e84d6&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ffront%2F40%2Fw480h360%2F20181021%2FHT9A-hmuuiyv3788175.jpg");
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor)
                .build();
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        DraweeController controller = builder.setImageRequest(imageRequest).build();
        mDrawView.setController(controller);
    }

    /***
     * 加载Gif
     */
    @SuppressWarnings("unchecked")
    private void setGif(){

        String gifUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569232869224&di=6eecce46dde2b51cbbccd37076b442c4&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20160129%2Fmp57148206_1454036961947_2.gif";
        DraweeController oldController = mDrawView.getController();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(gifUrl)
                .setControllerListener(new MyControllerListener())
                .setAutoPlayAnimations(true)
                .setOldController(oldController)
                .build();

        mDrawView.setController(controller);
        animatable = mDrawView.getController().getAnimatable();

    }

    /***
     * 判断某个图片是否缓存到内存中
     * @param uri
     * @return
     */
    private boolean isCache(Uri uri){
        if (uri == null) {
            return false;
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        return imagePipeline.isInBitmapMemoryCache(uri);
    }

    /***
     * 判断某个图片是否缓存到磁盘中
     */
    private void isInDiskCache(){

        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Boolean> inDiskCacheSource = imagePipeline.isInDiskCache(uri);

        DataSubscriber<Boolean> subscriber = new BaseDataSubscriber<Boolean>() {
            @Override
            protected void onNewResultImpl(DataSource<Boolean> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                boolean isInCache = dataSource.getResult();
                ToastUtils.showToast(TestFrescoActivity.this, "二哈图片缓存到磁盘了吗？-" + isInCache);
            }

            @Override
            protected void onFailureImpl(DataSource<Boolean> dataSource) {
                Log.d("zhaojing","onFailureImpl...");
            }
        };
        inDiskCacheSource.subscribe(subscriber, Executors.newSingleThreadExecutor());

    }

    /***
     * clear cache with Uri
     */
    private void clearCacheWithUri(){
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568960430036&di=0d5de502182a6ceb534aa628ab08443a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-12-13%2F5a3092c6d359d.jpg");

        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);

        //combines above two lines
        imagePipeline.evictFromCache(uri);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private class MyControllerListener extends BaseControllerListener{

        public MyControllerListener() {
            super();
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
            if (animatable != null) {
                animatable.start();
            }
        }

        @Override
        public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            Log.d("zhaojing","onFinalImageSet。。。");
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable Object imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
            Log.d("zhaojing","onIntermediateImageSet。。。");
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
            Log.d("zhaojing","onIntermediateImageFailed。。。");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Log.d("zhaojing","onFailure。。。");
        }

        @Override
        public void onRelease(String id) {
            super.onRelease(id);
            Log.d("zhaojing","onRelease。。。");
        }
    }

    public class MyPostProcessor extends BasePostprocessor{

        @Override
        public String getName() {
            return "redMeshPostProcessor";
        }

        /***
         * 只用于处理图片和原图片大小不一样的情况
         * 代码中将图片长宽都压缩为原来的1/4
         * 如果重写其他processor函数，可能执行调用结果有变化
         * 所以同时只重写一个
         * @param sourceBitmap
         * @param bitmapFactory
         * @return
         */
        @Override
        public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
            Log.d("zhaojing","只用于处理图片和原图片大小不一样的情况,代码中将图片长宽都压缩为原来的1/4");

//            int scale = 4;
//            CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(sourceBitmap.getWidth() / scale, sourceBitmap.getHeight() / scale);
//            try{
//                Bitmap destBitmap = bitmapRef.get();
//                for(int x = 0;x < destBitmap.getWidth();x++){
//                    for(int y = 0; y < destBitmap.getHeight(); y++){
//                        destBitmap.setPixel(x, y, sourceBitmap.getPixel(scale * x, scale * y));
//                    }
//                }
//                return CloseableReference.cloneOrNull(bitmapRef);
//            }finally {
//                CloseableReference.closeSafely(bitmapRef);
//            }

            //宽和高各缩小一半，显示图片的1/4
            CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(
                    sourceBitmap.getWidth()/2,
                    sourceBitmap.getHeight()/2);
            try {
                Bitmap destBitmap = bitmapRef.get();
                for (int x = 0; x < destBitmap.getWidth(); x ++) {
                    for (int y = 0; y < destBitmap.getHeight(); y ++) {
                        destBitmap.setPixel(x, y, sourceBitmap.getPixel(x, y));
                    }
                }
                return CloseableReference.cloneOrNull(bitmapRef);
            } finally {
                CloseableReference.closeSafely(bitmapRef);
            }

        }

//        /***
//         * 对图片无法进行及时处理
//         * 代码中对图片进行水平翻转，目标图片的处理需要用到原始图片的信息
//         * 如果重写其他processor函数，可能执行调用结果有变化
//         * 所以同时只重写一个
//         * @param destBitmap
//         * @param sourceBitmap
//         */
//        @Override
//        public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
//            super.process(destBitmap, sourceBitmap);
//            Log.d("zhaojing","对图片无法进行及时处理,代码中对图片进行反转，目标图片的处理需要用到原始图片的信息");
//
//            for(int x = 0; x < destBitmap.getWidth(); x++){
//                for(int y = 0; y < destBitmap.getHeight(); y++){
//                    destBitmap.setPixel(destBitmap.getWidth() - 1 - x, y, sourceBitmap.getPixel(x,y));
//                }
//            }
//        }

//        /***
//         * 对图片进行即使后处理
//         * 代码中在图片加上红点
//         *
//         * 如果重写其他processor函数，可能执行调用结果有变化
//         * 所以同时只重写一个
//         * @param bitmap
//         */
//        @Override
//        public void process(Bitmap bitmap) {
//            Log.d("zhaojing","对图片进行即使后处理,代码中在图片加上红点");
//
//            for(int x = 0; x < bitmap.getWidth(); x+=4){
//                for(int y = 0; y < bitmap.getHeight(); y+=4){
//                    bitmap.setPixel(x, y, Color.RED);
//                }
//            }
//
//        }


    }




}
