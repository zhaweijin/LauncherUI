package com.mirageTeam.api.imp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.mirageTeam.common.AllApplicationField;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.common.GetWeather;
import com.mirageTeam.common.NetUtil;
import com.mirageTeam.db.CategoryDBHelp;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.jsonHelper.UdownApi;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.store.model.Store;
import com.mirageTeam.store.model.StoreBuilder;
import com.mirageTeam.store.model.TaskInfo;
import com.mirageTeam.weather.bean.ProvinceBuilder;
import com.mirageTeam.weather.bean.ProvincesData;
import com.mirageTeam.weather.bean.Weather;
import com.mirageTeam.weather.bean.WeatherBuilder;
import com.mirageTeam.weather.bean.WeatherInfo;

public class UdownApiImp implements UdownApi {

	@Override
	public ArrayList<Store> getStoreLIst(HashMap<String, String> params) throws IOException, JSONException {
		// TODO Auto-generated method stub

		// use async http return jsonObject

		StoreBuilder builder = new StoreBuilder();

		return builder.builder(new JSONObject());
	}

	@Override
	public ProvincesData getProvins(InputStream inputStream) throws IOException, JSONException {
		// TODO Auto-generated method stub
		String json = CommonUtils.readTextFile(inputStream);
		ProvinceBuilder builder = new ProvinceBuilder();

		return builder.builder(new JSONObject(json));
	}

	@Override
	public Weather getWeatherByCityCode(int code) throws IOException, JSONException {
		// TODO Auto-generated method stub
		WeatherBuilder builder = new WeatherBuilder();
		JSONObject jsonObject = NetUtil.getResponseForGet(String.format(AllApplicationField.REQUEST_WEATHER_URL, code));

		return builder.builder(jsonObject);
	}

	@Override
	public Object saveCategory(Context context, CategoryModel model) throws IOException, JSONException {
		// TODO Auto-generated method stub
		CategoryDBHelp dbhelp = new CategoryDBHelp(context);
		return dbhelp.insert(model);
	}

	@Override
	public ArrayList<CategoryModel> getGategoryModesArray(Context context, int type) throws IOException, JSONException {
		// TODO Auto-generated method stub
		CategoryDBHelp dbhelp = new CategoryDBHelp(context);
		return dbhelp.getCategoryByType(type);
	}

	@Override
	public CategoryModel getDefaultAppByType(Context context, int type) throws IOException, JSONException {
		// TODO Auto-generated method stub
		CategoryDBHelp dbhelp = new CategoryDBHelp(context);
		return dbhelp.getDefaultAppByType(type);
	}

	@Override
	public boolean deleteCategoryByPackageName(Context context, String packageName) throws IOException, JSONException {
		// TODO Auto-generated method stub
		CategoryDBHelp dbhelp = new CategoryDBHelp(context);
		return dbhelp.delete(packageName);
	}

	@Override
	public boolean updateCategoryByActivityName(Context context, CategoryModel model, String activityName, int type) throws IOException, JSONException {
		// TODO Auto-generated method stub
		CategoryDBHelp dbhelp = new CategoryDBHelp(context);

		return dbhelp.update(model, activityName, String.valueOf(type));
	}

	@Override
	public ArrayList<TaskInfo> loadApplicationInfo(Context context) {
		// TODO Auto-generated method stub
		// List<ActivityManager.RunningTaskInfo> mRunningTasks =
		// ((ActivityManager) context
		// .getSystemService(Context.ACTIVITY_SERVICE))
		// .getRunningTasks(30);
		//
		// PackageManager pm =
		// context.getApplicationContext().getPackageManager();
		//
		// List<ApplicationInfo> allAppList = pm
		// .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		//
		// ArrayList<ApplicationInfo> applicationInfos =new
		// ArrayList<ApplicationInfo>();
		//
		// System.out.println("=================mRunningTasks========="+mRunningTasks.size());
		// for (ActivityManager.RunningTaskInfo amTask : mRunningTasks) {
		// for(ApplicationInfo appinfo : allAppList){
		// if (amTask.baseActivity.getPackageName().equals(appinfo.packageName)
		// &&
		// !amTask.baseActivity.getPackageName().equals(context.getPackageName()))
		// {
		// applicationInfos.add(appinfo);
		//
		// continue;
		// }
		// }
		// }

		return CommonUtils.getTaskList(context);
	}

	@Override
	public WeatherInfo getWeatherInfo(Context context, String city) throws IOException {
		// TODO Auto-generated method stub
		WeatherInfo info = new WeatherInfo();
		GetWeather weather = new GetWeather(context);
		weather.getweather(city, "0");
		info.setCityName(city);
		info.setTemp(weather.getLowTem() + context.getString(R.string.str_tem) + "~" + weather.getHighTem() + context.getString(R.string.str_tem));
		info.setWeatherData(weather.getWeatherStatus());
		return info;
	}

	@Override
	public String getCityName(Context context) throws IOException {
		// TODO Auto-generated method stub
		String str_province = context.getString(R.string.str_province);
		String str_city = context.getString(R.string.str_city);
		String str_area = context.getString(R.string.str_area);
		String str_come_from = context.getString(R.string.str_come_from);
		String str_tem = context.getString(R.string.str_tem);
		String str_dunhao = context.getString(R.string.str_dunhao);
		GetWeather mWeather = new GetWeather(context);
		String city = mWeather.getCity(str_province, str_city, str_area, str_come_from);

		return city;
	}

}
