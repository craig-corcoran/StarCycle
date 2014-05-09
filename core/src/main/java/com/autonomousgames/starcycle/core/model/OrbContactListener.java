package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.physics.box2d.*;

public class OrbContactListener implements ContactListener {

	private Fixture fixtureA;
	private Fixture fixtureB;
    private Collidable objA;
    private Collidable objB;

	@Override
	public void beginContact(Contact contact) {
		fixtureA = contact.getFixtureA();
		fixtureB = contact.getFixtureB();
		objA = (Collidable) fixtureA.getBody().getUserData();
		objB = (Collidable) fixtureB.getBody().getUserData();

	    if (objA != null & objB != null) {
            if (fixtureA.isSensor() | fixtureB.isSensor()) {
                objA.beginSensorContact(objB);
                objB.beginSensorContact(objA);
            }
            else {
                objA.collision(objB);
                objB.collision(objA);
            }
	    }
    }

	@Override
	public void endContact(Contact contact) {
		fixtureA = contact.getFixtureA();
		fixtureB = contact.getFixtureB();
        if (fixtureB != null & fixtureA != null) {
            objA = (Collidable) fixtureA.getBody().getUserData();
            objB = (Collidable) fixtureB.getBody().getUserData();
            if (fixtureA.isSensor() | fixtureB.isSensor()) {
                objA.endSensorContact(objB);
                objB.endSensorContact(objA);
            }
        }
    }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

}