package com.mirageTeam.weather.bean;

import java.io.Serializable;

public class WeatherInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -291147285869911130L;

	private String cityName;

	private String temp;

	private String weatherData;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWeatherData() {
		return weatherData;
	}

	public void setWeatherData(String weatherData) {
		this.weatherData = weatherData;
	}

}
