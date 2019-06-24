package com.mirageTeam.widget;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.business.ApplicationCategoryBusiness;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.controller.ApplicationCategoryController;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.launcherui.R;

public class ShortCutGridVIew extends GridView {

	public ICategoryClick onCategoryClick;

	public void setOnICategoryClick(ICategoryClick click) {
		this.onCategoryClick = click;
	}

	private ApplicationCategoryController mApplicationCategoryController;

	private shorctCutAdapter mAdapter;
	ArrayList<CategoryModel> models;
	CategoryModel defaultModel;

	public ShortCutGridVIew(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		mApplicationCategoryController = new ApplicationCategoryController(
				mHandler);

		onRefresh();
		models = new ArrayList<CategoryModel>();
		defaultModel = new CategoryModel();
		defaultModel.setType(0);
		models.add(defaultModel);
		mAdapter = new shorctCutAdapter(models);

		setAdapter(mAdapter);

	}


	
	public void onRefresh() {
		mApplicationCategoryController
				.getGategoryModesArray(
						getContext(),
						CommonUtils
								.transferEnumToInt(ApplicationCategoryType.FAST_SHORTCUT));
	}

	public CategoryModel getModel(int position) {
		return mAdapter.getItem(position);
	}

	
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ApplicationCategoryBusiness.GET_CATEGORY_LIST_SUCCES:
				models.clear();
				models = (ArrayList<CategoryModel>) msg.obj;
				if (null != models && models.size() < 9) {
					models.add(defaultModel);
				} else if (models == null || models.size() == 0) {
					models = new ArrayList<CategoryModel>();
					models.add(defaultModel);
				}
				mAdapter.setList(models);
				break;
			case ApplicationCategoryBusiness.DELETE_CATEGORY_BY_SYSTEM_REMOVED_SUCCESS:
				onRefresh();
				break;
			default:
				break;
			}
		};
	};

	
	public int getType(int position){
	return	mAdapter.getItem(position).getType();
	}
	
	class shorctCutAdapter extends BaseAdapter {
		ArrayList<CategoryModel> models;

		public shorctCutAdapter(ArrayList<CategoryModel> models) {
			this.models = models;
		}

		public void setList(ArrayList<CategoryModel> models) {
			this.models = models;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return models.size();
		}

		@Override
		public CategoryModel getItem(int position) {
			// TODO Auto-generated method stub
			return models.get(position);
		}

		
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final CategoryModel model = getItem(position);
			
			shorcutViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder=new shorcutViewHolder();
				convertView=LayoutInflater.from(getContext()).inflate(
						R.layout.item_of_shortcut_app, null);
				viewHolder.tv=(TextView)convertView.findViewById(R.id.item_shortcut);
				viewHolder.im=(ImageView)convertView.findViewById(R.id.app_pic);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(shorcutViewHolder)convertView.getTag();
			}
			
		 
		 
			if (model.getType() == 0) {
			 
			  
				viewHolder.im.setImageResource(R.drawable.ic_category_add);
				
				viewHolder.tv.setText("");
//				tv.setImageResource(R.drawable.ic_category_add);
//			 
//				 button.setOnClickListener(new OnClickListener() {
//				
//				 @Override
//				 public void onClick(View v) {
//				 // TODO Auto-generated method stub
//				 if (onCategoryClick != null) {
//				 onCategoryClick
//				 .onPlusClic(ApplicationCategoryType.FAST_SHORTCUT);
//				 }
//				 }
//				 });
				 
			} else {
			

				viewHolder.tv.setText(model.getLabelRes());
				try {
					Drawable icon = getContext()
							.createPackageContext(model.getPackageName(), 0)
							.getResources().getDrawable(model.getIconRes());
					viewHolder.im.setImageDrawable(icon);
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				 view.setOnClickListener(new OnClickListener() {
//				
//				 @Override
//				 public void onClick(View v) {
//				 // TODO Auto-generated method stub
//				 if (onCategoryClick != null) {
//				 onCategoryClick.onItemClick(model.getPackageName(),
//				 model.getActivityName());
//				 }
//				 }
//				 });
//				
//				 tv.setOnLongClickListener(new onItemLongClick(model));

			
			}
			return convertView;

		}
		
		 class shorcutViewHolder {
			TextView tv;
			ImageView im;
		}

	}

	public class onItemLongClick implements OnLongClickListener {

		private CategoryModel model;

		private ConfirmDialog mConfirmDialog;

		public onItemLongClick(CategoryModel model) {
			this.model = model;
			mConfirmDialog = new ConfirmDialog(getContext());
			mConfirmDialog.setDialogTitle(R.string.edit_mode);
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			mConfirmDialog.setPositiveButton(R.string.delete_item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mApplicationCategoryController
							.deleteCategoryBySystemRemoved(getContext(),
									model.getPackageName());
					mConfirmDialog.dismiss();

				}
			}).setNegativeButton(R.string.replace, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mConfirmDialog.dismiss();
					if (onCategoryClick != null) {
						onCategoryClick.onLongClick(model,ApplicationCategoryType.FAST_SHORTCUT);
					}
				}
			}).show();

			return true;
		}

	}

}
