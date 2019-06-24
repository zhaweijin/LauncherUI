package com.mirageTeam.weather.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mirageTeam.jsonHelper.JSONBuilder;

public class ProvinceBuilder extends JSONBuilder<ProvincesData>{

	@Override
	public ProvincesData builder(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		
		ProvincesData allData=new ProvincesData();
		ArrayList<Provices> provinceArraylist=new ArrayList<Provices>();
		JSONArray array=jsonObject.getJSONArray("cityJson");

		for(int i=0;i<array.length();i++){
			Provices province=new Provices();
			 JSONObject object=array.optJSONObject(i);
			province.setProviceName(object.optString("provice"));
			JSONArray citys=object.optJSONArray("city");
			ArrayList<City> cityArray=new ArrayList<City>();
			City defaultCity=new City(); 
			defaultCity.setCityCode(0);
			cityArray.add(defaultCity);
			for(int j=0;j<citys.length();j++){
				
				City city=new City();
				JSONObject cityObject=citys.optJSONObject(j);
				city.setCityName(cityObject.optString("cityName"));
				city.setCityCode(cityObject.optInt("cityCode"));
				cityArray.add(city);
				//System.out.println("============="+city.toString());
			}
			province.setCityArray(cityArray);
			provinceArraylist.add(province);

			//System.out.println("============="+province.toString());
		}
		allData.setProvinceArray(provinceArraylist);
	//	System.err.println("================="+allData.toString());
		return allData;
	}

}
