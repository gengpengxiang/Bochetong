package com.bolong.bochetong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class YkAdapter extends FragmentStatePagerAdapter {
	List<Fragment> mList;
	public YkAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		// TODO Auto-generated constructor stub
		mList=list;
	}

	@Override
	public Fragment getItem(int location) {
		// TODO Auto-generated method stub
		Fragment view=mList.get(location);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}


}
