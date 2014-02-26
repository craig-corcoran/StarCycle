package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class NetworkedPregame extends MenuScreen {

	public NetworkedPregame() {
		Gdx.input.setInputProcessor(new MenuController(this));

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERMODESELECT));

		Vector2 kubatkoVector = new Vector2((ui.getWidth()*4f)/8f, ui.getHeight()*1f/2f);
		LayeredButton lookingForGameText = new LayeredButton(kubatkoVector);
		lookingForGameText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge, "Looking for game...", kubatkoVector).rotateText(90f));
		
		ui.addActor(backButton);
		ui.addActor(lookingForGameText);
	}
	
	public String toString(){
		return "AboutScreen";
	}
}