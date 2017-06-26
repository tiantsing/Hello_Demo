package com.Course.Model;

import java.util.ArrayList;
import java.util.List;
import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import android.content.Context;

public class CourseModel {
	Context context;
	DBManager mgr;// 数据库Manager
	List<Coursedb>[] courseData = new ArrayList[7];
	List<Coursedb> courseweek = new ArrayList<Coursedb>();
	static final int MONDAY = 1;
	static final int TUESDAY = 2;
	static final int WEDNESDAY = 3;
	static final int THURSDAY = 4;
	static final int FRIDAY = 5;
	static final int SATURDAY = 6;
	static final int SUNDAY = 7;

	public CourseModel(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mgr = new DBManager(context);
	}
	public List<Coursedb> getCourseData(int lesson_week, String nameString, String teacherString,
			String classroomString, int lesson_start, int lesson_end) {
		courseweek = mgr.query_week(lesson_week, nameString, teacherString, classroomString, lesson_start, lesson_end);
		return courseweek;
	}

	public List<Coursedb>[] getCourseData(int showingWeek) {
		courseData[0] = mgr.query_week(MONDAY, showingWeek);
		courseData[1] = mgr.query_week(TUESDAY, showingWeek);
		courseData[2] = mgr.query_week(WEDNESDAY, showingWeek);
		courseData[3] = mgr.query_week(THURSDAY, showingWeek);
		courseData[4] = mgr.query_week(FRIDAY, showingWeek);
		courseData[5] = mgr.query_week(SATURDAY, showingWeek);
		courseData[6] = mgr.query_week(SUNDAY, showingWeek);
		return courseData;
	}
}
