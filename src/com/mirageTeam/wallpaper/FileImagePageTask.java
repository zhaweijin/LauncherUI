package com.mirageTeam.wallpaper;

import java.io.File;

import com.mirageTeam.launcherui.R;
import com.mirageteam.launcher.market.ImageData;
import com.mirageteam.launcher.market.Util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;



/*
 * @ImageData 传入的对象数据
 * 图标异步加载的线程，且固定当前图标在确定的位置
 */
@SuppressLint("NewApi")
public class FileImagePageTask extends AsyncTask<ImageData, Void, Bitmap> {
	private ImageView gView;

	@Override
	protected Bitmap doInBackground(ImageData... views) {
		Bitmap bmp = null;

		ImageView view = views[0].getImageView();
		String picUrl = views[0].getUrlString();

		File f = new File(picUrl);

		bmp = Util.fitSizePic(f);

		this.gView = view;
		return bmp;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final Bitmap bm) {

		if (bm != null) {
			this.gView.setImageBitmap(bm);
		} else {
			this.gView.setImageResource(R.drawable.image);
		}
	}
	
	

}