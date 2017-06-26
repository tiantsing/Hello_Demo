package com.pub;

import java.security.PublicKey;
import java.util.Calendar;

import android.R.integer;
import android.content.SharedPreferences;
import android.util.Log;

public class SingleMyCalendar {
	private static SingleMyCalendar instance;
	Calendar calendar;
	int TodayYear;
	int TodayMonth;
	int TodayDay;
	int TodayWeek;
	int MondayYear;
	int MondayMonth;
	int MondayDay;
	int MondayWeek;
	int ShowMonth;
	int FirstWeekMonDay;
	int FirstWeekMonth;
	int FirstWeekYear;

	// 1周日 2周一 3周二
	public SingleMyCalendar() {
		// TODO Auto-generated constructor stub
		calendar = Calendar.getInstance();
		TodayYear = calendar.get(Calendar.YEAR);
		TodayMonth = calendar.get(Calendar.MONTH);
		TodayDay = calendar.get(Calendar.DAY_OF_MONTH);
		TodayWeek = calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static SingleMyCalendar getInstance() {
		if (instance == null) {
			synchronized (SingleMyCalendar.class) {
				if (instance == null) {
					instance = new SingleMyCalendar();
				}
			}
		}
		return instance;
	}

	public int getFirstWeekMonday() {
		return FirstWeekMonDay;
	}

	public int getFirstWeekMonth() {
		return FirstWeekMonth;
	}

	public int getFirstWeekYear() {
		return FirstWeekYear;
	}

	public int getTodayYear() {
		return TodayYear;
	}

	public int getTodayMonth() {
		return TodayMonth;
	}

	public int getTodayDay() {
		return TodayDay;
	}

	public int getTodayWeek() {
		return TodayWeek;
	}

	public int getMondayMonth() {
		return MondayMonth;
	}

	public int getShowMonth() {
		return ShowMonth;
	}

	// 获取周一是哪天
	public void getMonday() {
		calendar = Calendar.getInstance();
		switch (TodayWeek) {
		case 1:
			calendar.add(Calendar.DAY_OF_YEAR, -6);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 2:
			calendar.add(Calendar.DAY_OF_YEAR, 0);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 3:
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 4:
			calendar.add(Calendar.DAY_OF_YEAR, -2);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 5:
			calendar.add(Calendar.DAY_OF_YEAR, -3);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 6:
			calendar.add(Calendar.DAY_OF_YEAR, -4);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		case 7:
			calendar.add(Calendar.DAY_OF_YEAR, -5);
			MondayDay = calendar.get(Calendar.DAY_OF_MONTH);
			MondayYear = calendar.get(Calendar.YEAR);
			MondayWeek = calendar.get(Calendar.DAY_OF_WEEK);
			MondayMonth = calendar.get(Calendar.MONTH) + 1;
			break;
		}

	}

	public String getDate(int i, int weekNum) {
		String Day = "";
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(MondayYear, MondayMonth, MondayDay);
		switch (i) {
		case 1:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			ShowMonth = (calendar2.get(Calendar.MONTH));
			break;
		case 2:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 1);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		case 3:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 2);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		case 4:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 3);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		case 5:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 4);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		case 6:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 5);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		case 7:
			calendar2.add(Calendar.DAY_OF_YEAR, weekNum * 7 + 6);
			Day = calendar2.get(Calendar.DAY_OF_MONTH) + "";
			if (Day.equals("1"))
				Day = calendar2.get(Calendar.MONTH) + "月";
			break;
		}
		return Day;
	}
	/*
	 * 作用：获取本学期第一周的周一是哪天 参数：当前是哪一天 返回：NULL 内容：将周一的相关数据存到变量中
	 */

	public void getFirstWeekMonday(int CurrentWeek) {
		getMonday();
		Calendar calendar = Calendar.getInstance();
		calendar.set(MondayYear, MondayMonth, MondayDay);
		int day = -(CurrentWeek - 1) * 7;
		calendar.add(Calendar.DAY_OF_YEAR, day);
		FirstWeekMonDay = calendar.get(Calendar.DAY_OF_MONTH);
		FirstWeekMonth = calendar.get(Calendar.MONTH);
		FirstWeekYear = calendar.get(Calendar.YEAR);
	}

	public int getWeek(int YEAR, int MONTH, int DAY) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(YEAR, MONTH, DAY);
		Calendar Today = Calendar.getInstance();
		Log.i("Today", "" + Today.get(Calendar.DAY_OF_YEAR));
		Log.i("cal", "" + calendar.get(Calendar.DAY_OF_YEAR));
		int mod = (Today.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)) / 7;
		int week = 1 + mod;
		return week;
	}
    /**
     * 获取当前时间，并将其处理成字符串返回，格式为 "YYYY-MM-DD HH-MM"
     * @return
     */
	public String getNowTime() {
		Calendar calendar = Calendar.getInstance();
		String month;
		String year;
		String day;
		String hour;
		String minute;
		String time;
		if (calendar.get(Calendar.MONTH) != 0) {
			month = 12 + "";
		} else {
			month = calendar.get(Calendar.MONTH) + 1 + "";
		}
		year = calendar.get(Calendar.YEAR) + "";
		day = calendar.get(Calendar.DAY_OF_MONTH) + "";
		hour = calendar.get(Calendar.HOUR) + "";
		minute = calendar.get(Calendar.MINUTE) + "";
		time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
		return time;
	}
}
