package com.Course.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.Course.Interface.Add_LessonInterface;
import com.Course.Presenter.Add_LessonPresenter;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import com.app_lock.service.HavingClass;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.DealString;
import com.pub.SingleGlobalVariables;
import com.pub.Dialog.CustomMultiChoiceDialog;
import com.pub.Dialog.ValuesPickerDialog;
import com.pub.Dialog.ValuesPickerDialog.OnValuesPickerListener;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class Add_LessonActivity extends Activity implements Add_LessonInterface {
	static final int RESULTCODE = 6;
	String get_Dialog[] = new String[3];
	int arrs_id[] = new int[25];// 存储25周
	String all_week[];
	String lesson_nameString;
	String lesson_teacherString;
	String lesson_roomString;
	EditText lesson_nameEditText;
	EditText lesson_teacherEditText;
	EditText lesson_roomEditText;
	TextView ensure_TextView;
	TextView add_OtherLessonTextView;
	ImageView back;
	TextView show_WeekTextView;
	TextView show_NumTextView;
	TextView title;
	DBManager mar;
	List<Coursedb> courseData;
	List<Map<String, Object>> mData;
	int spinnerPosition;
	TabHost th;
	Add_LessonPresenter add_LessonPresenter;
	boolean boos[] = { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false };
	boolean isClickShowWeek = false;
	boolean isClickShowNum = false;
	boolean canensure = true;
	CustomMultiChoiceDialog.Builder multiChoiceDialogBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.course_add_lesson);
		// 初始化数据//////////////////////////////////////////////////////////////////////////
		mar = new DBManager(this);
		add_LessonPresenter = new Add_LessonPresenter(this, this);
		AppExit.getInstance().addActivity(this);
		init();
		// init_Tab();
		back();
		add_LessonWeek();
		add_LessonNum();
		ensure_AddLesoon();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULTCODE);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 作用：初始化部分控件和变量 参数：NULL 返回：NULL
	 * 
	 */
	public void init() {
		back = (ImageView) findViewById(R.id.img_back);
		title = (TextView) findViewById(R.id.txt_title);
		ensure_TextView = (TextView) findViewById(R.id.txt_right);
		lesson_nameEditText = (EditText) findViewById(R.id.edit_lesson_name);
		lesson_teacherEditText = (EditText) findViewById(R.id.edit_lesson_teacher);
		lesson_roomEditText = (EditText) findViewById(R.id.edit_lesson_classroom);
		show_WeekTextView = (TextView) findViewById(R.id.textview_add_lesson_lesson_week);
		show_NumTextView = (TextView) findViewById(R.id.textview_add_lesson_num);
		all_week = getResources().getStringArray(R.array.week);

		back.setVisibility(View.VISIBLE);
		title.setText("添加课程");
		ensure_TextView.setText("确定");
	}
	// //////////////////////////////////////////////////////////////////////////
	/*
	 * 作用：调用显示选择周数控件-- 参数：NULL --返回:NULL --内容:初始化控件，通过监听函数调用显示函数·
	 */
	public void add_LessonWeek() {
		RelativeLayout add_LessonNumberRV = (RelativeLayout) findViewById(R.id.add_lesson_week);
		add_LessonNumberRV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMultiChoiceDialog(arg0);
			}
		});
	}

	/*
	 * -- 作用：显示选择周数控件 参数：View 返回: NULL
	 * --内容：显示自定义的控件，通过自定义PositiveClickListener来实现确定事件
	 */
	public void showMultiChoiceDialog(View view) {
		multiChoiceDialogBuilder = new CustomMultiChoiceDialog.Builder(this);
		CustomMultiChoiceDialog multiChoiceDialog = multiChoiceDialogBuilder.setTitle("单周")
				.setMultiChoiceItems(all_week, boos, null, true, true, true)
				.setPositiveButton("确定", new PositiveClickListener()).setNegativeButton("取消", null).create();
		multiChoiceDialog.show();
	}

	/*
	 * --作用：实现选择周数控件的确定事件 --内容：根据选择好的Item来确定arr_id的值，并将这些值整理成String，传递给主界面显示
	 * 
	 */
	class PositiveClickListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			boos = multiChoiceDialogBuilder.getCheckedItems();
			for (int i = 0; i < 25; i++) {
				arrs_id[i] = 0;
			}
			int j = 0;
			for (int i = 0; i < boos.length; i++) {
				if (boos[i]) {
					arrs_id[j] = (i + 1);
					j++;
				}
			}
			String string = "";
			for (int i = 0; i < 25; i++) {
				if (arrs_id[i] != 0)
					string += (arrs_id[i] + " ");
			}
			show_WeekTextView.setText("第" + string + "周");
			isClickShowWeek = true;
		}
	}

	/*
	 * --作用：实现后退事件 --参数：NULL --返回：NULL --内容:初始化back控件，通过监听事件返回值并finish该Actvity
	 */
	public void back() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(RESULTCODE);
				finish();
			}
		});
	}

	public void Dialog(int i, View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		switch (i) {
		case 1:
			builder.setMessage("请填写 课程名称  ");
			break;
		case 2:
			builder.setMessage("请填写 老师");
			break;
		case 3:
			builder.setMessage("请填写 教室");
			break;
		case 4:
			builder.setMessage("请选择 周数");
			break;
		case 5:
			builder.setMessage("请选择 节数");
			break;
		}
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});
		builder.show();
	}

	public void add_OtherLesson() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("添加成功");
		builder.setMessage("是否继续添加课程");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				canensure = true;
				lesson_nameEditText.setText("");
				lesson_teacherEditText.setText("");
				lesson_roomEditText.setText("");
				show_NumTextView.setText(R.string.select_lesson_lesson_num);
				show_WeekTextView.setText(R.string.select_lesson_week_num);
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				setResult(RESULTCODE);
				Log.i("1", "2");
				finish();
			}

		});
		builder.show();
	}

	public void add_LessonNum() {
		RelativeLayout add_LessonWeekRV = (RelativeLayout) findViewById(R.id.add_lesson_lesson_number);
		add_LessonWeekRV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ValuesPickerDialog dialog = new ValuesPickerDialog(Add_LessonActivity.this);
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
						String[] strarray = value.split(" ");
						for (int i = 0; i < strarray.length; i++)
							get_Dialog[i] = strarray[i];
						show_NumTextView.setText("周" + (DealString.DealWeekString(Integer.parseInt(get_Dialog[0])))
								+ " " + (Integer.parseInt(get_Dialog[1])) + "-" + (Integer.parseInt(get_Dialog[2]))
								+ "节");
						isClickShowNum = true;
					}
				});

				dialog.show();
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
		add_LessonPresenter.getData();
		if (cheack_havingLesson()) {
			Intent intent = new Intent(this, HavingClass.class);
			stopService(intent);
			startService(intent);
		}
	}

	public void ensure_AddLesoon() {
		ensure_TextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					lesson_nameString = lesson_nameEditText.getText().toString();
					lesson_teacherString = lesson_teacherEditText.getText().toString();
					lesson_roomString = lesson_roomEditText.getText().toString();
					if (!lesson_nameString.equals("")) {
						if (!lesson_roomString.equals("")) {
							if (!lesson_teacherString.equals("")) {
								if (isClickShowNum) {
									if (isClickShowWeek) {
										int length = 0;
										for (int i = 0; i < arrs_id.length; i++) {
											if (arrs_id[i] != 0)
												length++;
										}
										if (canensure) {
											for (int i = 0; i < length; i++) {
												Coursedb coursedb = new Coursedb(lesson_nameString,
														lesson_teacherString, lesson_roomString,
														Integer.parseInt(get_Dialog[0]),
														Integer.parseInt(get_Dialog[1]),
														Integer.parseInt(get_Dialog[2]), arrs_id[i]);
												mar.add(coursedb);
												canensure = false;
												updateMain();
											}
										}
										add_OtherLesson();
									} else {
										Dialog(4, v);
									}
								} else {
									Dialog(5, v);
								}
							} else {
								Dialog(2, v);
							}
						} else {
							Dialog(3, v);
						}
					} else {
						Dialog(1, v);
					}
			}
		});
	}

	@Override
	public void getData(List<Map<String, Object>> Data) {
		// TODO Auto-generated method stub
		mData = Data;
	}
}
