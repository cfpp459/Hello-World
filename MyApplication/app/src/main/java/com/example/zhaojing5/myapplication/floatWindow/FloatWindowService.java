package com.example.zhaojing5.myapplication.floatWindow;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {
    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(),0,5000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private boolean isHome(){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = activityManager.getRunningTasks(1);
        if(runningTaskInfoList != null){
           return getHome().contains(runningTaskInfoList.get(0).topActivity.getPackageName());
        }
        return false;
    }

    private List<String> getHome(){
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        List<String> pkgNameList = new ArrayList<>();
        for (ResolveInfo resolveInfo:resolveInfoList) {
            pkgNameList.add(resolveInfo.activityInfo.packageName);
        }
        return pkgNameList;
    }

    class RefreshTask extends TimerTask{
        @Override
        public void run() {
            //deal float window logic
            //当前界面是桌面，并没有悬浮窗显示，则创建悬浮窗
            if(isHome() && !MywindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MywindowManager.createSmallWindow(getApplicationContext());
                    }
                });
                //当前不是桌面，并且有悬浮窗显示，则移除悬浮窗
            }else if(!isHome() && MywindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MywindowManager.removeSmallWindow(getApplicationContext());
                        MywindowManager.removeBigWindow(getApplicationContext());
                    }
                });
                //当前界面是桌面，且有悬浮窗显示，则更新数据
            }else if(isHome() && MywindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MywindowManager.updateUsedPercent(getApplicationContext());
                    }
                });
            }

        }
    }
}
