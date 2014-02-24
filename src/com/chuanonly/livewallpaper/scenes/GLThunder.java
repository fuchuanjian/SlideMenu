package com.chuanonly.livewallpaper.scenes;


import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.model.Particle;
import com.chuanonly.livewallpaper.model.ParticleThunderLight;
import com.chuanonly.livewallpaper.model.ParticleWhiteBg;
import com.chuanonly.livewallpaper.model.TextureInfo;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.util.MatrixState;
import com.chuanonly.livewallpaper.util.OpenGLUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.opengl.GLES20;

public class GLThunder extends BaseScene
{

	private GLRain rainScen;
	
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
    private float whiteBgAlpha = 0.0f;
	private Particle whiteBg;
	public GLThunder()
	{
		setCategory(WeatherType.SHOWER_THUNDER);
		init();
		rainScen = new GLRain(WeatherType.RAINY_MODERATE);
		rainScen.setCaChe(false);
	}
	private void init()
	{		

		
		float scale = 0.75F; 
		if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {			
			scale = 0.4f;
		} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
			scale = 0.6f;
		} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
			scale = 1.0f;
		} else if (mScreenHeight > 1000) {
			scale = 1.0f;
			if ((mDensity > 0.0F) && (mDensity <= 1.0F))
				scale = 0.75f;
			else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
				scale  = 1.0f;
			else if (mDensity > 1.5F)
				scale  = 1.5f;
		}
		particleList.clear();
		Particle thunderParticle = new ParticleThunderLight();
		thunderParticle.setXY(mScreenWidth * 0.3f, 0.0f);
		thunderParticle.setAlpha(0);
		thunderParticle.setScale(scale);
		thunderParticle.setType(0);
		thunderParticle.setAlphaSpeed(20);
		particleList.add(thunderParticle);
		
		Particle thunderLight = new ParticleThunderLight();
		thunderLight.setXY(mScreenWidth * 0.4f, 0.0f);
		thunderLight.setAlpha(-500);
		thunderLight.setScale(scale);
		thunderLight.setAlphaSpeed(20);
		thunderLight.setType(1);
		particleList.add(thunderLight);
		
		whiteBg  = new ParticleWhiteBg();
		whiteBg.setXY(0.0f, 0.0f);
		whiteBg.setWidth(mScreenWidth);
		whiteBg.setHeight(mScreenHeight);
		whiteBg.setType(2);
		whiteBg.setAlpha(0);
		whiteBg.setAlphaSpeed(-40);
		particleList.add(whiteBg);
		
		
		
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.lightning_1,scale));
		textureInfos.add(new TextureInfo(R.drawable.lightning_2,scale));
		textureInfos.add(new TextureInfo(-1));
		
		
		
	}
	
	public int loadGLTexture(GL10 gl)
	{
		rainScen.loadGLTexture(null);
		
		for (TextureInfo ti : textureInfos)
		{			
			if (ti.drawableId == -1)
			{
				int[]  colors= {Color.WHITE,Color.BLACK,Color.BLUE};
				Bitmap whiteBm = Bitmap.createBitmap(colors,1,1, Config.ARGB_8888);
				ti.textureId = OpenGLUtils.loadTexture(whiteBm);
				ti.width = mScreenWidth;
				ti.height = mScreenHeight;
				whiteBm.recycle();	
				continue;
			}
			Bitmap bitmap = OpenGLUtils.decodeResource(ti.drawableId, ti.scaleX	, ti.scaleY, 0);
			ti.textureId = OpenGLUtils.loadTexture(bitmap);
			ti.width = bitmap.getWidth();
			ti.height = bitmap.getHeight();
			bitmap.recycle();
		}

		
		for (Particle p : particleList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height * p.getScaleY() /ti.scaleY , true);    
		}
		
		isFirst = false;
		isLoadBitmap = true;
		return 0;
	}
	public void draw(GL10 gl)
	{
		
		rainScen.draw(null);
		
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			return;
		}
		GLES20.glUseProgram(mProgram);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 3, GLES20.GL_FLOAT, false, texture.length, mTexCoorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);


		for (int i=0; i< particleList.size(); i++)
		{		
			Particle p = particleList.get(i);	
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());			
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);					
			
			MatrixState.pushMatrix();	
			float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth()/UNIT,p.getHeight()/UNIT, 1);		
			float alpha = (float) p.getAlpha() / 255;
			GLES20.glUniform1f(mAlphaHandle,alpha* GLOBE_ALPHA);
			
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();
			
			if (p != whiteBg)
			{
				if ((p.getAlpha() >=200 && (p.getAlpha()-p.getAlphaSpeed())<200) )
				{
					whiteBg.setAlpha(p.getAlpha()/4);
				}
			}
			if (!isStatic)
			{
				p.move(0);				
			}
		
		}


	}

	@Override
	public String getBackground() {
		return "bg_thunder_storm";
	}	
}
