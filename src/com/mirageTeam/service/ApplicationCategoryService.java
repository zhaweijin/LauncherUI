package com.mirageTeam.service;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.launcherui.LauncherUIApplication;
import com.mirageTeam.store.model.TaskInfo;

public class ApplicationCategoryService {
	private ApplicationCategoryService() {

	}

	private static class SingleTonHolder {
		private static ApplicationCategoryService INSTANCE = new ApplicationCategoryService();
	}

	public static ApplicationCategoryService getInstance() {
		return SingleTonHolder.INSTANCE;
	}

	public Object saveCategory(Context context, CategoryModel model) throws IOException, JSONException {

		return LauncherUIApplication.getInstance().getApiServer().saveCategory(context, model);
	}

	public ArrayList<CategoryModel> getGategoryModesArray(Context context, int type) throws IOException, JSONException {

		return LauncherUIApplication.getInstance().getApiServer().getGategoryModesArray(context, type);
	}

	public CategoryModel getDefaultAppByType(Context context, int type) throws IOException, JSONException {
		return LauncherUIApplication.getInstance().getApiServer().getDefaultAppByType(context, type);
	}

	public boolean deleteByPackageName(Context context, String packageName) throws IOException, JSONException {

		return LauncherUIApplication.getInstance().getApiServer().deleteCategoryByPackageName(context, packageName);
	}

	public boolean updateCategoryByActivityName(Context context, CategoryModel model, String activityName, int type) throws IOException, JSONException {

		return LauncherUIApplication.getInstance().getApiServer().updateCategoryByActivityName(context, model, activityName, type);
	}

	public ArrayList<TaskInfo> getApplicationInfos(Context context) {
		return LauncherUIApplication.getInstance().getApiServer().loadApplicationInfo(context);
	}
}
