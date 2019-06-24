package com.mirageteam.launcher.market;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Test extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_main);
		
		
		
		
		
		
		ArrayList<String> fonts = new ArrayList<String>();
		for(int i=0;i<3;i++){
			fonts.add("字体"+i);
		}
		
		
		/*
		 * 对话框选择
		 */
		/*
		ListView listView = (ListView)findViewById(R.id.select_listview);
		listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,fonts));
		listView.setSelector(R.drawable.setting_dialog_select_background);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		*/
		
		
		
		
		
		
		
		
		
//		SettingDialogSelectAdapter selectAdapter = new SettingDialogSelectAdapter(this, fonts);
//		listView.setAdapter(selectAdapter);
		
		
//		
//		TextView textView = new TextView(this);
//		
//		switch (getIntent().getIntExtra("state", 1)) {
//		case 1:
//			textView.setText("network");
//			setContentView(textView);
//			break;
//		case 2:
//			textView.setText("display");
//			setContentView(textView);
//			break;
//		case 3:
//			textView.setText("volume");
//			setContentView(textView);
//			break;
//		case 5:
//			textView.setText("update");
//			setContentView(textView);
//			break;
//		}
	}
}
