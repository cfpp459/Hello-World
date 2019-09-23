package com.example.zhaojing5.myapplication.menu;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.zhaojing5.myapplication.Utils.ToastUtils;

public class MyActionProvider extends ActionProvider {
    private Context mContext;

    @Override
    public View onCreateActionView() {
        return null;
    }

    public MyActionProvider(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        super.onPrepareSubMenu(subMenu);
        subMenu.add("subMenu1")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ToastUtils.showToast(mContext,"click subMenu1");
                        return true;
                    }
                });
        subMenu.add("subMenu2")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ToastUtils.showToast(mContext,"click subMenu2");
                        return true;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
