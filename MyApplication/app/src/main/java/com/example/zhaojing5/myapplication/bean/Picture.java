package com.example.zhaojing5.myapplication.bean;

public class Picture {
    /**
     * picture name
     */
    private String name;

    /**
     * picture resource id
     */
    private int resource;

    public Picture(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
