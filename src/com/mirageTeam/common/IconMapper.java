package com.mirageTeam.common;

import java.util.HashMap;
import android.util.Log;
import com.mirageTeam.launcherui.R;

public class IconMapper {

	HashMap<String, Integer> iconHashMap;

	private int[] background_pic = new int[] { R.drawable.background_blue, R.drawable.background_grayblue, R.drawable.background_green,
			R.drawable.background_yellow };

	// 新浪微博
	private static String package_name_weibotv = "com.sina.weibotv";
	private static int mapper_icon_weibo = R.drawable.com_sina_weibo;

	private static String package_name_weibo = "com.sina.weibo";

	// 腾讯QQ
	private static String package_name_hdqq = "com.tencent.hd.qq";
	private static String package_name_qq = "com.tencent.qq";
	private static String package_name_tvqq = "com.tencent.QQVideo";
	private static int mapper_icon_qq = R.drawable.com_tencent_qq;

	// PPLive
	private static String package_name_pplive = "com.pplive.androidpad";
	private static int mapper_icon_pplive = R.drawable.com_pplive_androidpad;

	// 搜狐视频
	private static String package_name_sohuvideo = "com.sohu.sohuvideo";
	private static int mapper_icon_sohuvideo = R.drawable.com_sohu_sohuvideo;

	// 奇艺
	private static String package_name_qiyi = "com.qiyi.video";
	private static int mapper_icon_qiyi = R.drawable.com_qiyi_video;

	// 腾讯视频
	private static String package_name_qqlive = "com.tencent.qqlive";
	private static int mapper_icon_qqlive = R.drawable.com_tencent_qqlive;
	// 兔子视频

	private static String package_name_tuzi = "com.luxtone.tuzi";
	private static int mapper_icon_tuzi = R.drawable.com_luxtone_tuzi;

	// 优酷
	private static String package_name_youku = "com.youku.tv";
	private static int mapper_icon_youku = R.drawable.com_youku_tv;

	// cloudtv
	private static String package_name_cloudtv = "org.xbmc.xbmc";
	private static int mapper_icon_cloudtv = R.drawable.org_xbmc_xbmc;

	// 网易公开课
	private static String package_name_vopen = "com.netease.vopen.tablet";
	private static int mapper_icon_vopen = R.drawable.com_netease_vopen_tablet;

	// 音乐台
	private static String package_name_yinyuetai = "com.airplayme.android.tvbox";
	private static int mapper_icon_yinyuetai = R.drawable.com_airplayme_android_tvbox;

	// 快手
	private static String package_name_kuaishou = "com.kandian.vodapp4tv";
	private static int mapper_icon_kuaishou = R.drawable.com_kandian_vodapp4tv;

	// 爱看
	private static String package_name_ikantv = "com_ikantv_activity";
	private static int mapper_icon_ikantv = R.drawable.com_ikantv_activity;

	// 泰捷视频
	private static String package_name_togic = "com.togic.livevideo";
	private static int mapper_icon_togic = R.drawable.com_togic_livevideo;

	// 乐视
	private static String package_name_letv = "com.letv.android.client";
	private static int mapper_icon_letv = R.drawable.com_letv_android_client;

	// 龙龙直播
	private static String package_name_longlong = "xlcao.sohutv4";
	private static int mapper_icon_longlong = R.drawable.xlcao_sohutv4;

	// PPS
	private static String package_name_ppspad = "tv.pps.pad";
	private static String package_name_ppstpad = "tv.pps.tpad";
	private static int mapper_icon_pps = R.drawable.tv_pps_pad;

	// 百事通
	private static String package_name_besttvhd = "com.bestv.apad";
	private static String package_name_besttv = "com.bestv.IPTVClient.UI";
	private static int mapper_icon_besttv = R.drawable.com_bestv_apad;

	// 达龙
	private static String package_name_daroonplayer = "com.daroonplayer.player";
	private static int mapper_icon_daroonplayer = R.drawable.com_daroonplayer_player;

	// 看客影视
	private static String package_name_kanke = "com.kanke.video";
	private static int mapper_icon_kanke = R.drawable.com_kanke_video;

	private static class SingleTonHolder {
		private static IconMapper INSTANCE = new IconMapper();
	}

	public static IconMapper getInstance() {
		return SingleTonHolder.INSTANCE;
	}

	public IconMapper() {
		iconHashMap = new HashMap<String, Integer>();
		initIconMap();
	}

	public void initIconMap() {
		iconHashMap.put(package_name_cloudtv, mapper_icon_cloudtv);
		iconHashMap.put(package_name_pplive, mapper_icon_pplive);
		iconHashMap.put(package_name_qiyi, mapper_icon_qiyi);
		iconHashMap.put(package_name_qq, mapper_icon_qq);
		iconHashMap.put(package_name_tvqq, mapper_icon_qq);
		iconHashMap.put(package_name_hdqq, mapper_icon_qq);
		iconHashMap.put(package_name_qqlive, mapper_icon_qqlive);
		iconHashMap.put(package_name_sohuvideo, mapper_icon_sohuvideo);
		iconHashMap.put(package_name_tuzi, mapper_icon_tuzi);
		iconHashMap.put(package_name_vopen, mapper_icon_vopen);
		iconHashMap.put(package_name_weibo, mapper_icon_weibo);
		iconHashMap.put(package_name_weibotv, mapper_icon_weibo);
		iconHashMap.put(package_name_youku, mapper_icon_youku);
		iconHashMap.put(package_name_yinyuetai, mapper_icon_yinyuetai);
		iconHashMap.put(package_name_kuaishou, mapper_icon_kuaishou);
		iconHashMap.put(package_name_ikantv, mapper_icon_ikantv);
		iconHashMap.put(package_name_togic, mapper_icon_togic);
		iconHashMap.put(package_name_letv, mapper_icon_letv);
		iconHashMap.put(package_name_longlong, mapper_icon_longlong);
		iconHashMap.put(package_name_ppspad, mapper_icon_pps);
		iconHashMap.put(package_name_ppstpad, mapper_icon_pps);
		iconHashMap.put(package_name_besttvhd, mapper_icon_besttv);
		iconHashMap.put(package_name_besttv, mapper_icon_besttv);
		iconHashMap.put(package_name_daroonplayer, mapper_icon_daroonplayer);
		iconHashMap.put(package_name_kanke, mapper_icon_kanke);
	}

	public HashMap<String, Integer> getIconsMap() {
		return iconHashMap;
	}

	public int getBackDrawable() {
		Log.i("IconUtil", "getBackDrawable");
		return background_pic[(int) (Math.random() * background_pic.length)];
	}
}
