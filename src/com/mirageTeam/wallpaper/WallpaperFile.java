package com.mirageTeam.wallpaper;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.launcherui.ui.MainUI;
import com.mirageTeam.widget.ConfirmDialog;
import com.mirageTeam.widget.WallPaperDialog;
import com.mirageteam.launcher.market.Util;
import com.mirageteam.launcher.setting.OtherSetting;
 

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class WallpaperFile extends ListActivity {
  
  private List<String> items = null;   //items filename
  private List<String> paths = null;   //paths file path
  private String rootPath="/mnt";         //rootPath first path

  private String currentPath="";
 
  private String TAG = "WallpaperFile";
  

  private final static int SHOW_SUCESS_TIP = 0x111;
  Handler handler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what) {
		case SHOW_SUCESS_TIP:
			 Toast.makeText(WallpaperFile.this, getResources().getString(R.string.wallpaper_set_sucess), 1500).show();
			break;
		default:
			break;
		}
	}
	  
  };

  @Override  
  public boolean onKeyDown(int keyCode,KeyEvent event) {   
  
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          File file = new File(currentPath);
          if(rootPath.equals(currentPath)){
            return super.onKeyDown(keyCode,event); 
          }else{
            getFileDir(file.getParent());
            return true; 
          }
      } else{  
          return super.onKeyDown(keyCode,event); 
      }
  }  

  @Override
  protected void onCreate(Bundle icicle){
    super.onCreate(icicle);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    setContentView(R.layout.wallpaper_filemain);
   

    currentPath = rootPath;
    getFileDir(rootPath);
  }
  

 
  @Override
  protected void onListItemClick(ListView l,View v,int position,long id){
    File file = new File(paths.get(position));
    fileOrDirHandle(file);
  }


  private void fileOrDirHandle(final File file){
    
    if(file.isDirectory()){
      getFileDir(file.getPath());
    }else{
      openFile(file);
    }
  
}  
  
  /**
   * 
   * @param filePath
   */
  private void getFileDir(String filePath){
	Log.v(TAG, "filepath>>>>>"+filePath);
    currentPath = filePath;
    items = new ArrayList<String>();
    paths = new ArrayList<String>();
    File f = new File(filePath);  
    File[] files = f.listFiles();
    if(files!=null){
        for(int i=0;i<files.length;i++){
          if(files[i].isDirectory()){
            items.add(files[i].getName());
            paths.add(files[i].getPath());
          }
        }
        for(int i=0;i<files.length;i++){
          if(files[i].isFile()){
            items.add(files[i].getName());
            paths.add(files[i].getPath());
          }
        }
    }
    setListAdapter(new WallpaperFileListAdapter(this,items,paths));
  }
  

  

  private void openFile(final File f){
	    Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setAction(android.content.Intent.ACTION_VIEW);

	    String type = "*/*";
	    type = Util.getMIMEType(f,true); 
	    Log.v(TAG, ">>>type="+type);
	    if(type.equals("image/*")){
	    	
	    	final ConfirmDialog mConfirmDialog = new ConfirmDialog(this);
			
			mConfirmDialog.setDialogTitle(R.string.dialog_title).setPositiveButton(R.string.ok, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								 //copy current file to data
							      CommonUtils.saveWallPaperCustomMark(WallpaperFile.this, true);
							      
							      copyFile(f.getAbsolutePath(), "/data/data/"+getPackageName()+"/wallpaper/");
							      //send message set wallpaper
								
								
								   Intent intent = new Intent(MainUI.UPDATE_WALLPAPER_BROATCAST_ACTION);
								   intent.putExtra("set_wallpaper_id", -1);
								   sendBroadcast(intent);
								   
								   handler.sendEmptyMessage(SHOW_SUCESS_TIP);
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}).start();
					

					mConfirmDialog.dismiss();
					
				}
			}).setNegativeButton(R.string.cancel, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mConfirmDialog.dismiss();
					
				}
			}).show();
	     
	      
	    }

//	    intent.setDataAndType(Uri.fromFile(f),type);
//	    startActivity(intent); 
	  }
	  
	  
	  public boolean copyFile(String oldPath, String newPath) {
	    try {
	      int bytesum = 0;
	      int byteread = 0;
	      String f_new = "";
	      String f_new_name = "default_wallpaper.png";
	      File f_old = new File(oldPath);
//	      if(newPath.endsWith(File.separator)){
//	        f_new = newPath+f_new_name;
//	      } else{
//	        f_new = newPath+File.separator+f_new_name;
//	      }
	      Log.v(TAG, "old file>>"+oldPath);
	      f_new = newPath+f_new_name;
	      Log.v(TAG, "new file>>"+f_new);
	      new File(newPath).mkdirs();             
	      new File(f_new).createNewFile();         

	      if (f_old.exists()) { 
	        InputStream inStream = new FileInputStream(oldPath); 
	        FileOutputStream fs = new FileOutputStream(f_new);
	        byte[] buffer = new byte[1444];
	        while ( (byteread = inStream.read(buffer)) != -1) {
	          bytesum += byteread;  
	          fs.write(buffer, 0, byteread);
	        }
	        inStream.close();
	      }
	    }catch (Exception e) {
	      return false;

	    }
	     return true;
	  }
	  
}
