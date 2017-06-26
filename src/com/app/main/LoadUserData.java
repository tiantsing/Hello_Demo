package com.app.main;

import com.app.main.HelloApplication;
import com.profile.ImageTools;
import com.pub.SingleGlobalVariables;
import com.pub.SingleMyCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoadUserData {
	static SharedPreferences sf_userinfo;
	static String phonepath;
	/**
	 * 加载函数，从数据库中获取到与账号相关的信息，并存储到文件和sql数据库中
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public static void load(Context context, String phone) {
		phonepath = HelloApplication.getPhonePath(phone);
		sf_userinfo = HelloApplication.PDAConfig(context, "userinfo", true, phonepath, sf_userinfo);
		SingleGlobalVariables.photo = ImageTools.getPhotoFromSDCard(phonepath, "head");
		Editor editor = sf_userinfo.edit();
		// 加载第一周的时间// MONTH是从0开始的,得到的数据从数据库读取
		SingleMyCalendar singleMyCalendar = SingleMyCalendar.getInstance();
		editor.putInt("current_week_num", singleMyCalendar.getWeek(2016, 7, 22));
		editor.commit();
		// 加载个人真实信息
	}

}
