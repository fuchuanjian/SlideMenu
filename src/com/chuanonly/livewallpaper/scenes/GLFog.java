package cn.fu.slidemenu.scenes;

import java.util.ArrayList;

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

public class GLFog extends BaseScene
{
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	
  	public GLFog()
  	{
  		setCategory(WeatherType.FOG);
		init();
  	}
  	
  	private void init()
	{
  		particleList.clear();
	    
	    float scaleTop = 1.0f;
	    float scaleBottom = 1.0f;
	    if (mScreenHeight > 0 && mScreenHeight <= 400)
	    {
	    	scaleTop = scaleBottom = 0.8f;
	    }else if (400 < mScreenHeight && mScreenHeight <= 500)
	    {
	    	scaleTop = scaleBottom = 1.2f;
	    }else if (500<mScreenHeight && mScreenHeight <= 1000){
			scaleTop = scaleBottom = 1.5f;
		}else if (mScreenHeight > 1000)
		{
			scaleBottom = scaleTop = 2.0f;
		}
	    
	    
	    
	    Particle fogTop = new ParticleRect();
	    fogTop.setScale(scaleTop);
	    fogTop.setXY(0.0f, 0.0f);
	    fogTop.setSpeed(30);
	    fogTop.setType(0);
	    particleList.add(fogTop);
	    
	    
	    Particle fogBottom = new ParticleRect();
	    fogBottom.setScale(scaleBottom);
	    fogTop.setType(1);
	    fogBottom.setXY(0.0f, 0.3f * mScreenHeight);
	    fogBottom.setSpeed(50);
	    particleList.add(fogBottom);
	    
	    
	    textureInfos.clear();
 		textureInfos.add(new TextureInfo(R.drawable.fog_cloud_1,scaleTop));
 		textureInfos.add(new TextureInfo(R.drawable.fog_cloud_2,scaleBottom));
		
	}

	@Override
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
				p.setBmParams(ti.width * p.getScaleX() /ti.scaleX , ti.height * p.getScaleY() /ti.scaleY, true);    
		}
		isFirst  = false;
		isLoadBitmap = true;
		return 0;
	}

	@Override
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
			MatrixState.scale( p.getWidth()/ UNIT, p.getHeight() / UNIT , 1);
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			MatrixState.popMatrix();

		}	
	}

	@Override
	public String getBackground() {
		return "bg_fog";
	}
}
