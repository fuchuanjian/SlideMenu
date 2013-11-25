package cn.fu.slidemenu.scenes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import cn.fu.slidemenu.model.TextureInfo;
import cn.fu.slidemenu.util.OpenGLUtils;
import cn.fu.slidemenu.util.ShaderManager;
import android.R.integer;
import android.os.Build;

public abstract class BaseScene
{ 
	protected ArrayList<TextureInfo> textureInfos = new ArrayList<TextureInfo>();
	public float GLOBE_ALPHA = 1.0f;
	public  int mProgram;
	public  int muMVPMatrixHandle;
	public  int maPositionHandle; 
	public  int maTexCoorHandle; 
	public  int mAlphaHandle;
    public String mVertexShader; 	 
    public String mFragmentShader; 
	protected int mScreenWidth = 0;
	protected int mScreenHeight = 0;
	protected float mDensity = 0;
	protected boolean mIsCanUseCustomBg = false; 
	
    protected boolean isFirst = true;
    protected boolean isLoadBitmap = false;
    protected boolean isNeedCaChe = true;
	protected boolean isStatic  = false;  
	
	protected FloatBuffer   mVertexBuffer;
	protected FloatBuffer   mTexCoorBuffer;
	protected ShortBuffer   mIndicesBuffer;
	protected float UNIT = 1.0f;
	protected int vCount=6;  
	protected float itemVertices[] = 
					{0.0f, -2.0f, 0.0f,
					2.0f, -2.0f, 0.0f,
					-0.0f,0.0f, 0.0f,
					2.0f, 0.0f, 0.0f};
	protected float texture[] = {
  			0.0f, 1.0f, 0.0f,
  			1.0f, 1.0f, 0.0f,
  			0.0f, 0.0f, 0.0f,
  			1.0f, 0.0f, 0.0f};
	protected short indices[] = {0, 1, 2, 2, 1, 3};
	
	public abstract int loadGLTexture(GL10 gl);
	public abstract void draw(GL10 gl);
	public BaseScene()
	{
		mScreenWidth = ShaderManager.getInstance().getScreenWidth();
		mScreenHeight = ShaderManager.getInstance().getScreenHeight();
		mDensity = ShaderManager.getInstance().getDensity();
		initShader();
		initVertexData();
	}
	// 初始化shader
	protected synchronized void initShader()
	{
		ShaderManager sm = ShaderManager.getInstance();
		mProgram = sm.getProgram();
		maPositionHandle =sm.getPositionHandle();
		maTexCoorHandle = sm.getTexCoorHandle();
		muMVPMatrixHandle = sm.getMatrixHandle();
		//
		mAlphaHandle = sm.getAlphaHandle();
	}
	
	private void initVertexData()
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(itemVertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); 
		mVertexBuffer = vbb.asFloatBuffer(); 
		for (int j = 0; j < itemVertices.length; j++)
		{
			if (j%3 == 0)
			{
				mVertexBuffer.put(itemVertices[j]* ((float)UNIT/mScreenWidth));					
			}else if (j%3 == 1) {
				mVertexBuffer.put(itemVertices[j]* ((float)UNIT/mScreenHeight));					
			}else if (j%3 ==2) {
				mVertexBuffer.put(0.0f);					
			}
		}			
		mVertexBuffer.position(0); 
		ByteBuffer cbb = ByteBuffer.allocateDirect(texture.length * 4);
		cbb.order(ByteOrder.nativeOrder()); 
		mTexCoorBuffer = cbb.asFloatBuffer(); 
		mTexCoorBuffer.put(texture); 
		mTexCoorBuffer.position(0); 
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(indices.length * 2);
		byteBuf.order(ByteOrder.nativeOrder());
		mIndicesBuffer = byteBuf.asShortBuffer();
		mIndicesBuffer.put(indices);
		mIndicesBuffer.position(0);
	}
	
	public abstract String getBackground();
	
	public void release()
	{
//		OpenGLUtils.deleteTexture(textureInfos);
		isLoadBitmap = false;
	}
	
	
	public int getCategory()
	{
		// TODO Auto-generated method stub
		return category;
	}
	public void setCategory(int n)
	{
		category = n;
	}
	private int category = 0;


	public void setCaChe(boolean b)
	{
		isNeedCaChe = b;		
	}
	public void destroy()
	{
		OpenGLUtils.deleteTexture(textureInfos);
		
	}
	public void setCanUseCostom(boolean isNeedCustom)
	{
		mIsCanUseCustomBg = isNeedCustom;   
	}
	public void setIsStatic(boolean isStatic)
	{
		this.isStatic = isStatic;
		
	}
	
}
