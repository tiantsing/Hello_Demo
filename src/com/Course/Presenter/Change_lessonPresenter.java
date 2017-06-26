package com.Course.Presenter;

import com.Course.db.Coursedb;
import com.Course.db.DBManager;
import android.content.Context;

public class Change_lessonPresenter {
	DBManager mgr;
	Context context;
	public Change_lessonPresenter(Context context) {
		// TODO Auto-generated constructor stub
		mgr = new DBManager(context);
		this.context =context;
	}
	public void delLesson(Coursedb courses)
	{
		mgr.del(courses);
	}
	public void addLesson(Coursedb courses)
	{
		mgr.add(courses);
	}
}
