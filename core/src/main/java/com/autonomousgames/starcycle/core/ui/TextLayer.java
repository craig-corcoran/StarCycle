package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TextLayer extends Layer{
	
	Vector2 cacheDims;
	TransformText cache;
	Texture originalTexture;
	Sprite textSprite;
	boolean dispOnce = true;
    boolean changed = false;
	Vector2 buttonCenter = new Vector2(0f, 0f);

	public TextLayer(BitmapFont font, java.lang.CharSequence text, Vector2 relPos) {
		super(relPos, new Vector2(0f, 0f));
		originalTexture = font.getRegion().getTexture();
		textSprite = new Sprite(originalTexture); 
		cache = new TransformText(new BitmapFont(font.getData(), textSprite, font.usesIntegerPositions()));
		cache.setText(text, 0, 0);
		cacheDims = new Vector2(cache.getBounds().width, cache.getBounds().height);
		cache.setPosition(MathUtils.round(- cacheDims.x/2f), MathUtils.round(cacheDims.y/2f));
	}
	
	public TextLayer(BitmapFont font, java.lang.CharSequence text) {
		this(font, text, new Vector2(0f, 0f));
	}
	
	public TextLayer(BitmapFont font, java.lang.CharSequence text, Vector2 relPos, Color color, float angle) {
		this(font, text, relPos);
		setLayerColor(color);
		setRotation(angle);
	}

	@Override
	public void rotate(float angle) {
		cache.rotate(angle);
        changed = true;
	}
	
	@Override
	public void setRotation(float angle) {
		rotate(angle - cache.getRotation());
	}
	
	@Override
	public float getRotation() {
		return cache.getRotation();
	}
	
	@Override
	public void setLayerColor(Color tint) {
		cache.setColor(tint);
	}
	
	@Override
	public Color getColor() {
		return cache.getColor();
	}
	
	public TextLayer setTextColor(Color tint) {
		setLayerColor(tint);
		return this;
	}
	
	public TextLayer rotateText(float angle) {
		rotate(angle);
		return this;
	}
	
	public TextLayer setTextAlpha(float a) {
		Color color = cache.getColor();
		cache.setColor(color.r, color.g, color.b, color.a * a);
		return this;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha, Vector2 groupCenter, float groupAngle) {
		// We shouldn't recalculate text positions unless something changes
		if (!buttonCenter.equals(groupCenter) || changed) {
			cache.setPosition(MathUtils.round(groupCenter.x + center.x - cacheDims.x / 2f), MathUtils.round(groupCenter.y + center.y + cacheDims.y / 2f));
			buttonCenter.set(groupCenter.x, groupCenter.y);
            changed = false;
		}
		cache.draw(batch, parentAlpha);
	}

}
