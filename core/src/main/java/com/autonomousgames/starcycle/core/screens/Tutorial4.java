package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.math.Vector2;

public class Tutorial4 extends TutorialSandbox {
	// "Don't crash" sandbox.
	
	public Tutorial4() {
		super(LevelType.SINGLE, ScreenType.TUTORIAL4, ScreenType.TUTORIAL5, ScreenType.TUTORIAL3);
		players[0].base.setPointer(StarCycle.pixelScreenCenter.cpy().div(StarCycle.pixelsPerMeter).sub(players[0].base.origin));
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[6],
				new Vector2(StarCycle.screenHeight, 0.5f*StarCycle.screenHeight)).rotateSprite(90f));
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f,-1f,-1f);
		orbFactory.setLife(10f, 10f);
		infoGraphic.deactivate();
		// Basic launchpad with no ammo or income.
//		players[0].disableIncome();
        players[0].launchPad.showMeter(false);
        orbFactory.setCosts(0f, 0f, 0f);
	}

	@Override
	boolean checkWin() {
		return (model.stars.get(0).numOrbs[0] >= 7);
	}
	
	@Override
	void setPlayers() {
		numPlayers = 1;
		players = new Player[numPlayers];
		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true, true);
        players[0].showIncomeOrbs = false;
	}

}
