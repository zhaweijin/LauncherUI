package com.mirageteam.launcher.market;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.baidu.location.f.c;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.launcherui.ui.MainUI;


import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.style.UpdateAppearance;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
public class Util {
	private static final int ERROR = -1;


    private static final String SendMacToServiceAndUpdatePath = "http://www.seatay.com.cn/apkmis/apklist.php?";
    //packagename=com.mirageTeam.launcherui&version=1.0
//	public static Map<String, Boolean> softwareDownloadState = new HashMap<String, Boolean>();
	
	private static DownloadDatabase downloadDatabase;

	
	
	public static void print(String tag, String value) {
//		Log.v(tag, value);
	}

	public static final int threadMAX = 2;
	// 线程池管理下载线程
	public static ExecutorService downloadThreadPool;

	public static ExecutorService getDownloadThreadPool() {
		if (downloadThreadPool == null) {
			downloadThreadPool = Executors.newFixedThreadPool(threadMAX);
		}
		return downloadThreadPool;
	}
	
	public static boolean getNetworkState(Context context) {
		boolean mIsNetworkUp = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			mIsNetworkUp = info.isAvailable();
		}
		return mIsNetworkUp;
	}

	public static ProgressDialog getProgressDialog(Context context,
			String message) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setIcon(R.drawable.ic_launcher);
		progressDialog.setIndeterminate(false);
		return progressDialog;
	}

	public static int getDisplayMetricsHeight(Activity activity) {
		android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static DownloadDatabase getDownloadDatabase(Context context) {
		if (downloadDatabase == null) {
			downloadDatabase = new DownloadDatabase(context);
		}
		return downloadDatabase;
	}

	public static void setDownloadDatabase() {
		downloadDatabase = null;
	}

	/*
	 * author carter
	 * 
	 * @ArrayList<String> 获取已安装列表
	 */
	public static ArrayList<String> getInstallPackageName(Context context) {
		List<ApplicationInfo> applicationInfos = context.getPackageManager()
				.getInstalledApplications(0);
		ArrayList<String> packagename = new ArrayList<String>();
		for (int i = 0; i < applicationInfos.size(); i++) {
			ApplicationInfo appInfo = applicationInfos.get(i);
			String temppackagename = appInfo.packageName;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				packagename.add(temppackagename);
			}
		}
		return packagename;
	}

	

	public static InputStream getContent(String urlpath, String encoding)
			throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] getImage(String urlpath) throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			return readStream(inStream);
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	
	public static List<PackageInfo> getpackageName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfoList = packageManager
				.getInstalledPackages(0);
		return packageInfoList;
	}

	public static List<ApplicationInfo> getUnintalledApp(
			PackageManager packageManager) {
		List<ApplicationInfo> installedAppList = packageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

		List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo appInfo : installedAppList) {
			if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
					|| (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				result.add(appInfo);
			}
		}
		installedAppList.clear();
		installedAppList = null;
		Collections.sort(result, new ApplicationInfo.DisplayNameComparator(
				packageManager));
		return result;
	}

	

	public static boolean checkPackageIsExist(Context context,
			String packageName) {
		try {
			PackageManager mPm = context.getPackageManager();
			ApplicationInfo mAppInfo = mPm.getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			// show dialog
			return false;
		}
	}

	public static Boolean isSystemApp(ApplicationInfo mAppInfo) {
		Boolean mUpdatedSysApp = (mAppInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
		if (mUpdatedSysApp) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Compare two versions.
	 * 
	 * @param newVersion
	 *            new version to be compared
	 * @param oldVersion
	 *            old version to be compared
	 * @return true if newVersion is greater then oldVersion, false on
	 *         exceptions or newVersion=oldVersion and newVersion is lower then
	 *         oldVersion
	 */
	public static boolean compareVersions(String newVersion, String oldVersion) {
		if (newVersion.equals(oldVersion))
			return false;

		// Replace all - by . So a CyanogenMod-4.5.4-r2 will be a
		// CyanogenMod.4.5.4.r2
		newVersion = newVersion.replaceAll("-", "\\.");
		oldVersion = oldVersion.replaceAll("-", "\\.");

		String[] sNewVersion = newVersion.split("\\.");
		String[] sOldVersion = oldVersion.split("\\.");

		ArrayList<String> newVersionArray = new ArrayList<String>();
		ArrayList<String> oldVersionArray = new ArrayList<String>();

		newVersionArray.addAll(Arrays.asList(sNewVersion));
		oldVersionArray.addAll(Arrays.asList(sOldVersion));

		// Make the 2 Arrays the Same size filling it with 0. So Version 2
		// compared to 2.1 will be 2.0 to 2.1
		if (newVersionArray.size() > oldVersionArray.size()) {
			int difference = newVersionArray.size() - oldVersionArray.size();
			for (int i = 0; i < difference; i++) {
				oldVersionArray.add("0");
			}
		} else {
			int difference = oldVersionArray.size() - newVersionArray.size();
			for (int i = 0; i < difference; i++) {
				newVersionArray.add("0");
			}
		}

		int i = 0;
		for (String s : newVersionArray) {
			String old = oldVersionArray.get(i);
			// First try an Int Compare, if its a string, make a string compare
			try {
				int newVer = Integer.parseInt(s);
				int oldVer = Integer.parseInt(old);
				if (newVer > oldVer)
					return true;
				else if (newVer < oldVer)
					return false;
				else
					i++;
			} catch (Exception ex) {
				// If we reach here, we have to string compare cause the version
				// contains strings
				int temp = s.compareToIgnoreCase(old);
				if (temp < 0)
					return false;
				else if (temp > 0)
					return true;
				else
					// its the same value so continue
					i++;
			}
		}
		// Its Bigger so return true
		return true;
	}

	

	/**
	 * @author carter
	 * @fucntion 解析网络数据---->软件
	 */
	public static DataSet getSoftwareWebData(String webpage) {
		try {

			URL url = new URL(webpage);
			Log.d("MyDebug", webpage);
			URLConnection mycConnection = url.openConnection();

			mycConnection.setConnectTimeout(20000);
			mycConnection.setReadTimeout(20000);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			DataHandle gwh = new DataHandle();
			InputStreamReader isr = new InputStreamReader(
					mycConnection.getInputStream(), "utf-8");
			InputSource is = new InputSource(isr);
			xr.setContentHandler(gwh);
			xr.parse(is);
			return gwh.getDataSet();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @author carter
	 * @fucntion 截取网络地址的文件名
	 */
	public static String[] fileName(String webpath) {
		try {
			String temp[] = new String[2];
			temp[0] = webpath.substring(webpath.lastIndexOf(".") + 1,
					webpath.length()).toLowerCase();
			temp[1] = webpath.substring(webpath.lastIndexOf("/") + 1,
					webpath.lastIndexOf("."));
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @author carter
	 * @fucntion 获取打开文件文件的类型
	 */
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		return type;
	}

	

	/**
	 * @author carter
	 * @fucntion 检测SDcard是否插入
	 */
	public static boolean avaiableMedia() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED))
			return true;
		else {
			return false;
		}
	}

	/**
	 * @author carter
	 * @fucntion 删除文件，根据路径名
	 */
	public static void delFile(String strFileName) {
		File myFile = new File(strFileName);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * @author carter
	 * @fucntion 删除目录文件
	 */
	public static boolean deleteFileDir(File f) {
		boolean result = false;
		try {
			if (f.exists()) {

				File[] files = f.listFiles();
				for (File file : files) {

					if (file.isDirectory()) {
						if (deleteFile(file))
							result = false;
					} else {
						deleteFile(file);
					}
				}
				f.delete();
				result = true;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	/**
	 * @author carter
	 * @fucntion 删除文件，根据文件对象
	 */
	public static boolean deleteFile(File f) {
		boolean result = false;
		try {
			if (f.exists()) {
				f.delete();
				result = true;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}


	/**
	 * 将汉字转为UTF8编码的串
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/*
	 * @packagename 包名 根据包名打开软件
	 */
	public static void OpenSoftware(Activity activity, String packagename) {
		Intent intent = new Intent();
		PackageManager packageManager = activity.getPackageManager();
		try {
			intent = packageManager.getLaunchIntentForPackage(packagename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		activity.startActivity(intent);
	}

	

	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	// 类似的wifi是否打开
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	
	public static String translateWebChar(String content) {
		StringBuffer buffer = new StringBuffer();
		if (content.contains("\n")) {
			String[] temp_content = content.split("\n");
			Util.print("aa", "bb" + temp_content.length);
			for (int i = 0; i < temp_content.length; i++) {
				buffer.append(temp_content[i] + "\n");
			}
			return buffer.toString();
		} else {
			return content;
		}
	}

	

	public static PackageInfo getPackageInfo(Context context, String packagename) {
		try {
			PackageManager manager = context.getPackageManager();
			return manager.getPackageInfo(packagename, 0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断SD卡存在
	 * 
	 * @return
	 */
	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

	

	public static boolean checkNetworkIsActive(Context context) {
		boolean mIsNetworkUp = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			mIsNetworkUp = info.isAvailable();
		}
		return mIsNetworkUp;
	}


	public static void startDownloadService(Context context,String webpath,String packagename) {
		Util.print("start download service", "start");
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra("path", webpath);
		intent.putExtra("package", packagename);
		context.startService(intent);
	}

	

	
	/*
	 * @file 文件对象 根据文件对象 调用系统安装包，安装当前软件
	 */
	public static void installFile(File file, Context context) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = Util.getMIMEType(file);
		Util.print("filepath", file.getAbsolutePath());
		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
	}

	public static DownloadManagerThread startDownload(Context context,final String downloadurl,String packagename) {

		DownloadManagerThread download = new DownloadManagerThread(context,downloadurl,packagename);
		
		Util.getDownloadThreadPool().execute(download);
		
		return download;

	}
	
	
	/**
	  * 获取图片流
	  *
	  * @param uri 图片地址

	  * @return
	  * @throws MalformedURLException
	  */
	 public static InputStream GetImageByUrl(String uri) throws MalformedURLException {
	  URL url = new URL(uri);
	  URLConnection conn;
	  InputStream is;
	  try {
	   conn = url.openConnection();
	   conn.connect();
	   is = conn.getInputStream();

	   return is;
	  } catch (Exception e) {
	   e.printStackTrace();
	  }

	  return null;
	 }

	 

	 
	/**
	 * 获取Bitmap
	 * 
	 * 
	 * @param uri
	 *            图片地址
	 * @return
	 */
	public static Bitmap GetBitmapByUrl(String uri) {

		Bitmap bitmap;
		InputStream is;
		try {
			is = GetImageByUrl(uri);
			if(is!=null){
				
//				BitmapFactory.Options options=new BitmapFactory.Options();
//	            options.inSampleSize = 5;
//	            bitmap = BitmapFactory.decodeStream(is,null,options); 
				
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
				return bitmap;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	 
	public static void checkUpdate(final Handler handler,final Context context,final int sleepTime) {
		
		//判断当前网络是已经连接上了才发送数据到服务器
		if(!getNetworkState(context)){
			return;   
		}
		
		new Thread(new Runnable() {

			public void run() {
				try {
					
					 
	                String packagename = context.getPackageName();
	                String version = "";
	                
	                version = context.getPackageManager().getPackageInfo(
	        				packagename, 0).versionName; 
	                
	                //version="0.0";
	                
	                String path = SendMacToServiceAndUpdatePath + "packagename=" + 
	                                 packagename +"&version=" + version;
	                Log.v("MainUI", ">>>>>"+path);
	                DataSet dataSet = getSoftwareWebData(path);
	                
	                UpdateData updateData = dataSet.updateData;
	                Log.v("MainUI", ">>>>>111"+updateData.getUrl());
	                
	                
	                Thread.sleep(sleepTime);
	                if(updateData!=null){
//		                Log.v("MainUI", "urlpath>>>>"+urlpath);
		                if(updateData.getUrl()!=null && !updateData.getUrl().equals("")) 
		                {
		                	Message message = new Message();
		                	message.what = MainUI.UPDATE_LAUNCHER;
		                	
		                	Bundle bundle = new Bundle();
		                	bundle.putString("url", updateData.getUrl());
		                	bundle.putString("description", updateData.getDescription());
		                	message.setData(bundle);
		                	
		                	handler.sendMessage(message);
		                }else {
							handler.sendEmptyMessage(MainUI.LAUNCHER_IS_LATEST);
						}
	                }

					     

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void sendMacToService(final Context context,final int sleepTime) {
		
		//判断当前网络是已经连接上了才发送数据到服务器
		if(!getNetworkState(context)){
			return;   
		}
		
		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(sleepTime);
					
					String[] key = { "mac","device","systemver"};
	                String macString="";
	                if(isWiFiActive(context)){
	                	macString = getWifiMacAddress(context);
	                }else {
	                	macString = getEthernetMacAddress();
					}
	                String device = android.os.Build.MODEL+"";  
	                String systemver = android.os.Build.VERSION.RELEASE+"";   
	                String path = SendMacToServiceAndUpdatePath+"mac="+macString+"&device="+device+"&systemver="+systemver;
	                if(macString!=null && !macString.equals(""))
	                {
	                	String[] value = {macString,device,systemver};
//						CommitDownloadNum(SendMacToServiceAndUpdatePath, key, value);
	                	commitCount(path);
	                	
	                }
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void commitCount(final String webpath){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(webpath);
					URLConnection connection = url.openConnection();
					connection.connect();
					
					InputStream inputStream = connection.getInputStream();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
	
	 
	/*
	 * @keyword 关键字
	 * 
	 * @value 关键值 提交下载数据到服务器
	 */
	public static void CommitDownloadNum(final String webpath,
			final String[] keyword, final String[] value) {
		new Thread(new Runnable() {

			public void run() {
				try {

					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httpost = new HttpPost(webpath);
					httpost.getParams().setBooleanParameter(
							CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					for (int i = 0; i < keyword.length; i++) {
						nvps.add(new BasicNameValuePair(keyword[i], value[i]));
					}
//					Log.d("MyDebug", nvps + webpath);
					httpost.setEntity(new UrlEncodedFormEntity(nvps, "gb2312"));
					httpclient.execute(httpost);

				} catch (Exception e) {
					e.printStackTrace();
					Util.print("commit download num error",
							"commit download num error");
				}
			}
		}).start();
	}
	
	
	public static String loadFileAsString(String filePath) throws java.io.IOException{
	    StringBuffer fileData = new StringBuffer(1000);
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    char[] buf = new char[1024];
	    int numRead=0;
	    while((numRead=reader.read(buf)) != -1){
	        String readData = String.valueOf(buf, 0, numRead);
	        fileData.append(readData);
	    }
	    reader.close();
	    return fileData.toString();
	}

	/*
	* Get the STB MacAddress
	*/
	public static String getEthernetMacAddress(){
	    try {
	        return loadFileAsString("/sys/class/net/eth0/address")
	            .toUpperCase().substring(0, 17);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	public static String getWifiMacAddress(Context context){
		String macAddress = null, ip = null;  
		WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());  
		if (null != info) {  
		    macAddress = info.getMacAddress().toUpperCase();  
		    ip = int2ip(info.getIpAddress());  
		}  
		return "" + macAddress;
	}
	
	public static long ip2int(String ip) {  
	    String[] items = ip.split("\\.");  
	    return Long.valueOf(items[0]) << 24  
	            | Long.valueOf(items[1]) << 16  
	            | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);  
	} 

	public static String int2ip(long ipInt) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(ipInt & 0xFF).append(".");  
        sb.append((ipInt >> 8) & 0xFF).append(".");  
        sb.append((ipInt >> 16) & 0xFF).append(".");  
        sb.append((ipInt >> 24) & 0xFF);  
        return sb.toString();  
    } 
	
	
	
	  public static String getMIMEType(File f,boolean isOpen){
		    String type="";
		    String fName=f.getName();
		    String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
		    if(isOpen){
		            if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
		              type = "audio"; 
		            }else if(end.equals("3gp")||end.equals("mp4")){
		              type = "video";
		            }
		            else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||end.equals("jpeg")||end.equals("bmp")){
		              type = "image";
		            }
		            else{
		              type="*";
		            }
		            type += "/*"; 
		    }else{
		          if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
		            type = "audio"; 
		          }else if(end.equals("3gp")||end.equals("mp4")){
		            type = "video";
		          }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||end.equals("jpeg")||end.equals("bmp")){
		            type = "image";
		          }else if(end.equals("apk")){
		            type = "apk";
		          }
		    }
		    return type; 
		  }
		  
		  /**
		   * bitmap scale 
		   * @param bitMap
		   * @param x
		   * @param y
		   * @param newWidth
		   * @param newHeight
		   * @param matrix
		   * @param isScale
		   * @return
		   */
		  public static Bitmap fitSizePic(File f){ 
		    Bitmap resizeBmp = null;
		    BitmapFactory.Options opts = new BitmapFactory.Options(); 
		    if(f.length()<20480){         //0-20k
		      opts.inSampleSize = 1;
		    }else if(f.length()<51200){   //20-50k
		      opts.inSampleSize = 2;
		    }else if(f.length()<307200){  //50-300k
		      opts.inSampleSize = 4;
		    }else if(f.length()<819200){  //300-800k
		      opts.inSampleSize = 6;
		    }else if(f.length()<1048576){ //800-1024k
		      opts.inSampleSize = 8;
		    }else{
		      opts.inSampleSize = 10;
		    }
		    resizeBmp = BitmapFactory.decodeFile(f.getPath(),opts);
		    return resizeBmp; 
		  }

		  /**
		   * file size
		   * @param f
		   * @return
		   */
		  public static String  fileSizeMsg(File f){ 
		    int sub_index = 0;
		    String  show = "";
		    if(f.isFile()){
		          long length = f.length();
		          if(length>=1073741824){
		            sub_index = (String.valueOf((float)length/1073741824)).indexOf(".");
		            show = ((float)length/1073741824+"000").substring(0,sub_index+3)+"GB";
		          }else if(length>=1048576){
		            sub_index = (String.valueOf((float)length/1048576)).indexOf(".");
		            show =((float)length/1048576+"000").substring(0,sub_index+3)+"MB";
		          }else if(length>=1024){
		            sub_index = (String.valueOf((float)length/1024)).indexOf(".");
		            show = ((float)length/1024+"000").substring(0,sub_index+3)+"KB";
		          }else if(length<1024){
		            show = String.valueOf(length)+"B";
		          }
		    }
		    return show; 
		  }
		  
		  /**
		   * 
		   * @param newName
		   * @return 
		   */
		  public static boolean checkDirPath(String newName){
		    boolean ret = false;
		    if(newName.indexOf("\\")==-1){
		      ret = true;
		    }
		    return ret;
		  }
		  
		  /**
		   *  
		   * @param newName
		   * @return
		   */
		  public static boolean checkFilePath(String newName){
		    boolean ret = false;
		    if(newName.indexOf("\\")==-1){
		      ret = true;
		    }
		    return ret;
		  }
		  

		  
		  public static Bitmap getWallpaperFromFile(File f){ 
		    Bitmap resizeBmp = null;
		    BitmapFactory.Options opts = new BitmapFactory.Options(); 
		    if(f.length()<614400 && f.length()>=409600){  //400-600k
		      opts.inSampleSize = 2;
		    }else if(f.length()<1048576 && f.length()>=614400){ //600-1024k
		      opts.inSampleSize = 3;
		    }else{
		      opts.inSampleSize = 5;
		    }
		    resizeBmp = BitmapFactory.decodeFile(f.getPath(),opts);
		    return resizeBmp; 
		  }
}
