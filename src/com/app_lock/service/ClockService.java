package com.app_lock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.app.main.HomeActivity;
import com.app_lock.main_time_control;
import com.app_lock.show_clock;
import com.example.hello1.R;
import com.pub.DealTime;
import com.pub.SingleGlobalVariables;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;

public class ClockService extends Service {
	Handler handler = new Handler();
	CountDownTimer countdownTimer1;
	CountDownTimer countdownTimer2;
	CountDownTimer countdownTimer3;
	LockScreenReceiver Lockreceiver;
	UnLockScreenReceiver UnLockreceiver;
	static int tomato_num = 0;
	Notification notification;
	PendingIntent pendingIntent;
	SharedPreferences sf_userinfo;
	public static long thismillisUntilFinished;
	Boolean isrest = false;
	String phonepath;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*
		 * 设置为前台服务
		 * 
		 */
		notification = new Notification(R.drawable.ic_launcher, "正在使用程序锁", System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, show_clock.class);
		pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		switch (main_time_control.item) {
		case 0:
			notification.setLatestEventInfo(this, "正在使用番茄学习法", "已有" + tomato_num + "个番茄", pendingIntent);
			break;
		case 1:
			notification.setLatestEventInfo(this, "正在使用计时学习法",
					"到" + DealTime.getPreTime("" + main_time_control.allMinute) + "结束", pendingIntent);
			break;
		}
		startForeground(1, notification);
		////////////////////////////////////////////////////////////
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(ClockService.this, "userinfo", true, phonepath, sf_userinfo);
		Lockreceiver = new LockScreenReceiver();
		UnLockreceiver = new UnLockScreenReceiver();
		SingleGlobalVariables.AppLockStart = true;
		registerReceiver(Lockreceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(UnLockreceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		switch (main_time_control.item) {
		case 0:
			show_clock.show_learn_way.setText("番茄学习");
			show_clock.tomatoNum.setVisibility(View.VISIBLE);
			show_clock.showTomatoTime.setVisibility(View.VISIBLE);
			tomatoLearn();
			break;
		case 1:
			show_clock.show_learn_way.setText("计时学习");
			int time = (main_time_control.hour * 60 + main_time_control.minute) * 60 * 1000;
			keepingLearn(time);
			break;
		}
	}

	/**
	 * --作用：开启看门狗服务 --参数：NULL --返回：NULL --内容：开启看门狗
	 */
	public void startWatchDogService() {
		Intent intent = new Intent(ClockService.this, WatchDogService.class);
		startService(intent);
	}

	/**
	 * 关闭看门狗服务
	 */
	public void stopWatchDogService() {
		Intent intent = new Intent(ClockService.this, WatchDogService.class);
		stopService(intent);
	}

	/**
	 * 锁屏下点亮屏幕10秒钟
	 */
	private void AcquireWakeLock() {
		PowerManager pm = (PowerManager) ClockService.this.getSystemService(Context.POWER_SERVICE);
		WakeLock m_wakeObj = (WakeLock) pm.newWakeLock(
				PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "");
		// m_wakeObj.acquire();
		// 点亮屏幕15秒钟
		m_wakeObj.acquire(1000 * 10);
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		m_wakeObj.release();// 释放资源

	}

	@SuppressWarnings("deprecation")
	public void tomatoLearn() {
		tomato_num++;
		notification.setLatestEventInfo(this, "正在使用番茄学习法", "正在第" + tomato_num + "个番茄", pendingIntent);
		startForeground(1, notification);
		show_clock.tomatoNum.setText("第" + tomato_num + "个番茄");
		countdownTimer1 = new CountDownTimer(1 * 60 * 1000, 1000) {
			public void onFinish() { // 计时结束
				stopWatchDogService();
				rest();
				countdownTimer1.cancel();
				Log.i("结束番茄", "结束番茄");
			}

			public void onTick(long millisUntilFinished) {
				thismillisUntilFinished = millisUntilFinished;
				String MinuteStr, SecondStr;
				int minute = (int) (millisUntilFinished / 1000 / 60);
				int second = (int) ((millisUntilFinished - minute * 60 * 1000) / 1000);
				Log.i("ClockTime", minute + ":" + second);
				if (minute < 10)
					MinuteStr = "0" + minute;
				else
					MinuteStr = "" + minute;
				if (second < 10)
					SecondStr = "0" + second;

				else
					SecondStr = "" + second;
				show_clock.showTomatoTime.setText(MinuteStr + ":" + SecondStr);
			}
		};
		countdownTimer1.start();
	}

	/**
	 * --作用：休息五分钟 --内容：使用倒计时功能，时间为5分钟
	 */
	public void rest() {
		isrest =true;
		notification.setLatestEventInfo(this, "正在休息", "休息一下吧", pendingIntent);
		startForeground(1, notification);
		Log.i("开始休息", "开始休息");
		show_clock.tomatoNum.setText("休息中");
		AcquireWakeLock();
		countdownTimer2 = new CountDownTimer(1 * 60 * 1000, 1000) {
			public void onFinish() { // 计时结束
				tomatoLearn();
				startWatchDogService();
				countdownTimer2.cancel();
			}

			public void onTick(long millisUntilFinished) {
				String MinuteStr, SecondStr;
				int minute = (int) (millisUntilFinished / 1000 / 60);
				int second = (int) ((millisUntilFinished - minute * 60 * 1000) / 1000);
				Log.i("RestTime", minute + ":" + second);
				if (minute < 10)
					MinuteStr = "0" + minute;
				else
					MinuteStr = "" + minute;
				if (second < 10)
					SecondStr = "0" + second;
				else
					SecondStr = "" + second;
				show_clock.showTomatoTime.setText(MinuteStr + ":" + SecondStr);
			}
		};
		countdownTimer2.start();
	}

	public void keepingLearn(int time) {
		countdownTimer3 = new CountDownTimer(time, 1000) {
			public void onFinish() { // 计时结束
				stopWatchDogService();
				countdownTimer3.cancel();
				Intent intent = new Intent(ClockService.this, Main_PageActiviy.class);
				startActivity(intent);
			}

			public void onTick(long millisUntilFinished) {
				thismillisUntilFinished = millisUntilFinished;
				String MinuteStr, SecondStr, HourStr;
				int minute = (int) (millisUntilFinished / 1000 / 60);
				int hour = minute / 60;
				int showMinute = minute - hour * 60;
				int second = (int) ((millisUntilFinished - minute * 60 * 1000) / 1000);
				if (hour < 10)
					HourStr = "0" + hour;
				else
					HourStr = "" + hour;
				if (showMinute < 10)
					MinuteStr = "0" + showMinute;
				else
					MinuteStr = "" + showMinute;
				if (second < 10)
					SecondStr = "0" + second;
				else
					SecondStr = "" + second;
				show_clock.showTomatoTime.setText(HourStr + ":" + MinuteStr + ":" + SecondStr);
				Log.i("time", "" + HourStr + ":" + MinuteStr + ":" + SecondStr);

			}
		};
		countdownTimer3.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		tomato_num =0;
		super.onDestroy();
		unregisterReceiver(Lockreceiver);
		unregisterReceiver(UnLockreceiver);
		Lockreceiver = null;
		UnLockreceiver = null;
		if (main_time_control.item != 1) {
			
			countdownTimer1.cancel();
			if(isrest)
			countdownTimer2.cancel();
		} else
			countdownTimer3.cancel();
		SingleGlobalVariables.AppLockStart = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class LockScreenReceiver extends BroadcastReceiver {
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Editor editor = sf_userinfo.edit();
			editor.putString("LockSystemTime", df.format(new Date()));
			editor.putLong("LockTime", thismillisUntilFinished);
			editor.commit();
			if (main_time_control.item == 1) {
				countdownTimer3.cancel();
			}
		}
	}

	private class UnLockScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String LockSystemtime = sf_userinfo.getString("LockSystemTime", "00:00:00");
			long ChaSystemtime = DealTime.getChaTime(LockSystemtime);
			long LockTime = sf_userinfo.getLong("LockTime", 0);
			int time = (int) (LockTime - ChaSystemtime);

			if (main_time_control.item == 1) {
				if (time >= 1000)
					keepingLearn(time);
				else {
					Intent intent1 = new Intent(ClockService.this, Main_PageActiviy.class);
					startActivity(intent1);
				}
			}
		}
	}

}
