package com.chuanonly.wallpaper2.scenes;

import java.util.ArrayList;
import java.util.Random;

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

public class GLShowerRain extends BaseScene
{

	private ArrayList<Integer> heightList;
	private ArrayList<Integer> widthList;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private final int ON_FRAME = 60*7;
	private final int OFF_FRAME = 30;
	private int frameCnt = 0;
	private int tmp[] = new int[2];
	private boolean isON = true;
	private final int LINE = 3;
	public GLShowerRain()
	{
		setCategory(WeatherType.SHOWER);
		init();
	}
	private void init()
	{
		
		int largeBitmapAngle = -10;
		int mediumBitmapAngle = -10;
		int tinyBitmapAngle = -10;
		int largeTrackAngle= 100;
		int mediumTrackAngle = 100;
		int tinyTrackAngle = 100;
		int largeRainNum =  0;
		int mediumRainNum = 30;
		int tinyRainNum = 70;
		int largeRainSpeed = 500;
		int mediumRainSpeed = 250;
		int tinyRainSpeed = 150;
		float rainScaleX = 0.75f;
		float rainScaleY = 1.0f;
		float lightningScale = 0.4f;
		float TrafficlightScale = 0.5f;
		if ((mScreenHeight > 0) && (mScreenHeight <= 400)) {
			largeBitmapAngle = -10;
			mediumBitmapAngle = -10;
			tinyBitmapAngle = -10;
			largeTrackAngle= 100;
			mediumTrackAngle = 100;
			tinyTrackAngle = 100;
			largeRainNum =  0;
			mediumRainNum = 30;
			tinyRainNum = 70;
			largeRainSpeed = 500;
			mediumRainSpeed = 250;
			tinyRainSpeed = 150;
			rainScaleX = 0.75f;
			rainScaleY = 1.0f;
			lightningScale = 0.4f;
			TrafficlightScale = 0.5f;
		} else if ((mScreenHeight > 400) && (mScreenHeight <= 500)) {
			largeBitmapAngle = -10;
			mediumBitmapAngle = -10;
			tinyBitmapAngle = -10;
			largeTrackAngle= 100;
			mediumTrackAngle = 100;
			tinyTrackAngle = 100;
			largeRainNum =  0;
			mediumRainNum = 30;
			tinyRainNum = 70;
			largeRainSpeed = 800;
			mediumRainSpeed = 450;
			tinyRainSpeed = 250;
			rainScaleX = 1.0f;
			rainScaleY = 1.5f;
			lightningScale = 0.5f;
			TrafficlightScale = 0.5f;
		} else if ((mScreenHeight > 500) && (mScreenHeight <= 1000)) {
			largeBitmapAngle = -10;
			mediumBitmapAngle = -10;
			tinyBitmapAngle = -10;
			largeTrackAngle= 100;
			mediumTrackAngle = 100;
			tinyTrackAngle = 100;
			largeRainNum =  0;
			mediumRainNum = 32;
			tinyRainNum = 80;
			largeRainSpeed = 1200;
			mediumRainSpeed = 700;
			tinyRainSpeed = 350;
			rainScaleX = 1.5f;
			rainScaleY = 2.0f;
			lightningScale = 0.8f;
			TrafficlightScale = 0.9f;
		} else if (mScreenHeight > 1000) {
			largeBitmapAngle = -10;
			mediumBitmapAngle = -10;
			tinyBitmapAngle = -10;
			largeTrackAngle= 100;
			mediumTrackAngle = 100;
			tinyTrackAngle = 100;
			largeRainNum =  0;
			mediumRainNum = 32;
			tinyRainNum = 80;
			largeRainSpeed = 1800;
			mediumRainSpeed = 900;
			tinyRainSpeed = 450;
			rainScaleX = 2.0f;
			rainScaleY = 3.0f;
			lightningScale = 0.8f;
			TrafficlightScale = 1.0f;
			if ((mDensity > 0.0F) && (mDensity <= 1.0F))
				lightningScale = 0.75f;
			else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
				lightningScale = 1.0f;
			else if (mDensity > 1.5F)
				lightningScale = 1.5f;
		}
		
		int rand = (int) (Math.random() * 21);
		int randomBmpAngle = rand - 10;
		
		
		for (int midIndex =0; midIndex < mediumRainNum; midIndex++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(rainScaleX);
			p.setScaleY(rainScaleY);
			p.setAngle(randomBmpAngle);
			p.setMoveDirectionAngle(randomBmpAngle-90);
			p.setSpeed(mediumRainSpeed);
			p.setType(0);
			particleList.add(p);	
		}
		
		for (int tinyIndex =0; tinyIndex < tinyRainNum; tinyIndex++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScaleX(rainScaleX);
			p.setScaleY(rainScaleY);
			p.setAngle(randomBmpAngle);
			p.setMoveDirectionAngle(randomBmpAngle-90);
			p.setSpeed(tinyRainSpeed);
			p.setType(1);
			particleList.add(p);		
		}
		resetLiveOfPartilce(LINE);
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.raindrop_m, rainScaleX, rainScaleY, randomBmpAngle));
		textureInfos.add(new TextureInfo(R.drawable.raindrop_s, rainScaleX, rainScaleY, randomBmpAngle));
	}
	
	public int[] getRandXY()
	{
		
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
		tmp[0] = ((Integer) this.widthList.get(xRandom)).intValue();
		tmp[1] = ((Integer) this.heightList.get(yRandom)).intValue();
		this.widthList.remove(xRandom);
		this.heightList.remove(yRandom);
		return tmp;
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
			p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height * p.getScaleY() / ti.scaleY , true);
		}
		isFirst = false;
		isLoadBitmap = true;
		return 0;
	}
	private int leftNum = 0;
	public void draw(GL10 gl)
	{
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			isLoadBitmap = true;
		}
		
		
		frameCnt++;
		if (isON == false && frameCnt >= OFF_FRAME && leftNum == 0)
		{
			isON = true;
			frameCnt = 0;
			resetLiveOfPartilce(LINE);
			leftNum = particleList.size();
		}else if (isON == true && frameCnt >= ON_FRAME)
		{
			isON = false;
			leftNum = particleList.size();
			frameCnt = 0;
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
			
			if (isON == false && p.getLive() > 0 &&  p.getY() -Math.sin(Math.PI * p.getMoveDirectionAngle() / 180.0D) * offset*p.getSpeed() >mScreenHeight )
			{			
				p.setLive(p.getLive()-1);
				if (p.getLive() <= 0)
				{					
					tmp = getRandXY();
					p.setXY(tmp[0], tmp[1]-mScreenHeight * (i%3+1));
					leftNum--;
					if (leftNum <= 0)
					{
						frameCnt = 0;
					}
				}
			}

			
			if (p.getLive()>0)
			{
				float glPos[] = MatrixState.convertUVToGLpoint(p.getX(), p.getY() ,mScreenWidth, mScreenHeight);
				
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, p.getTextureId());
				
				MatrixState.pushMatrix();			
				MatrixState.translate(glPos[0], glPos[1], 3.0f);
				MatrixState.scale( p.getWidth() / UNIT, p.getHeight() / UNIT, 1);
				GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
				GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
				GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
				
				MatrixState.popMatrix();				
				if (!isStatic)
				{
					p.move(offset*p.getSpeed());				
				}
			}

		}
	}

	
	private void resetLiveOfPartilce(int n)
	{
		for (Particle p : particleList)
		{
			p.setLive(getRandomInt(n)+1);
		}
	}
	public void setFloatValue(float[] s,  float ...values)
	{
		for (int i = 0; i < values.length; i++)
		{
			s[i] = values[i];
		}
	}
	private Random random;
	private int getRandomInt(int n)
	{
		if (random == null)
		{
			random = new Random(System.nanoTime());
		}
		random.setSeed(System.nanoTime());
		return random.nextInt(n);
	}
	@Override
	public String getBackground() {
		return "bg_rain1";
	}
}
