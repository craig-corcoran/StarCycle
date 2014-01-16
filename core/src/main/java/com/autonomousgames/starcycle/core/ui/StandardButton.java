package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class StandardButton extends LayeredButton {

	public StandardButton (Vector2 center, Vector2 size, TextureRegion texture, float padding) {
		super(center, size.cpy().add(padding * 2f, padding * 2f));
		Vector2 gradSize = new Vector2(Math.min(size.x, size.y), Math.min(size.x, size.y)).scl(1.5f);
		this.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, gradSize), LayerType.DOWN);
		this.addLayer(new SpriteLayer(texture, size));
	}
	
	@Override
	public void setColor(Color tint) {
		layers.get(1).setColor(tint);
	}
	
	@Override
	public void setAllColors(Color tint) {
		for (int i = 1; i < layers.size(); i ++) {
			setLayerColor(tint, i);
		}
	}
}
