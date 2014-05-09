package com.autonomousgames.starcycle.core.model;

import java.io.Serializable;

public class StarState implements Serializable {
    public int index = -1;
    public float x = 0f;
    public float y = 0f;
    public float[] possession = null;
    public int[] numActiveOrbs = null;
    public float tValue = 0f;
    public float startPercent = 0f;

    public StarState(int numPlayers)
    {
        possession = new float[numPlayers];
        numActiveOrbs = new int[numPlayers];
    }
}
