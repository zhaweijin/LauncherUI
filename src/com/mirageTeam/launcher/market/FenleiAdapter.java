package com.mirageteam.launcher.market;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FenleiAdapter extends BaseAdapter {

 
	private Context context;
 
	private ArrayList<FenleiData> fenleiDatas;

	 
	
	public FenleiAdapter(Context context,ArrayList<FenleiData> fenleiDatas) {
		this.context = context;
		this.fenleiDatas = fenleiDatas;
	}
	

	public int getCount() {
		return fenleiDatas.size();
	}

	public Object getItem(int position) {
		return fenleiDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (view == null) {
				viewHolder = new ViewHolder();
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.fenlei_listview_row, null);

				viewHolder.name = (TextView) view.findViewById(R.id.name);
 

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			
		 viewHolder.name.setText(fenleiDatas.get(position).getName());

		 if (position == selectItem) {   
			 Log.v("aaaa", "aaaa");
             view.setBackgroundResource(R.drawable.button_selected);
         }    
         else {   
        	 view.setBackgroundResource(R.drawable.button_noselected);
         }      

		return view;

	}

	public class ViewHolder {
		   TextView name;
	}

	public  void setSelectItem(int selectItem) {   
        this.selectItem = selectItem;   
    }   
    private int  selectItem=-1;   

}