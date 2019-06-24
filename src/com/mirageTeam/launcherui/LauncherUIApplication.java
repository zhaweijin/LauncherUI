package com.mirageTeam.launcherui;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import com.mirageTeam.api.imp.UdownApiImp;
import com.mirageTeam.weather.bean.ProvincesData;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 应用程序入口
 * @author terry
 *
 */
public class LauncherUIApplication extends Application{
	

	private static LauncherUIApplication instance;
	private final transient UdownApiImp apiServer =new UdownApiImp();
	
	/**
	 * 城市数据
	 */
	private ProvincesData provinceData;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this; 
		initImageLoader(getApplicationContext());
	}
	
	
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)  
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new WeakMemoryCache())
		        .memoryCacheSize(5 * 1024 * 1024) 
		        .discCacheSize(50 * 1024 * 1024)
		        .discCacheFileCount(100)
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(context)) // default
		        .imageDecoder(new BaseImageDecoder()) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default 
		        .build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	 
	
	/*
	 * 访问API的操作类
	 */
	public UdownApiImp getApiServer()
	{
		return apiServer;
	}
	
	public static LauncherUIApplication getInstance(){
		return instance;
	}
	
	public void setProvinceData(ProvincesData province){
		this.provinceData=province;
	}
	
	public ProvincesData getProvinceData(){
		return provinceData;
	}
	
	
	
	
}
