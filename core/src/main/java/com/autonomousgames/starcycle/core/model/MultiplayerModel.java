package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.screens.ModelScreen;

/**
 * Created by ccor on 3/6/14.
 */
public class MultiplayerModel extends Model {

    public MultiplayerModel(Level.LevelType lvl, ModelScreen screen) {
        super(lvl,screen);
    }

    @Override
    public Player[] initPlayers(ModelScreen screen) {

        Player[] players = new Player[numPlayers];
        for (int i=0; i < numPlayers; i++){
            players[i] = new Player(i, this, screen.ui, screen.skins[i], screen.colors[i], true, true);
        }

        return players;
    }
}
