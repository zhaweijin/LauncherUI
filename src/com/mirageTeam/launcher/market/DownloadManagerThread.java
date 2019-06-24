package com.mirageteam.launcher.market;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class DownloadManagerThread extends Thread {
	public String name;
	public  boolean isrun;
	public String downloadUrl;
	
	public String downloadApkDir = "/mnt/sdcard/launcher_market/download/apk/";
	public String apkFilePath;

	private String TAG = "DownloadManagerThread";
	private Context context;
	private String packagername;
	
	private DownloadDatabase downloadDatabase;
	
	private BufferedRandomAccessFile bufferedRandomAccessFile;
	private BufferedInputStream bis;
	
	private int count=0;
	private int currentDownloadNum = 0;
	
	public DownloadManagerThread(Context context,String downloadUrl,String packagename) {
		this.context = context;
		this.downloadUrl = downloadUrl;
		this.packagername = packagename;
		downloadDatabase = Util.getDownloadDatabase(context);
	}

	@Override
	public void run() {
		super.run();

		startDownload();
	}
	
	
	public void startDownload() {
		try {
			
            
			// 连接网络，设置网络参数
			String downpathString = Util.toUtf8String(downloadUrl);
			Util.print(TAG, "aaaaaaa,url="+downpathString);
            updateDownloadNumThread();
			URL downUrl = new URL(downpathString);
			HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(8*1000);

			InputStream inStream = http.getInputStream();
			long length = Long.parseLong(http.getHeaderField("Content-Length"));
 
			 
			// 创建文件夹
			File sdcardFile = new File(downloadApkDir);
			if (!sdcardFile.exists()) {
				sdcardFile.mkdirs();
			}
			 
			String[] fileName = Util.fileName(downloadUrl);
			if(fileName[0].equals("zip")){
				name = fileName[1] + "." + "apk";
			}else {
				name = fileName[1] + "." + fileName[0];
			}
			
			apkFilePath = downloadApkDir + name;
			Util.print("downlaod", "apk path="+apkFilePath);

			File tempFile = new File(apkFilePath);
			if (!tempFile.exists())
				tempFile.createNewFile();
			
			downloadDatabase.updateFilepath(apkFilePath, packagername);//更新下载文件路径，方便下载界面，判断是否显示未安装
			
			
			
			bufferedRandomAccessFile = new BufferedRandomAccessFile(tempFile,"rwd");
			bis = new BufferedInputStream(inStream, 1024);
			if (bis != null) {
				byte[] buf = new byte[8 * 1024];
				int ch = -1;

				while ((ch = bis.read(buf)) != -1) {
					bufferedRandomAccessFile.write(buf, 0, ch);
					count += ch;
					currentDownloadNum = (int) ((count / (float) length) * 100);
					Util.print(TAG, ""+currentDownloadNum);
				}
				bis.close();
				bufferedRandomAccessFile.close();
				Util.print(TAG, "download finished");
			}
			
			
            //for wait install apk
			Util.installFile(tempFile, context);
		} catch (Exception e) {
			Util.print(TAG, " download error");
			try {
				bis.close();
				bufferedRandomAccessFile.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			currentDownloadNum = -1;
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	private void updateDownloadNumThread(){
		new Thread(new Runnable() {

			public void run() {
				try {
					while (true) {
                        if (currentDownloadNum !=-1 && currentDownloadNum < 100)// 
						{
                        	downloadDatabase.updateDownloadNum(packagername, currentDownloadNum);
							Thread.sleep(1000);
						} else if (currentDownloadNum !=-1 && currentDownloadNum == 100)
						{
							downloadDatabase.updateDownloadNum(packagername, currentDownloadNum);
							break;
						}else if(currentDownloadNum == -1){
							downloadDatabase.deleteByPackageData(packagername);
							break;
						}
                        
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
