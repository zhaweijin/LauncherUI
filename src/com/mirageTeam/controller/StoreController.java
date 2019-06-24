package com.mirageTeam.controller;

import com.mirageTeam.business.StoreBusiness;
import com.mirageTeam.jsonHelper.DisplayCallback;

import android.os.Handler;
import android.os.Message;

/**
 * 控制器
 * @author terry
 *
 */
public class StoreController {

	private Handler mHandler;
	
	private StoreBusiness business=new StoreBusiness();
	
	public StoreController(Handler handler){
		this.mHandler=handler;
	}
	
	private DisplayCallback callback=new DisplayCallback() {
		
		@Override
		public void displayResult(int key, Object... param) {
			// TODO Auto-generated method stub
			switch (key) {
			case StoreBusiness.LOAD_STORE_LIST_SUCCESS:
			case StoreBusiness.LOAD_STORE_LIST_FAIL:
			case StoreBusiness.LOAD_STORE_LIST_TIMEOUT:
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
	 * 获取商城列表
	 * @param request
	 */
	public void getStoreList(String request){
		business.getStoreList(callback, request);
	}
}
