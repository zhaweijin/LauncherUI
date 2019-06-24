package com.mirageTeam.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppsInfo {
	private String appLabel;
	private Drawable iconDrawable;
	private Intent intent;
	private String pkgName;
	
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public Drawable getIconDrawable() {
		return iconDrawable;
	}
	public void setIconDrawable(Drawable iconDrawable) {
		this.iconDrawable = iconDrawable;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	
}
