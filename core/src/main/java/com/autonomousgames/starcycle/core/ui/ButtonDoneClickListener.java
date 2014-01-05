package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonDoneClickListener extends ClickListener {
	
	private ModelScreen screen;
	private Actor button;
	private Actor nextButton;
	

	public ButtonDoneClickListener(ModelScreen screen, Actor button){
		this.screen = screen;
		this.button = button;
		this.nextButton = null;
	}
	public ButtonDoneClickListener(ModelScreen screen, Actor button, Actor nextButton){
		this.screen = screen;
		this.button = button;
		this.nextButton = nextButton;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		button.remove();
		if (this.nextButton != null){
			screen.ui.addActor(nextButton);
		}
	}
}