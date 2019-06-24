package com.mirageTeam.launcherui.ui;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.widget.AppListBottonMenu;
import com.mirageTeam.widget.ScaleImageButton;

public class DefaultLauncherFragment extends Fragment implements OnClickListener {

	final private static int UPDATE_STATE_WIFI = 0;
	final private static int UPDATE_STATE_ETHERNET = 1;
	final private static int UPDATE_STATE_SD = 2;
	final private static int UPDATE_STATE_USB = 3;
	final private static int UPDATE_STATE_SOUND = 4;

	ImageButton movieCatalogButton, musicCatalogButton, tvCatalogButton, marketButton, appsButton, settingsButton;

//	ApplicationCategoryMenu applicationCategory;
	AppListBottonMenu appMenu;
	
	GridView mAppView;

	//notification area
	private LinearLayout notification_area;
    private ConnectivityManager mConnectivityManager;
    private AudioManager mAudioManager;
    private WifiManager mWifiManager;

 
	public static final String SD_PATH = "/storage/external_storage/sdcard1";
	public static final String USB_PATH ="/storage/external_storage";
	
	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    		LayoutParams.WRAP_CONTENT);
	
	
	public interface showMenuCallBackListener{
		void showMenu(ApplicationCategoryType type);
	}
	
	public showMenuCallBackListener listener;
	
	public void showMenuCallBackListener(showMenuCallBackListener showMenuCallBackListener){
		this.listener = showMenuCallBackListener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerStatusBarBroadcast();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.launcher_layout, container, false);
		
		movieCatalogButton = (ImageButton) rootView.findViewById(R.id.btn_movie);
		movieCatalogButton.setOnClickListener(this);

		musicCatalogButton = (ImageButton) rootView.findViewById(R.id.btn_browser);
		musicCatalogButton.setOnClickListener(this);

		tvCatalogButton = (ImageButton) rootView.findViewById(R.id.btn_televison);
		tvCatalogButton.setOnClickListener(this);

		marketButton = (ImageButton) rootView.findViewById(R.id.btn_appstore);
		marketButton.setOnClickListener(this);

		appsButton = (ImageButton) rootView.findViewById(R.id.btn_apps);
		appsButton.setOnClickListener(this);

		settingsButton = (ImageButton) rootView.findViewById(R.id.btn_settings);
		settingsButton.setOnClickListener(this);

		appMenu = new AppListBottonMenu(getActivity());
		
		
		notification_area = (LinearLayout)rootView.findViewById(R.id.notification_area); 
		
//		initNotificationArea();
		
		return rootView;
	}


	private void showSettingsMenu() {
		// TODO: show settings windows here
        //Intent settingtIntent = new Intent(getActivity(),
        //                com.mirageteam.launcher.setting.Setting.class);
        //startActivity(settingtIntent);
	}

	private void showMenu(ApplicationCategoryType type){
		if (listener != null) {
			listener.showMenu(type);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubvalue
		switch (v.getId()) {
		case R.id.btn_movie:
			// TODO: pop movie apps windows here
			showMenu(ApplicationCategoryType.Movie);
			break;
		case R.id.btn_browser:
			// TODO: pop music apps windows here
			showMenu(ApplicationCategoryType.Browser);
			break;
		case R.id.btn_televison:
			// TODO: pop television apps windows here
			showMenu(ApplicationCategoryType.TV);
			break;
		case R.id.btn_appstore:
			Intent marketIntent = new Intent(getActivity(), com.mirageteam.launcher.market.MainActivity.class);
			startActivity(marketIntent);
			break;
		case R.id.btn_apps:
			appMenu.show();
			break;
		case R.id.btn_settings:
			showSettingsMenu();
			break;
		default:
			break;
		}
	}

//	private void updateSystemState(int type) {
//		switch (type) {
//		case UPDATE_STATE_WIFI:
//			updateWifiState();
//			break;
//		case UPDATE_STATE_ETHERNET:
//			updateEthernetState();
//			break;
//		case UPDATE_STATE_SD:
//			updateSdcardSdate();
//			break;
//		case UPDATE_STATE_USB:
//			updateUSBDevicesState();
//			break;
//		case UPDATE_STATE_SOUND:
//			updateSoundState();
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void updateUSBDevicesState() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void updateSoundState() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void updateSdcardSdate() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void updateEthernetState() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void updateWifiState() {
//		// TODO Auto-generated method stub
//
//	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterStatusBarBroadcast();
	}
	
	
	private void initNotificationArea(){
		mConnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE); 
		mWifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
	}
	
	
	public void updateStatusBarIcon(){
		
		notification_area.removeAllViews();
		
//        updateWifiState();
//        updateEthernetState();
//        updateSdcardState();
//        updateUSBState();
//        updateVolumeState();
		
	}
	
	
	private void updateSdcardState(){
		if(isSdcardExists()){
			ImageView sdcard = new ImageView(getActivity());
			sdcard.setImageResource(R.drawable.sdcard);
			params.leftMargin=10;
			notification_area.addView(sdcard, params);
		}
	}
	
	private void updateUSBState(){
		if(isUsbExists()){
			ImageView usb = new ImageView(getActivity());
			usb.setImageResource(R.drawable.usbdevices);
			params.leftMargin=10;
			notification_area.addView(usb, params);
		}
	}
	
	private void updateVolumeState(){
    	
		ImageView sound = new ImageView(getActivity());
		params.leftMargin=10;

    	if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
    		//nute
    		sound.setImageResource(R.drawable.sound_off);
    	}else {
			//sound
    		sound.setImageResource(R.drawable.sound_on);
		}
    	
    	notification_area.addView(sound, params);
		
	}
	
	private void updateWifiState(){
		

        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int wifi_rssi = mWifiInfo.getRssi();
		int wifi_level = WifiManager.calculateSignalLevel(wifi_rssi, 5);
		 
		if(wifi_level>0){
			ImageView wifi = new ImageView(getActivity());
			wifi.setImageResource(R.drawable.wifi);
			params.leftMargin=10;
			notification_area.addView(wifi, params);
		}
 
	}
	
	
	private void updateEthernetState(){
		if(isEthernetOn()){
			ImageView eth = new ImageView(getActivity());
			eth.setImageResource(R.drawable.ethernet);
			params.leftMargin=10;
			notification_area.addView(eth, params);
		}

	}

	
	
	public static boolean isSdcardExists(){
//		if(Environment.getExternalStorage2State().startsWith(Environment.MEDIA_MOUNTED)) {
//			File dir = new File(SD_PATH);  
//			if (dir.exists() && dir.isDirectory()) {
//				return true;
//			}
//		}
		return false;
	}
	
	
	private boolean isEthernetOn(){
		NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	
		if (info.isConnected()){
//			Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ true");
			return true;
		} else {
//		    	Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ false");
			return false;
		}
	}
	
	public static boolean isUsbExists(){
		File dir = new File(USB_PATH);  
		if (dir.exists() && dir.isDirectory()) {
			if (dir.listFiles() != null) {
				if (dir.listFiles().length > 0) {
					for (File file : dir.listFiles()) {
						String path = file.getAbsolutePath();
						if (path.startsWith(USB_PATH+"/sd")&&!path.equals(SD_PATH)) {
					//	if (path.startsWith("/mnt/sd[a-z]")){
							return true;
						}
					}
				}
			}
		}

		return false;
	}
	
	
 
    
    /*
     * network status
     */
    public void registNetworkBroadcast(){
    	IntentFilter networkFilter = new IntentFilter();
		networkFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		getActivity().registerReceiver(mNetworkStateChangedReceiver,networkFilter);
    }
    
    private BroadcastReceiver mNetworkStateChangedReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//update network wifi or ethernet
//			updateStatusBarIcon();
		}
	};
 
    
    /*
     * storage status
     */
    public void registStorageBroadcast(){

        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_MEDIA_REMOVED);
        f.addAction(Intent.ACTION_MEDIA_EJECT);
        f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        f.addAction(Intent.ACTION_MEDIA_MOUNTED);
        f.addDataScheme("file");
        getActivity().registerReceiver(mStorageListener, f);
    }
    
    
    private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	//update media sdcard and usb
//        	updateStatusBarIcon();
        }
    };
    
    
    /*
     * volume status
     */
    private BroadcastReceiver mVolumeListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              //update storage volume or mute
//        	updateStatusBarIcon();
 
        }
    };
    public void registVolumeBroadcast(){
    	IntentFilter volumeFilter = new IntentFilter();
//    	volumeFilter.addAction(AudioManager.VOLUME_CHANGED_ACTION);
		getActivity().registerReceiver(mVolumeListener,volumeFilter);
    }
    
    
    private void registerStatusBarBroadcast(){

		registNetworkBroadcast();
		registStorageBroadcast();
		registVolumeBroadcast();
		
    }
 
    private void unregisterStatusBarBroadcast(){
    	getActivity().unregisterReceiver(mNetworkStateChangedReceiver);
    	getActivity().unregisterReceiver(mStorageListener);
    	getActivity().unregisterReceiver(mVolumeListener);
    }

}
