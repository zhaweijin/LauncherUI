package com.mirageTeam.wallpaper;


import java.io.File;
import java.util.List;

import com.mirageTeam.launcherui.R;
import com.mirageteam.launcher.market.ImageData;
import com.mirageteam.launcher.market.Util;
  

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class WallpaperFileListAdapter extends BaseAdapter{
  
  private LayoutInflater mInflater;
  private Bitmap mIcon_folder;
  private Bitmap mIcon_file;
  private Bitmap mIcon_image;
  private Bitmap mIcon_audio;
  private Bitmap mIcon_video;
  private Bitmap mIcon_apk;
  private List<String> items;
  private List<String> paths;
  
 
  public WallpaperFileListAdapter(Context context,List<String> it,List<String> pa){
   
    mInflater = LayoutInflater.from(context);
    items = it;
    paths = pa;
    mIcon_folder = BitmapFactory.decodeResource(context.getResources(),R.drawable.folder);       
    mIcon_file = BitmapFactory.decodeResource(context.getResources(),R.drawable.file);           
    mIcon_image = BitmapFactory.decodeResource(context.getResources(),R.drawable.image);         
    mIcon_audio = BitmapFactory.decodeResource(context.getResources(),R.drawable.audio);         
    mIcon_video = BitmapFactory.decodeResource(context.getResources(),R.drawable.video);         
    mIcon_apk = BitmapFactory.decodeResource(context.getResources(),R.drawable.apk);             
  }   
  

  @Override
  public int getCount(){
    return items.size();
  }

  @Override
  public Object getItem(int position){
    return items.get(position);
  }
  
  @Override
  public long getItemId(int position){
    return position;
  }
  
  @Override
  public View getView(int position,View convertView,ViewGroup par){
    Bitmap bitMap = null;
    ViewHolder holder = null;
      if(convertView == null){
        convertView = mInflater.inflate(R.layout.wallpaper_list_items, null);
        holder = new ViewHolder();
        holder.f_title = ((TextView) convertView.findViewById(R.id.f_title));
        holder.f_icon = ((ImageView) convertView.findViewById(R.id.f_icon)) ;
        convertView.setTag(holder);
      }else{
        holder = (ViewHolder) convertView.getTag();
      }
      File f = new File(paths.get(position).toString());

      holder.f_title.setText(f.getName());
      String f_type = Util.getMIMEType(f,false);
      if(f.isDirectory()){
        holder.f_icon.setImageBitmap(mIcon_folder);
      }else{
        if("image".equals(f_type)){
               
          try {
            holder.f_icon.setImageResource(R.drawable.image);
            FileImagePageTask pt = new FileImagePageTask();
            ImageData data = new ImageData();

            data.setUrlString(paths.get(position).toString());
            data.setImageView(holder.f_icon);
            pt.execute(data);
 
          } catch (Exception e) {
            e.printStackTrace();
          }
          
//          
//               bitMap = MyUtil.fitSizePic(f);
//               if(bitMap!=null){
//                 holder.f_icon.setImageBitmap(bitMap);
//               }else{
//                  holder.f_icon.setImageBitmap(mIcon_image);
//               }
//             
//              bitMap = null;
        }else if("audio".equals(f_type)){
              holder.f_icon.setImageBitmap(mIcon_audio);
        }else if("video".equals(f_type)){
              holder.f_icon.setImageBitmap(mIcon_video);
        }else if("apk".equals(f_type)){
              holder.f_icon.setImageBitmap(mIcon_apk);
        }else{
              holder.f_icon.setImageBitmap(mIcon_file);
        }
      }
    return convertView;
  }
  

  private class ViewHolder{
    TextView f_title;
    ImageView f_icon;
  }
}