package com.mirageTeam.widget;

import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;

import android.content.Context;

public class MovieCategory extends ApplicationCategoryMenu{

	
	public MovieCategory(Context context,ICategoryClick onCategoryClick) {
		super(context);
		// TODO Auto-generated constructor stub
		showApplicationCategoryType(ApplicationCategoryType.Movie, onCategoryClick);
	}
	
	public void onReresh() {
		mApplicationCategoryController.getGategoryModesArray(mContext,
				CommonUtils.transferEnumToInt(ApplicationCategoryType.Movie));
	}

}
