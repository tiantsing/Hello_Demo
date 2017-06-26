package com.PJ.pj;

import java.util.ArrayList;
import java.util.List;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import com.MainPage.Activity.Main_PageActiviy;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 教师评教类
 * @author Sun
 *
 */
public class testTeacher extends Activity implements android.view.View.OnClickListener {
	DBManager mgr;// 数据库Manager
	List<Coursedb> courseData;
	ImageView back;
	TextView title;// 标题
	TextView[] text1, text2;
	View view1;// 需要滑动的页卡
	View[] views;// 叶片数组
	View noclassview;// 无课程
	ViewPager mVPActivity;
	PagerTitleStrip pagerTitleStrip;// viewpager的标题
	PagerTabStrip pagerTabStrip;// 一个viewpager的指示器，效果就是一个横的粗的下划线
	List<View> viewList;// 把需要滑动的页卡添加到这个list中
	List<String> titleList;// viewpager的标题
	int num;// List大小

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.pj_teacher_main);
		AppExit.getInstance().addActivity(this);
		init();
		initViewPager();
	}

	/**
	 * 初始化变量
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void init() {
		mgr = new DBManager(testTeacher.this);
		title = (TextView) findViewById(R.id.txt_title);
		back = (ImageView) findViewById(R.id.img_back);
		// ViewPager
		mVPActivity = (ViewPager) findViewById(R.id.pj_activity);
		courseData = mgr.query_AllCourseAndTeacher();
		title.setText("评测列表");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		views = new View[10];
		text1 = new TextView[10];
		text2 = new TextView[10];
		num = courseData.size();
	}

	/** 初始化ViewPager
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void initViewPager() {
		view1 = findViewById(R.layout.pj_main);
		// 加载布局管理器
		LayoutInflater lf = getLayoutInflater().from(this);
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		titleList = new ArrayList<String>();// 每个页面的Title数据

		if (num <= 0) {
			noclassview = lf.inflate(R.layout.pj_noclass_bg, null);
			// 添加title
			titleList.add("无信息");
			viewList.add(noclassview);
		} else {
			for (int i = 0; i <= num; i++) {
				views[i] = view1;
			}
			for (int i = 0; i <= num; i++) {
				// 将xml布局转换为view对象
				views[i] = lf.inflate(R.layout.pj_main, null);
			}
			for (int i = 0; i < num; i++) {
				String name1 = courseData.get(i).getName();
				String teacher1 = courseData.get(i).getTeach();
				if (name1 != null && teacher1 != null) {
					// 利用view对象，找到布局中的组件
					text1[i] = (TextView) views[i].findViewById(R.id.pj_teacher_name);// 教师名称
					text2[i] = (TextView) views[i].findViewById(R.id.pj_course_name);// 课程名称
					text1[i].setText(name1);
					text2[i].setText(teacher1);
				} else {
					text1[i].setText("123");
					text2[i].setText("123");
				}
				titleList.add(name1);
				viewList.add(views[i]);
			}
		}
		mVPActivity.setAdapter(new pagerAdapter());
	}

    /** ViewPage适配器类
     * @author Sun
     * 
     */
	public class pagerAdapter extends PagerAdapter {
		// View个数
		@Override
		public int getCount() {

			return viewList.size();
		}

		// 当调用这个方法时销毁View
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));

		}

		// View Title
		public CharSequence getPageTitle(int position) {

			return titleList.get(position);// 直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_pager_demo, menu);
		return true;
	}
    /**
     * 监听事件
     * @param v
     */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Intent intent = new Intent(testTeacher.this, Main_PageActiviy.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
