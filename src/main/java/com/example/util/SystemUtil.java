package com.example.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.slidingview.ItemInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 保存bitmap到sd卡中
     * @param bitmap
     * @param filename
     */
    public static void writeBitmapToSDcard(Bitmap bitmap, String filename){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Debug/";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, filename);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 保存获取信息到文件中
     */
    private static String savefetchTime2File() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = dateFormat.format(date)+"\r\n";

        try {
            String fileName = "fetchtime.log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Debug/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(path+fileName);
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(str.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
