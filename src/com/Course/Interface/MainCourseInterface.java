package com.Course.Interface;

import java.util.List;
import com.Course.db.Coursedb;

public interface MainCourseInterface {
    public void getMonthDate(String Date);//获取第一列显示的月份信息
    public void getMondayDate(String Date);//获取周一到周末的日期信息
    public void getTuesdayDate(String Date);
    public void getWednesdayDate(String Date);
    public void getThursdayDate(String Date);
    public void getFridayDate(String Date);
    public void getSaturdayDate(String Date);
    public void getSundayDate(String Date);
    public void getCourseData(List<Coursedb>[] lists);//获取课程数据列表
}
