package com.Course.Activity;

import java.util.ArrayList;
import java.util.List;
import com.Course.Presenter.Change_lessonPresenter;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.DealString;
import com.pub.Dialog.CustomMultiChoiceDialog;
import com.pub.Dialog.ValuesPickerDialog;
import com.pub.Dialog.ValuesPickerDialog.OnValuesPickerListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Change_Lesson extends Activity {
	static final int INTENT_RESULT = 5;
	static final int RESULT = 6;
	int bfChange[] = new int[25];
	String allweek[];
	int afChange[] = new int[25];
	String nameString;
	String teacherString;
	String roomString;
	String lesson_numString;// 节数
	String week_numString;// 周数
	String get_Dialog[] = new String[3];
	int week_num;
	int start;
	int stop;
	EditText nameEditText;
	EditText roomEditText;
	EditText teacherEditText;
	TextView lesson_numTextView;
	TextView week_numTextView;
	DBManager mgr;
	boolean canensure = true;
	Change_lessonPresenter change_lessonPresenter;
	CustomMultiChoiceDialog.Builder multiChoiceDialogBuilder;
	boolean boos[] = { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		setContentView(R.layout.course_detail_change);
		// 初始化数据//////////////////////////////////////////////////////////////////////////
		AppExit.getInstance().addActivity(this);
		init();
		back();
		change_LessonWeek();
		change_LessonNum();
		this_finish();
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

	public void init() {
		nameEditText = (EditText) findViewById(R.id.edit_changelesson_name);
		roomEditText = (EditText) findViewById(R.id.edit_changelesson_classroom);
		teacherEditText = (EditText) findViewById(R.id.edit_changelesson_teacher);
		lesson_numTextView = (TextView) findViewById(R.id.textview_change_lesson_num);
		week_numTextView = (TextView) findViewById(R.id.textview_change_week_num);
		allweek = getResources().getStringArray(R.array.allweeks);
		mgr = new DBManager(this);
		change_lessonPresenter = new Change_lessonPresenter(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		nameString = bundle.getString("name");
		roomString = bundle.getString("room");
		teacherString = bundle.getString("teacher");
		lesson_numString = bundle.getString("lesson_num");
		start = bundle.getInt("start");
		stop = bundle.getInt("stop");
		bfChange = bundle.getIntArray("allweek");
		week_num = bundle.getInt("week");
		get_Dialog[0] = bundle.getInt("week") + "";
		get_Dialog[1] = bundle.getInt("start") + "";
		get_Dialog[2] = bundle.getInt("stop") + "";
		String string = "";
		for (int i = 0; i < 25; i++) {
			if (bfChange[i] != 0)
				string += bfChange[i] + ",";
		}
		for (int i = 0; i < 25; i++) {
			afChange[i] = bfChange[i];
		}
		nameEditText.setText(nameString);
		roomEditText.setText(roomString);
		teacherEditText.setText(teacherString);
		lesson_numTextView.setText(lesson_numString);
		week_numTextView.setText("第" + string + "周");
	}

	public void change_LessonWeek() {
		RelativeLayout changeLessonWeekRV = (RelativeLayout) findViewById(R.id.change_lesson_week);
		changeLessonWeekRV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ValuesPickerDialog dialog = new ValuesPickerDialog(Change_Lesson.this);
				List<String> list = new ArrayList<String>();
				list.add("周一");
				list.add("周二");
				list.add("周三");
				list.add("周四");
				list.add("周五");
				list.add("周六");
				list.add("周日");
				List<String> list2 = new ArrayList<String>();
				list2.add("第1节");
				list2.add("第2节");
				list2.add("第3节");
				list2.add("第4节");
				list2.add("第5节");
				list2.add("第6节");
				list2.add("第7节");
				list2.add("第8节");
				list2.add("第9节");
				list2.add("第10节");
				list2.add("第11节");
				list2.add("第12节");
				List<String> list3 = new ArrayList<String>();
				list3.add("到1节");
				list3.add("到2节");
				list3.add("到3节");
				list3.add("到4节");
				list3.add("到5节");
				list3.add("到6节");
				list3.add("到7节");
				list3.add("到8节");
				list3.add("到9节");
				list3.add("到10节");
				list3.add("到11节");
				list3.add("到12节");
				dialog.setTitleText("选择上课节数");
				dialog.setShowValues(list);
				dialog.setShowValues2(list2);
				dialog.setShowValues3(list3);

				dialog.setOnValuesPickerListener(new OnValuesPickerListener() {
					@Override
					public void onPickerValues(String value) {
						Log.i("111111111111111", value);
						String[] strarray = value.split(" ");
						for (int i = 0; i < strarray.length; i++)
							get_Dialog[i] = strarray[i];
						lesson_numTextView.setText("周" + DealString.DealWeekString(Integer.parseInt(get_Dialog[0]))
								+ " " + get_Dialog[1] + "-" + get_Dialog[2] + "节");
					}
				});
				dialog.show();
			}
		});
	}

	public void change_LessonNum() {
		RelativeLayout changeLessonNumber = (RelativeLayout) findViewById(R.id.change_lesson_lesson_number);
		changeLessonNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMultiChoiceDialog(arg0);
			}
		});
	}

	public void showMultiChoiceDialog(View view) {
		multiChoiceDialogBuilder = new CustomMultiChoiceDialog.Builder(this);
		CustomMultiChoiceDialog multiChoiceDialog = multiChoiceDialogBuilder.setTitle("单周")
				.setMultiChoiceItems(allweek, boos, null, true, true, true)
				.setPositiveButton("确定", new PositiveClickListener()).setNegativeButton("取消", null).create();
		multiChoiceDialog.show();
	}

	class PositiveClickListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			boos = multiChoiceDialogBuilder.getCheckedItems();
			for (int i = 0; i < 25; i++) {
				afChange[i] = 0;
			}
			int j = 0;
			for (int i = 0; i < boos.length; i++) {
				if (boos[i]) {
					afChange[j] = (i + 1);
					j++;
				}
			}
			String string = "";
			for (int i = 0; i < 25; i++) {
				if (afChange[i] != 0)
					string += afChange[i] + ",";
			}
			week_numTextView.setText("第" + string + "周");
		}
	}

	public void this_finish() {
		TextView finishTextView = (TextView) findViewById(R.id.finish);
		finishTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 修改后的信息
				String nameString1 = nameEditText.getText().toString();
				String teacherString1 = teacherEditText.getText().toString();
				String roomString1 = roomEditText.getText().toString();
				int int_week_num1 = Integer.parseInt(get_Dialog[0]);
				int start1 = Integer.parseInt(get_Dialog[1]);
				int stop1 = Integer.parseInt(get_Dialog[2]);
				int lengthbf = 0;// 修改前
				int lengthaf = 0;// 修改后
				for (int i = 0; i < bfChange.length; i++) {
					if (bfChange[i] != 0)
						lengthbf++;
				}
				for (int i = 0; i < 25; i++) {
					if (afChange[i] != 0)
						lengthaf++;
				}
				for (int i = 0; i < lengthbf; i++) {
					// 修改前的信息
					Coursedb courses2 = new Coursedb(nameString, teacherString, roomString, week_num, start, stop,
							bfChange[i]);
					change_lessonPresenter.delLesson(courses2);
				}
				// 修改后的信息
				for (int i = 0; i < lengthaf; i++) {
					Coursedb courses1 = new Coursedb(nameString1, teacherString1, roomString1, int_week_num1, start1,
							stop1, afChange[i]);
					change_lessonPresenter.addLesson(courses1);
				}
				bundle.putString("name", nameString1);
				bundle.putString("teacher", teacherString1);
				bundle.putString("classroom", roomString1);
				bundle.putInt("lesson_week", int_week_num1);
				bundle.putInt("lesson_start", start1);
				bundle.putInt("lesson_end", stop1);
				intent.putExtras(bundle);
				setResult(INTENT_RESULT, intent);
				finish();
			}
		});
	}

	public void back() {
		TextView lessonchange_back2course = (TextView) findViewById(R.id.lessonchange_back2course);
		lessonchange_back2course.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(RESULT);
				finish();
			}
		});
	}

}
