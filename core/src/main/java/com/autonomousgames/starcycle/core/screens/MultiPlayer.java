package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Model;
import com.autonomousgames.starcycle.core.model.MultiplayerModel;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class MultiPlayer extends ModelScreen {

	public MultiPlayer(LevelType lvl, BaseType[] skins, Color[][] colors, StarCycle starcycle) {
		super(lvl, ScreenType.MULTIPLAYER, skins, colors, starcycle);
		Gdx.input.setInputProcessor(new GameController(this, Model.numPlayers));
		nextScreen = ScreenType.MULTIPLAYERLEVELSELECT;
	}


    @Override
    public Model initModel(LevelType lvl, ModelScreen screen) {
        return new MultiplayerModel(lvl, screen);
    }

    // TODO clean up, you win, you lose, need non abstract method in ModelScreen?
	@Override
	public void addWinBanner(final Player winner) {
		super.addWinBanner(winner);
		winButton.addListener(new ScreenDoneClickListener(this, backScreen));
        ui.addActor(winButton);
	}

	public String toString(){
		return "Multiplayer";
	}
}

