package com.chuanonly.livewallpaper.scenes;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.model.Particle;
import com.chuanonly.livewallpaper.model.ParticleRect;
import com.chuanonly.livewallpaper.model.TextureInfo;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.util.CacheTextrue;
import com.chuanonly.livewallpaper.util.MatrixState;
import com.chuanonly.livewallpaper.util.OpenGLUtils;

import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GLRain extends BaseScene
{

	private ArrayList<Integer> heightList;
	private ArrayList<Integer> widthList;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private int type = WeatherType.RAINY_LIGHT;
	public GLRain(int type)
	{
		this.type = type;
		setCategory(type);
		init(type);
	}
	private void init(int type)
	{
		int largeNum = 0;
		int mediumNum =30;
		int tinyNum =70;
		
		int largeSpeed = 500;
		int mediumSpeed = 250;
		int tinySpeed = 150;
		
		float scaleX = 0.75f;
		float scaleY = 1.0f;
		switch (type) {
		case WeatherType.RAINY_LIGHT:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
				largeNum = 0;
				mediumNum =30;
				tinyNum =70;
				largeSpeed = 500;
				mediumSpeed = 250;
				tinySpeed = 150;
				scaleX = 0.75f;
				scaleY = 1.0f;

			} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
				largeNum = 0;
				mediumNum =30;
				tinyNum =70;
				largeSpeed = 800;
				mediumSpeed = 450;
				tinySpeed = 250;
				scaleX = 1.0f;
				scaleY = 1.5f;
			} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
				largeNum = 0;
				mediumNum =32;
				tinyNum =80;
				largeSpeed = 1200;
				mediumSpeed = 700;
				tinySpeed = 350;
				scaleX = 1.5f;
				scaleY = 2.0f;
			} else if (mScreenHeight > 1000) {
				largeNum = 0;
				mediumNum =32;
				tinyNum =80;
				largeSpeed = 1800;
				mediumSpeed = 900;
				tinySpeed = 450;
				scaleX = 2.0f;
				scaleY = 3.0f;
			}
			break;
		case WeatherType.RAINY_MODERATE:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
				largeNum = 30;
				mediumNum =40;
				tinyNum =100;
				largeSpeed = 500;
				mediumSpeed = 250;
				tinySpeed = 150;
				scaleX = 0.75f;
				scaleY = 1.0f;

			} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
				largeNum = 30;
				mediumNum =50;
				tinyNum =100;
				largeSpeed = 800;
				mediumSpeed = 450;
				tinySpeed = 250;
				scaleX = 1.0f;
				scaleY = 1.5f;
			} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
				largeNum = 35;
				mediumNum =50;
				tinyNum =120;
				largeSpeed = 1200;
				mediumSpeed = 700;
				tinySpeed = 350;
				scaleX = 1.5f;
				scaleY = 2.0f;
			} else if (mScreenHeight > 1000) {
				largeNum = 35;
				mediumNum =50;
				tinyNum =120;
				largeSpeed = 1800;
				mediumSpeed = 900;
				tinySpeed = 450;
				scaleX = 2.0f;
				scaleY = 3.0f;
			}
			break;
		case WeatherType.RAINY_HEAVY:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
				largeNum = 36;
				mediumNum =72;
				tinyNum =200;
				largeSpeed = 500;
				mediumSpeed = 250;
				tinySpeed = 150;
				scaleX = 0.75f;
				scaleY = 1.0f;

			} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
				largeNum = 36;
				mediumNum =72;
				tinyNum =200;
				largeSpeed = 800;
				mediumSpeed = 450;
				tinySpeed = 250;
				scaleX = 1.0f;
				scaleY = 1.5f;
			} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
				largeNum = 36;
				mediumNum =72;
				tinyNum =200;
				largeSpeed = 1200;
				mediumSpeed = 700;
				tinySpeed = 350;
				scaleX = 1.5f;
				scaleY = 2.0f;
			} else if (mScreenHeight > 1000) {
				largeNum = 50;
				mediumNum =90;
				tinyNum =200;
				largeSpeed = 1800;
				mediumSpeed = 900;
				tinySpeed = 450;
				scaleX = 2.0f;
				scaleY = 3.0f;
			}
			break;
		case WeatherType.RAINY_STORM:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
				largeNum = 36;
				mediumNum =72;
				tinyNum =130;
				largeSpeed = 500;
				mediumSpeed = 250;
				tinySpeed = 150;
				scaleX = 0.8f;
				scaleY = 1.1f;

			} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
				largeNum = 36;
				mediumNum =72;
				tinyNum =150;
				largeSpeed = 800;
				mediumSpeed = 450;
				tinySpeed = 250;
				scaleX = 1.1f;
				scaleY = 1.7f;
			} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
				largeNum = 40;
				mediumNum =80;
				tinyNum =150;
				largeSpeed = 1200;
				mediumSpeed = 700;
				tinySpeed = 350;
				scaleX = 1.6f;
				scaleY = 2.2f;
			} else if (mScreenHeight > 1000) {
				largeNum = 60;
				mediumNum =120;
				tinyNum =170;
				largeSpeed = 1800;
				mediumSpeed = 900;
				tinySpeed = 450;
				scaleX = 2.2f;
				scaleY = 3.4f;
			}
			break;
		}
		
		

		
		largeSpeed *= 0.8f;
		mediumSpeed *= 0.8f;
		tinySpeed *= 0.8f;
		
		//小雨太长，密度有点多，所以要改成短一点，稀疏一点
		if (type == WeatherType.RAINY_LIGHT)
		{
			scaleY *= 0.8f;
			mediumNum = (int) (mediumNum *0.8);
			tinyNum = (int) (tinyNum *0.8);
		}
		int rand = (int) (Math.random() * 21);
		int randomBmpAngle = rand - 10;
		int randMoveAngle = randomBmpAngle - 90;
	
	
		
		int large = largeNum / 2;
		
		for (int largeIndex = 0; largeIndex < large; largeIndex++) {
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(scaleX);
			p.setScaleY(scaleY);
			p.setAngle(randomBmpAngle);
			p.setSpeed(largeSpeed);
			p.setType(0);
			p.setMoveDirectionAngle(randMoveAngle);
			particleList.add(p);
		}	
		for (int largeIndex = 0; largeIndex < large; largeIndex++) {
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(scaleX);
			p.setScaleY(scaleY);
			p.setAngle(randomBmpAngle);
			p.setSpeed((int) (1.5F*largeSpeed));
			p.setType(1);
			p.setMoveDirectionAngle(randMoveAngle);
			particleList.add(p);
		}	
		
		for (int midIndex =0; midIndex < mediumNum; midIndex++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(scaleX);
			p.setScaleY(scaleY);
			p.setAngle(randomBmpAngle);
			p.setSpeed(mediumSpeed);
			p.setType(2);
			p.setMoveDirectionAngle(randMoveAngle);
			particleList.add(p);	
		}
		
		for (int tinyIndex =0; tinyIndex < tinyNum; tinyIndex++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(scaleX);
			p.setScaleY(scaleY);
			p.setAngle(randomBmpAngle);
			p.setSpeed(tinySpeed);
			p.setType(3);
			p.setMoveDirectionAngle(randMoveAngle);
			particleList.add(p);	
		}
		
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.raindrop_l, scaleX, scaleY, randomBmpAngle));
		textureInfos.add(new TextureInfo(R.drawable.raindrop_xl, scaleX, scaleY, randomBmpAngle));
		textureInfos.add(new TextureInfo(R.drawable.raindrop_m, scaleX, scaleY, randomBmpAngle));
		textureInfos.add(new TextureInfo(R.drawable.raindrop_s, scaleX, scaleY, randomBmpAngle));			
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
			isLoadBitmap = true;
		}
		GLES20.glUseProgram(mProgram);

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
			MatrixState.scale( p.getWidth() /UNIT, p.getHeight()/UNIT, 1);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			MatrixState.popMatrix();

		}
	}


	public void setFloatValue(float[] s,  float ...values)
	{
		for (int i = 0; i < values.length; i++)
		{
			s[i] = values[i];
		}
	}

	@Override
	public String getBackground() {
		if (type == WeatherType.RAINY_LIGHT)
		{
			return "bg_rain1";
		} 
		return "bg_rain";
	}
}
