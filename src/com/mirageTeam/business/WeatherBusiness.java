package com.mirageTeam.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;

import com.mirageTeam.jsonHelper.DisplayCallback;
import com.mirageTeam.service.StoreService;
import com.mirageTeam.service.WeatherService;
import com.mirageTeam.store.model.Store;
import com.mirageTeam.weather.bean.ProvinceBuilder;
import com.mirageTeam.weather.bean.ProvincesData;
import com.mirageTeam.weather.bean.Weather;
import com.mirageTeam.weather.bean.WeatherInfo;

public class WeatherBusiness {

	/**
	 * 加载城市列表成功状态
	 */
	public static final int LOAD_PROVINCE_LIST_SUCCESS = 1;
	/**
	 * 加载城市列表失败
	 */
	public static final int LOAD_PROVINCE_LIST_FAIL = 2;

	/**
	 * 加载城市列表超时
	 */
	public static final int LOAD_PROVINCE_LIST_TIMEOUT = 3;

	/**
	 * 加载天气数据成功状态
	 */
	public static final int LOAD_WEATHER_LIST_SUCCESS = 4;
	/**
	 * 加载天气数据失败
	 */
	public static final int LOAD_WEATHER_LIST_FAIL = 5;

	/**
	 * 加载天气数据超时
	 */
	public static final int LOAD_WEATHER_LIST_TIMEOUT = 6;

	/**
	 * 加载新浪天气成功
	 */
	public static final int LOAD_SINA_WEATHER_SUCCESS = 7;

	/**
	 * 加载新浪天气错误
	 */
	public static final int LOAD_SINA_WEATHER_ERROR = 8;

	/**
	 * 加载新浪天气超时
	 */
	public static final int LOAD_SINA_WEATHER_TIMEOUT = 9;

	public static final int LOAD_SINA_CITY_NAME = 10;

	public void getProvins(final DisplayCallback callback,
			final InputStream inputStream) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					ProvincesData province = WeatherService.getInstance()
							.getProvins(inputStream);
					key = LOAD_PROVINCE_LIST_SUCCESS;
					callback.displayResult(key, province);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					key = LOAD_PROVINCE_LIST_FAIL;
					callback.displayResult(key, "失败");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					key = LOAD_PROVINCE_LIST_TIMEOUT;
					callback.displayResult(key, "超时");
				}

			}
		}).start();
	}
	
	

	/**
	 * 获取天气数据
	 * @param callback
	 * @param inputStream
	 */
	public void getWeatherByCityCode(final DisplayCallback callback,
			final int code) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					Weather weather = WeatherService.getInstance()
							.getWeatherByCityCode(code);
					key = LOAD_WEATHER_LIST_SUCCESS;
					callback.displayResult(key, weather);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					key = LOAD_WEATHER_LIST_FAIL;
					callback.displayResult(key, "失败");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					key = LOAD_WEATHER_LIST_TIMEOUT;
					callback.displayResult(key, "超时");
				}

			}
		}).start();
	}

	/**
	 * 根据城市名获取天气
	 * 
	 * @param callback
	 * @param inputStream
	 */
	public void getWeatherByCity(final DisplayCallback callback,
			final Context context, final String city) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					WeatherInfo weather = WeatherService.getInstance()
							.getWeatherinfoByCity(context, city);
					key = LOAD_SINA_WEATHER_SUCCESS;
					callback.displayResult(key, weather);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					key = LOAD_SINA_WEATHER_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void getCity(final DisplayCallback callback, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					String city = WeatherService.getInstance().getCityName(
							context)==null?"":WeatherService.getInstance().getCityName(
									context);
					key = LOAD_SINA_CITY_NAME;
					callback.displayResult(key, city);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					key = LOAD_PROVINCE_LIST_FAIL;
					callback.displayResult(key, "失败");
				} catch (Exception e) {
					// TODO Auto-generated catch block

				}

			}
		}).start();
	}
}
