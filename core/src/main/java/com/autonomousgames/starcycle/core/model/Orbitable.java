package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.math.Vector2;

public abstract class Orbitable {
	float t; // orbit parameter
	public Vector2 position;

	abstract void updatePosition(float delta);
}
