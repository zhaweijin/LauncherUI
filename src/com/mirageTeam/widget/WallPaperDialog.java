package com.mirageTeam.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.mirageTeam.launcherui.R;
import com.mirageTeam.wallpaper.WallpaperFile;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WallPaperDialog extends PopupWindow {

	private View mMenuView;

	private Gallery mListView;

	private Context mContext;

	DisplayImageOptions options;

	ImageLoader loader;
	
	private Button more_custom;

	public interface onWallPaperItemClick {
		void wallPaperItemClick(int position);
	}

	public onWallPaperItemClick onItemClick;

	public void setOnWallPaperItemClick(onWallPaperItemClick onItem) {
		this.onItemClick = onItem;
	}

	public static final int[] RESOURCES = { R.drawable.sl_bjs_normal,
			R.drawable.sl_gy_normal, R.drawable.sl_ls_normal,
			R.drawable.sl_moren_normal, R.drawable.sl_mt_normal,
			R.drawable.sl_qb_normal, R.drawable.sl_xt_normal,
			R.drawable.sl_yd_normal, R.drawable.sl_ys_normal,
			R.drawable.sl_zs_normal };

	public static final int[] BACKGROUND_RESOURCES = { R.drawable.bjs, R.drawable.gy,
			R.drawable.ls,R.drawable.bj_moren, R.drawable.mt, R.drawable.qb, R.drawable.xt, R.drawable.yd,
		R.drawable.ys, R.drawable.zs };

	public WallPaperDialog(Context context) {
		super(context);

		loader = ImageLoader.getInstance();
		mContext = context;

		mMenuView = LayoutInflater.from(context).inflate(
				R.layout.layout_of_wallpaper, null);

		setContentView(mMenuView);

		setWidth(LayoutParams.FILL_PARENT);

		setHeight(LayoutParams.WRAP_CONTENT);

		setFocusable(true);

		// setAnimationStyle(R.style.AnimBottom);

		// ColorDrawable dw = new ColorDrawable(0xb0000000);

		// mMenuView.setBackgroundColor(0xb0000000);

		setBackgroundDrawable(new BitmapDrawable());

		mListView = (Gallery) getContentView().findViewById(R.id.wallpaper);
		
		more_custom = (Button)getContentView().findViewById(R.id.more_custom);
		more_custom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,WallpaperFile.class);
				mContext.startActivity(intent);
				dismiss();
			}
		});
		

		mListView.setAdapter(new wallpaperAdapter(RESOURCES));

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// if(onItemClick!=null){
				// onItemClick.wallPaperItemClick(arg2, RESOURCE_ASSERT[arg2]);

				// System.out.println("==============================="+arg2);
				if (onItemClick != null) {
					onItemClick.wallPaperItemClick(arg2);
				}

				// ((Activity) mContext).getWindow().getDecorView()
				// .setBackgroundResource(RESOURCE_ASSERT[arg2]);
				// }
			}
		});

	}
	
	
	

	class wallpaperAdapter extends BaseAdapter {

		private int[] wallpapers;

		public wallpaperAdapter(int[] wallpapers) {
			this.wallpapers = wallpapers;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return wallpapers.length;
		}

		@Override
		public Integer getItem(int position) {
			// TODO Auto-generated method stub
			return wallpapers[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			int res = getItem(position);

			ImageView mImageView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_of_wallpaper, null);
				mImageView = (ImageView) convertView;
			} else {
				convertView.setTag(mImageView);
			}
			Gallery.LayoutParams p = new Gallery.LayoutParams(
					Gallery.LayoutParams.WRAP_CONTENT,
					Gallery.LayoutParams.WRAP_CONTENT);
			mImageView.setLayoutParams(p);

			mImageView.setImageResource(res);

			return convertView;
		}

	}

	public void show() {
		showAtLocation(((Activity) mContext).getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
	}

	public void show(View view) {
		showAtLocation(view, Gravity.CENTER, 0, 0);
	}
}
