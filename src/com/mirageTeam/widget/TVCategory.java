package com.mirageTeam.widget;

import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;

import android.content.Context;

public class TVCategory extends ApplicationCategoryMenu{

	
	public TVCategory(Context context,ICategoryClick onCategoryClick) {
		super(context);
		// TODO Auto-generated constructor stub
		showApplicationCategoryType(ApplicationCategoryType.TV,onCategoryClick);
	}
	public void onReresh() {
		mApplicationCategoryController.getGategoryModesArray(mContext,
				CommonUtils.transferEnumToInt(ApplicationCategoryType.TV));
	}

}
