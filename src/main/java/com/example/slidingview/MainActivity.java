package com.example.slidingview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.example.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * this is main activity
 */
public class MainActivity extends Activity {

    CustomSlidingView slidingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingView = (CustomSlidingView)findViewById(R.id.slidingview);
        List<ItemInfo> items = SystemUtil.getInstallIcons(this);
        SlidingViewAdapter adapter = new SlidingViewAdapter(items , 4, 4);
        slidingView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
