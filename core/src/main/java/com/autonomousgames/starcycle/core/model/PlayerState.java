package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;

import java.io.Serializable;

public class PlayerState implements Serializable {
    public int playerNum = (StarCycle.playerNum != null) ? StarCycle.playerNum : -1;
    public float ammo = 0f;
    public int starsControlled = 0;
    public int numActiveOrbs = 0;
    public float pointerX = 0f;
    public float pointerY = 0f;
    public boolean[] buttonStates = new boolean[3];
    public int playerActionMessageNumber = 0;
    public void incrementPlayerActionMessageCount(){playerActionMessageNumber++;}

    public void setPlayerActionMessageNumber (int messageNumber){playerActionMessageNumber = messageNumber;}
    public int getPlayerActionMessageNumber()
    {
        return playerActionMessageNumber;
    }
}
