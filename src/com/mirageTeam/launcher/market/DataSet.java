package com.mirageteam.launcher.market;

import java.util.ArrayList;



/**
 * @author carter
 * @function 存储网络解析数据的集
 */
public class DataSet {


	public static int currentListSize = 0;

	private ArrayList<MarketData> marketDatas = new ArrayList<MarketData>();
	private ArrayList<String> topImages = new ArrayList<String>();
	
	private ArrayList<FenleiData> fenleiDatas = new ArrayList<FenleiData>(); 

	
	public String updateDownloadPath="";
	
	public UpdateData updateData = new UpdateData();
	
	public DataSet() {

	}

	
   
 
	
	
	public ArrayList<MarketData> getMarketDatas() {
		return marketDatas;
	}

	public MarketData getMarketData() {
		return marketDatas.get(marketDatas.size() - 1);
	}


	public ArrayList<String> getTopImages() {
		return topImages;
	}


	public void setTopImages(String topImage) {
		topImages.add(topImage);
	}

	
	public ArrayList<FenleiData> getFenleiDatas() {
		return fenleiDatas;
	}

	public FenleiData getFenleiData() {
		return fenleiDatas.get(fenleiDatas.size() - 1);
	}

	
}
