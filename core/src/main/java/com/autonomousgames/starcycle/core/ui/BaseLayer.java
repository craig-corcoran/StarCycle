package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BaseLayer extends SpriteLayer {
	public float phase;
	public float speed;
	public Color color;

	public BaseLayer(TextureRegion image, Vector2 center, Vector2 size, float phase, float speed, Color color) {
		super(image, center, size);
		this.phase = phase;
		this.speed = speed;
		this.color = color;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		this.phase += delta * this.speed;
	}
	
	
}
