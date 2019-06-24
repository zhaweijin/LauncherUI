package com.mirageTeam.weather.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProvincesData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -933650626841824381L;

	private ArrayList<Provices> provinceArray;

	public ArrayList<Provices> getProvinceArray() {
		return provinceArray;
	}

	public void setProvinceArray(ArrayList<Provices> provinceArray) {
		this.provinceArray = provinceArray;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "proinceSize:"+getProvinceArray().size();
	}

}
