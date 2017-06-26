package com.Login.Adapter;

/**
 * @author Admin-MuTou_TQ
 *
 *自定义的ViewPagerAdapter ViewPager适配器
 * create at 2016-07-01 23：00 
 */
import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> arrayList) {
		super(fragmentManager);
		this.fragmentList = arrayList;
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}
	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
