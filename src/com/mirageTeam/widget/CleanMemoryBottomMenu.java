package com.mirageTeam.widget;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.io.DataOutputStream; 

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.util.Log;

import com.mirageTeam.business.ApplicationCategoryBusiness;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.common.IconMapper;
import com.mirageTeam.controller.ApplicationCategoryController;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.store.model.TaskInfo;

public class CleanMemoryBottomMenu extends BaseBottomMenu implements Observer {

	public static final int REMOVE_POSITION_CHANGE = 1111;

	private HorizontalListView listView;

	private ArrayList<TaskInfo> runningApp;

	private ApplicationCategoryController mController;
	ActivityManager activityManager;

	View cleanButton;

	public CleanMemoryBottomMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		listView = (HorizontalListView) getContentView().findViewById(R.id.memoryApp);

		activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

		mController = new ApplicationCategoryController(mHandler);

		cleanButton = getContentView().findViewById(R.id.cleanMemory);
		cleanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(mContext, "Clean app cache here", Toast.LENGTH_SHORT).show();
				cleanThread thread = new cleanThread(runningApp);
				thread.addObserver(CleanMemoryBottomMenu.this);
				Toast.makeText(mContext, "功能正在开发中", Toast.LENGTH_SHORT).show();
			}
		});

	}

	class cleanThread extends Observable {
	    public cleanThread(final ArrayList<TaskInfo> infos) {
		new Thread(new Runnable() {

			@Override
			public void run() {
			    // TODO Auto-generated method stub
			    // for (int i = 0; i < infos.size(); i++) {
			    // ActivityManager activityManager = (ActivityManager)
			    // mContext
			    // .getSystemService(Context.ACTIVITY_SERVICE);
			    // activityManager.restartPackage(infos.get(i).packageName);
			    // setChanged();
			    // notifyObservers(i);
			    // }
			    //if (infos != null) {
			    //	for (int i = 0; i < infos.size(); i++) {
			//	    /*
			//	     * 须在在源码中编译 在eclipse中注释掉
			//	     */
			//	    activityManager.removeTask(infos.get(i).getRecentTask().persistentId, ActivityManager.REMOVE_TASK_KILL_PROCESS);
			//	    setChanged();
			//	    notifyObservers(i);
			//	}
			//    }

			//    mController.getApplicationInfos(mContext);
			}
		}).start();
	    }
	}

	@Override
	View baseBottomMenuContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(mContext).inflate(R.layout.layout_of_clean_memory, null);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		mController.getApplicationInfos(mContext);
	}

	class memoryAdapter extends BaseAdapter {

		private ArrayList<TaskInfo> runInfos;

		public memoryAdapter(ArrayList<TaskInfo> runningInfo) {
			runInfos = runningInfo;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return runInfos.size();
		}

		@Override
		public TaskInfo getItem(int position) {
			// TODO Auto-generated method stub
			return runInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TaskInfo info = getItem(position);

			applicationViewHolder viewHolder = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				viewHolder = new applicationViewHolder();
				convertView = inflater.inflate(R.layout.item_of_cacheapp, null, false);
				viewHolder.app_icon = (ImageButton) convertView.findViewById(R.id.cache_app_icon);
				viewHolder.app_icon.setClickable(true);
				viewHolder.app_name = (TextView) convertView.findViewById(R.id.cache_app_name);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (applicationViewHolder) convertView.getTag();
			}

			convertView.setFocusable(true);
			//convertView.setBackgroundResource(R.drawable.selector_of_wallpaper);
			final ResolveInfo resolve = info.getResoveInfo();

			viewHolder.app_icon.setBackgroundResource(IconMapper.getInstance().getBackDrawable());
			viewHolder.app_icon.setImageDrawable(resolve.loadIcon(mContext.getPackageManager()));
			viewHolder.app_name.setText(resolve.loadLabel(mContext.getPackageManager()));

			/*
			im.setFocusable(true);
			im.setBackgroundResource(R.drawable.selector_of_wallpaper);

			// im.setImageBitmap(info.thumbnail);
			im.setCompoundDrawablesWithIntrinsicBounds(null, resolve.loadIcon(mContext.getPackageManager()), null, null);
			im.setText(resolve.loadLabel(mContext.getPackageManager()));
			*/
			// applicationViewHolder viewHolder = null;
			// if (convertView == null) {
			// LayoutInflater inflater = (LayoutInflater) mContext
			// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// viewHolder = new applicationViewHolder();
			// convertView = inflater.inflate(R.layout.item_of_category, null,
			// false);
			// viewHolder.layoutSwitch = (ViewSwitcher) convertView
			// .findViewById(R.id.layout_switch);
			// viewHolder.categoryPic = (ImageView) convertView
			// .findViewById(R.id.categoryPic);
			// viewHolder.mcategoryText = (TextView) convertView
			// .findViewById(R.id.category_text);
			// viewHolder.categoryPlus = (Button) convertView
			// .findViewById(R.id.category_plus);
			// convertView.setTag(viewHolder);
			//
			// } else {
			// viewHolder = (applicationViewHolder) convertView.getTag();
			// }
			//
			// System.out.println("==================="+info.toString());
			// viewHolder.categoryPic.setImageBitmap(info.thumbnail);
			// viewHolder.mcategoryText.setText(info.);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CommonUtils.openApplication(mContext, resolve.activityInfo.packageName, resolve.activityInfo.name);
				}
			});

			return convertView;
		}
	}

	static class applicationViewHolder {
		ImageButton app_icon;
		TextView app_name;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ApplicationCategoryBusiness.GET_APPLICATION_ARRAYLIST:
				runningApp = (ArrayList<TaskInfo>) msg.obj;

				listView.setAdapter(new memoryAdapter(runningApp));

				break;
			case REMOVE_POSITION_CHANGE:
				listView.removeItem(msg.arg1);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		Message msg = mHandler.obtainMessage();
		msg.what = REMOVE_POSITION_CHANGE;
		msg.arg1 = Integer.parseInt(data.toString());
		msg.sendToTarget();
	}

}
