package com.mirageTeam.widget;

import com.mirageTeam.launcherui.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CommonDialog extends PopupWindow{

	private View mMenuView;
	
	private TextView message;
	
	private Button okButton,cancelButton;
	
	private Context mContext;
	
	public CommonDialog(Context context){
		super(context);
		
		mContext=context;
		
		mMenuView=LayoutInflater.from(context).inflate(R.layout.dialog_common_layout, null);
		
		setContentView(mMenuView);
		
		setWidth(LayoutParams.FILL_PARENT);
		
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
		
//		setAnimationStyle(R.style.AnimBottom);
		
		ColorDrawable dw=new ColorDrawable(0xee000000);
		
		setBackgroundDrawable(dw);
		
		message=(TextView)getContentView().findViewById(R.id.dialog_message);
		
		okButton=(Button)getContentView().findViewById(android.R.id.button1);
		
		cancelButton=(Button)getContentView().findViewById(android.R.id.button2);
		
		
		
	}
	
	public CommonDialog setMessage(String message){
		this.message.setText(message);
		return this;
	}
	
	
	public CommonDialog setOkButton(int strResource,OnClickListener onclick){
		return setOkButton(mContext.getString(strResource), onclick);
	}
	
	public CommonDialog setOkButton(String str,OnClickListener onclick){
		this.okButton.setText(str);
		this.okButton.setOnClickListener(onclick);
		return this;
	}
	
	public CommonDialog setCancelButton(int strResource,OnClickListener onclick){
		return setCancelButton(mContext.getString(strResource), onclick);
	}
	
	public CommonDialog setCancelButton(String str,OnClickListener onclick){
		cancelButton.setText(str);
		cancelButton.setOnClickListener(onclick);
		return this;
	}
	
	
	public void show(){
		showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
}
