package com.chuanonly.livewallpaper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

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
			R.drawable.bg_fine_night,
			R.drawable.bg_cloudy_night };
	private WallPaperGLsurfaceView mWallpaperView;
	private GalleryScrollView mScrollView;
	private static LinkedList<View> mViewList = new LinkedList<View>();
	private LayoutInflater mInflater;

	private int mItemWidth = MyApplication.width / 5;
	private int mItemHeight = (int) (mItemWidth * 1.6);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		mInflater = LayoutInflater.from(this);
		mScrollView = (GalleryScrollView) findViewById(R.id.scrollview);
		refreshView();
		FrameLayout fLayout = (FrameLayout) findViewById(R.id.wallpaper);
		mWallpaperView = new WallPaperGLsurfaceView(this);
		fLayout.addView(mWallpaperView);
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

			mScrollView.addView(view);
		}

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
	}
}
