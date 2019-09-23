package com.example.zhaojing5.myapplication;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;

public class TestApplication extends Application {
    public final String TAG = this.getClass().getSimpleName();
    private static TestApplication instance;
    /*application method*/

    private int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; //50MB
    private ImagePipelineConfig.Builder imagePipelineConfigBuilder;
    private ImagePipelineConfig imagePipelineConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init(){
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        setMultiNotification();

        initFresco();

    }

    public ImagePipelineConfig.Builder getImagePipelineConfigBuilder() {
        return imagePipelineConfigBuilder;
    }

    public static TestApplication getInstance(){
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.unregisterActivityLifecycleCallbacks(callback);
    }

    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.registerOnProvideAssistDataListener(callback);
    }

    @Override
    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.unregisterOnProvideAssistDataListener(callback);
    }

    /*context method*/

    /**
     * 定制声音，震动，闪烁等样式
     */
    private void setCustomNotification(){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.cat1;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1,builder);

    }

    private void setMultiNotification(){
        MultiActionsNotificationBuilder builder = new MultiActionsNotificationBuilder(this);
        builder.addJPushAction(R.drawable.cat2,"cat2","小猫咪2");
        builder.addJPushAction(R.drawable.cat3,"cat3","小猫咪3");
        builder.addJPushAction(R.drawable.cat4,"cat4","小猫咪4");
        JPushInterface.setPushNotificationBuilder(2,builder);
    }

    public void initFresco(){
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext())
                .setBaseDirectoryName("frescoCache")
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                .build();

        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        imagePipelineConfigBuilder = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                .setRequestListeners(requestListeners);
        setBitmapCache(imagePipelineConfigBuilder);
        registerMemoryTrimmable();

        imagePipelineConfig = imagePipelineConfigBuilder.build();

        Fresco.initialize(this, imagePipelineConfig);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }

    private void setBitmapCache(ImagePipelineConfig.Builder builder){
        if (builder == null) {
            return;
        }
        Supplier<MemoryCacheParams> supplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                //获取手机最大内存
                int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
                //设置为手机内存的1/5
                int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 5;
                MemoryCacheParams bitmapParams = new MemoryCacheParams(
                        //可用最大内存数
                        MAX_MEMORY_CACHE_SIZE
                        //内存允许的最多图片数量
                        , Integer.MAX_VALUE
                        //内存中准备清理图片的最大内存
                        , MAX_MEMORY_CACHE_SIZE
                        //内存中准备清理图片的最大数量
                        , Integer.MAX_VALUE
                        //内存中单张图片的最大大小
                        , Integer.MAX_VALUE);
                Log.d("zhaojing","手机最大内存为 " + MAX_HEAP_SIZE + ", 内存缓存最大为 " + MAX_MEMORY_CACHE_SIZE);
                return bitmapParams;
            }
        };

        builder.setBitmapMemoryCacheParamsSupplier(supplier);
    }

    /***
     * 检测内存缓存达较大时，清空内存
     */
    private void registerMemoryTrimmable(){
        MemoryTrimmableRegistry registry = NoOpMemoryTrimmableRegistry.getInstance();
        registry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatiio = trimType.getSuggestedTrimRatio();
                Log.d("zhaojing","suggestedTrimRatiio is " + suggestedTrimRatiio);

                if(MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatiio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatiio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatiio){
                    //清空内存缓存
                    clearMemory();
                }

            }
        });

        TestApplication.getInstance().getImagePipelineConfigBuilder().setMemoryTrimmableRegistry(registry);
    }

    /**
     * clearCache
     */
    private void clearMemory(){

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        //清空内存缓存（包括Bitmap缓存和未解码图片缓存）
        imagePipeline.clearMemoryCaches();
        //清空硬盘缓存，一般在设置界面供用户手动清理
        imagePipeline.clearDiskCaches();
        //同时清理内存和硬盘缓存
        imagePipeline.clearCaches();

    }

}
