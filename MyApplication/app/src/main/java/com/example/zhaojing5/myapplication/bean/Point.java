package com.example.zhaojing5.myapplication.bean;

public class Point {
    private int x,y;
    Point( int x, int y ){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof Point) ){
            return false;
        }
        Point point = (Point) obj;
        return point.x == x
                && point.y == y;
    }
}
