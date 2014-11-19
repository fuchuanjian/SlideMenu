package com.linonly.livewallpaper.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.linonly.livewallpaper.MyApplication;
import com.linonly.livewallpaper.model.TextureInfo;

@SuppressLint("NewApi")
public class OpenGLUtils {

	public static int loadTexture(Bitmap bitmap) {
				int[] textures = new int[1];
				GLES20.glGenTextures
				(
						1,          
						textures,    
						0          
				); 
				int textureId = textures[0];    
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		        
		        int bitmapFormat = bitmap.getConfig() == Config.ARGB_8888 ? GLES20.GL_RGBA : GLES20.GL_RGB;
		    	if (bitmapFormat == GLES20.GL_RGBA)
		    	{
		    		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapFormat, bitmap, 0);
		    		
		    	}else {
		    		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		    	}
				
//			   	GLUtils.texImage2D
//			    (
//			    		GLES20.GL_TEXTURE_2D,  
//			     		0, 
//			     		GLUtils.getInternalFormat(bitmap), 
//			     		bitmap, //纹理图像
//			     		GLUtils.getType(bitmap), 
//			     		0  
//			     );   
//		        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//		        bitmap.recycle(); 		 	        
		        return textureId;
	}
	
	
	public static Bitmap decodeResource(int drawableId, float scale)
	{
		return decodeResource(drawableId, scale, scale, 0);
	}
	
	public static Bitmap decodeResource(int drawableId, float scaleX, float scaleY, int angle)
	{	
		Bitmap bitmap = null;
		InputStream is = MyApplication.getContext().getResources().openRawResource(drawableId);
		try {
	        	bitmap= BitmapFactory.decodeStream(is);
	        	Matrix matrix = new Matrix();
	        	matrix.setScale(scaleX, scaleY);
	        	Bitmap bitmapTmp = null;
	        	if (bitmap != null) {
	        		bitmapTmp  = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        		if (bitmap != bitmapTmp) {
	        			if (bitmap != null && !bitmap.isRecycled()) {
	        				bitmap.recycle();
	        				bitmap = null;
	        			}
	        		}
	        		
	        		bitmap = bitmapTmp;
	        		
	        	}
	        	
	        	if (angle != 0)
	        	{
	        		matrix.setRotate(-angle);
	        		Bitmap bitmapTmp2 = null;
	        		if (bitmap != null)  
	        		{
	        			bitmapTmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        			if (bitmap != bitmapTmp2) {
	        				if (bitmap != null && !bitmap.isRecycled()) {
	        					bitmap.recycle();
	        					bitmap = null;
	        				}
	        			}
	        			bitmap = bitmapTmp2;
	        		}
	        	}
	        } catch (OutOfMemoryError oom) {
				Log.e("fu", "OPENGL occur OOM.");
			}
	        finally {
	            try {
	                is.close();
	            } 
	            catch(IOException e) {
	                e.printStackTrace();
	            }
	        }		
		return bitmap;
	}
	
	public static Bitmap decodeResource(int drawableId)
	{
//		Bitmap bitmap = null;
//		  InputStream is = ClockWeatherApplication.getInstance().getResources().openRawResource(drawableId);
//	        try {
//	        	bitmap = BitmapFactory.decodeStream(is);
//	        } catch (OutOfMemoryError oom) {
//				Log.e("fu", "OPENGL occur OOM.");
//			}
//	        finally {
//	            try {
//	                is.close();
//	            } 
//	            catch(IOException e) {
//	                e.printStackTrace();
//	            }
//	        }		
		return decodeResource(drawableId , 1.0f, 1.0f, 0);
	}
	
	public static Bitmap decodeFile(String path)
	{
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeFile(path);
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return bitmap;		
	}


	public static void deleteTexture(ArrayList<TextureInfo> textureInfos)
	{
		if (textureInfos == null || textureInfos.size() == 0)
			return;
		
		int id[] = new int[textureInfos.size()];
		for (int i = 0; i < textureInfos.size(); i++)
		{
			int x = textureInfos.get(i).textureId;
			id[i] = x;			
		}
		GLES20.glDeleteTextures(0, id, 0);	
	}
}
