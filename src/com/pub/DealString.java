package com.pub;

import android.content.SharedPreferences;
import android.util.Log;
/**
 * 处理字符串等数据的类
 * @author Sun
 *
 */
public class DealString {

	public DealString() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 获取当前的月份，处理后返回字符串
	 * @author Sun
	 * @return String
	 */
	public static String DealMonth() {
		SingleMyCalendar myCalendar = new SingleMyCalendar();
		int Month = myCalendar.getTodayMonth() + 1;
		String M = "";
		if (Month < 10)
			M = "0" + Month;
		else
			M = Month + "";
		return M + "月";
	}
	/**
	 * 获取当前的天数，处理后返回字符串
	 * @author Sun
	 * @return String
	 */
	public static String DealDay() {
		SingleMyCalendar myCalendar = new SingleMyCalendar();
		int Day = myCalendar.getTodayDay();
		String D = "";
		if (Day < 10)
			D = "0" + Day;
		else
			D = Day + "";
		return D + "日";
	}
	/**
	 * 获取当前的星期数，处理后返回字符串
	 * @author Sun
	 * @return String
	 */
	public static String DealWeekString() {
		SingleMyCalendar myCalendar = new SingleMyCalendar();
		String W = "";
		switch (myCalendar.getTodayWeek()) {
		case 1:
			W = "日";
			break;
		case 2:
			W = "一";
			break;
		case 3:
			W = "二";
			break;
		case 4:
			W = "三";
			break;
		case 5:
			W = "四";
			break;
		case 6:
			W = "五";
			break;
		case 7:
			W = "六";
			break;
		}
		return "星期" + W;
	}
	/**
	 * 传入在一个星期中是第几天，处理后返回为星期几
	 * @author Sun
	 * @param int
	 * @return String
	 */
	public static String DealWeekString(int week) {
		String weekString = "";
		switch (week) {
		case 1:
			weekString = "一";
			break;
		case 2:
			weekString = "二";
			break;
		case 3:
			weekString = "三";
			break;
		case 4:
			weekString = "四";
			break;
		case 5:
			weekString = "五";
			break;
		case 6:
			weekString = "六";
			break;
		case 7:
			weekString = "七";
			break;
		default:
			Log.i("计算周几出错", "" + week);
		}
		return weekString;

	}
	/**
	 * 传入SharedPreferences对象，从xml中获取到
	 * @author Sun
	 * @return String
	 */
	public static String DealSemeter(SharedPreferences sf) {
		int daji = sf.getInt("currentSemeter", 1);
		String Semeter = "";
		switch (daji) {
		case 1:
			Semeter = "一";
			break;
		case 2:
			Semeter = "二";
			break;
		case 3:
			Semeter = "三";
			break;
		case 4:
			Semeter = "四";
			break;
		}
		return "大" + Semeter;
	}
    /**
     * 处理学期，返回学期
     * @param sf
     * @return
     */
	public static String DealTerm(SharedPreferences sf) {
		int xueqi = sf.getInt("currentterm", 1);
		String Term = "";
		switch (xueqi) {
		case 1:
			Term = "一";
			break;
		case 2:
			Term = "二";
			break;
		}
		return "第" + Term + "学期";
	}
    /**
     * 返回当天是星期几，返回数值类型
     * 
     * @return int
     */
	public static int DealWeek() {
		SingleMyCalendar myCalendar = new SingleMyCalendar();
		int week = 0;
		switch (myCalendar.getTodayWeek()) {
		case 1:
			week = 7;
			break;
		case 2:
			week = 1;
			break;
		case 3:
			week = 2;
			break;
		case 4:
			week = 3;
			break;
		case 5:
			week = 4;
			break;
		case 6:
			week = 5;
			break;
		case 7:
			week = 6;
			break;
		}
		return week;
	}
    
	public static String deal(int array[]) {
		int i = 0, a = 1, j = 0;
		String string = "";
		while (i < array.length) {
			if (array[i] + 1 == array[i + 1]) {
				i++;
				a++;
			} else {
				if (a > 3) {
					string += array[j] + "-" + array[i] + " ";
					j = i + 1;
					a = 0;
					i++;
				} else {
					while (j != i) {
						string += (array[j] + " ");
						j++;
						i++;
					}
				}
			}
		}
		return string;
	}
    /**
     * 出入课程开始节数和结束节数，返回数组，包含所有单个节数
     * @param start
     * @param stop
     * @return
     */
	public static int[] dealStarttoStop(int start, int stop) {
		int course[] = new int[12];
		for (int i = 0; i < 12; i++) {
			course[i] = 0;
		}
		if (start == stop)
			course[0] = start;
		int i = 0;
		while (start <= stop) {
			course[i] = start;
			start++;
			i++;
		}
		return course;
	}
    /**
     * 处理周数的字符串
     * @param int数组
     * @return String
     */
	public static String dealWeek(int week[]) {
		String weekStr = "";
		int size = 0;
		boolean First = true;
		for (int i = 0; i < week.length; i++) {
			if (week[i] != 0)
				size++;
		}
		if (size == 1)
			weekStr = "第" + week[0] + "周";
		else if (size == 2)
			weekStr = "第" + week[0] + " " + week[1] + "周";
		else if (size == 25)
			weekStr = "第" + week[0] + "-" + week[24] + "周";
	    else {
			for (int i = 0; i < 25;) {
				if (week[i] + 4 == week[i + 4]) {
				}
			}
		}
		return null;
	}
}
