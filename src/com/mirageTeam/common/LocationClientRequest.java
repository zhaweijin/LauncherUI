package com.mirageTeam.common;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationClientRequest implements BDLocationListener {

	private LocationClient locationClient;

	public LocationClientRequest(Context mContext) {
		locationClient = new LocationClient(mContext);
		locationClient.registerLocationListener(this);
		setLocationOption();
		
	}
	
	public void start(){
		locationClient.start(); 
	}
	
	public void stop(){
		locationClient.stop(); 
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub 
		if (arg0.getLocType() == BDLocation.TypeNetWorkLocation){
			System.out.println("============BDLocation========"+arg0==null+"======="+arg0.getCity()+"==="+arg0.getAddrStr());

		}
//		locationClient.stop();
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub
		System.out.println("===========BDLocation======================"+arg0.getCity()+"==="+arg0.getCityCode());
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false); // 打开gps
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");

		option.setScanSpan(3000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位

		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先

		option.setPoiNumber(10);
		option.disableCache(true);
		locationClient.setLocOption(option);
	}
}
