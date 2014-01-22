package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ArcButton extends LayeredButton {
	
	Vector2 arcCenter; // The center of the button relative to its bottom-left corner.
	Vector2 angles;
	Vector2 radii;
	
	Vector2 tVec = new Vector2(); // The location of the touch relative to arcCenter.
	float tLen; // The length of the touch vector. 
	float tAng; // The angle of the touch vector.
	
	
	public ArcButton(Vector2 center, Vector2 angles, Vector2 radii) {
		super(center, new Vector2(radii.y, radii.y).scl(2f));
		arcCenter = new Vector2(radii.y, radii.y);
		this.angles = angles;
		this.radii = radii;
	}
	
	@Override
	public void setPosition(float x, float y) {
		assert false : "Use setCenter for ArcButton.";
	}
	
	@Override
	public boolean touchOn(float x, float y) {
		tVec.set(x, y).sub(arcCenter);
		tLen = tVec.len();
		tAng = tVec.angle();
		return (radii.x <= tLen && tLen <= radii.y && angles.x <= tAng && tAng <= angles.y) ? true : false;
	}
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		return (touchable && touchOn(x,y)) ? this : null;
	}
	
}
