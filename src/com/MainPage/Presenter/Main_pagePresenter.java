package com.MainPage.Presenter;

import com.MainPage.Interface.Main_PageInterface;
import com.MainPage.Model.Main_PageModel;
import android.content.Context;
/**
 * 主页 Presenter类，属于MVP模式的P，中间层，连接M和V
 * @author Administrator
 */
public class Main_pagePresenter {
	Context context;
	Main_PageInterface main_PageActivity;
	Main_PageModel main_PageModel;


	public Main_pagePresenter() {
		super();
	}

	public Main_pagePresenter(Main_PageInterface main_PageInterface, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.main_PageActivity = main_PageInterface;
		main_PageModel = new Main_PageModel(context);
	}
	public void getApp() {
		main_PageModel.getAPP();
	}
	public void getData() {
		main_PageActivity.getData(main_PageModel.getData());
	}
	public void getCoursedb() {
		main_PageModel.getWeekNum(main_PageActivity.setWeekNum());
		main_PageActivity.getCoursedb(main_PageModel.getCoursedb());
	}
}
