package com.mirageTeam.common;

import android.util.Log;
import android.content.Context;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class GetWeather { 
	private static final String TAG = "GetWeather";

	static String weather = null; 
	static String weather2 = null; 
	static String high = null; 
	static String low = null;  
	static String wind = null;

	private Context mContext = null;
	private String latitude = null;
	private String longitude = null;

	public  GetWeather(Context context){
		mContext = context;	
	}

	public void getweather(String name, String day)  
	{   
		URL ur;  

		try {  

			DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();  
			DocumentBuilder dombuilder=domfac.newDocumentBuilder();
			Document doc;
			Element root;
			NodeList books;
	
			ur = new URL("http://php.weather.sina.com.cn/xml.php?city="+URLEncoder.encode(name,"gb2312")+"&password=DJOYnieT8234jlsK&day="+ day);

			//Log.d("Weather", "    " + URLEncoder.encode(name,"gb2312"));

			doc = dombuilder.parse(ur.openStream()); 
			root=doc.getDocumentElement();
			books=root.getChildNodes();
			if (books==null || books.item(1)==null){
				 weather = null; 
				 weather2 = null; 
				 high = null; 
				 low = null;  
				 wind = null;
				 
				 return;
			}

			for(Node node=books.item(1).getFirstChild();node!=null;node=node.getNextSibling()){
				if(node.getNodeType()==Node.ELEMENT_NODE){
					if(node.getNodeName().equals("status1"))  weather=node.getTextContent(); 
					else if(node.getNodeName().equals("status2"))  weather2=node.getTextContent(); 
					else if(node.getNodeName().equals("temperature1"))  high=node.getTextContent(); 
					else if(node.getNodeName().equals("temperature2"))  low=node.getTextContent();  
					else if(node.getNodeName().equals("direction1"))  wind=node.getTextContent();  
				}
			}
			//	Log.d("Weather", " "+weather+"/"+weather2 + "  "+low+ "/"+high + "  " + wind); 
	

		}catch(Exception e){

			e.printStackTrace();
		}
	}   

	public String getCity(String str_province, String str_city, String str_area, String str_come_from) {

		URL url;
		URLConnection conn = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String str = "";

		try {
			url = new URL("http://iframe.ip138.com/ic.asp");
			conn = url.openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is,"gb2312");
			br = new BufferedReader(isr);
			String input = "";
			while ((input = br.readLine()) != null) {
				str += input;
			}

			String content = new String(str.getBytes("UTF-8"),"UTF-8");
			
			String mCity = null;
			if (content.indexOf(str_province)>=0 && content.indexOf(str_city)>=0)
				mCity = content.substring(content.indexOf(str_province) + 1, content.indexOf(str_city));
			else 
				mCity = null;
		
			if (mCity == null){
				if (content.indexOf(str_area)>=0 && content.indexOf(str_city)>=0)
					mCity = content.substring(content.indexOf(str_area) + 1, content.indexOf(str_city));
				else 
					mCity = null;
			}
		
			if (mCity == null){
				if (content.indexOf(str_come_from)>=0 && content.indexOf(str_city)>=0)
					mCity = content.substring(content.indexOf(str_come_from) + 2, content.indexOf(str_city));
				else 
					mCity = null;
			}
		
			if (mCity == null){
				mCity = getLocation();
			}

			Log.d(TAG, "   " + "  @@" + mCity + " ##" + content);


			return mCity;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getLocation() {    //for globe locating

		URL url;
		URLConnection conn = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String str = "";

		try {
			url = new URL("http://j.maxmind.com/app/geoip.js");
			conn = url.openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is,"gb2312");
			br = new BufferedReader(isr);
			String input = "";
			while ((input = br.readLine()) != null) {
				str += input;
			}

			String content = new String(str.getBytes("UTF-8"),"UTF-8");



			String [] list = content.split("'");

			for (int i = 0; i < list.length; i++) {

				Log.d(TAG, "   " + i + " = " + list[i]);
			}		

			latitude = list[11];
			longitude = list[13];

			return list[5];

		} catch (Exception e) {
			e.printStackTrace();
		}	

		return null;
	}   

	public String getWeatherStatus(){

		return weather;
	}

	public String getWeatherStatus2(){

		return weather2;
	}

	public boolean isWeatherWillChange(){
		if(weather == null) {
			return false;
		}
		return !weather.equals(weather2);
	}

	public String getLowTem(){

		return low;
	}

	public String getHighTem(){

		return high;
	}

	public String getWindDirection(){

		return wind;
	}


}  

