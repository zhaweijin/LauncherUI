package com.mirageTeam.business;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.mirageTeam.jsonHelper.DisplayCallback;
import com.mirageTeam.service.StoreService;
import com.mirageTeam.store.model.Store;

/**
 * 业务类
 * @author terry
 *
 */
public class StoreBusiness {

	/**
	 * 加载商城列表成功状态
	 */
	public static final int LOAD_STORE_LIST_SUCCESS=1;
	/**
	 * 加载商城列表成功失败
	 */
	public static final int LOAD_STORE_LIST_FAIL=2;
	
	/**
	 * 加载商城列表成功超时
	 */
	public static final int LOAD_STORE_LIST_TIMEOUT=3;
	
	public void getStoreList(final DisplayCallback callback,final String request){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int key=-1;
				try {
					ArrayList<Store> stores=StoreService.getInstance().getStoreList(request);
					key=LOAD_STORE_LIST_SUCCESS;
					callback.displayResult(key, stores);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					key=LOAD_STORE_LIST_FAIL;
					callback.displayResult(key, "失败");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					key=LOAD_STORE_LIST_FAIL;
					callback.displayResult(key, "超时");
				}
				
			}
		}).start();
	}
	
}
