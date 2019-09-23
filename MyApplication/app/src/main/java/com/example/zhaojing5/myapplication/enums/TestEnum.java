package com.example.zhaojing5.myapplication.enums;

import com.example.zhaojing5.myapplication.R;

public enum TestEnum {
    item1(0,R.drawable.ic_launcher_background),//实际上就是调用一遍构造函数！
    item2(1,R.drawable.ic_launcher_background),
    item3(2,R.drawable.ic_launcher_background);

    private int resId;
    private int index;

    TestEnum(int index,int resId) {
        this.index = index;
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public int getIndex() {
        return index;
    }


}
