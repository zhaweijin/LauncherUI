package com.mirageTeam.business;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.jsonHelper.DisplayCallback;
import com.mirageTeam.service.ApplicationCategoryService;
import com.mirageTeam.store.model.TaskInfo;

public class ApplicationCategoryBusiness {

	/**
	 * 保存应用到指定类别成功
	 */
	public static final int SAVE_CATEGORY_TO_DB_SUCCESS = 1;

	/**
	 * 保存应用到指定类别失败
	 */
	public static final int SAVE_CATEGORY_TO_DB_ERROR = 2;

	/**
	 * 得到分类成功
	 */
	public static final int GET_CATEGORY_LIST_SUCCES = 3;
	/**
	 * 得到分类失败
	 */
	public static final int GET_CATEGORY_LIST_ERROR = 4;

	/**
	 * 删除类别成功
	 */
	public static final int DELETE_CATEGORY_BY_SYSTEM_REMOVED_SUCCESS = 5;
	/**
	 * 删除类别失败
	 */
	public static final int DELETE_CATEGORY_BY_SYSTEM_REMOVED_ERROR = 6;

	/**
	 * 根据类别修改应用
	 */
	public static final int UPDATE_CATEGORY_BY_ACTIVITY_NAME = 7;

	/**
	 * 根据类别修改应用失败
	 */
	public static final int UPDATE_CATEGORY_BY_ACTIVITY_NAME_ERROR = 8;

	public static final int GET_APPLICATION_ARRAYLIST = 9;

	public static final int GET_DEFAULT_APP_INFO_SUCCESS = 10;
	public static final int GET_DEFAULT_APP_INFO_ERROR = 11;

	public void saveCategory(final DisplayCallback callback, final Context context, final CategoryModel model) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					Object isSuccess = ApplicationCategoryService.getInstance().saveCategory(context, model);
					key = SAVE_CATEGORY_TO_DB_SUCCESS;
					callback.displayResult(key, isSuccess);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					key = SAVE_CATEGORY_TO_DB_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void getGategoryModesArray(final DisplayCallback callback, final Context context, final int type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					ArrayList<CategoryModel> array = ApplicationCategoryService.getInstance().getGategoryModesArray(context, type);
					key = GET_CATEGORY_LIST_SUCCES;
					callback.displayResult(key, array);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					key = GET_DEFAULT_APP_INFO_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void getDefaultAppInfo(final DisplayCallback callback, final Context context, final int type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					CategoryModel mode = ApplicationCategoryService.getInstance().getDefaultAppByType(context, type);
					key = GET_DEFAULT_APP_INFO_SUCCESS;
					callback.displayResult(key, mode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					key = GET_CATEGORY_LIST_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void deleteByPackageName(final DisplayCallback callback, final Context context, final String packageName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					boolean isSuccess = ApplicationCategoryService.getInstance().deleteByPackageName(context, packageName);
					key = DELETE_CATEGORY_BY_SYSTEM_REMOVED_SUCCESS;
					callback.displayResult(key, isSuccess);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					key = DELETE_CATEGORY_BY_SYSTEM_REMOVED_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void updateCategoryByActivityName(final DisplayCallback callback, final Context context, final CategoryModel model, final String activityName,
			final int type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					boolean isSuccess = ApplicationCategoryService.getInstance().updateCategoryByActivityName(context, model, activityName, type);
					key = UPDATE_CATEGORY_BY_ACTIVITY_NAME;
					callback.displayResult(key, isSuccess);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					key = UPDATE_CATEGORY_BY_ACTIVITY_NAME_ERROR;
					callback.displayResult(key, "失败");
				}

			}
		}).start();
	}

	public void getApplicationInfos(final DisplayCallback callback, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key = -1;
				try {
					ArrayList<TaskInfo> infos = ApplicationCategoryService.getInstance().getApplicationInfos(context);
					key = GET_APPLICATION_ARRAYLIST;
					callback.displayResult(key, infos);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

}
