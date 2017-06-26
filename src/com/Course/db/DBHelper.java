package com.Course.db;
import com.pub.SingleGlobalVariables;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_Course = SingleGlobalVariables.Phone+".db";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        // CursorFactory设置为null,使用默认值
        super(context, DATABASE_Course, null, DATABASE_VERSION);
    }
    // 数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists test (id INTEGER PRIMARY KEY AUTOINCREMENT , name VARCHAR(100),teacher VARCHAR(100) , classroom VARCHER(100),week INTEGER ,start INTEGER ,step INTEGER ,when_week INTEGER)");
    }
    // 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }
}

