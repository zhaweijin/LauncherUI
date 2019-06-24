package com.mirageTeam.widget;

import com.mirageTeam.business.WeatherBusiness;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.common.NetUtil;
import com.mirageTeam.controller.WeatherController;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.weather.bean.Weather;
import com.mirageTeam.weather.bean.WeatherInfo;

import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherView extends LinearLayout {

	TextView cityText, tempText, weatherText;
	ImageView weatheryPic;

	Weather weather;

	private WeatherController mWeatherController;

	public WeatherView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public WeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.layout_weather_view, this);

		cityText = (TextView) findViewById(R.id.weather_city);

		tempText = (TextView) findViewById(R.id.weather_temp);

		weatherText = (TextView) findViewById(R.id.weather);

		weatheryPic = (ImageView) findViewById(R.id.weather_pic);

		mWeatherController = new WeatherController(mHandler);

		weather = CommonUtils.getWheater(context);
		initUi(weather);
		if (CommonUtils.checkNetwork(context)) {
//			mWeatherController.getWeatherByCityCode(weather.getCityID());
			mWeatherController.getWeatherByCity(getContext(), weather.getCityName());
		} else {
			initUi(weather);
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WeatherBusiness.LOAD_WEATHER_LIST_SUCCESS:
				Weather weather = (Weather) msg.obj;

				initUi(weather);
				break;
			case WeatherBusiness.LOAD_SINA_WEATHER_SUCCESS:
				WeatherInfo info=(WeatherInfo)msg.obj;
			 
			Weather myWeather =new Weather();
			myWeather.setCityName(info.getCityName());
			myWeather.setTemp1(info.getTemp());
			myWeather.setWeather1(info.getWeatherData());
			initUi(myWeather);
				break;

			default:
				break;
			}
		}
	};

	public void initUi(Weather weather) {
		if(null!=weather&&weather.getWeather1()!=null){
		CommonUtils.saveWheatherInfo(getContext(), weather);
		}else{
			weather=CommonUtils.getWheater(getContext());
		}
		cityText.setText(weather.getCityName());
		tempText.setText(weather.getTemp1());
		weatherText.setText(weather.getWeather1());
		Object obj = CommonUtils.weatherPicGenerate(getContext()).get(weather.getWeather1());
		int weatherPic = obj != null ? Integer.parseInt(obj.toString()) : R.drawable.moren;
		weatheryPic.setImageResource(weatherPic);
	}

//	public void reqeustWeatherById(int cityId) {
//		mWeatherController.getWeatherByCityCode(cityId);
//	}

	public void requestWeatherByCity(String city){
		mWeatherController.getWeatherByCity(getContext(), city);
	}
}
