package com.example.zhaojing5.myapplication.arouter;

/**
 * created by zhaojing 2020/3/10 下午1:59
 */
public class ARouterInfo {

    /***
     * 字段声明必须是public
     */
    public String startUseDate;

    public String comment;

    public int starLevel;

    public ARouterInfo() {
    }

    public ARouterInfo(String startUseDate, String comment, int starLevel) {
        this.startUseDate = startUseDate;
        this.comment = comment;
        this.starLevel = starLevel;
    }

    @Override
    public String toString() {
        return "ARouterInfo{" +
                "startUseDate='" + startUseDate + '\'' +
                ", comment='" + comment + '\'' +
                ", starLevel=" + starLevel +
                '}';
    }
}
