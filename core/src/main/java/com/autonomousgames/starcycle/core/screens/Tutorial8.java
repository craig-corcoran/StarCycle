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

public class Tutorial8 extends TutorialSandbox {
	// Nova sandbox
	Vector2 starPos;
	Vector2 orbDist;
	OrbType orbType;
	Bot tutor;

	public Tutorial8() {
		super(LevelType.TRIPLE, ScreenType.TUTORIAL8, ScreenType.STARTMENU, ScreenType.TUTORIAL7);
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f, 0f, 0f);
		orbDist = new Vector2(UserSettingz.getFloatSetting("chargeRadius"), 0f).scl(2f);
		starPos = new Vector2(0f,0f);
		starPos.set(model.stars[0].position);
		model.stars[0].populations[1] = model.stars[0].maxPop;
		for (int i = 0; i < 10; i ++) {
			orbDist.rotate(36f);
			orbType = i == 0 ? OrbType.VOID : OrbType.ORB;
			orbFactory.createOrb(orbType, players[1], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f), -1f);
		}
		for (int i = 1; i < 3; i ++) {
			starPos.set(model.stars[i].position);
			model.stars[i].populations[0] = model.stars[i].maxPop*(0.1f+0.15f*i);
			for (int j = 0; j < 9; j ++) {
				orbDist.rotate(40f);
				orbFactory.createOrb(OrbType.ORB, players[0], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f), -1f);
			}
		}
		
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
