package com.mirageteam.launcher.market;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.mirageTeam.launcherui.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;



/*
 * @ImageData 传入的对象数据
 * 图标异步加载的线程，且固定当前图标在确定的位置
 */
public class ImagePageTask extends AsyncTask<ImageData, Void, Bitmap> {
	private ImageView gView;

	@Override
	protected Bitmap doInBackground(ImageData... views) {
		Bitmap bmp = null;

		ImageView view = views[0].getImageView();
		String picUrl = views[0].getUrlString();

		 

		bmp = Util.GetBitmapByUrl(picUrl);

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
			this.gView.setImageResource(R.drawable.ic_launcher);
		}
	}
	
	

}