package com.mirageteam.launcher.market;

import java.util.ArrayList;
import java.util.Random;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.launcherui.ui.BaseActivity;
import com.mirageTeam.widget.CleanMemoryBottomMenu;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

 
	
	private ImageView topImageView;
	private GridView softwareGridView;
	
	private ListView listView;
	
	private MarketAdapter marketAdapter;
	private DataSet dataSet;
	
	private ProgressBar main_progressbar;
	private Bitmap topDiplayBitmap;
	
//	private String market_webpath = "http://update.xuzhitech.com/apkmis/apklist.php?sid=";
//	private String market_webpath = "lsrivate final static int MEDIAPLAY_ID = 2;
	
	private String market_webpath = "http://www.seatay.com.cn/apkmis/apklist.php?sid=";
    private final static int TUIJIAN_ID = 1;
	private final static int GAME_ID = 3;
	private final static int NEWS_ID = 4;
	private int currentPosition;
	
	private static final int DISPLAY_TOP_IMAGEVIEW =0x111;
	private static final int DISPLAY_SOFTWARE_DATA =0x112;
	private static final int DISPLAY_TIMEOUT = 0x113;
	private static final int DISPLAY_LEFT = 0x114;
	
	private FenleiAdapter fenleiAdapter;
	private boolean isLoadFenlei = false;
	
	
	private ArrayList<String> demoDatas = new ArrayList<String>();
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case DISPLAY_SOFTWARE_DATA:
				displaySoftwareData();
				
				if(!isLoadFenlei){
					isLoadFenlei = true;
					Log.v("load>>", "load fenlei");
					if(dataSet!=null){
						if(fenleiAdapter==null)
							fenleiAdapter = new FenleiAdapter(MainActivity.this, dataSet.getFenleiDatas());
						listView.setAdapter(fenleiAdapter);
					}
				}
				main_progressbar.setVisibility(View.INVISIBLE);
				break;
			case DISPLAY_TOP_IMAGEVIEW:
				if(topDiplayBitmap!=null)
					topImageView.setImageBitmap(topDiplayBitmap);
				break;
			case DISPLAY_TIMEOUT:
				Toast.makeText(MainActivity.this, getResources().getString(R.string.timeout), 1500).show();
				main_progressbar.setVisibility(View.INVISIBLE);
				break;
			case DISPLAY_LEFT:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.market_main);
		
		initlayout();
		loadData(TUIJIAN_ID+"");
	}

	private void initlayout(){
		listView = (ListView)findViewById(R.id.listview);	
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				fenleiAdapter.setSelectItem(arg2);
				fenleiAdapter.notifyDataSetChanged();
		    	
		    	loadData(dataSet.getFenleiDatas().get(arg2).getId());
			}
			
		});
		 
		
		listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				fenleiAdapter.setSelectItem(arg2);
				fenleiAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		topImageView = (ImageView)findViewById(R.id.top_image);
		softwareGridView = (GridView)findViewById(R.id.market_gridview);
		softwareGridView.setOnItemClickListener(onItemClickListener);
		
		main_progressbar = (ProgressBar) findViewById(R.id.main_progressbar);
		
		 
		 
	}
 
	
	
	private void loadData(final String id)
	{
 
		main_progressbar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			public void run() {
				try {
					if (setAdapterData(id) != null)
						handler.sendEmptyMessage(DISPLAY_SOFTWARE_DATA);
					else
						handler.sendEmptyMessage(DISPLAY_TIMEOUT);
					
//					topDiplayBitmap = Util.GetBitmapByUrl(dataSet.getTopImages().get(Integer.parseInt(id) -1 ));
					
					Random random = new Random();
					int topImageSize = dataSet.getTopImages().size();
					int randomId = Math.abs(random.nextInt())%topImageSize;
//					Util.print("image size", ""+topImageSize);
//					Util.print("ramdom id", ""+randomId);
					topDiplayBitmap = Util.GetBitmapByUrl(dataSet.getTopImages().get(randomId));
					
					handler.sendEmptyMessage(DISPLAY_TOP_IMAGEVIEW);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
//		handler.sendEmptyMessage(MainActivity.DISPLAY_SOFTWARE_DATA);
	}
	
	
	private DataSet setAdapterData(String id) {
		try {
			dataSet = Util.getSoftwareWebData(market_webpath+id);
//			dataSet = Util.getSoftwareWebData(market_webpath);
			return dataSet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void displaySoftwareData(){
//		marketAdapter = new MarketAdapter(this, demoDatas);
		marketAdapter = new MarketAdapter(this, dataSet);
		softwareGridView.setAdapter(marketAdapter);
	}
	
	OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,InstallActivity.class);
			intent.putExtra("marketData", dataSet.getMarketDatas().get(arg2));
			startActivity(intent);
//			showInformation();
 
		}
		
	};
	
	
	
//	private void showInformation(){
//	     Dialog dialog = new Dialog(this,R.style.dialog);
//	     
////	     LayoutInflater layoutInflater = LayoutInflater.from(this);
////		 View view = layoutInflater.inflate(R.layout.install_main, null);
//		 dialog.setContentView(R.layout.install_main);
//		 dialog.show();
//	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			event.startTracking();
			if (flag == true) {
				shortpressflag = false;
			} else {
				shortpressflag = true;
				flag = false;
			}
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			event.startTracking();
			if (shortpressflag) {
				Log.i("KEYCODE_MENU", "KEYCODE_MENU =======onKeyUp==== ");
				// Toast.makeText(mContext, "KEYCODE_MENU =======onKeyUp====",
				// Toast.LENGTH_SHORT).show();
//				showMenuOption();
			}
			shortpressflag = true;
			flag = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
