package com.chuanonly.wallpaper2.model;

public class ParticleDrop extends Particle
{
	@Override
	public void move(float offset)
	{
		double offsetX = Math.sin(Math.PI * getAngle() / 180.0D) * offset;
		double offsetY = Math.cos(Math.PI * getAngle() / 180.0D) * offset;
		
		setX(getX() + (float)offsetX);
		setY(getY() + (float)offsetY);
		
		if (getY() > mScreenHeight)
		{
			setY(getY()-mScreenHeight-getHeight());
		}
	    if(getX() < 0-getWidth())
	    {
	    	setX(mScreenWidth);
	    }
	    if (getX() >mScreenWidth+getWidth())
	    {
	    	setX(0);
	    }
	}
}
