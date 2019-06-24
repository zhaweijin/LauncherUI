package com.mirageTeam.CategoryInterface;

import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.db.CategoryModel;

public interface ICategoryClick {

	public void onPlusClic(ApplicationCategoryType type);

	public void onItemClick(String packageName, String activityName);

	public void onLongClick(CategoryModel model,ApplicationCategoryType type);

}
