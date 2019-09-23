package com.example.zhaojing5.myapplication.menu;

import android.content.Context;
import android.util.Log;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.zhaojing5.myapplication.R;

public class PlusActionProvider extends ActionProvider {
    public static final String TAG = PlusActionProvider.class.getSimpleName();
    private Context context;

    public PlusActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        super.onPrepareSubMenu(subMenu);
        subMenu.clear();
        subMenu.add("subMenu1")
                .setIcon(R.drawable.chat_dialog)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG,"subMenu1 clcik");
                        return true;
                    }
                });
        subMenu.add("subMenu2")
                .setIcon(R.drawable.chat_dialog)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG,"subMenu2 click");
                        return true;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
