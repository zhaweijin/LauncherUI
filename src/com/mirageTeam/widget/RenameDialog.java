package com.mirageTeam.widget;

import com.mirageTeam.launcherui.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RenameDialog extends Dialog {
	private Context mContext;

	TextView title;
	Button positiveButton, negativeButton;
	EditText rename_edit;
	
	public RenameDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
		mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.rename_dialog, null);
		setContentView(view);

		title = (TextView) view.findViewById(R.id.title);
		
		rename_edit = (EditText) view.findViewById(R.id.rename_edit);
		
		positiveButton = (Button) view.findViewById(R.id.rename_ok);
		negativeButton = (Button) view.findViewById(R.id.rename_cancel);

		android.view.WindowManager.LayoutParams lay = getWindow().getAttributes();

		//对话框高宽
		lay.height = 300;
		lay.width = 1280;

		 
	}

	public RenameDialog setDialogTitle(int strResource) {
		this.title.setText(mContext.getString(strResource));
		return this;
	}
	public RenameDialog setDialogTitle(String title) {
		this.title.setText(title);
		return this;
	}

	public RenameDialog setPositiveButton(int strResource, android.view.View.OnClickListener onclick) {
		return setPositiveButton(mContext.getString(strResource), onclick);
	}

	public RenameDialog setPositiveButton(String str, android.view.View.OnClickListener onclick) {
		this.positiveButton.setText(str);
		this.positiveButton.setOnClickListener(onclick);
		return this;
	}

	public RenameDialog setNegativeButton(int strResource, android.view.View.OnClickListener onclick) {
		return setNegativeButton(mContext.getString(strResource), onclick);
	}

	public RenameDialog setNegativeButton(String str, android.view.View.OnClickListener onclick) {
		negativeButton.setText(str);
		negativeButton.setOnClickListener(onclick);
		return this;
	}
	
	
	public void setDialogSysteType(){
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
	}

	public String getEditString() {
		return rename_edit.getText().toString();
	}
}
