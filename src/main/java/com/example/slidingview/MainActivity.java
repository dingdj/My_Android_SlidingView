package com.example.slidingview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.example.util.SystemUtil;
import java.util.List;

/**
 * this is main activity
 */
public class MainActivity extends Activity {

    CustomSlidingView slidingView;
    LightBar lightBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingView = (CustomSlidingView)findViewById(R.id.slidingview);
        List<ItemInfo> items = SystemUtil.getInstallIcons(this);
        SlidingViewAdapter adapter = new SlidingViewAdapter(items , 4, 4);
        initLightBar(adapter.getPageNum());
        slidingView.setAdapter(adapter);
        slidingView.registerPageChangeEvent(lightBar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initLightBar(int pageNums){
        lightBar = (LightBar)findViewById(R.id.lightBar);
        lightBar.setNormalLighter(getResources().getDrawable(R.drawable.drawer_lightbar_normal));
        lightBar.setSelectedLighter(getResources().getDrawable(R.drawable.drawer_lightbar_checked));
        lightBar.setItems(pageNums);
        lightBar.setCurItem(0);
        lightBar.initLightBar();
    }
    
}
