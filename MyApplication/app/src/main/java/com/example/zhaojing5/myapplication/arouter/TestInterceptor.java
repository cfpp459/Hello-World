package com.example.zhaojing5.myapplication.arouter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.example.zhaojing5.myapplication.Utils.ToastUtils;

/**
 * created by zhaojing 2020/3/11 下午5:44
 */
@Interceptor(priority = 0, name = "测试拦截器")
//priority值越小 优先级越高
public class TestInterceptor implements IInterceptor {
    public static final String TAG = TestInterceptor.class.getSimpleName();
    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {

        //登录拦截

        Log.i(TAG, "进入拦截器逻辑。。。");
        //处理完成，交换控制权
        callback.onContinue(postcard);
        Log.i(TAG, "通过拦截过滤，继续路由跳转。。。");

        //拦截，中断路由
//        callback.onInterrupt(new RuntimeException("登录拦截有异常。。。"));

        //以上两种需要调用其中一种，否则不会继续路由

    }

    @Override
    public void init(Context context) {
        //拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
        Log.d(TAG,"TestInterceptor init...");
        this.mContext = context;
    }

}
