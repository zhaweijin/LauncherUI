package com.mirageTeam.store.model;

import java.io.Serializable;

import android.app.ActivityManager.RecentTaskInfo;
import android.content.pm.ResolveInfo;

public class TaskInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 375653443738345196L;

	private RecentTaskInfo recentTask;

	private ResolveInfo resoveInfo;

	public RecentTaskInfo getRecentTask() {
		return recentTask;
	}

	public void setRecentTask(RecentTaskInfo recentTask) {
		this.recentTask = recentTask;
	}

	public ResolveInfo getResoveInfo() {
		return resoveInfo;
	}

	public void setResoveInfo(ResolveInfo resoveInfo) {
		this.resoveInfo = resoveInfo;
	}
	
	
	
	
}
