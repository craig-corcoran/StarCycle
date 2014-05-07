package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.log.LogHandler;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.log.UserId;
import com.autonomousgames.starcycle.core.log.UserProgress;
import com.autonomousgames.starcycle.core.model.BackgroundManager;
import com.autonomousgames.starcycle.core.network.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.autonomousgames.starcycle.core.screens.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Vector;

public class StarCycle implements ApplicationListener {
	public static float pixelsPerMeter;  
	public static float screenWidth;   
	public static float screenHeight; 
	public static float meterWidth;
	public static float meterHeight;
	public static Vector2 meterScreenCenter;
	public static Vector2 pixelScreenCenter;
    public static StarCycleAssetManager assetManager;
	public static LogHandler logHandler;
	public static long startTime;
	public static float padding;
    public static Soundz audio;
    public static Texturez tex;
    public static Json json = new Json();
    public static UserId uidHandler;
    public static UserProgress progressHandler;
    public static boolean virgin = false;
    public static String version = "00.00.03";
    private static BackgroundManager background;
    private GameScreen screen;
    boolean startAtEnd;
    private long lastChange;
    private StarCycle.Mode mode;
    private DatagramSocket stateSenderSocket;
    private Vector<NodeUDPInfo> nodeAddressVector;
    private StateSender stateSender;
    private StateReceiver stateReceiver;
    private TCPServerHandler serverHandler;
    public static Integer playerNum = -1;

    public enum Mode{
    	kClient,
    	kServer
    }

    public StarCycle(){
        this(StarCycle.Mode.kClient);
    }

    public StarCycle(StarCycle.Mode startMode){
        super();
        mode = startMode;
    }

    public StarCycle(StarCycle.Mode startMode, Vector<NodeUDPInfo> nodeAddressVector){
        super();
        mode = startMode;
        this.nodeAddressVector = nodeAddressVector;
    }

    @Override
	public void create() {
        lastChange = System.currentTimeMillis();
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
        Gdx.app.log("StarCycle", mode.toString());
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
            String[] screenLogEntry = {
                    screen.toString(), // screen type
                    StarCycle.startTime + "", // session start time
                    (System.currentTimeMillis() - lastChange) + "" // duration on screen
            };
            lastChange = System.currentTimeMillis();
            logHandler.logScreen(json.toJson(screenLogEntry));
            if (!screen.silentSwitch) {
			    audio.screenswitchSound.play(audio.sfxVolume);
            }
            startAtEnd = (screen instanceof Tutorial) && ((Tutorial) screen).startAtEnd;
            if (screen instanceof NetworkedPregame && screen.nextScreen.equals(ScreenType.MULTIPLAYERMODESELECT)){
                killConnections();
            }
			screen.dispose();

			switch (screen.nextScreen) {
			case MULTIPLAYER:
				screen = new MultiPlayer(screen.nextLvlConfig, screen.skins, screen.colors, this); // TODO use factory methods instead of constructor? may not have to repeatedly make objects
				break;
			case SINGLEPLAYER:
				screen = new SinglePlayer(screen.nextLvlConfig, screen.skins, screen.colors,
										  ((CampaignSelect)screen).nextLvlSinglePlayer,
										  ((CampaignSelect)screen).botType, this);
				break;
			case TUTORIAL0:
				screen = new Tutorial0(startAtEnd, this);
				break;
			case TUTORIAL1:
				screen = new Tutorial1(this);
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
                screen = new NetworkedPregame(mode, nodeAddressVector);
                setConnections();
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

    public void setConnections() {
        if (mode == StarCycle.Mode.kServer) {
            try {
                this.stateReceiver = new StateReceiver(new DatagramSocket(nodeAddressVector.get(0).getPort()), mode);
                Thread thread = new Thread (this.stateReceiver);
                thread.start();
                int port = nodeAddressVector.get(0).getPort();
                serverHandler = new TCPServerHandler(port);
                serverHandler.start();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            try {
                this.stateSenderSocket = new DatagramSocket();
            }
            catch (SocketException e)
            {
                Gdx.app.log ("StarCycle", "Exception creating server send socket");
            }
            this.stateSender = new StateSender(stateSenderSocket, nodeAddressVector, mode);
        }
        else {
            try {
                this.stateSenderSocket = new DatagramSocket();
            }
            catch (SocketException e)
            {
                Gdx.app.log ("StarCycle", "Exception creating client send socket");
            }
            this.stateSender = new StateSender(stateSenderSocket, nodeAddressVector, mode);
            initClientReceiver();
        }
    }

    public void initClientReceiver() {
        for (int i=0; i < nodeAddressVector.size(); i++) {
            try {
                // Hard-coding the
                if (i > 0) {
                    if (nodeAddressVector.get(i).getIp().toString().equals("/127.0.0.1")){
                        this.stateReceiver = new StateReceiver(new DatagramSocket(nodeAddressVector.get(i).getPort()), mode);
                        Gdx.app.log("StarCycle", "listening " + nodeAddressVector.get(i).getIp() + ":" + nodeAddressVector.get(i).getPort());
                        Thread thread = new Thread (this.stateReceiver);
                        thread.start();
                        break;
                    }
                }
            }
            catch (IOException e) {
                Gdx.app.log("StarCycle", e.getMessage());
            }
        }
    }

    public void killConnections() {
        this.stateReceiver = null;
        this.stateSender = null;
        this.stateSenderSocket = null;
        this.serverHandler.interrupt();
    }

    public static BackgroundManager getBackground() {
        return background;
    }

    public StarCycle.Mode getMode()
    {
        return mode;
    }

    public StateSender getStateSender (){ return stateSender; }

    public StateReceiver getStateReceiver(){ return stateReceiver;}
}
