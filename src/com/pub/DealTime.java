package com.pub;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DealTime {
	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	public static String getPreTime(String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(new Date());
		String mydate1 = "";
		try {
			Date date1 = format.parse(currentTime);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	public static long getChaTime(String time) {
		long diff = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = df.format(new Date());
		try {
			Date d1 = df.parse(currentTime);
			Date d2 = df.parse(time);
			diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
		} catch (Exception e) {
		}
		return diff;
	}
}
