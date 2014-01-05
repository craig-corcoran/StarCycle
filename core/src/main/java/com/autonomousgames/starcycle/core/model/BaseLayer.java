package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BaseLayer {
	public TextureRegion texture;
	public float phase;
	public float speed;
	public float scale;

	public BaseLayer(TextureRegion texture, float phase, float speed,
			float scale) {
		this.texture = texture;
		this.phase = phase;
		this.speed = speed;
		this.scale = scale;
	}
}