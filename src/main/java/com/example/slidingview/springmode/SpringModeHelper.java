package com.example.slidingview.springmode;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by dingdj on 13-6-30.
 */
public class SpringModeHelper {
    private float springGap;
    private float springShowPart;


    private float normalScreenWidth;
    private float normalScreenHeight;

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
     * 得到缩放的中心点
     * @return
     */
    public int[] getScaleCenterPoint(){
        int y = (int) (normalScreenHeight/10);
        int x  = (int)(normalScreenWidth/2);
        return new int[]{x, y};
    }

    /**
     * 进入到编辑模式
     */
    public void animationToSpringMode(View view){
        ScaleAnimation scaleAnimation;
        float springscale = getSpringScale();
        int[] centerPoint = getScaleCenterPoint();
        scaleAnimation = new ScaleAnimation(1f,springscale,1f,springscale,
                Animation.ABSOLUTE, centerPoint[0],
                Animation.ABSOLUTE, centerPoint[1]);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);
        view.startAnimation(scaleAnimation);
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
