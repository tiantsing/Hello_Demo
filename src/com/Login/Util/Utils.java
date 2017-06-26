package com.Login.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.BasicNameValuePair;
import com.example.hello1.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * App 启动动画 Utils
 * 
 * @author tianq 
 * 
 * 最近一次修改 2016-08-15 10：25 AM
 */
@SuppressWarnings("deprecation")
public class Utils {
	// 长Toast
	public static void showLongToast(Context context, String pMsg) {
		Toast.makeText(context, pMsg, Toast.LENGTH_LONG).show();
	}

	// 短Toast
	public static void showShortToast(Context context, String pMsg) {
		Toast.makeText(context, pMsg, Toast.LENGTH_SHORT).show();
	}

	//结束Activity
	public static void finish(Activity activity) {
		activity.finish();
		activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
    //页面跳转
	@SuppressWarnings("deprecation")
	public static void start_Activity(Activity activity, Class<?> cls, BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (name != null)
			for (int i = 0; i < name.length; i++) {
				intent.putExtra(name[i].getName(), name[i].getValue());
			}
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

	}
	
	    //判断是否连接网络
		public static boolean isNetworkAvailable(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager == null) {
				return false;
			} else {
				NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
				if (info != null) {
					for (NetworkInfo network : info) {
						if (network.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
			return false;
		}
    //判断手机号
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
