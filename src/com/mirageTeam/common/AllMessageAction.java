package com.mirageTeam.common;

import android.content.Intent;

public class AllMessageAction {

	public static final String BROADCAST_ACTION = "com.mirageTeam.test";

	/**
	 * 应用程序被删除
	 */
	public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";

	/**
	 * 应用安装
	 */
	public static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";

	/**
	 * 应用变化 
	 */
	public static final String PACKAGE_CHANGED = Intent.ACTION_PACKAGE_CHANGED;

}
