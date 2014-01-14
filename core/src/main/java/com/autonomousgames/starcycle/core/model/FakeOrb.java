package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class FakeOrb {

	final float radius;
	float angle = 0f;
	Vector2 position;
	Vector2 velocity;
	Vector2 acceleration;
	float vpWidth; // size of viewport (pixels or meters?)
	float vpHeight;
    public int age = 0;

    public FakeOrb(Float radius, Vector2 position, float vpWidth, float vpHeight) {
        this.radius = radius;
        this.position = position.cpy();
        this.vpWidth = vpWidth; // viewport width and height
        this.vpHeight = vpHeight;
    }

	public FakeOrb(Float radius, Vector2 position, Vector2 velocity, Vector2 acceleration, float vpWidth, float vpHeight) {
		this.radius = radius;
		this.position = position.cpy();
		this.vpWidth = vpWidth;
		this.vpHeight = vpHeight;
        this.velocity = velocity.cpy();
        this.acceleration = acceleration.cpy();
	}
    public abstract void draw(SpriteBatch batch);

    public boolean insideView() {
        return ((position.x > 0 - radius) & (position.y > 0 - radius)
                & (position.x < vpWidth + radius)
                & (position.y < vpHeight + radius));
    }

	public void update(float delta) {
        if (insideView()) {
            velocity.add(acceleration);
            position.add(velocity);
        }
	}

    public void move(float x, float y) {
        position.add(x, y);
    }

}
