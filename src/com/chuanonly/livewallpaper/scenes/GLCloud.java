package com.chuanonly.livewallpaper.scenes;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.model.Particle;
import com.chuanonly.livewallpaper.model.ParticleRect;
import com.chuanonly.livewallpaper.model.TextureInfo;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.util.CacheTextrue;
import com.chuanonly.livewallpaper.util.MatrixState;
import com.chuanonly.livewallpaper.util.OpenGLUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLES20;

@SuppressLint("NewApi")
public class GLCloud extends BaseScene
{
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
 
	public GLCloud()
	{
		setCategory(WeatherType.CLOUDY);
		init();
	}
	/**  初始化item  */
	private void init()
	{
	    particleList.clear();
	    
    	float cloudyScale1 = 1.6f;
 	    float cloudyScale2 = 1.6f;
 	    float cloudyScale3 = 1.8f;
 	    float cloudyScale4 = 1.8f;
 	    float cloudyY2 = 0.12f;  //0.12
 	    float cloudyY3 = 0.36f;  //0.36	    
 	    
	    if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
	    	cloudyScale1 = 0.4f;
	    	cloudyScale2 = 0.3f;
	    	cloudyScale3 = 0.4f;
	    	cloudyScale4 = 0.4f;
	    	cloudyY2 = 0.15f;
	    	cloudyY3 = 0.32f;
			
		} else if ((400 < mScreenHeight) && (mScreenHeight <= 500)) {
	    	cloudyScale1 = 0.6f;
	    	cloudyScale2 = 0.6f;
	    	cloudyScale3 = 0.8f;
	    	cloudyScale4 = 0.8f;
	    	cloudyY2 = 0.15f;
	    	cloudyY3 = 0.36f;
		} else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
	    	cloudyScale1 = 1.0f;
	    	cloudyScale2 = 1.0f;
	    	cloudyScale3 = 1.2f;
	    	cloudyScale4 = 1.2f;
	    	cloudyY2 = 0.12f;
	    	cloudyY3 = 0.36f;
		} else if (mScreenHeight > 1000) {
	    	cloudyScale1 = 1.6f;
	    	cloudyScale2 = 1.6f;
	    	cloudyScale3 = 1.8f;
	    	cloudyScale4 = 1.8f;
	    	cloudyY2 = 0.12f;
	    	cloudyY3 = 0.36f;
		}	    
	 		ParticleRect cloud1 = new ParticleRect();
	 		cloud1.setXY(0.0f, 0.0f);
	 		cloud1.setScale(cloudyScale1);
	 		cloud1.setSpeed(30);
	 		cloud1.setType(0);
	 		particleList.add(cloud1);
	 		
	 		ParticleRect cloud2 = new ParticleRect();
	 		cloud2.setXY(-0.08f * Particle.UNIT, cloudyY2*mScreenHeight);
	 		cloud2.setInitDiffXY(true, false);
	 		cloud2.setSpeed(25);
	 		cloud2.setType(1);
	 		cloud2.setScale(cloudyScale2);
	 		particleList.add(cloud2);
	 		

	 		
	 		ParticleRect cloud3 = new ParticleRect();
	 		cloud3.setXY(-0.25f * Particle.UNIT, cloudyY3 * mScreenHeight);
	 		cloud3.setInitDiffXY(true, false);
	 		cloud3.setSpeed(15);
	 		cloud3.setScale(cloudyScale3);
	 		cloud3.setType(2);
	 		particleList.add(cloud3);	
	 		
	 		ParticleRect cloud4 = new ParticleRect();
	 		cloud4.setXY(0.0f, cloudyY3*mScreenHeight);
	 		cloud4.setScale(cloudyScale4);
	 		cloud4.setSpeed(10);
	 		cloud4.setType(3);
	 		particleList.add(cloud4);
	 		
	 		ParticleRect cloud5 = new ParticleRect();
	 		cloud5.setXY(-0.08f * Particle.UNIT, cloudyY2*mScreenHeight);
	 		cloud5.setInitDiffXY(-mScreenWidth, 0,true, false);
	 		cloud5.setSpeed(25);
	 		cloud5.setScale(cloudyScale2);
	 		cloud5.setType(1);
	 		particleList.add(cloud5);
	 		
	 		ParticleRect cloud6 = new ParticleRect();
	 		cloud6.setXY(0.0f , cloudyY3*mScreenHeight);
	 		cloud6.setInitDiffXY(-mScreenWidth, 0,true, false);
	 		cloud6.setSpeed(10);
	 		cloud6.setScale(cloudyScale4);
	 		cloud6.setType(3);
	 		particleList.add(cloud6);
	 		
	 		textureInfos.clear();
	 		textureInfos.add(new TextureInfo(R.drawable.cloudy_day_1,cloudyScale1));
	 		textureInfos.add(new TextureInfo(R.drawable.cloudy_day_2,cloudyScale2));
	 		textureInfos.add(new TextureInfo(R.drawable.cloudy_day_3,cloudyScale3));
	 		textureInfos.add(new TextureInfo(R.drawable.cloudy_day_4,cloudyScale4));				
			
	}
	public int loadGLTexture(GL10 gl)
	{
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

		
		for (Particle p : particleList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX, ti.height* p.getScaleY() /ti.scaleY , true);    
		}

		isLoadBitmap = true;
		isFirst = false;
		return 0;
	}
	private long lastTime = 0;
	public void draw(GL10 gl)
	{
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			return;
		}
		GLES20.glUseProgram(mProgram);

		GLES20.glVertexAttribPointer(maTexCoorHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mTexCoorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		float offset = 0;
		if (lastTime != 0)
		{
			offset = (System.currentTimeMillis()-lastTime) /1000.0f;
			if (System.currentTimeMillis()-lastTime > 100)
			{
				offset = 0;
			}
		}
		lastTime = System.currentTimeMillis();
		for (int i=0; i<particleList.size(); i++)
		{
			Particle p = particleList.get(i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());
			
			p.setDrawTime(System.currentTimeMillis());
			if (!isStatic)
			{
				p.move(offset*p.getSpeed());				
			}
			
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);			
			
			MatrixState.pushMatrix();			
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth()/UNIT, p.getHeight() / UNIT, 1);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			MatrixState.popMatrix();
		}	
	}
	
	@Override
	public String getBackground() {
		return "bg_cloudy_day";
	}
}
