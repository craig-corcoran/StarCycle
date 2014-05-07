package com.autonomousgames.starcycle.core.model;

import java.io.Serializable;

/**
 * Created by Sarvesh on 4/8/14.
 */
public class ChargeOrbState extends OrbState implements Serializable {
    public float dAngle = 0f;
    public int star = -1;
    public boolean lockedOn = false;

    public ChargeOrbState (int uid)
    {
        super (uid);
    }
}
