package com.mirageTeam.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.IconMapper;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.widget.CommonDialog;
import com.mirageTeam.widget.ConfirmDialog;

public class ApplicationCategoryAdapter extends BaseAdapter {

	private Context context;
	private ApplicationCategoryType mApplicationCategoryType;
	private ArrayList<CategoryModel> objects;

	public ICategoryClick onCategoryClick;

	public void setOnICategoryClick(ICategoryClick click) {
		this.onCategoryClick = click;
	}

	public ApplicationCategoryAdapter(Context mContext, ApplicationCategoryType type) {
		context = mContext;
		mApplicationCategoryType = type;
	}

	public void setList(ArrayList<CategoryModel> data) {
		if (objects != null && objects.size() > 0)
			objects.clear();
		objects = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public CategoryModel getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CategoryModel model = getItem(position);

		applicationViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new applicationViewHolder();
			convertView = inflater.inflate(R.layout.item_of_category, null, false);
			viewHolder.layoutSwitch = (ViewSwitcher) convertView.findViewById(R.id.layout_switch);
			viewHolder.categoryPic = (ImageView) convertView.findViewById(R.id.categoryPic);
			viewHolder.mcategoryText = (TextView) convertView.findViewById(R.id.category_text);
			viewHolder.categoryPlus = (Button) convertView.findViewById(R.id.category_plus);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (applicationViewHolder) convertView.getTag();
		}

		if (model.getType() == 0) {
			viewHolder.layoutSwitch.showNext();

			/*
			 * 点击添加按钮添加新分类应用
			 */
			viewHolder.categoryPlus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("categoryPic", "setOnClickListener   " + (onCategoryClick != null));

					if (onCategoryClick != null) {
						onCategoryClick.onPlusClic(mApplicationCategoryType);
					}
				}
			});
			convertView.setBackgroundResource(R.drawable.transparent);
		} else {
			convertView.setFocusable(true);
			convertView.setBackgroundResource(R.drawable.selector_of_wallpaper);
			try {
				String packageName = model.getPackageName();

				// viewHolder.categoryPic.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_of_wallpaper));
				if (IconMapper.getInstance().getIconsMap().containsKey(packageName)) {

					viewHolder.categoryPic.setImageResource(IconMapper.getInstance().getIconsMap().get(packageName));
				} else {
					viewHolder.categoryPic.setBackgroundResource(IconMapper.getInstance().getBackDrawable());
					viewHolder.categoryPic.setImageDrawable(context.createPackageContext(model.getPackageName(), 0).getResources()
							.getDrawable(model.getIconRes()));
				}
				viewHolder.mcategoryText.setText(model.getLabelRes());

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * 单击进入该应用
			 */
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (onCategoryClick != null) {
						onCategoryClick.onItemClick(model.getPackageName(), model.getActivityName());
					}
				}
			});
			/*
			 * 长按删除该分类应用
			 */
			convertView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					final ConfirmDialog dialog = new ConfirmDialog(context);
					dialog.setDialogTitle(context.getResources().getString(R.string.dialog_catalog_title))
							.setPositiveButton(android.R.string.ok, new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.i("categoryPic", "setOnLongClickListener");
									dialog.dismiss();
									if (onCategoryClick != null) {
										onCategoryClick.onLongClick(model, mApplicationCategoryType);
									}

								}
							}).setNegativeButton(android.R.string.cancel, new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();

					return true;
				}
			});

		}

		return convertView;

		// Button button = new Button(context);
		//
		// if (model.getType() != 0) {
		// button.setText(model.getLabelRes());
		// try {
		// button.setCompoundDrawablesWithIntrinsicBounds(null, context
		// .createPackageContext(model.getPackageName(), 0)
		// .getResources().getDrawable(model.getIconRes()), null,
		// null);
		// } catch (NotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NameNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {
		// button.setBackgroundResource(R.drawable.ic_category_add);
		//
		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (onCategoryClick != null) {
		// onCategoryClick.onPlusClic(mApplicationCategoryType);
		// }
		// }
		// });
		//
		// }
		//
		// return button;
	}

	static class applicationViewHolder {
		ViewSwitcher layoutSwitch;
		ImageView categoryPic;
		TextView mcategoryText;
		Button categoryPlus;
	}

}
