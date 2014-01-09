package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

public class AboutScreen extends MenuScreen { 
	
	public AboutScreen() {
		Gdx.input.setInputProcessor(new MenuController(this));

		StandardButton backButton = new StandardButton(backPosition, backSize, Texturez.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MAINMENU));
		
		Vector2 versionVector = new Vector2((ui.getWidth()*1f)/8f, ui.getHeight()*1f/2f);
		LayeredButton versionText = new LayeredButton(versionVector);
		versionText.addLayer(new TextLayer(Texturez.latoLightLarge,"StarCycle V0.0.03", versionVector).rotateText(90f));
		
		Vector2 copyrightVector = new Vector2((ui.getWidth()*2f)/8f, ui.getHeight()*1f/2f);
		LayeredButton copyrightText = new LayeredButton(copyrightVector);
		copyrightText.addLayer(new TextLayer(Texturez.latoLightLarge,"Copyright Autonomous Games", copyrightVector).rotateText(90f));
		
		Vector2 websiteVector = new Vector2((ui.getWidth()*3f)/8f, ui.getHeight()*1f/2f);
		LayeredButton websiteText = new LayeredButton(websiteVector);
		websiteText.addLayer(new TextLayer(Texturez.latoLightLarge, "autonomousgames.com",websiteVector).rotateText(90f));
		
		Vector2 kubatkoVector = new Vector2((ui.getWidth()*4f)/8f, ui.getHeight()*1f/2f);
		LayeredButton kubatkoText = new LayeredButton(kubatkoVector);
		kubatkoText.addLayer(new TextLayer(Texturez.latoLightLarge, "Original Music by Kubatko",kubatkoVector).rotateText(90f));
		
		ui.addActor(backButton);
		ui.addActor(versionText);
		ui.addActor(copyrightText);
		ui.addActor(websiteText);
		ui.addActor(kubatkoText);
	}
	
	@Override
	public void render(float delta) {
		//super.render() is not called on purpose to skip out on fakeorb stuff
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.disableBlending();
		batch.begin();
		batch.draw(Texturez.mainMenuBG, 0f, 0f, StarCycle.screenWidth,StarCycle.screenHeight);
		batch.enableBlending();
		batch.end();
		ui.draw();
	}
	public String toString(){
		return "AboutScreen";
	}
}