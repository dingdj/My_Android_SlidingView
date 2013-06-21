package com.example.slidingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingdj on 13-6-18.
 */
public abstract class SlidingView extends ViewGroup{

    private SlidingViewAdapter mAdapter;

    protected int mCurrentScreen = 0;

    private List<CommonLayout> pageViewCache = new ArrayList<CommonLayout>();

    public SlidingView(Context context) {
        super(context);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;

        int childWidth = width/mAdapter.getCol();
        int childHeight = width/mAdapter.getRow();

        layoutChildren(mCurrentScreen, childWidth, childHeight);

    }

    /**
     *
     * @param screen
     * @param width
     * @param height
     */
    protected void layoutChildren(int screen, int width, int height){

        List<ItemInfo> data = mAdapter.getDataInPage(screen);
        int pageSize = mAdapter.getPageSize();
        int index = 0;
        int l = 0;
        int t = 0;
        for(int i=0; i<mAdapter.getRow(); i++){
            for(int j=0; j<mAdapter.getCol(); j++){
                ItemInfo itemInfo = data.get(index);
                if(itemInfo == null){
                    return;
                }
                View view = onGetItemView(pageSize*screen+index, null, this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.layout(l, t, l+width, t+height);
                l = l + width;
                this.addViewInLayout(view, this.getChildCount(), null, true);
                index++;
            }
            l = 0;
            t = t + height;
        }


    }

    /**
     *
     * @param screen
     */
    protected void makePage(int screen, int width, int height){
        List<ItemInfo> data = mAdapter.getDataInPage(screen);
        int pageSize = mAdapter.getPageSize();
        int index = 0;
        int l = 0;
        int t = 0;
        for(int i=0; i<mAdapter.getRow(); i++){
            for(int j=0; j<mAdapter.getCol(); j++){
                ItemInfo itemInfo = data.get(index);
                if(itemInfo == null){
                    return;
                }
                View view = onGetItemView(pageSize*screen+index, null, this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.layout(l, t, l+width, t+height);
                l = l + width;
                this.addViewInLayout(view, this.getChildCount(), null, true);
                index++;
            }
            l = 0;
            t = t + height;
        }

    }


    abstract protected View onGetItemView(int position, View contentView, View parent);

    public void setAdapter(SlidingViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

}
