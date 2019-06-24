package com.mirageteam.launcher.setting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mirageTeam.launcherui.R;
 

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemProperties;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.provider.Settings;
import android.app.ActivityManagerNative;


public class OtherSetting extends Activity{

	private String TAG = "OtherSetting";
	//system security
	private RelativeLayout install_apk_state;
	private TextView install_apk_message;
	private CheckBox install_apk_checkbox; 
	
	private RelativeLayout check_apk_state;
	private TextView check_apk_message;
	private CheckBox check_apk_checkbox;
	
	private RelativeLayout devices_manger;
	
	
	//system update
	private RelativeLayout localUpdateRelativeLayout;
	private RelativeLayout networkUpdateRelativeLayout;
	
	//display 
	private RelativeLayout wallpaperRelativeLayout;
	private RelativeLayout outputModeRelativeLayout;
	private TextView outputModeMessage;
	private RelativeLayout fontsizeRelativeLayout;
	private TextView fontsizeMessage;
	private RelativeLayout wifiDisplayRelativeLayout;
	private static final int GET_USER_OPERATION=1;
	private final String OUTPUT_MODE = "output_mode";
	private final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
    private final String ACTION_CVBSMODE_CHANGE = "android.intent.action.CVBSMODE_CHANGE";
    private static final String STR_HAS_CVBS = "ro.platform.has.cvbsmode";
    private static final String STR_CVBS_MODE = "ubootenv.var.cvbsmode";
    private int default_index_entry;
    private int select_index_entry;
    private ListView outputModelistView;
	
	private int wallpaperSelectID = -1;
	private int fontSelectID = -1;
	
	private String[] mOutputModeEntryValues;
    private HDMICheckHandler mHDMIHandler = null;
	
	//volume
	private RelativeLayout volumeRelativeLayout;
	private RelativeLayout keydownVolumeRelativeLayout;
	private RelativeLayout dataVolumeOutputReLayout;
	private TextView dataVolumeOutputMessage;
	private CheckBox keydownVolumeCheckBox;
	
	
	public static SeekBar mMusicSeekBar = null;
	public static SeekBar mRingSeekBar = null;
	public static SeekBar mAlarmSeekBar = null;
	public AudioManager audioManager;
	private MediaPlayer mMediaPlayer;
	
	
	//
	
	private static final String STR_DIGIT_AUDIO_OUTPUT = "ubootenv.var.digitaudiooutput";
	private int sel_index;
	private static String DigitalRawFile = "/sys/class/audiodsp/digital_raw";
	
    private final Configuration mCurConfig = new Configuration();
    public final static LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    

    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		switch (getIntent().getIntExtra("state", 1)) {
		case Setting.DISPLAY_DISPLAY:
			//system display
			
			setContentView(R.layout.display_setting);
			loadSystemDisplaySetting();
			break;
		case Setting.DISPLAY_SECURITY:
		    //security
			setContentView(R.layout.security_setting);
			loadSystemSecuritySetting();
			break;
		case Setting.DISPLAY_UPDATE:
			//update
		    setContentView(R.layout.update_setting);
			loadSystemUpdateSetting();
			break;
		case Setting.DISPLAY_VOLUME:
			Log.v(TAG, "display VOLUME");
			//system volume
			setContentView(R.layout.volume_setting);
			loadSystemVolumeSetting();
			break;
		}


	}
	
	
	
	private void loadSystemVolumeSetting(){
		volumeRelativeLayout = (RelativeLayout)findViewById(R.id.volume_manager);
		keydownVolumeRelativeLayout = (RelativeLayout)findViewById(R.id.keydown_volume);
		dataVolumeOutputReLayout = (RelativeLayout)findViewById(R.id.data_volume_output);
		dataVolumeOutputMessage = (TextView)findViewById(R.id.data_volume_output_message);
		keydownVolumeCheckBox = (CheckBox)findViewById(R.id.keydown_volume_checkbox);
		
		keydownVolumeCheckBox.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.SOUND_EFFECTS_ENABLED, 1) != 0);
		
		keydownVolumeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				 Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED,
		                    arg1 ? 1 : 0);
			}
		});
		
		
		volumeRelativeLayout.setOnClickListener(volumeClickListener);
		keydownVolumeRelativeLayout.setOnClickListener(volumeClickListener);
		dataVolumeOutputReLayout.setOnClickListener(volumeClickListener);
		
		
		
		
		String valDigitAudioOutput = SystemProperties.get(STR_DIGIT_AUDIO_OUTPUT);
		dataVolumeOutputMessage.setText(valDigitAudioOutput);
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

		initMediaPlayer(this);
	}
	
	
	
	
	
	
	private void loadSystemDisplaySetting(){
		Log.v(TAG, ">>>>>>>>>>>>load system display setting");
		wallpaperRelativeLayout = (RelativeLayout)findViewById(R.id.wallpaper_manager);
		outputModeRelativeLayout = (RelativeLayout)findViewById(R.id.output_mode);
		fontsizeRelativeLayout = (RelativeLayout)findViewById(R.id.font_size);
		fontsizeMessage = (TextView)findViewById(R.id.font_size_message);
		wifiDisplayRelativeLayout = (RelativeLayout)findViewById(R.id.wifi_display);
//		wifiDisplayMessage = (TextView)findViewById(R.id.wifi_display_message);
//		wifiDisplayCheckBox = (CheckBox)findViewById(R.id.wifi_display_checkbox);
		
		outputModeMessage = (TextView)findViewById(R.id.output_mode_message);
		
		wallpaperRelativeLayout.setOnClickListener(dispayClickListener);
		outputModeRelativeLayout.setOnClickListener(dispayClickListener);
		fontsizeRelativeLayout.setOnClickListener(dispayClickListener);
		wifiDisplayRelativeLayout.setOnClickListener(dispayClickListener);
		
//		wifiDisplayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		
		
		
		
		readFontSize();
		
		
		Config.Instance(this);
		if(isMBXDevice()){
            mHDMIHandler = new HDMICheckHandler(this, true);
            mHDMIHandler.startHDMICheck();
        }
		
		String valOutputmode = Config.getCurrentOutputResolution();
		Log.v(TAG, "outputmode>>>"+valOutputmode);
		mOutputModeEntryValues = getResources().getStringArray(R.array.outputmode_entries_display);
//		Log.v(TAG, "logic to display>>>"+Config.Logic2DisplayEntry(valOutputmode));
        int index_entry = findIndexOfEntry(Config.Logic2DisplayEntry(valOutputmode), mOutputModeEntryValues);
// 
//        Log.v(TAG, "outputmode>>>index="+index_entry);
//        
		outputModeMessage.setText(mOutputModeEntryValues[index_entry]);
	}
	
	
	private void loadSystemUpdateSetting(){
		localUpdateRelativeLayout = (RelativeLayout)findViewById(R.id.local_update);
		networkUpdateRelativeLayout = (RelativeLayout)findViewById(R.id.network_update);
		
		localUpdateRelativeLayout.setOnClickListener(systemUpdateClickListener);
		networkUpdateRelativeLayout.setOnClickListener(systemUpdateClickListener);
	}
	
	
	private void loadSystemSecuritySetting(){
		install_apk_state = (RelativeLayout)findViewById(R.id.layout_install_apk_state);
		install_apk_message = (TextView)findViewById(R.id.install_apk_message);
		install_apk_checkbox = (CheckBox)findViewById(R.id.install_apk_checkbox);
		
		check_apk_state = (RelativeLayout)findViewById(R.id.layout_check_apk_state);
		check_apk_message = (TextView)findViewById(R.id.check_apk_message);
		check_apk_checkbox = (CheckBox)findViewById(R.id.check_apk_checkbox);
		
		install_apk_state.setOnClickListener(securityClickListener);
		check_apk_state.setOnClickListener(securityClickListener);
		
		install_apk_checkbox.setChecked(isNonMarketAppsAllowed());
		check_apk_checkbox.setChecked(isVerifyAppsEnabled());
		
		check_apk_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.v("security install apk checkbox state", isChecked+"");
				Settings.Global.putInt(getContentResolver(), Settings.Global.PACKAGE_VERIFIER_ENABLE,
	                    isChecked ? 1 : 0);
			}
		});
		
		install_apk_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.v("security check apk checkbox state", isChecked+"");
				setNonMarketAppsAllowed(isChecked);
			}
		});
		
		
		devices_manger = (RelativeLayout)findViewById(R.id.devices_manger);
		devices_manger.setOnClickListener(securityClickListener);
	}
	
	
	View.OnClickListener securityClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v("aaa", ">>>>>>>>>>>>>>aaa");
			switch (v.getId()) {
			//install apk
			case R.id.layout_install_apk_state:
				install_apk_checkbox.setChecked(!install_apk_checkbox.isChecked());
				break;
			//check apk to install	
			case R.id.layout_check_apk_state:
				check_apk_checkbox.setChecked(!check_apk_checkbox.isChecked());
				break;
			case R.id.devices_manger:
				Intent devicesIntent = new Intent(Intent.ACTION_MAIN);   
				ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$DeviceAdminSettingsActivity");           
				devicesIntent.setComponent(cn);
				startActivity(devicesIntent);
				break;
			}
		}
	};
	
	
	
	
	View.OnClickListener systemUpdateClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.local_update:
				openSystemUpdate();
				break;
			case R.id.network_update:
				openSystemUpdate();
				break;
			}
		}
	};
	
	
	private void openSystemUpdate(){

		ComponentName componentName = new ComponentName(
                "com.example.Upgrade","com.example.Upgrade.UpgradeActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

	}
	
	
    View.OnClickListener dispayClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.wallpaper_manager:
				populateWallpaperTypes();
				break;
			case R.id.output_mode:
				populateOutputMode();
				break;
			case R.id.font_size:
				populateFonts();
				break;
			case R.id.wifi_display:
				ComponentName componentName = new ComponentName(
		                "com.android.settings","com.android.settings.Settings$WifiDisplaySettingsActivity");
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.setComponent(componentName);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		        try{
		            startActivity(intent);
		        }catch (ActivityNotFoundException e) {
		            // TODO: handle exception
		            e.printStackTrace();
		        }
//				wifiDisplayCheckBox.setChecked(!wifiDisplayCheckBox.isChecked());
				break;
			}
		}
	};
	
	
	
    View.OnClickListener volumeClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.volume_manager:
				populateVolume();
				break;
			case R.id.keydown_volume:
				keydownVolumeCheckBox.setChecked(!keydownVolumeCheckBox.isChecked());
				break;
			case R.id.data_volume_output:
				populateDataVolume();
				break;
			}
		}
	};
	
	
	 private boolean isNonMarketAppsAllowed() {
	        return Settings.Global.getInt(getContentResolver(),
	        		Settings.Global.INSTALL_NON_MARKET_APPS, 0) > 0;
	  }
	
	 
	 private void setNonMarketAppsAllowed(boolean enabled) {
	        // Change the system setting
		    Log.v(TAG, "enable==="+enabled);
	        Settings.Global.putInt(getContentResolver(), Settings.Global.INSTALL_NON_MARKET_APPS,
	                                enabled ? 1 : 0);
	 }
	 
	 
	 private void populateWallpaperTypes() {
	        // Search for activities that satisfy the ACTION_SET_WALLPAPER action
	        final Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
	        final PackageManager pm = getPackageManager();
	        final List<ResolveInfo> rList = pm.queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);

	        final Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
	        View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
	        dialog.setContentView(view);
	        ListView listView = new ListView(this);
	        listView.setCacheColorHint(0);
	        LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
	        
	        ArrayList<String> title = new ArrayList<String>();
	        // Add Preference items for each of the matching activities
	        for (ResolveInfo info : rList) {
//	            Intent prefIntent = new Intent(intent);
//	            prefIntent.setComponent(new ComponentName(
//	                    info.activityInfo.packageName, info.activityInfo.name));
	            CharSequence label = info.loadLabel(pm);
	            if (label == null) label = info.activityInfo.packageName;
	            title.add(label+"");
	        }
	        
	        TextView dialogTitle = (TextView)view.findViewById(R.id.title);
	        dialogTitle.setText(getResources().getString(R.string.wallpaper_manager));
	        
	        
	        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.simple_list_item_single_choice,title));
			listView.setSelector(R.drawable.setting_dialog_select_background);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			LinearLayout.LayoutParams LP_FW_TEMP = new LinearLayout.LayoutParams(
	    			LayoutParams.FILL_PARENT, 250);
			
			contentView.addView(listView,LP_FW_TEMP);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					wallpaperSelectID = arg2;
				}
				
			});
			
			
			LayoutParams lay = dialog.getWindow().getAttributes();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);

			lay.height = 450;
			lay.width = dm.widthPixels;
			
			
			final Button cancelButton = (Button)view.findViewById(R.id.cancel);
			final Button okButton = (Button)view.findViewById(R.id.ok);
			
			cancelButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			okButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(wallpaperSelectID!=-1){
						Intent prefIntent = new Intent(intent);
				        prefIntent.setComponent(new ComponentName(
				                    rList.get(wallpaperSelectID).activityInfo.packageName, rList.get(wallpaperSelectID).activityInfo.name));
				        OtherSetting.this.startActivity(prefIntent);
					}
					
				}
			});
			
			
			
			dialog.show();
	    }
	 
	 
		private void setParams(Dialog dialog) {

			LayoutParams lay = dialog.getWindow().getAttributes();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);

			lay.height = 400;
			lay.width = dm.widthPixels;
		}
	 
	   private boolean isVerifyAppsEnabled() {
	        return Settings.Global.getInt(getContentResolver(),
	                                      Settings.Global.PACKAGE_VERIFIER_ENABLE, 1) > 0;
	    }
	   
	   
	   
		 private void populateFonts() {
		        

		        final Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
		        View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
		        dialog.setContentView(view);
		        ListView listView = new ListView(this);
		        listView.setCacheColorHint(0);
		        LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
		        
		         
		        String[] title = getResources().getStringArray(R.array.font_size_name);
		        
		        TextView dialogTitle = (TextView)view.findViewById(R.id.title);
		        dialogTitle.setText(getResources().getString(R.string.font_set));
		        
		        
		        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.simple_list_item_single_choice,title));
				listView.setSelector(R.drawable.setting_dialog_select_background);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				contentView.addView(listView);
				
				//set default select
				try {
		            mCurConfig.updateFrom(ActivityManagerNative.getDefault().getConfiguration());
		        } catch (RemoteException e) {
		            Log.w(TAG, "Unable to retrieve font size");
		        }
		        int index = floatToIndex(mCurConfig.fontScale);
				listView.setSelection(index);
				
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						fontSelectID = arg2;
//						writeFontSizePreference(getResources().getStringArray(R.array.font_size_value)[arg2]);
//						fontsizeMessage.setText(getResources().getStringArray(R.array.font_size_name)[arg2]);
					}
					
				});
				
				
				LayoutParams lay = dialog.getWindow().getAttributes();
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);

				lay.height = 500;
				lay.width = dm.widthPixels;
				
				
				final Button cancelButton = (Button)view.findViewById(R.id.cancel);
				final Button okButton = (Button)view.findViewById(R.id.ok);
				
				cancelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if(fontSelectID!=-1){
							writeFontSizePreference(getResources().getStringArray(R.array.font_size_value)[fontSelectID]);
							fontsizeMessage.setText(getResources().getStringArray(R.array.font_size_name)[fontSelectID]);
						}
						
					}
				});
				
				
				dialog.show();
		    }
		 
		 public void readFontSize() {
		        try {
		            mCurConfig.updateFrom(ActivityManagerNative.getDefault().getConfiguration());
		        } catch (RemoteException e) {
		            Log.w(TAG, "Unable to retrieve font size");
		        }


		        int index = floatToIndex(mCurConfig.fontScale);
		        fontsizeMessage.setText(getResources().getStringArray(R.array.font_size_name)[index]);

		    }
		 
		   int floatToIndex(float val) {
		        String[] indices = getResources().getStringArray(R.array.font_size_value);
		        float lastVal = Float.parseFloat(indices[0]);
		        for (int i=1; i<indices.length; i++) {
		            float thisVal = Float.parseFloat(indices[i]);
		            if (val < (lastVal + (thisVal-lastVal)*.5f)) {
		                return i-1;
		            }
		            lastVal = thisVal;
		        }
		        return indices.length-1;
		    }
		   
		   public void writeFontSizePreference(Object objValue) {
		        try {
		            mCurConfig.fontScale = Float.parseFloat(objValue.toString());
		            ActivityManagerNative.getDefault().updatePersistentConfiguration(mCurConfig);
		        } catch (RemoteException e) {
		            Log.w(TAG, "Unable to save font size");
		        }
		    }
		   
		   
		   
		   
		   private void populateDataVolume() {
		        

		        final Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
		        View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
		        dialog.setContentView(view);
		        ListView listView = new ListView(this);
		        LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
		        
		         
		        String[] title = getResources().getStringArray(R.array.digit_audio_output_entries);
		        
		        TextView dialogTitle = (TextView)view.findViewById(R.id.title);
		        dialogTitle.setText(getResources().getString(R.string.data_audio_output));
		        
		        
		        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.simple_list_item_single_choice,title));
				listView.setSelector(R.drawable.setting_dialog_select_background);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				contentView.addView(listView);
				
				
				//set default select
				String valDigitAudioOutput = SystemProperties.get(STR_DIGIT_AUDIO_OUTPUT);
		        int index_entry = findIndexOfEntry(valDigitAudioOutput, title);
		        listView.setItemChecked(index_entry, true);
		       
				
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
//						writeFontSizePreference(getResources().getStringArray(R.array.font_size_value)[arg2]);
						if(!SystemProperties.getBoolean("ro.platform.has.digitaudio", false))
							return;
						
						sel_index = Integer.parseInt(getResources().getStringArray(R.array.digit_audio_output_values)[arg2]);
			            if(sel_index == 0)
			            {
			                //when digit audio output set to PCM
			                SystemProperties.set(STR_DIGIT_AUDIO_OUTPUT, "PCM");
			                File digitalRawFile = new File(DigitalRawFile);
			                try {
			                    BufferedWriter out = new BufferedWriter(new FileWriter(digitalRawFile), 32);
			                    try {
			                        out.write("0");
			                        dataVolumeOutputMessage.setText("PCM");
			                    }
			                    finally {
			                        out.close();
			                    }
			                }
			                catch (IOException e) {
			                    
			                    Log.e(TAG, "IOException when write "+digitalRawFile);
			                }
			                Log.i(TAG,"digit audio output set to PCM");
			            }
			            else if(sel_index == 1)
			            {
			                //when digit audio output set to RAW
			                SystemProperties.set(STR_DIGIT_AUDIO_OUTPUT, "RAW");
			                File digitalRawFile = new File(DigitalRawFile);
			                try {
			                    BufferedWriter out = new BufferedWriter(new FileWriter(digitalRawFile), 32);
			                    try {
			                        out.write("1");
			                        dataVolumeOutputMessage.setText("RAW");
			                    }
			                    finally {
			                        out.close();
			                    }
			                }
			                catch (IOException e) {
			                    
			                    Log.e(TAG, "IOException when write "+digitalRawFile);
			                }
			                Log.i(TAG,"digit audio output set to RAW");
			            }
			            else if(sel_index == 2)
			            {
			                    //when digit audio output set to SPDIF passthrough
			                    SystemProperties.set(STR_DIGIT_AUDIO_OUTPUT, "SPDIF passthrough");
			                    File digitalRawFile = new File(DigitalRawFile);
			                    try 
			                    {
			                        BufferedWriter out = new BufferedWriter(new FileWriter(digitalRawFile), 32);
			                        try 
			                        {
			                            out.write("1");
			                            dataVolumeOutputMessage.setText("SPDIF passthrough");
			                        }
			                        finally 
			                        {
			                            out.close();
			                        }
			                    }
			                    catch (IOException e) 
			                    {
			                        Log.e(TAG, "IOException when write "+digitalRawFile);
			                    }
			                    
			                    Log.i(TAG,"digit audio output set to SPDIF passthrough");
			            }
			            else if(sel_index == 3)
			            {
			                    //when digit audio output set to HDMI passthrough
			                    SystemProperties.set(STR_DIGIT_AUDIO_OUTPUT, "HDMI passthrough");
			                    File digitalRawFile = new File(DigitalRawFile);
			                    try 
			                    {
			                        BufferedWriter out = new BufferedWriter(new FileWriter(digitalRawFile), 32);
			                        try 
			                        {
			                            out.write("2");
			                            dataVolumeOutputMessage.setText("HDMI passthrough");
			                        }
			                        finally 
			                        {
			                            out.close();
			                        }
			                    }
			                    catch (IOException e) 
			                    {
			                        Log.e(TAG, "IOException when write "+digitalRawFile);
			                    }
			                    
			                    Log.i(TAG,"digit audio output set to HDMI passthrough");
			            }
						
						dataVolumeOutputMessage.setText(getResources().getStringArray(R.array.digit_audio_output_entries)[arg2]);
						dialog.dismiss();
					}
					
				});
				
				
				LayoutParams lay = dialog.getWindow().getAttributes();
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);

				lay.height = 300;
				lay.width = dm.widthPixels;
				
				
				
				final Button cancelButton = (Button)view.findViewById(R.id.cancel);
				cancelButton.setVisibility(View.INVISIBLE);
				final Button okButton = (Button)view.findViewById(R.id.ok);
				okButton.setVisibility(View.INVISIBLE);
				
				
				
				dialog.show();
		    }
		   
		   
		   private int findIndexOfEntry(String value, CharSequence[] entry) {
			 
		        if (value != null && entry != null) {
		            for (int i = entry.length - 1; i >= 0; i--) {
		                if (value.equalsIgnoreCase(entry[i]+"")) {
		                    return i;
		                }
		            }
		        }
		        return 0;  //set PCM as default
		    }
		   
		   
		   
     private void populateOutputMode() {
		        

		        final Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
		        View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
		        dialog.setContentView(view);
		        outputModelistView = new ListView(this);
		         
		        
		        outputModelistView.setCacheColorHint(0);
		        LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
		        
		         
		        final String[] title = getResources().getStringArray(R.array.outputmode_entries_display);
		        
		        TextView dialogTitle = (TextView)view.findViewById(R.id.title);
		        dialogTitle.setText(getResources().getString(R.string.output_mode));
		        
		        
		        outputModelistView.setAdapter(new ArrayAdapter<String>(this,R.layout.simple_list_item_single_choice,title));
		        outputModelistView.setSelector(R.drawable.setting_dialog_select_background);
		        outputModelistView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		        
		        LinearLayout.LayoutParams LP_FW_TEMP = new LinearLayout.LayoutParams(
		    			LayoutParams.FILL_PARENT, 300);
		        
				contentView.addView(outputModelistView,LP_FW_TEMP);
				
				//set default select
				String valOutputmode = Config.getCurrentOutputResolution();
	    		Log.v(TAG, "outputmode>>>"+valOutputmode);
	    		mOutputModeEntryValues = getResources().getStringArray(R.array.outputmode_entries_display);
	    		Log.v(TAG, "logic to display>>>"+Config.Logic2DisplayEntry(valOutputmode));
	    		default_index_entry = findIndexOfEntry(Config.Logic2DisplayEntry(valOutputmode), mOutputModeEntryValues);
	            outputModelistView.setItemChecked(default_index_entry, true);
				
				
				
	            outputModelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						if(!SystemProperties.getBoolean("ro.screen.has.tvout", false))
							return;
						Log.v(TAG, ">>>>>");
					      try 
					        {
					      
					    	select_index_entry = arg2;
					    	
							if(default_index_entry!=arg2)
							{
								Intent intent = new Intent(OtherSetting.this, OutputSetConfirm.class);
								intent.putExtra("set_mode", Config.Display2LogicEntry(mOutputModeEntryValues[arg2] + ""));
//								intent.putExtra("set_mode", title[arg2].toLowerCase() + "");
//								Config.Logd(getClass().getName(), Config.Display2LogicEntry(mEntryValues[sel_index] + ""));
								startActivityForResult(intent, GET_USER_OPERATION);
							}
						}
					        catch (NumberFormatException e)
					        {
					                Log.e(TAG, "could not persist output mode setting", e);
					        }
					  dialog.dismiss();
					}
					
				});
				
	            
	            LayoutParams lay = dialog.getWindow().getAttributes();
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);

				lay.height = 500;
				lay.width = dm.widthPixels;
				
				
				
				final Button cancelButton = (Button)view.findViewById(R.id.cancel);
				cancelButton.setVisibility(View.INVISIBLE);
				final Button okButton = (Button)view.findViewById(R.id.ok);
				okButton.setVisibility(View.INVISIBLE);
				 
				
				
				
				dialog.show();
	}
		   
		   
	@Override
	public  void onActivityResult(int requestCode,int resultCode,Intent data)
	{
				super.onActivityResult(requestCode,resultCode,data);
				switch(requestCode)
				{
					case (GET_USER_OPERATION):
						if(resultCode==Activity.RESULT_OK)
						{
		                       String []values = getResources().getStringArray(R.array.outputmode_entries_display);
		                       String tv_outputmode = Config.Display2LogicEntry(values[sel_index]);
		                       Config.Logd(getClass().getName(), "tv_outputmode is: " + tv_outputmode);                   
		                       Intent intent_outputmode_save = new Intent(ACTION_OUTPUTMODE_SAVE);
		                       intent_outputmode_save.putExtra(OUTPUT_MODE, tv_outputmode);
		                       OtherSetting.this.sendBroadcast(intent_outputmode_save);       
		                       outputModeMessage.setText(mOutputModeEntryValues[select_index_entry]);
//		                       index_entry = sel_index;
//		                       mOutputmode.setValueIndex(index_entry);
//		                       if (sw.getPropertyBoolean(STR_HAS_CVBS,false)) {
//		                           if(tv_outputmode.equals("576i")||tv_outputmode.equals("480i"))
////		                                mDualCvbsOutput.setEnabled(false);
//		                           else
////		                                mDualCvbsOutput.setEnabled(true);
//		                       }
						}
						else if(resultCode==Activity.RESULT_CANCELED)
						{
							outputModeMessage.setText(mOutputModeEntryValues[default_index_entry]);
						}
						
					}
		}  
		
	public static boolean isMBXDevice()
    {
            String mode = SystemProperties.get("ro.platform.has.mbxuimode");
            if("true".equalsIgnoreCase(mode))
            {
                    return true;
            }
                    
            return false;
    }
	
		   
		 private void populateVolume(){
			
			 
			 Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
		     View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
		     dialog.setContentView(view);
		     LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
		     
		     
		     View volumeView = LayoutInflater.from(this).inflate(R.layout.dialog_ringervolume, null);
		     contentView.addView(volumeView);
		     
		     
		     mMusicSeekBar =  (SeekBar)volumeView.findViewById(R.id.media_volume_seekbar);
		     mMusicSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		     mMusicSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//		     Log.v(TAG, "music sound=="+audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		     mMusicSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		     

		     mRingSeekBar =  (SeekBar)volumeView.findViewById(R.id.ringer_volume_seekbar);
		     mRingSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		     mRingSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
		     mRingSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		     
		     mAlarmSeekBar =  (SeekBar)volumeView.findViewById(R.id.alarm_volume_seekbar);
		     mAlarmSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		     mAlarmSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		     mAlarmSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		     
		     TextView dialogTitle = (TextView)view.findViewById(R.id.title);
		     dialogTitle.setText(getResources().getString(R.string.volume));
		     
		     dialog.show();
			 
		 }
		   
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

		// @Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			switch (seekBar.getId()) {
			case R.id.media_volume_seekbar:
//				Log.v(TAG, ">>>>>>"+progress);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				break;
			case R.id.ringer_volume_seekbar:
				audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
				break;
			case R.id.alarm_volume_seekbar:
				audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
				break;
			}
//			Log.v(TAG, "id==="+seekBar.getId());
//			

		}

		// @Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			playSound();
		}

		// @Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// stopSound();

		}

	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseMediaPlayer();
		
		if(isMBXDevice() && mHDMIHandler!=null){ 
            mHDMIHandler.stopHDMICheck();
	    }
	}

	public void initMediaPlayer(Context mContext) {
		if(mMediaPlayer==null)
		     mMediaPlayer = MediaPlayer.create(mContext, R.raw.media_volume);

	}

	private void playSound() {

		mMediaPlayer.start();

	}

	private void stopSound() {

		mMediaPlayer.stop();

	}
		 
	
//	public void populateDevicesManager() {
//		final Dialog dialog = new Dialog(this);
//		View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
//		dialog.setContentView(view);
//
//		LinearLayout contentView = (LinearLayout) view.findViewById(R.id.content);
//        View addView = LayoutInflater.from(this).inflate(R.layout.setting_security_devices, null);
//        contentView.addView(addView);
//		
//		
//		
//		LayoutParams lay = dialog.getWindow().getAttributes();
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		lay.height = 600;
//		lay.width = dm.widthPixels;
//		
//		
//		final Button cancelButton = (Button)view.findViewById(R.id.cancel);
//		final Button okButton = (Button)view.findViewById(R.id.ok);
//		
//	    cancelButton.setVisibility(View.INVISIBLE);
//	    okButton.setVisibility(View.INVISIBLE);
////		okButton.setOnClickListener(new View.OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
////				dialog.dismiss();
////				 
////			}
////		});
//
//		TextView dialogTitle = (TextView) view.findViewById(R.id.title);
//		dialogTitle.setText(getResources().getString(R.string.devices_manager));
//
//		dialog.show();
//	}
		   
}
