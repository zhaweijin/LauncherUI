package com.mirageTeam.widget;

import com.mirageTeam.launcherui.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateTipDialog extends Dialog {
	private Context mContext;

	TextView title;
	TextView message;
	Button positiveButton, negativeButton;

	public UpdateTipDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.update_tip_dialog, null);
		setContentView(view);

		title = (TextView) view.findViewById(R.id.title);

		message = (TextView)view.findViewById(R.id.message);
		
		positiveButton = (Button) view.findViewById(R.id.setting_ok);

		negativeButton = (Button) view.findViewById(R.id.setting_cancel);

		android.view.WindowManager.LayoutParams lay = getWindow().getAttributes();

		//对话框高宽
		lay.height = 350;
		lay.width = 1280;

	}

	public UpdateTipDialog setDialogTitle(int strResource) {
		this.title.setText(mContext.getString(strResource));
		return this;
	}
	public UpdateTipDialog setDialogTitle(String title) {
		this.title.setText(title);
		return this;
	}
	
	public UpdateTipDialog setDialogMessage(String message) {
		this.message.setText(message);
		return this;
	}

	public UpdateTipDialog setPositiveButton(int strResource, android.view.View.OnClickListener onclick) {
		return setPositiveButton(mContext.getString(strResource), onclick);
	}

	public UpdateTipDialog setPositiveButton(String str, android.view.View.OnClickListener onclick) {
		this.positiveButton.setText(str);
		this.positiveButton.setOnClickListener(onclick);
		return this;
	}

	public UpdateTipDialog setNegativeButton(int strResource, android.view.View.OnClickListener onclick) {
		return setNegativeButton(mContext.getString(strResource), onclick);
	}

	public UpdateTipDialog setNegativeButton(String str, android.view.View.OnClickListener onclick) {
		negativeButton.setText(str);
		negativeButton.setOnClickListener(onclick);
		return this;
	}

}
