package com.mirageTeam.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.AsyncTaskLoader;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * A custom Loader that loads all of the installed applications.
 */
public class AppsLoader extends AsyncTaskLoader<List<AppsInfo>> {
	final PackageManager mPackageManager;
	ActivityManager am = null;

	List<ResolveInfo> mApps;
	private List<RecentTaskInfo> mRecentTaskInfos;

	List<AppsInfo> mlistAppInfos;
	PackageIntentReceiver mPackageObserver;

	/*
	 * LoaderType: 0 for All apps loader 1 for Recently open apps loader
	 * default: 0
	 */
	int loadertype = 0;
	public final static int LOAD_ALL_APPS = 0;
	public final static int LOAD_RECENT_APPS = 1;

	public AppsLoader(Context context) {
		super(context);

		// Retrieve the package manager for later use; note we don't
		// use 'context' directly but instead the save global application
		// context returned by getContext().
		mPackageManager = context.getPackageManager();
		
//		this.am = activityManager;
//		loadertype = type;
		// mActivityManager =(ActivityManager)
		// getSystemService(Context.ACTIVITY_SERVICE);

	}

	/**
	 * This is where the bulk of our work is done. This function is called in a
	 * background thread and should generate a new set of data to be published
	 * by the loader.
	 */
	@Override
	public List<AppsInfo> loadInBackground() {
		// Retrieve all known applications.
		List<AppsInfo> list = new ArrayList<AppsInfo>();

		if (loadertype == LOAD_ALL_APPS) {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			mApps = mPackageManager.queryIntentActivities(mainIntent, 0);
			Collections.sort(mApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

			for (ResolveInfo resolveInfo : mApps) {
				String appLabel = (String) resolveInfo.loadLabel(mPackageManager);
				Drawable iconDrawable = (Drawable) resolveInfo.loadIcon(mPackageManager);
				if (iconDrawable != null) {
					
				}
				Intent launcherIntent = new Intent();
				String pkgName = resolveInfo.activityInfo.packageName;
				String activitName = resolveInfo.activityInfo.name;
				launcherIntent.setComponent(new ComponentName(pkgName, activitName));

				AppsInfo appInfo = new AppsInfo();
				appInfo.setAppLabel(appLabel);
				appInfo.setIconDrawable(iconDrawable);
				appInfo.setIntent(launcherIntent);
				appInfo.setPkgName(pkgName);

				list.add(appInfo);

			}
		} else if (loadertype == LOAD_RECENT_APPS) {
			mRecentTaskInfos = am.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);

			for (RecentTaskInfo mRecentTaskInfo : mRecentTaskInfos) {
				
				Intent intent = mRecentTaskInfo.baseIntent;
				ResolveInfo mResolveInfo = mPackageManager.resolveActivity(intent, 0);
				
				String appLabel = (String) mResolveInfo.loadLabel(mPackageManager);
				Drawable iconDrawable = (Drawable) mResolveInfo.loadIcon(mPackageManager);
				
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
				Log.i("***********loadInBackground : ", mResolveInfo.loadLabel(mPackageManager).toString());
			}
		}
		return list;
	}

	/**
	 * Called when there is new data to deliver to the client. The super class
	 * will take care of delivering it; the implementation here just adds a
	 * little more logic.
	 */
	@Override
	public void deliverResult(List<AppsInfo> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<AppsInfo> oldApps = apps;
		mlistAppInfos = apps;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(apps);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (mlistAppInfos != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mlistAppInfos);
		}

		// Start watching for changes in the app data.
		if (mPackageObserver == null) {
			mPackageObserver = new PackageIntentReceiver(this);
		}

		// Has something interesting in the configuration changed since we
		// last built the app list?
		// boolean configChange =
		// mLastConfig.applyNewConfig(getContext().getResources());

		if (takeContentChanged() || mApps == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(List<AppsInfo> apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (mApps != null) {
			onReleaseResources(mlistAppInfos);
			mApps = null;
		}

		// Stop monitoring for changes.
		if (mPackageObserver != null) {
			getContext().unregisterReceiver(mPackageObserver);
			mPackageObserver = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated with an
	 * actively loaded data set.
	 */
	protected void onReleaseResources(List<AppsInfo> apps) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}
}
