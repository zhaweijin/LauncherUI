package com.mirageteam.launcher.setting;




import java.io.File;
import java.io.UTFDataFormatException;
import java.util.List;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.widget.CleanMemoryBottomMenu;
import com.mirageteam.launcher.market.Util;




import android.R.integer;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends ActivityGroup{

	
	private RelativeLayout layout_network;
	private RelativeLayout layout_display;
	private RelativeLayout layout_security;
	private RelativeLayout layout_update;
	private RelativeLayout layout_volume;
	private RelativeLayout layout_storage;
	
	private LinearLayout container;
	private int Currentposition = DISPLAY_NETWORK;
	
	public final static int DISPLAY_NETWORK = 1;
	public final static int DISPLAY_DISPLAY = 2;
	public final static int DISPLAY_VOLUME = 3;
	public final static int DISPLAY_SECURITY = 4;
	public final static int DISPLAY_STORAGE = 5;
	public final static int DISPLAY_UPDATE = 6;

	private static final String TAG = "Settings";

	private TextView setting_subtitle;

	
	private Button wifiSwitch;
	private Button moreSetting;
	private Button openStorageInternal;
	private Button openStorageSdcard;
    	private AlertDialog.Builder builder;
	private boolean wifiState = false;
	
	
	private WifiManager wifiManager;
	private final static String WIFI_NAME_UPDATE = "com.wifi_name.update";
	
	public static final String SD_PATH = "/storage/external_storage/sdcard1";
	
	private final static int OPEN_WIFI_BUTTON = 0x111;
	private final static int CLOSE_WIFI_BUTTON = 0x112;
	private final static int DISPLAY_SDCARD_BUTTON = 0x113;
	private final static int HIDE_SDCARD_BUTTON = 0x114;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case OPEN_WIFI_BUTTON:
				wifiSwitch.setText(getResources().getString(R.string.open_wifi));
				break;
			case CLOSE_WIFI_BUTTON:
				wifiSwitch.setText(getResources().getString(R.string.close_wifi));
				break;
			case DISPLAY_SDCARD_BUTTON:
				openStorageSdcard.setVisibility(View.VISIBLE);
				break;
			case HIDE_SDCARD_BUTTON:
				openStorageSdcard.setVisibility(View.INVISIBLE);
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_main);
		
 
		wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		String displayString="";
		try {
			displayString = getResources().getString(R.string.software_name) + "甘果桌面\n" +
            getResources().getString(R.string.software_version) + getPackageManager().getPackageInfo(
    				getPackageName(), 0).versionName + "\n" +
    		   getResources().getString(R.string.software_web) +"www.cango.cc\n" +
    		   getResources().getString(R.string.software_qq) + "322063899";
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		builder = new AlertDialog.Builder(Setting.this);
        builder.setTitle(getResources().getString(R.string.about));
        builder.setMessage(displayString);

		initlayout();
		wifiSwitch.setText(wifiManager.isWifiEnabled()?getResources().getString(R.string.close_wifi)
				:getResources().getString(R.string.open_wifi));
		
//		wifiSwitch.setText(getResources().getString(R.string.close_wifi));
		setting_subtitle = (TextView)findViewById(R.id.setting_subtitle);
		
		loadWifi();
	}
	
	
	
	private void initlayout(){
		layout_network = (RelativeLayout)findViewById(R.id.layout_network);
		layout_display = (RelativeLayout)findViewById(R.id.layout_display);
		layout_security = (RelativeLayout)findViewById(R.id.layout_security);
		layout_update = (RelativeLayout)findViewById(R.id.layout_update);
		layout_volume = (RelativeLayout)findViewById(R.id.layout_volume);
		layout_storage = (RelativeLayout)findViewById(R.id.layout_storage);
		
		container = (LinearLayout) findViewById(R.id.containerBody);
		
		layout_network.setOnClickListener(onClickListener);
		layout_display.setOnClickListener(onClickListener);
		layout_security.setOnClickListener(onClickListener);
		layout_update.setOnClickListener(onClickListener);
		layout_volume.setOnClickListener(onClickListener);
		layout_storage.setOnClickListener(onClickListener);
		
		
		layout_network.setOnFocusChangeListener(onFocusChangeListener);
		layout_display.setOnFocusChangeListener(onFocusChangeListener);
		layout_security.setOnFocusChangeListener(onFocusChangeListener);
		layout_update.setOnFocusChangeListener(onFocusChangeListener);
		layout_volume.setOnFocusChangeListener(onFocusChangeListener);
		layout_storage.setOnFocusChangeListener(onFocusChangeListener);
		
		
		wifiSwitch = (Button)findViewById(R.id.open_or_close_wifi);
		moreSetting = (Button)findViewById(R.id.more_setting);
		openStorageInternal = (Button)findViewById(R.id.open_storage_internal);
		openStorageSdcard = (Button)findViewById(R.id.open_storage_sdcard);
		wifiSwitch.setOnClickListener(onClickListener);
		moreSetting.setOnClickListener(onClickListener);
		openStorageInternal.setOnClickListener(onClickListener);
		openStorageSdcard.setOnClickListener(onClickListener);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mCommonReceiver);
		unregisterReceiver(mStorageListener);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerForBroadcasts();
		registStorageBroadcast();
	}



	private void loadWifi(){
		
		
		wifiSwitch.setVisibility(View.VISIBLE);
		setting_subtitle.setText(getResources().getString(R.string.wifi_setting_subtitle));
		
		layout_network.setSelected(true);
        //Tony de
		Intent intent = new Intent(Setting.this, WifiSettings.class);
		intent.putExtra("state", DISPLAY_NETWORK);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		container.removeAllViews();
		container.addView(
				getLocalActivityManager().startActivity("Module1", intent)
						.getDecorView(), LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}
	
	
	OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            Log.v(TAG, "onclick");		
 			Intent intent;
			switch (v.getId()) {
			case R.id.layout_network:
				if (Currentposition != DISPLAY_NETWORK) {
 
					openStorageInternal.setVisibility(View.INVISIBLE);
					openStorageSdcard.setVisibility(View.INVISIBLE);
					wifiSwitch.setVisibility(View.VISIBLE);
					
		            layout_network.setSelected(true);
		            layout_display.setSelected(false);
		            layout_security.setSelected(false);
		            layout_update.setSelected(false);
		            layout_volume.setSelected(false);
		            layout_storage.setSelected(false);
 
		            setting_subtitle.setText(getResources().getString(R.string.wifi_setting_subtitle));
		            

					Currentposition = DISPLAY_NETWORK;
				    intent = new Intent(Setting.this,WifiSettings.class);
					intent.putExtra("state", DISPLAY_NETWORK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					container.removeAllViews();
					container.addView(getLocalActivityManager()
							.startActivity("Module1", intent)
							.getDecorView(),
							LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);
				}
				break;
			case R.id.layout_update:
				if (Currentposition != DISPLAY_UPDATE) {
					
					openStorageInternal.setVisibility(View.INVISIBLE);
					openStorageSdcard.setVisibility(View.INVISIBLE);
					wifiSwitch.setVisibility(View.INVISIBLE);
					
					setting_subtitle.setText(getResources().getString(R.string.update_setting_subtitle));
					
                    layout_update.setSelected(true);
                    layout_network.setSelected(false);
                    layout_storage.setSelected(false);
                    layout_volume.setSelected(false);
                    layout_display.setSelected(false);
                    layout_security.setSelected(false);
                    

					Currentposition = DISPLAY_UPDATE;
					intent = new Intent(Setting.this,OtherSetting.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("state", Currentposition);
					container.removeAllViews();
					container.addView(getLocalActivityManager()
							.startActivity("Module2", intent)
							.getDecorView(),
							LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);
				}
 				break;
			case R.id.layout_display:
				if(Currentposition!=DISPLAY_DISPLAY){
					
					openStorageInternal.setVisibility(View.INVISIBLE);
					openStorageSdcard.setVisibility(View.INVISIBLE);
					wifiSwitch.setVisibility(View.INVISIBLE);
					
					setting_subtitle.setText(getResources().getString(R.string.display_setting_subtitle));
					
					layout_display.setSelected(true);
					layout_network.setSelected(false);
					layout_security.setSelected(false);
					layout_storage.setSelected(false);
					layout_volume.setSelected(false);
					layout_update.setSelected(false);
					
					Currentposition = DISPLAY_DISPLAY;
					intent = new Intent(Setting.this,OtherSetting.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("state", Currentposition);
					container.removeAllViews();
					container.addView(getLocalActivityManager()
							.startActivity("Module2", intent)
							.getDecorView(),
							LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);
				}
				break;
			case R.id.layout_security:
				if(Currentposition!=DISPLAY_SECURITY){
					
					//openStorageInternal.setVisibility(View.INVISIBLE);
					//openStorageSdcard.setVisibility(View.INVISIBLE);
					//wifiSwitch.setVisibility(View.INVISIBLE);
					
					//setting_subtitle.setText(getResources().getString(R.string.security_setting_subtitle));
					
					//layout_security.setSelected(true);
					//layout_network.setSelected(false);
					//layout_storage.setSelected(false);
					//layout_volume.setSelected(false);
					//layout_update.setSelected(false);
					//layout_display.setSelected(false);
					
					
					//Currentposition = DISPLAY_SECURITY;
					//intent = new Intent(Setting.this,OtherSetting.class);
					//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//intent.putExtra("state", Currentposition);
					//container.removeAllViews();
					//container.addView(getLocalActivityManager()
					//		.startActivity("Module2", intent)
					//		.getDecorView(),
					//		LayoutParams.FILL_PARENT,
					//		LayoutParams.FILL_PARENT);
                    builder.show();
				}
				break;
			case R.id.layout_storage:
				if(Currentposition!=DISPLAY_STORAGE){
                        Toast.makeText(Setting.this, "功能正在开发中", Toast.LENGTH_SHORT).show();
				}		
				break;
			case R.id.layout_volume:
				if(Currentposition!=DISPLAY_VOLUME){
					
					openStorageInternal.setVisibility(View.INVISIBLE);
					openStorageSdcard.setVisibility(View.INVISIBLE);
					wifiSwitch.setVisibility(View.INVISIBLE);
					
					setting_subtitle.setText(getResources().getString(R.string.sound_setting_subtitle));
					
					layout_volume.setSelected(true);
					layout_network.setSelected(false);
					layout_security.setSelected(false);
					layout_update.setSelected(false);
					layout_display.setSelected(false);
					layout_storage.setSelected(false);
					
					Currentposition = DISPLAY_VOLUME;
					intent = new Intent(Setting.this,OtherSetting.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("state", Currentposition);
					container.removeAllViews();
					container.addView(getLocalActivityManager()
							.startActivity("Module2", intent)
							.getDecorView(),
							LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);
				}
				break;
			case R.id.open_or_close_wifi:
				
				if(wifiSwitch.getText().toString().equals(getResources().getString(R.string.close_wifi))){
					wifiManager.setWifiEnabled(false);
					container.removeAllViews();
				}else {
					wifiManager.setWifiEnabled(true);
					loadWifi();
				}

				
				break;
			case R.id.more_setting:
				Intent settingIntent = new Intent(Intent.ACTION_MAIN);
				settingIntent.addCategory(Intent.CATEGORY_LAUNCHER);           
				ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings");           
				settingIntent.setComponent(cn);
				startActivity(settingIntent);
				break;
			case R.id.open_storage_internal:
				
//				intent = new Intent(Setting.this,StorageSetting.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("state", "internal");
//				container.removeAllViews();
//				container.addView(getLocalActivityManager()
//						.startActivity("Module2", intent)
//						.getDecorView(),
//						LayoutParams.FILL_PARENT,
//						LayoutParams.FILL_PARENT);
				break;
			case R.id.open_storage_sdcard:

//				intent = new Intent(Setting.this,StorageSetting.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("state", "external");
//				container.removeAllViews();
//				container.addView(getLocalActivityManager()
//						.startActivity("Module2", intent)
//						.getDecorView(),
//						LayoutParams.FILL_PARENT,
//						LayoutParams.FILL_PARENT);
				break;
			}
		}
	};
	
	
	
	View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg1){
				switch (arg0.getId()) {
				case R.id.layout_network:
					layout_network.setSelected(true);
					layout_display.setSelected(false);
					layout_security.setSelected(false);
					layout_storage.setSelected(false);
					layout_volume.setSelected(false);
					layout_update.setSelected(false);
					break;
				case R.id.layout_display:
					layout_network.setSelected(false);
					layout_display.setSelected(true);
					layout_security.setSelected(false);
					layout_storage.setSelected(false);
					layout_volume.setSelected(false);
					layout_update.setSelected(false);
					break;
				case R.id.layout_volume:
					layout_network.setSelected(false);
					layout_display.setSelected(false);
					layout_security.setSelected(false);
					layout_storage.setSelected(false);
					layout_volume.setSelected(true);
					layout_update.setSelected(false);
					break;
				case R.id.layout_storage:
					layout_network.setSelected(false);
					layout_display.setSelected(false);
					layout_security.setSelected(false);
					layout_storage.setSelected(true);
					layout_volume.setSelected(false);
					layout_update.setSelected(false);
					break;
				case R.id.layout_security:
					layout_network.setSelected(false);
					layout_display.setSelected(false);
					layout_security.setSelected(true);
					layout_storage.setSelected(false);
					layout_volume.setSelected(false);
					layout_update.setSelected(false);
					break;
				case R.id.layout_update:
					layout_network.setSelected(false);
					layout_display.setSelected(false);
					layout_security.setSelected(false);
					layout_storage.setSelected(false);
					layout_volume.setSelected(false);
					layout_update.setSelected(true);
					break;
				}
			}
		}
	};
	
    private void registerForBroadcasts() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WIFI_NAME_UPDATE);
        //wifi
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //storage
        registerReceiver(mCommonReceiver, intentFilter);
        
        
    }
	
    
    private final BroadcastReceiver mCommonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(WIFI_NAME_UPDATE)){
//            	wifiSwitch.setText(wifiManager.isWifiEnabled()?getResources().getString(R.string.close_wifi)
//        				:getResources().getString(R.string.open_wifi));
            }else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            	int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN);
            	Log.v(TAG, "wifi state>>>>>>>"+state);
            	switch (state) {
                case WifiManager.WIFI_STATE_ENABLED:
                	handler.sendEmptyMessage(CLOSE_WIFI_BUTTON);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                	handler.sendEmptyMessage(CLOSE_WIFI_BUTTON);
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                	handler.sendEmptyMessage(OPEN_WIFI_BUTTON);
                    break;
                }
            }
       }
    };
	
    
    
	 
	public void registStorageBroadcast() {

		IntentFilter f = new IntentFilter();
		f.addAction(Intent.ACTION_MEDIA_REMOVED);
		f.addAction(Intent.ACTION_MEDIA_EJECT);
		f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		f.addAction(Intent.ACTION_MEDIA_MOUNTED);
		f.addDataScheme("file");
		registerReceiver(mStorageListener, f);
	}

	private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			 if(Currentposition==DISPLAY_STORAGE){
				 if(Util.avaiableMedia()){
					handler.sendEmptyMessage(DISPLAY_SDCARD_BUTTON); 
				 }else {
					handler.sendEmptyMessage(HIDE_SDCARD_BUTTON);
				}
			 }
		}
	};
	
	
	public static boolean isSdcardExists() {
//		if (Environment.getExternalStorage2State().startsWith(Environment.MEDIA_MOUNTED)) {
//			File dir = new File(SD_PATH);
//			if (dir.exists() && dir.isDirectory()) {
//				return true;
//			}
//		}
		return false;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "keycode>>"+keyCode);
		 if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
			switch (getCurrentFocus().getId()) {
			case R.id.layout_network:
				layout_network.setSelected(false);
				layout_display.setSelected(false);
				layout_security.setSelected(false);
				layout_storage.setSelected(false);
				layout_volume.setSelected(false);
				layout_update.setSelected(false);
				break;
			}
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			Log.v(TAG, ".................down");
			event.startTracking();
			if (flag == true) {
				shortpressflag = false;
			} else {
				shortpressflag = true;
				flag = false;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	protected boolean shortpressflag = false;
	protected	boolean flag = false;
	private void showMemoryCleanView() {
		CleanMemoryBottomMenu memoryMenu = new CleanMemoryBottomMenu(this);
		memoryMenu.show();
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			 Log.i("KEYCODE_MENU", "KEYCODE_MENU =======onKeyLongPress==== ");
			// Toast.makeText(mContext,
			// "KEYCODE_MENU =======onKeyLongPress====",
			// Toast.LENGTH_SHORT).show();
			shortpressflag = false;
			flag = true;

			showMemoryCleanView();
			return true;

		}
		return super.onKeyLongPress(keyCode, event);
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Log.v(TAG, ".................up");
			event.startTracking();
			if (shortpressflag) {
				Log.i("KEYCODE_MENU", "KEYCODE_MENU =======onKeyUp==== ");
				// Toast.makeText(mContext, "KEYCODE_MENU =======onKeyUp====",
				// Toast.LENGTH_SHORT).show();
//				showMenuOption();
			}
			shortpressflag = true;
			flag = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        
//        if(event.getKeyCode()==KeyEvent.KEYCODE_MENU){
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
}
