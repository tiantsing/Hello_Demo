package com.app_lock.service;

import java.util.Calendar;

import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.app_lock.main_time_control;
import com.app_lock.show_clock;
import com.example.hello1.R;
import com.pub.SingleGlobalVariables;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class HavingClass extends Service {
	boolean isStartSerice = false;
	boolean isClassing = false;
	Intent intent;
	int LessonNumSize = -1;
	int lessonNum[] = new int[12];
	SharedPreferences sf_userinfo;
	boolean next = false;
	int Minute;
	Handler handler = new Handler();
	Handler handler2 = new Handler();
	Calendar calendar;
	int Class1, Class2;
	int num = 0;
	String phonepath;
	Notification notification;
	PendingIntent pendingIntent;
	public HavingClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		intent = new Intent(HavingClass.this, WatchDogService.class);
		SingleGlobalVariables.IshavingStart = true;
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(HavingClass.this, "userinfo", true, phonepath, sf_userinfo);

		for (int i = 0; i <= 12; i++)
			for (int j = 0; j < 2; j++) {
				SingleGlobalVariables.time[i][j] = 0;
			}
		for (int i = 0; i < 12; i++) {
			lessonNum[i] = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 * 作用：重载Onstart函数 --参数：Intent接收传来的数据 startId 暂时无用 --返回：NULL --内容：从Intent中读取出
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		lessonNum = SingleGlobalVariables.lessonNum;
		for (int j = 1; j <= 12; j++) {
			set_time(j);
			if (lessonNum[j - 1] != 0) {
				LessonNumSize++;
				Log.i("LessonNumSize", "" + LessonNumSize);
			}
		}
		handler.postDelayed(runnable, 1000);
		super.onStart(intent, startId);
	}

	public void StartWatchDogService() {
		if (!isStartSerice) {
			Log.i("开启看门狗", "汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪汪");
			notification = new Notification(R.drawable.ic_launcher, "开启看门狗", System.currentTimeMillis());
			Intent notificationIntent = new Intent(this, show_clock.class);
			pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(this, "正在上课", "好好上课吧", pendingIntent);
			startForeground(1, notification);
			startService(intent);
			SingleGlobalVariables.HaveClass = true;
			isStartSerice = true;
		}
	}

	public void StopAppLock() {
		main_time_control.hour = 0;
		main_time_control.item = 0;
		main_time_control.minute = 0;
		main_time_control.allMinute = 0;
		StopWatchDogService();
		StopClockService();
		Intent intent2 = new Intent(HavingClass.this, main_time_control.class);
		startActivity(intent2);

	}

	public void StopWatchDogService() {
		if (isStartSerice) {
			Log.i("关闭看门狗", "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
			stopService(intent);
			SingleGlobalVariables.HaveClass = false;
			isStartSerice = false;
		}
	}

	public void StopClockService() {
		Intent intent = new Intent(HavingClass.this, ClockService.class);
		stopService(intent);
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			int time = 1000;
			calendar = Calendar.getInstance();
			Minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
			if (Minute > SingleGlobalVariables.time[lessonNum[LessonNumSize]][1]) {
				Log.i("没课了", "" + SingleGlobalVariables.time[lessonNum[LessonNumSize]][1]);
				Intent intent = new Intent(HavingClass.this, HavingClass.class);
				stopService(intent);
			} else if (Minute < SingleGlobalVariables.time[lessonNum[0]][0]) {
				Log.i("没上第一节课", "" + SingleGlobalVariables.time[lessonNum[0]][0]);
				time = (SingleGlobalVariables.time[lessonNum[0]][0] - Minute) * 60 * 1000;
			} else {
				for (int i = num; i <= LessonNumSize; i++) {
					if (Minute > SingleGlobalVariables.time[lessonNum[i]][0]
							&& Minute < SingleGlobalVariables.time[lessonNum[i]][1]) {
						if (SingleGlobalVariables.AppLockStart)
							StopAppLock();
						StartWatchDogService();
						time = (SingleGlobalVariables.time[lessonNum[i]][1] - Minute) * 60 * 1000;
						Log.i("time", "" + time);
						num = i;
						break;
					} else {
						StopWatchDogService();
						// 可优化，判断离最近上课还有多长时间，将time该为相应的时间
					}
				}
			}
			handler.postDelayed(runnable, time);
			Log.i("过多久进行刷新", "" + time / (60 * 1000));
		}
	};

	public void set_time(int i) {
		switch (i) {
		case 1:
			SingleGlobalVariables.time[1][0] = sf_userinfo.getInt("first_start", 480);
			SingleGlobalVariables.time[1][1] = sf_userinfo.getInt("first_stop", 525);
			break;
		case 2:// 修改名字
			SingleGlobalVariables.time[2][0] = sf_userinfo.getInt("first_start", 535);
			SingleGlobalVariables.time[2][1] = sf_userinfo.getInt("first_stop", 580);
			break;
		case 3:
			SingleGlobalVariables.time[3][0] = sf_userinfo.getInt("first_start", 600);
			SingleGlobalVariables.time[3][1] = sf_userinfo.getInt("first_stop", 645);
			break;
		case 4:
			SingleGlobalVariables.time[4][0] = sf_userinfo.getInt("first_start", 655);
			SingleGlobalVariables.time[4][1] = sf_userinfo.getInt("first_stop", 700);
			break;
		case 5:
			SingleGlobalVariables.time[5][0] = sf_userinfo.getInt("first_start", 840);
			SingleGlobalVariables.time[5][1] = sf_userinfo.getInt("first_stop", 885);
		case 6:
			SingleGlobalVariables.time[6][0] = sf_userinfo.getInt("first_start", 895);
			SingleGlobalVariables.time[6][1] = sf_userinfo.getInt("first_stop", 940);
			break;
		case 7:
			SingleGlobalVariables.time[7][0] = sf_userinfo.getInt("first_start", 960);
			SingleGlobalVariables.time[7][1] = sf_userinfo.getInt("first_stop", 1005);
			break;
		case 8:
			SingleGlobalVariables.time[8][0] = sf_userinfo.getInt("first_start", 1015);
			SingleGlobalVariables.time[8][1] = sf_userinfo.getInt("first_stop", 1060);
			break;
		case 9:
			SingleGlobalVariables.time[9][0] = sf_userinfo.getInt("first_start", 1140);
			SingleGlobalVariables.time[9][1] = sf_userinfo.getInt("first_stop", 1185);
			break;
		case 10:
			SingleGlobalVariables.time[10][0] = sf_userinfo.getInt("first_start", 1195);
			SingleGlobalVariables.time[10][1] = sf_userinfo.getInt("first_stop", 1240);
			break;
		case 11:
			SingleGlobalVariables.time[11][0] = sf_userinfo.getInt("first_start", 1260);
			SingleGlobalVariables.time[11][1] = sf_userinfo.getInt("first_stop", 1305);
			break;
		case 12:
			SingleGlobalVariables.time[12][0] = sf_userinfo.getInt("first_start", 1315);
			SingleGlobalVariables.time[12][1] = sf_userinfo.getInt("first_stop", 1350);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		SingleGlobalVariables.IshavingStart = false;
		StopWatchDogService();
		handler.removeCallbacks(runnable);
	}
}
