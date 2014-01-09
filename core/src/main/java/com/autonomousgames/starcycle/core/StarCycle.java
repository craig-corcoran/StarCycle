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
	private GameScreen screen;
	public static com.autonomousgames.starcycle.core.LogHandler logHandler;
	public static long startTime;
	public static StarCycle sc = new StarCycle(); // TODO is this a bad idea during restarts/regaining context
	public static float padding;
	public com.autonomousgames.starcycle.core.UserSettingz settings;
	public static Json json = new Json();
    public BackgroundManager background;

	@Override
	public void create() {
		json.setOutputType(JsonWriter.OutputType.json);
		pixelsPerMeter = Gdx.graphics.getHeight()/10f;
		padding = pixelsPerMeter * com.autonomousgames.starcycle.core.UserSettingz.getFloatSetting("paddingMeters"); // relative padding size for UI buttons
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		meterWidth = screenWidth/pixelsPerMeter;
		meterHeight = screenHeight/pixelsPerMeter;
		
		meterScreenCenter = new Vector2(meterWidth/2f, meterHeight/2f);
		pixelScreenCenter = new Vector2(screenWidth/2f, screenHeight/2f);
		screen = new com.autonomousgames.starcycle.core.screens.SplashScreen();

		logHandler = new com.autonomousgames.starcycle.core.LogHandler();
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
		Soundz.dispose();
		Texturez.dispose();
		screen.dispose();
		// TODO need to dispose sounds and textures?
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		screen.render(1/30f);
		if (screen.isDone) {
			logHandler.writeLogs();
			HashMap<String,Object> logMap = new HashMap<String,Object>();
			logMap.put("currentScreen", screen.toString());
			logMap.put("sessionTime", StarCycle.startTime);
			logMap.put("currentTime", System.currentTimeMillis());
			logHandler.logScreen(json.toJson(logMap));
			// dispose the current screen
			Soundz.screenswitchSound.play(UserSettingz.getFloatSetting("sfxVolume"));
					
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
			case TUTORIAL1:
				screen = new Tutorial1();
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
				//screen = new MainMenu();
			}
		}
	}
	@Override
	public void resize(int width, int height) {

	}
	@Override
	public void resume() {
        Texturez.resetFonts();
	}

    public void makeBackground() {
        background = new BackgroundManager();
    }

    public BackgroundManager getBackground() {
        return background;
    }

}
