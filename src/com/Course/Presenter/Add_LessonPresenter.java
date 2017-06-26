package com.Course.Presenter;

import com.Course.Interface.Add_LessonInterface;
import com.MainPage.Model.Main_PageModel;
import com.app.main.HelloApplication;
import com.pub.SingleGlobalVariables;
import android.content.Context;
import android.content.SharedPreferences;


public class Add_LessonPresenter {
	Add_LessonInterface add_LessonInterface;
	Main_PageModel main_PageModel;
	Context context;
	String phonepath;
    SharedPreferences sf_userinfo;
	public Add_LessonPresenter(Add_LessonInterface add_LessonInterface, Context context) {
		// TODO Auto-generated constructor stub
		this.add_LessonInterface = add_LessonInterface;
		this.context = context;
		main_PageModel = new Main_PageModel(context);
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo=HelloApplication.PDAConfig(context, "userinfo", true, phonepath,sf_userinfo);
	}
	public void getData() {
		main_PageModel.getWeekNum(sf_userinfo.getInt("current_week_num", 1));
		add_LessonInterface.getData(main_PageModel.getData());
	}
}
