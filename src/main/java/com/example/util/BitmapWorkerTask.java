package com.example.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by dingdj on 13-7-4.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{

    private final WeakReference<ImageView> imageViewWeakReference;
    private int data = 0;
    private Resources resources;

    public BitmapWorkerTask(ImageView imageView, Resources resources) {

        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.resources = resources;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        final Bitmap bitmap =  BitmapUtil.decodeSampledBitmapFromResource(resources, data,
                100, 100);
        BitmapUtil.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap = null;
        }
        if(imageViewWeakReference != null && bitmap != null){
            final ImageView imageView = imageViewWeakReference.get();
            final BitmapWorkerTask bitmapWorkerTask = BitmapUtil.gitBitmapWorkerTask(imageView);

            if(this == bitmapWorkerTask && imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public int getData() {
        return data;
    }
}
