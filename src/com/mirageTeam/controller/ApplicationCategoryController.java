package com.mirageTeam.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mirageTeam.business.ApplicationCategoryBusiness;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.jsonHelper.DisplayCallback;

public class ApplicationCategoryController {

private Handler mHandler;
	
	private ApplicationCategoryBusiness business=new ApplicationCategoryBusiness();
	
	public ApplicationCategoryController(Handler handler){
		this.mHandler=handler;
	}
	
	private DisplayCallback callback=new DisplayCallback() {
		
		@Override
		public void displayResult(int key, Object... param) {
			// TODO Auto-generated method stub
			switch (key) {
			case ApplicationCategoryBusiness.SAVE_CATEGORY_TO_DB_SUCCESS:
			case ApplicationCategoryBusiness.SAVE_CATEGORY_TO_DB_ERROR: 
			case ApplicationCategoryBusiness.GET_CATEGORY_LIST_ERROR:
			case ApplicationCategoryBusiness.GET_CATEGORY_LIST_SUCCES:
			case ApplicationCategoryBusiness.DELETE_CATEGORY_BY_SYSTEM_REMOVED_ERROR:
			case ApplicationCategoryBusiness.DELETE_CATEGORY_BY_SYSTEM_REMOVED_SUCCESS:
			case ApplicationCategoryBusiness.UPDATE_CATEGORY_BY_ACTIVITY_NAME:
			case ApplicationCategoryBusiness.UPDATE_CATEGORY_BY_ACTIVITY_NAME_ERROR:
			case ApplicationCategoryBusiness.GET_APPLICATION_ARRAYLIST:
			case ApplicationCategoryBusiness.GET_DEFAULT_APP_INFO_SUCCESS:
			case ApplicationCategoryBusiness.GET_DEFAULT_APP_INFO_ERROR:
				handlerCallback(key,param[0]);
				break;

			default:
				break;
			}
		}
	};
	
	private void handlerCallback(int key,Object result){
		Message msg =mHandler.obtainMessage();
		msg.what=key;
		msg.obj=result;
		msg.sendToTarget();
	}
	
	/**
	 * 保存类别
	 * @param request
	 */
	public void saveCategory(Context context,CategoryModel model){
		business.saveCategory(callback, context, model);
	}
	
	/**
	 * 获取类别
	 * @param request
	 */
	public void getGategoryModesArray(Context context,int type){
		business.getGategoryModesArray(callback, context, type);
	}
	
	/*
	 * 获取分类对应的应用程序
	 * @param request
	 */
	public void getDefaultAppInfo(Context context, int type) {
		business.getDefaultAppInfo(callback, context, type);
	}
	/**
	 * 系统删除时自动删除
	 * @param request
	 */
	public void deleteCategoryBySystemRemoved(Context context,String packagename){
		business.deleteByPackageName(callback, context, packagename);
	}
	
	
	/**
	 * 修改应用类别
	 * @param request
	 */
	public void updateCategoryByActivityName(Context context,CategoryModel model,
			String activityName, int type) {
		business.updateCategoryByActivityName(callback, context, model, activityName, type);
	}
	
	
	/**
	 * 获取应用信息
	 * @param request
	 */
	public void getApplicationInfos(Context context) {
		business.getApplicationInfos(callback, context);
	}
}
