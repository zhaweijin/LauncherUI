package com.mirageTeam.service;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;

import android.content.Context;

import com.mirageTeam.launcherui.LauncherUIApplication;
import com.mirageTeam.weather.bean.ProvincesData;
import com.mirageTeam.weather.bean.Weather;
import com.mirageTeam.weather.bean.WeatherInfo;

public class WeatherService {

private WeatherService(){
		
	}
	
	private static class SingleTonHolder{
		private static WeatherService INSTANCE=new WeatherService();
	}
	
	public static WeatherService getInstance(){
		return SingleTonHolder.INSTANCE;
	}
	
	public  ProvincesData getProvins(InputStream inputStream)throws IOException,JSONException{
		
		 
		return LauncherUIApplication.getInstance().getApiServer().getProvins(inputStream);
	}
	
	public Weather getWeatherByCityCode(int code)throws IOException,JSONException{
		
		 
		return LauncherUIApplication.getInstance().getApiServer().getWeatherByCityCode(code);
	}
	
	public WeatherInfo getWeatherinfoByCity(Context context,String city) throws IOException{
		return LauncherUIApplication.getInstance().getApiServer().getWeatherInfo(context, city);
	}
	
	
	public String getCityName(Context context)throws IOException{
		return LauncherUIApplication.getInstance().getApiServer().getCityName(context);
	}
}
