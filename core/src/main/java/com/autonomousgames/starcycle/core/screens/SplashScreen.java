package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Soundz;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class SplashScreen extends GameScreen {


    private final float splashHeight = StarCycle.screenHeight/3f;
	private final float splashWidth; 

	private float displayedTime;
	
	public SplashScreen() {
		super();
		splashWidth = splashHeight * (StarCycle.tex.splashTexture.getRegionWidth() / ((float)StarCycle.tex.splashTexture.getRegionHeight()));
		nextScreen = ScreenType.MAINMENU;
		displayedTime = 0;
        StarCycle.makeBackground();
	}

	@Override
	public void dispose() {
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(StarCycle.tex.splashTexture, StarCycle.screenWidth/2f-splashWidth/2f,
					StarCycle.screenHeight/2f-splashHeight/2f,splashWidth/2f,splashHeight/2f,splashWidth,splashHeight,1f,1f,90f);
		batch.end();
		update(delta);
	}

	public void update(float delta) {
		// show the splash screen for splashTime:
		displayedTime += delta;
        float splashTime = 3f;
        if (displayedTime >= splashTime) {
			isDone = true;
			StarCycle.audio.gameMusic.setVolume(UserSettingz.getFloatSetting("musicVolume"));
			StarCycle.audio.gameMusic.setLooping(true);
			StarCycle.audio.gameMusic.play();
		}
	}
	public String toString(){
		return "Splash";
	}
}
