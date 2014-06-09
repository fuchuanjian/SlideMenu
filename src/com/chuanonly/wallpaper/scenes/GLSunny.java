package com.chuanonly.wallpaper.scenes;


import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.chuanonly.wallpaper.R;
import com.chuanonly.wallpaper.model.GLFireBalloon;
import com.chuanonly.wallpaper.model.ParticalSunShine;
import com.chuanonly.wallpaper.model.Particle;
import com.chuanonly.wallpaper.model.TextureInfo;
import com.chuanonly.wallpaper.model.WeatherType;
import com.chuanonly.wallpaper.util.CacheTextrue;
import com.chuanonly.wallpaper.util.MatrixState;
import com.chuanonly.wallpaper.util.OpenGLUtils;

public class GLSunny extends BaseScene
{

	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	
	public GLSunny()
	{
		setCategory(WeatherType.FINE);
		init();
	}
	private void init()
	{
		
		float sunshineScale = 2.0f;//1.5f;
	    int sunAlphaSpeed = 1; // 10
	    float fireScale = 0.8f;
	    int balloonSpeed =1;
	    float balloonRadius = 12.0f;
	    float balloonPosX = 0.6f;
	    float balloonPosY = 0.35f;	 
	    if ((mScreenWidth > 0) && (mScreenWidth <= 240)) {
		    balloonRadius = 4.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.4f;
	    	
		} else if ((240 < mScreenWidth) && (mScreenWidth <= 320)) {
		    balloonRadius = 6.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.38f;
		} else if ((320 < mScreenWidth) && (mScreenWidth <= 540)) {
		    balloonRadius = 8.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.38f;
		} else if ((540 < mScreenWidth) && (mScreenWidth <= 700)) {
		    balloonRadius = 10.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.35f;
		} else if ((700 < mScreenWidth) && (mScreenWidth <= 1000)) {
		    balloonRadius = 12.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.35f;
		} else if (mScreenWidth > 1000) {
		    balloonRadius = 15.0f;
		    balloonPosX = 0.6f;
		    balloonPosY = 0.3f;
		}
		
		if ((mDensity > 0.0F) && (mDensity <= 1.0F)) {
			fireScale = 0.3f;
			sunshineScale = 0.5f;
		} else if ((mDensity > 1.0F) && (mDensity <= 1.5F)) {
			fireScale = 0.6f;
			sunshineScale = 1.0f;
		} else if (mDensity > 1.5F) {
			fireScale = 0.8f;
			sunshineScale = 1.4f;
			
		}
	    
		Particle sunShine1 = new ParticalSunShine();
		sunShine1.setXY(0.0f, 0.0f);
		sunShine1.setAlpha(255);
		sunShine1.setScale(sunshineScale);
		sunShine1.setAlphaSpeed(sunAlphaSpeed);
		sunShine1.setType(0);
		particleList.add(sunShine1);
		
		Particle sunShine2 = new ParticalSunShine();
		sunShine2.setXY(0.0f, 0.0f);
		sunShine2.setScale(sunshineScale);
		sunShine2.setAlpha(0);
		sunShine2.setAlphaSpeed(-sunAlphaSpeed);
		sunShine2.setType(1);
		particleList.add(sunShine2);
		
		Particle sunShine3 = new ParticalSunShine();
		sunShine3.setXY(0.0f, 0.0f);
		sunShine3.setScale(sunshineScale);
		sunShine3.setAlpha(255);
		sunShine3.setAlphaSpeed(-sunAlphaSpeed);
		sunShine3.setType(2);
		particleList.add(sunShine3);
		
		GLFireBalloon fireBalloon = new GLFireBalloon();
		fireBalloon.setXY(balloonPosX * mScreenWidth, balloonPosY*mScreenHeight);
		fireBalloon.setScale(fireScale);
		fireBalloon.setSpeed(balloonSpeed);
		fireBalloon.setType(3);
		particleList.add(fireBalloon);
		
		textureInfos.clear();
 		textureInfos.add(new TextureInfo(R.drawable.sunshine_1,sunshineScale));
 		textureInfos.add(new TextureInfo(R.drawable.sunshine_2,sunshineScale));
 		textureInfos.add(new TextureInfo(R.drawable.sunshine_3,sunshineScale));
 		textureInfos.add(new TextureInfo(R.drawable.fire_balloon,fireScale));
		
	}
	
	public int loadGLTexture(GL10 gl)
	{
		
		for (TextureInfo ti : textureInfos)
		{		
			int value = CacheTextrue.get(ti.drawableId, ti.scaleX, ti.scaleY ,ti.angle);
			if (isNeedCaChe && value >=0 )
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

		
		for (Particle p : particleList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX()/ti.scaleX , ti.height * p.getScaleY()/ti.scaleY, true);    
		}
		isFirst = false;
		isLoadBitmap = true;
		return 0;
	}
	public void draw(GL10 gl)
	{
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
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());			
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);					
		
			MatrixState.pushMatrix();	
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth() / UNIT, p.getHeight() / UNIT, 1);		
			float alpha = (float) p.getAlpha() / 255;
			GLES20.glUniform1f(mAlphaHandle,alpha * GLOBE_ALPHA);
			
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();
			if (!isStatic)
			{
				p.move(0);				
			}
		}
	}

	
	public int initTexture(int drawableId, int n){

    	Particle p = particleList.get(n);
    	Bitmap bitmapTmp = OpenGLUtils.decodeResource(drawableId, p.getScaleX(), p.getScaleY(), 0);	
    	if (isFirst == true)
    	{
    		p.setBmParams(bitmapTmp.getWidth(), bitmapTmp.getHeight(), true);

    	}    
        

        int textureId = 0;
        if (bitmapTmp != null)
        {
        	textureId = OpenGLUtils.loadTexture(bitmapTmp);
        	bitmapTmp.recycle();           	
        }
        
        return textureId;
	}
	@Override
	public String getBackground() {
		return "bg_fine_day";
	}	
}
