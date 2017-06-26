package com.exit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.Login.login.LoginActivity;
import com.app_lock.service.ClockService;
import com.app_lock.service.HavingClass;
import com.app_lock.service.WatchDogService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

public class AppExit extends Application {

	private List<Activity> activityList = new LinkedList();
	private static AppExit instance;
	Context context;

	private AppExit() {
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static AppExit getInstance() {
		if (null == instance) {
			instance = new AppExit();
		}
		return instance;

	}

	public void getConext(Context context) {
		this.context = context;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	// 遍历所有Activity并finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
    public void all_activity_finish()
    {
    	for (Activity activity : activityList) {
            if(!activity.equals(LoginActivity.class))
			activity.finish();
		}
    }
	public void stopService() {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < runningService.size(); i++) {
		 if (runningService.get(i).service.getClassName().toString()
					.equals("com.app_lock.service.HavingClass")) {
				Intent intent = new Intent(this, HavingClass.class);
				stopService(intent);
			} else if (runningService.get(i).service.getClassName().toString()
					.equals("com.app_lock.service.ClockService")) {
				Intent intent = new Intent(this, ClockService.class);
				stopService(intent);
			} else if (runningService.get(i).service.getClassName().toString()
					.equals("com.app_lock.service.WatchDogService")) {
				Intent intent = new Intent(this, WatchDogService.class);
				stopService(intent);
			}
		}
	}
}
