package com.chuanonly.wallpaper3.scenes;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.chuanonly.wallpaper3.R;
import com.chuanonly.wallpaper3.model.Particle;
import com.chuanonly.wallpaper3.model.ParticleDrop;
import com.chuanonly.wallpaper3.model.TextureInfo;
import com.chuanonly.wallpaper3.model.WeatherType;
import com.chuanonly.wallpaper3.util.CacheTextrue;
import com.chuanonly.wallpaper3.util.MatrixState;
import com.chuanonly.wallpaper3.util.OpenGLUtils;

public class GLHalistoneScene extends BaseScene {

	private ArrayList<Integer> heightList;
	private ArrayList<Integer> widthList;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
    
	
	public GLHalistoneScene() {
		setCategory(WeatherType.HAILSTONE);
		init();
	}
	
	private void init() {
		particleList.clear();
		int largeRainNum = 5;
	    int mediumRainNum = 10;
	    int tinyRainNum = 20;
	    int tinyRainNum1 = 30;
	    int largeRainSpeed = 2000;
	    int mediumRainSpeed = 1600;
	    int tinyRainSpeed = 1000;
	    int tinyRainSpeed1 = 800;
	    float rainScaleX = 2.0f;
	    float rainScaleY = 2.5f;
	    if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
	    	largeRainNum = 3;
		    mediumRainNum = 6;
		    tinyRainNum = 15;
		    tinyRainNum1 = 20;
		    largeRainSpeed = 1000;
		    mediumRainSpeed = 600;
		    tinyRainSpeed = 300;
		    tinyRainSpeed1 = 200;
		    rainScaleX = 0.7f;
		    rainScaleY = 1.1f;
		} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
			largeRainNum = 3;
		    mediumRainNum = 6;
		    tinyRainNum = 15;
		    tinyRainNum1 = 20;
		    largeRainSpeed = 1200;
		    mediumRainSpeed = 1000;
		    tinyRainSpeed = 800;
		    tinyRainSpeed1 = 600;
		    rainScaleX = 0.8f;
		    rainScaleY = 1.3f;
		} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
			largeRainNum = 4;
		    mediumRainNum = 8;
		    tinyRainNum = 18;
		    tinyRainNum1 = 25;
		    largeRainSpeed = 1800;
		    mediumRainSpeed = 1200;
		    tinyRainSpeed = 900;
		    tinyRainSpeed1 = 700;
		    rainScaleX = 1.1f;
		    rainScaleY = 1.5f;
		}
	    Bitmap bitmap5 = OpenGLUtils.decodeResource(R.drawable.hali_5, rainScaleX, rainScaleY, 0);
 		int largeW = bitmap5.getWidth();
 		int largeH = bitmap5.getHeight();
 		bitmap5.recycle();
 		
 		Bitmap bitmap4 = OpenGLUtils.decodeResource(R.drawable.hali_4, rainScaleX, rainScaleY, 0);
 		int mediumW = bitmap4.getWidth();
 		int mediumH = bitmap4.getHeight();
 		bitmap4.recycle();
 		
 		Bitmap bitmap3 = OpenGLUtils.decodeResource(R.drawable.hali_3, 1.0f, 1.0f, 0);
 		int tinyW = bitmap3.getWidth();
 		int tinyH = bitmap3.getHeight();
 		bitmap3.recycle();
 		
 		Bitmap bitmap2 = OpenGLUtils.decodeResource(R.drawable.hali_2, 1.0f, 1.0f, 0);
 		int tinyW1 = bitmap2.getWidth();
 		int tinyH1 = bitmap2.getHeight();
 		bitmap2.recycle();
 		
 		Bitmap bitmap1 = OpenGLUtils.decodeResource(R.drawable.hali_1, 1.0f, 1.0f, 0);
 		int tinyW2 = bitmap1.getWidth();
 		int tinyH2 = bitmap1.getHeight();
 		bitmap1.recycle();
 		
 		int largeSW = mScreenWidth - largeW;
 		int largeSH = mScreenHeight - largeH;
 		int mediumSW = mScreenWidth - mediumW;
 		int mediumSH = mScreenHeight - mediumH;
 		int tinySW = mScreenWidth - tinyW;
 		int tinySH = mScreenHeight - tinyH;
 		int tinySW1 = mScreenWidth - tinyW1;
 		int tinySH1 = mScreenHeight - tinyH1;
 		int tinySW2 = mScreenWidth - tinyW2;
 		int tinySH2 = mScreenHeight - tinyH2;
 		
	    for (int largeIndex =0; largeIndex < largeRainNum; largeIndex++)
		{
			Particle p = new ParticleDrop();
			int pos[] = getRandXY(largeW, largeH, largeSW, largeSH);
			p.setXY(pos[0], pos[1]);
			p.setScaleX(rainScaleX);
			p.setScaleY(rainScaleY);
			p.setAngle(0);
			p.setSpeed(largeRainSpeed);
			p.setType(0);
			particleList.add(p);	
		}
		
		for (int mediumIndex =0; mediumIndex < mediumRainNum; mediumIndex++)
		{
			Particle p = new ParticleDrop();
//			int pos[] = getRandXY(mediumW, mediumH, mediumSW, mediumSH);
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(rainScaleX);
			p.setScaleY(rainScaleY);
			p.setAngle(0);
			p.setSpeed(mediumRainSpeed);
			p.setType(1);
			particleList.add(p);		
		}
		
		for (int tinyIndex =0; tinyIndex < tinyRainNum; tinyIndex++)
		{
			Particle p = new ParticleDrop();
//			int pos[] = getRandXY(tinyW, tinyH, tinySW, tinySH);
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(1.0f);
			p.setAngle(0);
			p.setSpeed(tinyRainSpeed);
			p.setType(2);
			particleList.add(p);		
		}
		for (int tinyIndex1 =0; tinyIndex1 < tinyRainNum1; tinyIndex1++)
		{
			Particle p = new ParticleDrop();
//			int pos[] = getRandXY(tinyW1, tinyH1, tinySW1, tinySH1);
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(1.0f);
			p.setAngle(0);
			p.setSpeed(tinyRainSpeed1);
			p.setType(3);
			particleList.add(p);		
		}
		
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.hali_5,rainScaleX,rainScaleY));
		textureInfos.add(new TextureInfo(R.drawable.hali_4,rainScaleX,rainScaleY));
		textureInfos.add(new TextureInfo(R.drawable.hali_3));
		textureInfos.add(new TextureInfo(R.drawable.hali_1));
	}
	
	@Override
	public int loadGLTexture(GL10 gl) {

		for (TextureInfo ti : textureInfos)
		{		
			int value = CacheTextrue.get(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle);
			if (isNeedCaChe && value >0 )
			{
				ti.textureId = value;
				ti.width = CacheTextrue.getWidth(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle);
				ti.height = CacheTextrue.getHeight(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle);
			}else {			
				Bitmap bitmap = OpenGLUtils.decodeResource(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle);
				ti.textureId = OpenGLUtils.loadTexture(bitmap);
				ti.width = bitmap.getWidth();
				ti.height = bitmap.getHeight();
				if (isNeedCaChe)
				{					
					CacheTextrue.put(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle, ti.textureId,(int) ti.width,(int)ti.height);
				}
				bitmap.recycle();
			}		
		}
		
		for (Particle p : particleList) {
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
			{				
				p.setBmParams(ti.width *p.getScaleX()/ti.scaleX , ti.height*p.getScaleY()/ti.scaleY , true);
			}
		}
		
		isLoadBitmap = true;
		isFirst = false;
		return 0;
	}

	@Override
	public void draw(GL10 gl) {
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			isLoadBitmap = true;
		}
		GLES20.glUseProgram(mProgram);

//		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 3, GLES20.GL_FLOAT, false, texture.length, mTexCoorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		for (int i=0; i<particleList.size(); i++)
		{
			Particle p = particleList.get(i);
			float offset = 0;
			
			if (p.getDrawTime() != 0)
			{
				offset = (System.currentTimeMillis()-p.getDrawTime()) /1000.0f;
				if (System.currentTimeMillis()-p.getDrawTime() > 100)
				{
					offset = 0;
				}
			}	
			p.setDrawTime(System.currentTimeMillis());
			
			if (!isStatic)
			{
				p.move(offset*p.getSpeed());				
			}
			
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());

			MatrixState.pushMatrix();			
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth() / UNIT, p.getHeight() / UNIT, 1);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();

		}

	}

	
	public int[] getRandXY(int width, int height, int screenW, int screenH)
	{
		int pos[] = new int[2];
		if (this.widthList == null)
			this.widthList = new ArrayList<Integer>();
		if (this.widthList.size() == 0) {
			for (int i = width; i < screenW + 1; i++)
				this.widthList.add(Integer.valueOf(i));
		}
		if (this.heightList == null)
			this.heightList = new ArrayList<Integer>();
		if (this.heightList.size() == 0) {
			for (int i = height; i < screenH + 1; i++)
				this.heightList.add(Integer.valueOf(i));
		}
		
		int i = this.heightList.size();
		int j = this.widthList.size();
		int xRandom = (int) (Math.random() * j);
		int yRandom = (int) (Math.random() * i);
		pos[0] = ((Integer) this.widthList.get(xRandom)).intValue();
		pos[1] = ((Integer) this.heightList.get(yRandom)).intValue();
		this.widthList.remove(xRandom);
		this.heightList.remove(yRandom);
		return pos;
	}
	public int[] getRandXY()
	{
		int pos[] = new int[2];
		if (this.widthList == null)
			this.widthList = new ArrayList<Integer>();
		if (this.widthList.size() == 0) {
			for (int i = 0; i < mScreenWidth + 1; i++)
				this.widthList.add(Integer.valueOf(i));
		}
		if (this.heightList == null)
			this.heightList = new ArrayList<Integer>();
		if (this.heightList.size() == 0) {
			for (int i = 0; i < mScreenHeight + 1; i++)
				this.heightList.add(Integer.valueOf(i));
		}
		
		int i = this.heightList.size();
		int j = this.widthList.size();
		int xRandom = (int) (Math.random() * j);
		int yRandom = (int) (Math.random() * i);
		pos[0] = ((Integer) this.widthList.get(xRandom)).intValue();
		pos[1] = ((Integer) this.heightList.get(yRandom)).intValue();
		this.widthList.remove(xRandom);
		this.heightList.remove(yRandom);
		return pos;
	}
	
	@Override
	public String getBackground() {
		return "bg_snow";
	}

}
