package com.mirageteam.launcher.setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mirageTeam.launcherui.R;


import android.content.Context;
import android.util.Log;

public class Config
{
        private static final boolean mDebug = true;
        
        public static void Logd(String msg1, String msg2)
        {
                if(mDebug)
                {
                        Log.d(msg1, msg2);
                }
        }
        
        public static void Loge(String msg1, String msg2)
        {
                if(mDebug)
                {
                        Log.e(msg1, msg2);
                }
        }
        
        public static String mDisplayOutputEntries[] = null;
        public static String mLogicOutputEntries[] = null;
        
        public static final void Instance(Context context)
        {
                if((mDisplayOutputEntries != null) && (mLogicOutputEntries != null))
                {
                        return;
                }
                
                mDisplayOutputEntries = context.getResources().getStringArray(R.array.outputmode_entries_display);
                mLogicOutputEntries = context.getResources().getStringArray(R.array.outputmode_entries_logic);
                
                int index = 0;
                
                Logd("*************************", "*************************");
                for(index = 0; index < mDisplayOutputEntries.length; index++)
                {
                        Logd("Config", "mDisplayOutputEntries[" + index + "] is: " + mDisplayOutputEntries[index]);
                }              
                Logd("*************************", "*************************");
                for(index = 0; index < mLogicOutputEntries.length; index++)
                {
                        Logd("Config", "mLogicOutputEntries[" + index + "] is: " + mLogicOutputEntries[index]);
                }
                Logd("*************************", "*************************");
        }
        
        public static final String Display2LogicEntry(String displayEntry)
        {
                int index = 0;
                
                if((displayEntry != null) && (mDisplayOutputEntries != null) && (mLogicOutputEntries != null))
                {
                        for(index = 0; index < mDisplayOutputEntries.length; index++)
                        {
                                if(displayEntry.equalsIgnoreCase(mDisplayOutputEntries[index]))
                                {
                                        return mLogicOutputEntries[index];
                                }
                        }
                }
                
                return null;
        }
        
        public static final String Logic2DisplayEntry(String logicEntry)
        {
                int index = 0;
                
                if((logicEntry != null) && (mDisplayOutputEntries != null) && (mLogicOutputEntries != null))
                {
                        for(index = 0; index < mLogicOutputEntries.length; index++)
                        {
                                if(logicEntry.equalsIgnoreCase(mLogicOutputEntries[index]))
                                {
                                        return mDisplayOutputEntries[index];
                                }
                        }
                }
                
                return null;
        }

        public final static String mCurrentResolution = "/sys/class/display/mode";
        
	public static String getCurrentOutputResolution() {
		String currentMode = null;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(mCurrentResolution);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(fileReader);

		try {
			currentMode = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (currentMode.equalsIgnoreCase("480cvbs")) {
			currentMode = "480i";
		} else if (currentMode.equalsIgnoreCase("576cvbs")) {
			currentMode = "576i";
		}

		return currentMode;
	}
        
//        public static final String[] Display2LogicEntries(String[] displayEntries)
//        {
//                int index = 0;
//                
//                String[] logicEntries = null;
//                
//                if((displayEntries != null) && (mDisplayOutputEntries != null) && (mLogicOutputEntries != null))
//                {
//                        logicEntries = new String[displayEntries.length];
//                        
//                        for(index = 0; index < displayEntries.length; index++)
//                        {
//                                logicEntries[index] = Display2LogicEntry(displayEntries[index]);
//                        }
//                        
//                        return logicEntries;
//                }
//                
//                return null;
//        }
//        
//        public static final String[] Logic2DisplayEntries(String[] logicEntries)
//        {
//                int index = 0;
//                
//                String[] displayEntries = null;
//                
//                if((logicEntries != null) && (mDisplayOutputEntries != null) && (mLogicOutputEntries != null))
//                {
//                        displayEntries = new String[logicEntries.length];
//                        
//                        for(index = 0; index < logicEntries.length; index++)
//                        {
//                                displayEntries[index] = Logic2DisplayEntry(logicEntries[index]);
//                        }
//                        
//                        return displayEntries;
//                }
//                
//                return null;
//        }
        
            public static String readStringFromFile(String pathname)
            {
                    String msg = null;
                    
                    FileReader fileReader = null;
                    BufferedReader bufferedReader = null;
                    
                    try
                    {
                            fileReader = new FileReader(pathname);
                    } 
                    catch (FileNotFoundException e)
                    {
                            e.printStackTrace();
                    }
                    
                    bufferedReader = new BufferedReader(fileReader);
                    
                    try
                    {
                            msg = bufferedReader.readLine();
                            Logd("readStringFromFile()", "msg is: " + msg);
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
                    catch(IOException e)
                    {
                            e.printStackTrace();
                    }
                    
                    return msg;
            }
            
            public static void writeStringToFile(String pathname, String msg)
            {
                    FileWriter fileWriter = null;
                    BufferedWriter bufferedWriter = null;
                    
                    try
                    {
                            fileWriter = new FileWriter(pathname);
                    }
                    catch (IOException e)
                    {
                            e.printStackTrace();
                    }
                    
                    bufferedWriter = new BufferedWriter(fileWriter);
                    
                    try
                    {
                            bufferedWriter.write(msg);
                    }
                    catch (IOException e)
                    {
                            e.printStackTrace();
                    }
                    
                    try
                    {
                            bufferedWriter.close();
                            fileWriter.close();
                    }
                    catch(IOException e)
                    {
                            e.printStackTrace();
                    }
            }
}
