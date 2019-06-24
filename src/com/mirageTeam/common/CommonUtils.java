package com.mirageTeam.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.store.model.TaskInfo;
import com.mirageTeam.weather.bean.Weather;

public class CommonUtils {

	public static boolean checkNetwork(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}

	public static String readTextFile(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
		}
		return outputStream.toString();
	}

	public static int transferEnumToInt(ApplicationCategoryType type) {
		if (type == ApplicationCategoryType.Browser) {
			return 1;
		} else if (type == ApplicationCategoryType.Movie) {
			return 2;
		} else if (type == ApplicationCategoryType.TV) {
			return 3;
		} else if (type == ApplicationCategoryType.FAST_SHORTCUT) {
			return 4;
		} else {
			return 0;
		}

	}

	public static void openApplication(Context context, String packageName, String activityName) {
		Log.i("MainUI", "openApplication()");

		Intent intent = new Intent();
		intent.setComponent(new ComponentName(packageName, activityName));
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(intent);
	}

	/**
	 * 反射删除应用
	 * 
	 * @param am
	 * @param context
	 * @param perid
	 */
	public static void removeTask(ActivityManager am, Context context, int perid) {
		Class<?> classType = ActivityManager.class;
		try {
			Method removeTask = classType.getMethod("removeTask", new Class[] { int.class, int.class });
			try {
				Object result = removeTask.invoke(am, new Object[] { perid, 0x0001 });
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<TaskInfo> getTaskList(Context mContext) {

		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// List<ResolveInfo> recentList = new ArrayList<ResolveInfo>();
		ArrayList<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		try {
			List<RecentTaskInfo> list = am.getRecentTasks(64, 0);
			for (RecentTaskInfo ti : list) {
				Intent intent = ti.baseIntent;
				ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(intent, 0);
				if (resolveInfo == null) {
					continue;
				}
				String pkgName = resolveInfo.activityInfo.packageName;
				if (!pkgName.equals(mContext.getPackageName())) {

					TaskInfo info = new TaskInfo();
					info.setRecentTask(ti);
					info.setResoveInfo(resolveInfo);
					taskInfos.add(info);
				} else {
					continue;
				}
				// if (resolveInfo != null) {
				// recentList.add(resolveInfo);
				// }
			}
		} catch (SecurityException se) {
			se.printStackTrace();
		}
		return taskInfos;
	}

	public static void saveWheatherInfo(Context mContext, Weather weather) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences("weatherInfo", Context.MODE_PRIVATE).edit();
		if (weather.getCityName() != null)
			editor.putString("city", weather.getCityName());
		editor.putInt("cityID", weather.getCityID());
		if (weather.getTemp1() != null)
			editor.putString("temp", weather.getTemp1());
		if (weather.getWeather1() != null)
			editor.putString("weather", weather.getWeather1());
		editor.commit();
	}

	public static Weather getWheater(Context mContext) {
		Weather weather = new Weather();
		SharedPreferences pre = mContext.getSharedPreferences("weatherInfo", Context.MODE_PRIVATE);
		weather.setCityID(pre.getInt("cityID", 101280601));// 默认深圳
		weather.setCityName(pre.getString("city", mContext.getString(R.string.default_city)));
		weather.setTemp1(pre.getString("temp", mContext.getResources().getString(R.string.weihoqu)));
		weather.setWeather1(pre.getString("weather", ""));
		return weather;
	}

	public static HashMap<String, Integer> weatherPicGenerate(Context mContext) {
		HashMap<String, Integer> picsParam = new HashMap<String, Integer>();
		picsParam.put(mContext.getString(R.string.douyun), R.drawable.duoyun);
		picsParam.put(mContext.getString(R.string.dayu), R.drawable.dayu);
		picsParam.put(mContext.getString(R.string.douyunzhuanqin), R.drawable.duoyunzhuanqing);
		picsParam.put(mContext.getString(R.string.qinzhuandouyun), R.drawable.qingzhuanduoyun);
		picsParam.put(mContext.getString(R.string.xiaoyu), R.drawable.xiaoyu);
		picsParam.put(mContext.getString(R.string.yujiaxue), R.drawable.yujiaxue);
		picsParam.put(mContext.getString(R.string.daxue), R.drawable.daxue);
		picsParam.put(mContext.getString(R.string.weifen), R.drawable.weifeng);
		picsParam.put(mContext.getString(R.string.xiaoxue), R.drawable.xiaoxue);
		picsParam.put(mContext.getString(R.string.yin), R.drawable.yin);
		picsParam.put(mContext.getString(R.string.zhenyu), R.drawable.zhenyu);
		picsParam.put(mContext.getString(R.string.weihoqu), R.drawable.weihuoqu);
		picsParam.put(mContext.getString(R.string.qing), R.drawable.qing);
		picsParam.put(mContext.getString(R.string.leizhenyu), R.drawable.leizhenyu);
		picsParam.put(mContext.getString(R.string.douyunzhuangleizhenyu), R.drawable.duoyunzhuanleizhenyu);
		picsParam.put(mContext.getString(R.string.dayuzhuanleizhenyu), R.drawable.dayuzhuanleizhenyu);
		picsParam.put(mContext.getString(R.string.duoyunzhuangxiaoyu), R.drawable.xiaoyu);
		picsParam.put(mContext.getString(R.string.xiaoyuzhuanduoyun), R.drawable.duoyun);
		picsParam.put(mContext.getString(R.string.duoyunzhuanyin), R.drawable.yin);
		picsParam.put(mContext.getString(R.string.zhengyuzhuangduoyun), R.drawable.duoyun);
		picsParam.put(mContext.getString(R.string.yinzhuanqin), R.drawable.qing);
		picsParam.put(mContext.getString(R.string.duoyunzhuanzhengyun), R.drawable.duoyunzhuanleizhenyu);
		picsParam.put(mContext.getString(R.string.zhongyuzhuangxiaoyu), R.drawable.xiaoyu);
		picsParam.put(mContext.getString(R.string.dadaobaoyu), R.drawable.baoyu);
		picsParam.put(mContext.getString(R.string.zhengyuzhuanzhongdaodayu), R.drawable.dayu);
		picsParam.put(mContext.getString(R.string.zhongyu), R.drawable.zhongyu);
		return picsParam;
	}

	public static void saveWallPaperString(Context mContext, int resource) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences("wallpaperUtilInfo", Context.MODE_PRIVATE).edit();
		editor.putInt("wallPaperResource", resource);
		editor.commit();
	}

	public static int getWallPaperString(Context mContext) {
		return mContext.getSharedPreferences("wallpaperUtilInfo", Context.MODE_PRIVATE).getInt("wallPaperResource", R.drawable.bj_moren);
	}

	/*
	 * state boolean true: custom false: resure id
	 */
	public static void saveWallPaperCustomMark(Context mContext, boolean state) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences("wallpaperUtilInfo", Context.MODE_PRIVATE).edit();
		editor.putBoolean("wallPaperCustomMark", state);
		editor.commit();
	}

	public static boolean getWallPaperCustomMark(Context mContext) {
		return mContext.getSharedPreferences("wallpaperUtilInfo", Context.MODE_PRIVATE).getBoolean("wallPaperCustomMark", false);
	}

	public static void saveIsAutoChooseWeather(Context mContext, boolean isAuto) {
		SharedPreferences.Editor editor = mContext.getSharedPreferences("weatherInfo", Context.MODE_PRIVATE).edit();
		editor.putBoolean("isauto", isAuto);
		editor.commit();
	}

	public static boolean isAutoChooseWeather(Context mContext) {
		return mContext.getSharedPreferences("weatherInfo", Context.MODE_PRIVATE).getBoolean("isauto", true);
	}

}
