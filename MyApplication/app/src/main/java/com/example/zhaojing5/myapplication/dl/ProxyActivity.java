package com.example.zhaojing5.myapplication.dl;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.zhaojing5.myapplication.R;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ProxyActivity extends Activity {
    private static final String TAG = "ProxyActivity";

    public static final String FROM = "extra.from";
    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    private String mClass;
    private String mDexPath;
    private AssetManager mAssetManager;
    private Resources mResource;
    private Resources.Theme mTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);
        Log.d(TAG,"mDexPath is " + mDexPath
                + "mClass is " + mClass);
        if(mClass == null){
            launchTargetActivity();
        }else{
            lauchTargetActivity(mClass);
        }
    }

    public void launchTargetActivity(){
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(mDexPath, PackageManager.GET_ACTIVITIES);
        if(packageInfo != null && packageInfo.activities != null && packageInfo.activities.length > 0){
            String activityName = packageInfo.activities[0].name;
            Log.d(TAG,"");
            mClass = activityName;
            lauchTargetActivity(mClass);
        }else{
            Toast.makeText(this,getResources().getString(R.string.dexPathError),Toast.LENGTH_LONG).show();
        }
    }

    public void lauchTargetActivity(final String className){
        Log.d(TAG,"start lauchTargetActivity,className = " + className);
        File dexOutPutDir = this.getDir("dex", Context.MODE_PRIVATE);
        String dexOutPutPath = dexOutPutDir.getAbsolutePath();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath,dexOutPutPath,null,classLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            Constructor<?> localConstrictor = localClass.getConstructor(new Class[]{});
            Object instance = localConstrictor.newInstance(localClass);
            Log.d(TAG,"call instance is " + instance);

            loadResources();

            Method setProxy = localClass.getMethod("setProxy",new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(instance,new Object[]{this});

            Method onCreate = localClass.getDeclaredMethod("onCreate",new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM,FROM_EXTERNAL);
            onCreate.invoke(instance,new Object[]{bundle});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadResources(){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addassetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
            addassetPath.invoke(assetManager,mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResource = new Resources(mAssetManager,superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResource.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResource == null ? super.getResources() : mResource;
    }
}
