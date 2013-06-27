package com.example.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.slidingview.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingdj on 13-6-26.
 */
public class SystemUtil {

    public static List<ItemInfo>  getInstallIcons(Context context){
        List<ItemInfo> items = new ArrayList<ItemInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for(PackageInfo packageInfo : packages){
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0){
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setDrawable(packageInfo.applicationInfo.loadIcon(pm));
                itemInfo.setTitle(packageInfo.applicationInfo.loadLabel(pm).toString());
                items.add(itemInfo);
            }
        }

        return items;

    }

    public static int dip2px(Context context, float dipValue) {
        float currentDensity = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * currentDensity + 0.5f);
    }



}
