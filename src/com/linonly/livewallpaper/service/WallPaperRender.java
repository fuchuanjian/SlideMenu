
package com.linonly.livewallpaper.service;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.linonly.livewallpaper.MainHomeActivity;
import com.linonly.livewallpaper.MyApplication;
import com.linonly.livewallpaper.model.WeatherType;
import com.linonly.livewallpaper.scenes.Background;
import com.linonly.livewallpaper.scenes.BaseScene;
import com.linonly.livewallpaper.scenes.GLCloud;
import com.linonly.livewallpaper.scenes.GLCloudNight;
import com.linonly.livewallpaper.scenes.GLFog;
import com.linonly.livewallpaper.scenes.GLHalistoneScene;
import com.linonly.livewallpaper.scenes.GLHazeScene;
import com.linonly.livewallpaper.scenes.GLOvercast;
import com.linonly.livewallpaper.scenes.GLRain;
import com.linonly.livewallpaper.scenes.GLShowerRain;
import com.linonly.livewallpaper.scenes.GLShowerSnow;
import com.linonly.livewallpaper.scenes.GLSnowScene;
import com.linonly.livewallpaper.scenes.GLSunny;
import com.linonly.livewallpaper.scenes.GLSunnyNight;
import com.linonly.livewallpaper.scenes.GLThunder;
import com.linonly.livewallpaper.util.MatrixState;
import com.linonly.livewallpaper.util.ShaderManager;
import com.linonly.livewallpaper.util.Util;

/**
 * 所有的渲染工作都在这里写
 */
public class WallPaperRender implements GLSurfaceView.Renderer {
    private GLSurfaceView mSurfaceView; // The rendering surface

    private Background bg;

    private BaseScene scene;

    private Background nextBg;

    private BaseScene nextScene;

    private boolean isDirty = false;

    private int sceneCategory = WeatherType.NA_SCENE;

    public boolean mHasinit = false;
    
    public WallPaperRender() {
        // 这里最好是空，让初始化工作在onSurfaceCreated 里面做
        isDirty = false;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	int category = Util.getIntFromSharedPref(Util.TYPE, WeatherType.FINE);
    	int mode = Util.getIntFromSharedPref(Util.MODE, 1);
    	long lastPickTime = Util.getLongFromSharedPref(Util.LAST_PICK_TIME, 0);
    	
    	if (mode == 0)
    	{
    	}else if (mode == 1){	
    		
			long curTime = System.currentTimeMillis();
			long lasttime = Util.getLongFromSharedPref(Util.LAST_UPDATETIME, 0);
			if (lasttime + Util.HOUR_1 < curTime)
			{
				int index = new Random().nextInt(MainHomeActivity.imgType.length);
				if(index == 10) index = 2; //雪
				if(index == 11) index = 3; //雨
				category =  MainHomeActivity.imgType[index][new Random().nextInt(MainHomeActivity.imgType[index].length)];
			}
			
			if (lastPickTime + Util.HOUR_HALF > System.currentTimeMillis())
			{
			}else {
				category = Util.normalDayOrNight(category);
			}
			Util.setLongToSharedPref(Util.LAST_UPDATETIME, System.currentTimeMillis());
			Util.setIntToSharedPref(Util.TYPE, category);
    		
    	}else if (mode == 2) {	
    		int type = Util.getIntFromSharedPref(Util.REAL_TYPE, -1);
    		if (type == -1)
    		{
    			category = Util.getIntFromSharedPref(Util.TYPE, WeatherType.FINE);
    		}else {				
    			category = Util.getIntFromSharedPref(Util.REAL_TYPE, WeatherType.FINE);
			}
    		category = Util.normalDayOrNight(category);
		}
    	
  
        boolean reload = category != sceneCategory ? true : false;
        sceneCategory = category;

        MatrixState.setInitStack();
        MatrixState.initStack();
        ShaderManager.clear();
        ShaderManager.getInstance();
        // 设置屏幕背景色RGBA
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // 关闭背面剪裁
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        // gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        if (scene != null && scene.getCategory() != category) {
            reload = true;
        }

        if (scene == null || reload) {
            scene = createScene();
            scene.setCaChe(false);
        }
        scene.loadGLTexture(gl);
        scene.GLOBE_ALPHA = 1.0f;
        if (bg == null || reload) {
            bg = new Background(scene.getBackground());
            bg.setCaChe(false);
            // 创建纹理矩形对象
        }
        bg.loadGLTexture(gl);
        bg.GLOBE_ALPHA = 1.0f;
        mHasinit = true;

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        if (isDirty) {
            nextScene = createScene();
            nextScene.setCaChe(false);
            nextScene.loadGLTexture(null);
            nextScene.GLOBE_ALPHA = 0.0f;
            int newId = MyApplication.getContext().getResources().getIdentifier(nextScene.getBackground()
            		, "drawable", MyApplication.getContext().getPackageName());
            if (newId != bg.resId) {
                nextBg = new Background(nextScene.getBackground());
                nextBg.setCaChe(false);
                nextBg.GLOBE_ALPHA = 0.0f;
            }
            isDirty = false;
        }
        // 清除深度缓冲与颜色缓冲
        // GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT |
        // GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        switchScene();
        bg.draw(gl);
        scene.draw(gl);

    }

    private void switchScene() {
        if (nextBg == null && nextScene == null)
            return;
        // float diff = 0.01f;
        float diff = 0.045f;
        float min = 0.005f;
        if (nextBg != null) {
            bg.GLOBE_ALPHA -= diff;
            if (bg.GLOBE_ALPHA < min)
                bg.GLOBE_ALPHA = min;
        }
        if (nextScene != null) {
            scene.GLOBE_ALPHA -= diff;
            if (scene.GLOBE_ALPHA < min)
                scene.GLOBE_ALPHA = min;
        }

        if (nextBg != null) {
            nextBg.draw(null);
            nextBg.GLOBE_ALPHA += diff;
            if (nextBg.GLOBE_ALPHA > 1.0f) {
                nextBg.GLOBE_ALPHA = 1.0f;
                bg.release();
                bg.destroy();
                bg = nextBg;
                nextBg = null;

            }
        }
        if (scene != null) {
            nextScene.draw(null);
            nextScene.GLOBE_ALPHA += diff;
            if (nextScene.GLOBE_ALPHA > 1.0f) {
                nextScene.GLOBE_ALPHA = 1.0f;
                scene.release();
                scene.destroy();
                scene = nextScene;
                nextScene = null;
                gc();
            }
        }

    }

    public void setDirtyScene(int k) {
    	int category = k;
    	
        if (sceneCategory != category) {
            sceneCategory = category;
            isDirty = true;
        }
        if (bg == null || scene == null) {
            isDirty = true;
        } else if (scene != null && nextScene == null && scene.getCategory() != category) {
            isDirty = true;
        }

    }

    public Background getbBackground() {
        return bg;
    }

    private BaseScene createScene() {
        BaseScene newScene = null;
        switch (sceneCategory) {
            case WeatherType.CLOUDY:
                newScene = new GLCloud();
                break;
            case WeatherType.CLOUDY_NIGHT:
                newScene = new GLCloudNight();
                break;
            case WeatherType.FINE:
                newScene = new GLSunny();
                break;
            case WeatherType.FINE_NIGHT:
                newScene = new GLSunnyNight();
                break;
            case WeatherType.FOG:
                newScene = new GLFog();
                break;
            case WeatherType.HAZE:
                newScene = new GLHazeScene();
                break;
            case WeatherType.OVERCAST:
                newScene = new GLOvercast();
                break;
            case WeatherType.RAINY_LIGHT:
                newScene = new GLRain(WeatherType.RAINY_LIGHT);
                break;
            case WeatherType.RAINY_MODERATE:
                newScene = new GLRain(WeatherType.RAINY_MODERATE);
                break;
            case WeatherType.RAINY_HEAVY:
                newScene = new GLRain(WeatherType.RAINY_HEAVY);
                break;
            case WeatherType.RAINY_STORM:
                newScene = new GLRain(WeatherType.RAINY_STORM);
                break;
            case WeatherType.SAND_STORM:
//                newScene = new GLSandstormScene(WeatherType.SAND_STORM);
            	 newScene = new GLFog();
                break;
            case WeatherType.DUST_FLOATING:
//                newScene = new GLSandstormScene(WeatherType.DUST_FLOATING);
            	 newScene = new GLFog();
                break;
            case WeatherType.SNOW_LIGHT:
                newScene = new GLSnowScene(WeatherType.SNOW_LIGHT);
                break;
            case WeatherType.SNOW_MODERATE:
                newScene = new GLSnowScene(WeatherType.SNOW_MODERATE);
                break;
            case WeatherType.SNOW_HEAVY:
                newScene = new GLSnowScene(WeatherType.SNOW_HEAVY);
                break;
            case WeatherType.SNOW_STORM:
                newScene = new GLSnowScene(WeatherType.SNOW_STORM);
                break;
            case WeatherType.SHOWER_THUNDER:
                newScene = new GLThunder();
                break;
            case WeatherType.SHOWER:
                newScene = new GLShowerRain();
                break;
            case WeatherType.SNOW_SHOWER:
                newScene = new GLShowerSnow();
                break;
            case WeatherType.HAILSTONE:
                newScene = new GLHalistoneScene();
                break;
            case WeatherType.NA_SCENE:
                newScene = new GLSunny();
            default:
                break;
        }
        if (newScene == null) {
            newScene = new GLSunny();
        }
        return newScene;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        MatrixState.setorthoM(-1, 1, -1, 1, 0, 10);
        MatrixState.setCamera(0, 0, 10, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        MatrixState.setInitStack();
        gc();
    }

    public GLSurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    public void setSurfaceView(GLSurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }

    public void release() {
        ShaderManager.clear();
        if (bg != null) {
            bg.destroy();
            bg.release();
        }
        if (scene != null) {
            scene.destroy();
            scene.release();
        }
        nextBg = null;
        nextScene = null;
        gc();
    }

    public void gc() {
        System.gc();
    }

}
