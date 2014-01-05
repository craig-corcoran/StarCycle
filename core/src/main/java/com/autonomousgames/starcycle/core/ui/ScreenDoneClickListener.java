package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.screens.GameScreen;
import com.autonomousgames.starcycle.core.screens.ScreenType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScreenDoneClickListener extends ClickListener {
	GameScreen screen;
	ScreenType nextScreen;
	
	public ScreenDoneClickListener(GameScreen screen, ScreenType nextScreen){
		this.screen = screen;
		this.nextScreen = nextScreen;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		super.clicked(event, x, y);
		screen.nextScreen = nextScreen;
		screen.isDone = true;
	}
}