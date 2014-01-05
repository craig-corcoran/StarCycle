package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Soundz;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Orb.OrbType;
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

import java.util.HashMap;

public abstract class ModelScreen extends GameScreen{
	public Model model;
	public Player[] players;
	public int numPlayers;
	OrbFactory orbFactory;
	public boolean gameOver = false;
	public boolean paused = false;
	public boolean debugPaused = false;
	protected ScreenType screentype;
	GL10 gl = Gdx.graphics.getGL10();
	BackgroundManager background = new BackgroundManager();
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
		setPlayers();
		gameStartTime = System.currentTimeMillis();
		model = new Model(lvl, players);
		orbFactory = new OrbFactory(model);
		
		for (int i=0; i < numPlayers; i++) {
			if (players[i] instanceof Bot) {
				((Bot) players[i]).initializeModel(model);
			}
		}

		if (this.screentype == ScreenType.MULTIPLAYER) {
			backScreen = ScreenType.MULTIPLAYERSELECT;
		}
		else {
			backScreen = ScreenType.CAMPAIGNSELECT;
		}
		
		// add pause button
		pauseButton = new LayeredButton(new Vector2(StarCycle.screenWidth/2f, StarCycle.screenHeight/2f),new Vector2(StarCycle.screenWidth/3f, StarCycle.screenHeight)); 
		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y); // need?
				addPauseBanner();
			}
		});
		
		mainMenuButton = new StandardButton(new Vector2(StarCycle.screenWidth/2f, side), iconSize, Texturez.pauseUI[0], padding);
		mainMenuButton.addListener(new ScreenDoneClickListener(this,ScreenType.MAINMENU));
		mainMenuButton.setRotation(90f);
		
		resumeButton = new LayeredButton(new Vector2(StarCycle.screenWidth/2f, side*3f), iconSize);
		resumeButton.addLayer(new SpriteLayer(Texturez.gradientRound, iconSize.cpy().scl(1.5f)), LayerType.DOWN);
		resumeButton.addLayer(new SpriteLayer(Texturez.pauseUI[1], iconSize.cpy().scl(0.35f)));
		resumeButton.addLayer(new SpriteLayer(Texturez.pauseUI[2], iconSize.cpy().scl(1.2f)).setRotationSpeed(30f));
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
		
		backButton = new StandardButton(new Vector2(StarCycle.screenWidth/2f, side*5f), new Vector2( iconSize.x, iconSize.y*0.75f), Texturez.pauseUI[3], padding);
		backButton.addListener(new ScreenDoneClickListener(this,this.backScreen));
		backButton.setRotation(90f);
		
		ui.addActor(pauseButton);
	}
	
	abstract void setPlayers();

	public void addWinBanner(Player winner){
        for (Player player : players) {
            player.frozen = true;
        }
        Soundz.winSound.play(UserSettingz.getFloatSetting("sfxVolume"));
        //winner.base = new Base(winner, new Vector2(StarCycle.meterWidth / 2f, StarCycle.meterHeight / 2f),
        //        UserSettingz.getFloatSetting("baseRadius")*2f, ui, true);
        //winner.base.baseButton.setLevel(2);
        //winner.base.handleImDims = new Vector2(0f, 0f);
        //winner.base.chevronImDims = new Vector2(0f, 0f);
        winBase = new BaseButton(winner.basetype, winner.colors, StarCycle.pixelScreenCenter,
                winner.base.baseDims.scl(1.5f), 2);
        winButton = new LayeredButton(new Vector2(StarCycle.screenWidth/2f, side*3f), iconSize.cpy().scl(4f));
        ui.addActor(winBase);
        logState();
	}

    public void logState() {
        HashMap<String,Object> logMap = new HashMap<String,Object>();
        logMap.put("screenType",this.toString());
        logMap.put("numPlayers",this.numPlayers);
        logMap.put("sessionTime",StarCycle.startTime);
        logMap.put("gameStartTime", gameStartTime);
        logMap.put("currentTime",System.currentTimeMillis());
        logMap.put("p1orbs",this.orbFactory.p1orbs);
        logMap.put("p2orbs",this.orbFactory.p2orbs);
        logMap.put("p1voids",this.orbFactory.p1voids);
        logMap.put("p2voids",this.orbFactory.p2voids);
        logMap.put("p1novas",this.orbFactory.p1novas);
        logMap.put("p2novas",this.orbFactory.p2novas);
        StarCycle.logHandler.logGame(json.toJson(logMap));
    }

	
	public void addPauseBanner() {
		if (!paused && !gameOver){
			paused = true;
			ui.addActor(backButton);
			ui.addActor(resumeButton);
			ui.addActor(mainMenuButton);
		}
	}
	
	public void launch(OrbType orbType, Player player) {
		orbFactory.launch(orbType, player);
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
		
		//long start = System.currentTimeMillis();
		//super.render(delta);
		if (dbRender){
			debugRenderer.render(model.world,cam.combined); // TODO remove in final version
		}
		
		// TODO remove in final
		/*long start = System.currentTimeMillis();
		displayFPS(start);
		printFPS(start); // log fps to console output */
		
		if (!debugPaused) {
			if ((!gameOver) & (!paused)) {
				// while the gpu renders, we update the world physics and manage collisions
				update(delta);
			}
            if (gameOver){
                for (Player player : players) {
                    player.updateWinOrbs(delta);
                }
            }

			background.update(); // Moves while paused.
		}
	}
	
	public void update(float delta) {
		
		model.update(delta); // calls level.update
		
		// update players
        for (Player player : players) {
            player.update(delta, model.stars, model.starPositions);
            // check for win
            if (player.win) {
                gameOver = true;
                addWinBanner(player);
                player.setWinner();
            }
        }
	}
	
	void renderSprites(float delta) {
//		batch.getProjectionMatrix().set(cam.combined);
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
		for (int i=0; i < model.explosions.size(); i++) {
			model.explosions.get(i).draw(batch,delta,model.explosions);
		}
		batch.end();
	}
	
	void displayFPS(float start) {
		float updateTime = (System.currentTimeMillis() - start) / 1000.0f;
		batch.getProjectionMatrix().setToOrtho2D(0,0, StarCycle.meterWidth*StarCycle.pixelsPerMeter, StarCycle.meterHeight*StarCycle.pixelsPerMeter);
		batch.begin();
		Integer numOrbs = 0;
        for (Player player : players) {
            numOrbs += players[0].orbs.size();
        }
		Texturez.font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + " update time: " + 
				updateTime + " orbs: " + numOrbs + "", 0, 20);
		batch.end();
	}
	
	void printFPS(float start) {
		float updateTime = (System.currentTimeMillis() - start) / 1000.0f;
		batch.getProjectionMatrix().setToOrtho2D(0,0, StarCycle.meterWidth*StarCycle.pixelsPerMeter, StarCycle.meterHeight*StarCycle.pixelsPerMeter);
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
