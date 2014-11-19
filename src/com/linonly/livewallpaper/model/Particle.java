package com.linonly.livewallpaper.model;

import com.linonly.livewallpaper.util.ShaderManager;


public  class Particle
{
	public static final float UNIT = 100f;
	private int textureId;
	private int type = 0;
	protected float x = 0.0f;
	protected float y = 0.0f;
	protected float scaleX = 1.0f;
	protected float scaleY = 1.0f;
	protected float speed;
	protected int angle = 0;	
	protected long drawTime = 0;
	protected float width= 0;
	protected float height = 0;
	protected int alpha = 255;
	protected int alphaSpeed=0; 
	
	protected float initX = 0.0f;
	protected float initY = 0.0f;	
	protected boolean relateWith = false;
	protected boolean relateHeight = false;	
	public int mScreenWidth;
	public int mScreenHeight;
	protected int moveDirectionAngle = 0;
	protected int live = 3;
	public Particle()
	{
		mScreenWidth = ShaderManager.getInstance().getScreenWidth();
		mScreenHeight = ShaderManager.getInstance().getScreenHeight();
	}
	
	public void setInitDiffXY(float initX, float initY, boolean rX, boolean rY)
	{
		this.initX  = initX;
		this.initY = initY;
		relateWith = rX;
		relateHeight = rY;
	}
	public void setInitDiffXY(float initX, float initY)
	{
		this.initX  = initX;
		this.initY = initY;
	}
	public void setInitDiffXY(boolean rX, boolean rY)
	{
		relateWith = rX;
		relateHeight = rY;
	}
	public void caculateRealXY(float bmWidth, float bmHeight)
	{
		if (relateWith)
		{
			x = x * bmWidth / UNIT;
		}
		if (relateHeight)
		{
			y = y * bmHeight /UNIT;
		}
		x += initX;
		y += initY;
		
	}
	
	public int getAlphaSpeed()
	{
		return alphaSpeed;
	}
	public void setAlphaSpeed(int alphaSpeed)
	{
		this.alphaSpeed = alphaSpeed;
	}
	public int getAlpha()
	{
		return alpha;
	}
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}
	public int getTextureId()
	{
		return textureId;
	}
	public float getX()
	{
		return x;
	}
	public float getY()
	{
		return y;
	}
	public float getScaleX()
	{
		return scaleX;
	}
	public float getScaleY()
	{
		return scaleY;
	}
	public float getSpeed()
	{
		return speed;
	}
	public int getAngle()
	{
		return angle;
	}
	public void setTextureId(int textureId)
	{
		this.textureId = textureId;
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public void setXY(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public void setScaleX(float scaleX)
	{
		this.scaleX = scaleX;
	}
	public void setScale(float scale)
	{
		this.scaleX = scale;
		this.scaleY = scale;
	}
	public void setScaleY(float scaleY)
	{
		this.scaleY = scaleY;
	}
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	public void setAngle(int angle)
	{
		this.angle = angle;
	}

	public float getWidth()
	{
		return width;
	}
	public float getHeight()
	{
		return height;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public void setBmParams(float width, float height, boolean needReCaculate)
	{
		this.width = width;
		this.height = height;
		
		if (needReCaculate)
			caculateRealXY(width, height);
	}
	
	public long getDrawTime()
	{
		return drawTime;
	}
	public void setDrawTime(long drawTime)
	{
		this.drawTime = drawTime;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getType()
	{
		return type;
	}

	public int getMoveDirectionAngle()
	{
		return moveDirectionAngle;
	}
	public void setMoveDirectionAngle(int moveDirectionAngle)
	{
		this.moveDirectionAngle = moveDirectionAngle;
	}
	public int getLive()
	{
		return live;
	}
	public void setLive(int n)
	{
		live = n;
	}
	//让子类重写
	public void move(float offset)
	{		
	}
}
