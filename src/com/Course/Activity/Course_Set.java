package com.Course.Activity;

import java.util.ArrayList;
import java.util.List;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.SingleMyCalendar;
import com.pub.Dialog.MyNumberPiackerDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class Course_Set extends Activity {
	static final int RESULT = 1;
	ImageView back;
	TextView currentSemeterTextView;
	TextView nowWeekTextView;
	TextView title;
	SharedPreferences sp;
	SharedPreferences sf;
	Editor editor;
    String choose_semester[];
	String choose_now_week[];
	SingleMyCalendar singleMyCalendar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.course_set);
		// //////////////////////////////////////////////////////////////
		init();
		AppExit.getInstance().addActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(RESULT, intent);
			finish();
			Log.i("222","222");
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void init() {
		back = (ImageView) findViewById(R.id.img_back);
		back.setVisibility(View.VISIBLE);
		title=(TextView)findViewById(R.id.txt_title);
		title.setText("课程设置");
		sf = getSharedPreferences("save", MODE_PRIVATE);
	    singleMyCalendar = SingleMyCalendar.getInstance();
		currentSemeterTextView = (TextView) findViewById(R.id.Textview_current_semeter);
		nowWeekTextView = (TextView) findViewById(R.id.TextView_now_week);
		nowWeekTextView.setText("第" + (sf.getInt("current_week_num", 1) ) + "周");
		sp = this.getSharedPreferences("save", MODE_PRIVATE);
		choose_now_week = getResources().getStringArray(R.array.allweeks);
		choose_semester = getResources().getStringArray(R.array.semeter);
		back();// 后退按钮
		init_CurrentSemester();
		init_CurrentWeek();
	}
	public void init_CurrentSemester() {
		RelativeLayout currentSemesterRV = (RelativeLayout) findViewById(R.id.Relativelayout_current_semester);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("当前学期").setIcon(R.drawable.ic_launcher)
				.setItems(choose_semester, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						currentSemeterTextView.setText(choose_semester[which]);
						getSemeter(which);
						
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).create();
		currentSemesterRV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				alertDialog.show();
			}
		});
	}
	public void getSemeter(int which) {
		Editor editor = sp.edit();
		switch (which) {
		case 0:
			editor.putInt("currentSemeter", 1);// 大几
			editor.putInt("currentTerm", 1);// 第几学期
			editor.commit();
			break;
		case 1:
			editor.putInt("currentSemeter", 1);// 大几
			editor.putInt("currentterm", 2);// 第几学期
			editor.commit();
		case 2:
			editor.putInt("currentSemeter", 2);// 大几
			editor.putInt("currentterm", 1);// 第几学期
			editor.commit();
			break;
		case 3:
			editor.putInt("currentSemeter", 2);// 大几
			editor.putInt("currentterm", 2);// 第几学期
			editor.commit();
			break;
		case 4:
			editor.putInt("currentSemeter", 3);// 大几
			editor.putInt("currentterm", 1);// 第几学期
			editor.commit();
			break;
		case 5:
			editor.putInt("currentSemeter", 3);// 大几
			editor.putInt("currentterm", 2);// 第几学期
			editor.commit();

			break;
		case 6:
			editor.putInt("currentSemeter", 4);// 大几
			editor.putInt("currentterm", 1);// 第几学期
			editor.commit();
			break;
		case 7:
			editor.putInt("currentSemeter", 4);// 大几
			editor.putInt("currentterm", 2);// 第几学期
			editor.commit();
			break;
		}
	}
	public void init_CurrentWeek() {
		RelativeLayout currentWeekRV = (RelativeLayout) findViewById(R.id.Relativelayout_current_week);
		currentWeekRV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyNumberPiackerDialog dialog = new MyNumberPiackerDialog(Course_Set.this);
				List<String> list = new ArrayList<String>();
				for(int i=1;i<=25;i++)
				{
					list.add(choose_now_week[i]);
				}
				dialog.setTitleText("选择上课节数");
				dialog.setShowValues(list);
				dialog.setOnValuesPickerListener(new MyNumberPiackerDialog.OnValuesPickerListener() {
					@Override
					public void onPickerValues(int value) {
						 nowWeekTextView.setText(choose_now_week[value]);
						 setFirstWeek(value);
						 Editor editor = sp.edit();
						 editor.putInt("current_week_num", value);
						 editor.commit();
					}
				});
				dialog.show();
			}
		});
	}
	/* 作用：将第一周周一的数据存储到文件中，用来作为周数改变的依据
	 * 参数：当前是第几周
	 * 返回：空
	 * 内容：将获得的值存到“save”文件	中
	 */
	public void setFirstWeek(int week)
	{
		 singleMyCalendar.getFirstWeekMonday(week);
		 Editor editor = sp.edit();
		 editor.putInt("FirstWeekMonday",singleMyCalendar.getFirstWeekMonday());
		 editor.putInt("FirstWeekMonth",singleMyCalendar.getFirstWeekMonth());
		 editor.putInt("FirstWeekYear",singleMyCalendar.getFirstWeekYear());
		 editor.commit();
	}
	public void back() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT, intent);
				finish();
			}
		});
	}
}
