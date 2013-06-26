package com.example.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
        ItemInfo itemInfo = getAdapter().getItemInfoByIndex(position);
        if(itemInfo != null){
            imageView.setImageDrawable(itemInfo.getDrawable());
        }else{
            imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher));
        }
        imageView.setOnClickListener(this);
        imageView.setOnLongClickListener(this);
        return imageView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Toast toast = Toast.makeText(v.getContext(), "onClick", 200);
        toast.show();

    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     *          <p/>
     *          return True if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onLongClick(View v) {
        Toast toast = Toast.makeText(v.getContext(), "onLongClick", 200);
        toast.show();
        return true;
    }
}
