package com.chuanonly.wallpaper3.model;

public class GLSnowFall extends Particle {

	private int trackAngle;
	
	public void setTrackAngle(int trackAngle) {
		this.trackAngle = trackAngle;
	}
	public int getTrackAngle() {
		return trackAngle;
	}
	@Override
	public void move(float offset)
	{
		double offsetX = Math.cos(Math.PI * getTrackAngle() / 180.0D) * offset;
		double offsetY = Math.sin(Math.PI * getTrackAngle() / 180.0D) * offset;
		
		setX(getX() + (float)offsetX);
		setY(getY() + (float)offsetY);
		
		if (getY() > mScreenHeight+getHeight())
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
