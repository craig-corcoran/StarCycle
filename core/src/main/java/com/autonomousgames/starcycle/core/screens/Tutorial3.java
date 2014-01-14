package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Bot;
import com.autonomousgames.starcycle.core.model.BotType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.math.Vector2;

public class Tutorial3 extends TutorialSandbox {
	// "Don't crash" tutorial graphic.
	// This is full of terrible hacks, and I hate it.
	long switchTime = 3000;
	long fireTime = 1200;
	long lastSwitch = 0;
	long lastFire = 0;
	private long timeNow;


    public Tutorial3() {
		super(LevelType.DOUBLE, ScreenType.TUTORIAL3, ScreenType.TUTORIAL4, ScreenType.TUTORIAL0);
//		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[5], 
//				new Vector2(StarCycle.screenHeight, StarCycle.screenHeight*0.5f)).rotateSprite(90f));
		Vector2 signSize = iconSize.cpy().scl(0.5f);
		infoGraphic.addLayer(new SpriteLayer(Texturez.wrongIcon, new Vector2(StarCycle.screenWidth*5/8f-StarCycle.screenHeight/4f, -StarCycle.screenHeight/6f), signSize, Texturez.red, -90f), LayerType.ACTIVE);
		infoGraphic.addLayer(new SpriteLayer(Texturez.rightIcon, new Vector2(StarCycle.screenWidth*5/8f-StarCycle.screenHeight/4f, StarCycle.screenHeight/6f), signSize, Texturez.spinach, -90f).rotateSprite(180f), LayerType.INACTIVE);
		
		timeNow = System.currentTimeMillis();
		players[1].launchPad.streamOrbs = true;
        players[1].showIncomeOrbs = false;
		lastSwitch = timeNow;
		lastFire = timeNow;
	}

	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f,-1f,-1f);
		orbFactory.setLife(-1f, -1f);
		Vector2 basePos = new Vector2(StarCycle.meterWidth/4f, StarCycle.meterHeight/2f);
		players[1].base.moveBase(basePos);
		players[1].base.rotatePointer(-40f);
//		players[1].base.angle+=MathUtils.PI*-40f/180f;
		// Moving the base star and changing the aim direction proved unexpectedly difficult.
		
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		// Switch between crashing and orbiting. This may not work on different aspect ratios.
		timeNow = System.currentTimeMillis();
		if (timeNow - lastSwitch >= switchTime) {
			infoGraphic.switchActive();
            float aimAngle = 50f;
            if (infoGraphic.isActive()) {
				players[1].base.rotatePointer(-aimAngle);
			}
			else {
				players[1].base.rotatePointer(aimAngle);
			}
			players[1].launchPad.streamOrbs = true;
			lastSwitch = timeNow;
			lastFire = timeNow;
		}
		if (players[1].launchPad.streamOrbs && timeNow - lastFire >= fireTime) {
			players[1].launchPad.streamOrbs = false;
		}
	}
	
	@Override
	void setPlayers() {
		numPlayers = 2;
		players = new Player[numPlayers];
		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, false, false);
		players[1] = new Bot(1,BaseType.TAKETE, Texturez.warm, this, ui, true, false);
		((Bot)players[1]).setBotType(BotType.DEAD);
	}
	
	@Override
	boolean checkWin() {
		return false;
	}
}
