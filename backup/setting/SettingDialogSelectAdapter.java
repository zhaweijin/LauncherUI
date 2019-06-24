package com.mirageteam.launcher.setting;

import java.util.ArrayList;

import com.mirageTeam.launcherui.R;





import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;



public class SettingDialogSelectAdapter extends BaseAdapter {


	private Context context;
	private ArrayList<String> title;

	
	
	public SettingDialogSelectAdapter(Context context,ArrayList<String> title) {
		this.context = context;
		this.title = title;	
	}
	

	public int getCount() {
		return title.size();
	}

	public Object getItem(int position) {
		return title.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (view == null) {
				viewHolder = new ViewHolder();
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.setting_dialog_select_row, null);

				viewHolder.title = (TextView) view.findViewById(R.id.title);
				viewHolder.radioButton = (RadioButton)view.findViewById(R.id.radiobutton);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			
 

		return view;

	}

	public class ViewHolder {
		TextView title;
		RadioButton radioButton;
	}

	

}