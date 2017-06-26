package com.Login.viewPager;

import java.util.ArrayList;
import java.util.List;
import com.Login.Adapter.ViewPagerAdapter;
import com.Login.Fragment.Fragment1;
import com.Login.Fragment.Fragment2;
import com.Login.Fragment.Fragment3;
import com.Login.Fragment.Fragment4;
import com.example.hello1.R;
import com.exit.AppExit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

/**
 * ViewPager
 * 
 * @author Admin-MuTou_TQ
 *
 *         create at 2016-07-01 23：00 Pm
 * 
 *         最近一次修改 2016-08-15 10：26 AM
 */
public class ViewPagerActivity extends FragmentActivity {
	private ViewPager mVPActivity; // 引导界面ViewPager
	private Fragment1 mFragment1;
	private Fragment2 mFragment2;
	private Fragment3 mFragment3;
	private Fragment4 mFragment4;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();
	private PagerAdapter mPgAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_viewpager);
		AppExit.getInstance().addActivity(this);
		// 找到组件
		findView();
		// 初始化
		init();
	}

	private void findView() {
		mVPActivity = (ViewPager) findViewById(R.id.activity_vp);
	}

	private void init() {
		mFragment1 = new Fragment1();
		mFragment2 = new Fragment2();
		mFragment3 = new Fragment3();
		mFragment4 = new Fragment4();
		// 向List<Fragment> mListFragment添加数据
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mListFragment.add(mFragment4);
		mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
		mVPActivity.setAdapter(mPgAdapter);
	}
}
