package com.mirageTeam.db;

import java.util.ArrayList;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.launcherui.R;

public class CategoryDBHelp {

	private CategoryDB dbHelper;

	private Context mContext;

	public static final int CATELOG_FLAG = 0;
	public static final int APP_FLAG = 1;

	public CategoryDBHelp(Context mContext) {
		dbHelper = CategoryDB.getInstances(mContext);
		dbHelper.getWritableDatabase();
		this.mContext = mContext;
	}

	/**
	 * 
	 * 描述：判断数据库中该类别是否存在该应用
	 * 
	 * @return
	 */
	public boolean isExists(int type, String activityName, int flag) {
		SQLiteDatabase db = dbHelper.getDatabase();
		String sql = "select * from " + CategoryDB.TABLE_NAME + " where " + CategoryDB.CATEGORY_TYPE + "='" + type + "' and " + CategoryDB.ACTIVITY_NAME + "='"
				+ activityName + "' and " + CategoryDB.OPERATION_TYPE + "='" + flag + "';";
		System.out.println("=============sql=========" + sql);
		Cursor cursor = null;
		int c = 0;
		try {
			cursor = db.rawQuery(sql, null);
			c = cursor.getCount();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		if (c > 0)
			return true;
		else
			return false;
	}

	/**
	 * 插入数据到某个类别中
	 * 
	 * @param info
	 */
	public synchronized Object insert(CategoryModel info) {
		SQLiteDatabase db = dbHelper.getDatabase();
		boolean isExists = isExists(info.getType(), info.getActivityName(), info.getOpType());
		if (info.getType() == CommonUtils.transferEnumToInt(ApplicationCategoryType.FAST_SHORTCUT)) {

			if (getCategoryByType(info.getType()).size() < 9) {
				if (!isExists) {

					ContentValues values = new ContentValues();
					values.put(CategoryDB.PACKAGE_NAME, info.getPackageName());
					values.put(CategoryDB.ACTIVITY_NAME, info.getActivityName());
					values.put(CategoryDB.ICON_RESOURCE, info.getIconRes());
					values.put(CategoryDB.LABEL_RESOURCE, info.getLabelRes());
					values.put(CategoryDB.CATEGORY_TYPE, info.getType());

					return db.insert(CategoryDB.TABLE_NAME, null, values) > 0 ? "" : mContext.getString(R.string.is_exist);
				} else {
					return mContext.getString(R.string.is_exist);
				}
			} else {
				return mContext.getString(R.string.max_over_flow);
			}
		} else {
			if (!isExists) {

				ContentValues values = new ContentValues();
				values.put(CategoryDB.PACKAGE_NAME, info.getPackageName());
				values.put(CategoryDB.ACTIVITY_NAME, info.getActivityName());
				values.put(CategoryDB.ICON_RESOURCE, info.getIconRes());
				values.put(CategoryDB.LABEL_RESOURCE, info.getLabelRes());
				values.put(CategoryDB.CATEGORY_TYPE, info.getType());
				values.put(CategoryDB.OPERATION_TYPE, info.getOpType());

				return db.insert(CategoryDB.TABLE_NAME, null, values) > 0 ? true : false;
			} else {
				if (info.getOpType() == CategoryDBHelp.APP_FLAG) {
					update(info, info.getActivityName(), String.valueOf(info.getType()), info.getOpType());
					return null;
				}
			}
		}
		return false;
	}

	public int delete(int type, int id) {
		SQLiteDatabase db = dbHelper.getDatabase();
		return db.delete(CategoryDB.TABLE_NAME, "" + CategoryDB.CATEGORY_TYPE + "=" + type + " and _id = " + id + "", null);
	}

	public int delete(int type, String name) {
		SQLiteDatabase db = dbHelper.getDatabase();
		return db.delete(CategoryDB.TABLE_NAME, "" + CategoryDB.CATEGORY_TYPE + "=" + type + " and " + CategoryDB.ACTIVITY_NAME + " = '" + name + "'", null);
	}

	public boolean delete(String packageName) {
		SQLiteDatabase db = dbHelper.getDatabase();
		int effect = db.delete(CategoryDB.TABLE_NAME, CategoryDB.PACKAGE_NAME + " = ?", new String[] { packageName });
		// String
		// sql="delete from "+CategoryDB.TABLE_NAME+" where "+CategoryDB.PACKAGE_NAME+"='"+packageName+"';";
		// db.execSQL(sql);
		// System.out.println("===========delete=========="+effect);
		return effect > 0 ? true : false;
	}

	public boolean update(CategoryModel model, String activityName, String type) {

		return update(model, activityName, type, 0);

	}

	public boolean update(CategoryModel model, String activityName, String type, int optype) {
		boolean isExists = isExists(Integer.parseInt(type), model.getActivityName(), optype);
		if (!isExists) {

			SQLiteDatabase db = dbHelper.getDatabase();

			ContentValues values = new ContentValues();
			values.put(CategoryDB.PACKAGE_NAME, model.getPackageName());
			values.put(CategoryDB.ACTIVITY_NAME, model.getActivityName());
			values.put(CategoryDB.ICON_RESOURCE, model.getIconRes());
			values.put(CategoryDB.LABEL_RESOURCE, model.getLabelRes());
			values.put(CategoryDB.CATEGORY_TYPE, model.getType());
			values.put(CategoryDB.OPERATION_TYPE, model.getOpType());

			return db.update(CategoryDB.TABLE_NAME, values, CategoryDB.ACTIVITY_NAME + "=? and " + CategoryDB.CATEGORY_TYPE + "=? and "
					+ CategoryDB.OPERATION_TYPE + "=?", new String[] { activityName, type, String.valueOf(optype) }) > 0;
		}
		return false;

	}

	/**
	 * 
	 * 描述：根据类型从数据库中获取相应应用
	 * 
	 * 
	 * @return
	 */
	public ArrayList<CategoryModel> getCategoryByType(int type) {
		SQLiteDatabase db = dbHelper.getDatabase();
		ArrayList<CategoryModel> list = null;
		Cursor cursor = null;
		try {
			String sql = "select * from " + CategoryDB.TABLE_NAME + " where " + CategoryDB.CATEGORY_TYPE + "='" + type + "' and " + CategoryDB.OPERATION_TYPE
					+ "=" + CategoryDBHelp.CATELOG_FLAG + " ;";
			System.out.println("===========sql===============" + sql);
			cursor = db.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				list = new ArrayList<CategoryModel>();
				while (cursor.moveToNext()) {
					// list.add(cursor.getString(cursor.getColumnIndex("path")));
					CategoryModel model = new CategoryModel();
					model.setActivityName(cursor.getString(cursor.getColumnIndex(CategoryDB.ACTIVITY_NAME)));
					model.setPackageName(cursor.getString(cursor.getColumnIndex(CategoryDB.PACKAGE_NAME)));
					model.setIconRes(cursor.getInt(cursor.getColumnIndex(CategoryDB.ICON_RESOURCE)));
					model.setLabel(cursor.getString(cursor.getColumnIndex(CategoryDB.LABEL_RESOURCE)));
					model.setType(cursor.getInt(cursor.getColumnIndex(CategoryDB.CATEGORY_TYPE)));
					list.add(model);
				}
			} else {
				return new ArrayList<CategoryModel>();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	public CategoryModel getDefaultAppByType(int type) {
		SQLiteDatabase db = dbHelper.getDatabase();
		CategoryModel model = new CategoryModel();
		Cursor cursor = null;
		try {
			String sql = "select * from " + CategoryDB.TABLE_NAME + " where " + CategoryDB.CATEGORY_TYPE + "='" + type + "' and " + CategoryDB.OPERATION_TYPE
					+ "='" + CategoryDBHelp.APP_FLAG + "' ;";
			System.out.println("===========sql===============" + sql);
			cursor = db.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					model.setActivityName(cursor.getString(cursor.getColumnIndex(CategoryDB.ACTIVITY_NAME)));
					model.setPackageName(cursor.getString(cursor.getColumnIndex(CategoryDB.PACKAGE_NAME)));
					model.setIconRes(cursor.getInt(cursor.getColumnIndex(CategoryDB.ICON_RESOURCE)));
					model.setLabel(cursor.getString(cursor.getColumnIndex(CategoryDB.LABEL_RESOURCE)));
					model.setType(cursor.getInt(cursor.getColumnIndex(CategoryDB.CATEGORY_TYPE)));
					model.setOpType(CategoryDBHelp.APP_FLAG);
				}
			} else {
				return null;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return model;
	}
}
