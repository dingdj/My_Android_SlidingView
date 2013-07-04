package com.example.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.slidingview.R;

import java.lang.ref.WeakReference;

/**
 * Created by dingdj on 13-7-4.
 */
public class BitmapUtil {

    public static Bitmap mPlaceHolderBitmap;

    public static ImageCache imageCache;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight){
        //raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            //calculate rtios of height and width to requested height and width
            final int heightRatio = Math.round((float)height/(float)reqHeight);
            final int widthRatio = Math.round((float)width/(float)reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources resources,
                                                         int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }


    public static void loadBitmap(Resources resources, int resId, ImageView imageView){
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = imageCache.getBitmapFromMemCache(imageKey);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            if(cancelPotentialWork(resId, imageView)){
                BitmapWorkerTask task = new BitmapWorkerTask(imageView, resources);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(resources, mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(resId);
            }
        }

    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap){
        imageCache.addBitmapToMemoryCache(key, bitmap);
    }


    public static boolean cancelPotentialWork(int data, ImageView imageView){
        final BitmapWorkerTask bitmapWorkerTask = gitBitmapWorkerTask(imageView);
        if(bitmapWorkerTask != null){
            final int bitmapData = bitmapWorkerTask.getData();
            if(bitmapData != data){
                //Cancel previous task
                bitmapWorkerTask.cancel(true);
            }else{
                //the same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public static BitmapWorkerTask gitBitmapWorkerTask(ImageView imageView){
        if(imageView != null){
            final Drawable drawable = imageView.getDrawable();
            if(drawable instanceof AsyncDrawable){
                final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask){
            super(res, bitmap);
            bitmapWorkerTaskWeakReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask(){
            return bitmapWorkerTaskWeakReference.get();
        }
    }



}
