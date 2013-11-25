package cn.fu.slidemenu.util;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import cn.fu.slidemenu.MyApplication;

@SuppressLint("NewApi")
public class ShaderManager
{
	public static ShaderManager instance = null;
	
	
	public  int mProgram; 
	public  int muMVPMatrixHandle;
	public  int maPositionHandle;   
	public  int maTexCoorHandle; 
	public  int mAlphaHandle;
    public String mVertexShader;  	 
    public String mFragmentShader;
	protected int screenWidth = 0;
	protected int screenHeight = 0;
	protected float density = 0;
	private boolean isLowVersion = false;
	
	
	private ShaderManager(){}
	
	public synchronized static ShaderManager getInstance()
	{
		if (instance == null)
		{
			instance = new ShaderManager();
			instance.init();
		}
		return instance;
	}

	private void init()
	{
		screenWidth = MyApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
		screenHeight = MyApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
		density = MyApplication.getInstance().getResources().getDisplayMetrics().density;
		initShader();	
	}
		private synchronized void initShader()
		{
			mVertexShader = getVertexShaderStr();
			mFragmentShader = getFragmentShaderStr();
			mProgram = 0;
			int tryCnt = 0;
			while (mProgram == 0 && tryCnt < 10)
			{				
				tryCnt++;
				mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
			}
			// 获取程序中顶点位置属性引用id
			maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
			// 获取程序中顶点纹理坐标属性引用id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// 获取程序中总变换矩阵引用id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			//
			mAlphaHandle = GLES20.glGetUniformLocation(mProgram, "alpha");
		}
		private String getFragmentShaderStr()
		{
			final String perPixelFragmentShader =
					
			"precision mediump float;           \n"+
			"varying vec2 vTextureCoord;         \n"+
			"uniform sampler2D sTexture;         \n"+
			"uniform float alpha;               \n"+
			"void main()                       \n"+                         
			"{                                 \n"+
			"	vec4 finalColor = texture2D(sTexture, vTextureCoord); \n"+
			"	finalColor = finalColor * alpha;                                 \n"+
			"    gl_FragColor = finalColor;                             \n"+
			"}                                                          \n";         
			
			return perPixelFragmentShader;
		}	
		
		private String getVertexShaderStr()
		{
			final String vertexShaderStr =
			"uniform mat4 uMVPMatrix;  \n"+
			"attribute vec3 aPosition;  \n"+
			"attribute vec2 aTexCoor;   \n"+
			"varying vec2 vTextureCoord;  \n"+
			"void main()    \n"+ 
			"{  \n"+                            		
			"   gl_Position = uMVPMatrix * vec4(aPosition,1); \n"+
			"   vTextureCoord = aTexCoor;  \n"+
			"}   \n";                   
			return vertexShaderStr;
		}
		public int getProgram()
		{
			return mProgram;
		}

		public int getMatrixHandle()
		{
			return muMVPMatrixHandle;
		}

		public int getPositionHandle()
		{
			return maPositionHandle;
		}

		public int getTexCoorHandle()
		{
			return maTexCoorHandle;
		}

		public int getAlphaHandle()
		{
			return mAlphaHandle;
		}

		public String getVertexShader()
		{
			return mVertexShader;
		}

		public String getFragmentShader()
		{
			return mFragmentShader;
		}

		public int getScreenWidth()
		{
			return screenWidth;
		}

		public int getScreenHeight()
		{
			return screenHeight;
		}
		public float getDensity()
		{
			return density;
		}

		public boolean isLowVersion()
		{
			return isLowVersion;
		}

		public static void clear()
		{
			instance = null;
			
		}
}
