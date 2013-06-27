package com.example.slidingview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by dingdj on 13-6-27.
 */
public class LightBar extends LinearLayout implements SlidingView.PageChangeEvent{

    private static final String TAG = "LightBar";

    private Drawable normalLighter, selectedLighter;

    //指示灯的数目
    private int items;

    private Context context;


    private int curItem;

    public LightBar(Context context) {
        super(context);
        this.context = context;
    }

    public LightBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Drawable getNormalLighter() {
        return normalLighter;
    }

    public void setNormalLighter(Drawable normalLighter) {
        this.normalLighter = normalLighter;
    }

    public Drawable getSelectedLighter() {
        return selectedLighter;
    }

    public void setSelectedLighter(Drawable selectedLighter) {
        this.selectedLighter = selectedLighter;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getCurItem() {
        return curItem;
    }

    public void setCurItem(int curItem) {
        this.curItem = curItem;
    }

    /**
     * 初始化LightBar
     */
    public void initLightBar(){
        for(int i=0; i<items; i++){
            ImageView imageView;
            if(i == curItem){
                imageView = createSelectedImageView();
            }else{
                imageView = createNormalImageView();
            }
            this.addView(imageView);
            Log.v(TAG, "lightbar childs"+getChildCount());
        }
        requestLayout();
    }

    /* 当页数变化时 重新进行布局 */
    @Override
    public void pageChanged(int lastPage, int curPage, int pages) {
        if(pages > items){ //总页数变多
            for(int i = 0; i<pages-items; i++){
                ImageView imageView = createNormalImageView();
                this.addView(imageView);
            }
        }else if(pages < items){ //总页数变少
            for(int i=0; i<items-pages; i++){
                removeViewAt(items-i);
            }
        }
        curItem = curPage;
        items = pages;
        if(items != getChildCount()){
            Log.e("LightBar", "page index error");
        }

        for(int j=0; j<getChildCount(); j++){
            ImageView imageView = (ImageView)getChildAt(j);
            if(j == curPage){
                imageView.setImageDrawable(selectedLighter);
            }else{
                imageView.setImageDrawable(normalLighter);
            }
        }
        requestLayout();
    }


    private ImageView createNormalImageView(){
        return createImageView(normalLighter);
    }


    private ImageView createSelectedImageView(){
        return createImageView(selectedLighter);
    }

    private ImageView createImageView(Drawable drawable){
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawable);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }
}
