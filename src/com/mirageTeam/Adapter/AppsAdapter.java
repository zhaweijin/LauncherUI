package com.mirageTeam.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirageTeam.launcherui.R;

public class AppsAdapter extends BaseAdapter {

	private Context mContext;
	private PackageManager pm;
	private List<ResolveInfo> appInfos;
	private ViewHolder mViewHolder;

	public AppsAdapter(Context context, List<ResolveInfo> list, PackageManager mPackageManager) {
		mContext = context;
		pm = mPackageManager;
		appInfos = new ArrayList<ResolveInfo>();
		appInfos = list;
	}

	public AppsAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<ResolveInfo> data) {
		if (data != null) {
			appInfos = data;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.apps_item, parent, false);
			mViewHolder = new ViewHolder();
			mViewHolder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);

			mViewHolder.appIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
			mViewHolder.appName = (TextView) convertView.findViewById(R.id.app_name);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.appIcon.setBackgroundDrawable(appInfos.get(position).loadIcon(pm));
		mViewHolder.appName.setText(appInfos.get(position).loadLabel(pm));
		 

		return convertView;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appName;
	}

	public final int getCount() {
		return appInfos.size();
	}

	public final Object getItem(int position) {
		return appInfos.get(position);
	}

	public final long getItemId(int position) {
		return position;
	}
}
