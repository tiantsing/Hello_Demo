package com.app_lock.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.MainPage.Activity.Main_PageActiviy;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.app_lock.LockScreenActivity;
import com.app_lock.dao.AppLockDBDao;
import com.example.hello1.R;

public class WatchDogService extends Service {
	private boolean flag;// 开启软件锁
	private String packName;//
	private AppLockDBDao dao;// 上锁的程序
	private ActivityManager am;// 获得所有的软件
	private DBChangedReceiver receiver4;
	private LockScreenReceiver receiver2;
	private UnLockScreenReceiver receiver3;
	private Intent intent;
	private List<String> packNames;
	String currentapiVersion;
	int ApiVersion;
	PackageManager packageManager;
	ActivityManager activityManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i("看们狗服务开启", "111111111111111111111");
		receiver2 = new LockScreenReceiver();
		receiver3 = new UnLockScreenReceiver();
		receiver4 = new DBChangedReceiver();
		intent = new Intent(WatchDogService.this, LockScreenActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		registerReceiver(receiver4, new IntentFilter("com.xiong.dbChanged"));
		registerReceiver(receiver2, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(receiver3, new IntentFilter(Intent.ACTION_SCREEN_ON));
		super.onCreate();
		lock();
	}
	private void lock() {
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new AppLockDBDao(this);
		packNames = dao.findAll();
		flag = true;
		new Thread() {
			@SuppressLint("NewApi") public void run() {
				while (flag) {
					if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){// 5.0以下版本
						@SuppressWarnings("deprecation")
						ComponentName runningActivity = am.getRunningTasks(1)
								.get(0).topActivity;
						packName = runningActivity.getPackageName();// 得到正在运行的activity包名
					} else {
						String topPackageName = "888";
						UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
						long time = System.currentTimeMillis();

						List<UsageStats> stats = mUsageStatsManager
								.queryUsageStats(
										UsageStatsManager.INTERVAL_DAILY,
										time - 1000 * 10, time);
						if (stats != null) {
							SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
							for (UsageStats usageStats : stats) {
								mySortedMap.put(usageStats.getLastTimeUsed(),
										usageStats);
							}
							if (mySortedMap != null && !mySortedMap.isEmpty()) {
								topPackageName = mySortedMap.get(
										mySortedMap.lastKey()).getPackageName();
					
								packName = topPackageName;
							
								
							}
						}
					}
					if (packNames.contains(packName)) {
						intent.putExtra("packName", packName);
						startActivity(intent);
					}
				}
			}
		}.start();
	}
	
	@Override 
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
		unregisterReceiver(receiver2);
		unregisterReceiver(receiver3);
		unregisterReceiver(receiver4);
		receiver4 = null;
		receiver3 = null;
		receiver2 = null;
	}
	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			flag = false;
			// System.out.println(tempPackName);
		}
	}
	private class UnLockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			flag = true;
			lock();
			// System.out.println(tempPackName);
		}
	}

	private class DBChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			packNames = dao.findAll();
			// System.out.println(tempPackName);
		}
	}
}
