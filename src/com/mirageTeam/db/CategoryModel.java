package com.mirageTeam.db;

import java.io.Serializable;

public class CategoryModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4328234770038902079L;

	private String packageName;

	private String activityName;

	private int type;

	private int iconRes;

	private String labelRes;

	private int operation_type;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

	public String getLabelRes() {
		return labelRes;
	}

	public void setLabel(String label) {
		this.labelRes = label;
	}

	public void setOpType(int opType) {
		this.operation_type = opType;
	}

	public int getOpType() {
		return operation_type;
	}

}
