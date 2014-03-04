package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class MultiPlayer extends ModelScreen {

	public MultiPlayer(LevelType lvl, BaseType[] skins, Color[][] colors) { 
		super(lvl, ScreenType.MULTIPLAYER, skins, colors);
		Gdx.input.setInputProcessor(new GameController(this, numPlayers));
		nextScreen = ScreenType.MULTIPLAYERLEVELSELECT;
	}

	@Override
	void setPlayers() {
		numPlayers = 2;
		players = new Player[numPlayers];
		for (int i=0; i < numPlayers; i++){
				players[i] = new Player(i, skins[i], colors[i], this, ui, true);
		}
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

