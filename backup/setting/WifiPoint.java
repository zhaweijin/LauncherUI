package com.mirageteam.launcher.setting;



import com.mirageTeam.launcherui.R;

import android.R.integer;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.util.Log;

public class WifiPoint {

	private String state;
	private ScanResult result;
	private String ssid;
	private int mRssi;
    String bssid;
    int security;
    int networkId;
	
    public static String CONNECTED = "connected";
    public static String CONNECTING = "connecting";
    public static String DISABLE = "disable";
    public static String WIFINULL = "";
    
    
    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;
    
    private WifiConfiguration mConfig;
    
    boolean wpsAvailable = false;
    PskType pskType = PskType.UNKNOWN;
    
    enum PskType {
        UNKNOWN,
        WPA,
        WPA2,
        WPA_WPA2
    }
	
	public WifiPoint(WifiConfiguration configuration) {
		// TODO Auto-generated constructor stub
		loadConfig(configuration);
		
	}
	
	public PskType getPskType() {
		return pskType;
	}
	

	public void setPskType(PskType pskType) {
		this.pskType = pskType;
	}

	public WifiPoint(ScanResult result) {
		// TODO Auto-generated constructor stub
		loadResult(result);
		
	}
	
	public int getSecurity(){
		return security;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public ScanResult getResult() {
		return result;
	}
	public void setResult(ScanResult result) {
		this.result = result;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	
	
    public WifiConfiguration getmConfig() {
		return mConfig;
	}

	public void setmConfig(WifiConfiguration mConfig) {
		this.mConfig = mConfig;
	}

	private void loadConfig(WifiConfiguration config) {
        ssid = (config.SSID == null ? "" : removeDoubleQuotes(config.SSID));
        bssid = config.BSSID;
        security = getSecurity(config);
        networkId = config.networkId;
        mRssi = Integer.MAX_VALUE;
        mConfig = config;
    }

    private void loadResult(ScanResult result) {
        ssid = result.SSID;
        bssid = result.BSSID;
        security = getSecurity(result);
        wpsAvailable = security != SECURITY_EAP && result.capabilities.contains("WPS");
        if (security == SECURITY_PSK)
            pskType = getPskType(result);
        networkId = -1;
        mRssi = result.level;
        
    }
	
    static String removeDoubleQuotes(String string) {
        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }
	
    static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }
    
    static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }
    
    
    public static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }
    
     

    public int getmRssi() {
		return mRssi;
	}

	public void setmRssi(int mRssi) {
		this.mRssi = mRssi;
	}

	public static PskType getPskType(ScanResult result) {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) {
            return PskType.WPA_WPA2;
        } else if (wpa2) {
            return PskType.WPA2;
        } else if (wpa) {
            return PskType.WPA;
        } else {
            Log.w("aaa", "Received abnormal flag string: " + result.capabilities);
            return PskType.UNKNOWN;
        }
    }
    
    int getLevel() {
        if (mRssi == Integer.MAX_VALUE) {
            return -1;
        }
        return WifiManager.calculateSignalLevel(mRssi, 4);
    }
    
    
    public String getSecurityString(Context context,int security) {
        switch(security) {
            case SECURITY_EAP:
                return context.getString(R.string.wifi_security_short_eap);
            case SECURITY_PSK:
                switch (pskType) {
                    case WPA:
                        return context.getString(R.string.wifi_security_short_wpa);
                    case WPA2:
                        return context.getString(R.string.wifi_security_short_wpa2);
                    case WPA_WPA2:
                        return context.getString(R.string.wifi_security_short_wpa_wpa2);
                    case UNKNOWN:
                    default:
                        return context.getString(R.string.wifi_security_short_psk_generic);
                }
            case SECURITY_WEP:
                return context.getString(R.string.wifi_security_short_wep);
            case SECURITY_NONE:
            default:
                return "";
        }
    }

}
