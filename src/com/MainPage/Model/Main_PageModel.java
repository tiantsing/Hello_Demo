package com.MainPage.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import com.app_lock.dao.AppLockDBDao;
import com.app_lock.domain.AppInfo;
import com.app_lock.utils.AppInfoProvider;
import com.example.hello1.R;
import com.pub.DealString;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
/**
 * 主页Model
 * @author Sun
 *
 */
public class Main_PageModel {
	List<AppInfo> appInfos;
	boolean ischeack = true;
	AppLockDBDao dao;
	Context context;
	DBManager mgr;// 数据库Manager
	List<Coursedb> courseData;
	int weekNum;
	List<Map<String, Object>> mData;// 今天的所有课程
    int lessonNum[] = new int[12];// 存放今天所有的课程
	public Main_PageModel(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dao = new AppLockDBDao(context);
		mgr = new DBManager(context);
	}
	/**
	 * 获取到当天的所有课程
	 * @return List<Coursedb>
	 * @param void
	 * @author Sun
	 */
	public 	List<Coursedb> getCoursedb() {
		int week = DealString.DealWeek();
		courseData = mgr.query_week(week, weekNum);
		return courseData;
	}

	/**
	 * 检查服务是否开启
	 * @return boolean
	 * @param Context ,String
	 * @author Sun
	 */
	public static boolean isServiceWorked(Context context, String serviceName) {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**开启线程来获得所有的APP
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void getAPP() {
		new Thread() {
			public void run() {
				if (ischeack == true) {
					appInfos = AppInfoProvider.getAppInfos(context);
					for (AppInfo info : appInfos) {
						dao.add(info.getPackName());
					}
					ischeack = false;
				}
			}
		}.start();
	}
    /**
     * 获取今日是周几
     * @param weekNum
     * @return void
     */
	public void getWeekNum(int weekNum) {
		this.weekNum = weekNum;
	}
    /**
     * 获取课程信息并返回
     * @return List<Map<String, Object>>
     * @param void
     */
	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start, stop;
		String name;
		String room;
		int week = DealString.DealWeek();
		courseData = mgr.query_week(week, weekNum);
		for (int i = 0; i < courseData.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			start = courseData.get(i).getStart();
			stop = courseData.get(i).getStop();
			name = courseData.get(i).getName();
			room = courseData.get(i).getRoom();
			courseData.get(i).setTextViewId(i);
			map.put("start", start);
			map.put("stop", stop);
			map.put("course_jieshu", "" + start + "-" + stop + "");
			map.put("course_name", name);
			map.put("course_room", room);
			map.put("signed", R.drawable.signed_bg);
			list.add(map);
		}
		return list;
	}
}
