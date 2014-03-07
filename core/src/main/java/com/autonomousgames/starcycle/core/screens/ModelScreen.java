package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ModelScreen extends GameScreen{

    public final Model model;
	public boolean gameOver = false;
	public boolean paused = false;
	//public boolean debugPaused = false;
	protected ScreenType screentype;
	GL10 gl = Gdx.graphics.getGL10();
	private Json json = new Json();
	private BaseButton winBase;
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public final boolean dbRender = false;
	
	ScreenType backScreen;
	LayeredButton pauseButton;
	LayeredButton resumeButton;
	StandardButton backButton;
	StandardButton mainMenuButton;
	LayeredButton winButton;
	float side = StarCycle.screenHeight / 6f;
	Vector2 iconSize = new Vector2(side, side);
	private long gameStartTime;
	
	public ModelScreen(LevelType lvl, ScreenType screentype, BaseType[] skins, Color[][] colors) {
		json.setOutputType(JsonWriter.OutputType.json);
		nextLvlConfig = lvl;
		this.screentype = screentype;
		this.skins = skins;
		this.colors = colors;
		gameStartTime = System.currentTimeMillis();
		model = new Model(lvl); // TODO construct  players in model


        switch (this.screentype) {
            case MULTIPLAYER:
                backScreen = ScreenType.MULTIPLAYERLEVELSELECT;
                break;
            case SINGLEPLAYER:
                backScreen = ScreenType.CAMPAIGNSELECT;
                break;
            default:
                backScreen = ScreenType.STARTMENU;
                break;
        }
		
		// add pause button
        Vector2 pauseSize = new Vector2(StarCycle.screenWidth/2f, StarCycle.screenHeight/2f);
		pauseButton = new LayeredButton(pauseSize, new Vector2(StarCycle.screenWidth/3f, StarCycle.screenHeight));
		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y); // need?
				addPauseBanner();
			}
		});
		
		mainMenuButton = new StandardButton(new Vector2(StarCycle.screenWidth/2f, side), iconSize.cpy().scl(0.8f), StarCycle.tex.pauseUI[0], padding);
		mainMenuButton.addListener(new ScreenDoneClickListener(this,ScreenType.MAINMENU));
		mainMenuButton.setRotation(90f);
        mainMenuButton.setColor(Colors.navy);
		
		resumeButton = new LayeredButton(new Vector2(StarCycle.screenWidth/2f, side*3f), iconSize.cpy().scl(2f));
        resumeButton.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(StarCycle.screenHeight, StarCycle.screenWidth).scl(1.1f)).setSpriteColor(Color.BLACK).setSpriteAlpha(0.6f));
		resumeButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, iconSize.cpy().scl(2f)), LayerType.DOWN);
		resumeButton.addLayer(new SpriteLayer(StarCycle.tex.pauseUI[2], iconSize.cpy().scl(1.75f)).setRotationSpeed(30f).setSpriteColor(Colors.spinach));
		resumeButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						paused = false;
						mainMenuButton.remove();
						backButton.remove();
						resumeButton.remove();
					}
				});
		resumeButton.setRotation(90f);
		
		backButton = new StandardButton(new Vector2(StarCycle.screenWidth/2f, side*5f), new Vector2( iconSize.x, iconSize.y*0.75f), StarCycle.tex.pauseUI[3], padding);
		backButton.addListener(new ScreenDoneClickListener(this,this.backScreen));
		backButton.setRotation(90f);
        backButton.setColor(Colors.red);
		
		ui.addActor(pauseButton);
	}

	public void addWinBanner(Player winner){
        for (Player player : players) {
            player.frozen = true;
        }
        StarCycle.audio.winSound.play(StarCycle.audio.sfxVolume);
        winBase = new BaseButton(winner.basetype, winner.colors, StarCycle.pixelScreenCenter,
                winner.base.baseDims.scl(1.5f), 2);
        winButton = new LayeredButton(new Vector2(StarCycle.screenWidth/2f, side*3f), iconSize.cpy().scl(4f));
        ui.addActor(winBase);
        logGame(winner.number);
	}

    public void logGame(int whoWon) {
        String[] gameLog = {
                whoWon + "", // winning player number
                this.toString(), // screenType
                this.nextLvlConfig.toString(), // screenType
                this.numPlayers + "", // numPlayers
                StarCycle.startTime + "", // sessionStartTime
                gameStartTime + "", // gameStartTime
                (System.currentTimeMillis() - gameStartTime) + "", // gameDuration
                model.totalOrbs[0] + "",
                model.totalOrbs[1] + "",
                model.totalVoids[0] + "",
                model.totalVoids[1] + "",
                model.totalNovas[0] + "",
                model.totalNovas[1] + "",
        };
        StarCycle.logHandler.logGame(json.toJson(gameLog));
    }

	
	public void addPauseBanner() {
		if (!paused && !gameOver){
			paused = true;
            ui.addActor(resumeButton);
			ui.addActor(backButton);
			ui.addActor(mainMenuButton);
		}
	}
	
	public void render (float delta) {
		// we update the world using a constant time to get the same physics across devices (set in StarCycle.java
		//process the user button pushes
		ui.act(delta);
		
		// clear the color buffer and set the camera matrices
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();
		cam.apply(gl);
		renderSprites(delta); // orbs, stars, bases
		ui.draw();
		
		if (dbRender){
			debugRenderer.render(model.world,cam.combined);
    		long start = System.currentTimeMillis();
    		displayFPS(start);
     		printFPS(start); // log fps to console output
		}

		if (!debugPaused) {
			if ((!gameOver) & (!paused)) {
				// while the gpu renders, we update the world physics and manage collisions
				update(delta);
			}
            if (gameOver){
                for (Player player : players) {
                    //player.updateWinOrbs(delta);
                }
            }

			background.update(); // Moves while paused.
		}
	}
	
	public void update(float delta) {
		
		model.update(); // here's where the magic happens

        // check for win conditions
        int winner = model.winCondition.getWinner();
        if (winner >= 0) { // if not -1 (no winner)

        }

    }

	void renderSprites(float delta) {
		batch.begin();

		background.draw(batch);
        if (gameOver) {
            winBase.draw(batch, 1f);
        }

        for (Player player : players) {
            player.draw(batch);
        }
		for (int i = 0; i < model.stars.length; i++) {
			model.stars[i].draw(batch);
		}
		batch.end();
	}
	
	void displayFPS(float start) {
		float updateTime = (System.currentTimeMillis() - start) / 1000.0f;
//		batch.getProjectionMatrix().setToOrtho2D(0,0, StarCycle.meterWidth*StarCycle.pixelsPerMeter, StarCycle.meterHeight*StarCycle.pixelsPerMeter);
		batch.begin();
		Integer numOrbs = 0;
        for (Player player : players) {
            numOrbs += player.state.numActiveOrbs;
        }
		StarCycle.tex.font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + " update time: " +
				updateTime + " orbs: " + numOrbs + "", 0, 20);
		batch.end();
	}
	
	void printFPS(float start) {
		float updateTime = (System.currentTimeMillis() - start) / 1000.0f;
//		batch.getProjectionMatrix().setToOrtho2D(0,0, StarCycle.meterWidth*StarCycle.pixelsPerMeter, StarCycle.meterHeight*StarCycle.pixelsPerMeter);
		Gdx.app.log("model screen", "fps: " + Gdx.graphics.getFramesPerSecond() + " update time: " + updateTime);
	}
	
	public void dispose() {
		super.dispose();
		model.dispose();
        for (Player player : players) {
            player.dispose();
        }
	}
	
	public String toString(){
		return "ModelScreen";
	}
}
