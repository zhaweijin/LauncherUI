package com.mirageteam.launcher.market;

 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/*
 * @author zhaweijin
 * 通知广播，针对软件包的安装监听
 */
public class ReceivePackageManage extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")
					|| intent.getAction().equals(
							"android.intent.action.PACKAGE_REPLACED")) {
				String packagename = intent.getDataString();
				packagename = packagename.substring(
						packagename.indexOf(":") + 1, packagename.length());
				Util.print("name", packagename);
				DownloadDatabase downloadDatabase = Util.getDownloadDatabase(context);
				
				 

				if (packagename != "")  
				{
					Cursor cursor = downloadDatabase.queryByPackage(packagename);
					int size = cursor.getCount();
					Util.print("o", "" + size);
					cursor.moveToFirst();
					if (size != 0) {
						downloadDatabase.deleteByPackageData(packagename);
					}
					cursor.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
