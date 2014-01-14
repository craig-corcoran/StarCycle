package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.badlogic.gdx.math.Vector2;

public class StartMenu extends MenuScreen { 
	
	Vector2 iconSize = new Vector2(ui.getHeight()/5f, ui.getHeight()/5f);
	
	public StartMenu() {
		
		float padding = StarCycle.pixelsPerMeter/2f; // padding around text-only buttons

		StandardButton backButton = new StandardButton(backPosition, backSize, Texturez.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MAINMENU));
		
		StandardButton singlePlayerButton = new StandardButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/2f), iconSize, Texturez.soloIcon, padding);
		singlePlayerButton.setRotation(90f);
		singlePlayerButton.addListener(new ScreenDoneClickListener(this,ScreenType.CAMPAIGNSELECT));
		
		StandardButton multiPlayerButton = new StandardButton(new Vector2(ui.getWidth()/2f, ui.getHeight()/2f), new Vector2(iconSize.x*4f/3f, iconSize.y), Texturez.multiplayerIcon, padding);
		multiPlayerButton.setRotation(90f);
		multiPlayerButton.addListener(new ScreenDoneClickListener(this,ScreenType.MULTIPLAYERSELECT));
		
		StandardButton tutorialButton = new StandardButton(new Vector2(ui.getWidth()*3f/4f, ui.getHeight()/2f), iconSize.div(2f), Texturez.questionIcon, padding);
		tutorialButton.setRotation(90f);
		tutorialButton.addListener(new ScreenDoneClickListener(this,ScreenType.TUTORIAL0));

		ui.addActor(backButton);
		ui.addActor(multiPlayerButton);
		ui.addActor(singlePlayerButton); 
		ui.addActor(tutorialButton);
		
	}
	public String toString(){
		return "StartMenu";
	}
}