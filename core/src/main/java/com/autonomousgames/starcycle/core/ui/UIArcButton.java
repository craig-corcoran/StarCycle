package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UIArcButton extends UIButton {
	
	final Vector2 center;
	final Vector2 angles; // min, max
	final Vector2 radii;  // min, max
	
	private Vector2 diffVec = new Vector2(0f, 0f);
	private float theta;
	
	public UIArcButton(Vector2 corner, Vector2 angles, Vector2 radii){
		super(corner, new Vector2(radii.y*2f, radii.y*2f), 0f);
		center = new Vector2(radii.y, radii.y);
		this.angles = angles;
		this.radii = radii;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable) {
			diffVec.set(x - center.x, y - center.y);
			float dist = diffVec.len();
			if ((dist > radii.x) & (dist < radii.y)) {
				theta = diffVec.angle();
				return ((theta > angles.x) & (theta < angles.y)) ? this : null;
			}
		}
		return null;
	}
}

