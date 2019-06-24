package com.mirageTeam.common;

import android.util.Log;
import android.widget.Toast;

public class DebugFlags {
	public static final boolean DebugFlag = true;
	private static final String TAG = "LauncherUI";

	public static void showLog(String msg) {
		if (DebugFlag) {
			Log.d(TAG, msg);
		}
	}
}
