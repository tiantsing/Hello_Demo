package com.app_lock;

import com.app_lock.service.ClockService;
import com.app_lock.service.WatchDogService;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class show_clock extends Activity {
	public static TextView tomatoNum;
	public static TextView showTomatoTime;
	public static TextView show_learn_way;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lock_timing);
		AppExit.getInstance().addActivity(this);
		////////////////////////////////////////////////////////////////////////////
		tomatoNum = (TextView) findViewById(R.id.show_toamatoNum);
		showTomatoTime = (TextView) findViewById(R.id.show_tomatoTime);
		show_learn_way = (TextView) findViewById(R.id.show_learn_way);
		startClockServiceService();
		startWatchDogService();
		btn_abandon();
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

	/*
	 * --作用：开启看门狗服务 --参数：NULL --返回：NULL --内容：开启看门狗
	 */
	public void startWatchDogService() {
		Intent intent = new Intent(show_clock.this, WatchDogService.class);
		startService(intent);
	}

	public void stopWatchDogService() {
		Intent intent = new Intent(show_clock.this, WatchDogService.class);
		stopService(intent);
	}

	public void startClockServiceService() {
		Intent intent = new Intent(show_clock.this, ClockService.class);
		startService(intent);
	}

	public void stopClockServiceService() {
		Intent intent = new Intent(show_clock.this, ClockService.class);
		stopService(intent);
	}

	/*
	 * --作用：放弃程序锁 --参数：NULL --返回：NULL --内容：初始化放弃按钮，并将开门狗服务和计时服务关闭，然后返回到主界面
	 */
	public void btn_abandon() {
		TextView abandonTextView = (TextView) findViewById(R.id.btn_abandon);
		abandonTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				main_time_control.hour = 0;
				main_time_control.item = 0;
				main_time_control.minute = 0;
				main_time_control.allMinute = 0;
				stopWatchDogService();
				stopClockServiceService();
				Intent intent2 = new Intent(show_clock.this, main_time_control.class);
				startActivity(intent2);
				finish();
			}
		});
	}
}
