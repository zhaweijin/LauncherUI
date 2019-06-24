package com.mirageteam.launcher.setting;

import java.util.ArrayList;
import java.util.List;

import com.mirageTeam.launcherui.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Test extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.setting_main);
//		
//		
//		
//		
//		
//		
//		ArrayList<String> fonts = new ArrayList<String>();
//		for(int i=0;i<3;i++){
//			fonts.add("字体"+i);
//		}
		
		
		/*
		 * 对话框选择
		 */
		/*
		ListView listView = (ListView)findViewById(R.id.select_listview);
		listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,fonts));
		listView.setSelector(R.drawable.setting_dialog_select_background);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		*/
		
		
		
//		populateWallpaperTypes();
		
		
		
		
		
//		SettingDialogSelectAdapter selectAdapter = new SettingDialogSelectAdapter(this, fonts);
//		listView.setAdapter(selectAdapter);
		
		
		
		TextView textView = new TextView(this);
		
		switch (getIntent().getIntExtra("state", 1)) {
		case 1:
			textView.setText("network");
			setContentView(textView);
			break;
		case 2:
			textView.setText("display");
			setContentView(textView);
			break;
		case 3:
			textView.setText("volume");
			setContentView(textView);
			break;
		case 5:
			textView.setText("update");
			setContentView(textView);
			break;
		}
	}
	
    private void setParams(Dialog dialog) {  
    	
    	LayoutParams lay = dialog.getWindow().getAttributes(); 
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
 
        lay.height = 400;
        lay.width = dm.widthPixels;
       } 
	
	 private void populateWallpaperTypes() {
	        // Search for activities that satisfy the ACTION_SET_WALLPAPER action
	        final Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
	        final PackageManager pm = getPackageManager();
	        final List<ResolveInfo> rList = pm.queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);

	        Dialog dialog = new Dialog(this);
	        View view = LayoutInflater.from(this).inflate(R.layout.setting_common_dialog, null);
	        dialog.setContentView(view);
	        ListView listView = new ListView(this);
	        LinearLayout contentView = (LinearLayout)view.findViewById(R.id.content);
	        
	        ArrayList<String> title = new ArrayList<String>();
	        // Add Preference items for each of the matching activities
	        for (ResolveInfo info : rList) {
//	            Intent prefIntent = new Intent(intent);
//	            prefIntent.setComponent(new ComponentName(
//	                    info.activityInfo.packageName, info.activityInfo.name));
	            CharSequence label = info.loadLabel(pm);
	            if (label == null) label = info.activityInfo.packageName;
	            title.add(label+"");
	        }
	        
	        TextView dialogTitle = (TextView)view.findViewById(R.id.title);
	        dialogTitle.setText(getResources().getString(R.string.wallpaper_manager));
	        
	        
	        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,title));
			listView.setSelector(R.drawable.setting_dialog_select_background);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			contentView.addView(listView);
			
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					 Intent prefIntent = new Intent(intent);
			         prefIntent.setComponent(new ComponentName(
			                    rList.get(arg2).activityInfo.packageName, rList.get(arg2).activityInfo.name));
			         Test.this.startActivity(prefIntent);
				}
				
			});
			
			 
	        setParams(dialog); 
			
			dialog.show();
	    }
}
