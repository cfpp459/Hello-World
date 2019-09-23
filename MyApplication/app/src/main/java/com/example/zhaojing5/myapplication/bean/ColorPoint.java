package com.example.zhaojing5.myapplication.bean;

import android.graphics.Color;

public class ColorPoint {
    /**
     * 扩展可实例化的类，可以通过添加父类成员变量和添加新组建。
     */
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        if( color == null ){
            throw new NullPointerException();
        }
        this.point = new Point(x, y);
        this.color = color;
    }

    public Point asPoint(){
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof  ColorPoint) ){
            return false;
        }
        ColorPoint colorPoint = (ColorPoint) obj;
        return colorPoint.point.equals(point)
                && colorPoint.color.equals(color);
    }
}
