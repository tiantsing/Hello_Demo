package com.app_lock.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String name;
	private String packName;
	private Drawable appIcon;
	public AppInfo(){}
	
	public AppInfo(String name, String packName, Drawable appIcon
		) {
		
		this.name = name;
		this.packName = packName;
		this.appIcon = appIcon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", packName=" + packName
				+ ", appIcon=" + appIcon + "]";
	}
	
}
