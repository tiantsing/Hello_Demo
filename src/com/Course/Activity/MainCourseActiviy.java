package com.Course.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;
import com.Course.Interface.MainCourseInterface;
import com.Course.Presenter.MainCoursePresenter;
import com.Course.db.Coursedb;
import com.Discover.Activity.discoverActivity;
import com.Login.Util.Utils;
import com.MainPage.Activity.Main_PageActiviy;
import com.Message.Activity.MessageActivity;
import com.PJ.pj.testTeacher;
import com.app.main.ActionItem;
import com.app.main.HelloApplication;
import com.app.main.TitlePopup;
import com.app.main.TitlePopup.OnItemOnClickListener;
import com.example.hello1.R;
import com.exit.AppExit;
import com.profile.User;
import com.pub.DensityUtils;
import com.pub.ScreenUtils;
import com.pub.SingleGlobalVariables;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainCourseActiviy extends Activity implements MainCourseInterface {
	public static final int TOCOURSE = 1;
	static final int COURSE2LESSON_DETAIL = 1;
	static final int COURSE2ADD_LESSON = 2;
	static final int COURSE2MAIN_PAGE = 3;
	static final int COURSE2SET = 4;
	public static final int COURSE2MAINPAGE = 5;
	static final int MONDAY = 1;
	static final int TUESDAY = 2;
	static final int WEDNESDAY = 3;
	static final int THURSDAY = 4;
	static final int FRIDAY = 5;
	static final int SATURDAY = 6;
	static final int SUNDAY = 7;
	final int week_num = 25;// 周数
	String phonepath;
	TextView tv_month;
	TextView tv_day1;
	TextView tv_day2;// 课表第一行显示的日期时间
	TextView tv_day3;
	TextView tv_day4;
	TextView tv_day5;
	TextView tv_day6;
	TextView tv_day7;
	Intent intent;
	int current_Weeknum;// 当前是第几周
	int showingWeek;// 显示的是第几周
	RelativeLayout weekPanelsLV[] = new RelativeLayout[7];
	RelativeLayout courseLayoutRV;
	RelativeLayout reparent;
	LinearLayout linearLayout_0;
	@SuppressWarnings("unchecked")
	List<Coursedb> courseData[] = new ArrayList[7];// 获取到一周的课表信息
	int aveWidth, gridHeight;// 平均高度.宽度
	int lineHeight;// 分割线高度
	int marTop, marLeft;// 左右间隔
	SharedPreferences sf_userinfo;
	MainCoursePresenter coursePresenter;
	TextView tv1;
	LinearLayout layout;
	ListView listView;
	PopupWindow popupWindow;
	TitlePopup titlePopup;

	// //////////////////////////重载函数/////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.course_main);
		AppExit.getInstance().addActivity(this);
		// 初始化////////////////////////////////////////////////////////////////////////
		init();
		initPopWindow();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case COURSE2LESSON_DETAIL:
			if (resultCode == Lesson_Detail.RESULT) {
				init_sf();
				update_Course(showingWeek);
			}
			break;
		case COURSE2ADD_LESSON:
			if (resultCode == Add_LessonActivity.RESULTCODE) {
				// 更新
				init_sf();
				update_Course(showingWeek);
			}
			break;
		case COURSE2SET:
			if (resultCode == Course_Set.RESULT) {
				// 更新
				init_sf();
				init_popweek();
				coursePresenter.getTodayDate(showingWeek - current_Weeknum);// 设置显示的日期，把0改为当前周。
				update_Course(showingWeek);
			}
			break;
		}
	}

	// //////////////////////////初始化控件//////////////////////////////////////////////////////////

	public void init() {
		RelativeLayout kb = (RelativeLayout) findViewById(R.id.open_course);
		kb.setSelected(true);
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_day1 = (TextView) findViewById(R.id.tv_day1);
		tv_day2 = (TextView) findViewById(R.id.tv_day2);
		tv_day3 = (TextView) findViewById(R.id.tv_day3);
		tv_day4 = (TextView) findViewById(R.id.tv_day4);
		tv_day5 = (TextView) findViewById(R.id.tv_day5);
		tv_day6 = (TextView) findViewById(R.id.tv_day6);
		tv_day7 = (TextView) findViewById(R.id.tv_day7);
		coursePresenter = new MainCoursePresenter(this, this);
		gridHeight = (ScreenUtils.getScreenHeight(this)) / 12;
		courseLayoutRV = (RelativeLayout) findViewById(R.id.courseLayout);
		linearLayout_0 = (LinearLayout) findViewById(R.id.weekPanel_0);
		// 获取各种高度信息
		lineHeight = getResources().getDimensionPixelSize(R.dimen.line_height);
		marTop = getResources().getDimensionPixelSize(R.dimen.MarTop);
		marLeft = getResources().getDimensionPixelSize(R.dimen.MarLeft);
		aveWidth = (ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 35)) / 7;
		// 初始化1-7列课表
		for (int i = 0; i < weekPanelsLV.length; i++) {
			weekPanelsLV[i] = (RelativeLayout) findViewById(R.id.weekPanel_1 + i);
		}
		init_sf();
		init_CourseSet();
		init_BtnBack();
		init_popweek();
		init_WeekPanel_0(linearLayout_0);// 初始化第一列
		init_Grid();// 初始化格子
		coursePresenter.getTodayDate(showingWeek - current_Weeknum);// 设置显示的日期，把0改为当前周。
		coursePresenter.getCourseData(current_Weeknum);// 获得数据
		for (int i = 0; i < weekPanelsLV.length; i++) {
			init_WeekPanel(weekPanelsLV[i], courseData[i], i);// 初始化课程表
		}
	}

	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_zy:
			intent = new Intent(MainCourseActiviy.this, Main_PageActiviy.class);
			startActivity(intent);
			break;
		case R.id.open_course:
			break;
		case R.id.re_message:
			Intent intent11 = new Intent(MainCourseActiviy.this, MessageActivity.class);
			startActivity(intent11);
			break;
		case R.id.re_Find:
			Intent intent111 = new Intent(MainCourseActiviy.this, discoverActivity.class);
			startActivity(intent111);
			break;
		case R.id.re_profile:
			open_profile();
			break;
		}
	}

	// 打开教师评价
	public void open_pj() {
		intent = new Intent(MainCourseActiviy.this, testTeacher.class);
		startActivity(intent);

	}

	// 打开课程表
	public void open_course() {
		intent = new Intent(MainCourseActiviy.this, com.Course.Activity.MainCourseActiviy.class);
		startActivityForResult(intent, TOCOURSE);

	}

	// 打开程序锁
	public void open_app_lock() {
		// TODO Auto-generated method stub
		intent = new Intent(MainCourseActiviy.this, com.app_lock.main_time_control.class);
		startActivity(intent);

	}

	// 打开个人
	public void open_profile() {
		Intent intent = new Intent(MainCourseActiviy.this, User.class);
		startActivity(intent);
	}

	public void init_popweek()// 设置切换周数按钮
	{
		tv1 = (TextView) findViewById(R.id.spinner1);
		tv1.setText("第" + current_Weeknum + "周");
		showingWeek = current_Weeknum;
		Log.i("显示的周数是", showingWeek + "");
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(tv1);
			}
		});
	}

	@SuppressLint("InflateParams")
	public void showPopupWindow(View parent) {
		layout = (LinearLayout) LayoutInflater.from(MainCourseActiviy.this).inflate(R.layout.course_title_dialog, null);
		reparent = (RelativeLayout) findViewById(R.id.rela);
		List<String> list = new ArrayList<String>();
		String week[] = getResources().getStringArray(R.array.allweeks);
		for (int i = 1; i <= 25; i++) {
			if (i == current_Weeknum)
				list.add(week[i]);
			else
				list.add(week[i] + "(非本周)");
		}
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainCourseActiviy.this, R.layout.text,
				R.id.tv_text, list);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(layout, 500, 500);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(reparent, xpos, 15);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				showingWeek = position + 1;
				String strPreview = adapter.getItem(position);// 获取到的值
				tv1.setText(strPreview);// 设置显示
				coursePresenter.getTodayDate(showingWeek - current_Weeknum);
				update_Course(showingWeek);
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}

	// 初始化SharedPreferences
	public void init_sf() {
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(MainCourseActiviy.this, "userinfo", true, phonepath, sf_userinfo);
		current_Weeknum = sf_userinfo.getInt("current_week_num", 1);
	}

	// 初始化返回按钮
	public void init_BtnBack() {
		ImageView back = (ImageView) findViewById(R.id.course_back_main_page);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(COURSE2MAIN_PAGE);
				finish();
			}
		});
	}

	// 初始化设置按钮
	public void init_CourseSet() {
		final ImageView btn_set = (ImageView) findViewById(R.id.course_set);
		btn_set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				titlePopup.show(findViewById(R.id.rela));
			}
		});
	}

	// 添加课程泡泡窗口
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "添加课程", R.drawable.ic_add_course_pressed));
		// titlePopup.addAction(new ActionItem(this, "设置",
		// R.drawable.ic_dlg_remind_set_pressed));
		// titlePopup.addAction(new ActionItem(this, "扫一扫",
		// R.drawable.ic_bar_copycourse_press));
	}

	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {
		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0://
				Intent intent = new Intent(MainCourseActiviy.this, Add_LessonActivity.class);
				startActivityForResult(intent, COURSE2ADD_LESSON);
				break;
			case 1://
				Intent intent1 = new Intent(MainCourseActiviy.this, Course_Set.class);
				startActivityForResult(intent1, COURSE2SET);
				break;
			case 2://
				Utils.start_Activity(MainCourseActiviy.this, Add_LessonActivity.class);
				break;
			default:
				break;
			}
		}
	};

	// 初始化第一列，显示1-12节的上课时间
	public void init_WeekPanel_0(LinearLayout ll0) {
		for (int i = 1; i <= 12; i++) {
			TextView timeTextView = new TextView(this);
			LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 35);
			timeTextView.setGravity(Gravity.CENTER);
			timeTextView.setTextSize(8);
			String timesString = coursePresenter.getTime(i);
			timeTextView.setText(timesString);

			ll0.addView(timeTextView, rp);

			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, lineHeight);
			imageView.setBackgroundColor(Color.parseColor("#696969"));
			ll0.addView(imageView, lp1);

			TextView textView = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, gridHeight - 35);
			lp.setMargins(marLeft * 2, marTop, 0, 0);
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(12);
			textView.setText(i + "");
			ll0.addView(textView, lp);

			ImageView imageView1 = new ImageView(this);
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, lineHeight);
			imageView1.setBackgroundColor(Color.parseColor("#696969"));

			ll0.addView(imageView1, lp2);
		}
	}

	// 初始化一天的课程，并在每节课上添加监听，查看该节课的信息
	public void init_WeekPanel(RelativeLayout ll, List<Coursedb> data, int j) {
		if (ll == null || data == null || data.size() < 1)
			return;
		Coursedb pre = data.get(0);
		int id;
		for (int i = 0; i < data.size(); i++) {
			Coursedb c = data.get(i);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
					(gridHeight + 3 * lineHeight) * (c.getStop() - c.getStart() + 1));
			if (c.getStart() - 1 == 0)
				lp.setMargins(marLeft, marTop, marLeft, marTop);
			else
				lp.setMargins(marLeft,
						(c.getStart() - 1) * (gridHeight + marTop + lineHeight * 2) - 3 * lineHeight + marTop, marLeft,
						marTop);

			TextView tv = new TextView(this);
			id = j * 10 + i;
			tv.setId(id);
			c.setTextViewId(id);
			tv.setGravity(Gravity.TOP);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setTextSize(12);
			//设置背景
            tv.setBackgroundResource(coursePresenter.getcolor(c.getName()));
			tv.getBackground().setAlpha(180);
			tv.setTextColor(getResources().getColor(R.color.courseTextColor));
			tv.setText(c.getName() + "@" + c.getRoom());
			tv.setLayoutParams(lp);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					show_LessonDetail(arg0.getId());
				}
			});
			
			ll.addView(tv);
			pre = c;
		}
	}

	// 添加小点
	public void init_Grid() {
		for (int i = 1; i <= 12; i++) {// 琛�
			for (int j = 1; j <= 8; j++) {// 鍒�
				add_Dot(i, j);// 灏忛粦鐐�
			}
		}
	}

	// 生成小点
	public void add_Dot(int i, int j) {
		if (j == 8) {
			return;
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(this, 2),
				DensityUtils.dp2px(this, 2));
		params.topMargin = i * (gridHeight + marTop + lineHeight * 2) - 3 * lineHeight;
		params.leftMargin = (aveWidth) * j;
		ImageView view = new ImageView(this);
		view.setLayoutParams(params);
		view.setBackgroundColor(getResources().getColor(R.color.week_view_text_date));
		courseLayoutRV.addView(view);
	}

	// //////////////////////普通函数////////////////////////////////////////////////////////
	// 将所有的每列课表的view清空
	public void remove_AllViews(RelativeLayout ll) {
		ll.removeAllViews();
	}

	// 更新课表数据
	public void update_Course(int week) {
		coursePresenter.getCourseData(week);// 获得数据
		for (int i = 0; i < weekPanelsLV.length; i++) {
			remove_AllViews(weekPanelsLV[i]);
			init_WeekPanel(weekPanelsLV[i], courseData[i], i);
		}
	}

	// 查看课表详细信息
	public void show_LessonDetail(int id) {
		String name;
		String classroom;
		String teacher;
		int lesson_start, lesson_end;
		int week = id / 10;
		List<Coursedb> data = courseData[week];
		for (int j = 0; j < data.size(); j++) {
			Coursedb course = data.get(j);
			if (course.getTextViewId() == id) {
				name = course.getName();
				classroom = course.getRoom();
				teacher = course.getTeach();
				lesson_start = course.getStart();
				lesson_end = course.getStop();
				Intent intent = new Intent();
				intent.setClass(MainCourseActiviy.this, Lesson_Detail.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putString("classroom", classroom);
				bundle.putString("teacher", teacher);
				bundle.putInt("lesson_start", lesson_start);
				bundle.putInt("lesson_end", lesson_end);
				bundle.putInt("lesson_week", week + 1);
				bundle.putInt("when_week", showingWeek);
				intent.putExtras(bundle);
				startActivityForResult(intent, COURSE2LESSON_DETAIL);
			}
		}
	}

	@Override
	public void getMonthDate(String Date) {
		// TODO Auto-generated method stub
		tv_month.setText(Date);
	}

	@Override
	public void getMondayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day1.setText(Date);
	}

	@Override
	public void getTuesdayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day2.setText(Date);
	}

	@Override
	public void getWednesdayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day3.setText(Date);
	}

	@Override
	public void getThursdayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day4.setText(Date);
	}

	@Override
	public void getFridayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day5.setText(Date);
	}

	@Override
	public void getSaturdayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day6.setText(Date);
	}

	@Override
	public void getSundayDate(String Date) {
		// TODO Auto-generated method stub
		tv_day7.setText(Date);
	}

	@Override
	public void getCourseData(List<Coursedb>[] Data) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 7; i++) {
			courseData[i] = (List<Coursedb>) Data[i];

		}
	}

}
