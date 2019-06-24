package com.mirageTeam.widget;

import java.util.List;

import com.mirageTeam.Adapter.AppsAdapter;
import com.mirageTeam.launcherui.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

public class AppListMenu extends PopupWindow {

	private View mMenuView;
	protected Context mContext;
	
	private GridView mAppView;

	public AppListMenu(Context context) {
		super(context);

		mContext = context;

		mMenuView = LayoutInflater.from(context).inflate(
				R.layout.layout_of_app_list, null);

		setContentView(mMenuView);

		setWidth(LayoutParams.FILL_PARENT);

		setHeight(LayoutParams.FILL_PARENT);

		setFocusable(true);

		setAnimationStyle(R.style.AnimBottom);

		ColorDrawable dw = new ColorDrawable(0xee000000);

//		setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.applist_background));
		setBackgroundDrawable(dw);
		
		mAppView=(GridView)getContentView().findViewById(R.id.applistview);

		new LoadAppsTask().execute();
	}

	public void show() {
		showAtLocation(((Activity) mContext).getWindow().getDecorView(),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	
	public class LoadAppsTask extends AsyncTask<Void, Void, List<ResolveInfo>> {

		@Override
		protected List<ResolveInfo> doInBackground(Void... params) {
			List<ResolveInfo> resolveInfos = null;
			Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
			mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveInfos = mContext.getPackageManager().queryIntentActivities(mIntent, 0);

			return resolveInfos;
		}

		@Override
		protected void onPostExecute(List<ResolveInfo> list) {
			// TODO Auto-generated method stub
			super.onPostExecute(list); 
			mAppView.setAdapter(new AppsAdapter(mContext, list,  mContext.getPackageManager()));
		}
	}
	
	public void reload(){
		new LoadAppsTask().execute();
	}
	
	public void setOnItemClikListener(OnItemClickListener onItemClick){
		mAppView.setOnItemClickListener(onItemClick);
	}
}
