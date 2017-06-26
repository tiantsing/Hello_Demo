package com.pub.Dialog;

import com.pub.SingleGlobalVariables;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings;


public class AuthorizationDialog{
	Context context;
	public AuthorizationDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context =context;
	}
	
	
	// 检查是否设置了查看最近程序使用情况的权限
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public boolean isStatAccessPermissionSet() throws NameNotFoundException {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
			AppOpsManager aom = (AppOpsManager) context.getSystemService(context.APP_OPS_SERVICE);
			aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
			return aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid,
					info.packageName) == AppOpsManager.MODE_ALLOWED;
		} catch (Exception e) {
		}
		return false;
	}
	public void Dialog() {
		new AlertDialog.Builder(context).setTitle("设置权限").setMessage("使用本APP需要给予查看最近程序使用情况的权限，是否前去设置")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method
						// stub
						Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
						context.startActivity(intent);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent i = new Intent(Intent.ACTION_MAIN);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.addCategory(Intent.CATEGORY_HOME);
						context.startActivity(i);
					}
				}).show();
	}
	public void isSetAuthorization()
	{
		try {
			if (isStatAccessPermissionSet()) {
				SingleGlobalVariables.IsSetRecentAppUse = true;
			} else {
				Dialog();
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
