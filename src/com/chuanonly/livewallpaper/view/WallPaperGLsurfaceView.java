package com.chuanonly.livewallpaper.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.chuanonly.livewallpaper.service.WallPaperRender;
import com.chuanonly.livewallpaper.util.CacheTextrue;

import android.content.Context;
import android.opengl.GLSurfaceView;
public class WallPaperGLsurfaceView extends GLSurfaceView
{
	
	private final int RATE = 60;
	private WallPaperRender mRender;
	
	public WallPaperGLsurfaceView(Context context)
	{
		super(context);
		init(context);
	}
	
	private void init(Context context)
	{
		setEGLContextClientVersion(2);
		setEGLConfigChooser(false);
		mRender = new WallPaperRender();
		setRenderer(mRender);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); //必须在setRenderer之后
		
	}
	
	public void onResume()
	{
		startRendering();
		super.onResume();
	}

	public void onPause()
	{
		if (mRender != null)
		{
			mRender.release();
		}
		CacheTextrue.clear();
		stopRendering();
		super.onPause();
	}
	
	private ScheduledExecutorService mTimer;
	public void startRendering()
	{
		if ( mTimer != null) {return;}
		
		mTimer = Executors.newScheduledThreadPool(1);
		mTimer.scheduleAtFixedRate(renderTask, 0, (long)(1000 / RATE) , TimeUnit.MILLISECONDS);
	}
	
	public void stopRendering()
	{
		if (mTimer != null)
		{
			mTimer.shutdownNow();
			mTimer = null;
		}
	}
	
	private Runnable renderTask = new Runnable()
	{
		@Override
		public void run()
		{
			requestRender();
			
		}
	};
	public void setDirty(int index)
	{
		mRender.setDirtyScene(index);
	}
}
