package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Layer extends Actor{

	Vector2 center = new Vector2();
	Vector2 size;
	public LayerType type;
	LayeredButton button;
    boolean specialDraw = true;
	
	public Layer(Vector2 relPos, Vector2 dims) {
		size = new Vector2(dims.x, dims.y);
		setOrigin(dims.x/2f, dims.y/2f);
		setCenter(relPos.x, relPos.y);
	}
	
	public void setCenter(float x, float y) {
		super.setPosition(x - size.x/2f, y - size.y/2f);
		this.center.set(x , y);
	}
	
	public void setCenter(Vector2 pos) {
		setCenter(pos.x, pos.y);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.center.set(x + size.x/2f, y + size.y/2f);
	}
	
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}
	
	@Override
	public void setColor(Color tint) {
		setLayerColor(tint);
	}
	
	abstract public void setLayerColor(Color tint);
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		assert false: "Use draw(SpriteBatch batch, float parentAlpha, Vector2 groupCenter";
	}
	
	public abstract void draw(SpriteBatch batch, float parentAlpha, Vector2 groupCenter, float groupAngle);
	
	public void flip(boolean x, boolean y) {
		assert false: "Only SpriteLayers should be flipped.";
	}
	
	public Vector2 getCenter() {
		return new Vector2(center);
	}
	
	// Sometimes the layer needs to be aware of the button, like with special draw conditions.
	public void setButton(LayeredButton button) {
		this.button = button;
	}
	
	// Any special layers should modulate the specialDraw flag or override this method.
	public boolean drawCondition() {
		return specialDraw;
	}
}
