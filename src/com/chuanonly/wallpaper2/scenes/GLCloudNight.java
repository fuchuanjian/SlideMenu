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
public class GLCloudNight extends BaseScene
{
	private static ArrayList<Particle> particleList = new ArrayList<Particle>();

    public GLCloudNight()
	{
    	setCategory(WeatherType.CLOUDY_NIGHT);
		init();
	}
	private void init()
	{
	    particleList.clear();
	    
		float cloudyScale1 = 1.5f;
		float cloudyScale2 = 2.0f;
		int cloudySpeed1 = 20;
		int cloudySpeed2 = 15;

		if ((mDensity > 0.0F) && (mDensity <= 1.0F)) {
			cloudyScale1 = 0.8f;
			cloudyScale2 = 1.0f;
			cloudySpeed1 = 10;
			cloudySpeed2 = 8;
		} else if ((mDensity > 1.0F) && (mDensity <= 1.5F)) {
			cloudyScale1 = 1.2f;
			cloudyScale2 = 1.5f;
			cloudySpeed1 = 15;
			cloudySpeed2 = 10;
		} else if (mDensity > 1.5F) {
			cloudyScale1 = 1.5f;
			cloudyScale2 = 2.0f;
			cloudySpeed1 = 20;
			cloudySpeed2 = 15;
		}
		
		
		
		Particle cloud1 = new ParticleRect();
		cloud1.setXY(0.0f, 0.0f);
		cloud1.setSpeed(cloudySpeed1);
		cloud1.setScale(cloudyScale1);
		cloud1.setType(0);
		particleList.add(cloud1);

		Particle cloud2 = new ParticleRect();
		cloud2.setXY(0.0f, 0.45f * mScreenHeight);
		cloud2.setSpeed(cloudySpeed2);
		cloud2.setScale(cloudyScale2);
		cloud2.setType(1);
		particleList.add(cloud2);
		
		Particle cloud3 = new ParticleRect();
		cloud3.setXY(0.0f, 0.0f);
		cloud3.setInitDiffXY(-mScreenWidth, 0,true, false);
		cloud3.setSpeed(cloudySpeed1);
		cloud3.setScale(cloudyScale1);
		cloud3.setType(0);
		particleList.add(cloud3);
		
		Particle cloud4 = new ParticleRect();
		cloud4.setXY(0.0f, 0.45f * mScreenHeight);
		cloud4.setInitDiffXY(-mScreenWidth, 0,true, false);
		cloud4.setSpeed(cloudySpeed2);
		cloud4.setScale(cloudyScale2);
		cloud4.setType(1);
		particleList.add(cloud4);
		
		
		textureInfos.clear();
 		textureInfos.add(new TextureInfo(R.drawable.cloudy_night1,cloudyScale1));
 		textureInfos.add(new TextureInfo(R.drawable.cloudy_night2,cloudyScale2));
		
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
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height* p.getScaleY() /ti.scaleY , true);    
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
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);			
			
			MatrixState.pushMatrix();			
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth() / UNIT, p.getHeight() /UNIT, -5);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			MatrixState.popMatrix();
			

		}	
	}

	@Override
	public String getBackground() {
		return "bg_cloudy_night";
	}
	
}
