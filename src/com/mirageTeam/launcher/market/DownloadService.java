package com.mirageteam.launcher.market;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/*
 * @author carter
 * 管理下载的服务
 */
public class DownloadService extends Service {


	private String software_name; // 软件名称
	private String path; // 文件路径
	private String packagename;
	private SharedPreferences settingPreferences;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		path = intent.getStringExtra("path");
		packagename = intent.getStringExtra("package");
		try {

			Util.startDownload(this,path,packagename);
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();

	
	}

}
