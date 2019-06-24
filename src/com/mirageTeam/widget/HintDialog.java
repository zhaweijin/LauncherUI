package com.mirageTeam.widget;

import com.mirageTeam.launcherui.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class HintDialog extends Dialog {
	private Context mContext;

	TextView title;
	Button positiveButton, negativeButton;

	public HintDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.hint_dialog, null);
		setContentView(view);

		title = (TextView) view.findViewById(R.id.hint_title);

		positiveButton = (Button) view.findViewById(R.id.hint_ok);

		android.view.WindowManager.LayoutParams lay = getWindow().getAttributes();

		//对话框高宽
		lay.height = 250;
		lay.width = 1280;

		 
	}

	public HintDialog setDialogTitle(int strResource) {
		this.title.setText(mContext.getString(strResource));
		return this;
	}
	public HintDialog setDialogTitle(String title) {
		this.title.setText(title);
		return this;
	}

	public HintDialog setPositiveButton(int strResource, android.view.View.OnClickListener onclick) {
		return setPositiveButton(mContext.getString(strResource), onclick);
	}

	public HintDialog setPositiveButton(String str, android.view.View.OnClickListener onclick) {
		this.positiveButton.setText(str);
		this.positiveButton.setOnClickListener(onclick);
		return this;
	}

	// public HintDialog setNegativeButton(int strResource,
	// android.view.View.OnClickListener onclick) {
	// return setNegativeButton(mContext.getString(strResource), onclick);
	// }
	//
	// public HintDialog setNegativeButton(String str,
	// android.view.View.OnClickListener onclick) {
	// negativeButton.setText(str);
	// negativeButton.setOnClickListener(onclick);
	// return this;
	// }
	
	
	public void setDialogSysteType(){
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
	}

}
