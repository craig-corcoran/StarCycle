package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MenuOrb extends FakeOrb {
    private TextureRegion image;
    public MenuOrb(TextureRegion image, Float radius, Vector2 position, float vpWidth, float vpHeight, boolean direction) {
        super(radius, position, vpWidth, vpHeight);
        this.velocity = new Vector2((float) Math.random() * 2f - 1, (float) Math.random() * 2f - 1).scl(13f);
        this.acceleration = (direction) ? new Vector2(-1f, 0f) : new Vector2(1f, 0f);
        this.image = image;
    }

    public MenuOrb(TextureRegion image, Float radius, Vector2 position, float vpWidth, float vpHeight, Vector2 velocity, Vector2 acceleration) {
        super(radius, position, vpWidth, vpHeight);
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.image = image;
    }

	public void draw(SpriteBatch batch) {
        batch.draw(image, position.x - radius, position.y - radius, radius,
					radius, 2f * radius, 2f * radius, 1, 1, angle);
	}
}
