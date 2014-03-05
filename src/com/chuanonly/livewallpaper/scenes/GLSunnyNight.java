package com.chuanonly.livewallpaper.scenes;


import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.model.Particle;
import com.chuanonly.livewallpaper.model.ParticleMetor;
import com.chuanonly.livewallpaper.model.ParticleStar;
import com.chuanonly.livewallpaper.model.TextureInfo;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.util.CacheTextrue;
import com.chuanonly.livewallpaper.util.MatrixState;
import com.chuanonly.livewallpaper.util.OpenGLUtils;

import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GLSunnyNight extends BaseScene
{

	private ArrayList<Particle> starList = new ArrayList<Particle>();
	private ArrayList<Particle> metorList = new ArrayList<Particle>();

	public GLSunnyNight()
	{
		setCategory(WeatherType.FINE_NIGHT);
		init();
	}
	private void init()
	{
	    		
	    starList.clear();
	    metorList.clear();
	    
	    float seawaterPosyScaleMid = 1.2f;//1.2f;
	    float seawaterPosyScaleTop = 1.5f;
	    float seawaterScale = 1.2f;
	    float seawaterSpeedMin = 1.2f;
	    float seawaterSpeedMax = 1.0f;
	    
	    float starScale = 1.0f;
	    float fickerStarLargeScale = 1.0f;
	    float fickerStarNormalScale = 1.0f;
	    float fickerStarSmallScale = 1.0f;
	    float meteorscale = 1.0f;
	    float moonScale = 1.0f;
	    float tringleDiff ;
	    if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
		   seawaterPosyScaleMid = 0.7f;//1.2f;
		   seawaterPosyScaleTop = 0.9f;
		   seawaterScale = 0.5f;
		   seawaterSpeedMin = 20f;
		   seawaterSpeedMax = 200f;
		} else if ((400 < mScreenHeight) && (mScreenHeight <= 500)) {
		   seawaterPosyScaleMid = 0.9f;//1.2f;
		   seawaterPosyScaleTop = 1.1f;
		   seawaterScale = 0.6f;
		   seawaterSpeedMin = 20f;
		   seawaterSpeedMax = 200f;
		} else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
			   seawaterPosyScaleMid = 1.1f;//1.2f;
			   seawaterPosyScaleTop = 1.3f;
			   seawaterScale = 1.1f;
			   seawaterSpeedMin = 30f;
			   seawaterSpeedMax = 200f;
		} else if (mScreenHeight > 1000) {
			   seawaterPosyScaleMid = 1.4f;//1.2f;
			   seawaterPosyScaleTop = 1.55f;
			   seawaterScale = 1.3f;
			   seawaterSpeedMin = 40f;
			   seawaterSpeedMax = 200f;
		}
		if ((mDensity > 0.0F) && (mDensity <= 1.0F)) {
		    starScale = 0.7f;
		    fickerStarLargeScale = 1.0f;
		    fickerStarNormalScale = 1.0f;
		    fickerStarSmallScale = 1.0f;
		    meteorscale = 0.5f;
		    moonScale = 0.6f;
		    tringleDiff = 0.1f;
		} else if ((mDensity > 1.0F) && (mDensity <= 1.5F)) {
		    starScale = 1.1f;
		    fickerStarLargeScale = 1.2f;
		    fickerStarNormalScale = 1.0f;
		    fickerStarSmallScale = 1.0f;
		    meteorscale = 0.6f;
		    moonScale = 0.75f;
		    tringleDiff = 0.2f;
		} else if (mDensity > 1.5F) {
		    starScale = 1.3f;
		    fickerStarLargeScale = 1.5f;
		    fickerStarNormalScale = 1.2f;
		    fickerStarSmallScale = 1.2f;
		    meteorscale = 1.0f;
		    moonScale = 1.0f;
		    tringleDiff = 0.3f;
		}
		
	    for (int i = 0; i < 12; i++)  //8
		{
	    	Particle star = new ParticleStar();
			int x = new Random().nextInt(mScreenWidth);
			int y = new Random().nextInt(mScreenHeight/2);
			star.setAlpha(new Random().nextInt(255));
			star.setAlphaSpeed(2);
			star.setScale(starScale);
			star.setXY(x, y);
			star.setType(0);
			starList.add(star);
		}
	    for (int i = 0; i < 10; i++)  //5
		{
	    	Particle star = new ParticleStar();
			int x = new Random().nextInt(mScreenWidth);
			int y = new Random().nextInt(mScreenHeight/2);
			star.setAlpha(new Random().nextInt(255));
			star.setAlphaSpeed(5);
			star.setScale(starScale);
			star.setXY(x, y);	
			star.setType(1);
			starList.add(star);
		}
	    for (int i = 0; i < 6; i++) //3
		{
	    	Particle star = new ParticleStar();
			int x = new Random().nextInt(mScreenWidth);
			int y = new Random().nextInt(mScreenHeight/2);
			star.setAlpha(new Random().nextInt(255));
			star.setAlphaSpeed(3);
			star.setScale(starScale);
			star.setXY(x, y);	
			star.setType(2);
			starList.add(star);
		}
	    
	    for (int i= 0; i<6; i++) // 3
	    {	    	
	    	Particle star = new ParticleStar();
			int x = new Random().nextInt(mScreenWidth);
			int y = new Random().nextInt(mScreenHeight/2);
			star.setAlpha(new Random().nextInt(255));
			star.setAlphaSpeed(3);
			star.setScale(starScale);
			star.setXY(x, y);	
			star.setType(3);
			starList.add(star);   
	    }
	    int angle = 75;
	    for (int i= 0; i<2; i++)
	    {	    	
	    	Particle star = new ParticleMetor();
	    	int x = new Random().nextInt(mScreenWidth) - mScreenWidth* (i+1);
	    	int y = new Random().nextInt(mScreenHeight/4) - mScreenHeight/10 ;
	    	star.setScale(starScale);
	    	star.setSpeed(900);
	    	star.setXY(x, y);	
	    	star.setAngle(angle);
	    	star.setAlphaSpeed(10);
	    	star.setAlpha(0);
	    	star.setType(4);
	    	metorList.add(star);   
	    }
		
	    textureInfos.clear();
 		textureInfos.add(new TextureInfo(R.drawable.fine_min_star, starScale));
 		textureInfos.add(new TextureInfo(R.drawable.fine_small_star, starScale));
 		textureInfos.add(new TextureInfo(R.drawable.fine_middle_star, starScale));
 		textureInfos.add(new TextureInfo(R.drawable.fine_big_star, starScale)); 
 		textureInfos.add(new TextureInfo(R.drawable.meteor, meteorscale, meteorscale, 90 + angle));  
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
		
		for (Particle p : starList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height *p.getScaleY() /ti.scaleY , true);    
		}
		for (Particle p : metorList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height *p.getScaleY() /ti.scaleY , true);   
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
		
		for (int i=0; i<starList.size(); i++)
		{
			Particle p = starList.get(i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());			
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);					
		
			MatrixState.pushMatrix();	
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth()/UNIT,p.getHeight()/UNIT, 1);	
			float alpha = (float) p.getAlpha() / 255;
			GLES20.glUniform1f(mAlphaHandle,alpha);
			
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();
			if (!isStatic)
			{
				p.move(0);				
			}
		}
		
		for (int i=0; i<metorList.size(); i++)
		{
			Particle p = metorList.get(i);
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
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());			
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);					
		
			MatrixState.pushMatrix();	
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth()/UNIT, p.getHeight()/UNIT, 1);
			float alpha = (float) p.getAlpha() / 255;
			GLES20.glUniform1f(mAlphaHandle,alpha);
			
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();
			if (!isStatic)
			{
				p.move(offset*p.getSpeed());				
			}
		}
		
	
	}


	
	
	@Override
	public String getBackground() {
		return "bg_fine_night";
	}
}
