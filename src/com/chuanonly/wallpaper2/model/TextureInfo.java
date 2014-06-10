package com.chuanonly.wallpaper2.model;




public class TextureInfo
{
	public int drawableId;
	public int textureId;
	public float width;
	public float height;
	public float scaleX = 1.0f;
	public float scaleY = 1.0f;
	public int angle = 0;
	
	public TextureInfo()
	{
	}
	public TextureInfo(int drawableId)
	{
		this.drawableId = drawableId;
	}
	
	public TextureInfo(int drawableId, float scale)
	{
		this.drawableId = drawableId;
		scaleX = scale;
		scaleY = scale;
	}
	public TextureInfo(int drawableId, float scaleX, float scaleY)
	{
		this.drawableId = drawableId;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	public TextureInfo(int drawableId, float scaleX, float scaleY ,int angle)
	{
		this.drawableId = drawableId;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.angle = angle;
	}
	
}
