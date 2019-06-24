package com.mirageteam.launcher.market;


import java.io.File;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.launcherui.ui.BaseActivity;
 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.R.plurals;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InstallActivity extends Activity {

	private String TAG = "InstallActivity";
	
	private Button install;
	private TextView title;
	private TextView introduce;
	private ImageView iconImageView;
	
	private Gallery gallery;
	private GalleryAdapter galleryAdapter;
	
	private MarketData marketData;
	
	private int currentDownloadValue;
	
	private String[] introduceImageStrings;
	private Bitmap iconImage;
	
	private View download_progress = null;
	private ProgressBar progressBar;
	private TextView progressvalue;
	
	private boolean galleryLeftTop = false;
	
	private String currentPackagename;
	private String software_name;
	private DownloadDatabase downloadDatabase;
	
	private LinearLayout layout_progressLayout;
	
	private boolean threadProgress = false;;
	
	private final static int DISPLAY_ICON = 0x111;
	private final static int DISPLAY_GALLERY_IMAGE = 0x112;
	private final static int UPDATE_DOWNLOAD_PROGRESS = 0x113;
	private final static int DISPLAY_INSTALL_BUTTON = 0x114;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case DISPLAY_ICON:
				if(iconImage!=null)
				   iconImageView.setImageBitmap(iconImage);
				break;
			case DISPLAY_GALLERY_IMAGE:
				break;
			case UPDATE_DOWNLOAD_PROGRESS:
				progressBar.setProgress(currentDownloadValue);
				progressvalue.setText(currentDownloadValue + "%");
				if (currentDownloadValue == 100) {
                    layout_progressLayout.setVisibility(View.INVISIBLE);
					install.setText(getResources().getString(R.string.uninstall));
				}
				break;
			case DISPLAY_INSTALL_BUTTON:
				install.setText(getResources().getString(R.string.install));
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.install_main);
		
		marketData =  (MarketData)getIntent().getParcelableExtra("marketData");
		
//		Util.print("title", marketData.getTitle());
		Util.print("message", marketData.getMessage());
		Util.print("intrduceimage", marketData.getIntroduceImagesUrl());
		downloadDatabase = Util.getDownloadDatabase(this);
		
		initlayout();
		
		
		software_name = marketData.getTitle();
		
		initInstallText();
		
		initData();
		
		
		//receive package installed message
 
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
//		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(mPackageInstallListener, filter);
	}

 
	private void initlayout(){
		
		install = (Button)findViewById(R.id.install);
		title = (TextView)findViewById(R.id.title);
		introduce = (TextView)findViewById(R.id.introduce);
		gallery = (Gallery)findViewById(R.id.introduce_gallery);
		gallery.setSpacing(10);
		
		iconImageView = (ImageView)findViewById(R.id.app_icon);
		layout_progressLayout = (LinearLayout)findViewById(R.id.layout_progress);
		
		
		progressBar = (ProgressBar) findViewById(R.id.progress);
        progressvalue = (TextView) findViewById(R.id.value);
        
        layout_progressLayout.setVisibility(View.INVISIBLE);
		
	}
	
	private void initInstallText(){
		if(marketData!=null){
			String packagename = marketData.getPackagename();
			currentPackagename = packagename;
			String version = marketData.getVersion();
			install.setText(getResources().getString(R.string.install));
			
			//由于没有填写报名，所以这个功能暂时不打开
//			if(Util.checkPackageIsExist(this, packagename)){
//				PackageInfo packageInfo = Util.getPackageInfo(this, packagename);
//				if(packageInfo!=null){
//					String oldVersion = packageInfo.versionName;
//					if(Util.compareVersions(version, oldVersion)){
//						if(checkIsDownloading()){
//							install.setText(getResources().getString(R.string.installing));
//						}else {
//							install.setText(getResources().getString(R.string.update));
//						}
//					}else {
//						install.setText(getResources().getString(R.string.open));
//					}
//				}
//			}else {
				if(checkIsDownloading()){
					//=100未安装
					if(checkIsDownloaded()){
						if(checkDownloadFileExist()){
							install.setText(getResources().getString(R.string.uninstall));
						}else {
							downloadDatabase.deleteByPackageData(currentPackagename);
							install.setText(getResources().getString(R.string.install));
						}
					}else {
						install.setText(getResources().getString(R.string.installing));
						loadingDownloadProgress();
					}
				}
			}

//		}
	}
	
	private void initData(){
		if(marketData!=null){
			title.setText(marketData.getTitle());
			introduce.setText("      "+marketData.getMessage());
			introduceImageStrings = marketData.getIntroduceImagesUrl().split("\\|");
			Util.print("introduce imagesize", introduceImageStrings.length+"");
			
			Util.print("introducaaa",  "aaaaa");
			
			install.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!Util.checkNetworkIsActive(InstallActivity.this))
						return;
					if(install.getText().toString().equals(getResources().getString(R.string.install)) || 
							install.getText().toString().equals(getResources().getString(R.string.update))){
						install.setText(getResources().getString(R.string.installing));
//						Util.softwareDownloadState.put(marketData.getPackagename(), true);
						downloadDatabaseManager();
						
						loadingDownloadProgress();
						
						Util.startDownloadService(InstallActivity.this, marketData.getInstallUrl().replaceAll(" ", "%20"),
								marketData.getPackagename());
					}else if(install.getText().toString().equals(getResources().getString(R.string.open))){
						Util.OpenSoftware(InstallActivity.this, marketData.getPackagename());
					}else if(install.getText().toString().equals(getResources().getString(R.string.uninstall))){
						
						String filepath = getDownloadFilePath();
  
						Util.installFile(new File(filepath), InstallActivity.this);
					}
					
				}
			});
			
			
			loadImage();
			loadGalley();
		}
		
	}
	
	private void loadImage(){
		if(marketData!=null){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						iconImage = Util.GetBitmapByUrl(marketData.getImageUrl()); 
						handler.sendEmptyMessage(DISPLAY_ICON);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	
	private void loadGalley(){
		galleryAdapter = new GalleryAdapter(this,introduceImageStrings);
		gallery.setAdapter(galleryAdapter);
//		gallery.setSelection(0);
		
		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v(TAG, "pos>>>>>>>>>>>>>"+arg2);
				if(arg2==0)
					galleryLeftTop = true;
				else {
					galleryLeftTop = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
//		gallery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mPackageInstallListener);
		threadProgress = false;
	}
	
	
	/*
	 * volume status
	 */
	private BroadcastReceiver mPackageInstallListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			 
            if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){
//            	Log.v("package", ">>>>>>>add packagename="+currentPackagename);
            	String packagename = intent.getDataString();
				packagename = packagename.substring(
						packagename.indexOf(":") + 1, packagename.length());
				Util.print("name", packagename);
            	if(currentPackagename!=null && packagename.equals(currentPackagename)){
            		install.setText(getResources().getString(R.string.install));                  //modify by no packagename  
            	}
            }
		}
	};
 

	public void downloadDatabaseManager() {

		try {

			Cursor temCursor = downloadDatabase.queryByPackage(currentPackagename);
			temCursor.moveToFirst();
			int size = temCursor.getCount();
			if (size == 0) {
				Log.v(TAG, "inseart data");
				downloadDatabase.insertData(software_name,currentPackagename, 0, "");
			}
			temCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean checkIsDownloading(){
		try {

			Cursor temCursor = downloadDatabase.queryByPackage(currentPackagename);
			temCursor.moveToFirst();
			int size = temCursor.getCount();
			if (size != 0) {
				return true;
			}
			temCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkIsDownloaded(){
		try {

			Cursor temCursor = downloadDatabase.queryByPackage(currentPackagename);
			temCursor.moveToFirst();
			int size = temCursor.getCount();
			if (size != 0) {
				int count = temCursor.getInt(temCursor.getColumnIndexOrThrow(DownloadDatabase.KEY_DOWNLOND_NUM));
				if(count==100)
				  return true;
			}
			temCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkDownloadFileExist(){
		try {

			Cursor temCursor = downloadDatabase.queryByPackage(currentPackagename);
			temCursor.moveToFirst();
			int size = temCursor.getCount();
			if (size != 0) {
				String filepath = temCursor.getString(temCursor.getColumnIndexOrThrow(DownloadDatabase.KEY_FILEPATH));
				File file = new File(filepath);
				if(file.exists())
				   return true;
			}
			temCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getDownloadFilePath(){
		try {

			Cursor temCursor = downloadDatabase.queryByPackage(currentPackagename);
			temCursor.moveToFirst();
			int size = temCursor.getCount();
			if (size != 0) {
				String filepath = temCursor.getString(temCursor.getColumnIndexOrThrow(DownloadDatabase.KEY_FILEPATH));
				File file = new File(filepath);
				if(file.exists())
				   return file.getAbsolutePath();
			}
			temCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public void loadingDownloadProgress() {

		Util.print(TAG, "loadingDownloadProgress");
		threadProgress = true;
        
        layout_progressLayout.setVisibility(View.VISIBLE);
		
		new Thread(new Runnable() {

			public void run() {
				try {
					Cursor temCursor = null;
					int returnvalue = -1;
					int size;
					while (threadProgress) {
						Util.print(TAG, ">>>>update progress");
						temCursor = downloadDatabase.queryByPackage(currentPackagename);
						size = temCursor.getCount();
						temCursor.moveToFirst();
						if (size != 0) {
							returnvalue = temCursor.getInt(temCursor
									.getColumnIndexOrThrow(DownloadDatabase.KEY_DOWNLOND_NUM));
						}else {
							Util.print(TAG, ">>>>is delete record");
							handler.sendEmptyMessage(DISPLAY_INSTALL_BUTTON);
							break;
						}
						if (!temCursor.isClosed())
							temCursor.close();
						if (returnvalue == -1) {
							break;
						} else {
							if (returnvalue != 100) {
								Thread.sleep(300);
							}
							if (returnvalue != -1) {
								currentDownloadValue = returnvalue;
								if (currentDownloadValue > 0)
									handler.sendEmptyMessage(UPDATE_DOWNLOAD_PROGRESS);
							} else {
								break;
							}
						}
						if (returnvalue == 100) {
							break;
						}
					}
					Util.print("threadbreak", "threadbreak");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//        Log.v(TAG, "keycode>>>"+keyCode);
//		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
//		{
//			Log.v(TAG, "focus>>>");
// 
//			if(galleryLeftTop){
//				Log.v(TAG, "aaaaaaa>>>");
//				install.requestFocus();
//			}
//			
//		}
//		return super.onKeyDown(keyCode, event);
//		
//	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		Log.v(TAG, "keycode>>>"+keyCode);
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
				if(galleryLeftTop){
					install.requestFocus();
				}
				
		}
		return super.onKeyUp(keyCode, event);
	}
}
