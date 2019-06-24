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



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;

import android.net.NetworkInfo.DetailedState;
import android.net.http.SslCertificate;
import android.net.wifi.ScanResult;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import android.net.ProxyProperties;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
 
import com.mirageTeam.launcherui.R;
import com.mirageteam.launcher.market.Util;

import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.IpAssignment;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.ProxySettings;
import android.net.wifi.WifiConfiguration.Status;
import static android.net.wifi.WifiConfiguration.INVALID_NETWORK_ID;


 


/**
 * Two types of UI are provided here.
 *
 * The first is for "usual Settings", appearing as any other Setup fragment.
 *
 * The second is for Setup Wizard, with a simplified interface that hides the action bar
 * and menus.
 */
public class WifiSettings extends Activity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "WifiSettings";

	// Combo scans can take 5-6s to complete - set to 10s.
    private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;


    private int mAccessPointSecurity;

    private IntentFilter mFilter;
    private BroadcastReceiver mReceiver;
    private Scanner mScanner;

    private WifiManager mWifiManager;
    private boolean mP2pSupported;


    private static final int WIFI_DIALOG_ID = 1;
    
    private DetailedState mLastState;
    private WifiInfo mLastInfo;

    private AtomicBoolean mConnected = new AtomicBoolean(false);
    private boolean loading = false;
  


//    private WifiManager.ActionListener mConnectListener;
//    private WifiManager.ActionListener mSaveListener;
//    private WifiManager.ActionListener mForgetListener;

    private Bundle mAccessPointSavedState;

    private String mcurrentSSID ="";
    private ListView listView;

    private IpAssignment mIpAssignment = IpAssignment.UNASSIGNED;
    private ProxySettings mProxySettings = ProxySettings.UNASSIGNED;
    private LinkProperties mLinkProperties = new LinkProperties();
    
    private Spinner mSecuritySpinner;
    private Spinner mEapMethodSpinner;
    private Spinner mEapCaCertSpinner;
    private Spinner mPhase2Spinner;
    private Spinner mEapUserCertSpinner;
    private TextView mEapIdentityView;
    private TextView mEapAnonymousView;
    
    private ArrayList<WifiPoint> scanResults = new ArrayList<WifiPoint>();
    private Map<String, ScanResult> map = new HashMap<String, ScanResult>();

    private WifiPoint mSelectedWifiPoint;
    private boolean mDlgEdit;

    private Button up;
    private Button next;
    
    private WiFiPointAdapter wiFiPointAdapter;
    
    private int wifiListviewPosition = -1;
    
    private final int UPDATE = 0x111;
    private final int INIT = 0x222;
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE:
				
				break;
			case INIT:
				wiFiPointAdapter = new WiFiPointAdapter(WifiSettings.this, scanResults);
	            listView.setAdapter(wiFiPointAdapter);
	            loading = false;
				break;
			default:
				break;
			}
		}
    	
    };
    
    @Override
    public void onCreate(Bundle icicle) {
        // Set this flag early, as it's needed by getHelpResource(), which is called by super
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wifi_setting);
        
        listView = (ListView)findViewById(R.id.listview);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        
        Log.v(TAG, "wifi setting>>>>>>>>");


//        mConnectListener = new WifiManager.ActionListener() {
//                                   public void onSuccess() {
//                                   }
//                                   public void onFailure(int reason) {
//                                        Toast.makeText(WifiSettings.this,
//                                            R.string.wifi_failed_connect_message,
//                                            Toast.LENGTH_SHORT).show();
//                                   }
//                               };

//        mSaveListener = new WifiManager.ActionListener() {
//                                public void onSuccess() {
//                                }
//                                public void onFailure(int reason) {
//                                    Toast.makeText(WifiSettings.this,
//                                        R.string.wifi_failed_save_message,
//                                        Toast.LENGTH_SHORT).show();
//                                }
//                            };
//
//        mForgetListener = new WifiManager.ActionListener() {
//                                   public void onSuccess() {
//                                   }
//                                   public void onFailure(int reason) {
//                                        Toast.makeText(WifiSettings.this,
//                                            R.string.wifi_failed_forget_message,
//                                            Toast.LENGTH_SHORT).show();
//                                   }
//                               };
        
        
        mFilter = new IntentFilter();
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleEvent(context, intent);
            }
        };

        mScanner = new Scanner();
        
        
//       if(mWifiManager.getWifiState()!=WifiManager.WIFI_STATE_ENABLED){
//    	   mWifiManager.setWifiEnabled(true);
//       }
       
       
       
       listView.setSelector(R.drawable.listview_selector);
       listView.setOnItemClickListener(onItemClickListener);
       listView.setOnItemSelectedListener(onItemSelectedListener);
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			listView.clearFocus();
	    	listView.requestFocusFromTouch();

	    	
	    	
	    	
			ListView listView = (ListView)arg0;  
		    WifiPoint wifiPoint = (WifiPoint)listView.getItemAtPosition(arg2);
		    Log.v(TAG, "wifipoint=="+wifiPoint.getState());
//		    if(wifiPoint.getState()!=null){
//			    if(!wifiPoint.getState().equals(WifiPoint.CONNECTED))
			       showDialog(wifiPoint, false);
//		    }

			

		}
    	
	};
   
	
	AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			wifiListviewPosition = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	  private void showDialog(final WifiPoint wifiPoint, boolean edit) {
		  
	     mSelectedWifiPoint = wifiPoint;
	        
	        final String currentSSID = mWifiManager.getConnectionInfo().getSSID();
	        Util.print(TAG, "curren>>"+currentSSID);
	        Util.print(TAG, "security>>"+mSelectedWifiPoint.getSecurity());
//	        String ok = currentSSID.equals(mSelectedWifiPoint.getSsid())?getResources().getString(R.string.forget):
//	        	getResources().getString(R.string.connect);
	         Util.print(TAG, "select wifi point>>"+mSelectedWifiPoint.getSsid());
                if (null == currentSSID) {
			displayWifiEditDialog(wifiPoint);
			return;
                }
	        if((currentSSID.equals("\""+mSelectedWifiPoint.getSsid()+"\"")) || 
			(currentSSID.equals(mSelectedWifiPoint.getSsid()))){
	        	//displayWifiForgetDialog(wifiPoint);
			return;
		}else {
			displayWifiEditDialog(wifiPoint);
		}
	    }

	  
	private void displayWifiEditDialog(final WifiPoint wifiPoint){
		
		
		 
		 final Dialog dialog = new Dialog(this,R.style.Theme_CustomDialog);
	     View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
	     
	     TextView  titleTextView = (TextView)view.findViewById(R.id.title);
	     titleTextView.setText(getResources().getString(R.string.connect_network));
	     
	     
	     dialog.setContentView(view);
	     LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
		
	     LayoutInflater factory = LayoutInflater.from(WifiSettings.this);
         final View mView = factory.inflate(R.layout.wifi_dialog, null);
        
         Log.v(TAG, "mrssi=="+wifiPoint.getLevel());
         Log.v(TAG, "psktype=="+wifiPoint.getSecurityString(this, wifiPoint.getSecurity()));
         TextView wifi_siginal_value = (TextView)mView.findViewById(R.id.wifi_siginal_value);
         TextView wifi_security_value = (TextView)mView.findViewById(R.id.wifi_security_value);
         
         wifi_siginal_value.setText(getResources().getStringArray(R.array.wifi_siginal_level_name)[wifiPoint.getLevel()]);
         wifi_security_value.setText(wifiPoint.getSecurityString(this, wifiPoint.getSecurity()));
        
        
        final EditText mpsEditText = (EditText)mView.findViewById(R.id.password);
        if (wifiPoint.security != WifiPoint.SECURITY_EAP) {
        	mView.findViewById(R.id.eap).setVisibility(View.GONE);
        }else {
        	mView.findViewById(R.id.eap).setVisibility(View.VISIBLE);
		}
        
        
        final TextView passwordTextView = (TextView)mView.findViewById(R.id.password_textview);
        if(wifiPoint.getSecurity()==WifiPoint.SECURITY_NONE){
        	passwordTextView.setVisibility(View.INVISIBLE);
        	mpsEditText.setVisibility(View.INVISIBLE);
        }else {
        	passwordTextView.setVisibility(View.VISIBLE);
        	mpsEditText.setVisibility(View.VISIBLE);
		}
        
//        mEapMethodSpinner = (Spinner) mView.findViewById(R.id.method);
//        mEapMethodSpinner.setOnItemSelectedListener(this);
//        mPhase2Spinner = (Spinner) mView.findViewById(R.id.phase2);
//        mEapCaCertSpinner = (Spinner) mView.findViewById(R.id.ca_cert);
//        mEapUserCertSpinner = (Spinner) mView.findViewById(R.id.user_cert);
//        mEapIdentityView = (TextView) mView.findViewById(R.id.identity);
//        mEapAnonymousView = (TextView) mView.findViewById(R.id.anonymous);
//
//        loadCertificates(mEapCaCertSpinner, Credentials.CA_CERTIFICATE);
//        loadCertificates(mEapUserCertSpinner, Credentials.USER_PRIVATE_KEY);
//
//        if (mAccessPoint != null && mAccessPoint.networkId != INVALID_NETWORK_ID) {
//            WifiConfiguration config = mAccessPoint.getConfig();
//            setSelection(mEapMethodSpinner, config.eap.value());
//
//            final String phase2Method = config.phase2.value();
//            if (phase2Method != null && phase2Method.startsWith(PHASE2_PREFIX)) {
//                setSelection(mPhase2Spinner, phase2Method.substring(PHASE2_PREFIX.length()));
//            } else {
//                setSelection(mPhase2Spinner, phase2Method);
//            }
//
//            setCertificate(mEapCaCertSpinner, KEYSTORE_SPACE + Credentials.CA_CERTIFICATE,
//                    config.ca_cert.value());
//            setCertificate(mEapUserCertSpinner, Credentials.USER_PRIVATE_KEY,
//                    config.key_id.value());
//            mEapIdentityView.setText(config.identity.value());
//            mEapAnonymousView.setText(config.anonymous_identity.value());
//        }
        
        
        Log.v(TAG, "currentSSID="+wifiPoint.getSsid());
        Button cancelButton = (Button)view.findViewById(R.id.cancel);
        cancelButton.setText(getResources().getString(R.string.ok));
        cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if(wifiPoint.getSecurity()==WifiPoint.SECURITY_NONE){
					 
					 mcurrentSSID = wifiPoint.getSsid();
					 
//		             mWifiManager.connect(generateOpenNetworkConfig(wifiPoint), mConnectListener);
					 connectNetwork(generateOpenNetworkConfig(wifiPoint));
				 }else {
					 submit(wifiPoint,mpsEditText.getText().toString());
				 }

				dialog.dismiss();
			}
		});
      
        
         contentView.addView(mView);
         setParams(dialog);
         dialog.show();
       
	}
	
	
	private void connectNetwork(WifiConfiguration wifiConfiguration){
		 
		int networkId = mWifiManager.addNetwork(wifiConfiguration);
		// Connect to network by disabling others.
		mWifiManager.updateNetwork(wifiConfiguration);
		mWifiManager.saveConfiguration();
		mWifiManager.enableNetwork(networkId, true);
	}
	
	
	
	private WifiConfiguration generateOpenNetworkConfig(WifiPoint wifiPoint) {
  
	        WifiConfiguration mConfig = new WifiConfiguration();
	        mConfig.SSID = WifiPoint.convertToQuotedString(wifiPoint.getSsid());
	        mConfig.allowedKeyManagement.set(KeyMgmt.NONE);
	        
	        return mConfig;
	 }
	  
	private void setParams(Dialog dialog) {

		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		lay.height = 420;
		lay.width = dm.widthPixels;
	}
	
	private void displayWifiForgetDialog(final WifiPoint wifiPoint){
        AlertDialog dlg = new AlertDialog.Builder(WifiSettings.this)
        .setTitle(wifiPoint.getSsid())
        .setPositiveButton(getResources().getString(R.string.forget), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

    				forget(wifiPoint);
            }
        })
        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                
            }
        })
        .create();
        dlg.show();
	}
	
	 private  WifiConfiguration getConfig(WifiPoint wifiPoint,String password) {
	        

	        WifiConfiguration config = new WifiConfiguration();

	        
	        if (wifiPoint.networkId == INVALID_NETWORK_ID) {
	            config.SSID = WifiPoint.convertToQuotedString(wifiPoint.getSsid());
	        } else {
	            config.networkId = wifiPoint.networkId;
	        }

	        mAccessPointSecurity = (wifiPoint == null) ? WifiPoint.SECURITY_NONE :
                wifiPoint.security;
	        
	        Log.v("mAccessPointSecurity", mAccessPointSecurity+"");
	        switch (mAccessPointSecurity) {
	            case WifiPoint.SECURITY_NONE:
	                config.allowedKeyManagement.set(KeyMgmt.NONE);
	                break;

	            case WifiPoint.SECURITY_WEP:
	                config.allowedKeyManagement.set(KeyMgmt.NONE);
	                config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
	                config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
	                if (password.length() != 0) {
	                    int length = password.length();
	                    // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
	                    if ((length == 10 || length == 26 || length == 58) &&
	                            password.matches("[0-9A-Fa-f]*")) {
	                        config.wepKeys[0] = password;
	                    } else {
	                        config.wepKeys[0] = '"' + password + '"';
	                    }
	                }
	                break;

	            case WifiPoint.SECURITY_PSK:
	                config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
	                if (password.length() != 0) {
	                    if (password.matches("[0-9A-Fa-f]{64}")) {
	                        config.preSharedKey = password;
	                    } else {
	                        config.preSharedKey = '"' + password + '"';
	                    }
	                }
	                break;

//	            case WifiPoint.SECURITY_EAP:
//	                config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
//	                config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
//	                config.eap.setValue((String) mEapMethodSpinner.getSelectedItem());
//
//	                config.phase2.setValue((mPhase2Spinner.getSelectedItemPosition() == 0) ? "" :
//	                        PHASE2_PREFIX + mPhase2Spinner.getSelectedItem());
//	                config.ca_cert.setValue((mEapCaCertSpinner.getSelectedItemPosition() == 0) ? "" :
//	                        KEYSTORE_SPACE + Credentials.CA_CERTIFICATE +
//	                        (String) mEapCaCertSpinner.getSelectedItem());
//	                config.client_cert.setValue((mEapUserCertSpinner.getSelectedItemPosition() == 0) ?
//	                        "" : KEYSTORE_SPACE + Credentials.USER_CERTIFICATE +
//	                        (String) mEapUserCertSpinner.getSelectedItem());
//	                final boolean isEmptyKeyId = (mEapUserCertSpinner.getSelectedItemPosition() == 0);
//	                config.key_id.setValue(isEmptyKeyId ? "" : Credentials.USER_PRIVATE_KEY +
//	                        (String) mEapUserCertSpinner.getSelectedItem());
//	                config.engine.setValue(isEmptyKeyId ? WifiConfiguration.ENGINE_DISABLE :
//	                        WifiConfiguration.ENGINE_ENABLE);
//	                config.engine_id.setValue(isEmptyKeyId ? "" : WifiConfiguration.KEYSTORE_ENGINE_ID);
//	                config.identity.setValue((mEapIdentityView.length() == 0) ? "" :
//	                        mEapIdentityView.getText().toString());
//	                config.anonymous_identity.setValue((mEapAnonymousView.length() == 0) ? "" :
//	                        mEapAnonymousView.getText().toString());
//	                if (mPasswordView.length() != 0) {
//	                    config.password.setValue(mPasswordView.getText().toString());
//	                }
//	                break;

	            default:
	                    return null;
	        }

	        config.proxySettings = mProxySettings;
	        config.ipAssignment = mIpAssignment;
	        config.linkProperties = new LinkProperties(mLinkProperties);

	        return config;
	    }
	
	
	   @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	        if (parent == mSecuritySpinner) {
	            mAccessPointSecurity = position;
//	            showSecurityFields();
	        } else if (parent == mEapMethodSpinner) {
//	            showSecurityFields();
	        }
//	        } else if (parent == mProxySettingsSpinner) {
//	            showProxyFields();
//	        } else {
////	            showIpConfigFields();
//	        }

	    }
	 
	   @Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	 
//	    private void showSecurityFields() {
//	        
//	        if (mAccessPointSecurity == AccessPoint.SECURITY_NONE) {
//	            mView.findViewById(R.id.security_fields).setVisibility(View.GONE);
//	            return;
//	        }
//	        mView.findViewById(R.id.security_fields).setVisibility(View.VISIBLE);
//
//	        if (mPasswordView == null) {
//	            mPasswordView = (TextView) mView.findViewById(R.id.password);
//	            mPasswordView.addTextChangedListener(this);
//	            ((CheckBox) mView.findViewById(R.id.show_password)).setOnClickListener(this);
//
//	            if (mAccessPoint != null && mAccessPoint.networkId != INVALID_NETWORK_ID) {
//	                mPasswordView.setHint(R.string.wifi_unchanged);
//	            }
//	        }
//
//	        if (mAccessPointSecurity != AccessPoint.SECURITY_EAP) {
//	            mView.findViewById(R.id.eap).setVisibility(View.GONE);
//	            return;
//	        }
//	        mView.findViewById(R.id.eap).setVisibility(View.VISIBLE);
//
//	        if (mEapMethodSpinner == null) {
//	            mEapMethodSpinner = (Spinner) mView.findViewById(R.id.method);
//	            mEapMethodSpinner.setOnItemSelectedListener(this);
//	            mPhase2Spinner = (Spinner) mView.findViewById(R.id.phase2);
//	            mEapCaCertSpinner = (Spinner) mView.findViewById(R.id.ca_cert);
//	            mEapUserCertSpinner = (Spinner) mView.findViewById(R.id.user_cert);
//	            mEapIdentityView = (TextView) mView.findViewById(R.id.identity);
//	            mEapAnonymousView = (TextView) mView.findViewById(R.id.anonymous);
//
//	            loadCertificates(mEapCaCertSpinner, Credentials.CA_CERTIFICATE);
//	            loadCertificates(mEapUserCertSpinner, Credentials.USER_PRIVATE_KEY);
//
//	            if (mAccessPoint != null && mAccessPoint.networkId != INVALID_NETWORK_ID) {
//	                WifiConfiguration config = mAccessPoint.getConfig();
//	                setSelection(mEapMethodSpinner, config.eap.value());
//
//	                final String phase2Method = config.phase2.value();
//	                if (phase2Method != null && phase2Method.startsWith(PHASE2_PREFIX)) {
//	                    setSelection(mPhase2Spinner, phase2Method.substring(PHASE2_PREFIX.length()));
//	                } else {
//	                    setSelection(mPhase2Spinner, phase2Method);
//	                }
//
//	                setCertificate(mEapCaCertSpinner, KEYSTORE_SPACE + Credentials.CA_CERTIFICATE,
//	                        config.ca_cert.value());
//	                setCertificate(mEapUserCertSpinner, Credentials.USER_PRIVATE_KEY,
//	                        config.key_id.value());
//	                mEapIdentityView.setText(config.identity.value());
//	                mEapAnonymousView.setText(config.anonymous_identity.value());
//	            }
//	        }
//
//	        mView.findViewById(R.id.l_method).setVisibility(View.VISIBLE);
//	        mView.findViewById(R.id.l_identity).setVisibility(View.VISIBLE);
//
//	        if (mEapMethodSpinner.getSelectedItemPosition() == WIFI_EAP_METHOD_PWD){
//	            mView.findViewById(R.id.l_phase2).setVisibility(View.GONE);
//	            mView.findViewById(R.id.l_ca_cert).setVisibility(View.GONE);
//	            mView.findViewById(R.id.l_user_cert).setVisibility(View.GONE);
//	            mView.findViewById(R.id.l_anonymous).setVisibility(View.GONE);
//	        } else {
//	            mView.findViewById(R.id.l_phase2).setVisibility(View.VISIBLE);
//	            mView.findViewById(R.id.l_ca_cert).setVisibility(View.VISIBLE);
//	            mView.findViewById(R.id.l_user_cert).setVisibility(View.VISIBLE);
//	            mView.findViewById(R.id.l_anonymous).setVisibility(View.VISIBLE);
//	        }
//	    }   
	   
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);

        mScanner.resume();
        loadResult();
    }

    @Override
    public void onPause() {
        super.onPause();
        
        unregisterReceiver(mReceiver);
        mScanner.pause();
    }


     private void forget(WifiPoint wifiPoint) {
//        if (wifiPoint.networkId == INVALID_NETWORK_ID) {
//            // Should not happen, but a monkey seems to triger it
//            Log.e(TAG, "Failed to forget invalid network " + mSelectedWifiPoint.getmConfig());
//            return;
//        }
//
//        mWifiManager.forget(mSelectedWifiPoint.networkId, mForgetListener);
//
//        if (mWifiManager.isWifiEnabled()) {
//            mScanner.resume();
//        }
//        handler.sendEmptyMessage(INIT);
    }
     
   private void submit(WifiPoint wifiPoint,String password) {
	   
	     if(password==null || password.length()==0)
	    	 return;
         WifiConfiguration config = getConfig(wifiPoint, password);

        mcurrentSSID = wifiPoint.getSsid();
        	 
         if (config.networkId != INVALID_NETWORK_ID) {
            if (mSelectedWifiPoint != null) {
                mWifiManager.addNetwork(config);
            }
        } else {
//             mWifiManager.connect(config, mConnectListener);
        	connectNetwork(config);
            
        }

        if (mWifiManager.isWifiEnabled()) {
            mScanner.resume();
        }
        
        handler.sendEmptyMessage(INIT);
    }



    private synchronized void loadResult() {

	    if(loading)
	    	return;
	    loading  = true;
	    
//    	new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
					    
					    final List<ScanResult> results = mWifiManager.getScanResults();
				        scanResults.clear();
				       
				        Log.v(TAG, "scan result>>>>>>>>>");
				        
//				        final List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
//				        if (configs != null) {
//				            for (WifiConfiguration config : configs) {
//				               WifiPoint point = new WifiPoint(config);
//				               scanResults.add(point);
//				            }
//				        }
				        
				        
				        if (results != null) {
				            for (ScanResult result : results) {
				                 
				                if (result.SSID == null || result.SSID.length() == 0 ||
				                        result.capabilities.contains("[IBSS]")) {
				                    continue;
				                }
				                WifiPoint point = new WifiPoint(result);
				                if(!checkIsScan(result))  
				                   scanResults.add(point);
				            }
				        }
				        
				        updateState();
 
				        
				        handler.sendEmptyMessage(INIT);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}).start();
       
    }

   private boolean checkIsScan(ScanResult result){
	   boolean r = false;
	   for(int i=0;i<scanResults.size();i++){
		   if(scanResults.get(i).getSsid()==result.SSID){
			   r = true;
			   break;
		   }
	   }
	   
	   return r;
   }
    

   private  void updateState(){
	   
//	   //sort scanresult
//	   ArrayList<WifiPoint> tempResult = new ArrayList<WifiPoint>();
//	   tempResult.clear();
	   
	   String currentSSID = mWifiManager.getConnectionInfo().getSSID();
           Log.v(TAG, "mcurrentSSID>>>>>>>>>>>>"+currentSSID);
	   //Log.v("current>>ssid===", currentSSID);
	   for(int i=0;i<scanResults.size();i++){
		   //Log.v(TAG, "mcurrentSSID>>>>>>>>>>>>"+mcurrentSSID);
		   Log.v(TAG, "mcurrentSSID>>>>>>>>>>>>"+currentSSID + ">>>>>"+scanResults.get(i).getSsid());
		   if( (currentSSID!=null) && 
				((currentSSID.equals("\""+scanResults.get(i).getSsid()+"\"")) || 
				((currentSSID.equals(scanResults.get(i).getSsid()))))){
			   
			   //Log.v(TAG, ">>>>>>>>>>>>aaaaaaaaaaa");
			   scanResults.get(i).setState(WifiPoint.CONNECTED);
			   mcurrentSSID = "";
//			   tempResult.add(scanResults.get(i));
		   }
		   
		   if(scanResults.get(i).getSsid().equals(mcurrentSSID)){
			   scanResults.get(i).setState(WifiPoint.CONNECTING);
//			   tempResult.add(scanResults.get(i));
		   }
	   }
	   
//	   if(tempResult.size()>0){
//		  for(int i=0;i<scanResults.size();i++){
//			  if(!scanResults.get(i).getSsid().equals(tempResult.get(0).getSsid())){
//				  tempResult.add(scanResults.get(i));
//			  }
//		  }
//		  
//		  scanResults.clear();
//		  scanResults.addAll(tempResult);
//	   }
	   
	   Collections.sort(scanResults);
   }
    
    
    private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
//            updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
//                    WifiManager.WIFI_STATE_UNKNOWN));
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                if(mWifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
                	loadResult();
                }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
//            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(
//                    WifiManager.EXTRA_NETWORK_INFO);
//            mConnected.set(info.isConnected());
//            changeNextButtonState(info.isConnected());
//            updateAccessPoints();
//            updateConnectionState(info.getDetailedState());
//            if (mAutoFinishOnConnection && info.isConnected()) {
//                this.finish();
//                return;
//            }
        } else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
//            updateConnectionState(null);
        }
    }


    private class Scanner extends Handler {
        private int mRetry = 0;

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void forceScan() {
            removeMessages(0);
            sendEmptyMessage(0);
        }

        void pause() {
            mRetry = 0;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message) {
            if (mWifiManager.startScan()) {
                mRetry = 0;
            } else if (++mRetry >= 3) {
                mRetry = 0;
                Toast.makeText(WifiSettings.this, R.string.wifi_fail_to_scan,
                        Toast.LENGTH_LONG).show();
                return;
            }
            sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
        }
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
			Log.v(TAG, "FOUCES view>>"+getCurrentFocus());
			switch (getCurrentFocus().getId()) {
			case R.id.listview:
				Log.v(TAG, "listview>>>positon="+wifiListviewPosition);
				if(wifiListviewPosition==0)
				   getParent().findViewById(R.id.layout_network).requestFocus();
				break;
			};
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
			switch (getCurrentFocus().getId()) {
			case R.id.listview:
				Log.v(TAG, "listview>>>positon="+wifiListviewPosition);
				getParent().findViewById(R.id.more_setting).requestFocus();
				break;
			};
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			return getParent().onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	 
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return getParent().onKeyUp(keyCode, event);
		}
		return super.onKeyUp(keyCode, event);
	}
	 
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return getParent().onKeyLongPress(keyCode, event);
		}
		return super.onKeyLongPress(keyCode, event);
	}
//	 public String getWifiSiginalLevelName(String )
	
//	
//	class SortByConnected implements Comparator {
//		public int compare(Object o1, Object o2) {
//			WifiPoint s1 = (WifiPoint) o1;
//			WifiPoint s2 = (WifiPoint) o2;
//			if (s1.getState() != null
//					&& s1.getState().equals(WifiPoint.CONNECTED)){
//				Log.v(TAG, "aaaaaaaaaaaaaaaaaaaaaa");
//				return 1;
//			}
//				
//			return -1;
//		}
//	}
}
