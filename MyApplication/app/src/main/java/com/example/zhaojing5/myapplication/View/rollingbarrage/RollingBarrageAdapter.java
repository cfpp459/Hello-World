package com.example.zhaojing5.myapplication.View.rollingbarrage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhaojing5.myapplication.R;

import java.util.List;

/**
 * created by zhaojing 2020/1/2 下午3:21
 */
public abstract class RollingBarrageAdapter<T> {

    private LayoutInflater mLayoutInflater;
    protected List<T> mData;

    public RollingBarrageAdapter(Context context, List<T> data) {
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    protected View getRootView(int layoutID) {
        return mLayoutInflater.inflate(layoutID, null);
    }

    public int getSize(){
        int size = 0;
        if (mData != null) {
            size = mData.size();
        }
        return size;
    }

    public abstract View getView(int position);

}
