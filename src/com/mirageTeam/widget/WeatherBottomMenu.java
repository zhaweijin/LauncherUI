package com.mirageTeam.widget;

import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.mirageTeam.Adapter.CityAdapter;
import com.mirageTeam.business.WeatherBusiness;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.controller.WeatherController;
import com.mirageTeam.launcherui.LauncherUIApplication;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.weather.bean.City;
import com.mirageTeam.weather.bean.ProvincesData;

public class WeatherBottomMenu extends BaseBottomMenu implements
		OnClickListener {

	private GridView cityGrid;
	private CityAdapter adapter;
	private WeatherController mController;

	private TextView location_text;

	public interface onCityChooseClick {
		public void onCityChoose(String cityName, int CityCode);
	}

	public onCityChooseClick onCityChoose;

	public void setOnCityChooseListernerClick(onCityChooseClick onChooseClick) {
		this.onCityChoose = onChooseClick;
	}

	public WeatherBottomMenu(Context context) {
		super(context);
		mController = new WeatherController(mHandler);
		location_text = (TextView) getContentView().findViewById(
				R.id.location_text);
		cityGrid = (GridView) getContentView().findViewById(R.id.cityChoose);

		if (LauncherUIApplication.getInstance().getProvinceData() == null) {
			try {
				mController.getProvins(mContext.getAssets()
						.open("weather.json"));
				mController.getCity(mContext);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			initGridData();
		}

		getContentView().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	void initGridData() {
		adapter = new CityAdapter(mContext, LauncherUIApplication.getInstance()
				.getProvinceData());
		adapter.setPosition(-1);
		cityGrid.setAdapter(adapter);

		cityGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (adapter.getPosition() == -1) {

					adapter.setPosition(arg2);
				} else {
					City city = (City) LauncherUIApplication.getInstance()
							.getProvinceData().getProvinceArray()
							.get(adapter.getPosition()).getCityArray()
							.get(arg2);
					if (city.getCityCode() == 0) {
						adapter.setPosition(-1);
						return;
					}

					if (onCityChoose != null) {
						onCityChoose.onCityChoose(city.getCityName(),
								city.getCityCode());
						CommonUtils.saveIsAutoChooseWeather(mContext, false);
						dismiss();
					}

				}
			}
		});

	}

	public void cityChooseReturn() {
		adapter.setPosition(-1);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		cityGrid.requestFocus();
	}

	@Override
	View baseBottomMenuContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(mContext).inflate(
				R.layout.layout_province_city_choose, null);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WeatherBusiness.LOAD_PROVINCE_LIST_SUCCESS:
				LauncherUIApplication.getInstance().setProvinceData(
						(ProvincesData) msg.obj);
				initGridData();
				break;
			case WeatherBusiness.LOAD_SINA_CITY_NAME:
				if(!msg.obj.toString().equals("")){
				location_text.setText((String) msg.obj);
				location_text.setOnClickListener(WeatherBottomMenu.this);
				if (onCityChoose != null&&CommonUtils.isAutoChooseWeather(mContext)) {
					 
					onCityChoose.onCityChoose((String) msg.obj, -1);
				}
				}else{
					location_text.setText("无");
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.location_text:
			if (onCityChoose != null)
				onCityChoose.onCityChoose(location_text.getText().toString()
						.trim(), -1);
			dismiss();
			break;

		default:
			break;
		}
	}

	public boolean isCanBack() {
		return adapter.getPosition() != -1;
	}

}
