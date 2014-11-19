package com.linonly.livewallpaper.util;
import android.opengl.Matrix;
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] mMVPMatrix  = new float[16];
    
    private static float[] mMMatrix;
    private static float point[] = new float[2];
    private static float[][] mStack=new float[5][16];
    private  static int stackTop = 1;
    private static Object lock = new Object();
    
    public static void setInitStack()
    {
    	mMMatrix = new float[16]; 
    	Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    }
    public synchronized static void initStack()
    {
    	stackTop = 1;
    }
    
    
    public static void pushMatrix()
    {
    	synchronized (lock)
		{
    		if (stackTop < 5)
    		{    			
    			stackTop++;
    			for(int i=0;i<16;i++)
    			{
    				mStack[stackTop][i]=mMMatrix[i];
    			}			
    		}
		}

    }
    
    public static void popMatrix()
    {
    	synchronized (lock)
		{			
    		if (stackTop >= 0)
    		{    			
    			for(int i=0;i<16;i++)
    			{
    				mMMatrix[i]=mStack[stackTop][i];
    			}
    			stackTop--;
    		}
		}
    }
    
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(mMMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(mMMatrix,0,angle,x,y,z);
    }
    public static void scale(float x,float y,float z)
    {
    	Matrix.scaleM(mMMatrix,0, x, y, z);
    }
    
    public static void setCamera
    (
    		float cx,	 
    		float cy,    
    		float cz,    
    		float tx,   
    		float ty,   
    		float tz,   
    		float upx, 
    		float upy, 
    		float upz  
    )
    {
    	Matrix.setLookAtM
        (
        		mVMatrix, 
        		0, 
        		cx,
        		cy,
        		cz,
        		tx,
        		ty,
        		tz,
        		upx,
        		upy,
        		upz
        );
    }
    
    public static void setProject
    (
    	float left,		 
    	float right,    
    	float bottom,  
    	float top,     
    	float near,		 
    	float far     
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
    public static void setorthoM
    (
    	float left,		
    	float right,    
    	float bottom,   
    	float top,    
    	float near,	
    	float far      
    )
    {
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
   
    public static float[] getFinalMatrix()
    {
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
	public static float[] convertUVToGLpoint(float x, float y , int mScreenWidth, int mScreenHeight)
	{
		point[0] = -1 + 2* ((float) x/mScreenWidth);
		point[1] = 1 - 2*((float) y/mScreenHeight);
		return point;
	}
}
