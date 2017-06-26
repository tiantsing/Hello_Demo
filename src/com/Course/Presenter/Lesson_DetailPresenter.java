package com.Course.Presenter;

import com.Course.Interface.Lesson_DetailInterface;
import com.Course.Model.CourseModel;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import com.MainPage.Model.Main_PageModel;
import com.app.main.HelloApplication;
import com.pub.SingleGlobalVariables;

import android.content.Context;
import android.content.SharedPreferences;

public class Lesson_DetailPresenter {
	Lesson_DetailInterface lesson_Detail;
	CourseModel courseModel;
	Main_PageModel main_PageModel;
	Context context;
	DBManager mgr;// 数据库Manager
	SharedPreferences sf_userinfo;
	String phonepath;
	public Lesson_DetailPresenter(Lesson_DetailInterface view, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		lesson_Detail = view;
		mgr = new DBManager(context);
		courseModel = new CourseModel(context);
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo=HelloApplication.PDAConfig(context, "userinfo", true, phonepath,sf_userinfo);
		main_PageModel =new Main_PageModel(context);
	}
	public void delLesson(Coursedb coursedb)
	{
		mgr.del(coursedb);
	}
    public void getData()
    {
    	main_PageModel.getWeekNum(sf_userinfo.getInt("current_week_num", 1));
    	lesson_Detail.getData(main_PageModel.getData());
    }
	public void getCourseData(int lesson_week, String nameString, String teacherString,
			String classroomString, int lesson_start, int lesson_end)
	{
		lesson_Detail.getCourseData(courseModel.getCourseData(lesson_week, nameString, teacherString, classroomString, lesson_start, lesson_end));
	}

}
