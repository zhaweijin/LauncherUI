package com.mirageTeam.Store.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.mirageTeam.common.AppsInfo;
import com.mirageTeam.launcherui.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppsAdapter extends BaseAdapter {

	private Context mContext;
	private List<AppsInfo> appInfos;
	private ViewHolder mViewHolder;

	public AppsAdapter(Context context, List<AppsInfo> list) {
		mContext = context;
		appInfos = new ArrayList<AppsInfo>();
		appInfos = list;
	}

	public AppsAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<AppsInfo> data) {
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
			mViewHolder.appName.setTextColor(Color.WHITE);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.appIcon.setBackgroundDrawable(appInfos.get(position).getIconDrawable());
		mViewHolder.appName.setText(appInfos.get(position).getAppLabel());

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
