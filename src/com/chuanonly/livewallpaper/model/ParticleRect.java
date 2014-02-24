package com.chuanonly.livewallpaper.model;


public class ParticleRect extends Particle
{

	@Override
	public void move(float offset)
	{
		if (moveDirectionAngle == 0)
		{
			moveLeft(offset);
			return;
		}
		
		double offsetX = Math.cos(Math.PI * moveDirectionAngle/ 180.0D) * offset;
		double offsetY = -Math.sin(Math.PI * moveDirectionAngle / 180.0D) * offset;
		
		setX(getX() + (float)offsetX);
		if (getX() > mScreenWidth && Math.abs(moveDirectionAngle) <= 90)
		{
			setX(getX()-mScreenWidth-getWidth());
		}
		if (getX() < -getWidth() && Math.abs(moveDirectionAngle) > 90)
		{
			setX(getX()+mScreenWidth+getWidth());
		}
		
		setY(getY() + (float)offsetY);
		if (getY() > mScreenHeight && moveDirectionAngle < 0)
		{
			setY(getY()-mScreenHeight-getHeight());
		}
	    if(getY() < -getHeight() && moveDirectionAngle > 0)
	    {
	    	setY(getY() + mScreenHeight + getHeight());
	    }
		
	}

	private void moveLeft(float offset)
	{
		setX(getX() + offset);
		if (getX() > mScreenWidth)
		{
			setX(getX()-mScreenWidth-getWidth());
		}
	}
	
}
