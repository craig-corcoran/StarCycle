package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class AboutScreen extends MenuScreen { 
	
	public AboutScreen() {
		Gdx.input.setInputProcessor(new MenuController(this));

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MAINMENU));
		
		Vector2 versionVector = new Vector2((ui.getWidth()*1f)/8f, ui.getHeight()*1f/2f);
		LayeredButton versionText = new LayeredButton(versionVector);
		versionText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge,"StarCycle V" + StarCycle.version, versionVector).rotateText(90f));
		
		Vector2 copyrightVector = new Vector2((ui.getWidth()*2f)/8f, ui.getHeight()*1f/2f);
		LayeredButton copyrightText = new LayeredButton(copyrightVector);
		copyrightText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge,"Copyright Autonomous Games", copyrightVector).rotateText(90f));
		
		Vector2 websiteVector = new Vector2((ui.getWidth()*3f)/8f, ui.getHeight()*1f/2f);
		LayeredButton websiteText = new LayeredButton(websiteVector);
		websiteText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge, "autonomousgames.com",websiteVector).rotateText(90f));
		
		Vector2 kubatkoVector = new Vector2((ui.getWidth()*4f)/8f, ui.getHeight()*1f/2f);
		LayeredButton kubatkoText = new LayeredButton(kubatkoVector);
		kubatkoText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge, "Original Music by Kubatko",kubatkoVector).rotateText(90f));
		
		ui.addActor(backButton);
		ui.addActor(versionText);
		ui.addActor(copyrightText);
		ui.addActor(websiteText);
		ui.addActor(kubatkoText);
	}
	
	public String toString(){
		return "AboutScreen";
	}
}