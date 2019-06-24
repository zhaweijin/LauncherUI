package com.mirageteam.launcher.market;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.mirageTeam.launcherui.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class GalleryAdapter extends BaseAdapter {
	private Context mContext;

 
//    private int[] pic = {R.drawable.a,R.drawable.b,R.drawable.c};
    
	private String[] pic;
 
	public GalleryAdapter(Context context,String[] pic) {
		mContext = context;
        this.pic = pic;
	}

	public int getCount() {
		return pic.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		// convertView=layoutInflater.inflate(R.layout.software_gallery_item,
		// null);

		// ImageView imageView =
		// (ImageView)convertView.findViewById(R.id.software_gallery_image);
		//
		// //ImageView i = new ImageView(mContext);
		//
		// //i.setImageResource(imageResource[position]);
		// imageView.setImageBitmap(BitmapFactory.decodeFile(filename[position]));

		// i.setScaleType(ImageView.ScaleType.FIT_XY);
		// i.setLayoutParams(new Gallery.LayoutParams(100, 140));
		//
		// // The preferred Gallery item background
		// // i.setBackgroundResource(mGalleryItemBackground);
		//

		try {
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			    convertView = layoutInflater.inflate(R.layout.software_gallery_item, null);
				viewHolder.image = (ImageView) convertView.findViewById(R.id.software_gallery_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			
			try {
				viewHolder.image.setImageResource(R.drawable.ic_launcher);
				ImagePageTask pt = new ImagePageTask();
				ImageData data = new ImageData();

				data.setUrlString(pic[position].replaceAll(" ", "%20"));
				data.setImageView(viewHolder.image);
				pt.execute(data);

//				Util.print("tag2", position + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
//			viewHolder.image.setImageResource(pic[position]);
//			Bitmap current = dateCache.get(position);
//			if (current != null) {// 如果缓存中已解码该图片，则直接返回缓存中的图片
//				viewHolder.image.setImageBitmap(current);
//			} else {
//				current = getPhotoItem(filename[position], 1); // 缩放二分之一
//				viewHolder.image.setImageBitmap(current);
//				dateCache.put(position, current);
//			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView image;
	}
	
	public Bitmap getPhotoItem(String filepath,int size) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize=size;
		File file = new File(filepath);
		try {
			FileInputStream fis = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			bitmap=Bitmap.createScaledBitmap(bitmap, 320, 480, false);//预先缩放，避免实时缩放，可以提高更新率
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		

		} 
//	
//	
//	public void freeBitmap() {
//		// 释放之外的bitmap资源
//		Bitmap delBitmap;
//		for (int del = 0; del < dateCache.size(); del++) {
//			delBitmap = dateCache.get(del);
//			if (delBitmap != null) {
//				dateCache.remove(del);
//				delBitmap.recycle();
//			}
//		}
//	}
}
