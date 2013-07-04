package com.example.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by dingdj on 13-7-4.
 */
public class ImageCache {

    private LruCache<String, Bitmap> lruCache;

    public ImageCache(int cacheSize){
        lruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return (bitmap. getRowBytes() * bitmap.getHeight())/1024;
            }
        };
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return lruCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            lruCache.put(key, bitmap);
        }
    }

}
