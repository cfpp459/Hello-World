package com.example.zhaojing5.myapplication.menu;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.example.zhaojing5.myapplication.R;

import java.lang.reflect.Type;

public class MyActionbarTab<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public MyActionbarTab(Activity mActivity, String mTag, Class<T> mClass) {
        this.mActivity = mActivity;
        this.mTag = mTag;
        this.mClass = mClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(mFragment == null){
            mFragment = Fragment.instantiate(mActivity,mClass.getName());
            ft.add(android.R.id.content,mFragment,mTag);
        }else{
            ft.attach(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(mFragment != null){
            ft.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
