package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ImageOrb extends FakeOrb {
    SpriteLayer sprite;
    private TextureRegion image;
    public ImageOrb(TextureRegion image, Float radius, Vector2 position, float vpWidth, float vpHeight, boolean direction) {
        super(radius, position, vpWidth, vpHeight);

        // random velocity and acceleration
        this.velocity = new Vector2((float) Math.random() * 2f - 1, (float) Math.random() * 2f - 1).scl(13f);
        this.acceleration = (direction) ? new Vector2(-1f, 0f) : new Vector2(1f, 0f);
        sprite = new SpriteLayer(image, new Vector2(vpWidth, vpWidth).scl(1/ StarCycle.pixelsPerMeter));
    }

    public ImageOrb(TextureRegion image, Float radius, Vector2 position, float vpWidth, float vpHeight, Vector2 velocity, Vector2 acceleration) {
        super(radius, position, velocity, acceleration, vpWidth, vpHeight);
        sprite = new SpriteLayer(image, new Vector2(vpWidth, vpWidth).scl(1/ StarCycle.pixelsPerMeter));
    }

	public void draw(SpriteBatch batch) {
        sprite.draw(batch, 1f, position, angle);
	}

    public ImageOrb tint(Color color) {
        sprite.setLayerColor(color);
        return this;
    }
}
