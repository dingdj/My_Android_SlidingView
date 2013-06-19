package com.example.slidingview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainActivity extends Activity {

    CustomSlidingView slidingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingView = (CustomSlidingView)findViewById(R.id.slidingview);
        List<ItemInfo> list = new ArrayList<ItemInfo>();
        list.add(new ItemInfo("", null));
        list.add(new ItemInfo("", null));
        list.add(new ItemInfo("", null));
        list.add(new ItemInfo("", null));
        list.add(new ItemInfo("", null));
        list.add(new ItemInfo("", null));
        SlidingViewAdapter adapter = new SlidingViewAdapter(list , 2, 2);
        slidingView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
