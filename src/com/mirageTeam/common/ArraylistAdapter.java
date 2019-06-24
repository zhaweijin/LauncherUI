package com.mirageTeam.common;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 适配器通用类
 * @author terry
 *
 * @param <T>
 */
public abstract class ArraylistAdapter<T> extends BaseAdapter {

	protected ArrayList<T> mList = new ArrayList<T>();
	protected Context mContext; 

	public  ArraylistAdapter(Context context,ArrayList<T> list) {
		this.mContext = context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView,
			ViewGroup parent);

	public void setList(ArrayList<T> list) {
		this.mList.clear();
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	public ArrayList<T> getList() {
		return mList;
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

 

	public void addData(ArrayList<T> list) {
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(T data) {
		mList.add(data);
		notifyDataSetChanged();
	}

	public void addDataToFirst(ArrayList<T> list) {
		for (int i = 0; i < list.size(); i++) {
			mList.add(i, list.get(i));
		}
		notifyDataSetChanged();
	}

	public void clearData() {
		if (mList != null) {
			mList.clear();
		}
	}

}
