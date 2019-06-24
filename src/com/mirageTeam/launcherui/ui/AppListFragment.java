package com.mirageTeam.launcherui.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mirageTeam.Store.Adapter.AppsAdapter;
import com.mirageTeam.common.AppsInfo;
import com.mirageTeam.launcherui.R;

public class AppListFragment extends Fragment implements OnItemClickListener {

	private PackageManager pm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// loadAllApps();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.app_list_layout, container, false);

		GridView appGridView = (GridView) rootView.findViewById(R.id.appview);
		appGridView.setAdapter(new AppsAdapter(getActivity(), loadAllApps()));
		appGridView.setOnItemClickListener(this);
		return rootView;

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "onItemClick at : " + position, Toast.LENGTH_LONG).show();
		Log.i("onItemClick", "onItemClick at : " + position);
	}

	private List<AppsInfo> loadAllApps() {
		pm = getActivity().getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mAppsInfos = pm.queryIntentActivities(mainIntent, 0);

		List<AppsInfo> list = new ArrayList<AppsInfo>();

		for (ResolveInfo mRecentTaskInfo : mAppsInfos) {
			String appLabel = (String) mRecentTaskInfo.loadLabel(pm);
			Drawable iconDrawable = (Drawable) mRecentTaskInfo.loadIcon(pm);
			if (iconDrawable != null) {
			}
			Intent launcherIntent = new Intent();
			String pkgName = mRecentTaskInfo.activityInfo.packageName;
			String activitName = mRecentTaskInfo.activityInfo.name;
			launcherIntent.setComponent(new ComponentName(pkgName, activitName));

			AppsInfo appInfo = new AppsInfo();
			appInfo.setAppLabel(appLabel);
			appInfo.setIconDrawable(iconDrawable);
			appInfo.setIntent(launcherIntent);
			appInfo.setPkgName(pkgName);

			list.add(appInfo);
		}
		return list;
	}

	private List<RecentTaskInfo> mRecentTaskInfos;

	private List<AppsInfo> getRecentAppList() {
		// private void getAppList() {

		ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		mRecentTaskInfos = am.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
		List<AppsInfo> list = new ArrayList<AppsInfo>();

		for (RecentTaskInfo mRecentTaskInfo : mRecentTaskInfos) {
			Intent intent = mRecentTaskInfo.baseIntent;
			ResolveInfo mResolveInfo = pm.resolveActivity(intent, 0);

			if (mResolveInfo != null) {
				Log.i("***********", mResolveInfo.loadLabel(pm).toString());
				String appLabel = (String) mResolveInfo.loadLabel(pm);
				Drawable iconDrawable = (Drawable) mResolveInfo.loadIcon(pm);

				if (iconDrawable != null) {

				}

				Intent launcherIntent = new Intent();
				String pkgName = mResolveInfo.activityInfo.packageName;
				String activitName = mResolveInfo.activityInfo.name;
				launcherIntent.setComponent(new ComponentName(pkgName, activitName));

				AppsInfo appInfo = new AppsInfo();
				appInfo.setAppLabel(appLabel);
				appInfo.setIconDrawable(iconDrawable);
				appInfo.setIntent(launcherIntent);
				appInfo.setPkgName(pkgName);

				list.add(appInfo);
			}
		}
		return list;
	}

}
