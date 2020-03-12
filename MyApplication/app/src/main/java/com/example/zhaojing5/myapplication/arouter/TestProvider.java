package com.example.zhaojing5.myapplication.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * created by zhaojing 2020/3/12 上午11:46
 */
@Route(path = ARouterConstant.mARouterPathProvider)
public class TestProvider implements IProvider {

    @Override
    public void init(Context context) {

    }


    public String test(){
        return ("TestProvider func test...");
    }

}
