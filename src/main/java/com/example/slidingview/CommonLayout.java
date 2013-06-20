package com.example.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by dingdj on 13-6-20.
 */
public class CommonLayout extends ViewGroup{

    public CommonLayout(Context context) {
        super(context);
    }

    public CommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
