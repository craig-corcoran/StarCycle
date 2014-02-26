package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.badlogic.gdx.math.Vector2;

public class MultiplayerModeScreen extends MenuScreen {

	Vector2 iconSize = new Vector2(ui.getHeight()/5f, ui.getHeight()/5f);

	public MultiplayerModeScreen() {
		float padding = StarCycle.pixelsPerMeter/2f; // padding around text-only buttons

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.STARTMENU));
		
		StandardButton localMultiplayerButton = new StandardButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/2f), iconSize, StarCycle.tex.soloIcon, padding);
		localMultiplayerButton.setRotation(90f);
		localMultiplayerButton.addListener(new ScreenDoneClickListener(this, ScreenType.NETWORKEDPREGAME));
		
		StandardButton networkedMultiplayerButton = new StandardButton(new Vector2(ui.getWidth()/2f, ui.getHeight()/2f), new Vector2(iconSize.x*4f/3f, iconSize.y), StarCycle.tex.multiplayerIcon, padding);
		networkedMultiplayerButton.setRotation(90f);
		networkedMultiplayerButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERLEVELSELECT));

		ui.addActor(backButton);
		ui.addActor(networkedMultiplayerButton);
		ui.addActor(localMultiplayerButton);
	}

	public String toString(){
		return "MultiplayerModeScreen";
	}
}