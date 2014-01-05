package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Bot;
import com.autonomousgames.starcycle.core.model.BotType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Orb.OrbType;
import com.autonomousgames.starcycle.core.model.Player;
import com.badlogic.gdx.math.Vector2;

public class Tutorial6 extends TutorialSandbox {
	// Void sandbox.
	Vector2 starPos;
	Vector2 orbDist;
	Bot tutor;
	
	public Tutorial6() {
		super(LevelType.DOUBLE, ScreenType.TUTORIAL6, ScreenType.TUTORIAL7, ScreenType.TUTORIAL5);
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f, 0f, -1f);
		orbDist = new Vector2(UserSettingz.getFloatSetting("chargeRadius"), 0f).scl(2f);
		for (int i = 0; i < 2; i++) {
			starPos = new Vector2(model.stars.get((i+1)%2).position);
			for (int j = 0; j < 9; j++) {
				orbDist.rotate(40f);
				orbFactory.createOrb(OrbType.ORB, players[i], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f), -1f);
			}
			model.stars.get((i+1)%2).populations[i]=model.stars.get((i+1)%2).maxPop*(0.45f+i*0.55f);
		}
	}

	@Override
	boolean checkWin() {
		return (model.stars.get(0).numOrbs[1] == 0);
	}
	
	@Override
	void setPlayers() {
		numPlayers = 2;
		players = new Player[numPlayers];
		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true, true);
		players[0].disableIncome();
		players[1] = new Bot(1,BaseType.TAKETE, Texturez.warm, this, ui, false, false);
		tutor = (Bot) players[1];
		tutor.setBotType(BotType.DEAD);
		tutor.disableIncome();
	}

}
