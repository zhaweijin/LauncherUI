package com.mirageteam.launcher.setting;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.text.format.Formatter;
import com.google.android.collect.Lists;
import com.mirageTeam.launcherui.R;
import com.mirageteam.launcher.setting.StorageMeasurement.MeasurementDetails;
import com.mirageteam.launcher.setting.StorageMeasurement.MeasurementReceiver;


import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StorageSetting extends Activity{

    private String TAG = "StorageSetting";
    private static final int ORDER_USAGE_BAR = -2;
    private static final int ORDER_STORAGE_LOW = -1;

    /** Physical volume being measured, or {@code null} for internal. */
    private  StorageVolume mVolume;
    private  StorageMeasurement mMeasure;

    private  StorageManager mStorageManager;


    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    		LayoutParams.WRAP_CONTENT);
    
    
    private StorageItem mItemTotal;
    private StorageItem mItemAvailable;
    private StorageItem mItemApps;
    private StorageItem mItemDcim;
    private StorageItem mItemMusic;
    private StorageItem mItemDownloads;
    private StorageItem mItemCache;
    private StorageItem mItemMisc;
    private List<StorageItem> mItemUsers = Lists.newArrayList();

    private boolean mUsbConnected;
    private String mUsbFunction;

    private long mTotalSize;

    private static final int MSG_UI_UPDATE_APPROXIMATE = 1;
    private static final int MSG_UI_UPDATE_DETAILS = 2;
    
    
    private  StorageVolume[] storageVolumes;
    private PercentageBarChart mChart = null;

    private final List<PercentageBarChart.Entry> mEntries = Lists.newArrayList();
    
    
    private TextView totalTextView;
    private TextView availableTextView;
    private LinearLayout layout_storage_item;
    private TextView title;
    
    private Handler mUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UI_UPDATE_APPROXIMATE: {
                    final long[] size = (long[]) msg.obj;
                    Log.v(TAG, "totalsize>>>"+size[0]);
                    Log.v(TAG, "availedsize>>>"+size[1]);
                    updateApproximate(size[0], size[1]);
                    break;
                }
                case MSG_UI_UPDATE_DETAILS: {
                    final MeasurementDetails details = (MeasurementDetails) msg.obj;
                    updateDetails(details);
                    break;
                }
            }
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storage_setting);
		
		mStorageManager = StorageManager.from(StorageSetting.this);
		storageVolumes = mStorageManager.getVolumeList();
		
		Log.v(TAG, "storagevolumes,length="+storageVolumes.length);
		for(int i=0;i<storageVolumes.length;i++){
			Log.v(TAG, "path>>>>"+ storageVolumes[0].getPath());
		}
		
		
		mVolume = storageVolumes[0];
		
		if(getIntent().getStringExtra("state").equals("internal")){
			 mMeasure = StorageMeasurement.getInstance(this, null);
		}else if(getIntent().getStringExtra("state").equals("external")){
			 mMeasure = StorageMeasurement.getInstance(this, mVolume);
		}
		
		
       

//        Log.v(TAG, "getpath===="+mVolume.getPath());
   
        
		
		
		init();
		
		
		
		if(mVolume==null){
			title.setText(getResources().getString(R.string.internal_storage));
		}else{
			title.setText(getResources().getString(R.string.external_storage));
		}
	}
	
    public void init() {
    
    	mChart = (PercentageBarChart) findViewById(R.id.percentage_bar_chart);

        

        mItemTotal = new StorageItem(R.string.memory_size, 0,0);
        mItemAvailable = new StorageItem(R.string.memory_available, getResources().getColor(R.color.memory_avail),1);
  

        mItemApps = new StorageItem(R.string.memory_apps_usage, getResources().getColor(R.color.memory_apps_usage),2);
        mItemDcim = new StorageItem(R.string.memory_dcim_usage, getResources().getColor(R.color.memory_dcim),3);
        mItemMusic = new StorageItem(R.string.memory_music_usage, getResources().getColor(R.color.memory_music),4);
        mItemDownloads = new StorageItem(R.string.memory_downloads_usage, getResources().getColor(R.color.memory_downloads),5);
        mItemCache = new StorageItem(R.string.memory_media_cache_usage, getResources().getColor(R.color.memory_cache),6);
        mItemMisc = new StorageItem(R.string.memory_media_misc_usage, getResources().getColor(R.color.memory_misc),7);


        totalTextView = (TextView)findViewById(R.id.total_storage_size);
        availableTextView = (TextView)findViewById(R.id.availed_storage_size);
        title = (TextView)findViewById(R.id.storage_title);
        layout_storage_item = (LinearLayout)findViewById(R.id.layout_storage_item);
 
    }
    
//    private void displayInitStorage(){
//    	Util.print(TAG, value);
//    }
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMeasure.setReceiver(mReceiver);
        measure();
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMeasure.cleanUp();
	}
	
	
	private void measure() {
        mMeasure.invalidate();
        mMeasure.measure();
    }
	
	public StorageVolume getStorageVolume() {
        return mVolume;
    }
	public void updateApproximate(long totalSize, long availSize) {
//        mItemTotal.setSummary(formatSize(totalSize));
//        mItemAvailable.setSummary(formatSize(availSize));
		totalTextView.setText(formatSize(totalSize));
		availableTextView.setText(formatSize(availSize));

        mTotalSize = totalSize;

        final long usedSize = totalSize - availSize;
        
        Log.v(TAG, "total="+mTotalSize);
        Log.v(TAG, "availsize="+availSize);

        //update top progressbar view
        mEntries.clear();
        addEntry(0, usedSize / (float) totalSize, android.graphics.Color.GRAY);
        mChart.setEntries(mEntries);
        commit();
        
 
    }
	
	private String formatSize(long size) {
        return Formatter.formatFileSize(this, size);
    }
	
	public void commit() {
        if (mChart != null) {
        	Log.v(TAG, ">>>>>>commit");
            mChart.invalidate();
        }
    }
	
	  private MeasurementReceiver mReceiver = new MeasurementReceiver() {
	        @Override
	        public void updateApproximate(StorageMeasurement meas, long totalSize, long availSize) {
	            mUpdateHandler.obtainMessage(MSG_UI_UPDATE_APPROXIMATE, new long[] {
	                    totalSize, availSize }).sendToTarget();
	        }

	        @Override
	        public void updateDetails(StorageMeasurement meas, MeasurementDetails details) {
	            mUpdateHandler.obtainMessage(MSG_UI_UPDATE_DETAILS, details).sendToTarget();
	        }
	    };
	    
	    
	    public void updateDetails(MeasurementDetails details) {
	        

	        // Count caches as available space, since system manages them
//	        mItemTotal.setSummary(formatSize(details.totalSize));
//	        mItemAvailable.setSummary(formatSize(details.availSize));
	    	totalTextView.setText(getResources().getString(R.string.storge_total_size)+" :"+ formatSize(details.totalSize));
			availableTextView.setText(getResources().getString(R.string.storge_avalied_size)+" :"+ formatSize(details.availSize));

			
			
	        mEntries.clear();

	        updateStorageItem(mItemApps, details.appsSize);

	        final long dcimSize = totalValues(details.mediaSize, Environment.DIRECTORY_DCIM,
	                Environment.DIRECTORY_MOVIES, Environment.DIRECTORY_PICTURES);
	        updateStorageItem(mItemDcim, dcimSize);

	        final long musicSize = totalValues(details.mediaSize, Environment.DIRECTORY_MUSIC,
	                Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_NOTIFICATIONS,
	                Environment.DIRECTORY_RINGTONES, Environment.DIRECTORY_PODCASTS);
	        updateStorageItem(mItemMusic, musicSize);

	        final long downloadsSize = totalValues(details.mediaSize, Environment.DIRECTORY_DOWNLOADS);
	        updateStorageItem(mItemDownloads, downloadsSize);

	        updateStorageItem(mItemCache, details.cacheSize);
	        updateStorageItem(mItemMisc, details.miscSize);


	       
	        mChart.setEntries(mEntries);
	        
	        Log.v(TAG, "entries,size="+mEntries.size());
	        
	        commit();
	    }
	    
	    private void updateStorageItem(StorageItem storageItem, long size) {
	        if (size > 0) {
//	            pref.setSummary(formatSize(size));
	        	updateStorageItemView(getResources().getString(storageItem.getName()), size);
	        	Log.v(TAG, "name="+getResources().getString(storageItem.getName()));
	        	Log.v(TAG, "size="+formatSize(size));
	            final int order = storageItem.getOrder();
	            addEntry(order, size / (float) mTotalSize, storageItem.getColor());
	        }
	    }
	    
	    
	    public void addEntry(int order, float percentage, int color) {
	    	Log.v(TAG, "percent=="+percentage+",clor="+color);
	        mEntries.add(PercentageBarChart.createEntry(order, percentage, color));
	        Collections.sort(mEntries);
	         
	    }
	    
	    
	    private void updateStorageItemView(String name,long size){
	    	View storageItemLayout = LayoutInflater.from(this).inflate(R.layout.storage_item, null);
	    	TextView mSize = (TextView)storageItemLayout.findViewById(R.id.size);
	    	TextView mName = (TextView)storageItemLayout.findViewById(R.id.name);
	    	TextView mUnit = (TextView)storageItemLayout.findViewById(R.id.unit);
	    	
	    	if(name.equals(getResources().getString(R.string.memory_apps_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_app_background);
	    	}else if(name.equals(getResources().getString(R.string.memory_dcim_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_picture_background);
	    	}else if(name.equals(getResources().getString(R.string.memory_downloads_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_download_background);
	    	}else if(name.equals(getResources().getString(R.string.memory_media_cache_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_cache_background);
	    	}else if(name.equals(getResources().getString(R.string.memory_media_misc_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_misc_background);
	    	}else if(name.equals(getResources().getString(R.string.memory_music_usage))){
	    		storageItemLayout.setBackgroundResource(R.drawable.storage_music_background);
	    	}
	    	
	    	
	    	String displaySize  = formatSize(size);
	    	
	    	mSize.setText(displaySize.substring(0, displaySize.length()-2));
	    	mUnit.setText(displaySize.substring(displaySize.length()-2, displaySize.length()));
	    	
	    	Log.v(TAG, "size="+formatSize(size));
	    	mName.setText(name);
	    	params.leftMargin = 10;
	    	params.width = 122;
	    	params.height = 266;
	    	layout_storage_item.addView(storageItemLayout, params);
	    	
	    }
	    
	    private static long totalValues(HashMap<String, Long> map, String... keys) {
	        long total = 0;
	        for (String key : keys) {
	            Long v = map.get(key);
	            if (v != null)
	                total += v.longValue();
	        }
	        return total;
	    }
}
