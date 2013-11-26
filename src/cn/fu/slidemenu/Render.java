package cn.fu.slidemenu;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.fu.slidemenu.model.WeatherType;
import cn.fu.slidemenu.scenes.Background;
import cn.fu.slidemenu.scenes.BaseScene;
import cn.fu.slidemenu.scenes.GLCloud;
import cn.fu.slidemenu.scenes.GLCloudNight;
import cn.fu.slidemenu.scenes.GLFog;
import cn.fu.slidemenu.scenes.GLHalistoneScene;
import cn.fu.slidemenu.scenes.GLHazeScene;
import cn.fu.slidemenu.scenes.GLNAScene;
import cn.fu.slidemenu.scenes.GLOvercast;
import cn.fu.slidemenu.scenes.GLRain;
import cn.fu.slidemenu.scenes.GLSandstormScene;
import cn.fu.slidemenu.scenes.GLShowerRain;
import cn.fu.slidemenu.scenes.GLShowerSnow;
import cn.fu.slidemenu.scenes.GLSnowScene;
import cn.fu.slidemenu.scenes.GLSunny;
import cn.fu.slidemenu.scenes.GLSunnyNight;
import cn.fu.slidemenu.scenes.GLThunder;
import cn.fu.slidemenu.util.MatrixState;
import cn.fu.slidemenu.util.ShaderManager;
import android.R.integer;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
public class Render implements GLSurfaceView.Renderer
{
	private Background bg;
	private BaseScene scene;
	private Background nextBg;
	private BaseScene nextScene;
    private boolean isDirty = false;
    private int sceneCategory = WeatherType.FINE;
    public boolean mHasinit = false;
	private byte lock[] = new byte[0];
	public Render(Context context)
	{
    	isDirty = false;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
//		int category =  getCurrentCategory();
//		
//		boolean reload = category != sceneCategory? true: false;
//		sceneCategory = category;
		boolean reload = true;
		
		MatrixState.setInitStack();
		MatrixState.initStack();
		ShaderManager.clear();
		ShaderManager.getInstance();
		
        GLES20.glClearColor(0.0f,0.0f,0.0f, 1.0f);  
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        int category = 0;
        if (scene != null && scene.getCategory() != category)
        {
        	reload = true;
        }
        // TODO:release old scene
        //fix bug 有时候壁纸没有实时换
        
        if (scene == null || reload )
        {
        	scene =  createScene();
        }
        scene.loadGLTexture(gl);
        scene.GLOBE_ALPHA = 1.0f;
        if (bg == null || reload)
        {
        	bg = new Background(scene.getBackground());
        	//创建纹理矩形对象
        }
        bg.loadGLTexture(gl);
        bg.GLOBE_ALPHA = 1.0f;
        mHasinit = true;       
		
	}
	
	private BaseScene createScene()
	{
		BaseScene newScene = null;
		switch (sceneCategory)
		{
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
			newScene = new GLNAScene();
		default:
			break;
		}
		if (newScene == null)
		{
			newScene = new GLNAScene();
		}
		return newScene;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
    	GLES20.glViewport(0, 0, width, height); 
//        float ratio = (float) width / height;
        MatrixState.setorthoM(-1, 1, -1, 1, 0, 10);
        MatrixState.setCamera(0,0,10,0f,0f,0f,0f,1.0f,0.0f);
        MatrixState.setInitStack();
        System.gc();
		
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		if (isDirty)
		{
			nextScene = createScene();
			nextScene.loadGLTexture(null);
			nextScene.GLOBE_ALPHA = 0.0f;
			int newId = MyApplication.getInstance().getResources().getIdentifier(nextScene.getBackground(), "drawable", MyApplication.getInstance().getPackageName());
			if (newId != bg.resId)
			{
				nextBg = new Background(nextScene.getBackground());
				nextBg.GLOBE_ALPHA = 0.0f;
			}
			isDirty = false;
		}	
		GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
		
		switchScene();
		bg.draw(gl);			
		scene.draw(gl);
		
	}
	private void switchScene()
	{
		if (nextBg == null && nextScene == null)
			return ;
		float diff = 0.045f;
		float min = 0.005f;
		if (nextBg != null)
		{
			bg.GLOBE_ALPHA -= diff;
			if (bg.GLOBE_ALPHA < min)
				bg.GLOBE_ALPHA = min;			
		}
		if (nextScene != null)
		{
			scene.GLOBE_ALPHA -= diff;
			if (scene.GLOBE_ALPHA <min)
				scene.GLOBE_ALPHA = min;			
		}
		
		if (nextBg != null)
		{
			nextBg.draw(null);	
			nextBg.GLOBE_ALPHA += diff;
			if (nextBg.GLOBE_ALPHA > 1.0f)
			{
				nextBg.GLOBE_ALPHA = 1.0f;
				bg.release();
				bg = nextBg;
				nextBg = null;		

			}
		}
		if (scene != null)
		{
			nextScene.draw(null);
			nextScene.GLOBE_ALPHA += diff;
			if (nextScene.GLOBE_ALPHA > 1.0f)
			{
				nextScene.GLOBE_ALPHA = 1.0f;
				scene.release();
				scene = nextScene;
				nextScene = null;
				System.gc();
			}
		}
		
	}
	public void release()
	{
		synchronized (lock)
		{			
			ShaderManager.clear();
			if (bg != null)
			{
				bg.release();
			}
			if (scene != null)
			{
				scene.release();
			}
			nextBg = null;
			nextScene = null;
			System.gc();
		}
	}
	public void setDirtyScene(int k)
	{
		if (mHasinit == false)
		{
			sceneCategory = k;
			return;
		}
		synchronized (lock)
		{			
			if (sceneCategory != k || scene.getCategory() != k)
			{
				sceneCategory = k;
				isDirty = true;			
			}
		}
		
	}

}
