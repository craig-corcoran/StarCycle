package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.math.Vector2;

public class MainMenu extends MenuScreen {
	
	/** image displayed before main menu **/
	
	public MainMenu() {
		
		float startButtonWidth = ui.getWidth()/3f;
		Vector2 startButtonDims = new Vector2(startButtonWidth, startButtonWidth);
		ScreenType startDestination = ScreenType.STARTMENU;//(UserSettingz.getSetting(SinglePlayerLevel.TUTORIAL.toString()) == 1f) ? ScreenType.STARTMENU : ScreenType.TUTORIAL_ORB; 
		
		StandardButton startButton = new StandardButton(new Vector2(ui.getWidth()/3f, ui.getHeight()/2f), startButtonDims, Texturez.genericLogo, padding);
		startButton.addLayer(new TextLayer(Texturez.gridnikLarge, "Start", startButtonDims));
		startButton.rotateLayers(90f);
		startButton.addListener(new ScreenDoneClickListener(this,startDestination));
		
		float hcenter = (ui.getWidth()/2f-startButtonWidth)/2f;
		Vector2 iconSize = new Vector2(hcenter, hcenter);
		Vector2 aboutPos = new Vector2(ui.getWidth()*5/8f+hcenter, ui.getHeight()/3f);
		
		StandardButton aboutButton = new StandardButton(aboutPos, iconSize, Texturez.infoIcon, padding);
		aboutButton.setRotation(90f);
		aboutButton.addListener(new ScreenDoneClickListener(this,ScreenType.ABOUT));
		
		Vector2 settingsPos = new Vector2(ui.getWidth()*5/8f+hcenter, ui.getHeight()*2/3f);
		
		StandardButton settingsButton = new StandardButton(settingsPos, iconSize, Texturez.settingsIcon, padding);
		settingsButton.setRotation(90f);
		settingsButton.addListener(new ScreenDoneClickListener(this,ScreenType.SETTINGS));
		
		ui.addActor(startButton);
		ui.addActor(aboutButton);
		ui.addActor(settingsButton); 

	}
	public String toString(){
		return "MainMenu";
	}
}