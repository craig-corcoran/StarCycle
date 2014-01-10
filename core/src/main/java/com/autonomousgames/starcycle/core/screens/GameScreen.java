package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.model.BackgroundManager;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameScreen implements Screen {
	
	public boolean isDone = false;
	public ScreenType nextScreen = null;
	// basic elements needed to draw sprites and have a user interface
	final SpriteBatch batch = new SpriteBatch(250);
	public final Stage ui = new Stage(StarCycle.screenWidth, StarCycle.screenHeight, true, batch);
	final OrthographicCamera cam = new OrthographicCamera(StarCycle.meterWidth, StarCycle.meterHeight);
	
	Vector2 backPosition = new Vector2(ui.getWidth()*8f/9f, ui.getHeight()*6/7f);
	Vector2 backSize = new Vector2(ui.getHeight()/8f, ui.getHeight()/8f*0.75f);
	float padding = StarCycle.pixelsPerMeter * UserSettingz.getFloatSetting("paddingMeters");
	
	// These need to be in both GameScreen and MenuScreen because MultiPlayerSelect gets made after StartMenu and after MultiPlayer.
	public LevelType nextLvlConfig;
	public BaseType[] skins = new BaseType[] {null, null};
	public Color[][] colors = new Color[][] {new Color[] {null, null}, new Color[] {null, null}};
    BackgroundManager background;

	public GameScreen(){
		cam.position.set(StarCycle.meterWidth / 2f, StarCycle.meterHeight / 2f, 0f);
        background = StarCycle.sc.getBackground();

	}

	@Override
	public void dispose() {
		ui.dispose();
		batch.dispose(); // TODO need?
	}

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void render(float delta) { }

	@Override
	public void resize(int width, int height) { }

	@Override
	public void resume() { }

	@Override
	public void show() { }
	
	public void setSkins(BaseType skin0, BaseType skin1) {
		this.skins = new BaseType[] {skin0, skin1};
	}
	
	public void setColors(Color[] colors0, Color[] colors1) {
		this.colors = new Color[][] {colors0, colors1};
	}

}
