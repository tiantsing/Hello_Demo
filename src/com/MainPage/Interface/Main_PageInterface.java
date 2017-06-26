package com.MainPage.Interface;

import java.util.List;
import java.util.Map;

import com.Course.db.Coursedb;
/**
 * 主页接口类
 * @author Sun
 *
 */
public interface Main_PageInterface {
	public void getCoursedb(List<Coursedb> coursedb);
	public int setWeekNum();
	public void getData(List<Map<String, Object>> Data);
	public void showSucessToast(int scuess);
}
