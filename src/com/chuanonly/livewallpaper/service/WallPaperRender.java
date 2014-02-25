
package com.chuanonly.livewallpaper.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.scenes.Background;
import com.chuanonly.livewallpaper.scenes.BaseScene;
import com.chuanonly.livewallpaper.scenes.GLCloud;
import com.chuanonly.livewallpaper.scenes.GLCloudNight;
import com.chuanonly.livewallpaper.scenes.GLFog;
import com.chuanonly.livewallpaper.scenes.GLHalistoneScene;
import com.chuanonly.livewallpaper.scenes.GLHazeScene;
import com.chuanonly.livewallpaper.scenes.GLNAScene;
import com.chuanonly.livewallpaper.scenes.GLOvercast;
import com.chuanonly.livewallpaper.scenes.GLRain;
import com.chuanonly.livewallpaper.scenes.GLSandstormScene;
import com.chuanonly.livewallpaper.scenes.GLShowerRain;
import com.chuanonly.livewallpaper.scenes.GLShowerSnow;
import com.chuanonly.livewallpaper.scenes.GLSnowScene;
import com.chuanonly.livewallpaper.scenes.GLSunny;
import com.chuanonly.livewallpaper.scenes.GLSunnyNight;
import com.chuanonly.livewallpaper.scenes.GLThunder;
import com.chuanonly.livewallpaper.util.MatrixState;
import com.chuanonly.livewallpaper.util.ShaderManager;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.Util;

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
        int category = Util.getCurrentCategory(Util.getIntFromSharedPref(Util.SCENE_TYPE, WeatherType.NA_SCENE));
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
        if (sceneCategory != k) {
            sceneCategory = k;
            isDirty = true;
        }
        if (bg == null || scene == null) {
            isDirty = true;
        } else if (scene != null && nextScene == null && scene.getCategory() != k) {
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
                newScene = new GLSandstormScene(WeatherType.SAND_STORM);
                break;
            case WeatherType.DUST_FLOATING:
                newScene = new GLSandstormScene(WeatherType.DUST_FLOATING);
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
