package com.example.zhaojing5.myapplication.bean.databasebean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * created by zhaojing 2020/2/17 上午10:59
 */
@Entity
public class PersionInfo {

    @Id(autoincrement = true)
    private long id;

    @Index(unique = true)
    private String perNo;

    private String name;

    private String sex;

    @Generated(hash = 1121681892)
    public PersionInfo(long id, String perNo, String name, String sex) {
        this.id = id;
        this.perNo = perNo;
        this.name = name;
        this.sex = sex;
    }

    @Generated(hash = 811456262)
    public PersionInfo() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPerNo() {
        return this.perNo;
    }

    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
