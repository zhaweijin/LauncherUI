package com.mirageTeam.controller;

import java.io.InputStream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mirageTeam.business.WeatherBusiness;
import com.mirageTeam.jsonHelper.DisplayCallback;

public class WeatherController {

private Handler mHandler;
	
	private WeatherBusiness business=new WeatherBusiness();
	
	public WeatherController(Handler handler){
		this.mHandler=handler;
	}
	
	private DisplayCallback callback=new DisplayCallback() {
		
		@Override
		public void displayResult(int key, Object... param) {
			// TODO Auto-generated method stub
			switch (key) {
			case WeatherBusiness.LOAD_PROVINCE_LIST_SUCCESS:
			case WeatherBusiness.LOAD_PROVINCE_LIST_FAIL:
			case WeatherBusiness.LOAD_PROVINCE_LIST_TIMEOUT:
			case WeatherBusiness.LOAD_WEATHER_LIST_SUCCESS:
			case WeatherBusiness.LOAD_WEATHER_LIST_FAIL:
			case WeatherBusiness.LOAD_WEATHER_LIST_TIMEOUT:
			case WeatherBusiness.LOAD_SINA_WEATHER_ERROR:
			case WeatherBusiness.LOAD_SINA_WEATHER_SUCCESS:
			case WeatherBusiness.LOAD_SINA_CITY_NAME:
				handlerCallback(key,param[0]);
				break;

			default:
				break;
			}
		}
	};
	
	private void handlerCallback(int key,Object result){
		Message msg =mHandler.obtainMessage();
		msg.what=key;
		msg.obj=result;
		msg.sendToTarget();
	}
	
	/**
	 * 获取城市列表
	 * @param request
	 */
	public void getProvins(InputStream inputStream){
		business.getProvins(callback, inputStream);
	}
	
	/**
	 * 根据城市代码获取天气
	 * @param code
	 */
	public void getWeatherByCityCode(int code){
		business.getWeatherByCityCode(callback, code);
	}
	
	/**
	 * 根据城市代获取天气
	 * @param code
	 */
	public void getWeatherByCity(Context context,String city){
		business.getWeatherByCity(callback, context, city);
	}
	
	/**
	 * 获取城市 
	 * @param code
	 */
	public void getCity(Context context){
		business.getCity(callback, context);
	} 
}
