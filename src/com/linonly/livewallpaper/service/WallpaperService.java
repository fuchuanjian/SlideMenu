package com.linonly.livewallpaper.service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import com.linonly.livewallpaper.model.WeatherType;
import com.linonly.livewallpaper.task.HTTPTask;
import com.linonly.livewallpaper.util.Util;

public class WallpaperService extends GLWallpaperService
{

	public static final String ACTION_CHANGE_BROCAST = "com.chuanonly.livewallpaper.changewallpaper";

	public static final String ACTION_PAUSE_BROCAST = "com.chuanonly.livewallpaper.pausewallpaper";

	public final static int RATE = 50;

	private WallPaperRender renderer;

	private ArrayList<WeakReference<GLEngine>> mEngines = new ArrayList<WeakReference<GLEngine>>();

	public WallpaperService()
	{
		mEngines.clear();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

	}

	@Override
	public void onDestroy()
	{

		if (renderer != null)
		{
			renderer.release();
			renderer = null;
		}
		mEngines.clear();
		System.gc();
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine()
	{

		GLEngine engine = new GLEngine();
		mEngines.add(new WeakReference<GLEngine>(engine));
		return engine;
	}

	private class GLEngine extends GLWallpaperService.GLSurfaceViewEngine
	{
		private ScheduledExecutorService mTimer;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder)
		{
			super.onCreate(surfaceHolder);
			init(this);
		}

		@Override
		public void onVisibilityChanged(boolean visible)
		{

			if (!visible && getRenderMode() == 0)
			{
				unregisterReceiver();
				stopRendering();
				super.onVisibilityChanged(visible);
				if (renderer != null)
				{
					renderer.release();
				}
				System.gc();
			} else
			{
				registerReceiver();
				checkIFChangeWallpaer();
				super.onVisibilityChanged(visible);
				startRendering();
			}
		}

		private void checkIFChangeWallpaer()
		{
			//mode
			int mode = Util.getIntFromSharedPref(Util.MODE, 1);
			if (mode == 0)
			{
				//not change
			}else if (mode == 1){
				
			}else if (mode == 2)
			{
				long curTime = System.currentTimeMillis();
				long lasttime = Util.getLongFromSharedPref(Util.LAST_UPDATETIME, 0);
				String city = Util.getStringFromSharedPref(Util.CODE, "");
				if (!TextUtils.isEmpty(city) && lasttime + Util.HOUR_1 <curTime )
				{
					if (Util.isNetworkAvailable(getApplicationContext()))
					{
						//todo
						new HTTPTask().execute();
					}
				}
			}
			
		}

		@Override
		public void onDestroy()
		{

			mEngines.remove(this);
			stopRendering();
			if (renderer != null)
			{
				renderer.release();
			}
			super.onDestroy();
		}

		private void startRendering()
		{
			if (mTimer != null)
			{
				return;
			}

			mTimer = Executors.newScheduledThreadPool(1);
			mTimer.scheduleAtFixedRate(rendeRunnable, 0, (long) (1000 / RATE), TimeUnit.MILLISECONDS);
		}

		private boolean stopRendering()
		{
			if (mTimer != null)
			{
				mTimer.shutdownNow();
				mTimer = null;
				return true;
			}
			return false;
		}

		private Runnable rendeRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				requestRender();
			}
		};

	}

	void init(GLEngine engine)
	{

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2)
		{
			engine.setEGLContextClientVersion(2);
			if (renderer == null)
				renderer = new WallPaperRender();
			try
			{
				engine.setRenderer(renderer);
				engine.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
			} catch (Exception e)
			{
			}

		} else
		{
			return;
		}
	}

	private IntentFilter mFilter;

	private void registerReceiver()
	{
		if (mFilter == null)
		{
			mFilter = new IntentFilter();
			mFilter.addAction(ACTION_CHANGE_BROCAST);
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
			if (ACTION_CHANGE_BROCAST.equals(action))
			{
				if (renderer == null)
					return;
				
				int category  = Util.getIntFromSharedPref(Util.REAL_TYPE, WeatherType.FINE);
				category = Util.normalDayOrNight(category);
				renderer.setDirtyScene(category);
			} else if (ACTION_PAUSE_BROCAST.equals(action))
			{
				for (WeakReference<GLEngine> engineREF : mEngines)
				{
					GLEngine engine =  engineREF.get();
					if (engine != null)
					{						
						engine.stopRendering();
					}
				}
				if (renderer != null)
				{
					renderer.release();
				}
			}
		};
	};
	
}
