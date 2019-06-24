package com.mirageTeam.weather.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Provices implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 808691328778976677L;
	
	
	private ArrayList<City> cityArray;
	
	private String proviceName;

	public ArrayList<City> getCityArray() {
		return cityArray;
	}

	public void setCityArray(ArrayList<City> cityArray) {
		this.cityArray = cityArray;
	}

	public String getProviceName() {
		return proviceName;
	}

	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "provinceName:"+proviceName;
	}
	

}
