package com.mirageTeam.widget;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
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

public class CityView extends FrameLayout implements OnClickListener{
	
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

	public CityView(Context context) {
		this(context, null);
	}
	public CityView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ColorDrawable dw = new ColorDrawable(0xb0000000);

		setBackgroundDrawable(dw);
		
		LayoutInflater.from(context).inflate(R.layout.layout_province_city_choose, this);
		mController = new WeatherController(mHandler);
		location_text=(TextView)findViewById(R.id.location_text);
				cityGrid = (GridView) findViewById(R.id.cityChoose);
				 

				if (LauncherUIApplication.getInstance().getProvinceData() == null) {
					try {
						mController.getProvins(context.getAssets().open("weather.json"));
				mController.getCity(context);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					initGridData();
				}
				
				cityGrid.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if(!hasFocus){
							setVisibility(View.GONE);
						}
					}
				});
	}
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		super.setVisibility(visibility);
		switch (visibility) {
		case View.GONE:
			startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_bottom_out));
			break;
		case View.VISIBLE:
			cityGrid.requestFocus();
			startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_bottom_in));
			break;

		default:
			break;
		}
	}
	
	void initGridData() {
		adapter = new CityAdapter(getContext(), LauncherUIApplication.getInstance().getProvinceData());
		adapter.setPosition(-1);
		cityGrid.setAdapter(adapter);

		cityGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (adapter.getPosition() == -1) {

					adapter.setPosition(arg2);
				} else {
					City city = (City) LauncherUIApplication.getInstance().getProvinceData().getProvinceArray().get(adapter.getPosition()).getCityArray()
							.get(arg2);
					if (onCityChoose != null) {
						onCityChoose.onCityChoose(city.getCityName(), city.getCityCode());
						CommonUtils.saveIsAutoChooseWeather(getContext(), false);
						 setVisibility(View.GONE);
					}

				}
			}
		});
	}

	public void cityChooseReturn() {
		adapter.setPosition(-1);
	}
 

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WeatherBusiness.LOAD_PROVINCE_LIST_SUCCESS:
				LauncherUIApplication.getInstance().setProvinceData((ProvincesData) msg.obj);
				initGridData();
				break;
	case WeatherBusiness.LOAD_SINA_CITY_NAME:
				location_text.setText((String)msg.obj);
				location_text.setOnClickListener(CityView.this);
				if (onCityChoose != null) {
					onCityChoose.onCityChoose((String)msg.obj, -1);
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
			if(onCityChoose!=null)
				onCityChoose.onCityChoose(location_text.getText().toString().trim(), -1);
				setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	public boolean isCanBack(){
		return adapter.getPosition()!=-1;
	}

}
