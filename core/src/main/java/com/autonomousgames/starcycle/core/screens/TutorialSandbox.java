package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class TutorialSandbox extends ModelScreen {
		
	final Vector2 textPosCenter = new Vector2(ui.getWidth()/8f, ui.getHeight()/2f);
	final Vector2 tutorialButtonSize = new Vector2(ui.getHeight(), ui.getHeight());
	StandardButton nextPage;
	StandardButton prevPage;
	LayeredButton infoGraphic;
	boolean taskComplete = false;
	float navHeight = 0.5f*StarCycle.screenHeight+backSize.y*0.8f;
	
	public TutorialSandbox(LevelType lvlType, ScreenType scnType, ScreenType nextScreen, ScreenType prevScreen) { 
		super(lvlType, scnType, 
			new BaseType[] {BaseType.MALUMA, BaseType.TAKETE},
			new Color[][] {Colors.cool, Colors.warm});
		background.noDraw = true;
		this.nextScreen = nextScreen;
		Gdx.input.setInputProcessor(new GameController(this, 1)); // only one active touch interface
		
		infoGraphic = new LayeredButton(new Vector2(StarCycle.screenHeight/4f, StarCycle.screenHeight/2f));
		// Layers will be added per screen.
		ui.addActor(infoGraphic);
		
		nextPage = new StandardButton(new Vector2(navHeight, StarCycle.screenHeight-backSize.x*0.8f),
				backSize,	StarCycle.tex.backIcon, padding);
		nextPage.rotateLayers(-90f).flip(false, true);
		nextPage.addLayer(new SpriteLayer(StarCycle.tex.noIcon, iconSize).setSpriteColor(Colors.red), LayerType.INACTIVE);
		nextPage.addListener(new ScreenDoneClickListener(this, nextScreen));
		ui.addActor(nextPage);
		
		prevPage = new StandardButton(new Vector2(navHeight, backSize.x*0.8f), backSize, StarCycle.tex.backIcon, padding);
		prevPage.rotateLayers(90f);
		prevPage.addListener(new ScreenDoneClickListener(this, prevScreen));
		ui.addActor(prevPage);
		
		pauseButton.deactivate();
		
		setInitialConditions();
	}
	
	@Override
	public void update(float delta) {
		
		model.update(delta); // calls level.update
		
		// update players
        for (Player player : players) {
            player.update(delta, model.stars, model.starPositions);
        }
		// Check for task completion
		if (!taskComplete & checkWin()) {
			taskComplete = true;
			addWinBanner(players[0]);
			nextPage.activate();
		}
	}
	
	boolean checkWin() {
		return players[0].win; // Default win condition.
	}
	
	@Override
	public void addWinBanner(final Player winner) {
		//super.addWinBanner(winner);
		TextureRegion texture = (winner.number==0) ? StarCycle.tex.trophy : StarCycle.tex.thumbsDown;
		winButton = new LayeredButton(new Vector2(StarCycle.screenHeight/4f, StarCycle.screenHeight/2f), iconSize.cpy().scl(4f));
		winButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, iconSize.cpy().scl(4f)));
		winButton.addLayer(new SpriteLayer(texture, iconSize.scl(2f)).setSpriteColor(winner.colors[0]));
		winButton.rotate(90f);
		ui.addActor(winButton);
		gameOver = false;
	}
	
	abstract void setInitialConditions();
	
	public String toString(){
		return "Tutorial";
	}

    @Override
    public void dispose() {
        background.noDraw = false;
        super.dispose();
    }
}

