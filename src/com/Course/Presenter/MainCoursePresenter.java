package com.Course.Presenter;

import com.Course.Interface.MainCourseInterface;
import com.Course.Model.CourseModel;
import com.Course.Model.Course_Time;
import com.app.main.HelloApplication;
import com.example.hello1.R;
import com.pub.SingleGlobalVariables;
import com.pub.SingleMyCalendar;
import com.pub.SingleSetColor;
import android.content.Context;
import android.content.SharedPreferences;

public class MainCoursePresenter {
	MainCourseInterface mainCourseInterface;
	SingleMyCalendar calendar;
	CourseModel courseModel;
	Context context;
	SingleSetColor setColor;
	Course_Time time;// 设置第一列的时间信息
	SharedPreferences sf_userinfo;
	String phonepath;
	public MainCoursePresenter(MainCourseInterface view, Context context) {
		// TODO Auto-generated constructor stub
		calendar = SingleMyCalendar.getInstance();
		calendar.getMonday();// 可以在开始时初始化
		this.mainCourseInterface = view;
		courseModel = new CourseModel(context);
		setColor = SingleSetColor.getInstance();
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(context, "userinfo", true, phonepath, sf_userinfo);
		// sharedPreferenceModel= new SharedPreferenceModel(context);
		time = new Course_Time(sf_userinfo.getInt("morning_class_start_time_num", 480),
				sf_userinfo.getInt("afternoon_class_start_time_num", 840),
				sf_userinfo.getInt("night_class_start_time_num", 1140), sf_userinfo.getInt("little_recess_num", 10),
				sf_userinfo.getInt("big_recess_num", 20), sf_userinfo.getInt("every_class_time_num", 45));
	}

	public void getCourseData(int week) {
		mainCourseInterface.getCourseData(courseModel.getCourseData(week));
	}

	public int getcolor(String Name) {
		return setColor.cheackName(Name);
	}

	public String getTime(int i) {
		return time.set_course_time(i);
	}

	/*
	 * 作用：获得当前周的日期数据 参数：显示周与当前周的差 返回：NULL
	 * 内容：通过调用SingleMyCalendar的getData函数获取盖州时间，调用MainCourseInterface来返回数据
	 */
	public void getTodayDate(int week) {
		String Month;
		String Monday;
		String Tuesday;
		String Wednesday;
		String Thursday;
		String Friday;
		String Saturday;
		String Sunday;
		Monday = calendar.getDate(1, week);
		Tuesday = calendar.getDate(2, week);
		Wednesday = calendar.getDate(3, week);
		Thursday = calendar.getDate(4, week);
		Friday = calendar.getDate(5, week);
		Saturday = calendar.getDate(6, week);
		Sunday = calendar.getDate(7, week);
		Month = calendar.getShowMonth() + "";// 因为月数是在getDate（）中获取到的，所以要先运行getDate函数
		mainCourseInterface.getMonthDate(Month);
		mainCourseInterface.getMondayDate(Monday);
		mainCourseInterface.getTuesdayDate(Tuesday);
		mainCourseInterface.getWednesdayDate(Wednesday);
		mainCourseInterface.getThursdayDate(Thursday);
		mainCourseInterface.getFridayDate(Friday);
		mainCourseInterface.getSaturdayDate(Saturday);
		mainCourseInterface.getSundayDate(Sunday);
	}
}
