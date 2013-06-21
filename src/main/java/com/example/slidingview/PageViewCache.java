package com.example.slidingview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 13-6-21.
 */
public class PageViewCache {

    private List<CommonLayout> pageView;

    public CommonLayout getPageView(Context context){
        if(pageView == null || pageView.size() == 0){
            if(pageView == null){
                pageView = new ArrayList<CommonLayout>();
            }
            CommonLayout commonLayout = newCommonLayout(context);
            pageView.add(commonLayout);
            return pageView.remove(0);
        }
        return pageView.remove(0);
    }

    private CommonLayout newCommonLayout(Context context){
        CommonLayout commonLayout = new CommonLayout(context);
        return commonLayout;
    }

    public void retainPageView(CommonLayout commonLayout){
        if(pageView == null){
            pageView = new ArrayList<CommonLayout>();
        }
        pageView.add(commonLayout);
    }
}
