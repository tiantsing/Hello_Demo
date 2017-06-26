package com.Course.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {
	public DBHelper helper;
	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
	}

	public void add(Coursedb courses) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("INSERT INTO test(name,teacher,classroom,week,start,step,when_week) VALUES (?,?,?,?,?,?,?)",
				new Object[] { courses.name, courses.teacher, courses.room, courses.week, courses.start, courses.stop,
						courses.when_week });
		System.out.println("插入了：数据库, hello");
        db.close();
	}

	public void update(Coursedb courses1, Coursedb courses2) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", courses1.name);
		values.put("teacher", courses1.teacher);
		values.put("classroom", courses1.room);
		values.put("week", courses2.week);
		values.put("start", courses2.start);
		values.put("step", courses2.stop);
		values.put("when_week", courses2.when_week);
		String week = String.valueOf(courses2.week);
		String start = String.valueOf(courses2.start);
		String step = String.valueOf(courses2.stop);
		String when_week = String.valueOf(courses2.when_week);
		db.update("test", values,
				"name =? and teacher =? and classroom =? and week =? and start =? and step =? and when_week =?",
				new String[] { courses2.name, courses2.teacher, courses2.room, week, start, step, when_week });
		db.close();

	}

	public void del(Coursedb courses) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("test",
				"name = ? and teacher = ? and classroom =? and week = ? and start =? and step =? and when_week =?",
				new String[] { courses.name, courses.teacher, courses.room, courses.week + "", courses.start + "",
						courses.stop + "", courses.when_week + "" });
        db.close();
	}

	public Cursor query_name(String name) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT DISTINCT * FROM test WHERE name = ? order by name ASC", new String[] { name });
		db.close();
		return c;
	}

	public List<Coursedb> query_AllCourseAndTeacher() {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Coursedb> coursedbs = new ArrayList<Coursedb>();
		Cursor c = db.rawQuery("SELECT DISTINCT name,teacher FROM test", null);
		while (c.moveToNext()) {
			Coursedb coursedb = new Coursedb();
			coursedb.name = c.getString(c.getColumnIndex("name"));
			coursedb.teacher = c.getString(c.getColumnIndex("teacher"));
			coursedbs.add(coursedb);
		}
        db.close();
		return coursedbs;
	}

	public List<Coursedb> query_week(int week, int when_week) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Coursedb> coursedbs = new ArrayList<Coursedb>();
		String weekString = String.valueOf(week);
		String when_weekString = String.valueOf(when_week);
		Cursor c = db.rawQuery("SELECT DISTINCT * FROM test WHERE week = ? and when_week =? order by start ASC",
				new String[] { weekString, when_weekString });
		while (c.moveToNext()) {
			Coursedb coursedb = new Coursedb();
			coursedb.name = c.getString(c.getColumnIndex("name"));
			coursedb.teacher = c.getString(c.getColumnIndex("teacher"));
			coursedb.room = c.getString(c.getColumnIndex("classroom"));
			coursedb.start = c.getInt(c.getColumnIndex("start"));
			coursedb.stop = c.getInt(c.getColumnIndex("step"));
			coursedb.week = c.getInt(c.getColumnIndex("week"));
			coursedb.when_week = c.getInt(c.getColumnIndex("when_week"));
			coursedbs.add(coursedb);
		}
		db.close();
		return coursedbs;
	}

	public List<Coursedb> query_week(int week, String name, String teacherString, String room, int start, int stop) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Coursedb> coursedbs = new ArrayList<Coursedb>();
		String startString = String.valueOf(start);
		String stopString = String.valueOf(stop);
		String weekString = String.valueOf(week);
		Cursor c = db.rawQuery(
				"SELECT  * FROM test WHERE name =? and teacher =? and classroom =? and week =? and start =? and step =? order by start ASC",
				new String[] { name, teacherString, room, weekString, startString, stopString });
		while (c.moveToNext()) {
			Coursedb coursedb = new Coursedb();
			coursedb.name = c.getString(c.getColumnIndex("name"));
			coursedb.teacher = c.getString(c.getColumnIndex("teacher"));
			coursedb.room = c.getString(c.getColumnIndex("classroom"));
			coursedb.start = c.getInt(c.getColumnIndex("start"));
			coursedb.stop = c.getInt(c.getColumnIndex("step"));
			coursedb.week = c.getInt(c.getColumnIndex("week"));
			coursedb.when_week = c.getInt(c.getColumnIndex("when_week"));
			coursedbs.add(coursedb);
		}
		db.close();
		return coursedbs;
	}

	public Cursor query_week1(int week) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String weekString = String.valueOf(week);
		Cursor c = db.rawQuery("SELECT DISTINCT * FROM test WHERE week = ?", new String[] { weekString });
		db.close();
		return c;
	}
}