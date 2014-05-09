package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.screens.ModelScreen;

public class MultiplayerModel extends Model {

    public MultiplayerModel(Level.LevelType lvl, ModelScreen screen) {
        super(lvl,screen);
    }

    @Override
    public Player[] initPlayers(ModelScreen screen) {

        Player[] players = new Player[numPlayers];
        for (int i=0; i < numPlayers; i++){
            players[i] = new Player(i, screen.ui, screen.skins[i], screen.colors[i], true, true);
        }

        return players;
    }
}
