package com.mirageTeam.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.weather.bean.City;
import com.mirageTeam.weather.bean.Provices;
import com.mirageTeam.weather.bean.ProvincesData;

public class CityAdapter extends BaseAdapter{

	private Context mContext;
	private ProvincesData province;
	
	private int dataPosition=-1;
	
	public  CityAdapter(Context mContext,ProvincesData data){
		this.mContext=mContext;
		this.province=data;
	}
	
	public void setPosition(int index){
		dataPosition=index;
		notifyDataSetChanged();
	}
	
	public int getPosition(){
		return dataPosition;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataPosition==-1?province.getProvinceArray().size():province.getProvinceArray().get(dataPosition).getCityArray().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
//		return position==-1?province.getProvinceArray().get(position):province.getProvinceArray().get(position).getCityArray().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	 
		TextView tv=	(TextView)LayoutInflater.from(mContext).inflate(R.layout.item_of_city, null);
		
		 if(dataPosition ==-1){
			    Provices pro =(Provices) province.getProvinceArray().get(position);
			    tv.setText(pro.getProviceName());
		 }else{
			  
			 City city=(City)province.getProvinceArray().get(dataPosition).getCityArray().get(position);
			 if(city.getCityCode()==0){
				 tv.setText(mContext.getString(R.string.backtoprovince));
			 }else
			 tv.setText(city.getCityName());
		 }
		
		
		
		return tv;
	}

}
