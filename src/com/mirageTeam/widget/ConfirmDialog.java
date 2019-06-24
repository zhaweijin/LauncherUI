package com.mirageTeam.widget;

import com.mirageTeam.launcherui.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends Dialog {
	private Context mContext;

	TextView title;
	Button positiveButton, negativeButton;

	public ConfirmDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.confirm_dialog, null);
		setContentView(view);

		title = (TextView) view.findViewById(R.id.title);

		positiveButton = (Button) view.findViewById(R.id.setting_ok);

		negativeButton = (Button) view.findViewById(R.id.setting_cancel);

		android.view.WindowManager.LayoutParams lay = getWindow().getAttributes();

		//对话框高宽
		lay.height = 250;
		lay.width = 1280;

	}

	public ConfirmDialog setDialogTitle(int strResource) {
		this.title.setText(mContext.getString(strResource));
		return this;
	}
	public ConfirmDialog setDialogTitle(String title) {
		this.title.setText(title);
		return this;
	}

	public ConfirmDialog setPositiveButton(int strResource, android.view.View.OnClickListener onclick) {
		return setPositiveButton(mContext.getString(strResource), onclick);
	}

	public ConfirmDialog setPositiveButton(String str, android.view.View.OnClickListener onclick) {
		this.positiveButton.setText(str);
		this.positiveButton.setOnClickListener(onclick);
		return this;
	}

	public ConfirmDialog setNegativeButton(int strResource, android.view.View.OnClickListener onclick) {
		return setNegativeButton(mContext.getString(strResource), onclick);
	}

	public ConfirmDialog setNegativeButton(String str, android.view.View.OnClickListener onclick) {
		negativeButton.setText(str);
		negativeButton.setOnClickListener(onclick);
		return this;
	}

}
