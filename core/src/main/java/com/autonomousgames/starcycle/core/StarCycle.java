package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.model.BackgroundManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.autonomousgames.starcycle.core.screens.*;

import java.util.HashMap;

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
	public static com.autonomousgames.starcycle.core.LogHandler logHandler;
	public static long startTime;
	public static float padding;
    public static Soundz audio;
    public static Texturez tex;
	public UserSettingz settings;
	public static Json json = new Json();

	@Override
	public void create() {
		json.setOutputType(JsonWriter.OutputType.json);
		pixelsPerMeter = Gdx.graphics.getHeight()/10f;
		padding = pixelsPerMeter * UserSettingz.getFloatSetting("paddingMeters"); // relative padding size for UI buttons
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

		logHandler = new LogHandler();
		startTime = System.currentTimeMillis();
		HashMap<String,Object> logMap = new HashMap<String,Object>();
		logMap.put("currentScreen", screen.toString());
		logMap.put("sessionTime", StarCycle.startTime);
		logMap.put("currentTime", System.currentTimeMillis());
		logHandler.logScreen(json.toJson(logMap));
		Gdx.app.log("msg",logHandler.toString());
		logHandler.processLogs();
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
			HashMap<String,Object> logMap = new HashMap<String,Object>();
			logMap.put("currentScreen", screen.toString());
			logMap.put("sessionTime", StarCycle.startTime);
			logMap.put("currentTime", System.currentTimeMillis());
			logHandler.logScreen(json.toJson(logMap));
			// dispose the current screen
			audio.screenswitchSound.play(UserSettingz.getFloatSetting("sfxVolume"));
					
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
				screen = new Tutorial0();
				break;
			case TUTORIAL2:
				screen = new Tutorial2();
				break;
			case TUTORIAL3:
				screen = new Tutorial3();
				break;
			case TUTORIAL4:
				screen = new Tutorial4();
				break;
			case TUTORIAL5:
				screen = new Tutorial5();
				break;
			case TUTORIAL6:
				screen = new Tutorial6();
				break;
			case TUTORIAL7:
				screen = new Tutorial7();
				break;
			case TUTORIAL8:
				screen = new Tutorial8();
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
			case MULTIPLAYERSELECT:
				screen = new MultiplayerSelect(screen.nextLvlConfig, screen.skins, screen.colors);
				break;
			default:
				throw new AssertionError("No valid next screen set when scren isDone is true");
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
