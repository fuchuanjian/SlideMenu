package com.chuanonly.wallpaper2.scenes;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.chuanonly.wallpaper2.R;
import com.chuanonly.wallpaper2.model.GLSnowFall;
import com.chuanonly.wallpaper2.model.TextureInfo;
import com.chuanonly.wallpaper2.model.WeatherType;
import com.chuanonly.wallpaper2.util.CacheTextrue;
import com.chuanonly.wallpaper2.util.MatrixState;
import com.chuanonly.wallpaper2.util.OpenGLUtils;

public class GLSnowScene extends BaseScene {

	private ArrayList<Integer> snowFlakeAngleArray;
	private ArrayList<Integer> snowPosxArray;
	private ArrayList<Integer> snowPosyArray;
	
	private ArrayList<GLSnowFall> GLSnowFallList = new ArrayList<GLSnowFall>();

	public final int SNOW_LIGHT = 1;
	public int myMode = 1;
	public GLSnowScene(int type)
	{
		setCategory(type);
		init(type);
	}
	private void init(int sceneCategory)
	{
		int SnowNum1 = 70;
		int SnowNum2 = 50;
		int SnowNum3 = 50;
		int SnowNum4 = 40;
		int SnowNum5 = 80;
		int SnowNum6 = 20;
		int SnowSpeed1 = 140;
		int SnowSpeed2 = 100;
		int SnowSpeed3 = 180;
		int  SnowSpeed4 = 260;
		int  SnowSpeed5 = 220;
		int  SnowSpeed6 = 380;
	    float SnowActorScales =1.0f ;
		switch (sceneCategory) {
		case WeatherType.SNOW_LIGHT:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400))
		    {
				SnowNum1 = 40;
			    SnowNum2 = 40;
			    SnowNum3 = 20;
			    SnowNum4 = 15;
			    SnowNum5 = 5;
			    SnowNum6 = 5;
			    SnowSpeed1 = 80;
			    SnowSpeed2 = 60;
			    SnowSpeed3 = 100;
			    SnowSpeed4 = 120;
			    SnowSpeed5 = 0;
			    SnowSpeed6 = 0;

		    }else if ((400 < mScreenHeight) && (mScreenHeight <= 500)){
				SnowNum1 = 30;
			    SnowNum2 = 15;
			    SnowNum3 = 15;
			    SnowNum4 = 10;
			    SnowNum5 = 0;
			    SnowNum6 = 0;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 60;
			    SnowSpeed3 = 120;
			    SnowSpeed4 = 160;
			    SnowSpeed5 = 0;
			    SnowSpeed6 = 0;
		    }else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
				SnowNum1 = 40;
			    SnowNum2 = 20;
			    SnowNum3 = 20;
			    SnowNum4 = 10;
			    SnowNum5 = 0;
			    SnowNum6 = 0;
			    SnowSpeed1 = 120;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 200;
			    SnowSpeed5 = 0;
			    SnowSpeed6 = 0;
		     } else if (mScreenHeight > 1000){
				SnowNum1 = 50;
			    SnowNum2 = 40;
			    SnowNum3 = 30;
			    SnowNum4 = 20;
			    SnowNum5 = 0;
			    SnowNum6 = 0;
			    SnowSpeed1 = 140;
			    SnowSpeed2 = 100;
			    SnowSpeed3 = 180;
			    SnowSpeed4 = 220;
			    SnowSpeed5 = 0;
			    SnowSpeed6 = 0;
		  }	      
		      if ((mDensity <= 0.0F) || (mDensity > 1.0F))
		    	  SnowActorScales = 0.8f;
		      else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
		    	  SnowActorScales = 1.0f;
		      else if (mDensity > 1.5F)
		    	  SnowActorScales =1.5f;
		      
		      
			break;
		case WeatherType.SNOW_MODERATE:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400))
		    {
				SnowNum1 = 40;
			    SnowNum2 = 40;
			    SnowNum3 = 20;
			    SnowNum4 = 15;
			    SnowNum5 = 5;
			    SnowNum6 = 5;
			    SnowSpeed1 = 80;
			    SnowSpeed2 = 60;
			    SnowSpeed3 = 100;
			    SnowSpeed4 = 120;
			    SnowSpeed5 = 220;
			    SnowSpeed6 = 260;

		    }else if ((400 < mScreenHeight) && (mScreenHeight <= 500)){
				SnowNum1 = 50;
			    SnowNum2 = 30;
			    SnowNum3 = 30;
			    SnowNum4 = 25;
			    SnowNum5 = 0;
			    SnowNum6 = 0;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 60;
			    SnowSpeed3 = 120;
			    SnowSpeed4 = 160;
			    SnowSpeed5 = 280;
			    SnowSpeed6 = 300;
		    }else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
				SnowNum1 = 60;
			    SnowNum2 = 40;
			    SnowNum3 = 40;
			    SnowNum4 = 30;
			    SnowNum5 = 8;
			    SnowNum6 = 8;
			    SnowSpeed1 = 120;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 200;
			    SnowSpeed5 = 320;
			    SnowSpeed6 = 360;
		     } else if (mScreenHeight > 1000){
				SnowNum1 = 70;
			    SnowNum2 = 50;
			    SnowNum3 = 50;
			    SnowNum4 = 40;
			    SnowNum5 = 12;
			    SnowNum6 = 12;
			    SnowSpeed1 = 140;
			    SnowSpeed2 = 100;
			    SnowSpeed3 = 180;
			    SnowSpeed4 = 220;
			    SnowSpeed5 = 340;
			    SnowSpeed6 = 380;
		  }	      
		      if ((mDensity <= 0.0F) || (mDensity > 1.0F))
		    	  SnowActorScales = 0.8f;
		      else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
		    	  SnowActorScales = 1.0f;
		      else if (mDensity > 1.5F)
		    	  SnowActorScales =1.5f;
			break;
		case WeatherType.SNOW_HEAVY:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400))
		    {
				SnowNum1 = 50;
			    SnowNum2 = 40;
			    SnowNum3 = 40;
			    SnowNum4 = 40;
			    SnowNum5 = 60;
			    SnowNum6 = 10;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 260;
			    SnowSpeed5 = 200;
			    SnowSpeed6 = 270;

		    }else if ((400 < mScreenHeight) && (mScreenHeight <= 500)){
				SnowNum1 = 50;
			    SnowNum2 = 40;
			    SnowNum3 = 40;
			    SnowNum4 = 40;
			    SnowNum5 = 60;
			    SnowNum6 = 10;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 260;
			    SnowSpeed5 = 200;
			    SnowSpeed6 = 300;
		    }else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
				SnowNum1 = 60;
			    SnowNum2 = 40;
			    SnowNum3 = 40;
			    SnowNum4 = 40;
			    SnowNum5 = 80;
			    SnowNum6 = 15;
			    SnowSpeed1 = 120;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 260;
			    SnowSpeed5 = 200;
			    SnowSpeed6 = 360;
		     } else if (mScreenHeight > 1000){
				SnowNum1 = 70;
			    SnowNum2 = 50;
			    SnowNum3 = 50;
			    SnowNum4 = 40;
			    SnowNum5 = 80;
			    SnowNum6 = 20;
			    SnowSpeed1 = 140;
			    SnowSpeed2 = 100;
			    SnowSpeed3 = 180;
			    SnowSpeed4 = 260;
			    SnowSpeed5 = 220;
			    SnowSpeed6 = 380;
		  }	      
		      if ((mDensity <= 0.0F) || (mDensity > 1.0F))
		    	  SnowActorScales = 0.8f;
		      else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
		    	  SnowActorScales = 1.0f;
		      else if (mDensity > 1.5F)
		    	  SnowActorScales =1.5f;
			break;
		case WeatherType.SNOW_STORM:
			if ((mScreenHeight > 0) && (mScreenHeight <= 400))
		    {
				SnowNum1 = 60;
			    SnowNum2 = 60;
			    SnowNum3 = 50;
			    SnowNum4 = 50;
			    SnowNum5 = 10;
			    SnowNum6 = 15;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 230;
			    SnowSpeed5 = 200;
			    SnowSpeed6 = 300;

		    }else if ((400 < mScreenHeight) && (mScreenHeight <= 500)){
				SnowNum1 = 60;
			    SnowNum2 = 60;
			    SnowNum3 = 50;
			    SnowNum4 = 60;
			    SnowNum5 = 10;
			    SnowNum6 = 15;
			    SnowSpeed1 = 100;
			    SnowSpeed2 = 80;
			    SnowSpeed3 = 160;
			    SnowSpeed4 = 230;
			    SnowSpeed5 = 200;
			    SnowSpeed6 = 330;
		    }else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
				SnowNum1 = 100;
			    SnowNum2 = 80;
			    SnowNum3 = 50;
			    SnowNum4 = 50;
			    SnowNum5 = 20;
			    SnowNum6 = 10;
			    SnowSpeed1 = 200;
			    SnowSpeed2 = 120;
			    SnowSpeed3 = 200;
			    SnowSpeed4 = 250;
			    SnowSpeed5 = 400;
			    SnowSpeed6 = 500;
		     } else if (mScreenHeight > 1000){
				SnowNum1 = 140;
			    SnowNum2 = 80;
			    SnowNum3 = 50;
			    SnowNum4 = 80;
			    SnowNum5 = 50;
			    SnowNum6 = 10;
			    SnowSpeed1 = 250;
			    SnowSpeed2 = 160;
			    SnowSpeed3 = 250;
			    SnowSpeed4 = 250;
			    SnowSpeed5 = 400;
			    SnowSpeed6 = 500;
		  }	      
		      if ((mDensity <= 0.0F) || (mDensity > 1.0F))
		    	  SnowActorScales = 0.8f;
		      else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
		    	  SnowActorScales = 1.0f;
		      else if (mDensity > 1.5F)
		    	  SnowActorScales =1.5f;
			break;
		}
	    
		for (int i = 0; i < SnowNum1; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed1);
			p.setType(0);
			GLSnowFallList.add(p);
		}
		for (int i = 0; i < SnowNum2; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed2);
			p.setType(1);
			GLSnowFallList.add(p);
		}
		for (int i = 0; i < SnowNum3; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed3);
			p.setType(2);
			GLSnowFallList.add(p);
		}
		for (int i = 0; i < SnowNum4; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed4);
			p.setType(3);
			GLSnowFallList.add(p);
		}
		for (int i = 0; i < SnowNum5; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed5);
			p.setType(4);
			GLSnowFallList.add(p);
		}
		for (int i = 0; i < SnowNum6; i++) {
			GLSnowFall p = new GLSnowFall();
			p.setXY(getRandomPosX(), getRandomPosY());
			p.setScale(SnowActorScales);
			p.setTrackAngle(getRandomAngle());
			p.setSpeed(SnowSpeed6);
			p.setType(5);
			GLSnowFallList.add(p);
		}
		
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.snowflake_tiny, SnowActorScales, SnowActorScales));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_s, SnowActorScales, SnowActorScales));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_m, SnowActorScales, SnowActorScales));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_l, SnowActorScales, SnowActorScales));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_xl, SnowActorScales, SnowActorScales));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_xxl, SnowActorScales, SnowActorScales));
	}
	
	protected int getRandomAngle()
	  {
	    if (this.snowFlakeAngleArray == null)
	      this.snowFlakeAngleArray = new ArrayList();
	    if (this.snowFlakeAngleArray.size() == 0)
	      for (int k = 75; k < 106; k++)
	        this.snowFlakeAngleArray.add(Integer.valueOf(k));
	    int i = (int)(Math.random() * this.snowFlakeAngleArray.size());
	    int j = ((Integer)this.snowFlakeAngleArray.get(i)).intValue();
	    this.snowFlakeAngleArray.remove(i);
	    return j;
	  }

	  protected int getRandomPosX()
	  {
	    if (this.snowPosxArray == null)
	      this.snowPosxArray = new ArrayList();
	    if (this.snowPosxArray.size() == 0)
	      for (int k = 0; k < 20 + mScreenWidth; k++)
	        this.snowPosxArray.add(Integer.valueOf(k));
	    int i = (int)(Math.random() * this.snowPosxArray.size());
	    int j = ((Integer)this.snowPosxArray.get(i)).intValue();
	    this.snowPosxArray.remove(i);
	    return j;
	  }

	  protected int getRandomPosY()
	  {
	    if (this.snowPosyArray == null)
	      this.snowPosyArray = new ArrayList();
	    if (this.snowPosyArray.size() == 0)
	      for (int k = -10; k < mScreenHeight; k++)
	        this.snowPosyArray.add(Integer.valueOf(k));
	    int i = (int)(Math.random() * this.snowPosyArray.size());
	    int j = ((Integer)this.snowPosyArray.get(i)).intValue();
	    this.snowPosyArray.remove(i);
	    return j;
	  }
	
	public int loadGLTexture(GL10 gl)
	{
		
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
		for (GLSnowFall p : GLSnowFallList)
		{			
			TextureInfo ti = textureInfos.get(p.getType());
			p.setTextureId(ti.textureId);
			if (isFirst)
				p.setBmParams(ti.width * p.getScaleX()/ti.scaleX , ti.height*p.getScaleY()/ti.scaleY , true);
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
		
		for (int i=0; i<GLSnowFallList.size(); i++)
		{
			GLSnowFall p = GLSnowFallList.get(i);
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
			MatrixState.scale( p.getWidth()/UNIT, p.getHeight()/UNIT, 1);
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
		return "bg_snow";
	}
}
