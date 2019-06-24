package com.mirageTeam.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import com.mirageTeam.launcherui.R;

public abstract class ApplistBaseBottomMenu extends PopupWindow {

	private View mMenuView;
	protected Context mContext;

	public ApplistBaseBottomMenu(Context context) {
		super(context);

		mContext = context;

//		mMenuView = LayoutInflater.from(context).inflate(
//				R.layout.menu_common_layout, null);
		
		mMenuView=baseBottomMenuContentView();

		setContentView(mMenuView);

		setWidth(LayoutParams.FILL_PARENT);

		setHeight(LayoutParams.FILL_PARENT);

		setFocusable(true);

		setAnimationStyle(R.style.AnimBottom);

		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);

		
		//setBackgroundDrawable(context.getResources().getDrawable(R.drawable.menu_background));

	}
	 

	abstract View baseBottomMenuContentView();
	
	public void show() {
		showAtLocation(((Activity) mContext).getWindow().getDecorView(),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
}