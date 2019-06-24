package com.mirageTeam.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.mirageTeam.launcherui.R;

public class ManuallyUpdateDialog extends Dialog {
	private Context context = null;
	private static ManuallyUpdateDialog manuallyUpdateDialog = null;

	public ManuallyUpdateDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ManuallyUpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	public static ManuallyUpdateDialog createDialog(Context context) {
		manuallyUpdateDialog = new ManuallyUpdateDialog(context, R.style.Theme_CustomDialog);
		manuallyUpdateDialog.setContentView(R.layout.manually_update_dialog);
		manuallyUpdateDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		android.view.WindowManager.LayoutParams lay = manuallyUpdateDialog.getWindow().getAttributes();

		// 对话框高宽
		lay.height = 720;
		lay.width = 1280;

		return manuallyUpdateDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (manuallyUpdateDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) manuallyUpdateDialog.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}

}