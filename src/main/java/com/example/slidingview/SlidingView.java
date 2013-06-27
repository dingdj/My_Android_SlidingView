package com.example.slidingview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
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

    private List<PageChangeEvent> pageChangeEvents;

    /**
     * Fling灵敏度
     */
    public static final int SNAP_VELOCITY = 2000;

    private Scroller mScroller;

    private float width;

    private int mTouchState = TOUCH_STATE_REST;

    private static int TOUCH_STATE_REST = 0;

    private static final int TOUCH_STATE_SCROLLING = 4;

    private int mMaximumVelocity;

    private VelocityTracker mVelocityTracker;

    public SlidingView(Context context) {
        super(context);
        init();
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init(){
        pageViewCache = new PageViewCache();
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mScroller = new Scroller(getContext());
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.bg);
        setBackgroundDrawable(drawable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.v(TAG, "onLayout changed"+changed);
        int width = r - l;
        Log.v(TAG, "onLayout width"+width);
        int height = b - t;
        Log.v(TAG, "onLayout height"+height);

        if(changed){
            int childWidth = width/mAdapter.getCol();
            int childHeight = height/mAdapter.getRow();
            this.width = width;
            layoutChildren(mCurrentScreen, width, height, childWidth, childHeight);
        }
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
                view.measure(width, height);
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
                Log.v(TAG, "onInterceptTouchEvent ACTION_MOVE");
                int deltaX = (int) (mLastMotionX - x);
                if(Math.abs(deltaX) > 5){
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;

        }
        return mTouchState != TOUCH_STATE_REST;
    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action  = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (action){
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                if(velocityX > SNAP_VELOCITY){
                    scrollToScreen(mCurrentScreen-1);
                }else if(velocityX < -SNAP_VELOCITY){
                    scrollToScreen(mCurrentScreen+1);
                }else{
                    int screen = getScreen();
                    Log.v(TAG, "snap to screen:"+screen);
                    scrollToScreen(screen);
                }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouchEvent ACTION_MOVE"+mScroller.getCurrX());
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                smoothScrollBy((int)(3*deltaX/4), 0);
                break;

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
            //deal page change event
            if(mCurrentScreen != screen){
                if(pageChangeEvents != null){
                    for(PageChangeEvent pageChangeEvent : pageChangeEvents){
                        pageChangeEvent.pageChanged(mCurrentScreen, screen, screenNum);
                    }
                }
                mCurrentScreen = screen;
            }
        }else{
            Log.w(TAG, "screen index error");
        }
    }

    private int getScreen(){
        int x = mScroller.getFinalX();
        Log.v("getScreen x", "x:"+ x);
        float interval = x%width;
        int rtn = (int)(x/width);
        return rtn + (int)(interval/(width/2));
    }

    abstract protected View onGetItemView(int position, View contentView, ViewGroup parent);

    public void setAdapter(SlidingViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public SlidingViewAdapter getAdapter() {
        return mAdapter;
    }

    public void registerPageChangeEvent(PageChangeEvent event){
        if(pageChangeEvents == null){
            pageChangeEvents = new ArrayList<PageChangeEvent>();
        }
        pageChangeEvents.add(event);
    }

    static interface PageChangeEvent{
        void pageChanged(int lastPage, int curPage, int pages);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
