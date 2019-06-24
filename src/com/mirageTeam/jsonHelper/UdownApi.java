package com.mirageTeam.jsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.store.model.Store;
import com.mirageTeam.store.model.TaskInfo;
import com.mirageTeam.weather.bean.ProvincesData;
import com.mirageTeam.weather.bean.Weather;
import com.mirageTeam.weather.bean.WeatherInfo;

public interface UdownApi {

	ArrayList<Store> getStoreLIst(HashMap<String, String> params)
			throws IOException, JSONException;

	ProvincesData getProvins(InputStream inputStream) throws IOException,
			JSONException;

	Weather getWeatherByCityCode(int code) throws IOException, JSONException;

	Object saveCategory(Context context, CategoryModel model)
			throws IOException, JSONException;

	ArrayList<CategoryModel> getGategoryModesArray(Context context, int type)
			throws IOException, JSONException;
	
	CategoryModel getDefaultAppByType(Context context, int type)
			throws IOException, JSONException;
	
	boolean deleteCategoryByPackageName(Context context, String packageName)
			throws IOException, JSONException;

	boolean updateCategoryByActivityName(Context context, CategoryModel model,
			String activityName, int type) throws IOException, JSONException;

	ArrayList<TaskInfo> loadApplicationInfo(Context context);
	
	
	WeatherInfo getWeatherInfo(Context context,String city) throws IOException;
	
	String getCityName(Context context)throws IOException;
}
