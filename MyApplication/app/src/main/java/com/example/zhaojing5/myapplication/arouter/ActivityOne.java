package com.example.zhaojing5.myapplication.arouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.zhaojing5.myapplication.R;

/**
 * @author
 * created by zhaojing 2020/3/10 上午10:29
 */

@Route(path = ARouterConstant.mARouterPathActivityOne, group = ARouterConstant.ACTIVITY_GROUP,extras = ARouterConstant.mARouterPathActivityOneExtras)
public class ActivityOne extends AppCompatActivity {

    public static final String TAG = ActivityOne.class.getSimpleName();

    /***
     * 传值方式1：@Autowired
     * 字段名命名必须和调起时传值的key一致，或者注解Autowired单独声明key值
     */
    @Autowired(name = "key1")
    protected String name;

    @Autowired(name = "key2")
    protected int age;

    @Autowired(name = "key3")
    protected ARouterInfo aRouterInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_arouter);
        //调用inject方法，会自动解析@Autowired注解
        ARouter.getInstance().inject(this);

        //传值方式2
        if (getIntent() != null) {
            SerializationService serializationService = ARouter.getInstance().navigation(SerializationService.class);
            serializationService.init(this);
            ARouterInfo aRouterInfo = serializationService.parseObject(getIntent().getStringExtra("key3"), ARouterInfo.class);
            Log.i("TAG","use serializationService ARouterInfo is " + ( aRouterInfo == null ? "null" : aRouterInfo.toString() ) );
        }

        Log.i(TAG, "name is " + name + "\n age is " + age + "\n ARouterInfo is " + ( this.aRouterInfo == null ? "null" : this.aRouterInfo.toString() ) );
    }

}
