package com.app_lock.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDBDao {
	private AppLockDBOpenHelper helper;
	private Context context;
	public AppLockDBDao(Context context){
		helper=new AppLockDBOpenHelper(context);
		this.context=context;
	}
	public boolean find(String name){
		boolean boo=false;
		SQLiteDatabase database=helper.getReadableDatabase();
		Cursor cursor=database.rawQuery("select * from applock where name=?", new String[]{name});
		if(cursor.moveToNext()){
			boo=true;
		}
		cursor.close();
		database.close();
		return boo;
	}
	public List<String> findAll(){
		List<String> names=new ArrayList<String>();
		SQLiteDatabase database=helper.getReadableDatabase();
		Cursor cursor=database.rawQuery("select name from applock" , null);
		while(cursor.moveToNext()){
			names.add(cursor.getString(0));
		}
		cursor.close();
		database.close();
		return names;
	}
	public void add(String name){
		SQLiteDatabase database=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("name", name);
		
		database.insert("applock", null, values);
		
		database.close();
		Intent intent=new Intent();
		intent.setAction("com.xiong.dbChanged");
		context.sendBroadcast(intent);
	}
	
	public void delete(String name){
		SQLiteDatabase database=helper.getWritableDatabase();
		database.delete("applock", "name=?", new String[]{name});
		database.close();
		Intent intent=new Intent();
		intent.setAction("com.xiong.dbChanged");
		context.sendBroadcast(intent);
	}
	
}
