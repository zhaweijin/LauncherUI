package com.mirageteam.launcher.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.mirageTeam.launcherui.R;
import com.mirageteam.launcher.market.Util;

import android.os.SystemProperties;
import android.os.FileObserver;
import android.content.res.Resources;
import android.app.ActivityManagerNative;
import android.content.res.Configuration;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.os.RemoteException;
import java.util.Locale;
import java.io.File;
import android.os.UEventObserver;
import android.util.Log;

public class HDMICheckHandler extends Handler
{
    private static final String TAG = "HDMICheckHandler";
    private final static String mHDMIStatusConfig = "/sys/class/amhdmitx/amhdmitx0/hpd_state";
    private final String mOutputStatusConfig = "/sys/class/amhdmitx/amhdmitx0/disp_cap";
    private final String mCurrentResolution = "/sys/class/display/mode";
    
    private final String ACTION_OUTPUTMODE_CHANGE = "android.intent.action.OUTPUTMODE_CHANGE";
    private final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
    private final String ACTION_OUTPUTMODE_CANCEL = "android.intent.action.OUTPUTMODE_CANCEL";
    private final String OUTPUT_MODE = "output_mode";
    private final String CVBS_MODE = "cvbs_mode";
    
    private static final String PROPERTY_AUTO_OUTPUT_MODE = "auto.output.mode.property";
    private static final String ACTION_AUTO_OUTPUT_MODE = "android.intent.action.AUTO_OUTPUT_MODE";
    private static final String BOOLEAN_AUTO_OUTPUT_MODE = "auto_output_mode";
    private static final String PREFERENCE_AUTO_OUTPUT_MODE = "preference_auto_output_mode";
    
    private BroadcastReceiver mReceiver = null;
    private boolean mAutoOutputMode = false;
    
    private static final String FREQ_DEFAULT = "";
    private static final String FREQ_SETTING = "50hz";
    
    private static final String mCECLanguageConfig = "/sys/class/switch/lang_config/state";
    public static final boolean mAutoStartConfig = true;
    private static final String PROP_CEC_LANGUAGE_AUTO_SWITCH = "auto.swtich.language.by.cec";
    private static final String mAutoLanguagePreferencesFile = "cec_language_preferences";
    
    public static final int HDMICHECK_START = 18001;
    public static final int HDMICHECK_STOP = 18002;
    
    public static final int EXECUTE_ONCE = 1;
    public static final int EXECUTE_MANY = 2;
    
    private Context mContext = null;
    
    public HDMICheckHandler(Context context, boolean receiverRegister)
    {
        mContext = context;
        
        SharedPreferences sharedpreference = mContext.getSharedPreferences(PREFERENCE_AUTO_OUTPUT_MODE, 
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        String defaultProperty = null;
        
        if(mAutoStartConfig)
        {
            defaultProperty = sharedpreference.getString(PROPERTY_AUTO_OUTPUT_MODE, "true");
        }
        else
        {
            defaultProperty = sharedpreference.getString(PROPERTY_AUTO_OUTPUT_MODE, "false");
        }
        
        Editor editor = sharedpreference.edit();
        editor.putString(PROPERTY_AUTO_OUTPUT_MODE, defaultProperty);
        editor.commit();
        
        if("true".equalsIgnoreCase(defaultProperty))
        {
            mAutoOutputMode = true;
        }
        else
        {
            mAutoOutputMode = false;
        }
        Log.v(TAG, "hdmi is plugged="+isHDMIPlugged());
                  if(!isHDMIPlugged())
                {
                        mAutoOutputMode = false;
                        String oldMode = getOutputResolution();
                        Util.print(TAG, "hdmi output mode>>>>"+oldMode);
                        if(!(oldMode.contains("480i") || oldMode.contains("576i") || oldMode.contains("480p") || oldMode.contains("576p")))
                        {
                            setOutputResolution("576i");
                        }
                }      

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(intent.getAction().equalsIgnoreCase(ACTION_AUTO_OUTPUT_MODE))
                {
                    String str = intent.getStringExtra(BOOLEAN_AUTO_OUTPUT_MODE);
                    if(str.equalsIgnoreCase("true"))
                    {
                            mAutoOutputMode = true;
                    }
                    else
                    {
                            mAutoOutputMode = false;
                    }
                    
                    Editor editor = mContext.getSharedPreferences(PREFERENCE_AUTO_OUTPUT_MODE, 
                                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).edit();
                    if(mAutoOutputMode)
                    {
                            editor.putString(PROPERTY_AUTO_OUTPUT_MODE, "true");
                    }
                    else
                    {
                            editor.putString(PROPERTY_AUTO_OUTPUT_MODE, "false");
                    }
                    
                    editor.commit();
                    
                    if(isHDMIPlugged()  && mAutoOutputMode)
                    {
                        if(!isAmlogicVideoPlayerRunning())
                        {
                            setOutputResolution(getBestMatchResolution());
                        }
                    }
                }
            }
        };
    }
    
    public void startHDMICheck()
    {
        Message start_msg = obtainMessage(HDMICHECK_START);
        start_msg.arg1 = EXECUTE_MANY;
        sendMessage(start_msg);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTO_OUTPUT_MODE);
        
        mContext.registerReceiver(mReceiver, filter);
        if (SystemProperties.getBoolean("ro.platform.has.cecmode",false))
         {
             startListenCecDev();
         }
    }
    
    public void stopHDMICheck()
    {
        mContext.unregisterReceiver(mReceiver);
        
        Message stop_msg = obtainMessage(HDMICHECK_STOP);
        sendMessageAtFrontOfQueue(stop_msg);
    }
    
    @Override
    public void handleMessage(Message msg) 
    {
        super.handleMessage(msg);
        
        switch(msg.what)
        {
            case HDMICHECK_START:
            {
                if(isHDMIPlugged()  && mAutoOutputMode)
                {
                    if(!isAmlogicVideoPlayerRunning())
                    {
                        setOutputResolution(getBestMatchResolution());
                    }
                }
                
//                if(msg.arg1 == EXECUTE_MANY)
//                {
//                    Message start_msg = obtainMessage(HDMICHECK_START);
//                    start_msg.arg1 = EXECUTE_MANY;
//                    sendMessageDelayed(start_msg, 600);
//                }
            }
            break;
            
            case HDMICHECK_STOP:
            {
                removeMessages(HDMICHECK_START);
            }
            break;
        }
    }
    
    public static boolean isHDMIPlugged()
    {
            int hdmiStatus = 0;
            FileInputStream inputStream = null;
            try
            {
                    inputStream = new FileInputStream(mHDMIStatusConfig);
            } 
            catch (FileNotFoundException e)
            {
                    e.printStackTrace();
            }
            
            try
            {
                    hdmiStatus = inputStream.read();
            }
            catch (IOException e)
            {
                    
                    e.printStackTrace();
            }
            
            // ASCII 49 = '1'
            // ASCII 48 = '0'
            if(hdmiStatus == 49)
            {
                    hdmiStatus = 1;
            }
            
            if(hdmiStatus == 48)
            {
                    hdmiStatus = 0;
            }
            
            try
            {
                    inputStream.close();
            } 
            catch (IOException e)
            {
                    e.printStackTrace();
            }
            
            Config.Logd("HDMICheckService", "hdmiStatus is: " + hdmiStatus);
            
            if(hdmiStatus == 1)
            {
                    return true;
            }
            else
            {
                    return false;
            }
    }
    
    private final boolean isForTopResolution = false;
    private final String[] mUsualResolutions =
    {
            "1080p",
            "1080p50hz",
            "1080i",
            "1080i50hz",
            "720p",
            "720p50hz",
            "576p",
            "576i",
            "480p",
            "480i"
    };
    private String getBestMatchResolution()
    {
        ArrayList<String> resolutionList = new ArrayList<String>();
        
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            String readLine = null;
            
            try
            {
                    fileReader = new FileReader(mOutputStatusConfig);
            } 
            catch (FileNotFoundException e)
            {
                    e.printStackTrace();
            }
            
            bufferedReader = new BufferedReader(fileReader);
            
            try
            {
                    while((readLine = bufferedReader.readLine()) != null)
                    {
                        resolutionList.add(readLine);
                    }
            } 
            catch (IOException e)
            {
                    e.printStackTrace();
            }
            
            try
            {
                bufferedReader.close();
                fileReader.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            if(resolutionList.isEmpty())
            {
                return "720p";
            }
            
            if(isForTopResolution)
            {
                for(int index = 0; index < mUsualResolutions.length; index++)
                {
                    if(resolutionList.contains(mUsualResolutions[index]))
                    {
                        return mUsualResolutions[index];
                    }
                }
            }
            else
            {
                for(int index = 0; index < resolutionList.size(); index++)
                {
                    if(resolutionList.get(index).contains("*"))
                    {
                        return resolutionList.get(index);
                    }
                }
            }
            
            return "720p";
    }
           
        public String getOutputResolution()
        {
                String currentMode = null;
                
                FileReader fileReader = null;
                try
                {
                        fileReader = new FileReader(mCurrentResolution);
                } 
                catch (FileNotFoundException e)
                {
                        e.printStackTrace();
                }
                
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(fileReader);
                
                try
                {
                        currentMode = bufferedReader.readLine();
                } 
                catch (IOException e)
                {
                        e.printStackTrace();
                }          
                
                try
                {
                        bufferedReader.close();
                } 
                catch (IOException e)
                {
                        e.printStackTrace();
                }
                
                try
                {
                        fileReader.close();
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
                
                return currentMode;
        }
         
    private void setOutputResolution(String resolution)
    {
            Intent change_intent = new Intent(ACTION_OUTPUTMODE_CHANGE);
            Intent save_intent = new Intent(ACTION_OUTPUTMODE_SAVE);
            
            String newMode = null;
            String currentMode = null;
            
            FileReader fileReader = null;
            try
            {
                    fileReader = new FileReader(mCurrentResolution);
            } 
            catch (FileNotFoundException e)
            {
                    e.printStackTrace();
            }
            
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(fileReader);
            
            try
            {
                    currentMode = bufferedReader.readLine();
            } 
            catch (IOException e)
            {
                    e.printStackTrace();
            }          
            
            try
            {
                    bufferedReader.close();
            } 
            catch (IOException e)
            {
                    e.printStackTrace();
            }
            
            try
            {
                    fileReader.close();
            }
            catch (IOException e)
            {
                    e.printStackTrace();
            }
            
            Util.print(TAG, "resolution="+resolution);
            
            // Force to set to 720p
            if(resolution == null)
            {        
                    change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
                    save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
            }
            else
            {
                    if(resolution.contains("480i"))
                    {
                            change_intent.putExtra(OUTPUT_MODE, "480i");
                            save_intent.putExtra(OUTPUT_MODE, "480i");
                    }
                    else if(resolution.contains("480p"))
                    {
                            change_intent.putExtra(OUTPUT_MODE, "480p");
                            save_intent.putExtra(OUTPUT_MODE, "480p");
                    }
                    else if(resolution.contains("576i"))
                    {
                            change_intent.putExtra(OUTPUT_MODE, "576i");
                            save_intent.putExtra(OUTPUT_MODE, "576i");
                    }
                    else if(resolution.contains("576p"))
                    {
                            change_intent.putExtra(OUTPUT_MODE, "576p");
                            save_intent.putExtra(OUTPUT_MODE, "576p");
                    }
                    else if(resolution.contains("720p"))
                    {
                            if(resolution.contains(FREQ_SETTING))
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_SETTING);
                                    save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_SETTING);
                            }
                            else
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
                                    save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
                            }
                    }
                    else if(resolution.contains("1080i"))
                    {
                            if(resolution.contains(FREQ_SETTING))
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_SETTING);
                                    save_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_SETTING);
                            }
                            else
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_DEFAULT);
                                    save_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_DEFAULT);
                            }
                    }
                    else if(resolution.contains("1080p"))
                    {
                            if(resolution.contains(FREQ_SETTING))
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_SETTING);
                                    save_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_SETTING);
                            }
                            else
                            {
                                    change_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_DEFAULT);
                                    save_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_DEFAULT);
                            }
                    }
            }
            
            Util.print(TAG, "save intent="+save_intent.getStringExtra(OUTPUT_MODE));
            
            newMode = change_intent.getStringExtra(OUTPUT_MODE);
            if(newMode != null)
            {
                    Config.Logd(getClass().getName(), "new mode is: " + newMode);
            }
            else
            {
                    Config.Logd(getClass().getName(), "new mode is: " + "null");
            }
            
            if(currentMode != null)
            {
                    Config.Logd(getClass().getName(), "current mode is: " + currentMode);
            }
            else
            {
                    Config.Logd(getClass().getName(), "current mode is: " + "null");
            }
            
            if(currentMode != null && newMode != null)
            {
                    if(currentMode.equals(newMode))
                    {  
                            return;
                    }
            }
            
            mContext.sendBroadcast(change_intent);
            mContext.sendBroadcast(save_intent);
    }
    
    private boolean isAmlogicVideoPlayerRunning()
    {
            ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
            String className = componentName.getClassName();
            String packageName = componentName.getPackageName();
            
            String videoPlayerClassName = "com.farcore.videoplayer.playermenu";
            
            if(className.equalsIgnoreCase(videoPlayerClassName))
            {
                    return true;
            }
            
            return false;
    }


private void startListenCecDev()
    {
                  
    if (new File("/sys/devices/virtual/switch/lang_config/state").exists()) 
        {
        mCedObserver.startObserving("DEVPATH=/devices/virtual/switch/lang_config");
        //final String filename = "/sys/class/switch/lang_config/state";
        }
}

private UEventObserver mCedObserver = new UEventObserver() {
    @Override
    public void onUEvent(UEventObserver.UEvent event) {
        SharedPreferences sharedpreference = mContext.getSharedPreferences(mAutoLanguagePreferencesFile, 
                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        Boolean isNeedAutoSwitchLanguage = sharedpreference.getBoolean(PROP_CEC_LANGUAGE_AUTO_SWITCH, false);
        if(!isNeedAutoSwitchLanguage)
            return;
        
        String mNewLanguage = event.get("SWITCH_STATE");
        //Log.e(TAG,"------onUEvent:"+mNewLanguage);
        int i = Integer.parseInt(mNewLanguage, 16);
        //Log.e(TAG,"-------i:"+i);
        String able=mContext.getResources().getConfiguration().locale.getCountry();
        if (i < 0)
            return;
        String [] language_list = mContext.getResources().getStringArray(R.array.language);
        String [] country_list = mContext.getResources().getStringArray(R.array.country);
        //Log.e(TAG,"--------current language is "+able);
        //Log.e(TAG,"--------new language is "+country_list[i]);
        if(able.equals(country_list[i]))
            return ;
        Locale l = new Locale(language_list[i], country_list[i]);
        
        updateLocale(l);
    }
};
private  void updateLocale(Locale locale) {
    try {
        IActivityManager am = ActivityManagerNative.getDefault();
        Configuration config = am.getConfiguration();

        config.locale = locale;

        // indicate this isn't some passing default - the user wants this remembered
        config.userSetLocale = true;

        am.updateConfiguration(config);
        // Trigger the dirty bit for the Settings Provider.
        BackupManager.dataChanged("com.android.providers.settings");
    } catch (RemoteException e) {
        // Intentionally left blank
    }
}
}
