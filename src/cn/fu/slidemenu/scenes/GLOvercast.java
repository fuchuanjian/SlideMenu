package cn.fu.slidemenu.scenes;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import cn.fu.slidemenu.R;
import cn.fu.slidemenu.model.Particle;
import cn.fu.slidemenu.model.ParticleRect;
import cn.fu.slidemenu.model.TextureInfo;
import cn.fu.slidemenu.model.WeatherType;
import cn.fu.slidemenu.util.CacheTextrue;
import cn.fu.slidemenu.util.MatrixState;
import cn.fu.slidemenu.util.OpenGLUtils;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLES20;

@SuppressLint("NewApi")
public class GLOvercast extends BaseScene
{
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	public GLOvercast()
	{
		setCategory(WeatherType.OVERCAST);
		init();
	}
	/**  初始化item  */
	private void init()
	{
	    particleList.clear();
	    
    	float cloudyscale = 1.6f;
 	    float cloudymaskscale = 1.6f;
 	    float cloudscale2 = mScreenWidth /480.0f;
  
 	    
 	   if ((mDensity > 0.0F) && (mDensity <= 1.0F)) {
//			localHashMap.put("cloudyscale", Float.valueOf(1.0F));
//			localHashMap.put("cloudymaskscale", Float.valueOf(0.8F));
 	    	cloudyscale = 1.0f;
 	 	    cloudymaskscale = 0.8f;
		} else if ((mDensity > 1.0F) && (mDensity <= 1.5F)) {
//			localHashMap.put("cloudyscale", Float.valueOf(2.0F));
//			localHashMap.put("cloudymaskscale", Float.valueOf(1.2F));
 	    	cloudyscale = 2.0f;
 	 	    cloudymaskscale = 1.2f;
		} else if (mDensity > 1.5F && mDensity <= 2.0F) {
//			localHashMap.put("cloudyscale", Float.valueOf(3.2F));
//			localHashMap.put("cloudymaskscale", Float.valueOf( 1.5F));
 	    	cloudyscale = 3.2f;
 	 	    cloudymaskscale = 1.5f;
		} else {
//			localHashMap.put("cloudyscale", Float.valueOf(3.6F));
//			localHashMap.put("cloudymaskscale", Float.valueOf(2.0F));
 	    	cloudyscale = 3.6f;
 	 	    cloudymaskscale = 2.0f;
		}
	 		Particle cloud1 = new ParticleRect();
	 		cloud1.setXY(0.0f, 0.0f);
	 		cloud1.setScale(cloudyscale);
	 		cloud1.setSpeed(80);
	 		cloud1.setType(0);
	 		particleList.add(cloud1);
	 		
	 		Particle cloud2 = new ParticleRect();
	 		cloud2.setXY(-200.0f, 0.0f);
	 		cloud2.setSpeed(50);
	 		cloud2.setType(1);
	 		cloud2.setScale(0.3f);
	 		particleList.add(cloud2);
	 		

	 		
	 		Particle cloud3 = new ParticleRect();
	 		cloud3.setXY(0.0f, 0.0f);
	 		cloud3.setSpeed(0);
	 		cloud3.setScale(cloudscale2);
	 		cloud3.setType(2);
	 		particleList.add(cloud3);	

	 		
	 		textureInfos.clear();
	 		textureInfos.add(new TextureInfo(R.drawable.overcast_cloud,cloudyscale));
	 		textureInfos.add(new TextureInfo(R.drawable.overcast_cloud, 0.3f));
	 		textureInfos.add(new TextureInfo(R.drawable.overcast_cloud_mask,cloudscale2));
	}
	/**  载入纹理  */
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
				p.setBmParams(ti.width *p.getScaleX() / ti.scaleX , ti.height * p.getScaleY() / ti.scaleY , true);    
		}

		isLoadBitmap = true;
		isFirst = false;
		return 0;
	}
	private long lastTime = 0;
	/** 每帧画图  */
	public void draw(GL10 gl)
	{
		if (isLoadBitmap == false)
		{
			loadGLTexture(gl);
			return;
		}
		//初始化
		// 制定使用某套shader程序
		GLES20.glUseProgram(mProgram);

//		// 将最终变换矩阵传入shader程序
//		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, maPositionHandle);
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, maTexCoorHandle);
		// 为画笔指定顶点纹理坐标数据
		GLES20.glVertexAttribPointer(maTexCoorHandle, 3, GLES20.GL_FLOAT, false, texture.length, mTexCoorBuffer);
		// 允许顶点位置数据数组
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);

		// 绑定纹理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//计算每帧之间的间隔
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
			// 为画笔指定顶点位置数据
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, itemVertices.length, mVertexBuffer);			
			
			MatrixState.pushMatrix();			
			MatrixState.translate(glPos[0], glPos[1], 3.0f);
			MatrixState.scale( p.getWidth()/UNIT,p.getHeight()/UNIT, 1);
			// 将最终变换矩阵传入shader程序
			GLES20.glUniform1f(mAlphaHandle, 1.0f * GLOBE_ALPHA);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			MatrixState.popMatrix();
		}	
	}

	
	


	@Override
	public String getBackground()
	{
		// TODO Auto-generated method stub
		return "bg_overcast";
	}
}
