package com.mirageTeam.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mirageTeam.Adapter.ApplicationCategoryAdapter;
import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.business.ApplicationCategoryBusiness;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.controller.ApplicationCategoryController;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.launcherui.R;

public class ApplicationCategoryMenu extends BaseBottomMenu {
	
	
	private ApplicationCategoryType mType;

	private ImageView catalogIcon;
	private HorizontalListView mHorizontalListView;

	private ApplicationCategoryAdapter mAdapter;

	 
	protected ApplicationCategoryController mApplicationCategoryController;

	 

	public ApplicationCategoryMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		catalogIcon = (ImageView) getContentView().findViewById(R.id.catalog_icon);
		mHorizontalListView = (HorizontalListView) getContentView().findViewById(R.id.category);
		mApplicationCategoryController = new ApplicationCategoryController(mHandler);

	}

	public void showApplicationCategoryType(ApplicationCategoryType type,ICategoryClick onCategoryClick) {
		mType = type;
		if (type == ApplicationCategoryType.Browser) {
			catalogIcon.setBackgroundResource(R.drawable.music_catalog);
		} else if (type == ApplicationCategoryType.Movie) {
			catalogIcon.setBackgroundResource(R.drawable.movie_catalog);
		} else if (type == ApplicationCategoryType.TV) {
			catalogIcon.setBackgroundResource(R.drawable.tv_catalog);
		}
		
		mApplicationCategoryController.getGategoryModesArray(mContext, CommonUtils.transferEnumToInt(mType));
		mAdapter = new ApplicationCategoryAdapter(mContext, mType);
		mAdapter.setOnICategoryClick(onCategoryClick);
//		show();
	}

	@Override
	View baseBottomMenuContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(mContext).inflate(R.layout.layout_of_category, null);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ApplicationCategoryBusiness.GET_CATEGORY_LIST_SUCCES:
			
				ArrayList<CategoryModel> models = (ArrayList<CategoryModel>) msg.obj;
				if (models == null)
					models = new ArrayList<CategoryModel>();
				CategoryModel modelPlus = new CategoryModel();
				modelPlus.setType(0);
				models.add(modelPlus);
				mAdapter.setList(models);
				mHorizontalListView.setAdapter(mAdapter);
				break;

			default:
				break;
			}
		};
	};

	 
}
