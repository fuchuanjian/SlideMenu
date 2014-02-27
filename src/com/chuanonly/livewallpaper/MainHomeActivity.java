package com.chuanonly.livewallpaper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanonly.livewallpaper.service.WallpaperService;
import com.chuanonly.livewallpaper.task.LocateHandler;
import com.chuanonly.livewallpaper.task.HTTPTask;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.URLUtil;
import com.chuanonly.livewallpaper.util.Util;
import com.chuanonly.livewallpaper.view.GalleryScrollView;
import com.chuanonly.livewallpaper.view.WallPaperGLsurfaceView;

public class MainHomeActivity extends Activity
{
	private static HashMap<Integer, BitmapDrawable> sIconMap = new HashMap<Integer, BitmapDrawable>();
	private Integer[] imgages =
	{ 
			R.drawable.bg_rain, 
			R.drawable.bg_cloudy_day, 
			R.drawable.bg_fine_day,
			R.drawable.bg_fog, 
			R.drawable.bg_haze, 
			R.drawable.bg_snow,
			R.drawable.bg_overcast, 
			R.drawable.bg_cloudy_night,
			R.drawable.bg_sand_storm,
			R.drawable.bg_na};
		int[][] imgType = new int[10][];
		
	//R.drawable.bg_rain, 
	//R.drawable.bg_cloudy_day, 
	private WallPaperGLsurfaceView mWallpaperView;
	private GalleryScrollView mScrollView;
	private static LinkedList<View> mViewList = new LinkedList<View>();
	private LayoutInflater mInflater;

	private int mItemWidth = MyApplication.width / 7 ;
	private int mItemHeight = (int) (mItemWidth * 1.5);
	private LocateHandler mlLocateAsyncTask = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		mInflater = LayoutInflater.from(this);
		mScrollView = (GalleryScrollView) findViewById(R.id.scrollview_gallery);
		refreshView();
		FrameLayout fLayout = (FrameLayout) findViewById(R.id.wallpaper);
		mWallpaperView = new WallPaperGLsurfaceView(this);
		fLayout.addView(mWallpaperView);
		findViewById(R.id.setting_menu).setOnClickListener(clickListener);
		findViewById(R.id.setting_wallpaper).setOnClickListener(clickListener);
		//bg_rain
		imgType[0] = new int[]{7,8,9,10,3};
		//bg_cloudy_day
		imgType[1] = new int[]{1};
		//bg_fine_day
		imgType[2] = new int[]{0};
		//bg_fog
		imgType[3] = new int[]{18};
		//bg_haze
		imgType[4] = new int[]{33};
		//bg_snow
		imgType[5] = new int[]{14,15,16,17,13};
		//bg_overcast
		imgType[6] = new int[]{2};
		//bg_fine_night
		imgType[7] = new int[]{100,101};
		//bg_sand_storm
		imgType[8] = new int[]{20,29};
		//bg_na
		imgType[9] = new int[]{5};
		
		String cityCode = Util.getStringFromSharedPref(Util.CODE, "");
		String canLocate = Util.getStringFromSharedPref(Util.CAN_NOT_LOACATE_FLG, "") ;
		if (TextUtils.isEmpty(cityCode) && TextUtils.isEmpty(canLocate)  &&  Util.isNetworkAvailable(getApplicationContext()))
		{
			mlLocateAsyncTask = new LocateHandler(this);
			mlLocateAsyncTask.tryToLacate();
		}
	}

	private void refreshView()
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
			}else if (view.getId() == R.id.setting_wallpaper)
			{
				startIntentWallpaperChooser();
			}else {				
				int index = ((ViewHolder)view.getTag()).index;
				Util.setLongToSharedPref(Util.LAST_PICK_TIME, System.currentTimeMillis());
				changeWallPaper(index);
			}
		}

	};
	
	private void changeWallPaper(int index)
	{
		int category = imgType[index][new Random().nextInt(imgType[index].length)];
		mWallpaperView.setDirty(category);
		Util.setIntToSharedPref(Util.SCENE_TYPE, category);
		
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
		mWallpaperView.onResume();
	}

	@Override
	protected void onPause()
	{
		mWallpaperView.onResume();
		super.onPause();
	}
	@Override
	protected void onDestroy()
	{
		if (mlLocateAsyncTask != null)
		{			
			mlLocateAsyncTask.release();
		}
		super.onDestroy();
	}
	private long lastPressback = 0;

	@Override
	public void onBackPressed()
	{
		if (lastPressback + 2000 < System.currentTimeMillis())
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
        } catch (Exception e) {
            intent = new Intent();
            intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            try {
                startActivity(intent);
            } catch (Exception e2) {
                sucess = false;
            }
        }
        if (sucess == false) {
        	Util.showToast(MyApplication.getContext().getString(R.string.error));
        }
    }
}
