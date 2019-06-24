package com.mirageTeam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoryDB extends SQLiteOpenHelper {

	private static CategoryDB instances = null;

	private SQLiteDatabase sqlLiteDB = null;

	private static String DB_NAME = "launcherui.db";
	public static final int DB_VERSION = 8;
	public static final String TABLE_NAME = "category";
	/**
	 * 包名
	 */
	public static final String PACKAGE_NAME = "packagename";

	public static final String ACTIVITY_NAME = "activityname";

	public static final String LABEL_RESOURCE = "labelResource";

	public static final String ICON_RESOURCE = "iconResource";

	public static final String CATEGORY_TYPE = "cateogry_id";

	// operation type, default 0
	// 0, catelog operation
	// 1, app operation
	public static final String OPERATION_TYPE = "operation_type";

	public CategoryDB(Context context, String name) {
		super(context, DB_NAME, null, DB_VERSION);
		System.out.println("=================DB_NAME========" + DB_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("===============sql oncreate==========");
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id integer primary key autoincrement, " + PACKAGE_NAME + " varchar(60)," + ACTIVITY_NAME
				+ " varchar(60)," + LABEL_RESOURCE + " varchar(40)," + ICON_RESOURCE + " integer," + CATEGORY_TYPE + " integer," + OPERATION_TYPE
				+ " integer default 0);";
		System.out.println("create table sql is :" + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("====================onUpgrade====================");
		String sql = "drop table if EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);

	}

	/**
	 * 
	 * 描述：获得数据库实例，创建数据库
	 * 
	 * @param context
	 * @param dbName
	 *            数据库的绝对路径
	 * @return
	 */
	public static CategoryDB getInstances(Context context) {
		if (instances == null) {
			synchronized (CategoryDB.class) {
				if (instances == null) {
					instances = new CategoryDB(context, DB_NAME);
				}
			}
		}
		return instances;
	}

	/**
	 * 创建表
	 * 
	 * @param sql
	 */
	public synchronized void createTable(String sql) {
		SQLiteDatabase db = instances.getWritableDatabase();
		db.execSQL(sql);
	}

	public void closeDB() {
		if (sqlLiteDB != null && sqlLiteDB.isOpen()) {
			sqlLiteDB.close();
		}
		if (instances != null) {
			instances.close();
			instances = null;
		}
	}

	public SQLiteDatabase getDatabase() {
		if (sqlLiteDB != null) {
			return sqlLiteDB;

		} else {
			sqlLiteDB = getWritableDatabase();
			return sqlLiteDB;
		}
	}

}
