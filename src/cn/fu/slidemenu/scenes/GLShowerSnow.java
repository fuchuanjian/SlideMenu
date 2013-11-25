package cn.fu.slidemenu.scenes;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import cn.fu.slidemenu.R;
import cn.fu.slidemenu.model.Particle;
import cn.fu.slidemenu.model.ParticleRect;
import cn.fu.slidemenu.model.TextureInfo;
import cn.fu.slidemenu.model.WeatherType;
import cn.fu.slidemenu.util.CacheTextrue;
import cn.fu.slidemenu.util.MatrixState;
import cn.fu.slidemenu.util.OpenGLUtils;
public class GLShowerSnow extends BaseScene
{

	private ArrayList<Integer> heightList;
	private ArrayList<Integer> widthList;
	private ArrayList<Integer> angleList;
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	public GLShowerSnow()
	{
		setCategory(WeatherType.SNOW_SHOWER);
		init();
	}
	private void init()
	{
    	int snowNum1 = 40;
    	int  snowNum2 = 40;
    	int snowNum3 = 20;
    	int snowNum4 = 15;
    	int snowNum5 = 5;
    	int snowNum6 = 5;
    	int snowSpeed1 = 80;
    	int snowSpeed2 = 60;
    	int snowSpeed3 = 100;
    	int snowSpeed4 = 120;
    	int snowSpeed5 = 0;
    	int snowSpeed6 = 0;	
    	float snowActorScales = 1.0f;
	    if ((mScreenHeight > 0) && (mScreenHeight <= 400))
	    {
	    	
	    	snowNum1 = 40;
	    	snowNum2 = 40;
	    	snowNum3 = 20;
	    	snowNum4 = 15;
	    	snowNum5 = 5;
	    	snowNum6 = 5;
	    	snowSpeed1 = 80;
	    	snowSpeed2 = 60;
	    	snowSpeed3 = 100;
	    	snowSpeed4 = 120;
	    	snowSpeed5 = 0;
	    	snowSpeed6 = 0;

	    }else if ((400 < mScreenHeight) && (mScreenHeight <= 500)){
	    	snowNum1 = 30;
	    	snowNum2 = 15;
	    	snowNum3 = 15;
	    	snowNum4 = 10;
	    	snowNum5 = 0;
	    	snowNum6 = 0;
	    	snowSpeed1 = 100;
	    	snowSpeed2 = 60;
	    	snowSpeed3 = 120;
	    	snowSpeed4 = 160;
	    	snowSpeed5 = 0;
	    	snowSpeed6 = 0;
	 
	    }else if ((500 < mScreenHeight) && (mScreenHeight <= 1000)) {
	    	snowNum1 = 40;
	    	snowNum2 = 20;
	    	snowNum3 = 15;
	    	snowNum4 = 10;
	    	snowNum5 = 0;
	    	snowNum6 = 0;
	    	snowSpeed1 = 100;
	    	snowSpeed2 = 60;
	    	snowSpeed3 = 120;
	    	snowSpeed4 = 200;
	    	snowSpeed5 = 0;
	    	snowSpeed6 = 0;
	     } else if (mScreenHeight > 1000){
		    	snowNum1 = 50;
		    	snowNum2 = 40;
		    	snowNum3 = 30;
		    	snowNum4 = 20;
		    	snowNum5 = 0;
		    	snowNum6 = 0;
		    	snowSpeed1 = 140;
		    	snowSpeed2 = 100;
		    	snowSpeed3 = 180;
		    	snowSpeed4 = 220;
		    	snowSpeed5 = 0;
		    	snowSpeed6 = 0;
	  }
	      
	      
	      if ((mDensity <= 0.0F) || (mDensity > 1.0F))
	    	  snowActorScales = 0.8f;
	      else if ((mDensity > 1.0F) && (mDensity <= 1.5F))
	    	  snowActorScales = 1.0f;
	      else if (mDensity > 1.5F)
	    	  snowActorScales = 1.5f;
	      
		int rand = new Random().nextInt(30) +75;
			
		for (int i =0; i < snowNum1; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed1);
			p.setType(0);
			particleList.add(p);		
		}
		for (int i =0; i < snowNum2; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed2);
			p.setType(1);
			particleList.add(p);		
		}
		for (int i =0; i < snowNum3; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed3);
			p.setType(2);
			particleList.add(p);		
		}
		for (int i =0; i < snowNum4; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed4);
			p.setType(3);
			particleList.add(p);		
		}
		for (int i =0; i < snowNum5; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed5);
			p.setType(4);
			particleList.add(p);		
		}
		for (int i =0; i < snowNum6; i++)
		{
			Particle p = new ParticleRect();
			int pos[] = getRandXY();
			p.setXY(pos[0], pos[1]);
			p.setScale(snowActorScales);
			int randAngle = getRandAngle();
			p.setAngle(randAngle);
			p.setMoveDirectionAngle(randAngle - 90);
			p.setSpeed(snowSpeed6);
			p.setType(5);
			particleList.add(p);		
		}
		
		textureInfos.clear();
		textureInfos.add(new TextureInfo(R.drawable.snowflake_tiny, snowActorScales, snowActorScales, 0));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_s, snowActorScales, snowActorScales, 0));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_m, snowActorScales, snowActorScales, 0));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_l, snowActorScales, snowActorScales, 0));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_xl, snowActorScales, snowActorScales, 0));
		textureInfos.add(new TextureInfo(R.drawable.snowflake_xxl, snowActorScales, snowActorScales, 0));

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
	public int getRandAngle()
	{
		int angle = 90;
		if (this.angleList == null)
			this.angleList = new ArrayList<Integer>();
		if (this.angleList.size() == 0) {
			for (int i = 75; i < 106 ; i++)
				this.angleList.add(Integer.valueOf(i));
		}
		int index = new Random().nextInt(angleList.size());
		angle = ((Integer) this.angleList.get(index)).intValue();
		angleList.remove(index);
		return angle -90;
	}
	
	public int loadGLTexture(GL10 gl)
	{
		// 载入背景图片
		
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
				p.setBmParams(ti.width *p.getScaleX() /ti.scaleX , ti.height  * p.getScaleY() /ti.scaleY, false);
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
			MatrixState.scale( p.getWidth() / UNIT, p.getHeight() / UNIT, 1);
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
