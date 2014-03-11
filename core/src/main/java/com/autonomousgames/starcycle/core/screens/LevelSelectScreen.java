package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class LevelSelectScreen extends MenuScreen {

    StandardButton backButton;

	public LevelSelectScreen () {
		backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		ui.addActor(backButton);
        drawFakeOrbs = false;
	}

	public String toString(){
		return "LevelSelectScreen";
	}
}
