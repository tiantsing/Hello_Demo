package com.Course.Model;

public class Course_Time {
	int morning_class_start_time = 480;
	int afternoon_class_start_time = 840;
	int night_class_start_time = 1140;
	int little_recess = 10;
	int big_recess = 20;
	int course_time = 45;// 每节时长

	public Course_Time(int morning_class_start_time1,
			int afternoon_class_start_time1, int night_class_start_time1,
			int little_recess1, int big_recess1, int course_time1) {
		this.morning_class_start_time = morning_class_start_time1;
		this.afternoon_class_start_time = afternoon_class_start_time1;
		this.night_class_start_time = night_class_start_time1;
		this.little_recess = little_recess1;
		this.big_recess = big_recess1;
		this.course_time = course_time1;
	}

	public int get_time(int i) {
		int time = 480;
		switch (i) {
		case 1:
			time = morning_class_start_time;
			break;
		case 2:
			time = morning_class_start_time + (i - 1) * course_time
					+ little_recess;
			break;
		case 3:
			time = morning_class_start_time + (i - 1) * course_time
					+ little_recess + big_recess;
			break;
		case 4:
			time = morning_class_start_time + (i - 1) * course_time
					+ little_recess * 2 + big_recess;
			break;
		case 5:
			time = afternoon_class_start_time;
			break;
		case 6:
			time = afternoon_class_start_time + (i - 5) * course_time
					+ little_recess;
			break;
		case 7:
			time = afternoon_class_start_time + (i - 5) * course_time
					+ little_recess + big_recess;
			break;
		case 8:
			time = afternoon_class_start_time + (i - 5) * course_time
					+ little_recess * 2 + big_recess;
			break;
		case 9:
			time = night_class_start_time;
			break;
		case 10:
			time = night_class_start_time + (i - 9) * course_time
					+ little_recess;
			break;
		case 11:
			time = night_class_start_time + (i - 9) * course_time
					+ little_recess + big_recess;
			break;
		case 12:
			time = night_class_start_time + (i - 9) * course_time
					+ little_recess * 2 + big_recess;
			break;
		default:
			break;
		}
		return time;
	}

	public String put_time(int start, int end) {
		int start_time = get_time(start);
		int end_time = get_time(end) + course_time;
		int start_hour = start_time / 60;
		int start_minute = start_time - start_hour * 60;
		int end_hour = end_time / 60;
		int end_minute = end_time - end_hour * 60;
		String startString = "", endString = "";
		if (start_minute < 10) {
			startString = start_hour + ":" + "0" + start_minute;
		}
		if (start_minute >= 10) {
			startString = start_hour + ":" + start_minute;
		}
		if (end_minute < 10) {
			endString = end_hour + ":" + "0" + end_minute;
		}
		if (end_minute >= 10) {
			endString = end_hour + ":" + end_minute;
		}
		String a = startString + " ~ " + endString;
		return a;
	}

	public String set_course_time(int i) {

		int time = get_time(i);
		int hour = time / 60;
		int minute = time - hour * 60;
		String b;
		if (minute >= 10)
			b = minute + "";
		else
			b = "0" + minute;
		String a = hour + ":" + b;
		return a;
	}
}
