package com.mirageTeam.launcherui.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.IntentCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.baidu.location.f.c;
import com.mirageTeam.CategoryInterface.ICategoryClick;
import com.mirageTeam.business.ApplicationCategoryBusiness;
import com.mirageTeam.common.AllMessageAction;
import com.mirageTeam.common.AllMessageReceiver;
import com.mirageTeam.common.ApplicationCategoryType;
import com.mirageTeam.common.CommonUtils;
import com.mirageTeam.common.DebugFlags;
import com.mirageTeam.controller.ApplicationCategoryController;
import com.mirageTeam.controller.WeatherController;
import com.mirageTeam.db.CategoryDB;
import com.mirageTeam.db.CategoryDBHelp;
import com.mirageTeam.db.CategoryModel;
import com.mirageTeam.launcherui.R;
import com.mirageTeam.service.ApplicationCategoryService;
import com.mirageTeam.widget.AppListBottonMenu;
import com.mirageTeam.widget.AppListMenu;
import com.mirageTeam.widget.ApplicationCategoryMenu;
import com.mirageTeam.widget.CleanMemoryBottomMenu;
import com.mirageTeam.widget.CommonDialog;
import com.mirageTeam.widget.ConfirmDialog;
import com.mirageTeam.widget.HintDialog;
import com.mirageTeam.widget.MovieCategory;
import com.mirageTeam.widget.MusicCategory;
import com.mirageTeam.widget.OptionBottomMenu;
import com.mirageTeam.widget.OptionDialog;
import com.mirageTeam.widget.OptionDialog.onChooseAppsOptionClick;
import com.mirageTeam.widget.RenameDialog;
import com.mirageTeam.widget.ScaleImageButton;
import com.mirageTeam.widget.TVCategory;
import com.mirageTeam.widget.OptionBottomMenu.OptionBottomMenuListener;
import com.mirageTeam.widget.ShortCutGridVIew;
import com.mirageTeam.widget.UpdateTipDialog;
import com.mirageTeam.widget.WallPaperDialog;
import com.mirageTeam.widget.WallPaperDialog.onWallPaperItemClick;
import com.mirageTeam.widget.WeatherBottomMenu;
import com.mirageTeam.widget.WeatherBottomMenu.onCityChooseClick;
import com.mirageTeam.widget.WeatherView;
import com.mirageteam.launcher.market.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class MainUI extends BaseActivity implements OnItemClickListener, OnClickListener, OnLongClickListener {

	// =============================

	final private static int UPDATE_STATE_WIFI = 0;
	final private static int UPDATE_STATE_ETHERNET = 1;
	final private static int UPDATE_STATE_SD = 2;
	final private static int UPDATE_STATE_USB = 3;
	final private static int UPDATE_STATE_SOUND = 4;

	ScaleImageButton movieCatalogButton, browserCatalogButton, tvCatalogButton, marketButton, appsButton, settingsButton;

	// ApplicationCategoryMenu applicationCategory;

	// notification area
	private LinearLayout notification_area;
	private ConnectivityManager mConnectivityManager;
	private AudioManager mAudioManager;
	private WifiManager mWifiManager;

	public static final String SD_PATH = "/storage/external_storage/sdcard1";
	public static final String USB_PATH = "/storage/external_storage";

	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	public final static String UPDATE_WALLPAPER_BROATCAST_ACTION = "com.mirageteam.launcher.wallpaper.acton";
	// 应用列表
	private AppListMenu applist;

	// ========================
	Context mContext;
	PackageManager pm;
	android.app.LoaderManager lm;

	final private static int MSG_UPDATE_WEATHER = 0;
	public static final int MSG_LOAD_APPS_DONE = 99;

	private boolean isLeftDpad = false;

	private AllMessageReceiver messageReceiver;
	private WeatherController mWeatherController;
	private ApplicationCategoryController categoryController;
	private ApplicationCategoryType mType;
	private ApplicationCategoryType optionCategoryType;

	// ApplicationCategoryMenu applicationCategory;
	MusicCategory musicCategory;
	MovieCategory movieCategory;
	TVCategory tvCategory;

	private String shortCutTempName = "";

	private WeatherView weatherView;

	WeatherBottomMenu weatherMenu;
	AppListBottonMenu appMenu;
	OptionBottomMenu optionMenu;

	private ShortCutGridVIew shortCutGridView;

	private View rootLayout;

	private ConfirmDialog mConfirmDialog;

	private UpdateTipDialog mUpdateTipDialog;

	public static WallPaperDialog dialog;
	private final static int UPDATE_WALLPAPER = 0x456;
	public final static int UPDATE_LAUNCHER = 0x457;
	private final static int UPDATE_NETWORK_BROADCAST = 0x458;
	public final static int LAUNCHER_IS_LATEST = 0x459;

	private boolean isSystemDelete = false;

	final ScaleAnimation zoomOutanimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

	private boolean dataChange = false; // 分类数据增加时

	/*
	 * operation preference definition
	 */
	public static final String PREFERENCE_FLAG = "catelog_operation_flag";
	private static final String MOVIE_OPERATION_FLAG = "movie_op_flag";
	private static final String BROWSER_OPERATION_FLAG = "browser_op_flag";
	private static final String TV_OPERATION_FLAG = "tv_op_flag";

	// icon name preference
	private static final String MOVIE_NEW_NAME = "movie_save_name";
	private static final String BROWSER_NEW_NAME = "browser_save_name";
	private static final String TV_NEW_NAME = "tv_save_name";
	private static final String STORE_NEW_NAME = "store_save_name";
	private static final String LOCAL_NEW_NAME = "local_save_name";
	private static final String SETTING_NEW_NAME = "setting_save_name";

	static SharedPreferences flag_preference;

	/*
	 * define user operation type, true or false; false: catelog type; true: app
	 * type; default value: false;
	 */
	private boolean operation_type = false;

	Resources mResources;

	/*
	 * click type, true or false; single click: false; long click: true default
	 * value: false
	 */
	protected boolean CLICK_TYPE = false;

	protected ApplicationCategoryController mApplicationCategoryController;

	HashMap<Integer, ApplicationCategoryType> typeMapper;
	HashMap<Integer, int[]> LocationMapper;

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_WEATHER:
				// TODO: update weather here
				break;

			case MSG_LOAD_APPS_DONE:
				applist.reload();
				break;

			case ApplicationCategoryBusiness.SAVE_CATEGORY_TO_DB_SUCCESS: // save
																			// category
																			// success
				if (!operation_type) {
					if (mType == ApplicationCategoryType.FAST_SHORTCUT) {
						if (msg.obj instanceof String && msg.obj.equals("")) {
							Toast.makeText(mContext, R.string.hint_shortcut_add, Toast.LENGTH_SHORT).show();
							shortCutGridView.onRefresh();
						} else {
							Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						}
						break;
					}

					if (msg.obj instanceof Boolean && ((Boolean) msg.obj)) {
						dataChange = true;
						Toast.makeText(mContext, R.string.hint_catelog_add, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, getString(R.string.is_exist), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, getString(R.string.ok), Toast.LENGTH_SHORT).show();
				}

				// showCategoryMenu();
				break;
			case AllMessageReceiver.PACKAGE_REMOVED:// 应用被删除时
				System.out.println("=========application delete===============");
				isSystemDelete = true;
				categoryController.deleteCategoryBySystemRemoved(mContext, ((Intent) msg.obj).getDataString().split(":")[1].trim());

				applist.reload();
				appMenu.reload();
				break;
			case AllMessageReceiver.PACKAGE_ADDED: // 应用安装
			case AllMessageReceiver.PACKAGE_CHANGED:// 应用改变
				applist.reload();
				appMenu.reload();
				break;

			case ApplicationCategoryBusiness.UPDATE_CATEGORY_BY_ACTIVITY_NAME: // 编辑替换应用
				boolean isSuccess = (Boolean) msg.obj;
				if (isSuccess) {
					applist.dismiss();
					shortCutGridView.onRefresh();
				} else {
					Toast.makeText(mContext, getString(R.string.is_exist), Toast.LENGTH_SHORT).show();
				}
				break;

			case ApplicationCategoryBusiness.DELETE_CATEGORY_BY_SYSTEM_REMOVED_SUCCESS: // 删除快捷方式
																						// 和分类

				if (isSystemDelete) {
					musicCategory.onReresh();
					movieCategory.onReresh();
					tvCategory.onReresh();
					isSystemDelete = false;
					return;
				}
				if (mType == ApplicationCategoryType.FAST_SHORTCUT) {
					shortCutGridView.onRefresh();
				} else {
					// applicationCategory.showApplicationCategoryType(mType);
					// showCategoryMenu(mType);
					refreshCategory(mType);
				}
				break;
			case UPDATE_WALLPAPER:
				Bundle bundle = msg.peekData();
				int wallpaperId = bundle.getInt("set_wallpaper_id");
				loadWallPaper(wallpaperId);
				break;
			case UPDATE_LAUNCHER:
				Bundle bundle2 = msg.getData();
				String url = bundle2.getString("url");
				String description = bundle2.getString("description");
				showUpdateDialog(url, description);
				break;
			case UPDATE_NETWORK_BROADCAST:
				Util.checkUpdate(mHandler, MainUI.this, 0);
				Util.sendMacToService(MainUI.this, 1000);
				break;
			case ApplicationCategoryBusiness.GET_DEFAULT_APP_INFO_SUCCESS:

				// CategoryModel models = (CategoryModel) msg.obj;
				// if (models != null) {
				//
				// // long click, if not is exist, showChooseAppView
				// if (CLICK_TYPE) {
				// showReplaceAppDialog();
				// } else {
				// // single click, open app directly
				// CommonUtils.openApplication(mContext,
				// models.getPackageName(), models.getActivityName());
				//
				// }
				// } else {
				// if (CLICK_TYPE) {
				// showChooseAppView(optionCategoryType);
				// } else {
				// showAddAppItemDialog();
				// }
				// }
				break;
			default:
				break;

			}
		};
	};

	/**
	 * 显示菜单
	 * 
	 * @param type
	 */
	public void showCategoryMenu(ApplicationCategoryType type) {
		if (type == ApplicationCategoryType.Browser) {
			musicCategory.show();
		} else if (type == ApplicationCategoryType.Movie) {
			movieCategory.show();
		} else {
			tvCategory.show();
		}
	}

	/**
	 * 关闭菜单
	 * 
	 * @param type
	 */
	public void dissCategoryMenu(ApplicationCategoryType type) {
		if (type == ApplicationCategoryType.Browser) {
			musicCategory.dismiss();
		} else if (type == ApplicationCategoryType.Movie) {
			movieCategory.dismiss();
		} else {
			tvCategory.dismiss();
		}
	}

	public void refreshCategory(ApplicationCategoryType type) {
		if (type == ApplicationCategoryType.Browser) {
			musicCategory.onReresh();
		} else if (type == ApplicationCategoryType.Movie) {
			movieCategory.onReresh();
		} else {
			tvCategory.onReresh();
		}
	}

	public class categoryClick implements ICategoryClick {
		@Override
		public void onPlusClic(ApplicationCategoryType type) {
			// TODO Auto-generated method stub
			operation_type = false;
			applist.show();
			mType = type;
			dissCategoryMenu(type);
		}

		@Override
		public void onItemClick(String packageName, String activityName) {
			// TODO Auto-generated method stub
			CommonUtils.openApplication(mContext, packageName, activityName);
		}

		@Override
		public void onLongClick(CategoryModel model, ApplicationCategoryType mApplicationCategoryType) {
			// TODO Auto-generated method stub
			categoryController.deleteCategoryBySystemRemoved(MainUI.this, model.getPackageName());
			mType = mApplicationCategoryType;
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_fragment);

		flag_preference = getSharedPreferences(PREFERENCE_FLAG, MODE_PRIVATE);

		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		mContext = this;
		pm = getPackageManager();
		lm = getLoaderManager();
		categoryController = new ApplicationCategoryController(mHandler);

		zoomOutanimation.setDuration(200);
		zoomOutanimation.setFillAfter(false);

		initView();
		initNotificationArea();

		initListener();

		loadDefaultFrag();

		registerStatusBarBroadcast();

	}

	public static boolean getOperationFlagByType(ApplicationCategoryType type) {
		boolean flag = false;
		if (type == ApplicationCategoryType.Movie) {
			flag = flag_preference.getBoolean(MOVIE_OPERATION_FLAG, false);
		} else if (type == ApplicationCategoryType.TV) {
			flag = flag_preference.getBoolean(TV_OPERATION_FLAG, false);
		} else if (type == ApplicationCategoryType.Browser) {
			flag = flag_preference.getBoolean(BROWSER_OPERATION_FLAG, false);
		}
		return flag;
	}

	private void setOperationFlagByType(ApplicationCategoryType type, boolean flag) {
		SharedPreferences.Editor editor = flag_preference.edit();

		if (type == ApplicationCategoryType.Movie) {
			movieCatalogButton.setOperationFlag(flag);
			editor.putBoolean(MOVIE_OPERATION_FLAG, flag);
		} else if (type == ApplicationCategoryType.TV) {
			tvCatalogButton.setOperationFlag(flag);
			editor.putBoolean(TV_OPERATION_FLAG, flag);
		} else if (type == ApplicationCategoryType.Browser) {
			browserCatalogButton.setOperationFlag(flag);
			editor.putBoolean(BROWSER_OPERATION_FLAG, flag);
		}
		editor.commit();
	}

	private void loadDefaultFrag() {
		mApplicationCategoryController = new ApplicationCategoryController(mHandler);

		movieCatalogButton = (ScaleImageButton) findViewById(R.id.btn_movie);
		movieCatalogButton.rename(getSaveIconName(ApplicationCategoryType.Movie));
		movieCatalogButton.setOnClickListener(this);
		movieCatalogButton.setOnLongClickListener(this);
		movieCatalogButton.setOperationFlag(getOperationFlagByType(ApplicationCategoryType.Movie));

		browserCatalogButton = (ScaleImageButton) findViewById(R.id.btn_browser);
		browserCatalogButton.rename(getSaveIconName(ApplicationCategoryType.Browser));
		browserCatalogButton.setOnClickListener(this);
		browserCatalogButton.setOnLongClickListener(this);
		browserCatalogButton.setOperationFlag(getOperationFlagByType(ApplicationCategoryType.Browser));

		tvCatalogButton = (ScaleImageButton) findViewById(R.id.btn_televison);
		tvCatalogButton.rename(getSaveIconName(ApplicationCategoryType.TV));
		tvCatalogButton.setOnClickListener(this);
		tvCatalogButton.setOnLongClickListener(this);
		tvCatalogButton.setOperationFlag(getOperationFlagByType(ApplicationCategoryType.TV));

		marketButton = (ScaleImageButton) findViewById(R.id.btn_appstore);
		marketButton.rename(getSaveIconName(ApplicationCategoryType.STORE));
		marketButton.setOnClickListener(this);
		marketButton.setOnLongClickListener(this);

		appsButton = (ScaleImageButton) findViewById(R.id.btn_apps);
		appsButton.rename(getSaveIconName(ApplicationCategoryType.APP));
		appsButton.setOnClickListener(this);
		appsButton.setOnLongClickListener(this);

		settingsButton = (ScaleImageButton) findViewById(R.id.btn_settings);
		settingsButton.rename(getSaveIconName(ApplicationCategoryType.SETTING));
		settingsButton.setOnClickListener(this);
		settingsButton.setOnLongClickListener(this);

		notification_area = (LinearLayout) findViewById(R.id.notification_area);

		typeMapper = new HashMap<Integer, ApplicationCategoryType>();
		typeMapper.put(R.id.btn_movie, ApplicationCategoryType.Movie);
		// typeMapper.put(R.id.btn_music, ApplicationCategoryType.Music);
		typeMapper.put(R.id.btn_televison, ApplicationCategoryType.TV);

		LocationMapper = new HashMap<Integer, int[]>();
		LocationMapper.put(R.id.btn_movie, new int[] { 602, 145 });
		LocationMapper.put(R.id.btn_browser, new int[] { 792, 145 });
		LocationMapper.put(R.id.btn_televison, new int[] { 820, 327 });
		LocationMapper.put(R.id.btn_appstore, new int[] { 983, 126 });
		LocationMapper.put(R.id.btn_apps, new int[] { 960, 348 });
		LocationMapper.put(R.id.btn_settings, new int[] { 960, 531 });

		mResources = getResources();
	}

	private void showCategoryMenu() {

		// applist.dismiss();
		if (mType != ApplicationCategoryType.FAST_SHORTCUT)
			// applicationCategory.showApplicationCategoryType(mType);
			if (dataChange) {
				refreshCategory(mType);
				dataChange = false;
			}
		showCategoryMenu(mType);
	}

	private void showMenuOption() {
		Log.i("MainUI", "showMenuOption()");
		optionMenu.show();
	}

	private void initView() {
		dialog = new WallPaperDialog(mContext);
		weatherMenu = new WeatherBottomMenu(MainUI.this);
		weatherView = (WeatherView) findViewById(R.id.weather_view);
		appMenu = new AppListBottonMenu(MainUI.this);
		optionMenu = new OptionBottomMenu(MainUI.this);

		applist = new AppListMenu(mContext);
		applist.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if (mType != ApplicationCategoryType.FAST_SHORTCUT && !CLICK_TYPE)
					showCategoryMenu();
				else
					shortCutTempName = "";
			}
		});

		applist.setOnItemClikListener(this);

		// applicationCategory = new ApplicationCategoryMenu(MainUI.this);
		musicCategory = new MusicCategory(mContext, new categoryClick());
		movieCategory = new MovieCategory(mContext, new categoryClick());
		tvCategory = new TVCategory(mContext, new categoryClick());
		shortCutGridView = (ShortCutGridVIew) findViewById(R.id.shortcutgridview);

		rootLayout = (View) findViewById(R.id.rootLayout);

		notification_area = (LinearLayout) findViewById(R.id.notification_area);

		registerReceiver();

		showWeatherView();
		// showShortCutView();

		loadWallPaper(CommonUtils.getWallPaperString(mContext));
	}

	private void showWeatherView() {
		weatherView.setVisibility(View.VISIBLE);
		weatherView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.push_up_in));

		shortCutGridView.setVisibility(View.GONE);
		shortCutGridView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_out));
	}

	private void showShortCutView() {
		weatherView.setVisibility(View.GONE);
		weatherView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.push_up_out));
		shortCutGridView.setVisibility(View.VISIBLE);
		shortCutGridView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in));
		shortCutGridView.setSelection(0);
	}

	private void showWeatherSettings() {
		weatherMenu.show();
	}

	private void showWallPaperSettings() {

		// TODO Auto-generated method stub

		dialog.show();

	}

	void initListener() {

		weatherView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weatherMenu.show();
				// cityView.setVisibility(View.VISIBLE);
			}
		});

		weatherMenu.setOnCityChooseListernerClick(new onCityChooseClick() {

			@Override
			public void onCityChoose(String cityName, int CityCode) {
				// TODO Auto-generated method stub
				if (!CommonUtils.checkNetwork(mContext)) {
					Toast.makeText(mContext, R.string.hint_no_network, Toast.LENGTH_SHORT).show();
					return;
				}
				weatherView.requestWeatherByCity(cityName);
			}
		});

		// musicCategory.setOnICategoryClick(new categoryClick(musicCategory));
		// movieCategory.setOnICategoryClick(new categoryClick(movieCategory));
		// tvCategory.setOnICategoryClick(new categoryClick(tvCategory));

		shortCutGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (shortCutGridView.getType(arg2) == 0) {
					operation_type = false;
					applist.show();
					// applicationCategory.dismiss();
					dissCategoryMenu(mType);
					mType = ApplicationCategoryType.FAST_SHORTCUT;
					operation_type = false;
				} else {
					CommonUtils.openApplication(mContext, shortCutGridView.getModel(arg2).getPackageName(), shortCutGridView.getModel(arg2).getActivityName());
				}
			}
		});

		shortCutGridView.setOnItemLongClickListener(new onItemLongClick());

		// shortCutGridView.setOnICategoryClick(new ICategoryClick() {
		//
		// @Override
		// public void onPlusClic(ApplicationCategoryType type) {
		// // TODO Auto-generated method stub
		// // new LoadAppsTask().execute();
		// applist.show();
		// applicationCategory.dismiss();
		// mType = type;
		// }
		//
		// @Override
		// public void onItemClick(String packageName, String activityName) {
		// // TODO Auto-generated method stub
		//
		// CommonUtils.openApplication(mContext, packageName, activityName);
		// }
		//
		// @Override
		// public void onLongClick(CategoryModel model) {
		// // TODO Auto-generated method stub
		// shortCutTempName = model.getActivityName();
		// // new LoadAppsTask().execute();
		// applist.show();
		// applicationCategory.dismiss();
		// mType = ApplicationCategoryType.FAST_SHORTCUT;
		// }
		// });

		// findViewById(R.id.wallpaper).setOnClickListener(new OnClickListener()
		// {

		// @Override
		// public void onClick(View v) {
		// TODO Auto-generated method stub
		// final WallPaperDialog dialog = new WallPaperDialog(mContext);
		// dialog.setOnWallPaperItemClick(new onWallPaperItemClick() {
		//
		// @Override
		// public void wallPaperItemClick(int position) {
		// // TODO Auto-generated method stub
		// loadWallPaper(WallPaperDialog.RESOURCE_ASSERT[position]);
		//
		// // System.out.println("======================" +
		// // position);
		// // BitmapFactory.Options cwj = new
		// // BitmapFactory.Options();
		// // cwj.inTempStorage = new byte[1024 * 1024 * 5]; //
		// // 5MB的临时存储空间
		// //
		// // try {
		// // Bitmap map = BitmapFactory.decodeStream(getAssets()
		// // .open("bjs.jpg"), new Rect(), cwj);
		// // rootLayout
		// // .setBackgroundDrawable(new BitmapDrawable(
		// // map));
		// // } catch (IOException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		//
		// }
		// });
		// dialog.show();
		// Just for a test, Pls remove it when release
		// showMenuOption();
		// }
		// });

		optionMenu.setOptionBottomMenuListener(new OptionBottomMenuListener() {

			@Override
			public void settingWeather() {
				// TODO Auto-generated method stub
				Log.i("setOptionBottomMenuListener", "========= settingWeather() =============");
				showWeatherSettings();
			}

			@Override
			public void settingWallpaper() {
				// TODO Auto-generated method stub
				Log.i("setOptionBottomMenuListener", "========= settingWallpaper() =============");
				showWallPaperSettings();
			}
		});

		dialog.setOnWallPaperItemClick(new onWallPaperItemClick() {

			@Override
			public void wallPaperItemClick(final int position) {

				final ConfirmDialog wallPaperSettingConfirmDialog = new ConfirmDialog(MainUI.this);
				wallPaperSettingConfirmDialog.setDialogTitle(R.string.dialog_title).setPositiveButton(R.string.ok, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// loadWallPaper(WallPaperDialog.RESOURCE_ASSERT[position]);
						CommonUtils.saveWallPaperCustomMark(MainUI.this, false);

						loadWallPaper(WallPaperDialog.BACKGROUND_RESOURCES[position]);
						wallPaperSettingConfirmDialog.dismiss();
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.cancel, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						wallPaperSettingConfirmDialog.dismiss();
						dialog.dismiss();
					}
				}).show();

			}

		});
	}

	public class onItemLongClick implements OnItemLongClickListener {

		private ConfirmDialog commonDialog;

		public onItemLongClick() {
			commonDialog = new ConfirmDialog(mContext);
			commonDialog.setDialogTitle(R.string.edit_mode);
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
			// TODO Auto-generated method stub
			if (shortCutGridView.getType(arg2) == 0) {
				return false;
			}
			commonDialog.setPositiveButton(R.string.delete_item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mType = ApplicationCategoryType.FAST_SHORTCUT;
					categoryController.deleteCategoryBySystemRemoved(mContext, shortCutGridView.getModel(arg2).getPackageName());
					commonDialog.dismiss();

				}
			}).setNegativeButton(R.string.replace, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					commonDialog.dismiss();
					shortCutTempName = shortCutGridView.getModel(arg2).getActivityName();
					// new LoadAppsTask().execute();
					operation_type = false;
					applist.show();
					// applicationCategory.dismiss();
					dissCategoryMenu(mType);
					mType = ApplicationCategoryType.FAST_SHORTCUT;
				}
			}).show();

			return true;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
			// // showCategoryMenu();
			// if (mAppView.getVisibility() == View.VISIBLE)
			// mAppView.setVisibility(View.GONE);

		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (shortCutGridView.hasFocus()) {
				// showWeatherView();
				return true;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_LEFT:
			isLeftDpad = true;
			System.out.println("==========KEYCODE_DPAD_LEFT============" + weatherView.isFocused() + "+==" + shortCutGridView.isFocused() + "====="
					+ movieCatalogButton.isFocused());
			if (movieCatalogButton.hasFocus() || tvCatalogButton.hasFocus()) {
				showShortCutView();
			}

			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			System.out.println("==========KEYCODE_DPAD_RIGHT===========" + weatherView.hasFocus() + "+====" + shortCutGridView.hasFocus() + "===="
					+ movieCatalogButton.hasFocus());
			isLeftDpad = false;
			if (shortCutGridView.hasFocus()) {
				showWeatherView();
			}
			break;

		case KeyEvent.KEYCODE_DPAD_UP:
			if (shortCutGridView.hasFocus()) {
				// showWeatherView();
				return true;
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			event.startTracking();
			if (shortpressflag) {
				Log.i("KEYCODE_MENU", "KEYCODE_MENU =======onKeyUp==== ");
				// Toast.makeText(mContext, "KEYCODE_MENU =======onKeyUp====",
				// Toast.LENGTH_SHORT).show();
				showMenuOption();
			}
			shortpressflag = true;
			flag = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void registerReceiver() {
		messageReceiver = new AllMessageReceiver(mHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(AllMessageAction.PACKAGE_REMOVED);
		filter.addAction(AllMessageAction.PACKAGE_ADDED);
		filter.addAction(AllMessageAction.PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(messageReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(messageReceiver);
		unregisterStatusBarBroadcast();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		ResolveInfo mResolveInfo = (ResolveInfo) adapter.getItemAtPosition(position);
		String packageName = mResolveInfo.activityInfo.packageName;
		String activityName = mResolveInfo.activityInfo.name;
		CategoryModel model = new CategoryModel();
		model.setActivityName(activityName);
		model.setPackageName(packageName);
		model.setIconRes(mResolveInfo.getIconResource());
		model.setLabel(mResolveInfo.activityInfo.loadLabel(getPackageManager()).toString());

		if (!operation_type) {
			// 文件夹分类操作
			operation_type = false;
			model.setType(CommonUtils.transferEnumToInt(mType));
			model.setOpType(CategoryDBHelp.CATELOG_FLAG);
		} else {
			// 分类快捷应用操作
			operation_type = true;
			model.setOpType(CategoryDBHelp.APP_FLAG);
			model.setType(CommonUtils.transferEnumToInt(optionCategoryType));
		}

		if (!shortCutTempName.equals("")) {
			categoryController.updateCategoryByActivityName(mContext, model, shortCutTempName,
					CommonUtils.transferEnumToInt(ApplicationCategoryType.FAST_SHORTCUT));

		} else {
			categoryController.saveCategory(mContext, model);
		}
	}

	void loadWallPaper(final int resource) {
		if (!CommonUtils.getWallPaperCustomMark(mContext)) {
			rootLayout.setBackgroundResource(resource);
			CommonUtils.saveWallPaperString(mContext, resource);
		} else {
			// from data/data/packagename/wallpaper.png set wallpaper
			File file = new File("/data/data/" + getPackageName() + "/wallpaper/default_wallpaper.png");
			// 如果文件有效
			if (file.exists()) {
				rootLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), Util.getWallpaperFromFile(file)));
			} else { // 如果文件无效
				rootLayout.setBackgroundResource(resource);
				CommonUtils.saveWallPaperString(mContext, resource);
			}
		}

		// ImageLoader.getInstance().loadImage("assets://" + resource, options,
		// new SimpleImageLoadingListener() {
		// @Override
		// public void onLoadingComplete(String imageUri, View view, Bitmap
		// loadedImage) {
		// // TODO Auto-generated method stub
		// super.onLoadingComplete(imageUri, view, loadedImage);
		// rootLayout.setBackgroundDrawable(new BitmapDrawable(loadedImage));
		// CommonUtils.saveWallPaperString(mContext, resource);
		//
		// }
		// });
	}

	private void queryAppByType(ApplicationCategoryType type) {
		// mApplicationCategoryController.getDefaultAppInfo(mContext,
		// CommonUtils.transferEnumToInt(type));
		CategoryModel model = null;
		try {
			model = ApplicationCategoryService.getInstance().getDefaultAppByType(mContext, CommonUtils.transferEnumToInt(type));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (model != null && !CLICK_TYPE) {
			CommonUtils.openApplication(mContext, model.getPackageName(), model.getActivityName());
		} else if (model != null && CLICK_TYPE) {
			// 长按
			showReplaceAppDialog(model.getLabelRes());
		} else if (model == null && CLICK_TYPE) {
			showChooseAppView(type);
		} else {
			showAddAppItemDialog();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!v.hasFocus()) {
			v.bringToFront();
			v.startAnimation(zoomOutanimation);
		}
		CLICK_TYPE = false;

		switch (v.getId()) {
		case R.id.btn_movie:
			// TODO: pop movie apps windows here
			if (movieCatalogButton.getOperationFlag()) {
				queryAppByType(ApplicationCategoryType.Movie);
			} else {
				showCategoryMenu(ApplicationCategoryType.Movie);
			}
			break;
		case R.id.btn_browser:
			// TODO: pop music apps windows here
			if (browserCatalogButton.getOperationFlag()) {
				queryAppByType(ApplicationCategoryType.Browser);
			} else {
				// showCategoryMenu(ApplicationCategoryType.Browser);
				gotoSystemBrowser();
			}

			break;
		case R.id.btn_televison:
			// TODO: pop television apps windows here
			if (tvCatalogButton.getOperationFlag()) {
				queryAppByType(ApplicationCategoryType.TV);
			} else {
				showCategoryMenu(ApplicationCategoryType.TV);
			}
			break;
		case R.id.btn_appstore:
			Intent marketIntent = new Intent(this, com.mirageteam.launcher.market.MainActivity.class);
			startActivity(marketIntent);
			break;
		case R.id.btn_apps:
			showApplistMenu();
			break;
		case R.id.btn_settings:
			showSettingsMenu();
			break;
		default:
			break;
		}
	}

	private void gotoSystemBrowser() {
		Intent mIntent = new Intent();
		mIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		startActivity(mIntent);
	}

	private void showApplistMenu() {
		appMenu.show();
	}

	private void showSettingsMenu() {
		// TODO: show settings windows here
		Intent settingtIntent = new Intent(this, com.mirageteam.launcher.setting.Setting.class);
		startActivity(settingtIntent);
	}

	private void initNotificationArea() {
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	}

	public void updateStatusBarIcon() {

		notification_area.removeAllViews();

		// updateWifiState();
		// updateEthernetState();
		// updateSdcardState();
		// updateUSBState();
		// updateVolumeState();

	}

	private void updateSdcardState() {
		if (isSdcardExists()) {
			ImageView sdcard = new ImageView(this);
			sdcard.setImageResource(R.drawable.sdcard);
			params.leftMargin = 10;
			notification_area.addView(sdcard, params);
		}
	}

	private void updateUSBState() {
		if (isUsbExists()) {
			ImageView usb = new ImageView(this);
			usb.setImageResource(R.drawable.usbdevices);
			params.leftMargin = 10;
			notification_area.addView(usb, params);
		}
	}

	private void updateVolumeState() {

		ImageView sound = new ImageView(this);
		params.leftMargin = 10;

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Log.v("carter", "ring mote=" + mAudioManager.getRingerMode());
		if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
			// nute
			sound.setImageResource(R.drawable.sound_off);
		} else {
			// sound
			sound.setImageResource(R.drawable.sound_on);
		}

		notification_area.addView(sound, params);

	}

	private void updateWifiState() {

		WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
		int wifi_rssi = mWifiInfo.getRssi();
		int wifi_level = WifiManager.calculateSignalLevel(wifi_rssi, 5);

		if (wifi_level > 0) {
			ImageView wifi = new ImageView(this);
			wifi.setImageResource(R.drawable.wifi);
			params.leftMargin = 10;
			notification_area.addView(wifi, params);
		}

	}

	private void updateEthernetState() {
		if (isEthernetOn()) {
			ImageView eth = new ImageView(this);
			eth.setImageResource(R.drawable.ethernet);
			params.leftMargin = 10;
			notification_area.addView(eth, params);
		}

	}

	public static boolean isSdcardExists() {
		// if
		// (Environment.getExternalStorage2State().startsWith(Environment.MEDIA_MOUNTED))
		// {
		// File dir = new File(SD_PATH);
		// if (dir.exists() && dir.isDirectory()) {
		// return true;
		// }
		// }
		return false;
	}

	private boolean isEthernetOn() {
		NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		if (info.isConnected()) {
			// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ true");
			return true;
		} else {
			// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ false");
			return false;
		}
	}

	public static boolean isUsbExists() {
		File dir = new File(USB_PATH);
		if (dir.exists() && dir.isDirectory()) {
			if (dir.listFiles() != null) {
				if (dir.listFiles().length > 0) {
					for (File file : dir.listFiles()) {
						String path = file.getAbsolutePath();
						if (path.startsWith(USB_PATH + "/sd") && !path.equals(SD_PATH)) {
							// if (path.startsWith("/mnt/sd[a-z]")){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/*
	 * network status
	 */
	public void registNetworkBroadcast() {
		IntentFilter networkFilter = new IntentFilter();
		networkFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(mNetworkStateChangedReceiver, networkFilter);
	}

	private BroadcastReceiver mNetworkStateChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// update network wifi or ethernet
			updateStatusBarIcon();

			// Log.v("MainUI", ">>>>>>>>>>>>>network change");
			mHandler.sendEmptyMessage(UPDATE_NETWORK_BROADCAST);

		}
	};

	/*
	 * storage status
	 */
	public void registStorageBroadcast() {

		IntentFilter f = new IntentFilter();
		f.addAction(Intent.ACTION_MEDIA_REMOVED);
		f.addAction(Intent.ACTION_MEDIA_EJECT);
		f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		f.addAction(Intent.ACTION_MEDIA_MOUNTED);
		f.addDataScheme("file");
		registerReceiver(mStorageListener, f);
	}

	private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// update media sdcard and usb
			updateStatusBarIcon();
		}
	};

	/*
	 * volume status
	 */
	private BroadcastReceiver mVolumeListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// update storage volume or mute
			updateStatusBarIcon();

		}
	};

	public void registVolumeBroadcast() {
		IntentFilter volumeFilter = new IntentFilter();
		volumeFilter.addAction(AudioManager.VOLUME_CHANGED_ACTION);
		registerReceiver(mVolumeListener, volumeFilter);
	}

	private void registerStatusBarBroadcast() {

		registNetworkBroadcast();
		registStorageBroadcast();
		registVolumeBroadcast();

		registWallpaperBroadcast();

	}

	private void unregisterStatusBarBroadcast() {
		unregisterReceiver(mNetworkStateChangedReceiver);
		unregisterReceiver(mStorageListener);
		unregisterReceiver(mVolumeListener);
		unregisterReceiver(mWallpaperListener);
	}

	/*
	 * wallpaper
	 */
	private BroadcastReceiver mWallpaperListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// update storage volume or mute
			Log.v("AAA", "bbb");
			if (intent.getAction().equals(UPDATE_WALLPAPER_BROATCAST_ACTION)) {

				Message message = new Message();
				message.what = UPDATE_WALLPAPER;

				Bundle bundle = new Bundle();
				// bundle.putString("set_wallpaper_url",
				// intent.getStringExtra("set_wallpaper_url"));
				bundle.putInt("set_wallpaper_id", intent.getIntExtra("set_wallpaper_id", 0));
				message.setData(bundle);
				Log.v("AAA", "aaaaaaaa=" + intent.getIntExtra("set_wallpaper_id", 0));
				mHandler.sendMessage(message);
			}
		}
	};

	public void registWallpaperBroadcast() {
		IntentFilter wallpaperFilter = new IntentFilter();
		wallpaperFilter.addAction(UPDATE_WALLPAPER_BROATCAST_ACTION);
		registerReceiver(mWallpaperListener, wallpaperFilter);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		CLICK_TYPE = true;
		boolean opflag = false;
		int mDrawable = 0;
		int[] location = new int[2];

		switch (v.getId()) {
		case R.id.btn_movie:
			optionCategoryType = ApplicationCategoryType.Movie;
			opflag = movieCatalogButton.getOperationFlag();
			location = LocationMapper.get(R.id.btn_movie);
			mDrawable = R.drawable.option_movie;
			break;
		case R.id.btn_televison:
			optionCategoryType = ApplicationCategoryType.TV;
			opflag = tvCatalogButton.getOperationFlag();
			location = LocationMapper.get(R.id.btn_televison);
			mDrawable = R.drawable.option_tv;
			break;
		case R.id.btn_browser:
			optionCategoryType = ApplicationCategoryType.Browser;
			opflag = browserCatalogButton.getOperationFlag();
			location = LocationMapper.get(R.id.btn_browser);
			mDrawable = R.drawable.option_browser;
			break;
		case R.id.btn_appstore:
			optionCategoryType = ApplicationCategoryType.STORE;
			location = LocationMapper.get(R.id.btn_appstore);
			mDrawable = R.drawable.option_store;
			break;
		case R.id.btn_apps:
			optionCategoryType = ApplicationCategoryType.APP;
			location = LocationMapper.get(R.id.btn_apps);
			mDrawable = R.drawable.option_addlist;
			break;
		case R.id.btn_settings:
			optionCategoryType = ApplicationCategoryType.SETTING;
			location = LocationMapper.get(R.id.btn_settings);
			mDrawable = R.drawable.option_settings;
			break;
		default:
			break;
		}

		final OptionDialog optionDialog = new OptionDialog(mContext, mDrawable, optionCategoryType, opflag);
		optionDialog.setOnChooseAppsOptionClick(new onChooseAppsOptionClick() {

			@Override
			public void ChooseAppsOptionClick(ApplicationCategoryType type) {

				if (type == ApplicationCategoryType.Browser && getOperationFlagByType(type)) {

					setOperationFlagByType(type, false);
					Toast.makeText(mContext, "已切换到默认操作", Toast.LENGTH_SHORT).show();

				} else {
					optionCategoryType = type;
					setOperationFlagByType(type, true);
					queryAppByType(type);
					optionDialog.dismiss();
				}

			}

			@Override
			public void ChooseCatelogOptionClick(ApplicationCategoryType type) {

				optionCategoryType = type;
				setOperationFlagByType(type, false);
				optionDialog.dismiss();
			}

			@Override
			public void ChooseRenameOpionClick(ApplicationCategoryType type) {
				showRenameDialog(type);
				optionDialog.dismiss();

			}
		});
		optionDialog.dialog_show(location[0], location[1]);
		return true;
	}

	public void showChooseAppView(ApplicationCategoryType type) {
		operation_type = true;
		mType = type;
		applist.show();
	}

	public void showUpdateDialog(final String url, final String description) {

		if (mUpdateTipDialog != null && mUpdateTipDialog.isShowing())
			return;
		mUpdateTipDialog = new UpdateTipDialog(MainUI.this);

		mUpdateTipDialog.setDialogMessage(description);
		mUpdateTipDialog.setDialogTitle(R.string.launcher_update_title).setPositiveButton(R.string.update, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.startDownloadService(MainUI.this, url.replaceAll(" ", "%20"), getPackageName());
				Toast.makeText(MainUI.this, getResources().getString(R.string.downloading_background), 1500).show();
				mUpdateTipDialog.dismiss();
			}
		}).setNegativeButton(R.string.cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mUpdateTipDialog.dismiss();

			}
		}).show();
	}

	public void showReplaceAppDialog(String name) {
		if (mConfirmDialog != null && mConfirmDialog.isShowing())
			return;
		mConfirmDialog = new ConfirmDialog(MainUI.this);

		mConfirmDialog.setDialogTitle(getString(R.string.hint_replace_app_head) + name + getString(R.string.hint_replace_app_tail))
				.setPositiveButton(R.string.replace, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showChooseAppView(optionCategoryType);
						mConfirmDialog.dismiss();
					}
				}).setNegativeButton(R.string.go_cancel, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mConfirmDialog.dismiss();

					}
				}).show();
	}

	public void showAddAppItemDialog() {
		// Toast.makeText(mContext, "showAddAppItemDialog",
		// Toast.LENGTH_SHORT).show();
		final HintDialog mHintDialog = new HintDialog(mContext);

		mHintDialog.setDialogTitle(R.string.hint_openapp).setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHintDialog.dismiss();
			}
		}).show();
	}

	RenameDialog mRenameDialog;

	public void showRenameDialog(final ApplicationCategoryType type) {
		if (mRenameDialog != null && mRenameDialog.isShowing()) {
			return;
		}
		mRenameDialog = new RenameDialog(mContext);
		mRenameDialog.setDialogTitle(R.string.option_rename).setPositiveButton(R.string.ok, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = mRenameDialog.getEditString();
				String key = "";
				if (type == ApplicationCategoryType.Movie) {
					movieCatalogButton.rename(name);
					key = MOVIE_NEW_NAME;
				} else if (type == ApplicationCategoryType.Browser) {
					browserCatalogButton.rename(name);
					key = BROWSER_NEW_NAME;
				} else if (type == ApplicationCategoryType.TV) {
					tvCatalogButton.rename(name);
					key = TV_NEW_NAME;
				} else if (type == ApplicationCategoryType.STORE) {
					marketButton.rename(name);
					key = STORE_NEW_NAME;
				} else if (type == ApplicationCategoryType.APP) {
					appsButton.rename(name);
					key = LOCAL_NEW_NAME;
				} else if (type == ApplicationCategoryType.SETTING) {
					settingsButton.rename(name);
					key = SETTING_NEW_NAME;
				}
				saveIconNewName(key, name);
				mRenameDialog.dismiss();
			}
		}).setNegativeButton(R.string.go_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRenameDialog.dismiss();

			}
		}).show();
	}

	public void saveIconNewName(String key, String name) {
		SharedPreferences.Editor editor = flag_preference.edit();
		editor.putString(key, name);
		editor.commit();
	}

	public String getSaveIconName(ApplicationCategoryType type) {
		String name = "";
		if (type == ApplicationCategoryType.Movie) {
			name = flag_preference.getString(MOVIE_NEW_NAME, getString(R.string.movie_string));
		} else if (type == ApplicationCategoryType.TV) {
			name = flag_preference.getString(TV_NEW_NAME, getString(R.string.tv_string));
		} else if (type == ApplicationCategoryType.Browser) {
			name = flag_preference.getString(BROWSER_NEW_NAME, getString(R.string.browser_string));
		} else if (type == ApplicationCategoryType.STORE) {
			name = flag_preference.getString(STORE_NEW_NAME, getString(R.string.appstore_string));
		} else if (type == ApplicationCategoryType.APP) {
			name = flag_preference.getString(LOCAL_NEW_NAME, getString(R.string.applist_string));
		} else if (type == ApplicationCategoryType.SETTING) {
			name = flag_preference.getString(SETTING_NEW_NAME, getString(R.string.settings_string));
		}
		return name;
	}
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// float x = event.getRawX();
	// float y = event.getRawY();
	// Toast.makeText(mContext, "======================== x = " + x +
	// "================== y = " + y, Toast.LENGTH_SHORT).show();
	// Log.d("onTouchEvent", "======================== x = " + x +
	// "================== y = " + y);
	//
	// return super.onTouchEvent(event);
	// }
}
