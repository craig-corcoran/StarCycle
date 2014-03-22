package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.lang.*;

public class Sensor {

	public static Fixture addSensor(Class cls, int playerNum, Body body, float radius) {

		// add sensor for detecting if within radius
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
        shape.dispose();

		if (cls == Void.class) {
			fixtureDef.filter.categoryBits = Model.voidCategoryBits[playerNum];
			fixtureDef.filter.maskBits = Model.voidMaskBits[playerNum];
		} 
		else if( cls == Star.class) {
			fixtureDef.filter.categoryBits = Model.starCat;
			fixtureDef.filter.maskBits = Model.starMask;
		}
		Fixture sensor = body.createFixture(fixtureDef);
		sensor.setSensor(true);

        return sensor;
	}
}
