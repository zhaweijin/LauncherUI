package com.mirageteam.launcher.setting;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;



import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class WiFiPointAdapter extends BaseAdapter {


	private Context context;
    private ArrayList<WifiPoint> wifiPoints;

    private WifiManager wifiManager;

	public WiFiPointAdapter(Context context,ArrayList<WifiPoint> wifiPoints) {
		this.context = context;
        this.wifiPoints = wifiPoints;
        
//        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
//        wifiInfo.
//        Log.d("wifiInfo", wifiInfo.toString());  
//        Log.d("SSID",wifiInfo.getSSID());  
	}

	public int getCount() {
		return wifiPoints.size();
	}

	public Object getItem(int position) {
		return wifiPoints.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		
		final ViewHolder viewHolder;

	    try {
			if (view == null) {
				viewHolder = new ViewHolder();
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.wifi_item, null);

				viewHolder.ssid = (TextView) view.findViewById(R.id.ssid);
				viewHolder.state = (TextView) view.findViewById(R.id.state);
				viewHolder.level = (ImageView) view.findViewById(R.id.signal);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			viewHolder.ssid.setText(wifiPoints.get(position).getSsid());
			viewHolder.state.setText(wifiPoints.get(position).getState());
			

			int security = wifiPoints.get(position).getSecurity();
			int level = wifiPoints.get(position).getLevel();
			Log.v("level", ">>>>>>>>>>>"+level);
//			
//			
			Log.v("state", ">>>>>>>>>>>"+wifiPoints.get(position).getState());
			
			if(wifiPoints.get(position).getState()!=null && wifiPoints.get(position).getState().equals(WifiPoint.CONNECTED)){
				viewHolder.state.setText(context.getResources().getString(R.string.wifi_connected));
			}else if(wifiPoints.get(position).getState()!=null && wifiPoints.get(position).getState().equals(WifiPoint.CONNECTING)){
				viewHolder.state.setText(context.getResources().getString(R.string.wifi_connecting));
			}

			if(security!=WifiPoint.SECURITY_NONE){
				if(level==0){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_lock_signal_1);
				}else if(level==1){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_lock_signal_2);
				}else if(level==2){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_lock_signal_3);
				}else if(level==3){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_lock_signal_4);
				}else {
					viewHolder.level.setBackgroundResource(android.R.color.transparent);
				}
			}else {
				if(level==0){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_signal_1);
				}else if(level==1){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_signal_2);
				}else if(level==2){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_signal_3);
				}else if(level==3){
					viewHolder.level.setBackgroundResource(R.drawable.ic_wifi_signal_4);
				}else {
					viewHolder.level.setBackgroundResource(android.R.color.transparent);
				}
			}


			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return view;
		
	}

	public class ViewHolder {
		ImageView level;
		TextView ssid;
		TextView state;
	}

	
	 

}
