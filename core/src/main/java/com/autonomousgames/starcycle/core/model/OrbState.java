package com.autonomousgames.starcycle.core.model;

import java.io.Serializable;

/**
 * Created by Sarvesh on 4/8/14.
 */
public class OrbState implements Serializable {
    public float x = 0f;
    public float y = 0f;
    public float v = 0f;
    public float w = 0f;
    public int age = 0;
    public int uid = 0;

    public OrbState (int uid)
    {
        this.uid = uid;
    }
}
