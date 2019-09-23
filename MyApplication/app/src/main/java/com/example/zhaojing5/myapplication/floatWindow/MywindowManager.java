package com.example.zhaojing5.myapplication.floatWindow;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhaojing5.myapplication.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.Integer.parseInt;

public class MywindowManager {
    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowSmallView smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams smallWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static WindowManager.LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fileReader = new FileReader(dir);
            BufferedReader bufferedReader = new BufferedReader(fileReader,20248);
            String line = bufferedReader.readLine();
            String totalMem = line.substring(line.indexOf(":") + 1);
            bufferedReader.close();
            int totalMemorySize = Integer.parseInt(totalMem.replaceAll("[\\D]",""));
            int avaliableSize = getAvaliableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - avaliableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    private static int getAvaliableMemory(Context context){
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(memoryInfo);
        return (int)memoryInfo.availMem;
    }

    private static ActivityManager getActivityManager(Context context){
        if(mActivityManager == null){
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    public static void createBigWindow(Context context){
        WindowManager windowManager = getmWindowManager(context);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if(bigWindow == null){
            bigWindow = new FloatWindowBigView(context);
            if(bigWindowParams == null){
                bigWindowParams = new WindowManager.LayoutParams();
                bigWindowParams.x = (point.x - FloatWindowBigView.viewWidth) / 2;
                bigWindowParams.y = (point.y - FloatWindowBigView.viewHeight) / 2;
                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.START | Gravity.TOP;
                bigWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
        }
        windowManager.addView(bigWindow,bigWindowParams);
    }

    public static void createSmallWindow(Context context){
        WindowManager windowManager = getmWindowManager(context);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if(smallWindow == null){
            smallWindow = new FloatWindowSmallView(context);
            if(smallWindowParams == null){
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.x = (point.x - FloatWindowSmallView.viewWidth) / 2;
                smallWindowParams.y = (point.y - FloatWindowSmallView.viewHeight) / 2;
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.gravity = Gravity.START | Gravity.TOP;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
            }
        }
        smallWindow.setParams(smallWindowParams);
        windowManager.addView(smallWindow,smallWindowParams);
    }

    public static void removeSmallWindow(Context context){
        if(smallWindow != null){
            WindowManager windowManager = getmWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    public static void removeBigWindow(Context context){
        if(bigWindow != null){
            WindowManager windowManager = getmWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    public static WindowManager getmWindowManager(Context context) {
        if(mWindowManager == null){
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
    public static boolean isWindowShowing(){
        return smallWindow != null || bigWindow != null;
    }

    public static void updateUsedPercent(Context context){
        if(smallWindow != null){
            TextView percentView = smallWindow.findViewById(R.id.percent);
            percentView.setText(getUsedPercentValue(context));
        }
    }
}
