package com.mirageteam.launcher.setting;

public class StorageItem {

	private int name;
	private int color;
	private int order;
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
    StorageItem(int name,int color,int order){
		this.name = name;
		this.color = color;
		this.order = order;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
    
    
	
}
