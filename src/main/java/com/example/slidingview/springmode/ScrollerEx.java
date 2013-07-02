package com.example.slidingview.springmode;

import android.content.Context;
import android.opengl.GLDebugHelper;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.example.slidingview.SlidingView;
import com.example.util.LogHelper;

/**
 * Created by dingdj on 13-7-2.
 */
public class ScrollerEx extends Scroller{

    private SlidingView.Mode mode = SlidingView.Mode.Normal;

    //可滑动的最左边的限制
    private int leftLimit;

    //可滑动的最右边的限制
    private int rightLimit;

    //normal Screen Width
    private int screenWidth;

    public ScrollerEx(Context context) {
        super(context);
    }

    public ScrollerEx(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public SlidingView.Mode getMode() {
        return mode;
    }

    public void setMode(SlidingView.Mode mode) {
        this.mode = mode;
    }

    public int getRightLimit() {
        return rightLimit;
    }

    public void setRightLimit(int rightLimit) {
        this.rightLimit = rightLimit;
    }

    public int getLeftLimit() {
        return leftLimit;
    }

    public void setLeftLimit(int leftLimit) {
        this.leftLimit = leftLimit;
    }

    /**
     * @param screenNum
     * @param curScreen
     * @param mode
     * @param screenWidth
     */
    public void initScrollerParams(int screenNum, int curScreen,
                                   SlidingView.Mode mode,
                                   int screenWidth){

        this.mode = mode;
        if(mode == SlidingView.Mode.Normal){
            this.rightLimit = 0;
            this.leftLimit = (screenNum-1)*screenWidth;
        }else if(mode == SlidingView.Mode.Spring){
            int finalX = this.getFinalX();
            SpringModeHelper springModeHelper = SpringModeHelper.getInstance();
            float springGap = springModeHelper.getSpringGap();
            float springScreenWidth = springModeHelper.getSpringScreenWidth();
            this.rightLimit = (int) (finalX - curScreen *
                    (screenWidth - springGap - springScreenWidth));
            this.leftLimit = (int)(finalX + (screenNum-curScreen-1)*(springGap+springScreenWidth));
        }else{
            Log.w("ScrollEx.initScrollerParams", "unknown mode");
        }
    }

    /**
     * 得到每个屏相对滚动位置的偏移量
     * @param screen
     * @return
     */
    public Float getScrollXByScreen(int screen, float screenWidth){
        if(mode == SlidingView.Mode.Normal){
            return screen*screenWidth;
        }else if(mode == SlidingView.Mode.Spring){
            SpringModeHelper springModeHelper = SpringModeHelper.getInstance();
            float springGap = springModeHelper.getSpringGap();
            float springScreenWidth = springModeHelper.getSpringScreenWidth();
            LogHelper.d("SlidingView","flingWidth:"+rightLimit + screen * (springGap+springScreenWidth));
            return rightLimit + screen * (springGap+springScreenWidth);
        }else{
            Log.w("ScrollEx.getScrollXByScreen", "unknown mode");
            return null;
        }
    }
}
