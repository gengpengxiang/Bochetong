package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.bolong.bochetong.fragment.Fragment1;
import com.bolong.bochetong.fragment.Fragment2;
import com.bolong.bochetong.fragment.Fragment3;
import com.bolong.bochetong.fragment.Fragment4;
import com.bolong.bochetong.utils.StatusBarUtil;
import com.bolong.bochetong.view.ViewPagerCompat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPagerCompat viewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private Unbinder unbind;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide);
        unbind = ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.compat(this, Color.parseColor("#000000"));
        }

        initViews();
        setSelect(0);
    }

    private void initViews() {
        mFragments = new ArrayList<Fragment>();
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int i)
    {
        viewPager.setCurrentItem(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            GuideActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }
}
