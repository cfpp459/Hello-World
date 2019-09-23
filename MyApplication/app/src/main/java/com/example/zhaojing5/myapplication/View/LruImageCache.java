package com.example.zhaojing5.myapplication.View;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class LruImageCache implements ImageLoader.ImageCache {
    private LruCache<String,Bitmap> lruCache;

    public LruImageCache() {
        int size = 10 * 1024 * 1024;
        lruCache = new LruCache<String,Bitmap>(size){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        if(lruCache != null){
            lruCache.get(s);
        }
        return null;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        if(lruCache != null){
            lruCache.put(s,bitmap);
        }
    }
}
