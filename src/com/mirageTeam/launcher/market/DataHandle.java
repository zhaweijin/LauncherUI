package com.mirageteam.launcher.market;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;



/**
 * @author zhaweijin
 * @function 网络数据的解析 --->软件、游戏
 */
public class DataHandle extends DefaultHandler {

	private DataSet dataSet;

	private boolean updateUrl = false;
	@Override
	public void startDocument() throws SAXException {
		dataSet = new DataSet();
	}

	public DataSet getDataSet() {
		return this.dataSet;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			
			if(localName.equals("content")){
				dataSet.getMarketDatas().add(new MarketData());
				dataSet.getMarketData().setTitle(attributes.getValue("title"));
				dataSet.getMarketData().setInstallUrl(attributes.getValue("url"));
				dataSet.getMarketData().setImageUrl(attributes.getValue("thumb"));
				dataSet.getMarketData().setMessage(attributes.getValue("description"));
				dataSet.getMarketData().setIntroduceImagesUrl(attributes.getValue("picture"));
				dataSet.getMarketData().setPackagename(attributes.getValue("packagename"));
				dataSet.getMarketData().setVersion(attributes.getValue("version"));
			}else if(localName.equals("adimage")){
//				Util.print("ssssss", attributes.getValue("adurl"));
				dataSet.setTopImages(attributes.getValue("adurl"));
			}else if(localName.equals("subject")){
				dataSet.getFenleiDatas().add(new FenleiData());
				dataSet.getFenleiData().setName(attributes.getValue("name"));
				dataSet.getFenleiData().setId(attributes.getValue("id"));
			}else if(localName.equals("package")){
				dataSet.updateData.setUrl(attributes.getValue("url"));
				dataSet.updateData.setDescription(attributes.getValue("description"));
				dataSet.updateData.setVersion(attributes.getValue("version"));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
	         throws SAXException {
		
//		if(updateUrl){
//			String data = new String(ch, start, length);
//            dataSet.updateDownloadPath = data;
//			updateUrl = false;
//		}
			
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

}
