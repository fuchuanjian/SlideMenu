package cn.fu.slidemenu.model;

import java.util.Random;

public class ParticleThunderLight extends Particle
{
	@Override
	public void move(float offset)
	{
		int alpha = getAlpha()+getAlphaSpeed();
		if (alpha > 255 || alpha < -1000)
		{
			setAlphaSpeed(-getAlphaSpeed());
			if (alpha < -255)
			{
				setX(new Random().nextInt(mScreenWidth - (int)getWidth()));
			}
		}	
		setAlpha(alpha);
	}
}
