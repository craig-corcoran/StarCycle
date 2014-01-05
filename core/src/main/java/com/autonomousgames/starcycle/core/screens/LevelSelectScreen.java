package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class LevelSelectScreen extends MenuScreen {
	
	public LevelSelectScreen () {
		StandardButton backButton = new StandardButton(backPosition, backSize, Texturez.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.STARTMENU));
		
		ui.addActor(backButton);
	}
	
	@Override
	public void render(float delta) {
		//super.render() is not called on purpose to skip out on fakeorb stuff
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.disableBlending();
		batch.begin();
		batch.draw(Texturez.selectBG, 0f, 0f, StarCycle.screenHeight*2f,StarCycle.screenHeight);
		batch.enableBlending();
		batch.end();
		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		ui.draw();
	}
	
	public String toString(){
		return "LevelSelectScreen";
	}
}
