/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mirageteam.launcher.setting;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;

//import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

public class OutputSetConfirm extends Activity {
        private static final String TAG = "OutputSetConfirm";

        /** If there is output mode option, use this. */
        private int sel_index;
	private AlertDialog OutPutSetConfirmDiag=null;
	private Handler mWaitHandler;
	private final static long set_delay = 15*1000;
	private Handler mProgressHandler;
	private int index_entry;
	
	private final String ACTION_OUTPUTMODE_CHANGE = "android.intent.action.OUTPUTMODE_CHANGE";
	private final String ACTION_OUTPUTMODE_CANCEL = "android.intent.action.OUTPUTMODE_CANCEL";
	private final String OUTPUT_MODE = "output_mode";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        	super.onCreate(savedInstanceState);
        
        	Intent intent =this.getIntent();
        	String new_mode = intent.getStringExtra("set_mode");
        	
        	Config.Logd(getClass().getName(), "new_mode is: " + new_mode);
        	Log.e(TAG,"----------------------set preview mode:"+new_mode);
        	
        	
        	int cvbs_mode = intent.getIntExtra("cvbs_mode", 0);
        	Log.e(TAG,"----------------------set preview cvbs_mode:"+cvbs_mode);
        	
        	Intent intent_outputmode_change = new Intent(ACTION_OUTPUTMODE_CHANGE);
        	intent_outputmode_change.putExtra(OUTPUT_MODE, new_mode);
        	intent_outputmode_change.putExtra("cvbs_mode", cvbs_mode);
        	
        	OutputSetConfirm.this.sendBroadcast(intent_outputmode_change);
        	showDispmodeSetMsg();  
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

	private class SetconfirmHandler extends Handler {
		@Override
		public void handleMessage(Message msg) 
		   {
		   super.handleMessage(msg);
		   Intent intent_outputmode = new Intent(ACTION_OUTPUTMODE_CANCEL);
		   OutputSetConfirm.this.sendBroadcast(intent_outputmode);
		   Log.e(TAG,"----------------------timeout");
		   setResult(RESULT_CANCELED,null);
		   OutPutSetConfirmDiag.dismiss();
		   finish();
		   }
   }

   private void showDispmodeSetMsg() {

	mProgressHandler = new SetconfirmHandler();		  
	mProgressHandler.sendEmptyMessageDelayed(0, set_delay);

        OutPutSetConfirmDiag = new AlertDialog.Builder(this)
            .setTitle(R.string.tv_output_mode)
            .setMessage(R.string.tv_output_mode_set_confirm_dialog)
            .setPositiveButton(R.string.ok, 
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i) {
									mProgressHandler.removeMessages(0);
									setResult(RESULT_OK,null);
									finish();
                                    }
                            }
                            )
            .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i) {
									mProgressHandler.removeMessages(0);
									Intent intent_outputmode_cancel = new Intent(ACTION_OUTPUTMODE_CANCEL);
									OutputSetConfirm.this.sendBroadcast(intent_outputmode_cancel);
									setResult(RESULT_CANCELED,null);
									finish();
									}
								}
                            )
            .setOnKeyListener(new DialogInterface.OnKeyListener() {
            	@Override
            	public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
            		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
            			dialog.cancel(); 
            			mProgressHandler.removeMessages(0);
						Intent intent_outputmode_cancel = new Intent(ACTION_OUTPUTMODE_CANCEL);
            			OutputSetConfirm.this.sendBroadcast(intent_outputmode_cancel);
            			setResult(RESULT_CANCELED,null);
            			finish();
            			return true;
            		}
            		return false;
            	}
            })                 
            .show();
        OutPutSetConfirmDiag.getButton(-2).requestFocus();
    }
}
