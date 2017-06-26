package com.app_lock;

import java.util.ArrayList;
import java.util.List;
import com.MainPage.Activity.Main_PageActiviy;
import com.app_lock.service.WatchDogService;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class main_time_control extends Activity implements OnClickListener{
	public static int hour = 0;
	public static int minute = 0;
	public static int allMinute = 0;
	public static int item = 0;// 学习模式代号
	TextView show_time;
	boolean isKeeping = false;
	TextView changetime;
	private ImageView back_main;

	public main_time_control() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lock_time_control);
		AppExit.getInstance().addActivity(this);
		////////////////////////////////////////////////////////////////////////
		show_time = (TextView) findViewById(R.id.show_timeTv);
		change_time();
		init_spinner();
		init();
		btn_start();
		set();
	}

	private void init() {
		back_main=(ImageView)findViewById(R.id.time_back_main);
		
		back_main.setOnClickListener(this);
		
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

	// 打开主页面
	public void open_MainPage() {
		Intent intent = new Intent(main_time_control.this, Main_PageActiviy.class);
		startActivity(intent);
		finish();
	}
	/*
	 * --作用：打开设置 --参数：NULL --返回：NULL --内容：初始化设置按钮，跳转到设置界面
	 */
	public void set() {
		ImageView seTextView = (ImageView) findViewById(R.id.main_time_control_set);
		seTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(main_time_control.this, learn_setting.class);
				startActivity(intent);
			}
		});
	}

	/*
	 * --作用：选择学习模式 --参数：NULL --返回：NULL --内容：选择学习模式，根据不同的学习模式显示不同的界面
	 */
	public void init_spinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		final List<String> list = new ArrayList<String>();
		list.add("番茄学习");
		list.add("计时学习");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spiner2_layout, list);
		spinner.setAdapter(adapter);
		spinner.setSelection(0, true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Log.i("按下的是几", "" + arg2);
				if (arg2 == 0) {
					item = 0;
					show_time.setText("00时25分");
					changetime.setVisibility(View.INVISIBLE);
					hour = 0;
					minute = 25;
				} else if (arg2 == 1) {
					item = 1;
					show_time.setText("00时00分");
					changetime.setVisibility(View.VISIBLE);
					isKeeping = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/*
	 * --作用：开始学习 --参数：NULL --返回：NULL --内容：初始化开始按钮，同事开启服务并将数据传入
	 */
	public void btn_start() {
		Button btn_startButton = (Button) findViewById(R.id.start_clock);
		btn_startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (hour == 0 && minute == 0 && isKeeping) {
					show();
				} else {
					Intent intent = new Intent(main_time_control.this, WatchDogService.class);
					startService(intent);
					Intent intent2 = new Intent(main_time_control.this, show_clock.class);
					startActivity(intent2);
				}
			}
		});
	}

	public void show() {
		Toast.makeText(this, "要先设置时间哦", Toast.LENGTH_SHORT).show();
	}

	/*
	 * --作用：开启修改时间 --参数：NULL --返回：NULL --内容：初始化修改按钮，同事开启修改Dialog
	 */
	public void change_time() {
		changetime = (TextView) findViewById(R.id.change_time);
		changetime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createTimePickerDialog();
			}
		});
	}

	/*
	 * --作用：修改时间 --参数：NULL --返回：NULL --内容：选择时间并将时间存储
	 */
	public void createTimePickerDialog() {
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				hour = arg1;
				minute = arg2;
				allMinute = hour * 60 + minute;
				show_Time();
			}
		}, 0, 0, true);
		timePickerDialog.setTitle("选择时间");
		timePickerDialog.show();
	}

	/*
	 * --作用：显示时间 --参数：NULL --返回：NULL --内容：将时间显示在界面上
	 */
	public void show_Time() {
		String zerohourString, zerominuteString;
		if (hour < 10)
			zerohourString = "0";
		else
			zerohourString = "";
		if (minute < 10)
			zerominuteString = "0";
		else
			zerominuteString = "";
		show_time.setText(zerohourString + hour + "时" + zerominuteString + minute + "分");
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.time_back_main:
			open_MainPage();
			break;

		default:
			break;
		}
	}
}
