package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Bot;
import com.autonomousgames.starcycle.core.model.BotType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Orb.OrbType;
import com.autonomousgames.starcycle.core.model.Player;
import com.badlogic.gdx.math.Vector2;

public class Tutorial5 extends TutorialSandbox {
	// Void tutorial graphic.
	Vector2 starPos;
	Vector2 orbDist;
	Bot tutor;
	long timeNow;
	long refreshTime = 4500;
	long lastDemo;
	
	public Tutorial5() {
		super(LevelType.SINGLE, ScreenType.TUTORIAL5, ScreenType.TUTORIAL6, ScreenType.TUTORIAL0);
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f, 0f, -1f);
		orbFactory.setLife(8f, 8f);
		orbDist = new Vector2(UserSettingz.getFloatSetting("chargeRadius"), 0f).scl(2f);
		starPos = new Vector2(StarCycle.meterWidth, StarCycle.meterHeight).scl(0.5f);
		tutor.base.setPointerPolar(10f, -5f);
		timeNow = System.currentTimeMillis();
		voidDemo();
		lastDemo = timeNow;
	}

	private void voidDemo() {
		model.stars.get(0).populations[0] = 0f;
		model.stars.get(0).populations[1] = 0f;
		for (int i = 0; i < 6; i++) {
			orbDist.rotate(60f);
			orbFactory.createOrb(OrbType.ORB, players[0], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f));
		}
		orbDist.rotate(30f);
		for (int i = 0; i < 6; i++) {
			orbDist.rotate(60f);
			orbFactory.createOrb(OrbType.ORB, players[1], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f));
		}
		tutor.launchPad.launch(OrbType.VOID);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		timeNow = System.currentTimeMillis();
		if (timeNow - lastDemo >= refreshTime) {
			voidDemo();
			lastDemo = timeNow;
		}
	}
	
	@Override
	void setPlayers() {
		numPlayers = 2;
		players = new Player[numPlayers];
		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, false, false);
		players[1] = new Bot(1,BaseType.TAKETE, Texturez.warm, this, ui, true, true);
		tutor = (Bot) players[1];
		tutor.setBotType(BotType.DEAD);
//		tutor.disableIncome();
        tutor.showIncomeOrbs = false;
        players[0].showIncomeOrbs = false;
        players[1].launchPad.showMeter(false);
	}
	
	@Override
	boolean checkWin() {
		return false;
	}

}
