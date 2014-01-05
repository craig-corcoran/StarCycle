package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class DoubleEllipticalPath implements PathType {

	private float a1;
	private float a2;
	private float b1;
	private float b2;
	private float t02;
	private float dt2co;
	private Vector2 center;

	public DoubleEllipticalPath(float a1, float b1, Vector2 center, float a2,
			float b2, float t02, float dt2co) {
		this.a1 = a1;
		this.b1 = b1;
		this.center = center;
		this.a2 = a2;
		this.b2 = b2;
		this.t02 = t02;
		this.dt2co = dt2co;
	}

	@Override
	public Vector2 getPosition(float t, float startPercent) {
		float t0 = 2f * MathUtils.PI * startPercent;
		return new Vector2(a1 * MathUtils.cos(t + t0) + a2
				* MathUtils.cos(dt2co * t + t02) + center.x, b1
				* MathUtils.sin(t + t0) + b2 * MathUtils.sin(dt2co * t + t02)
				+ center.y);
	}

}
