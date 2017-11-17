package com.bolong.bochetong.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerCompat extends ViewPager {

    public ViewPagerCompat(Context context) {
        super(context);
    }

    public ViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v.getClass().getName().equals("com.baidu.mapapi.map.TextureMapView")) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }


}
