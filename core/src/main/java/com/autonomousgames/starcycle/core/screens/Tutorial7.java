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
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.math.Vector2;

public class Tutorial7 extends TutorialSandbox {
	// Nova tutorial graphic.
	Vector2 starPos;
	Vector2 orbDist;
	Bot tutor;
	boolean voidShot = false;
	boolean shotWaiting = false;
	boolean novaShot = false;
	long timeNow;
	long refreshTime = 10500;
	long lastDemo;
	long launchDelay = 250;
	long launchTime;

	public Tutorial7() {
		super(LevelType.TRIPLE, ScreenType.TUTORIAL7, ScreenType.TUTORIAL8, ScreenType.TUTORIAL6);
		// TODO Auto-generated constructor stub
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f, 0f, 0f);
		orbFactory.setLife(10f, 8f);
		orbDist = new Vector2(UserSettingz.getFloatSetting("chargeRadius"), 0f).scl(2f);
		starPos = new Vector2(0f,0f);
		for (int i = 0; i < 2; i ++) {
			starPos.set(model.stars.get(i).position);
			for (int j = 0; j < 9; j ++) {
				orbDist.rotate(40f);
				orbFactory.createOrb(OrbType.ORB, players[1], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f), -1f);
			}
		}
		
		winButton = new LayeredButton(new Vector2(StarCycle.screenHeight/4f, StarCycle.screenHeight/2f), iconSize.cpy().scl(2f));
		winButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, iconSize.cpy().scl(2f)));
		winButton.addLayer(new SpriteLayer(StarCycle.tex.trophy, iconSize).setSpriteColor(tutor.colors[0]));
		winButton.rotate(90f);
		// winButton is created, but not added to the ui yet.
		
		timeNow = System.currentTimeMillis();
		resetDemo();
		lastDemo = timeNow;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (tutor.base.level == 1 && !voidShot) {
			if (!shotWaiting) {
				launchTime = System.currentTimeMillis() + launchDelay;
				shotWaiting = true;
			}
			else if (System.currentTimeMillis() >= launchTime) {
				tutor.aimAtStar(model.stars.get(2));
				tutor.launchPad.launch(OrbType.VOID);
				voidShot = true;
				shotWaiting = false;
			}
		}
		else if (tutor.base.level == 2 && !novaShot) {
			if (!shotWaiting) {
				launchTime = System.currentTimeMillis() + launchDelay;
				shotWaiting = true;
			}
			else if (System.currentTimeMillis() >= launchTime) {
				tutor.vecAim(model.stars.get(2).position);
				tutor.launchPad.launch(OrbType.NOVA);
				novaShot = true;
			}
		}
		timeNow = System.currentTimeMillis();
		if (timeNow - lastDemo >= refreshTime) {
			resetDemo();
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
	
	private void resetDemo() {
		tutor.base.level = 0;
		tutor.base.baseButton.setLevel(0);
		starPos.set(model.stars.get(2).position);
		model.stars.get(2).populations[1] = 0f;
		model.stars.get(2).populations[0] = model.stars.get(2).maxPop*0.75f;
		for (int i = 0; i < 2; i ++) {
			model.stars.get(i).populations[1] = model.stars.get(i).maxPop*(0.25f+0.17f*i);
		}
		for (int i = 0; i < 9; i ++) {
			orbDist.rotate(40f);
			orbFactory.createOrb(OrbType.ORB, players[0], new Vector2(starPos.x+orbDist.x, starPos.y+orbDist.y), orbDist.cpy().scl(2f).rotate(90f), -1f);
		}
		voidShot = false;
		shotWaiting = false;
		novaShot = false;
		winButton.remove();
		taskComplete = false;
	}
	
	@Override
	boolean checkWin() {
		return players[1].win; // Default win condition.
	}
	
	@Override
	public void addWinBanner(final Player winner) {
		ui.addActor(winButton);
		gameOver = false;
	}

}
