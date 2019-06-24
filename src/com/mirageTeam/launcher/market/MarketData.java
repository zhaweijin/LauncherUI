package com.mirageteam.launcher.market;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MarketData implements Parcelable{

	private final static String TAG = "MarketData";
	
	private String title;
	private String message;
	private String imageUrl;
	private String installUrl;
	private String introduceImagesUrl;
	private String packagename;
	private String version;
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getInstallUrl() {
		return installUrl;
	}
	public void setInstallUrl(String installUrl) {
		this.installUrl = installUrl;
	}
	public String getIntroduceImagesUrl() {
		return introduceImagesUrl;
	}
	public void setIntroduceImagesUrl(String introduceImagesUrl) {
		this.introduceImagesUrl = introduceImagesUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	public static final Parcelable.Creator<MarketData> CREATOR = new Creator<MarketData>() {  
		@Override  
		public MarketData createFromParcel(Parcel source) {  
		Log.d(TAG,"createFromParcel");  
		MarketData marketData = new MarketData();  
		marketData.title = source.readString();  
		marketData.message = source.readString();  
		marketData.imageUrl = source.readString();
		marketData.installUrl = source.readString();
		marketData.introduceImagesUrl = source.readString();
		marketData.packagename = source.readString();
		marketData.version = source.readString();
		return marketData;  
		}  
		@Override  
		public MarketData[] newArray(int size) {  
		// TODO Auto-generated method stub  
		return new MarketData[size];  
		}  
		};  
		@Override  
		public int describeContents() {  
		// TODO Auto-generated method stub  
		Log.d(TAG,"describeContents");  
		return 0;  
		}  
		@Override  
		public void writeToParcel(Parcel dest, int flags) {  
		// TODO Auto-generated method stub  
		Log.d(TAG,"writeToParcel");  
		dest.writeString(title);  
		dest.writeString(message);  
		dest.writeString(imageUrl);
		dest.writeString(installUrl);
		dest.writeString(introduceImagesUrl);
		dest.writeString(packagename);
		dest.writeString(version);
		}  
	
}
