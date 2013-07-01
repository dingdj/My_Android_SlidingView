package com.example.slidingview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slidingview.springmode.SpringModeHelper;
import com.example.util.SystemUtil;

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
    protected View onGetItemView(int position, View contentView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, this, false);
        ImageView imageView = (ImageView)(view.findViewById(R.id.imageView));
        ItemInfo itemInfo = getAdapter().getItemInfoByIndex(position);
        if(itemInfo != null){
            imageView.setImageDrawable(itemInfo.getDrawable());
        }else{
            imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher));
        }
        LayoutParams lp = imageView.getLayoutParams();
        lp.width = lp.height = SystemUtil.dip2px(getContext(), 48);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(this);
        imageView.setOnLongClickListener(this);
        Log.v(TAG, imageView.getWidth()+"");
        TextView tv = (TextView)(view.findViewById(R.id.title));
        LayoutParams tlp = tv.getLayoutParams();
        tlp.width = SystemUtil.dip2px(getContext(), 68);
        tv.setLayoutParams(tlp);
        tv.setText(itemInfo.getTitle());
        return view;
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
       // Toast toast = Toast.makeText(v.getContext(), "onLongClick", 200);
       // toast.show();
        SpringModeHelper springModeHelper = SpringModeHelper.getInstance();
        int height = this.getHeight();
        int width = this.getWidth();
        springModeHelper.initSpringModeParams(width/8,width/8,width, height);
        springModeHelper.animationToSpringMode(this);
        this.setMode(Mode.Spring);
        this.computeScrollScale();
        //Log.v("SlidingView", "this.getFinalScrollX():"+this.getFinalScrollX());
        return true;
    }
}
