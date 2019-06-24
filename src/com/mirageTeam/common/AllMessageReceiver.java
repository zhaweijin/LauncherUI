package com.mirageTeam.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class AllMessageReceiver extends BroadcastReceiver {

	public static final int BROADCAST_RECEIVER = 1;
	/**
	 * 应用程序被删除
	 */
	public static final int PACKAGE_REMOVED = 2;
	
	public static final int PACKAGE_ADDED=3;
	
	public static final int PACKAGE_CHANGED=4;

	private Handler handler;

	public AllMessageReceiver(Handler mHandler) {
		handler = mHandler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AllMessageAction.BROADCAST_ACTION)) {
			handlerCallback(BROADCAST_RECEIVER, intent);
		} else if (intent.getAction().equals(AllMessageAction.PACKAGE_REMOVED)) { 
			handlerCallback(PACKAGE_REMOVED, intent);
		}else if(intent.getAction().equals(AllMessageAction.PACKAGE_ADDED)){
			handlerCallback(PACKAGE_ADDED, intent);
		}else if (intent.getAction().equals(AllMessageAction.PACKAGE_CHANGED)){
			handlerCallback(PACKAGE_CHANGED, intent);
		}

	}

	private void handlerCallback(int key, Object result) {
		Message msg = handler.obtainMessage();
		msg.what = key;
		msg.obj = result;
		msg.sendToTarget();
	}

}
