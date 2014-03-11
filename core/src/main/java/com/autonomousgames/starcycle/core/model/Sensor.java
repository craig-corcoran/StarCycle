package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.lang.*;

public class Sensor {

	public static void addSensor(Object obj, Body body, float radius) {

		// add sensor for detecting if within radius
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		if (obj instanceof Void) {
			fixtureDef.filter.categoryBits = Model.voidCategoryBits[((Orb)obj).playerNum];
			fixtureDef.filter.maskBits = Model.voidMaskBits[((Orb)obj).playerNum];
		} 
		else if(obj instanceof Star) {
			fixtureDef.filter.categoryBits = Model.starCat;
			fixtureDef.filter.maskBits = Model.starMask;
		}
		Fixture sensor = body.createFixture(fixtureDef);
		sensor.setUserData(obj); // XXX use class instead of object?
		sensor.setSensor(true);
		shape.dispose();
	}

}
