package com.Course.db;
public class Coursedb {
	String name, room, teacher;// 课程名称、上课教室，教师，课程编号
	int TextViewId;//对应的TextView的ID
	int start, stop; // 开始上课节次， 一共几节课
	int time;
	String week_numString;
	String lesson_numsString;
	int week;//周几
    int when_week;
	public Coursedb() {
		super();
	}
	public Coursedb(String name, String teacher, String room, int week,
			int start, int step,int when_week) {
		super();
		this.name = name;
		this.room = room;
		this.start = start;
		this.week = week;
		this.stop = step;
		this.teacher = teacher;
		this.when_week = when_week;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}
	
	public int getWhen_week() {
		return when_week;
	}
	
	public void setWhen_week(int when_week) {
		this.when_week = when_week;
	}

	public String getTeach() {
		return teacher;
	}

	public void setTeach(String teach) {
		this.teacher = teach;
	}

	public void setTextViewId(int id) {
		this.TextViewId = id;
	}

	public int getTextViewId() {
		return TextViewId;
	}
}
