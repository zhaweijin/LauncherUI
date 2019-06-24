package com.mirageteam.launcher.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.SystemProperties;
import android.app.SystemWriteManager;

 

public class OutputSettingsBroadcastReceiver extends BroadcastReceiver
{
    private final String ACTION_OUTPUTMODE_CHANGE = "android.intent.action.OUTPUTMODE_CHANGE";
    private final String ACTION_OUTPUTMODE_CANCEL = "android.intent.action.OUTPUTMODE_CANCEL";
    private final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
    private final String ACTION_OUTPUTPOSITION_CHANGE = "android.intent.action.OUTPUTPOSITION_CHANGE";
    private final String ACTION_OUTPUTPOSITION_CANCEL = "android.intent.action.OUTPUTPOSITION_CANCEL";
    private final String ACTION_OUTPUTPOSITION_SAVE = "android.intent.action.OUTPUTPOSITION_SAVE";
    private final String ACTION_OUTPUTPOSITION_DEFAULT_SAVE = "android.intent.action.OUTPUTPOSITION_DEFAULT_SAVE";
    private final String ACTION_DISP_CHANGE = "android.intent.action.DISP_CHANGE";
    private final String ACTION_REALVIDEO_ON = "android.intent.action.REALVIDEO_ON";
    private final String ACTION_REALVIDEO_OFF = "android.intent.action.REALVIDEO_OFF";
    private final String ACTION_VIDEOPOSITION_CHANGE = "android.intent.action.VIDEOPOSITION_CHANGE";
    private final String ACTION_CVBSMODE_CHANGE = "android.intent.action.CVBSMODE_CHANGE";
    
    private final String OUTPUT_MODE = "output_mode";

    private final String OUTPUT_POSITION_X = "output_position_x";
    private final String OUTPUT_POSITION_Y = "output_position_y";
    private final String OUTPUT_POSITION_W = "output_position_w";
    private final String OUTPUT_POSITION_H = "output_position_h";
    private final String OUTPUT_POSITION_MODE = "output_position_mode";

    private final String DISP_W = "disp_w";
    private final String DISP_H = "disp_h";

    private static final String STR_OUTPUT_VAR="ubootenv.var.outputmode";
    private static final String STR_HAS_CVBS = "ro.platform.has.cvbsmode";
    private static final String STR_DEFAULT_FREQUENCY_VAR="ubootenv.var.defaulttvfrequency";
    private static final String STR_1080SCALE="ro.platform.has.1080scale";
    private final String OutputModeFile= "/sys/class/display/mode";
    private final String PpscalerFile= "/sys/class/ppmgr/ppscaler";
    private final String PpscalerRectFile= "/sys/class/ppmgr/ppscaler_rect";
    private final String DispFile = "/sys/class/ppmgr/disp";
    private final String FreescaleFb0File = "/sys/class/graphics/fb0/free_scale";
    private final String FreescaleFb1File = "/sys/class/graphics/fb1/free_scale";
    private final String VideoAxisFile = "/sys/class/video/axis";
    private final String request2XScaleFile = "/sys/class/graphics/fb0/request2XScale";
    private final String scaleAxisOsd1File = "/sys/class/graphics/fb1/scale_axis";
    private final String scaleOsd1File = "/sys/class/graphics/fb1/scale";
    private final String blankFb0File = "/sys/class/graphics/fb0/blank";
    private final static String sel_480ioutput_x = "ubootenv.var.480ioutputx";
    private final static String sel_480ioutput_y = "ubootenv.var.480ioutputy";
    private final static String sel_480ioutput_width = "ubootenv.var.480ioutputwidth";
    private final static String sel_480ioutput_height = "ubootenv.var.480ioutputheight";
    private final static String sel_480poutput_x = "ubootenv.var.480poutputx";
    private final static String sel_480poutput_y = "ubootenv.var.480poutputy";
    private final static String sel_480poutput_width = "ubootenv.var.480poutputwidth";
    private final static String sel_480poutput_height = "ubootenv.var.480poutputheight";
    private final static String sel_576ioutput_x = "ubootenv.var.576ioutputx";
    private final static String sel_576ioutput_y = "ubootenv.var.576ioutputy";
    private final static String sel_576ioutput_width = "ubootenv.var.576ioutputwidth";
    private final static String sel_576ioutput_height = "ubootenv.var.576ioutputheight";
    private final static String sel_576poutput_x = "ubootenv.var.576poutputx";
    private final static String sel_576poutput_y = "ubootenv.var.576poutputy";
    private final static String sel_576poutput_width = "ubootenv.var.576poutputwidth";
    private final static String sel_576poutput_height = "ubootenv.var.576poutputheight";
    private final static String sel_720poutput_x = "ubootenv.var.720poutputx";
    private final static String sel_720poutput_y = "ubootenv.var.720poutputy";
    private final static String sel_720poutput_width = "ubootenv.var.720poutputwidth";
    private final static String sel_720poutput_height = "ubootenv.var.720poutputheight";
    private final static String sel_1080ioutput_x = "ubootenv.var.1080ioutputx";
    private final static String sel_1080ioutput_y = "ubootenv.var.1080ioutputy";
    private final static String sel_1080ioutput_width = "ubootenv.var.1080ioutputwidth";
    private final static String sel_1080ioutput_height = "ubootenv.var.1080ioutputheight";
    private final static String sel_1080poutput_x = "ubootenv.var.1080poutputx";
    private final static String sel_1080poutput_y = "ubootenv.var.1080poutputy";
    private final static String sel_1080poutput_width = "ubootenv.var.1080poutputwidth";
    private final static String sel_1080poutput_height = "ubootenv.var.1080poutputheight";
    private static final int OUTPUT480_FULL_WIDTH = 720;
    private static final int OUTPUT480_FULL_HEIGHT = 480;
    private static final int OUTPUT576_FULL_WIDTH = 720;
    private static final int OUTPUT576_FULL_HEIGHT = 576;
    private static final int OUTPUT720_FULL_WIDTH = 1280;
    private static final int OUTPUT720_FULL_HEIGHT = 720;
    private static final int OUTPUT1080_FULL_WIDTH = 1920;
    private static final int OUTPUT1080_FULL_HEIGHT = 1080;
    public static SystemWriteManager sw;
    private boolean hasCvbsOutput = false;
    
    private final String[] mOutputModeList = {
        "480i",  
        "480p",
        "576i",  
        "576p",         
        "720p",
        "1080i", 
        "1080p",
        "720p50hz",
        "1080i50hz",
        "1080p50hz",
        "480cvbs",
        "576cvbs",
    };
    private final String[] mOutputModeList_50hz = {
        "480i",  
        "480p",
        "576i",  
        "576p",         
        "720p",
        "1080i",
        "1080p",
        "720p50hz",
        "1080i50hz", 
        "1080p50hz",
    };
    
    static{
        System.loadLibrary("outputsettings");
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        sw = (SystemWriteManager)context.getSystemService("system_write");
        hasCvbsOutput = sw.getPropertyBoolean("ro.platform.has.cvbsmode", false);
        // boot completed
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            if(sw.getPropertyInt(STR_1080SCALE, 0) == 2){
                writeFileJNI(VideoAxisFile, "0 0 1280 720");
                writeFileJNI(DispFile, "1280 720");
            }
        }
        // change output mode
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTMODE_CHANGE)){
            int[] curPosition = {0, 0, 1280, 720};
            
            String outputMode = intent.getStringExtra(OUTPUT_MODE);
            Config.Logd(getClass().getName(), "outputMode is: " + outputMode);
            if (hasCvbsOutput && (outputMode.equals("576i") || outputMode.equals("480i"))){
                if(outputMode.equals("480i"))
                    outputMode = "480cvbs";
                if(outputMode.equals("576i"))
                    outputMode = "576cvbs";
                disableVpp2();
            }
        
            for(int new_index = 0; new_index < mOutputModeList.length; new_index++){
                if(outputMode.equalsIgnoreCase(mOutputModeList[new_index])){
                    Config.Logd("onReceive", "new mode is: " + mOutputModeList[new_index]);
                    
                    String old_mode = readFileJNI(OutputModeFile);
                    Config.Logd(getClass().getName(), "old_mode is: " + old_mode);

                    for(int old_index = 0; old_index < mOutputModeList.length; old_index++){
                        if(old_mode.equalsIgnoreCase(mOutputModeList[old_index])){
                            if(new_index != old_index){
                                // curPosition = getPosition(outputMode);
                                
                                if(outputMode.contains("720") || outputMode.contains("1080")){
                                        writeFileJNI(OutputModeFile, mOutputModeList_50hz[new_index]);
                                }
                                else{
                                        writeFileJNI(OutputModeFile, mOutputModeList[new_index]);
                                }
                            }
                            curPosition = getPosition(outputMode);
                            
                            writeFileJNI(PpscalerRectFile,
                                        curPosition[0] + " " +
                                        curPosition[1] + " " +
                                        (curPosition[2] + curPosition[0] - 1) + " " +
                                        (curPosition[3] + curPosition[1] - 1) + " " + 0);
                            writeFileJNI(FreescaleFb0File, "0");
                            writeFileJNI(FreescaleFb1File, "0");
                            writeFileJNI(FreescaleFb0File, "1");
                            writeFileJNI(FreescaleFb1File, "1");
                        }
                    }
                }
            }
            if (hasCvbsOutput && (!( outputMode.equals("576i") || outputMode.equals("480i")
                    ||outputMode.equals("576cvbs") || outputMode.equals("480cvbs")))){
                String cvbs_mode = sw.getProperty("ubootenv.var.cvbsmode");
                setCvbsMode(cvbs_mode, outputMode);
            }
        }
        //cancel output mode change
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTMODE_CANCEL)){
            int[] curPosition = {0, 0, 1280, 720};
            String old_mode = sw.getProperty(STR_OUTPUT_VAR);
            // String old_mode = Config.getCurrentOutputResolution();
            old_mode = sw.getProperty(STR_OUTPUT_VAR);    
            if (hasCvbsOutput && (old_mode.equals("576i") || old_mode.equals("480i"))){
                if(old_mode.equals("480i"))
                    old_mode = "480cvbs";
                if(old_mode.equals("576i"))
                    old_mode = "576cvbs";
                disableVpp2();
            }
            
            curPosition = getPosition(old_mode);
            
            writeFileJNI(OutputModeFile, old_mode);
            writeFileJNI(PpscalerRectFile,
                    curPosition[0] + " " +
                    curPosition[1] + " " +
                    (curPosition[2] + curPosition[0] - 1) + " " +
                    (curPosition[3] + curPosition[1] - 1) + " " + 0);
            writeFileJNI(FreescaleFb0File, "0");
            writeFileJNI(FreescaleFb1File, "0");
            writeFileJNI(FreescaleFb0File, "1");
            writeFileJNI(FreescaleFb1File, "1");
        
            if (hasCvbsOutput && (!( old_mode.equals("576i") || old_mode.equals("480i")
                    ||old_mode.equals("576cvbs") || old_mode.equals("480cvbs")))){
                //int cvbs_mode = intent.getIntExtra("cvbs_mode", 0);
                String cvbs_mode = sw.getProperty("ubootenv.var.cvbsmode");
                setCvbsMode(cvbs_mode, old_mode);
            }
        }
        //save output mode change
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTMODE_SAVE)){
            String outputMode = intent.getStringExtra(OUTPUT_MODE);
            SystemProperties.set(STR_OUTPUT_VAR, outputMode);
        }
        // change output position
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTPOSITION_CHANGE)){
            int x = intent.getIntExtra(OUTPUT_POSITION_X, -1);
            int y = intent.getIntExtra(OUTPUT_POSITION_Y, -1);
            int w = intent.getIntExtra(OUTPUT_POSITION_W, -1);
            int h = intent.getIntExtra(OUTPUT_POSITION_H, -1);
            int mode = intent.getIntExtra(OUTPUT_POSITION_MODE, -1);
            
            if((x != -1) && (y != -1) && (w!= -1) && (h != -1) && (mode != -1)){
                writeFileJNI(PpscalerRectFile, x + " " + y + " " + w + " " + h + " " + mode);
            }
        }
        //cancel output position change
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTPOSITION_CANCEL)){
            int[] curPosition = {0, 0, 1280, 720};
            // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
            String cur_mode = Config.getCurrentOutputResolution();
            curPosition = getPosition(cur_mode);
            writeFileJNI(PpscalerRectFile,
                    curPosition[0] + " " +
                    curPosition[1] + " " +
                    (curPosition[2] + curPosition[0] - 1) + " " +
                    (curPosition[3] + curPosition[1] - 1) + " " + 0);
        }
        //save output position change
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTPOSITION_SAVE)){
            int x = intent.getIntExtra(OUTPUT_POSITION_X, -1);
            int y = intent.getIntExtra(OUTPUT_POSITION_Y, -1);
            int w = intent.getIntExtra(OUTPUT_POSITION_W, -1);
            int h = intent.getIntExtra(OUTPUT_POSITION_H, -1);
            if((x != -1) && (y != -1) && (w!= -1) && (h != -1))
            {
                savePosition(String.valueOf(x), String.valueOf(y), String.valueOf(w), String.valueOf(h));
            }
        }
        //set and save output position to default values
        else if(intent.getAction().equalsIgnoreCase(ACTION_OUTPUTPOSITION_DEFAULT_SAVE)){
            int w = 1280, h = 720;
            // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
            String cur_mode = Config.getCurrentOutputResolution();
            if((cur_mode.equals(mOutputModeList[0])) || (cur_mode.equals(mOutputModeList[1]))){
                w = OUTPUT480_FULL_WIDTH;
                h = OUTPUT480_FULL_HEIGHT;
            }
            else if((cur_mode.equals(mOutputModeList[2])) || (cur_mode.equals(mOutputModeList[3]))){
                w = OUTPUT576_FULL_WIDTH;
                h = OUTPUT576_FULL_HEIGHT;
            }
            else if((cur_mode.equals(mOutputModeList[4])) || (cur_mode.equals(mOutputModeList[7]))){
                w = OUTPUT720_FULL_WIDTH;
                h = OUTPUT720_FULL_HEIGHT;              
            }
            else if((cur_mode.equals(mOutputModeList[5])) || (cur_mode.equals(mOutputModeList[6]))
                || (cur_mode.equals(mOutputModeList[8])) || (cur_mode.equals(mOutputModeList[9])) ){
                w = OUTPUT1080_FULL_WIDTH;
                h = OUTPUT1080_FULL_HEIGHT;
            }
            else{
                w = OUTPUT720_FULL_WIDTH;
                h = OUTPUT720_FULL_HEIGHT;
            }
            writeFileJNI(PpscalerRectFile,
                    "0 0 " +
                    (w - 1) + " " +
                    (h - 1) + " " + 0);
            savePosition("0", "0", String.valueOf(w), String.valueOf(h));
        }
        // disp  width height
        else if(intent.getAction().equalsIgnoreCase(ACTION_DISP_CHANGE)){
            int w = intent.getIntExtra(DISP_W, -1);
            int h = intent.getIntExtra(DISP_H, -1);

            if((w != -1) && (h != -1)){
                writeFileJNI(DispFile, w + " " + h);
            }   
        }
        //real video on
        else if(intent.getAction().equalsIgnoreCase(ACTION_REALVIDEO_ON)){
            writeFileJNI(blankFb0File, "1");        //surfaceflinger will set back to 0
            // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
            String cur_mode = Config.getCurrentOutputResolution();
            writeFileJNI(PpscalerFile, "0");
            writeFileJNI(FreescaleFb0File, "0");
            writeFileJNI(FreescaleFb1File, "0");
            if((cur_mode.equals(mOutputModeList[0])) || (cur_mode.equals(mOutputModeList[1]))){
                writeFileJNI(request2XScaleFile, "16 720 480");
                writeFileJNI(scaleAxisOsd1File, "1280 720 720 480");
                writeFileJNI(scaleOsd1File, "0x10001");
            }
            else if((cur_mode.equals(mOutputModeList[2])) || (cur_mode.equals(mOutputModeList[3]))){
                writeFileJNI(request2XScaleFile, "16 720 576");
                writeFileJNI(scaleAxisOsd1File, "1280 720 720 576");
                writeFileJNI(scaleOsd1File, "0x10001");
            }
            else if((cur_mode.equals(mOutputModeList[5])) || (cur_mode.equals(mOutputModeList[6])) 
                            || (cur_mode.equals(mOutputModeList[8])) || (cur_mode.equals(mOutputModeList[9]))){
                writeFileJNI(request2XScaleFile, "8");
                writeFileJNI(scaleAxisOsd1File, "1280 720 1920 1080");
                writeFileJNI(scaleOsd1File, "0x10001");
            }
            else if((cur_mode.equals(mOutputModeList[4])) || (cur_mode.equals(mOutputModeList[7]))){
                    writeFileJNI(request2XScaleFile, "16 1280 720");  //for setting blank to 0
            }
            else{
                writeFileJNI(request2XScaleFile, "16 1280 720");    //for setting blank to 0
            }
        }
        //real video off
        else if(intent.getAction().equalsIgnoreCase(ACTION_REALVIDEO_OFF)){
            int[] curPosition = {0, 0, 1280, 720};
            writeFileJNI(blankFb0File, "1");        //surfaceflinger will set back to 0
            // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
            String cur_mode = Config.getCurrentOutputResolution();
            curPosition = getPosition(cur_mode);
            writeFileJNI(FreescaleFb0File, "0");
            writeFileJNI(FreescaleFb1File, "0");
            writeFileJNI(VideoAxisFile, "0 0 1280 720");
            writeFileJNI(PpscalerFile, "1");
            writeFileJNI(PpscalerRectFile,
                    curPosition[0] + " " +
                    curPosition[1] + " " +
                    (curPosition[2] + curPosition[0] - 1) + " " +
                    (curPosition[3] + curPosition[1] - 1) + " " + "0");
            writeFileJNI(scaleOsd1File, "0");
            writeFileJNI(request2XScaleFile, "2");
            writeFileJNI(FreescaleFb0File, "1");
            writeFileJNI(FreescaleFb1File, "1");
        }
        //change video position when disable freescale
        else if(intent.getAction().equalsIgnoreCase(ACTION_VIDEOPOSITION_CHANGE)){
            int[] curPosition = {0, 0, 0, 0};
            // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
            String cur_mode = Config.getCurrentOutputResolution();
            curPosition = getPosition(cur_mode);
            writeFileJNI(VideoAxisFile,
                    curPosition[0] + " " +
                    curPosition[1] + " " +
                    (curPosition[2] + curPosition[0] - 1) + " " +
                    (curPosition[3] + curPosition[1] - 1));
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_CVBSMODE_CHANGE)){
            // String outputmode = sw.getProperty(STR_OUTPUT_VAR);
            String outputmode = Config.getCurrentOutputResolution();
            String cvbs_mode = intent.getStringExtra("cvbs_mode");
            setCvbsMode(cvbs_mode, outputmode);
        }
        
        Log.d("OutputSettingsBroadcastReceiver","Action:"+ intent.getAction());
    }

    private int[] getPosition(String mode){
            Config.Logd(getClass().getName(), "getPosition(), mode is: " + mode);
            
        int[] curPosition = {0, 0, 1280, 720};
        int index = 4;  //720p
        for(int i = 0; i<mOutputModeList.length; i++){
            if(mode.equalsIgnoreCase(mOutputModeList[i]))
                index = i;
        }
        
        Config.Logd(getClass().getName(), "getPosition(), index is: " + index);
        
        switch(index){
            case 0:     //480i
                curPosition[0] = sw.getPropertyInt(sel_480ioutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_480ioutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_480ioutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_480ioutput_height, 480);
                break;
            case 1:     //480p
                curPosition[0] = sw.getPropertyInt(sel_480poutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_480poutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_480poutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_480poutput_height, 480);
                break;
            case 2:     //576i
                curPosition[0] = sw.getPropertyInt(sel_576ioutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_576ioutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_576ioutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_576ioutput_height, 576);
                break;
            case 3:     //576p
                curPosition[0] = sw.getPropertyInt(sel_576poutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_576poutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_576poutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_576poutput_height, 576);
                break;
            case 4:     //720p
            case 7:            // 720p50hz
                curPosition[0] = sw.getPropertyInt(sel_720poutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_720poutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_720poutput_width, 1280);
                curPosition[3] = sw.getPropertyInt(sel_720poutput_height, 720);
                break;
                
            case 5:     //1080i
            case 8:            // 1080i50hz
                curPosition[0] = sw.getPropertyInt(sel_1080ioutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_1080ioutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_1080ioutput_width, 1920);
                curPosition[3] = sw.getPropertyInt(sel_1080ioutput_height, 1080);
                break;
                
            case 6:     //1080p
            case 9:            // 1080p50hz
                curPosition[0] = sw.getPropertyInt(sel_1080poutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_1080poutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_1080poutput_width, 1920);
                curPosition[3] = sw.getPropertyInt(sel_1080poutput_height, 1080);
                break;
            case 10:        //480cvbs
                curPosition[0] = sw.getPropertyInt(sel_480ioutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_480ioutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_480ioutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_480ioutput_height, 480);
                break;
            case 11:        //576cvbs
                curPosition[0] = sw.getPropertyInt(sel_576ioutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_576ioutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_576ioutput_width, 720);
                curPosition[3] = sw.getPropertyInt(sel_576ioutput_height, 576);
                break;
            default:    //720p
                curPosition[0] = sw.getPropertyInt(sel_720poutput_x, 0);
                curPosition[1] = sw.getPropertyInt(sel_720poutput_y, 0);
                curPosition[2] = sw.getPropertyInt(sel_720poutput_width, 1280);
                curPosition[3] = sw.getPropertyInt(sel_720poutput_height, 720);
                break;
        }
        
        int tmp_index = 0;
        for(tmp_index = 0; tmp_index < curPosition.length; tmp_index++){
                Config.Logd(getClass().getName(), curPosition[tmp_index] + "");
        }
        
        return curPosition;
    }
    
    private void savePosition(String x, String y, String w, String h){
        // String cur_mode = sw.getProperty(STR_OUTPUT_VAR);
        String cur_mode = Config.getCurrentOutputResolution();
        Config.Logd(getClass().getName(), "savePosition(), cur_mode is: " + cur_mode);
        
        int index = 4;  //720p
        for(int i=0; i < mOutputModeList.length; i++){
            if(cur_mode.equalsIgnoreCase(mOutputModeList[i])){
                index = i;
            }
        }
        
        Config.Logd(getClass().getName(), "savePosition(), index is: " + index);
        
        switch(index){
        case 0:     //480i
            SystemProperties.set(sel_480ioutput_x, x);
            SystemProperties.set(sel_480ioutput_y, y);
            SystemProperties.set(sel_480ioutput_width, w);
            SystemProperties.set(sel_480ioutput_height, h);
            break;
        case 1:     //480p
            SystemProperties.set(sel_480poutput_x, x);
            SystemProperties.set(sel_480poutput_y, y);
            SystemProperties.set(sel_480poutput_width, w);
            SystemProperties.set(sel_480poutput_height, h);
            break;
        case 2:     //576i
            SystemProperties.set(sel_576ioutput_x, x);
            SystemProperties.set(sel_576ioutput_y, y);
            SystemProperties.set(sel_576ioutput_width, w);
            SystemProperties.set(sel_576ioutput_height, h);
            break;
        case 3:     //576p
            SystemProperties.set(sel_576poutput_x, x);
            SystemProperties.set(sel_576poutput_y, y);
            SystemProperties.set(sel_576poutput_width, w);
            SystemProperties.set(sel_576poutput_height, h);
            break;
        case 4:     //720p
        case 7:
            SystemProperties.set(sel_720poutput_x, x);
            SystemProperties.set(sel_720poutput_y, y);
            SystemProperties.set(sel_720poutput_width, w);
            SystemProperties.set(sel_720poutput_height, h);
            break;
        case 5:     //1080i
        case 8:
            SystemProperties.set(sel_1080ioutput_x, x);
            SystemProperties.set(sel_1080ioutput_y, y);
            SystemProperties.set(sel_1080ioutput_width, w);
            SystemProperties.set(sel_1080ioutput_height, h);
            break;
        case 6:     //1080p
        case 9:
            SystemProperties.set(sel_1080poutput_x, x);
            SystemProperties.set(sel_1080poutput_y, y);
            SystemProperties.set(sel_1080poutput_width, w);
            SystemProperties.set(sel_1080poutput_height, h);
            break;
        }
    }
    
public void  disableVpp2(){
    writeFileJNI("/sys/class/display2/mode", "null");
    //SystemProperties.set("ubootenv.var.cvbsmode", "null");
}
                
public void setCvbsMode(String mode, String valOutputMode){   
    if (!( valOutputMode.equals("576i") || valOutputMode.equals("480i"))){
        if (mode.equals("480cvbs")) {
            writeFileJNI("/sys/class/display2/mode", "null");
            writeFileJNI("/sys/class/display2/mode", "480cvbs");
            writeFileJNI("/sys/class/video2/screen_mode", "1");
            SystemProperties.set("ubootenv.var.cvbsmode", "480cvbs");
                    
        } else if(mode.equals("576cvbs")){          
            writeFileJNI("/sys/class/display2/mode", "null");
            writeFileJNI("/sys/class/display2/mode", "576cvbs");
            writeFileJNI("/sys/class/video2/screen_mode", "1");
            SystemProperties.set("ubootenv.var.cvbsmode", "576cvbs");             
        }else{
            disableVpp2();
        }
        if (valOutputMode.contains("1080")){
            writeFileJNI( "/sys/module/amvideo2/parameters/clone_frame_scale_width", "960");
                    
        }else {
            writeFileJNI( "/sys/module/amvideo2/parameters/clone_frame_scale_width", "0");
        }
    }else {
        disableVpp2();
    }
    
}
    
    public native int writeFileJNI(String fileName, String str);
    public native String readFileJNI(String fileName);
}
