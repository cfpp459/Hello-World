package com.example.zhaojing5.myapplication.bean;

public class UserInfo {
    String name;
    String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    UserInfo(String name,String pwd){
        this.name = name;
        this.pwd = pwd;
    }
}
