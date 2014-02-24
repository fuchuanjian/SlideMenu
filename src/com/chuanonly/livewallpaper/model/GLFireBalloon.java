package cn.fu.slidemenu.model;

import android.graphics.Matrix;

public class GLFireBalloon extends Particle {

	private static final int DEGREES_MAX = 165;
	private int degrees = -45;
	private float mRadius = 10.0F;
	private Matrix matrix = new Matrix();
	private float originalXY[] = new float[2];
	private float destXY[] = new float[2];
	private float zeros[] = {0,0};
	public void setRotateRadius(int radius) {
		this.mRadius = radius;
	}
	
	@Override
	public void setXY(float x, float y) {
		super.setXY(x, y);
		originalXY[0] = x;
		originalXY[1] = y;
		destXY[0] = x;
		destXY[1] = y;
	}
	
	@Override
	public void move(float offset)
	{
		this.degrees += this.speed;
		this.matrix.reset();
		this.matrix.preRotate(-this.degrees);
		this.matrix.postTranslate(this.originalXY[0], this.originalXY[1]);
		this.matrix.postRotate(this.degrees, this.originalXY[0] - this.mRadius, this.originalXY[1] - this.mRadius);
		matrix.mapPoints(destXY, zeros);
		x = destXY[0];
		y = destXY[1];
		if (this.degrees > DEGREES_MAX || this.degrees < -45) {
			this.speed = -this.speed;
		} 
	}
	
	
	public float[] getDestXY() {
		return destXY;
	}

}
