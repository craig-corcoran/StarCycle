package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EllipticalPath implements PathType {

	private float a;
	private float b;
	private Vector2 center;

	public EllipticalPath(float a, float b, Vector2 center) {
		this.a = a;
		this.b = b;
		this.center = center;
	}

	@Override
	public Vector2 getPosition(float t, float startPercent) {
		float t0 = 2f * MathUtils.PI * startPercent;
		return new Vector2(a * MathUtils.cos(t + t0) + center.x, b
				* MathUtils.sin(t + t0) + center.y);
	}

}
