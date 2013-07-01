package com.example.slidingview.springmode;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.slidingview.SlidingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingdj on 13-6-30.
 */
public class SpringModeHelper {
    private float springGap;
    private float springShowPart;


    private float normalScreenWidth;
    private float normalScreenHeight;

    private float springScreenWidth;
    private float springScreenHeight;

    private static SpringModeHelper instance;

    public static SpringModeHelper getInstance(){
        if(instance == null){
            instance = new SpringModeHelper();
        }
        return instance;
    }

    private SpringModeHelper() {
    }

    public void  initSpringModeParams(float springGap,
                                        float springShowPart,
                                        float normalScreenWidth,
                                        float normalScreenHeight) {
        this.springGap = springGap;
        this.springShowPart = springShowPart;
        this.normalScreenWidth = normalScreenWidth;
        this.normalScreenHeight = normalScreenHeight;
    }

    /**
     * 获取编辑模式下的单屏的宽度
     * @return
     */
    public float getSpringModeWidth(){
        return normalScreenWidth - (springGap+springShowPart)*2;
    }

    /**
     * 获取编辑模式下的缩放比例
     * @return
     */
    public float getSpringScale(){
        return getSpringModeWidth()/normalScreenWidth;
    }


    /**
     * 得到所有屏的缩放中心点 从当前屏开始计算
     * @return
     */
    public List<int[]> getScaleCenterPoint(int viewNums, int curViewIndex){
        springScreenWidth  = normalScreenWidth/2;
        springScreenHeight = normalScreenHeight/10;

        List<int[]> rtn = new ArrayList<int[]>();
        for(int i=0; i< viewNums; i++){
            int v_delta = i - curViewIndex;
            int x  = (int) (0*(springGap+springScreenWidth) + springScreenWidth/2-springGap);
            rtn .add(new int[]{x, (int)springScreenHeight});
        }
        return rtn;
    }

    /**
     * 进入到编辑模式
     */
    public void animationToSpringMode(SlidingView view){
        float springscale = getSpringScale();
        int viewNums = view.getChildCount();
        int mCurrentView = view.getCurrentScreen();
        for(int i=0; i<viewNums; i++){
            AnimationSet animatorSet = new AnimationSet(true);
            ScaleAnimation scaleAnimation;
            scaleAnimation = new ScaleAnimation(1f,springscale,1f,springscale,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.1f);
            scaleAnimation.setFillEnabled(true);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setDuration(500);
            animatorSet.addAnimation(scaleAnimation);
            if(i != mCurrentView){
                float translationToX =   (i - mCurrentView)*((normalScreenWidth-springScreenWidth)/2 + springShowPart);
                //基于缩放动画后的位置的偏移量
                TranslateAnimation translateAnimation = new TranslateAnimation(0f, -translationToX, 0f, 0f);
                translateAnimation.setFillEnabled(true);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(0);
                animatorSet.addAnimation(translateAnimation);
            }
            animatorSet.setFillAfter(true);
            view.getChildAt(i).startAnimation(animatorSet);


        }
    }

    public void setNormalScreenWidth(float normalScreenWidth) {
        this.normalScreenWidth = normalScreenWidth;
    }

    public void setSpringShowPart(float springShowPart) {
        this.springShowPart = springShowPart;
    }

    public void setSpringGap(float springGap) {
        this.springGap = springGap;
    }

    public void setNormalScreenHeight(float normalScreenHeight) {
        this.normalScreenHeight = normalScreenHeight;
    }
}
