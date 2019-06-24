package com.mirageTeam.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.launcherui.R;

public class OptionDialog extends Dialog implements android.view.View.OnClickListener {

	public interface onChooseAppsOptionClick {
		public void ChooseAppsOptionClick(ApplicationCategoryType type);

		public void ChooseCatelogOptionClick(ApplicationCategoryType type);

		public void ChooseRenameOpionClick(ApplicationCategoryType type);
	};

	onChooseAppsOptionClick chooseAppsOptionClick;

	public void setOnChooseAppsOptionClick(onChooseAppsOptionClick click) {
		this.chooseAppsOptionClick = click;
	}

	Context mContext;
	ImageView imageView;
	RadioGroup group;
	RadioButton openappButton, catelognButton, renameButton;

	int drawable;
	ApplicationCategoryType type;
	boolean flag;

	public OptionDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public OptionDialog(Context context, int drawable, ApplicationCategoryType type, boolean flag) {
		super(context, R.style.CustomDialog_transparent);
		this.mContext = context;
		this.drawable = drawable;
		this.type = type;
		this.flag = flag;

		View view = LayoutInflater.from(mContext).inflate(R.layout.optiondialog, null);
		setContentView(view);

		// imageView = (ImageView) findViewById(R.id.icon_imageview);
		// imageView.setBackgroundResource(drawable);

		group = (RadioGroup) findViewById(R.id.option_list);
		// group.setOnCheckedChangeListener(this);

		openappButton = (RadioButton) findViewById(R.id.operation_openapp);
		openappButton.setOnClickListener(this);

		catelognButton = (RadioButton) findViewById(R.id.operation_catelog);
		catelognButton.setOnClickListener(this);

		if (flag) {
			openappButton.setChecked(true);
			catelognButton.setChecked(false);
		} else {
			openappButton.setChecked(false);
			catelognButton.setChecked(true);
		}
		renameButton = (RadioButton) findViewById(R.id.operation_rename);
		renameButton.setOnClickListener(this);
		if (type == ApplicationCategoryType.Movie || type == ApplicationCategoryType.TV) {

		} else {
			if (type == ApplicationCategoryType.Browser) {
				catelognButton.setVisibility(View.INVISIBLE);
			}else {
				openappButton.setVisibility(View.INVISIBLE);
				catelognButton.setVisibility(View.INVISIBLE);
			}

		}

	}

	public void dialog_show(int x, int y) {

		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.TOP | Gravity.LEFT);
		lp.x = x;
		lp.y = y;
		dialogWindow.setAttributes(lp);

		this.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.operation_openapp:
			chooseAppsOptionClick.ChooseAppsOptionClick(type);
			this.dismiss();
			break;
		case R.id.operation_catelog:
			chooseAppsOptionClick.ChooseCatelogOptionClick(type);
			this.dismiss();
			break;
		case R.id.operation_rename:
			chooseAppsOptionClick.ChooseRenameOpionClick(type);
			this.dismiss();
			break;

		default:
			break;
		}
	}

	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId) {
	// // TODO Auto-generated method stub
	// switch (checkedId) {
	// case R.id.operation_openapp:
	// chooseAppsOptionClick.ChooseAppsOptionClick(type);
	// break;
	// case R.id.operation_catelog:
	// chooseAppsOptionClick.ChooseCatelogOptionClick(type);
	// break;
	// default:
	// break;
	// }
	// }
}
