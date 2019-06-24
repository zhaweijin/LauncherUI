package com.mirageTeam.weather.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.mirageTeam.jsonHelper.JSONBuilder;

public class WeatherBuilder extends JSONBuilder<Weather>{

	@Override
	public Weather builder(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		Weather weather=new Weather(); 
		JSONObject weatherInfo=jsonObject.optJSONObject("weatherinfo");
		 weather.setCityName(weatherInfo.optString("city"));
		 weather.setDate_y(weatherInfo.optString("date_y"));
		 weather.setWeek(weatherInfo.optString("week"));
		 weather.setCityID(weatherInfo.optInt("cityid"));
		 weather.setTemp1(weatherInfo.optString("temp1"));
		 weather.setTemp2(weatherInfo.optString("temp2"));
		 weather.setTemp3(weatherInfo.optString("temp3"));
		 weather.setTemp4(weatherInfo.optString("temp4"));
		 weather.setTemp5(weatherInfo.optString("temp5"));
		 weather.setTemp6(weatherInfo.optString("temp6"));
		 
		 
		 weather.setWeather1(weatherInfo.optString("weather1"));
		 weather.setWeather2(weatherInfo.optString("weather2"));
		 weather.setWeather3(weatherInfo.optString("weather3"));
		 weather.setWeather4(weatherInfo.optString("weather4"));
		 weather.setWeather5(weatherInfo.optString("weather5"));
		 weather.setWeather6(weatherInfo.optString("weather6"));
		 
		 
		 
		 
		 
		
		return weather;
	}

}
