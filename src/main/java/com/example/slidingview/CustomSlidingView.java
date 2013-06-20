package com.example.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 13-6-18.
 */
public class CustomSlidingView extends SlidingView{

    public CustomSlidingView(Context context) {
        super(context);
    }

    public CustomSlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onGetItemView(int position, View contentView, View parent) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_launcher));
        return imageView;
    }
}
