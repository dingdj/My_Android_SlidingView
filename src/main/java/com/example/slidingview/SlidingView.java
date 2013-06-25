package com.example.slidingview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingdj on 13-6-18.
 */
public abstract class SlidingView extends ViewGroup implements View.OnClickListener, View.OnLongClickListener{

    public static final String TAG = "SlidingView";

    private SlidingViewAdapter mAdapter;

    protected int mCurrentScreen = 0;

    private PageViewCache pageViewCache;

    private float mLastMotionX;

    private float mLastMotionY;

    private float mFilingDistance = 10;

    private Scroller mScroller;

    private float width;

    private int mTouchState = TOUCH_STATE_REST;

    private static int TOUCH_STATE_REST = 0;

    private static int TOUCH_STATE_FLING_UP = 1;

    private static int TOUCH_STATE_FLING_DOWN = 2;

    private static final int TOUCH_STATE_DOWN = 3;

    private static final int TOUCH_STATE_SCROLLING = 4;

    private static final int TOUCH_STATE_DONE_WAITING = 5;

    public SlidingView(Context context) {
        super(context);
        pageViewCache = new PageViewCache();
        mScroller = new Scroller(context);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pageViewCache = new PageViewCache();
        mScroller = new Scroller(context);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pageViewCache = new PageViewCache();
        mScroller = new Scroller(context);
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
        this.width = width;
        layoutChildren(mCurrentScreen, width, height, childWidth, childHeight);

    }

    /**
     *
     * @param width
     * @param height
     */
    protected void layoutChildren(int currentScreen, int screenWidth, int screenHeight, int width, int height){
        int pageNum = mAdapter.getPageNum();
        makePages(currentScreen, pageNum, screenWidth, screenHeight, width, height);
    }

    /**
     * 对页面进行布局
     * @param startPage
     * @param endPage
     * @param screenWidth
     * @param screenHeight
     * @param width
     * @param height
     */
    private void makePages(int startPage, int endPage, int screenWidth, int screenHeight,
                          int width, int height){
        for(int screen = startPage; screen<endPage; screen++){
            makePage(screen, screenWidth, screenHeight, width, height);
        }
    }

    /**
     * 对页面进行布局
     * @param screen
     * @param screenWidth
     * @param screenHeight
     * @param width
     * @param height
     */
    private void makePage(int screen,  int screenWidth, int screenHeight,
                           int width, int height){
        List<ItemInfo> data = mAdapter.getDataInPage(screen);
        CommonLayout layout = pageViewCache.getPageView(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.layout((screen)*screenWidth, 0, (screen+1)*screenWidth, screenHeight);
        layout.setBackgroundColor(Color.RED);
        this.addViewInLayout(layout, this.getChildCount(), null, true);
        int pageSize = mAdapter.getPageSize();
        int index = 0;
        int l = 0;
        int t = 0;
        for(int i=0; i<mAdapter.getRow(); i++){
            for(int j=0; j<mAdapter.getCol(); j++){
                if(index >= data.size() ){
                    return;
                }
                ItemInfo itemInfo = data.get(index);
                if(itemInfo == null){
                    return;
                }
                View view = onGetItemView(pageSize*screen+index, null, layout);
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                view.layout(l, t, l+width, t+height);
                l = l + width;
                layout.addViewInLayout(view, layout.getChildCount(), null, true);
                index++;
            }
            l = 0;
            t = t + height;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "onInterceptTouchEvent ACTION_DOWN");
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * ACTION_DOWN在一个子view上时，判定fling up/down
                 */
                Log.v(TAG, "onInterceptTouchEvent ACTION_MOVE");
                dealTouchStateInActionMove(x, y);
                break;

        }
        return true;//mTouchState != TOUCH_STATE_REST;
    }


    /**
     *
     * @param x
     * @param y
     */
    private void dealTouchStateInActionMove(float x, float y){
        float deltaX = mLastMotionX-x;
        float deltaY = mLastMotionY -y;

        if(mFilingDistance > Math.abs(deltaX)){
            if(deltaX > 0){//filing up
                mTouchState = TOUCH_STATE_FLING_UP;
            }else{ //filling down
                mTouchState = TOUCH_STATE_FLING_DOWN;
            }
        }


    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理fling
        final int action  = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        switch (action){
            case MotionEvent.ACTION_UP:
                /*if (mTouchState == TOUCH_STATE_FLING_DOWN ){
                    flingToScreen(mCurrentScreen-1);
                }else if(mTouchState == TOUCH_STATE_FLING_UP){
                    flingToScreen(mCurrentScreen+1);
                }*/
                int screen = getScreen();
                Log.v(TAG, "snap to screen:"+screen);
                scrollToScreen(screen);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouchEvent ACTION_MOVE"+mScroller.getCurrX());
                //dealTouchStateInActionMove(x, y);
                /*if (mTouchState != TOUCH_STATE_SCROLLING && mTouchState != TOUCH_STATE_DOWN) {
                    break;
                }*/
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                smoothScrollBy(deltaX, 0);

        }
        return true;
    }


    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {

        if(mScroller.getFinalX()+dx<=(mAdapter.getPageNum()-1)*width && mScroller.getFinalX()+dx >=0){
            //设置mScroller的滚动偏移量
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
            invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        }
    }


    /**
     * Called by a parent to request that a child update its values for mScrollX
     * and mScrollY if necessary. This will typically be done if the child is
     * animating a scroll using a {@link android.widget.Scroller Scroller}
     * object.
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){//滚动完成
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }else{//滚动未完成

        }
        super.computeScroll();
    }

    /**
     * 滚动到某一屏
     * @param screen
     */
    private void scrollToScreen(int screen){
        int screenNum = mAdapter.getPageNum();
        if(screen >=0 && screen < screenNum){
            float x = screen*width;
            int dx = (int)(x - mScroller.getFinalX());
            smoothScrollBy(dx, 0);
        }
        Log.w(TAG, "screen index error");
    }

    private int getScreen(){
        int x = mScroller.getFinalX();
        return (int)(x/(width/2));
    }

    abstract protected View onGetItemView(int position, View contentView, View parent);

    public void setAdapter(SlidingViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

}
