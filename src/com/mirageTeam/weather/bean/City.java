package com.mirageTeam.weather.bean;

import java.io.Serializable;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -228387810173853455L;
	
	private String cityName;
	
	private int cityCode;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "cityCode:"+cityCode+",cityName:"+cityName;
	}

}
