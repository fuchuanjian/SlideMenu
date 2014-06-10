package com.chuanonly.wallpaper2.scenes;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.chuanonly.wallpaper2.R;
import com.chuanonly.wallpaper2.model.Particle;
import com.chuanonly.wallpaper2.model.ParticleRect;
import com.chuanonly.wallpaper2.model.TextureInfo;
import com.chuanonly.wallpaper2.model.WeatherType;
import com.chuanonly.wallpaper2.util.CacheTextrue;
import com.chuanonly.wallpaper2.util.MatrixState;
import com.chuanonly.wallpaper2.util.OpenGLUtils;

public class GLSandstormScene extends BaseScene {

	
	private ArrayList<Integer> heightList;
	private ArrayList<Integer> widthList;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
    
	public GLSandstormScene(int type) {
		setCategory(type);
		init(type);
	}
	
	private void init(int type) {
		int largeSandNum = 50;
		int mediumSandNum = 250;
		int tinySandNum = 550;
		float largeSandScale = 0.8f;
		float mediumSandScale = 0.9f;
		float tinySandScale = 0.6f;
		int sandDiscreteNum = 20;
		int largeSandSpeed = 60;
		int mediumSandSpeed = 50;
		int tinySandSpeed = 40;
		float cloudyScale = 8.0f;

		if ((mScreenWidth > 0) && (mScreenWidth <= 450)) {
			largeSandNum = 50;
			mediumSandNum = 200;
			tinySandNum = 400;
			largeSandSpeed = 40;
			mediumSandSpeed = 30;
			tinySandSpeed = 20;
			largeSandScale = 0.4f;
			mediumSandScale = 0.45f;
			tinySandScale = 0.3f;
			sandDiscreteNum = 10;
		} else if ((450 < mScreenWidth) && (mScreenWidth <= 650)) {
			largeSandNum = 50;
			mediumSandNum = 250;
			tinySandNum = 500;
			largeSandSpeed = 50;
			mediumSandSpeed = 40;
			tinySandSpeed = 30;
			largeSandScale = 0.53f;
			mediumSandScale = 0.6f;
			tinySandScale = 0.4f;
			sandDiscreteNum = 15;
		} else if ((650 < mScreenWidth) && (mScreenWidth <= 850)) {
			largeSandNum = 50;
			mediumSandNum = 250;
			tinySandNum = 550;
			largeSandSpeed = 60;
			mediumSandSpeed = 50;
			tinySandSpeed = 40;
			largeSandScale = 0.8f;
			mediumSandScale = 0.9f;
			tinySandScale = 0.6f;
			sandDiscreteNum = 20;
		}
		if ((mDensity > 0.0F) && (mDensity <= 1.0F)) {
			cloudyScale = 3.0f;
		} else if ((mDensity > 1.0F) && (mDensity <= 1.5F)) {
			cloudyScale = 4.0f;
		}
		
		largeSandSpeed = largeSandSpeed + produceRandom(sandDiscreteNum);
		mediumSandSpeed = mediumSandSpeed + produceRandom(sandDiscreteNum);
		tinySandSpeed = tinySandSpeed + produceRandom(sandDiscreteNum);
		
		particleList.clear();
			
 		
 		for (int largeIndex = 0; largeIndex < largeSandNum; largeIndex++) {
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(largeSandScale);
			int trackAngle = 0;
	        if (type == WeatherType.DUST_FLOATING)
	        {
	          trackAngle = 0;
	          if (largeIndex < 0.8F * largeSandNum)
	        	  trackAngle = 180;
	        }
			p.setAngle(trackAngle);
			p.setSpeed(largeSandSpeed);
			p.setMoveDirectionAngle(trackAngle);
			p.setType(0);
			particleList.add(p);
		}
 		
 		for (int midIndex = 0; midIndex < mediumSandNum; midIndex++) {
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(mediumSandScale);
			int trackAngle = 0;
	        if (type == WeatherType.DUST_FLOATING)
	        {
	          trackAngle = 0;
	          if (midIndex < 0.8F * mediumSandNum)
	        	  trackAngle = 180;
	        }
			p.setAngle(trackAngle);
			p.setMoveDirectionAngle(trackAngle);
			p.setSpeed(mediumSandSpeed);
			p.setType(1);
			particleList.add(p);
		}
 		
 		for (int tinyIndex = 0; tinyIndex < tinySandNum; tinyIndex++) {
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(tinySandScale);
			int trackAngle = 0;
	        if (type == WeatherType.DUST_FLOATING)
	        {
	          trackAngle = 0;
	          if (tinyIndex < 0.8F * tinySandNum)
	        	  trackAngle = 180;
	        }
			p.setAngle(trackAngle);
			p.setSpeed(tinySandSpeed);
			p.setMoveDirectionAngle(trackAngle);
			p.setType(2);
			particleList.add(p);
		}
	
		Particle cloud1 = new ParticleRect();
 		cloud1.setXY(0.0F*mScreenWidth, 0.3F*mScreenHeight);
 		cloud1.setScale(cloudyScale * 0.5f);
 		cloud1.setSpeed(30);
 		cloud1.setType(3);
 		particleList.add(cloud1);
 		
 		Particle cloud2 = new ParticleRect();
 		cloud2.setXY(0.9F*mScreenWidth, 0.0F*mScreenHeight);
 		cloud2.setSpeed(50);
 		cloud2.setScale(cloudyScale);
 		cloud2.setType(3);
 		particleList.add(cloud2);
 		
 		textureInfos.clear();
 		textureInfos.add(new TextureInfo(R.drawable.sand_l,largeSandScale));
 		textureInfos.add(new TextureInfo(R.drawable.sand_m,mediumSandScale)); 
 		textureInfos.add(new TextureInfo(R.drawable.sand_m,tinySandScale)); 
// 		textureInfos.add(new TextureInfo(R.drawable.dustcloud)); 
 		
	}
	@Override
	public int loadGLTexture(GL10 gl) {
		
		for (TextureInfo ti : textureInfos)
		{		
			int value = CacheTextrue.get(ti.drawableId, ti.scaleX, ti.scaleY ,0);
			if (isNeedCaChe && value >=0 )
			{
				ti.textureId = value;
				ti.width = CacheTextrue.getWidth(ti.drawableId, ti.scaleX, ti.scaleY ,0);
				ti.height = CacheTextrue.getHeight(ti.drawableId, ti.scaleX, ti.scaleY ,0);
			}else {			
				Bitmap bitmap = OpenGLUtils.decodeResource(ti.drawableId, ti.scaleX, ti.scaleY ,0);
				ti.textureId = OpenGLUtils.loadTexture(bitmap);
				ti.width = bitmap.getWidth();
				ti.height = bitmap.getHeight();
				if (isNeedCaChe)
				{					
					CacheTextrue.put(ti.drawableId, ti.scaleX, ti.scaleY ,0, ti.textureId,(int) ti.width,(int)ti.height);
				}
				bitmap.recycle();
			}		
		}
		for (Particle p : particleList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width *p.getScaleX()/ti.scaleX , ti.height*p.getScaleY()/ti.scaleY , true);
		}
		
		isFirst = false;
		isLoadBitmap = true;
		return 0;
	}
	
	
		
	@Override
	public void draw(GL10 gl) {
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			return;
		}
		GLES20.glUseProgram(mProgram);

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
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);

			MatrixState.pushMatrix();			
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth() / UNIT, p.getHeight() / UNIT, 1);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();

		}

	}

	
	@Override
	public String getBackground() {
		return "bg_sand_storm";
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
	
	public static int produceRandom(int seed) {
		return (int) (Math.random() * seed);
	}
}
