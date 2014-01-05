package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

public abstract class TutorialGraphic extends MenuScreen {

	Vector2 iconSize = new Vector2(ui.getHeight()/5f, ui.getHeight()/5f);
	LayeredButton infoGraphic;
	StandardButton nextPage;
	StandardButton prevPage;
	float navHeight = 0.5f*StarCycle.screenHeight+backSize.y*0.8f;
	
	public TutorialGraphic (ScreenType nextScreen, ScreenType prevScreen) {
		
		infoGraphic = new LayeredButton(StarCycle.pixelScreenCenter);
		
		nextPage = new StandardButton(new Vector2(navHeight, StarCycle.screenHeight-backSize.x*0.8f),
				backSize,	Texturez.backIcon, padding);
		nextPage.rotateLayers(-90f).flip(false, true);
		nextPage.addListener(new ScreenDoneClickListener(this, nextScreen));
		
		prevPage = new StandardButton(new Vector2(navHeight, backSize.x*0.8f), backSize, Texturez.backIcon, padding);
		prevPage.rotateLayers(90f);
		prevPage.addListener(new ScreenDoneClickListener(this, prevScreen));
		
		ui.addActor(infoGraphic);
		ui.addActor(nextPage);
		ui.addActor(prevPage);
	}
	
	@Override
	public void render(float delta) {
		//super.render() is not called on purpose to skip out on fakeorbs, background, etc.
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		ui.draw();
	}
}
