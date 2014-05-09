package com.autonomousgames.starcycle.core.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class GameState implements Serializable {

    int frame = 0;
    int orbID = 0;

    LinkedHashMap<Integer, ChargeOrbState>[] orbStates = null;
    LinkedHashMap<Integer,ChargeOrbState>[] voidStates = null;
    LinkedHashMap<Integer,OrbState>[] novaStates = null;
    public PlayerState[] playerStates = null;
    StarState[] starStates;

    public GameState(int numStars, int numPlayers) {

        orbStates = new LinkedHashMap[numPlayers];
        voidStates = new LinkedHashMap[numPlayers];
        novaStates = new LinkedHashMap[numPlayers];
        playerStates = new PlayerState[numPlayers];

        starStates = new StarState[numStars];

        for (int i=0; i < numPlayers; i++) {
            orbStates[i] = new LinkedHashMap<Integer,ChargeOrbState>(500);
            voidStates[i] = new LinkedHashMap<Integer,ChargeOrbState>(50);
            novaStates[i] = new LinkedHashMap<Integer,OrbState>(50);
        }
    }
}
