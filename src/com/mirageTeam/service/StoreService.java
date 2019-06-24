package com.mirageTeam.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.mirageTeam.launcherui.LauncherUIApplication;
import com.mirageTeam.store.model.Store;

/**
 * 服务
 * @author terry
 *
 */
public class StoreService {

	private StoreService(){
		
	}
	
	private static class SingleTonHolder{
		private static StoreService INSTANCE=new StoreService();
	}
	
	public static StoreService getInstance(){
		return SingleTonHolder.INSTANCE;
	}
	
	public ArrayList<Store> getStoreList(String request)throws IOException,JSONException{
		
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("", request);
		return LauncherUIApplication.getInstance().getApiServer().getStoreLIst(params);
	}
}
