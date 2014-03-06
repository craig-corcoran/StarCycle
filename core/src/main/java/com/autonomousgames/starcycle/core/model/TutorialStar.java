package com.autonomousgames.starcycle.core.model;

/**
 * Created by ccor on 3/6/14.
 */
public class TutorialStar extends Star {

    public float mass;
    public boolean hitByNova = false;

    public void gravityOff() {
        mass = 0f;
    }

    public void gravityOn() {
        mass = radius * radius;
    }
}
