package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.log.LogHandler;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.log.UserId;
import com.autonomousgames.starcycle.core.log.UserProgress;
import com.autonomousgames.starcycle.core.model.BackgroundManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.autonomousgames.starcycle.core.screens.*;

public class StarCycle implements ApplicationListener {
	public static float pixelsPerMeter;  
	public static float screenWidth;   
	public static float screenHeight; 
	public static float meterWidth;
	public static float meterHeight;
	public static Vector2 meterScreenCenter;
	public static Vector2 pixelScreenCenter;
    public static StarCycleAssetManager assetManager;
    private static BackgroundManager background;
    private GameScreen screen;
	public static LogHandler logHandler;
	public static long startTime;
	public static float padding;
    public static Soundz audio;
    public static Texturez tex;
    public static Json json = new Json();
    boolean startAtEnd;
    public static UserId uidHandler;
    public static UserProgress progressHandler;
    public static boolean virgin = false;

    @Override
	public void create() {
		json.setOutputType(JsonWriter.OutputType.json);
		pixelsPerMeter = Gdx.graphics.getHeight()/10f;
		padding = pixelsPerMeter * ModelSettings.getFloatSetting("paddingMeters"); // relative padding size for UI buttons
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		meterWidth = screenWidth/pixelsPerMeter;
		meterHeight = screenHeight/pixelsPerMeter;
        assetManager = new StarCycleAssetManager();
        audio = new Soundz();
		tex = new Texturez();
        meterScreenCenter = new Vector2(meterWidth/2f, meterHeight/2f);
		pixelScreenCenter = new Vector2(screenWidth/2f, screenHeight/2f);
		screen = new SplashScreen();
        uidHandler = new UserId();
        progressHandler = new UserProgress();
		logHandler = new LogHandler();
		startTime = System.currentTimeMillis();
		logHandler.run();
	}

	@Override
    public void dispose() {
		Gdx.app.log("StarCycle", "root dispose called");
        screen.dispose();
        assetManager.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		screen.render(1/30f);
        assetManager.update();
		if (screen.isDone) {
			logHandler.writeLogs();
            String[] screenLogEntry = {screen.toString(), StarCycle.startTime + "", System.currentTimeMillis() + ""};
            logHandler.logScreen(json.toJson(screenLogEntry));
            if (!screen.silentSwitch) {
			    audio.screenswitchSound.play(audio.sfxVolume);
            }
			if (screen instanceof Tutorial) {
                startAtEnd = ((Tutorial) screen).startAtEnd;
            }
            else
            {
                startAtEnd = false;
            }
			screen.dispose();

			switch (screen.nextScreen) {
			case MULTIPLAYER:
				screen = new MultiPlayer(screen.nextLvlConfig, screen.skins, screen.colors); // TODO use factory methods instead of constructor? may not have to repeatedly make objects
				break;
			case SINGLEPLAYER:
				screen = new SinglePlayer(screen.nextLvlConfig, screen.skins, screen.colors,
										  ((CampaignSelect)screen).nextLvlSinglePlayer,
										  ((CampaignSelect)screen).botType);
				break;
			case TUTORIAL0:
				screen = new Tutorial0(startAtEnd);
				break;
			case TUTORIAL1:
				screen = new Tutorial1();
				break;
			case ABOUT:
				screen = new AboutScreen();
				break;
			case SETTINGS:
				screen = new SettingsScreen();
				break;
			case MAINMENU:
				screen = new MainMenu();
				break;
			case STARTMENU:
				screen = new StartMenu();
				break;
			case CAMPAIGNSELECT:
				screen = new CampaignSelect();
				break;
            case MULTIPLAYERLEVELSELECT:
                screen = new MultiplayerSelect(screen.nextLvlConfig, screen.skins, screen.colors);
                break;
            case NETWORKEDPREGAME:
                screen = new NetworkedPregame();
                break;
			case MULTIPLAYERMODESELECT:
				screen = new MultiplayerModeScreen();
				break;
			default:
				throw new AssertionError("No valid next screen set when screen isDone is true");
			}
		}
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
        tex.resetFonts();
	}

    public static void makeBackground() {
        background = new BackgroundManager();
    }

    public static BackgroundManager getBackground() {
        return background;
    }
}
