package com.mirageTeam.widget;

import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;

import android.content.Context;

public class MusicCategory extends ApplicationCategoryMenu {

	public MusicCategory(Context context,ICategoryClick onCategoryClick) {
		super(context);
		// TODO Auto-generated constructor stub
		showApplicationCategoryType(ApplicationCategoryType.Browser,onCategoryClick);
	}

	 
	
	public void onReresh() {
		mApplicationCategoryController.getGategoryModesArray(mContext,
				CommonUtils.transferEnumToInt(ApplicationCategoryType.Browser));
	}

}
