package com.autonomousgames.starcycle.core.model;

public interface Collidable {

	void beginSensorContact(Collidable obj);

	void endSensorContact(Collidable obj);

	void collision(Collidable obj);

}
