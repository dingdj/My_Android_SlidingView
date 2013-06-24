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

        layoutChildren(mCurrentScreen, width, height, childWidth, childHeight);

    }

    /**
     *
     * @param width
     * @param height
     */
    protected void layoutChildren(int currentScreen, int screenWidth, int screenHeight, int width, int height){
        int pageSize = mAdapter.getPageSize();
        makePages(currentScreen, pageSize-1, screenWidth, screenHeight, width, height);
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
        for(int screen = startPage; screen<endPage+1; screen++){
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
        int l = screenWidth;
        int t = 0;
        for(int i=0; i<mAdapter.getRow(); i++){
            for(int j=0; j<mAdapter.getCol(); j++){
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




    /**
     * Implement this method to intercept all touch screen motion events.  This
     * allows you to watch events as they are dispatched to your children, and
     * take ownership of the current gesture at any point.
     * <p/>
     * <p>Using this function takes some care, as it has a fairly complicated
     * interaction with {@link android.view.View#onTouchEvent(android.view.MotionEvent)
     * View.onTouchEvent(MotionEvent)}, and using it requires implementing
     * that method as well as this one in the correct way.  Events will be
     * received in the following order:
     * <p/>
     * <ol>
     * <li> You will receive the down event here.
     * <li> The down event will be handled either by a child of this view
     * group, or given to your own onTouchEvent() method to handle; this means
     * you should implement onTouchEvent() to return true, so you will
     * continue to see the rest of the gesture (instead of looking for
     * a parent view to handle it).  Also, by returning true from
     * onTouchEvent(), you will not receive any following
     * events in onInterceptTouchEvent() and all touch processing must
     * happen in onTouchEvent() like normal.
     * <li> For as long as you return false from this function, each following
     * event (up to and including the final up) will be delivered first here
     * and then to the target's onTouchEvent().
     * <li> If you return true from here, you will not receive any
     * following events: the target view will receive the same event but
     * with the action {@link android.view.MotionEvent#ACTION_CANCEL}, and all further
     * events will be delivered to your onTouchEvent() method and no longer
     * appear here.
     * </ol>
     *
     * @param ev The motion event being dispatched down the hierarchy.
     * @return Return true to steal motion events from the children and have
     *         them dispatched to this ViewGroup through onTouchEvent().
     *         The current target will receive an ACTION_CANCEL event, and no further
     *         messages will be delivered here.
     */
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
                if (mTouchState == TOUCH_STATE_FLING_DOWN ){
                    flingToScreen(mCurrentScreen-1);
                }else if(mTouchState == TOUCH_STATE_FLING_UP){
                    flingToScreen(mCurrentScreen+1);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouchEvent ACTION_DOWN");
                //dealTouchStateInActionMove(x, y);
                /*if (mTouchState != TOUCH_STATE_SCROLLING && mTouchState != TOUCH_STATE_DOWN) {
                    break;
                }*/
                int deltaX = (int) (mLastMotionX - x);
                Log.v(TAG, "scrollBy");
                scrollBy(deltaX, 0);
        }
        return true;
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

    private void flingToScreen(int screen){

    }

    abstract protected View onGetItemView(int position, View contentView, View parent);

    public void setAdapter(SlidingViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

}
