package com.mirageTeam.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class HorizontalListView extends HorizontalScrollView {

	private LinearLayout layout;
	 
	public void removeItem(int position){
//		if(layout.getChildCount()>0)
//		layout.removeViewAt(position);
	}

	private BaseAdapter baseAdapter;
	public HorizontalListView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		addView(layout,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public void setAdapter(final BaseAdapter mAdapter){
	 
		layout.removeAllViews();
		invalidate();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<mAdapter.getCount();i++){
					View mView =mAdapter.getView(i, null, null);
					Message msg=mHandler.obtainMessage();
					msg.what=1;
					msg.obj=mView;
				 msg.sendToTarget();
					}
			}
		}).start();
		
		
//		mHandler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				scrollTo(layout.getMeasuredWidth(), 0);
//			}
//		}, 50);
	}

	Handler mHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			parm.leftMargin=25;  
			layout.addView((View)msg.obj, parm);
		
//			invalidate();
		};
	};
	
}
