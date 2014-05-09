package com.autonomousgames.starcycle.core.model;

import java.io.Serializable;

/**
 * Created by Sarvesh on 4/8/14.
 */
public class CombinedState implements Serializable {

    public GameState gameState;
    public OrbState orbstate;
    public StarState starState;
}
