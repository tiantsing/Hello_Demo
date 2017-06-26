package com.Course.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.DealString;
import com.pub.SingleGlobalVariables;
import com.pub.Dialog.AlertDialog;
import com.Course.Interface.Lesson_DetailInterface;
import com.Course.Model.Course_Time;
import com.Course.Presenter.Lesson_DetailPresenter;
import com.Course.db.Coursedb;
import com.Login.login.LoginActivity;
import com.app.main.HelloApplication;
import com.app_lock.service.HavingClass;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Lesson_Detail extends Activity implements Lesson_DetailInterface {
	public static final int RESULT = 3;
	public static final int PUTCODE = 4;
	int allweek[] = new int[25];
	List<Coursedb> courseweek = new ArrayList<Coursedb>();
	String nameString;
	String phonepath;
	String classroomString;
	String teacherString;
	String lesson_numString;// lesson_num
	String week_numString;// week_num
	String weekString = null;//
	TextView show_nameTextView;
	TextView lesson_nameTextView;
	TextView lesson_classroomTextView;
	TextView lesson_teacherTextView;
	TextView lesson_numberTextView;
	TextView week_numberTextView;
	TextView timeTextView;
	ImageView back;//返回
	int lesson_start;
	int lesson_end;
	int lesson_week;
	int when_week;
	Coursedb coursedb;
	SharedPreferences sf_userinfo;
	Course_Time course_time;
	List<Coursedb> courseData;
	List<Map<String, Object>> mData;
	int spinnerPosition;
	Lesson_DetailPresenter lesson_DetailPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.course_detail);
		AppExit.getInstance().addActivity(this);
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PUTCODE:
			if (resultCode == Change_Lesson.INTENT_RESULT) {
				// 重载数据
				init_LessonDetail(data);
			}
			break;
		}
	}

	
	public void init() {
		show_nameTextView = (TextView) findViewById(R.id.txt_title);
		lesson_nameTextView = (TextView) findViewById(R.id.lesson_name);
		lesson_classroomTextView = (TextView) findViewById(R.id.classroom);
		lesson_teacherTextView = (TextView) findViewById(R.id.teacher);
		lesson_numberTextView = (TextView) findViewById(R.id.lesson_number);
		week_numberTextView = (TextView) findViewById(R.id.week_number);
		timeTextView = (TextView) findViewById(R.id.time);
		back = (ImageView) findViewById(R.id.img_back);
		back.setVisibility(View.VISIBLE);
		
		lesson_DetailPresenter = new Lesson_DetailPresenter(this, this);
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo=HelloApplication.PDAConfig(Lesson_Detail.this, "userinfo", true, phonepath,sf_userinfo);
		
		course_time = new Course_Time(sf_userinfo.getInt("morning_class_start_time_num", 480),
				sf_userinfo.getInt("afternoon_class_start_time_num", 840), sf_userinfo.getInt("night_class_start_time_num", 1140),
				sf_userinfo.getInt("little_recess_num", 10), sf_userinfo.getInt("big_recess_num", 20),
				sf_userinfo.getInt("every_class_time_num", 45));
		Intent intent = getIntent();
		init_LessonDetail(intent);
		del_Lesson();
		back();
		init_Change();
	}

	public void init_Change() {
		Button change_lesson_name = (Button) findViewById(R.id.change_lesson_name);
		change_lesson_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Lesson_Detail.this, Change_Lesson.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", nameString);
				bundle.putString("teacher", teacherString);
				bundle.putString("room", classroomString);
				bundle.putString("lesson_num", lesson_numString);// zhouji
				bundle.putInt("week", lesson_week);
				bundle.putInt("start", lesson_start);
				bundle.putInt("stop", lesson_end);
				bundle.putIntArray("allweek", allweek);
				intent.putExtras(bundle);
				startActivityForResult(intent, PUTCODE);
			}
		});
	}

	public void del_Lesson() {
		RelativeLayout del_lesson = (RelativeLayout) findViewById(R.id.Relative_del_lesson);
		del_lesson.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ensure_DelLesson();
			}
		});
	}

	// 检查今天是否有课，有课则检查时间，无课
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

	public void updateMain() {
		lesson_DetailPresenter.getData();
		if (cheack_havingLesson()) {
			Intent intent = new Intent(this, HavingClass.class);
			stopService(intent);
			startService(intent);
		} else {
			Intent intent = new Intent(this, HavingClass.class);
			stopService(intent);
			SingleGlobalVariables.HaveClass = false;
		}
	}

	public void ensure_DelLesson() {		
		new AlertDialog(this).builder().setTitle("提示").setMsg("确定删除该课程？")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				coursedb = new Coursedb(nameString, teacherString, classroomString, lesson_week, lesson_start,
						lesson_end, when_week);
				lesson_DetailPresenter.delLesson(coursedb);
				updateMain();
				setResult(RESULT);
				finish();
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		}).show();
	}

	public void back() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(RESULT);
				finish();
			}
		});
	}

	public void init_LessonDetail(Intent intent) {
		Bundle bundle = intent.getExtras();
		nameString = bundle.getString("name");
		classroomString = bundle.getString("classroom");
		teacherString = bundle.getString("teacher");
		lesson_start = bundle.getInt("lesson_start");
		lesson_end = bundle.getInt("lesson_end");
		lesson_week = bundle.getInt("lesson_week");// 星期几
		when_week = bundle.getInt("when_week");// 第几周
		for (int i = 0; i < 25; i++) {
			allweek[i] = 0;
		}
		lesson_DetailPresenter.getCourseData(lesson_week, nameString, teacherString, classroomString, lesson_start,
				lesson_end);// 获得课程信息
		for (int i = 0; i < courseweek.size(); i++) {
			Coursedb aCoursedb = courseweek.get(i);
			allweek[i] = aCoursedb.getWhen_week();
		}
		show_nameTextView.setText(nameString);
		lesson_nameTextView.setText(nameString);
		lesson_classroomTextView.setText(classroomString);
		lesson_teacherTextView.setText(teacherString);
		String lesson_startString = getResources().getString(R.string.lesson_num);
		weekString = DealString.DealWeekString(lesson_week);
		lesson_numString = String.format(lesson_startString, weekString, lesson_start, lesson_end);
		lesson_numberTextView.setText(lesson_numString);
		String week_numberString = getResources().getString(R.string.week_num);
		week_numberString = String.format(week_numberString, lesson_start, lesson_end);
		String string = "";
		for (int i = 0; i < 25; i++) {
			if (allweek[i] != 0)
				string += allweek[i] + ",";
		}
		week_numberTextView.setText("第" + string + "周");
		String set_time = course_time.put_time(lesson_start, lesson_end);
		timeTextView.setText(set_time);
	}

	@Override
	public void getData(List<Map<String, Object>> mData) {
		this.mData = mData;
		// TODO Auto-generated method stub
	}

	@Override
	public void getCourseData(List<Coursedb> coursedb) {
		// TODO Auto-generated method stub
		this.courseweek = coursedb;
	}

}
