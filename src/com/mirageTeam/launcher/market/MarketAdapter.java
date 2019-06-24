package com.mirageteam.launcher.market;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class MarketAdapter extends BaseAdapter {

	private DataSet dataSet;
	private Context context;
	private Activity activity;


	private SharedPreferences settingPreferences;
	private ArrayList<String> demoData;

	public MarketAdapter(Context context,DataSet dataSet) {
		this.context = context;
		this.dataSet = dataSet;
		
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public MarketAdapter(Context context,ArrayList<String> demodata) {
		this.context = context;
		this.demoData = demodata;
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	

	public int getCount() {
		return dataSet.getMarketDatas().size();
//		return demoData.size();
	}

	public Object getItem(int position) {
		return dataSet.getMarketDatas().get(position);
//		return demoData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (view == null) {
				viewHolder = new ViewHolder();
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.market_grid_row, null);

				viewHolder.title = (TextView) view.findViewById(R.id.title);
				viewHolder.message = (TextView)view.findViewById(R.id.message);
				viewHolder.icon_image = (ImageView) view.findViewById(R.id.app_icon);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			
			viewHolder.title.setText(dataSet.getMarketDatas().get(position).getTitle());
			viewHolder.message.setText(dataSet.getMarketDatas().get(position).getMessage());
		try {
			viewHolder.icon_image.setImageResource(R.drawable.ic_launcher);
			ImagePageTask pt = new ImagePageTask();
			ImageData data = new ImageData();

			data.setUrlString(dataSet.getMarketDatas().get(position).getImageUrl().replaceAll(" ", "%20"));
			data.setImageView(viewHolder.icon_image);
			pt.execute(data);

//			Util.print("tag2", position + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
			

		

		return view;

	}

	public class ViewHolder {
		ImageView icon_image;
		   TextView title;
		TextView message;
	}

	

}