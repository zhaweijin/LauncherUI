package com.mirageTeam.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.mirageTeam.launcherui.R;

public class OptionBottomMenu extends BaseBottomMenu implements OnClickListener {

	View btn_wallpaper, btn_weather, btn_network, btn_settings;

	public interface OptionBottomMenuListener {
		void settingWallpaper();

		void settingWeather();
	}

	public OptionBottomMenuListener listener;

	public void setOptionBottomMenuListener(OptionBottomMenuListener optionBottomMenuListener) {
		this.listener = optionBottomMenuListener;
	}

	public OptionBottomMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		View rootView = getContentView();
		btn_wallpaper = rootView.findViewById(R.id.layout_wallpaper);
		btn_wallpaper.setOnClickListener(this);

		btn_weather = rootView.findViewById(R.id.layout_weather);
		btn_weather.setOnClickListener(this);

		btn_network = rootView.findViewById(R.id.layout_menu_network);
		btn_network.setOnClickListener(this);

		btn_settings = rootView.findViewById(R.id.layout_menu_settings);
		btn_settings.setOnClickListener(this);

	}

	@Override
	View baseBottomMenuContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(mContext).inflate(R.layout.optionmenu, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_wallpaper:
			Log.d("OptionBottomMenu", " R.id.btn_wallpaper");
			listener.settingWallpaper();
			break;
		case R.id.layout_weather:
			Log.d("OptionBottomMenu", " R.id.btn_weather");
			listener.settingWeather();
			break;
		case R.id.layout_menu_network:
			gotoNetworkSettings();
			break;
		case R.id.layout_menu_settings:
			gotoSystemSettings();
			break;
		default:
			break;
		}
	}

	private void gotoNetworkSettings() {
		//mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                Intent settingtIntent = new Intent(mContext, com.mirageteam.launcher.setting.Setting.class);
                mContext.startActivity(settingtIntent);

	}

	private void gotoSystemSettings() {
		Intent settingtIntent = new Intent(mContext, com.mirageteam.launcher.setting.Setting.class);
		mContext.startActivity(settingtIntent);
		//mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
	}
}
