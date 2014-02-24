package cn.fu.slidemenu.model;

import java.util.Random;

public class ParticleMetor extends Particle
{
	@Override
	public void move(float offset)
	{
		double offsetX = Math.sin(Math.PI * getAngle() / 180.0D) * offset;
		double offsetY = Math.cos(Math.PI * getAngle() / 180.0D) * offset;
		
		setX(getX() + (float)offsetX);
		setY(getY() + (float)offsetY);
		int alpha = getAlpha()+getAlphaSpeed();
		if (alpha > 300)
		{
			alpha = 300;
		}
		setAlpha(alpha);
	    if (getX() > (new Random().nextInt(60)+4)* mScreenWidth) // 60分之1的概率
	    {
	    	setX(- new Random().nextInt(mScreenWidth)/4);
	    	setY(new Random().nextInt(mScreenHeight)/2);
	    	setAlpha(30);
	    }
	}
}
