package com.Discover.notes;

import java.util.ArrayList;
import java.util.List;

import com.pub.SingleGlobalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = SingleGlobalVariables.Phone + "note_db"; // 数据库名
	private final static int DATABASE_VERSION = 1; // 版本号
	private final static String TABLE_NAME = "notepad";
	public final static String NOTE_ID = "_id";
	public final static String NOTE_TITLE = "title";
	public final static String NOTE_CONTENT = "content";
	public final static String NOTE_TYPE ="type";
	public final static String NOTE_TIME = "time";

	/* 构造函数 */
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* 创建数据库 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 定义SQL语句
		String sql = "create table " + TABLE_NAME + " (" + NOTE_ID + " integer primary key autoincrement, " + NOTE_TITLE
				+ " text," + NOTE_TIME + " text," + NOTE_TYPE
				+ " text," + NOTE_CONTENT + " text )";
		// 直接执行 sql 语句
		db.execSQL(sql);
	}

	public Cursor selectNotes() {
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getReadableDatabase();
		// 获取一个指向数据库的游标，用来查询数据库
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	/**
	 * 插入记事
	 */
	public long insertNote(String title, String content, String time ,String type) {
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * 将需要修改的数据放在 ContentValues 对象中 ContentValues
		 * 是以键值对形式储存数据，其中键是数据库的列名，值是列名对应的数据
		 */
		Log.i("", "" + NOTE_TITLE);
		Log.i("", "" + NOTE_CONTENT);
		Log.i("", "" + NOTE_TIME);
		ContentValues cv = new ContentValues();
		cv.put(NOTE_TITLE, title);
		cv.put(NOTE_CONTENT, content);
		cv.put(NOTE_TIME, time);
		cv.put(NOTE_TYPE, type);
		Log.i("TYPE", "" + type);
		// insert()方法：插入数据，成功返回行数，否则返回-1
		long rowid = db.insert(TABLE_NAME, null, cv);
		db.close();
		return rowid;
	}

	/**
	 * 删除记事
	 * 
	 * @param id
	 *            _id字段
	 */
	public boolean deleteNote(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = NOTE_ID + "=?";
		String[] whereValues = { id };
		// delete方法：根据条件删除数据，where表示删除的条件
		boolean is = (db.delete(TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}

	/**
	 * 更新记事
	 * 
	 * @param id
	 *            _id字段
	 */
	public int updateNote(String id, String title, String content, String time,String type) {
		Log.i("gengxin", id);
		Log.i("gengxin", title);
		Log.i("gengxin", content);
		Log.i("gengxin", time);
		SQLiteDatabase db = this.getWritableDatabase();
		String where = NOTE_ID + "=?";
		String[] whereValues = { id };
		ContentValues cv = new ContentValues();
		cv.put(NOTE_TITLE, title);
		cv.put(NOTE_CONTENT, content);
		cv.put(NOTE_TIME, time);
		cv.put(NOTE_TYPE, type);
		// update()方法：根据条件更新数据库，cv保存更新后的数据，where为更新条件
		int numRow = db.update(TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}

	/**
	 * 查找所有记事
	 */
	public List<NotesObj> queryNote() {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<NotesObj> notesobj_list = new ArrayList<NotesObj>();
		// Cursor cursor = db.rawQuery("select * from notepad ",null);
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			NotesObj note = new NotesObj();
			note.Title = cursor.getString(cursor.getColumnIndex("title"));
			note.Content = cursor.getString(cursor.getColumnIndex("content"));
			note.Time = cursor.getString(cursor.getColumnIndex("time"));
			note.Id = cursor.getString(cursor.getColumnIndex("_id"));
			note.Type = cursor.getString(cursor.getColumnIndex("type"));
			notesobj_list.add(note);
		}
		db.close();
		return notesobj_list;
	}

	public NotesObj queryId(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		NotesObj notesobj_list = new NotesObj();
//		Cursor cursor = db.query(TABLE_NAME, new String[]{"_id"},"_id = ?", new String[]{id}, null, null, null);
		Cursor cursor = db.rawQuery("SELECT DISTINCT * FROM notepad WHERE _id = ? ", new String[] {id});
		while (cursor.moveToNext()) {
			notesobj_list.Title = cursor.getString(cursor.getColumnIndex("title"));
			notesobj_list.Content = cursor.getString(cursor.getColumnIndex("content"));
			notesobj_list.Time = cursor.getString(cursor.getColumnIndex("time"));
			notesobj_list.Id = cursor.getString(cursor.getColumnIndex("_id"));
			notesobj_list.Type = cursor.getString(cursor.getColumnIndex("type"));
			Log.i("33333333333",	notesobj_list.Id );
			Log.i("33333333333",	notesobj_list.Time);
			Log.i("33333333333",	notesobj_list.Title);
			Log.i("33333333333",	notesobj_list.Content);
			Log.i("33333333333",	notesobj_list.Type);
		}
		db.close();
		return notesobj_list;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
