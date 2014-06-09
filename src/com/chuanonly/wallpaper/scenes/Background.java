package com.chuanonly.wallpaper.scenes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.chuanonly.wallpaper.MyApplication;
import com.chuanonly.wallpaper.R;
import com.chuanonly.wallpaper.util.CacheTextrue;
import com.chuanonly.wallpaper.util.MatrixState;
import com.chuanonly.wallpaper.util.OpenGLUtils;
import com.chuanonly.wallpaper.util.ShaderManager;

public class Background {

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
	protected float itemVertices[] = 
					{0.0f, -2.0f, 0.0f,
					2.0f, -2.0f, 0.0f,
					-0.0f,0.0f, 0.0f,
					2.0f, 0.0f, 0.0f};
	
	FloatBuffer   mVertexBuffer;
	FloatBuffer   mTexCoorBuffer;
	ShortBuffer mIndicesBuffer;
    int vCount=6;   
	private int textureBgId = 0;;
	public int resId = -1;

	private String mBgStr;   
	private boolean mIsCanUseCustomBg = false; 
	private boolean isLoadBitmap = false;
	private boolean isNeedCache = true;

	private float vertices[] = {
			-1.0f, -1.0f, 0.0f, // Bottom Left
			1.0f,  -1.0f, 0.0f, // Bottom Right
			-1.0f,  1.0f, 0.0f, // Top Left
			1.0f,   1.0f, 0.0f  // Top Right
	};

	private float texture[] = {
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f};

	private short indices[] = {0, 1, 2, 2, 1, 3};


	public Background(String bgStr)
	{
		initBg(bgStr);
	}
	public Background(String bgStr , boolean isNeedCustom)
	{
		mIsCanUseCustomBg = isNeedCustom;  
		initBg(bgStr);
	}

	private void initBg(String bgStr)
	{
		mBgStr = bgStr;
		mScreenWidth = ShaderManager.getInstance().getScreenWidth();
		mScreenHeight = ShaderManager.getInstance().getScreenHeight();
		mDensity = ShaderManager.getInstance().getDensity();
		initShader();
		
		resId = MyApplication.getContext().getResources().getIdentifier(bgStr, "drawable", MyApplication.getContext().getPackageName());
		initVertexData();
		
	}
	protected synchronized void initShader() {
		ShaderManager sm = ShaderManager.getInstance();
		mProgram = sm.getProgram();
		maPositionHandle = sm.getPositionHandle();
		maTexCoorHandle = sm.getTexCoorHandle();
		muMVPMatrixHandle = sm.getMatrixHandle();
		mAlphaHandle = sm.getAlphaHandle();
	}
		
	@SuppressLint("NewApi")
	public void draw(GL10 gl)
	{
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
		}

		GLES20.glUseProgram(mProgram);

		GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, vertices.length, mVertexBuffer);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 3, GLES20.GL_FLOAT, false, texture.length, mTexCoorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureBgId);
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 1);
//		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
//		GLES20.glDeleteProgram(mProgram);
		MatrixState.popMatrix();

	}
	public int loadGLTexture(GL10 gl)
	{
		Bitmap bitmap = null;
		
		if (mIsCanUseCustomBg == false )
		{		
			if (resId <= 0)
			{
				resId = R.drawable.bg_fine_day;			
				return -1;					
			}
			if (isNeedCache)
			{			
				int value = CacheTextrue.get(resId);
				
				if (value >0 )
				{
					textureBgId = value;
					isLoadBitmap = true;
					return textureBgId;
				}
			}
			
			
			bitmap = OpenGLUtils.decodeResource(resId);
			
			
			
			textureBgId = OpenGLUtils.loadTexture(bitmap);
			if (isNeedCache)
			{					
				CacheTextrue.put(resId, textureBgId);
			}
			bitmap.recycle();
			bitmap = null;
			
			isLoadBitmap = true;
			return textureBgId;
		}
		return textureBgId;
	}
	public void setDirty(String str)
	{
		mBgStr = str;
		int newId = MyApplication.getContext().getResources().getIdentifier(str, "drawable", MyApplication.getContext().getPackageName());
		if (resId != newId)
		{
			resId = newId;
			isLoadBitmap = false;
		}
	}

	public void initVertexData()
	{

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(texture.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer = cbb.asFloatBuffer();
		mTexCoorBuffer.put(texture);
		mTexCoorBuffer.position(0);

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(indices.length *2);
		 byteBuf.order(ByteOrder.nativeOrder());
		 mIndicesBuffer = byteBuf.asShortBuffer();
		 mIndicesBuffer.put(indices);
		 mIndicesBuffer.position(0);
	}

	public void release()
	{
		isLoadBitmap = false;
		
	}

	public String getBgStr()
	{
		if (mBgStr == null || mBgStr.equals(""))
			return "bg_fine_day";
		return mBgStr;
	}

	public void setCaChe(boolean b)
	{
		isNeedCache = b;
		
	}
	public void setCurrentSceneDefaultBackground()
	{
		isLoadBitmap = false;
	}
	@SuppressLint("NewApi")
	public void destroy()
	{
		if (textureBgId != 0)
		{
			GLES20.glDeleteTextures(0, new int[]{textureBgId}, 0);
		}		
	}

}
