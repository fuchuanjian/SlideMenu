package com.chuanonly.wallpaper.scenes;

import javax.microedition.khronos.opengles.GL10;

import com.chuanonly.wallpaper.model.WeatherType;

public class GLNAScene extends BaseScene {

	public GLNAScene()
	{
		setCategory(WeatherType.NA_SCENE);
	}
	@Override
	public int loadGLTexture(GL10 gl) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(GL10 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBackground() {
		return "bg_fine_day";
	}

}
