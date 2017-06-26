package com.app_lock.utils;

import java.util.ArrayList;
import java.util.List;
import com.app_lock.domain.AppInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;


public class AppInfoProvider {
	public static List<AppInfo> getAppInfos(Context context) {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		String android = "com.android";
		String meizu ="com.meizu" ;
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, 0);
		for (ResolveInfo info : resolveInfo) {
			String packName = info.activityInfo.packageName;
			if (!packName.equals("com.example.hello1")&&(packName.indexOf(meizu)==-1)
					&& (packName.indexOf(android) == -1)||(packName.equals("com.android.browser"))) {
				String name = (String) info.loadLabel(packageManager);
				Drawable appIcon = info.loadIcon(packageManager);
				AppInfo appInfo = new AppInfo(name, packName, appIcon);
				appInfos.add(appInfo);
			}
		}
		return appInfos;
	}

}
