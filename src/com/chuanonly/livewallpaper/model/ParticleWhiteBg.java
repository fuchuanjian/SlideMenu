package com.chuanonly.livewallpaper.model;

public class ParticleWhiteBg extends Particle
{
	@Override
	public void move(float offset)
	{
		int alpha = getAlpha()+getAlphaSpeed();
		if (alpha < 0)
			alpha = 0;
		setAlpha(alpha);
	}
}
