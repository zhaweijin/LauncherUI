package com.mirageteam.launcher.market;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

 

/**
 * @author zhaweijin
 * @function 下载数据库
 */
public class DownloadDatabase {

	
	private String TAG = "DownloadDatabase";
	
	public final static String KEY_NAME = "name";
	public final static String KEY_ROWID = "_id";
	public final static String KEY_DOWNLOND_NUM = "downloadnum";
	public final static String KEY_PACKAGE_NAME = "package";
	public final static String KEY_FILEPATH = "filepath";

	private SQLiteDatabase db;
	private final static String DATABASE_NAME = "downloads";
	private final static String TABLE_NAME = "download";

	private final static String CREATE_DATABASE = "create table if not exists "
			+ TABLE_NAME + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_PACKAGE_NAME
			+ " text not null, " + KEY_NAME
			+ " text, "  + KEY_DOWNLOND_NUM
			+ " integer, " + KEY_FILEPATH + " text);";

	Context mContext;

	public DownloadDatabase(Context c) {
		this.mContext = c;
		db = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,
				null);
		try {
			db.execSQL(CREATE_DATABASE);
			Util.print("download database", "Create mydownloadTable note ok");
		} catch (Exception e) {
		}
	}

	public long insertData(String name,String packagename,int num, String filepath) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_NAME, name);
		contentValues.put(KEY_PACKAGE_NAME, packagename);
		contentValues.put(KEY_DOWNLOND_NUM, num);
		contentValues.put(KEY_FILEPATH, filepath);

		db.insert(TABLE_NAME, null, contentValues);

		return 0;
	}




	public boolean deleteData(String name) {

		String delete = KEY_NAME + " ='" + name + "'";
		boolean result = db.delete(TABLE_NAME, delete, null) > 0;

		return result;
	}

	public boolean deleteByPackageData(String packagename) {

		String delete = KEY_PACKAGE_NAME + " ='" + packagename + "'";
		boolean result = db.delete(TABLE_NAME, delete, null) > 0;

		return result;
	}

	public Cursor queryAllData() {

		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from download", null);
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
			}
			
			e.printStackTrace();
		}
		return cursor;
	}

	public Cursor queryByPackage(String name) {

		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from download where package =?",
					new String[] { name });
		} catch (Exception e) {
			cursor.close();
			e.printStackTrace();
		}

		return cursor;
	}

	public Cursor queryID(String name) {

		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from download where name =?",
					new String[] { name });
		} catch (Exception e) {
			cursor.close();
			e.printStackTrace();
		}
		return cursor;
	}

	public void close() {
		db.close();
	}

	public Integer getDownloadNum(String software_name) {

		Cursor temCursor = null;
		int returnvalue = 0;
		int size;

		temCursor = queryID(software_name);
		size = temCursor.getCount();
		temCursor.moveToFirst();
		if (size != 0) {
			returnvalue = temCursor.getInt(temCursor
					.getColumnIndexOrThrow(DownloadDatabase.KEY_DOWNLOND_NUM));
		}

		temCursor.close();

		return returnvalue;
	}
	
	
	public Integer getDownloadNumByPackage(String packagename) {

		Cursor temCursor = null;
		int returnvalue = 0;
		int size;

		temCursor = queryByPackage(packagename);
		size = temCursor.getCount();
		temCursor.moveToFirst();
		if (size != 0) {
			returnvalue = temCursor.getInt(temCursor
					.getColumnIndexOrThrow(DownloadDatabase.KEY_DOWNLOND_NUM));
		}

		temCursor.close();

		return returnvalue;
	}

	public void updateDownloadNum(String packagename, int num) {

		Util.print(TAG, ">>>update download nun="+num);
		String sqlString = "update download set downloadnum = ? where package=?";
		db.execSQL(sqlString, new Object[] { num, packagename });

	}
	
	public void updateFilepath(String filepath,String packagename) {

		String sqlString = "update download set filepath = ? where package=?";
		db.execSQL(sqlString, new Object[] { filepath, packagename });

	}

	
	public void updateAll(String name, long length, int num) {

		String sqlString = "update download set downloadlength = ?, downloadnum = ? where name=?";
		db.execSQL(sqlString, new Object[] { length, num, name });

	}

	public void deleteTable() {
		try {
			String sqlString = "drop table " + TABLE_NAME;
			Util.print("sql", sqlString);
			db.execSQL(sqlString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
