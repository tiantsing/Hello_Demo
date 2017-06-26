package com.MainPage.Activity;

import java.util.List;
import java.util.Map;
import com.Course.Activity.Lesson_Detail;
import com.Course.Activity.MainCourseActiviy;
import com.Course.db.Coursedb;
import com.Discover.Activity.discoverActivity;
import com.Login.login.LoginActivity;
import com.MainPage.Interface.Main_PageInterface;
import com.MainPage.Model.ListViewCourseAdapter;
import com.MainPage.Presenter.Main_pagePresenter;
import com.Message.Activity.MessageActivity;
import com.PJ.pj.testTeacher;
import com.app.main.HelloApplication;
import com.profile.User;
import com.pub.DealString;
import com.pub.SingleGlobalVariables;
import com.pub.Dialog.AuthorizationDialog;
import com.app_lock.service.HavingClass;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

/**
 * 主页的Activity类
 * 
 * @author Sun
 * 
 *
 */
public class Main_PageActiviy extends Activity implements Main_PageInterface {
	public static final int TOCOURSE = 1;
	public static final int TODETAIL = 2;
	ListView li;// 课程表的listview
	List<Coursedb> courseData;// 今天的所有课程
	List<Map<String, Object>> mData;// 今天的所有课程
	ListViewCourseAdapter adapter;// 今日课程课程的适配器
	Intent intent;
	TextView open_app_lockLayout;
	TextView open_course;
	SharedPreferences sf_userinfo;
	TextView weektextView;
	Handler handler = new Handler();
	TextView semeter;
	TextView whichWeek;
	TextView testteacherTextView;
	TextView mainPage_empty_course_today_bgtv;
	Main_pagePresenter main_pagePresenter;
	AuthorizationDialog authorizationDialog;
	ImageView mainPage_empty_course_today_bg;
	String phonepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainpage);
		AppExit.getInstance().addActivity(this);
		///////////////////////////////////////////////////////////////
//		  // 测试 SDK 是否正常工作的代码
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });
/////////////////////////////////////////////////////////////////////////
		RelativeLayout zy = (RelativeLayout) findViewById(R.id.re_zy);
		zy.setSelected(true);
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(Main_PageActiviy.this, "userinfo", true, phonepath, sf_userinfo);
		authorizationDialog = new AuthorizationDialog(Main_PageActiviy.this);
		main_pagePresenter = new Main_pagePresenter(this, this);
		mainPage_empty_course_today_bg = (ImageView) findViewById(R.id.mainPage_empty_course_today_bg);
		mainPage_empty_course_today_bgtv = (TextView) findViewById(R.id.mainPage_empty_course_today_bgTv);
		main_pagePresenter.getApp();
		main_pagePresenter.getCoursedb();
		main_pagePresenter.getData();
		// 下面3条注释 勿删
		// smslistview = (ListView)
		// findViewById(R.id.mainpage_ListView_schoolmsg);
		// List<Map<String, Object>> list = getschoolsmsData();
		// smslistview.setAdapter(new mainpage_listsms_adapter(this, list));
		init_KJ();
		init_ListCourse();
		// 开启“程序锁”
		startService();
	}

	// @TargetApi(19)
	// private void setTranslucentStatus(boolean on) {
	// Window win = getWindow();
	// WindowManager.LayoutParams winParams = win.getAttributes();
	// final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	// if (on) {
	// winParams.flags |= bits;
	// } else {
	// winParams.flags &= ~bits;
	// }
	// win.setAttributes(winParams);
	// }
	/**
	 * 重定义onKeyDown事件，当按下后退键时，直接退到HOME
	 * 
	 * @author Sun
	 */
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

	/**
	 * 重定义Activity返回事件，根据返回的值不同，触发不同的操作
	 * 
	 * @author Sun
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TOCOURSE:
			if (resultCode == MainCourseActiviy.COURSE2MAINPAGE) {
				main_pagePresenter.getApp();
				main_pagePresenter.getCoursedb();
				main_pagePresenter.getData();
				init_ListCourse();
			}
			break;
		case TODETAIL:
			if (resultCode == Lesson_Detail.RESULT) {
				main_pagePresenter.getApp();
				main_pagePresenter.getCoursedb();
				main_pagePresenter.getData();
				init_ListCourse();
			}
			break;
		}
	}

	// public void isExit() {
	// new
	// AlertDialog.Builder(Main_PageActiviy.this).setTitle("确定退出吗？").setMessage("上课期间退出会被视为逃课哦！")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// AppExit exit = AppExit.getInstance();
	// exit.getConext(Main_PageActiviy.this);
	// exit.stopService();
	// exit.exit();
	// }
	// }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// }
	// }).show();
	// }
	/**
	 * 底栏触摸事件，根据值不同触发相应的事件
	 * 
	 * @param view
	 * @author Sun
	 * @return void
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_zy:
			break;
		case R.id.open_course:
			open_course();
			break;
		case R.id.re_message:
			Intent intent = new Intent(Main_PageActiviy.this, MessageActivity.class);
			startActivity(intent);
			break;
		case R.id.re_Find:
			Intent intent1 = new Intent(Main_PageActiviy.this, discoverActivity.class);
			startActivity(intent1);
			break;
		case R.id.re_profile:
			open_profile();
			break;
		}
	}

	/**
	 * 响应主页中部功能栏点击事件
	 * 
	 * @param view
	 * @author Sun
	 */
	public void onFunctionClicked(View view) {
		switch (view.getId()) {
		case R.id.re_cxs:
			if (SingleGlobalVariables.HaveClass) {
				Toast.makeText(this, "正在上课", Toast.LENGTH_SHORT).show();
			} else
				open_app_lock();
			break;
		case R.id.re_d1:
			open_pj();
			break;
		}
	}

	/**
	 * 初始化控件
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void init_KJ() {
		weektextView = (TextView) findViewById(R.id.mainpage_today_date1);
		semeter = (TextView) findViewById(R.id.mainpage_semeter);
		whichWeek = (TextView) findViewById(R.id.mainpage_week);
		li = (ListView) findViewById(R.id.mainpage_ListView);
	}

	/**
	 * 新开线程检查系统是不是5.0以后的，是的话就去开启查看最近程序使用情况的权限
	 * 
	 * @author Sun
	 * 
	 */
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				authorizationDialog.isSetAuthorization();
			} else {
				SingleGlobalVariables.IsSetRecentAppUse = true;
			}
		}
	};

	/**
	 * 检查今天是否有课，有课则检查时间，无课无操作
	 * 
	 * @return boolean
	 * @param void
	 * @author Sun
	 */
	//
	public boolean cheack_havingLesson() {
		if (mData.size() == 0)
			return false;
		else {
			int size = 0;
			for (int i = 0; i < 12; i++) {
				SingleGlobalVariables.lessonNum[i] = 0;
			}
			for (int i = 0; i < mData.size(); i++) {
				int Course[] = new int[12];
				Course = DealString.dealStarttoStop((Integer) mData.get(i).get("start"),
						(Integer) mData.get(i).get("stop"));
				for (int j = 0; j < 12; j++) {
					if (Course[j] != 0) {
						SingleGlobalVariables.lessonNum[size] = Course[j];
						size++;
					}
				}
			}
			return true;
		}
	}

	/**
	 * 开启“程序锁”
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void startService() {
		Log.i("MainActivity", "11111111");
		if (cheack_havingLesson()) {
			mainPage_empty_course_today_bg.setVisibility(View.GONE);
			mainPage_empty_course_today_bgtv.setVisibility(View.GONE);
			if (!SingleGlobalVariables.IshavingStart && !SingleGlobalVariables.TeaOpen) {
				Intent intent = new Intent(this, HavingClass.class);
				startService(intent);
			}
		} else {
			mainPage_empty_course_today_bg.setVisibility(View.VISIBLE);
			mainPage_empty_course_today_bgtv.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化首页今日课程表
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void init_ListCourse() {
		adapter = new ListViewCourseAdapter(this, mData);
		weektextView.setText(DealString.DealMonth() + DealString.DealDay() + " " + DealString.DealWeekString());
		semeter.setText(DealString.DealSemeter(sf_userinfo) + " " + DealString.DealTerm(sf_userinfo));
		whichWeek.setText("第" + (sf_userinfo.getInt("current_week_num", 1)) + "周");
		li.setAdapter(adapter);
		//////////// 使ListView大小随内容改变
		int totalHeight = 0;
		int num = adapter.getCount();
		for (int i = 0; i < num; i++) {
			View listItem = adapter.getView(i, null, li);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = li.getLayoutParams();
		params.height = totalHeight + (li.getDividerHeight() * (num - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		li.setLayoutParams(params);
		/////////////////////////////////////////////
		li.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				show_LessonDetail(arg2);
			}
		});
	}

	/**
	 * 根据传入的位置来确定显示的Lesson细节
	 * 
	 * @author Sun
	 * @return void
	 * @param id
	 */
	public void show_LessonDetail(int id) {
		String name, classroom, teacher;
		int lesson_start, lesson_end, week, when_week;
		Coursedb course = courseData.get(id);
		name = course.getName();
		classroom = course.getRoom();
		teacher = course.getTeach();
		lesson_start = course.getStart();
		lesson_end = course.getStop();
		week = DealString.DealWeek();
		when_week = sf_userinfo.getInt("current_week_num", 1);
		Intent intent = new Intent();
		intent.setClass(Main_PageActiviy.this, Lesson_Detail.class);
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putString("classroom", classroom);
		bundle.putString("teacher", teacher);
		bundle.putInt("lesson_start", lesson_start);
		bundle.putInt("lesson_end", lesson_end);
		bundle.putInt("lesson_week", week);
		bundle.putInt("when_week", when_week);
		intent.putExtras(bundle);
		startActivityForResult(intent, TODETAIL);
	}

	/**
	 * 打开教师评价
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void open_pj() {
		if (!SingleGlobalVariables.IsSetRecentAppUse)
			authorizationDialog.isSetAuthorization();
		else {
			intent = new Intent(Main_PageActiviy.this, testTeacher.class);
			startActivity(intent);
		}
	}

	/**
	 * 打开课程表
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void open_course() {
		if (!SingleGlobalVariables.IsSetRecentAppUse)
			authorizationDialog.isSetAuthorization();
		else {
			intent = new Intent(Main_PageActiviy.this, com.Course.Activity.MainCourseActiviy.class);
			startActivityForResult(intent, TOCOURSE);
		}
	}

	/**
	 * 打开程序锁
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void open_app_lock() {

		// TODO Auto-generated method stub
		if (!SingleGlobalVariables.IsSetRecentAppUse)
			authorizationDialog.isSetAuthorization();
		else {
			intent = new Intent(Main_PageActiviy.this, com.app_lock.main_time_control.class);
			startActivity(intent);
		}
	}

	/**
	 * 打开个人
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void open_profile() {
		if (!SingleGlobalVariables.IsSetRecentAppUse)
			authorizationDialog.isSetAuthorization();
		else {
			Intent intent = new Intent(Main_PageActiviy.this, User.class);
			startActivity(intent);
		}
	}

	/**
	 * 向Model中传入当前是第几周
	 * 
	 * @author Sun
	 * @param void
	 * @return int
	 */
	@Override
	public int setWeekNum() {
		// TODO Auto-generated method stub
		int weekNum = sf_userinfo.getInt("current_week_num", 1);
		return weekNum;
	}

	/**
	 * 从Model中获取到Coursedb
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	@Override
	public void getCoursedb(List<Coursedb> coursedb) {
		this.courseData = coursedb;
	}

	/**
	 * 从Model中获取到今天的课程信息
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	@Override
	public void getData(List<Map<String, Object>> Data) {
		// TODO Auto-generated method stub
		mData = Data;
	}

	/** 显示Toast
	 * @author Sun
	 * @param void
	 * @return void
	 * @deprecated 弃用
	 */
	@Override
	public void showSucessToast(int sucess) {
		// TODO Auto-generated method stub
	}
    /**
     *  @author Sun
	 *  @param void
	 *  @return void
     */
	@Override
	public void onResume() {
		super.onResume();
		handler.post(runnable);
	}
}
