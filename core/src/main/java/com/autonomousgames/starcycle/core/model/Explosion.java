package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Explosion {
	public float stateTime = 0;
	private final Animation animation;
	private final float frameDuration = 1f / 15f; // around the length of one
													// frame
	private final float radius;
	public final Vector2 position;

	public Explosion(float orbRadius, TextureRegion[] keyFrames, Vector2 pos) {
		radius = orbRadius * 3f;
		animation = new Animation(frameDuration, keyFrames);
		position = pos.cpy(); // need to make a copy here or bad things
								// happen...
	}

	public void draw(SpriteBatch batch, float timePassed,
			ArrayList<Explosion> explosions) {
		stateTime += timePassed;
		if (stateTime >= 16f * frameDuration) { // if all frames have been
												// displayed
			explosions.remove(this);
		} else {
			batch.draw(animation.getKeyFrame(stateTime, false), position.x
					- radius, position.y - radius, 2 * radius, 2 * radius);
		}
	}
}
