package com.mirageTeam.widget;

import java.util.ArrayList;
import java.util.List;

//import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.mirageTeam.Store.Adapter.AppsAdapter;
import com.mirageTeam.common.AppsInfo;
import com.mirageTeam.launcherui.R;

public class AppListBottonMenu extends ApplistBaseBottomMenu implements OnItemClickListener, OnClickListener {

	View mBtnAllApps;
	Button mBtnAppMgr;
	Button mBtnRecApp;
	GridView mAppGridView;

	AppsAdapter mAppsAdapter;
	PackageManager pm = null;
	List<AppsInfo> mAllAppsList;
	private List<ResolveInfo> resolveInfos;

//	@SuppressLint("NewApi")
	public AppListBottonMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mBtnAllApps = getContentView().findViewById(R.id.show_all_apps);
		mBtnAllApps.setOnClickListener(this);
		mBtnAllApps.setSelected(true);

		mBtnAppMgr = (Button) getContentView().findViewById(R.id.appmgr);
		mBtnAppMgr.setOnClickListener(this);

		mBtnRecApp = (Button) getContentView().findViewById(R.id.recopenapp);
		mBtnRecApp.setOnClickListener(this);

		mAppGridView = (GridView) getContentView().findViewById(R.id.appview);
		mAppGridView.setOnItemClickListener(this);
		mAllAppsList = new ArrayList<AppsInfo>();
		// lm.initLoader(0, null, this);
		pm = mContext.getApplicationContext().getPackageManager();
		getApps();

	}

	private void getApps() {
		new AsyncTask<Void, Void, List<AppsInfo>>() {
			List<AppsInfo> alllist = new ArrayList<AppsInfo>();

			@Override
			protected List<AppsInfo> doInBackground(Void... params) {
				Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
				mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				resolveInfos = mContext.getPackageManager().queryIntentActivities(mIntent, 0);

				for (ResolveInfo infos : resolveInfos) {
					String appLabel = (String) infos.loadLabel(pm);
					Drawable iconDrawable = (Drawable) infos.loadIcon(pm);
					if (iconDrawable != null) {
					}
					Intent launcherIntent = new Intent();
					String pkgName = infos.activityInfo.packageName;

					if (!pkgName.equals(mContext.getPackageName())) {

						String activitName = infos.activityInfo.name;
						launcherIntent.setComponent(new ComponentName(pkgName, activitName));

						AppsInfo appInfo = new AppsInfo();
						appInfo.setAppLabel(appLabel);
						appInfo.setIconDrawable(iconDrawable);
						appInfo.setIntent(launcherIntent);
						appInfo.setPkgName(pkgName);

						alllist.add(appInfo);
					} else {
						continue;
					}
				}
				return alllist;
			}

			@Override
			protected void onPostExecute(List<AppsInfo> data) {
				mAllAppsList = data;
				mAppGridView.setAdapter(new AppsAdapter(mContext, data));
			};

		}.execute();
	}

	public void reload(){
		getApps();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		AppsInfo appInfo = (AppsInfo) adapter.getItemAtPosition(position);
		Intent intent = appInfo.getIntent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		mContext.getApplicationContext().startActivity(intent);
	}

	@Override
	View baseBottomMenuContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(mContext).inflate(R.layout.app_list_layout, null);
	}

	private List<RecentTaskInfo> mRecentTaskInfos;

	private List<AppsInfo> getRecentAppList() {
		// private void getAppList() {

		ActivityManager am = (ActivityManager) mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		mRecentTaskInfos = am.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
		List<AppsInfo> list = new ArrayList<AppsInfo>();

		for (RecentTaskInfo mRecentTaskInfo : mRecentTaskInfos) {
			Intent intent = mRecentTaskInfo.baseIntent;
			ResolveInfo mResolveInfo = pm.resolveActivity(intent, 0);

			if (mResolveInfo != null) {
				String appLabel = (String) mResolveInfo.loadLabel(pm);
				Drawable iconDrawable = (Drawable) mResolveInfo.loadIcon(pm);

				if (iconDrawable != null) {

				}

				Intent launcherIntent = new Intent();
				String pkgName = mResolveInfo.activityInfo.packageName;
				if (!pkgName.equals(mContext.getPackageName())) {
					String activitName = mResolveInfo.activityInfo.name;
					launcherIntent.setComponent(new ComponentName(pkgName, activitName));

					AppsInfo appInfo = new AppsInfo();
					appInfo.setAppLabel(appLabel);
					appInfo.setIconDrawable(iconDrawable);
					appInfo.setIntent(launcherIntent);
					appInfo.setPkgName(pkgName);

					list.add(appInfo);
				} else {
					continue;
				}

			}
		}
		return list;
	}

	private boolean all_app_flag = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_all_apps:

			break;

		case R.id.appmgr:
			Intent intent = new Intent(Intent.ACTION_ALL_APPS);
			ComponentName cName = new ComponentName("com.android.settings", "com.android.settings.ManageApplications");
			intent.setComponent(cName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.getApplicationContext().startActivity(intent);

			break;
		case R.id.recopenapp:
			if (all_app_flag) {
				mBtnRecApp.setText(R.string.app_recently_opened);
				mAppGridView.setAdapter(new AppsAdapter(mContext, mAllAppsList));
				all_app_flag = false;

			} else {
				mBtnRecApp.setText(R.string.show_all_apps);
				all_app_flag = true;
				mAppGridView.setAdapter(new AppsAdapter(mContext, getRecentAppList()));// getAppList();
			}
			break;
		default:
			break;
		}
	}
}
