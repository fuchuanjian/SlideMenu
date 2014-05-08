package com.chuanonly.livewallpaper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.service.WallpaperService;
import com.chuanonly.livewallpaper.task.HTTPTask;
import com.chuanonly.livewallpaper.task.LocateHandler;
import com.chuanonly.livewallpaper.util.URLUtil;
import com.chuanonly.livewallpaper.util.Util;
import com.chuanonly.livewallpaper.view.GalleryScrollView;
import com.chuanonly.livewallpaper.view.WallPaperGLsurfaceView;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

public class MainHomeActivity extends Activity
{
	private static final String ID = "kFVFFTT6V0dPR0Z08ERrFjWHZVa=";
	private static final String ADID ="a15310888895deb";
	private static HashMap<Integer, BitmapDrawable> sIconMap = new HashMap<Integer, BitmapDrawable>();
	private Integer[] imgages =
	{ 

			R.drawable.bg_fine_day,
			R.drawable.bg_cloudy_day, 
			R.drawable.bg_snow,
			R.drawable.bg_rain, 
			R.drawable.bg_thunder_storm,
			R.drawable.bg_cloudy_night,
			R.drawable.bg_fine_night,
			R.drawable.bg_fog, 
			R.drawable.bg_haze,
			R.drawable.bg_overcast
	};
	public static int[][] imgType =
	{
			// bg_fine_day
			{ 0 },
			// bg_cloudy_day
			{ 1 },
			// bg_snow
			{ 5, 14, 15, 16, 17, 13 },
			// bg_rain
			{ 7, 8, 9, 10, 3 },
			// bg_na
			{ 4 },
			// bg_fine_night
			{ 101 },
			// bg_cloud_night
			{ 100 },
			// bg_fog
			{ 18, 20, 29 },
			// bg_haze
			{ 33 },
			// bg_overcast
			{ 2 } };
		
	//R.drawable.bg_rain, 
	//R.drawable.bg_cloudy_day, 
	private WallPaperGLsurfaceView mWallpaperView;
	private GalleryScrollView mScrollView;
	private static LinkedList<View> mViewList = new LinkedList<View>();
	private LayoutInflater mInflater;

	private int mItemWidth =(int) ((float) MyApplication.width / 6.5 );
	private int mItemHeight = (int) (mItemWidth * 1.4);
	private LocateHandler mlLocateAsyncTask = null;
	private Handler mHandler = new Handler();
	private LinearLayout mContentLayout ;
	private LinearLayout mSettingLayout;
	private AdView mAdView;
	private LinearLayout mADLayout;
	private ImageView mArrowIV;
	private TextView mWeatherInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		mInflater = LayoutInflater.from(this);
		mContentLayout = (LinearLayout)findViewById(R.id.layout_content);
		mSettingLayout = (LinearLayout) findViewById(R.id.setting_panel_layout);
		mADLayout = (LinearLayout) findViewById(R.id.layout_ad);
		mWeatherInfo = (TextView) findViewById(R.id.city_info);
		mContentLayout.setVisibility(View.INVISIBLE);
		mSettingLayout.setVisibility(View.INVISIBLE);
		mWeatherInfo.setVisibility(View.INVISIBLE);
		mScrollView = new GalleryScrollView(this);
		mContentLayout.addView(mScrollView);
		initScrollView();
		FrameLayout fLayout = (FrameLayout) findViewById(R.id.wallpaper);
		mWallpaperView = new WallPaperGLsurfaceView(this);
		fLayout.addView(mWallpaperView);
		findViewById(R.id.setting_menu).setOnClickListener(clickListener);
		findViewById(R.id.setting_wallpaper).setOnClickListener(clickListener);
		mArrowIV = (ImageView) findViewById(R.id.setting_arrow);
		mArrowIV.setOnClickListener(clickListener);
		
		
//		//bg_fine_day
//		imgType[0] = new int[]{0};
//		//bg_cloudy_day
//		imgType[1] = new int[]{1};
//		//bg_snow
//		imgType[2] = new int[]{5,14,15,16,17,13};
//		//bg_rain
//		imgType[3] = new int[]{7,8,9,10,3};
//		//bg_na
//		imgType[4] = new int[]{4};
//		//bg_fine_night
//		imgType[5] = new int[]{101};
//		//bg_cloud_night
//		imgType[6] = new int[]{100};
//		//bg_fog
//		imgType[7] = new int[]{18,20,29};
//		//bg_haze
//		imgType[8] = new int[]{33};
//		//bg_overcast
//		imgType[9] = new int[]{2};
//		//bg_sand_storm
////		imgType[8] = new int[]{20,29};
		checkifNeedTolacate();
		checkCanShowAd();
//		String enString = URLUtil.encodeURL("a15310888895deb");

	}


	private void checkCanShowAd()
	{
		int loginCnt = Util.getIntFromSharedPref(Util.LOG_INT_CNT, 0);
		if (loginCnt >= 0 && Util.isNetworkAvailable(getApplicationContext()))
		{			
			mAdView = new AdView(this, AdSize.BANNER, URLUtil.decodeURL(ID));
//			mAdView = new AdView(this, AdSize.BANNER, ADID);
			mADLayout.addView(mAdView);
			mAdView.loadAd(new AdRequest());
			mAdView.setAdListener(new AdListener()
			{
				@Override
				public void onReceiveAd(Ad arg0)
				{
				}
				@Override
				public void onPresentScreen(Ad arg0)
				{
				}
				@Override
				public void onLeaveApplication(Ad arg0)
				{
				}
				@Override
				public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1)
				{
				}
				@Override
				public void onDismissScreen(Ad arg0)
				{
					Util.setIntToSharedPref(Util.LOG_INT_CNT, -2);
					mAdView.setVisibility(View.GONE);
				}
			});
			
		}
		Util.setIntToSharedPref(Util.LOG_INT_CNT, loginCnt+1);
		
	}


	private void initScrollView()
	{
		// clear
		int size = mScrollView.getChildCount();
		View view = null;
		for (int i = 0; i < size; i++)
		{
			view = mScrollView.getChildAt(i);
			if (view != null)
			{
				mViewList.add(view);
			}
			mScrollView.removeView(view);
		}

		mScrollView.scrollTo(0, mScrollView.getScrollY());
		for (int i = 0; i < imgages.length; i++)
		{
			view = mViewList.poll();
			ViewHolder holder = null;
			if (view == null)
			{
				view = mInflater.inflate(R.layout.item_scrollview, null);
				android.view.ViewGroup.LayoutParams params = view
						.getLayoutParams();
				if (params == null)
				{
					params = new LayoutParams(mItemWidth,
							LayoutParams.MATCH_PARENT);
				} else
				{
					params.width = mItemWidth;
					params.height = mItemHeight;
				}
				view.setLayoutParams(params);
				holder = new ViewHolder();
				holder.icon = (ImageView) view.findViewById(R.id.icon);
				view.setTag(holder);
			} else
			{
				holder = (ViewHolder) view.getTag();
			}
			holder.icon.setImageDrawable(getIconDrawable(imgages[i]));
			holder.index = i;
			view.setOnClickListener(clickListener);
			mScrollView.addView(view);
		}

	}
	private OnClickListener clickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View view)
		{
			if (view.getId() == R.id.setting_menu)
			{
				Intent intent = new Intent(MainHomeActivity.this, SettingActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_defalut);
			}else if (view.getId() == R.id.setting_wallpaper)
			{
				startIntentWallpaperChooser();
			}else if (view.getId() == R.id.city_info)
			{
				if (Util.isNetworkAvailable(MyApplication.getContext()) 
						&& !TextUtils.isEmpty( Util.getStringFromSharedPref(Util.CODE, "")))
				{
					new HTTPTask().execute();
				}
				int sence = Util.getIntFromSharedPref(Util.REAL_TYPE, -1);
				if (sence >= 0)
				{
					sence = Util.normalDayOrNight(sence);
					mWallpaperView.setDirty(sence);
				}
			}else if (view.getId() == R.id.setting_arrow)
			{
				if (mContentLayout.getVisibility() == View.VISIBLE)
				{
					mContentLayout.setVisibility(View.GONE);
					mArrowIV.setImageResource(R.drawable.arrow_up);
				}else 
				{
					mContentLayout.setVisibility(View.VISIBLE);
					mArrowIV.setImageResource(R.drawable.arrow_down);
				}
			}else if (lastClickTime + 300 < System.currentTimeMillis())
			{				
				lastClickTime = System.currentTimeMillis();
				int index = ((ViewHolder)view.getTag()).index;
				Util.setLongToSharedPref(Util.LAST_PICK_TIME, System.currentTimeMillis());
				changeWallPaper(index);
			}
		}

	};
	private long lastClickTime = 0;
	private void changeWallPaper(int index)
	{
		int category = imgType[index][new Random().nextInt(imgType[index].length)];
		mWallpaperView.setDirty(category);
		Util.setIntToSharedPref(Util.TYPE, category);
		
	}
	private Drawable getIconDrawable(int resId)
	{
		BitmapDrawable d = sIconMap.get(resId);

		if (d == null)
		{
			Bitmap b = BitmapFactory.decodeResource(this.getResources(), resId);
			d = new BitmapDrawable(this.getResources(), b);
			sIconMap.put(resId, d);
		}
		return d;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver();
		Util.checkIfNeedToUpdateWeather();
		mWallpaperView.onResume();
		mHandler.postDelayed(mResumeRunnable, 200);
	}
	
	private Runnable mResumeRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if (mContentLayout.getVisibility() == View.GONE)
			{				
			}else {
				mContentLayout.setVisibility(View.VISIBLE);
			}
			mSettingLayout.setVisibility(View.VISIBLE);
			checkTitleBar();
		}
	};

	private void checkTitleBar()
	{
		String cityName = Util.getCityName();
		String info = Util.getWeatherInfoOfReal();
		String temperatrue = Util.getStringFromSharedPref(Util.SCENE_TEMPERATUR, "");
		if (!TextUtils.isEmpty(temperatrue))
		{
			temperatrue = temperatrue + MyApplication.getContext().getString(R.string.temp_unit);
		}
		if (!TextUtils.isEmpty(cityName))
		{
			mWeatherInfo.setVisibility(View.VISIBLE);
			mWeatherInfo.setText(cityName +" \t "+info +" \t "+ temperatrue);
		}else {
			mWeatherInfo.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onPause()
	{
		mHandler.removeCallbacks(mResumeRunnable);
		unregisterReceiver();
		mWallpaperView.onResume();
		super.onPause();
	}
	@Override
	protected void onDestroy()
	{
		if (mAdView != null)
		{
			mAdView.destroy();
		}
		if (mlLocateAsyncTask != null)
		{			
			mlLocateAsyncTask.release();
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());
//		System.exit(0);
		super.onDestroy();
	}
	private long lastPressback = 0;

	@Override
	public void onBackPressed()
	{
		if (lastPressback + 3000 < System.currentTimeMillis())
		{
			lastPressback = System.currentTimeMillis();
			Util.showToast(MyApplication.getContext().getString(
					R.string.toast_exit));
		} else
		{
			super.onBackPressed();
		}
	}

	private static class ViewHolder
	{
		public ImageView icon;
		public TextView desc;
		public int index;
	}
	
    private void startIntentWallpaperChooser() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (!supportsEs2) {
        	Util.showToast(MyApplication.getContext().getString(R.string.error));
            return;
        }

        boolean sucess = true;
        Intent intent = new Intent();

        intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, WallpaperService.class));
        try {
            startActivity(intent);
			overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_defalut);
        } catch (Exception e) {
            intent = new Intent();
            intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            try {
                startActivity(intent);
				overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_defalut);
            } catch (Exception e2) {
                sucess = false;
            }
        }
        if (sucess == false) {
        	Util.showToast(MyApplication.getContext().getString(R.string.error));
        }
    }
    private IntentFilter mFilter;
    private void registerReceiver()
	{
		if (mFilter == null)
		{
			mFilter = new IntentFilter();
			mFilter.addAction(WallpaperService.ACTION_CHANGE_BROCAST);
		}
		try
		{
			registerReceiver(mReceiver, mFilter);
		} catch (Exception e)
		{
		}
	}
    private void unregisterReceiver()
	{
		try
		{
			this.unregisterReceiver(mReceiver);
		} catch (Exception e)
		{
		}
	}
    private BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (WallpaperService.ACTION_CHANGE_BROCAST.equals(action))
			{
				if (mWallpaperView == null)
					return;
				int category  = Util.getIntFromSharedPref(Util.REAL_TYPE, WeatherType.FINE);
				category = Util.normalDayOrNight(category);
				mWallpaperView.setDirty(category);
				checkTitleBar();
			}
		};
	};
	
	private void checkifNeedTolacate()
	{
		String cityCode = Util.getStringFromSharedPref(Util.CODE, "");
		int mode = Util.getIntFromSharedPref(Util.MODE, -1);
		
		if (MyApplication.language <2 && Util.isNetworkAvailable(MyApplication.getContext()))
		{
			if (TextUtils.isEmpty(cityCode))
			{
				if (mode == -1)
				{
					mlLocateAsyncTask = new LocateHandler(this);
					mlLocateAsyncTask.tryToLacate();
				}
			}
		}
	}
}
