package com.example.slidingview;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 13-6-18.
 */
public class ItemInfo {
    private Drawable drawable;
    private String title;

    public ItemInfo() {
    }

    public ItemInfo(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }
}
