package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class Texturez {
	
	public enum TextureType {
		ORB0, ORB1, VOID0, VOID1, NOVA0, NOVA1, CONTROL}
	
	private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("packed-images/packed-textures.atlas"));
	
	// TODO  get rid of extra textures
		
	// TUTORIAL (old?)
	public static final AtlasRegion tutorialTextBox = atlas.findRegion("tutorial-text-box");
	public static final AtlasRegion fingerRight = atlas.findRegion("icon-finger"); // 3:4
	public static final TextureRegion fingerLeft;// = atlas.findRegion("icon-finger"); // 3:4
	public static final AtlasRegion moreIcon = atlas.findRegion("icon-more"); // 4:1
	// new?
	private static final AtlasRegion tutorial0 = atlas.findRegion("holdinghands");
	private static final AtlasRegion tutorial1 = atlas.findRegion("aim1");
	private static final AtlasRegion tutorial2 = atlas.findRegion("aim2");
	private static final AtlasRegion tutorial3 = atlas.findRegion("shoot1");
	private static final AtlasRegion tutorial4 = atlas.findRegion("shoot2");
	private static final AtlasRegion tutorial5 = atlas.findRegion("dontcrash");
	public static final AtlasRegion[] tutorialImages = new AtlasRegion[] {tutorial0, tutorial1, tutorial2, tutorial3, tutorial4, tutorial5};
	
	
	// COLORS
	public static Color cyan = new Color(0f, 0.678f, 0.933f, 1f);
	public static Color navy = new Color(0.18f, 0.192f, 0.569f, 1f);
	public static Color yellow = new Color(1f, 0.867f, 0.082f, 1f);
	public static Color red = new Color(0.745f, 0.118f, 0.176f, 1f);
	public static Color smoke = new Color(0.533f, 0.533f, 0.533f, 1f);
	public static Color charcoal = new Color(0.4f, 0.4f, 0.4f, 1f);
	public static Color night = new Color(0.267f, 0.267f, 0.267f, 1f);
	public static Color matcha = new Color(0.624f, 0.925f, 0.443f, 1f);
	public static Color spinach = new Color(0f, 0.51f, 0.133f, 1f);
	public static Color magenta = new Color(0.769f, 0f, 0.471f, 1f);
	public static Color indigo = new Color(0.341f, 0.267f, 0.918f, 1f);
	public static Color copper = new Color(0.784f, 0.384f, 0f, 1f);
	public static Color bronze = new Color(0.918f, 0.762f, 0.212f, 1f);
	public static Color[] cool = new Color[]{cyan, navy};
	public static Color[] warm = new Color[]{yellow, red};
	public static Color[] neutral = new Color[]{Color.WHITE, smoke};
	public static Color[] leafy = new Color[]{matcha, spinach};
	public static Color[] floral = new Color[]{magenta, indigo};
	public static Color[] metallic = new Color[]{copper, bronze};
	public static ArrayList<Color[]> colors = new ArrayList<Color[]>();
		
	// ORBS & POWERUPS (UPDATED)
	static final AtlasRegion explosionTexture = atlas.findRegion("explode");
	private static final AtlasRegion orbMaluma0 = atlas.findRegion("orb-mal0");
	private static final AtlasRegion orbMaluma1 = atlas.findRegion("orb-mal1");
	private static final AtlasRegion orbTakete0 = atlas.findRegion("orb-tak0");
	private static final AtlasRegion orbTakete1 = atlas.findRegion("orb-tak1");
	private static final AtlasRegion orbTarget0 = atlas.findRegion("orb-tar0");
	private static final AtlasRegion orbTarget1 = atlas.findRegion("orb-tar1");
	private static final AtlasRegion orbDerelict0 = atlas.findRegion("orb-der0");
	private static final AtlasRegion orbDerelict1 = atlas.findRegion("orb-der1");
	private static final AtlasRegion orbClockwork0 = atlas.findRegion("orb-clo0");
	private static final AtlasRegion orbClockwork1 = atlas.findRegion("orb-clo1");
	private static final AtlasRegion voidMaluma0 = atlas.findRegion("void-mal0");
	private static final AtlasRegion voidMaluma1 = atlas.findRegion("void-mal1");
	private static final AtlasRegion voidTakete0 = atlas.findRegion("void-tak0");
	private static final AtlasRegion voidTakete1= atlas.findRegion("void-tak1");
	private static final AtlasRegion voidTarget0 = atlas.findRegion("void-tar0");
	private static final AtlasRegion voidTarget1 = atlas.findRegion("void-tar1");
	private static final AtlasRegion voidDerelict0 = atlas.findRegion("void-der0");
	private static final AtlasRegion voidDerelict1 = atlas.findRegion("void-der1");
	private static final AtlasRegion voidClockwork0 = atlas.findRegion("void-clo0");
	private static final AtlasRegion voidClockwork1 = atlas.findRegion("void-clo1");
	private static final AtlasRegion novaMaluma0 = atlas.findRegion("nova-mal0");
	private static final AtlasRegion novaMaluma1 = atlas.findRegion("nova-mal1");
	private static final AtlasRegion novaTakete0 = atlas.findRegion("nova-tak0");
	private static final AtlasRegion novaTakete1 = atlas.findRegion("nova-tak1");
	private static final AtlasRegion novaTarget0 = atlas.findRegion("nova-tar0");
	private static final AtlasRegion novaTarget1 = atlas.findRegion("nova-tar1");
	private static final AtlasRegion novaDerelict0 = atlas.findRegion("nova-der0");
	private static final AtlasRegion novaDerelict1 = atlas.findRegion("nova-der1");
	private static final AtlasRegion novaClockwork0 = atlas.findRegion("nova-clo0");
	private static final AtlasRegion novaClockwork1 = atlas.findRegion("nova-clo1");
	private static final AtlasRegion fakeredOrbTexture = atlas.findRegion("fake-orb-red");
	private static final AtlasRegion fakeblueOrbTexture = atlas.findRegion("fake-orb-blue");
	public static final AtlasRegion chargeBeam = atlas.findRegion("charge");
	public static final AtlasRegion voidRing = atlas.findRegion("voidring");

	public static final AtlasRegion[] fakeorbTextures = new AtlasRegion[] {fakeblueOrbTexture, fakeredOrbTexture};

    // LAUNCHPAD (NEW)
    private static final AtlasRegion launchBackground = atlas.findRegion("launchpad-bg");
    private static final AtlasRegion launchFill = atlas.findRegion("launchpad-fillmeter");
    private static final AtlasRegion launchMeter = atlas.findRegion("launchpad-bordermeter");
    private static final AtlasRegion launchButton1 = atlas.findRegion("launchpad-orb");
    private static final AtlasRegion launchButton2 = atlas.findRegion("launchpad-powerup");
    private static final AtlasRegion launchTriangle = atlas.findRegion("launchpad-mark");
    public static final AtlasRegion[] launchTextures = new AtlasRegion[]{launchBackground, launchFill, launchMeter, launchButton1, launchButton2, launchTriangle};

	// LAUNCHPAD (OLD)
	public static final AtlasRegion launchBlip = atlas.findRegion("launchpad-blip"); // Is this being used anywhere?

	// AIMING UI (UPDATED)
	private static final AtlasRegion handle = atlas.findRegion("handle");
	private static final AtlasRegion chevron0 = atlas.findRegion("chevron0");
	private static final AtlasRegion chevron1 = atlas.findRegion("chevron1");
	public static final AtlasRegion[] aimer = new  AtlasRegion[]{handle, chevron0, chevron1};

    // BACKGROUND (NEW)
    public static final AtlasRegion mainMenuBG = atlas.findRegion("starcycle-bg");
    public static final AtlasRegion selectBG = atlas.findRegion("levelselect");
    private static final String[] bgBlockNames = new String[]{"disco", "nebula", "hexnut"};
    public static final int[] bgBlockDims = new int[]{6, 6};
    public static final ArrayList<TextureRegion[]> bgBlocks = new ArrayList<TextureRegion[]>();
    private static final String[] bgStripNames = new String[]{"bubbles", "bacon", "lobe", "ribbon", "triangles"};
    public static final int[] bgStripDims = new int[]{6, 2};
    public static final ArrayList<TextureRegion[]> bgStrips = new ArrayList<TextureRegion[]>();
    public static final int[] bgStarsDims = new int[]{8, 13};
    public static final TextureRegion[] bgStars = bgTile("stardust", bgStarsDims[0], bgStarsDims[1]);

	// BACKGROUND
//	private static final String[] bgBlockNames = new String[]{"purp","blue","green"};
	public static final AtlasRegion bgMote = atlas.findRegion("mote");
	public static final AtlasRegion bgPent = atlas.findRegion("pent");
	private static final AtlasRegion bgJagged1 = atlas.findRegion("jaggedband1");
	private static final AtlasRegion bgJagged2 = atlas.findRegion("jaggedband2");
	private static final AtlasRegion bgCurve = atlas.findRegion("smoothband");
//	public static final AtlasRegion[] bgStrips = new AtlasRegion[]{bgJagged1,bgJagged2,bgCurve,bgBubbles,bgLobe};

	
	// FONTS
	public static final BitmapFont font = new BitmapFont();
	//public static final BitmapFont test_font = new BitmapFont//new BitmapFont(Gdx.files.internal("fonts/gridnik-yellow.fnt"), Gdx.files.internal("fonts/gridnik-yellow.png"), false); // size 52 i think
	//public static final BitmapFont gridnik_yellow = new BitmapFont(Gdx.files.internal("fonts/gridnik-yellow.fnt"), Gdx.files.internal("fonts/gridnik-yellow.png"), false); // size 52 i think
	//public static final BitmapFont gridnik_cyan = new BitmapFont(Gdx.files.internal("fonts/gridnik-cyan.fnt"), Gdx.files.internal("fonts/gridnik-cyan.png"), false); //size 52 i think
	//public static final BitmapFont gridnik_navy = new BitmapFont(Gdx.files.internal("fonts/gridnik-navy.fnt"), Gdx.files.internal("fonts/gridnik-navy.png"), false);
	//public static final BitmapFont gridnik_red = new BitmapFont(Gdx.files.internal("fonts/gridnik-red.fnt"), Gdx.files.internal("fonts/gridnik-red.png"), false);
	//public static final BitmapFont gridnik_white = new BitmapFont(Gdx.files.internal("fonts/gridnik-white.fnt"), Gdx.files.internal("fonts/gridnik-white.png"), false, false);
	//public static final BitmapFont gridnik_gray = new BitmapFont(Gdx.files.internal("fonts/gridnik-gray.fnt"), Gdx.files.internal("fonts/gridnik-gray.png"), false);
	
	//public static final BitmapFont[] playerFonts = new BitmapFont[] {gridnik_cyan, gridnik_yellow, gridnik_navy, gridnik_red, gridnik_white, gridnik_gray};
	
	// MENU UI
	public static final AtlasRegion splashTexture = atlas.findRegion("ag-logo");	
	public static final AtlasRegion logo = atlas.findRegion("logo");
	public static final AtlasRegion genericLogo = atlas.findRegion("generic-starcycle-logo");
	public static final AtlasRegion questionIcon = atlas.findRegion("icon-question");
	public static final AtlasRegion settingsIcon = atlas.findRegion("icon-tools");
	public static final AtlasRegion soloIcon = atlas.findRegion("icon-solo");
	public static final AtlasRegion multiplayerIcon = atlas.findRegion("icon-multi"); // 4:3
	public static final AtlasRegion musicIcon = atlas.findRegion("icon-music");
	public static final AtlasRegion soundIcon = atlas.findRegion("icon-bell");
	public static final AtlasRegion noIcon = atlas.findRegion("icon-no");
	public static final AtlasRegion infoIcon = atlas.findRegion("icon-info");
	public static final AtlasRegion swapIcon = atlas.findRegion("icon-swap");
	public static final AtlasRegion swatchIcon = atlas.findRegion("icon-swatches");
	public static final AtlasRegion sliderIcon = atlas.findRegion("icon-slider"); // 2:1
	public static final AtlasRegion backIcon = atlas.findRegion("icon-back"); // 4:3
	public static final AtlasRegion readyIcon = atlas.findRegion("icon-check");
	public static final AtlasRegion lockIcon = atlas.findRegion("icon-lock");
	public static final AtlasRegion rightIcon = atlas.findRegion("icon-altcheck");
	public static final AtlasRegion wrongIcon = atlas.findRegion("icon-x");
	private static final AtlasRegion trefoilIcon = atlas.findRegion("trefoilselect");
	private static final AtlasRegion doublebinaryIcon = atlas.findRegion("doublebinaryselect");
	private static final AtlasRegion concentricIcon = atlas.findRegion("concentricselect");
	private static final AtlasRegion venndiagramIcon = atlas.findRegion("venndiagramselect");
	private static final AtlasRegion trefoilIconOn = atlas.findRegion("trefoilselectOn");
	private static final AtlasRegion doublebinaryIconOn = atlas.findRegion("doublebinaryselectOn");
	private static final AtlasRegion concentricIconOn = atlas.findRegion("concentricselectOn");
	private static final AtlasRegion venndiagramIconOn = atlas.findRegion("venndiagramselectOn");
	public static final AtlasRegion[] levelIconsOff = new AtlasRegion[] {trefoilIcon, doublebinaryIcon, concentricIcon, venndiagramIcon};
	public static final AtlasRegion[] levelIconsOn = new AtlasRegion[] {trefoilIconOn, doublebinaryIconOn, concentricIconOn, venndiagramIconOn};
	
	// NEBULAE
//	private static final AtlasRegion nebulaTutorial0 = atlas.findRegion("tutorial0");
	public static final AtlasRegion threeSpheres = atlas.findRegion("tutorial1");
	public static final AtlasRegion nebula0 = atlas.findRegion("nebulacloud");
	public static final AtlasRegion nebula1 = atlas.findRegion("nebulacloud2");
//	public static final AtlasRegion[] nebulae = new AtlasRegion[] {nebulaCloud0, nebulaCloud1, nebulaCloud2, nebulaCloud3};

	// BASIC UI
	public static final AtlasRegion line = atlas.findRegion("line");
	public static final AtlasRegion block = atlas.findRegion("block");
    public static final AtlasRegion circle = atlas.findRegion("solidcircle");
    public static final AtlasRegion gradientRound = atlas.findRegion("gradient-round");
	
	// IN-GAME PAUSE UI
	public static final AtlasRegion menuIcon = atlas.findRegion("icon-menu");
	public static final AtlasRegion pauseIcon = atlas.findRegion("icon-pause"); // 3:4
	public static final AtlasRegion arrowsIcon = atlas.findRegion("icon-cycle");
	public static final AtlasRegion[] pauseUI = new AtlasRegion[]{menuIcon,pauseIcon,arrowsIcon,backIcon};
	public static final AtlasRegion trophy = atlas.findRegion("icon-trophy");
	public static final AtlasRegion thumbsDown = atlas.findRegion("icon-thumb");
	
	// STARS
	public static final AtlasRegion hexStar = atlas.findRegion("starquarter");

	// BASE SHAPES
	public static final AtlasRegion baseMaluma0 = atlas.findRegion("base-maluma0");
	public static final AtlasRegion baseMaluma1 = atlas.findRegion("base-maluma1");
	
	public static final AtlasRegion baseTakete0 = atlas.findRegion("base-takete0");
	public static final AtlasRegion baseTakete1 = atlas.findRegion("base-takete1");
	
	public static final AtlasRegion baseTarget0 = atlas.findRegion("base-target0");
	public static final AtlasRegion baseTarget1 = atlas.findRegion("base-target1");
	public static final AtlasRegion baseTarget2 = atlas.findRegion("base-target2");
	public static final AtlasRegion baseTarget3 = atlas.findRegion("base-target3");

	public static final AtlasRegion baseDerelict0a = atlas.findRegion("base-derelict0a");
	public static final AtlasRegion baseDerelict0b = atlas.findRegion("base-derelict0b");
	public static final AtlasRegion baseDerelict1a = atlas.findRegion("base-derelict1a");
	public static final AtlasRegion baseDerelict1b = atlas.findRegion("base-derelict1b");
	public static final AtlasRegion baseDerelict2a = atlas.findRegion("base-derelict2a");
	public static final AtlasRegion baseDerelict2b = atlas.findRegion("base-derelict2b");
	
	public static final AtlasRegion baseClockwork0a = atlas.findRegion("base-clockwork0a");
	public static final AtlasRegion baseClockwork0b = atlas.findRegion("base-clockwork0b");
	public static final AtlasRegion baseClockwork1a = atlas.findRegion("base-clockwork1a");
	public static final AtlasRegion baseClockwork1b = atlas.findRegion("base-clockwork1b");
	public static final AtlasRegion baseClockwork2a = atlas.findRegion("base-clockwork2a");
	public static final AtlasRegion baseClockwork2b = atlas.findRegion("base-clockwork2b");
	public static final AtlasRegion baseClockwork2c = atlas.findRegion("base-clockwork2c");
	
	
	//SKIN HASHMAPS
	public static HashMap<BaseType, HashMap<TextureType,AtlasRegion>> skinMap = new HashMap<BaseType, HashMap<TextureType,AtlasRegion>>();
	
	private static HashMap<TextureType, AtlasRegion> malumaMap = new HashMap<TextureType,AtlasRegion>();
	private static HashMap<TextureType, AtlasRegion> taketeMap = new HashMap<TextureType,AtlasRegion>();
	private static HashMap<TextureType, AtlasRegion> targetMap = new HashMap<TextureType,AtlasRegion>();
	private static HashMap<TextureType, AtlasRegion> derelictMap = new HashMap<TextureType,AtlasRegion>();
	private static HashMap<TextureType, AtlasRegion> clockworkMap = new HashMap<TextureType,AtlasRegion>();

	public static BitmapFont gridnikMedium;
	public static BitmapFont gridnikLarge;
	public static BitmapFont latoLightLarge;
	private static FreeTypeFontGenerator gridnikGenerator;
	private static FreeTypeFontGenerator latoLightGenerator;
	static {
		gridnikGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Gridnik.ttf"));
		latoLightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Lato-Light.ttf"));
		gridnikMedium = gridnikGenerator.generateFont(22);
		gridnikLarge = gridnikGenerator.generateFont(42);
		latoLightLarge = latoLightGenerator.generateFont(52);
		fingerLeft = new TextureRegion(fingerRight);
		fingerLeft.flip(true, false);
		
		//Add colors to color list
		colors.add(cool);
		colors.add(warm);
		colors.add(leafy);
		colors.add(floral);
		colors.add(neutral);
		colors.add(metallic);
		
		//PUT SKIN SUPERHASHMAP TOGETHER (ONE FOR EACH BaseType)
		skinMap.put(BaseType.MALUMA, malumaMap);
		skinMap.put(BaseType.TAKETE, taketeMap);
		skinMap.put(BaseType.TARGET, targetMap);
		skinMap.put(BaseType.DERELICT, derelictMap);
		skinMap.put(BaseType.CLOCKWORK, clockworkMap);		
		
		//MALUMA MAP
		skinMap.get(BaseType.MALUMA).put(TextureType.ORB0, orbMaluma0);
		skinMap.get(BaseType.MALUMA).put(TextureType.ORB1, orbMaluma1);
		skinMap.get(BaseType.MALUMA).put(TextureType.VOID0, voidMaluma0);
		skinMap.get(BaseType.MALUMA).put(TextureType.VOID1, voidMaluma1);
		skinMap.get(BaseType.MALUMA).put(TextureType.NOVA0, novaMaluma0);
		skinMap.get(BaseType.MALUMA).put(TextureType.NOVA1, novaMaluma1);
//		skinMap.get(BaseType.MALUMA).put(TextureType.CONTROL, controlMaluma);
		
		//TAKETE MAP
		skinMap.get(BaseType.TAKETE).put(TextureType.ORB0, orbTakete0);
		skinMap.get(BaseType.TAKETE).put(TextureType.ORB1, orbTakete1);
		skinMap.get(BaseType.TAKETE).put(TextureType.VOID0, voidTakete0);
		skinMap.get(BaseType.TAKETE).put(TextureType.VOID1, voidTakete1);
		skinMap.get(BaseType.TAKETE).put(TextureType.NOVA0, novaTakete0);
		skinMap.get(BaseType.TAKETE).put(TextureType.NOVA1, novaTakete1);
//		skinMap.get(BaseType.TAKETE).put(TextureType.CONTROL, controlTakete);
		
		//TARGET MAP
		skinMap.get(BaseType.TARGET).put(TextureType.ORB0, orbTarget0);
		skinMap.get(BaseType.TARGET).put(TextureType.ORB1, orbTarget1);
		skinMap.get(BaseType.TARGET).put(TextureType.VOID0, voidTarget0);
		skinMap.get(BaseType.TARGET).put(TextureType.VOID1, voidTarget1);
		skinMap.get(BaseType.TARGET).put(TextureType.NOVA0, novaTarget0);
		skinMap.get(BaseType.TARGET).put(TextureType.NOVA1, novaTarget1);
//		skinMap.get(BaseType.TARGET).put(TextureType.CONTROL, controlTarget);
		
		//DERELICT MAP
		skinMap.get(BaseType.DERELICT).put(TextureType.ORB0, orbDerelict0);
		skinMap.get(BaseType.DERELICT).put(TextureType.ORB1, orbDerelict1);
		skinMap.get(BaseType.DERELICT).put(TextureType.VOID0, voidDerelict0);
		skinMap.get(BaseType.DERELICT).put(TextureType.VOID1, voidDerelict1);
		skinMap.get(BaseType.DERELICT).put(TextureType.NOVA0, novaDerelict0);
		skinMap.get(BaseType.DERELICT).put(TextureType.NOVA1, novaDerelict1);
//		skinMap.get(BaseType.DERELICT).put(TextureType.CONTROL, controlDerelict);
		
		//CLOCKWORK MAP
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.ORB0, orbClockwork0);
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.ORB1, orbClockwork1);
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.VOID0, voidClockwork0);
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.VOID1, voidClockwork1);
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.NOVA0, novaClockwork0);
		skinMap.get(BaseType.CLOCKWORK).put(TextureType.NOVA1, novaClockwork1);
//		skinMap.get(BaseType.CLOCKWORK).put(TextureType.CONTROL, controlClockwork);
		

//		// PARSE BACKGROUND TILES
        for (String bgBlockName : bgBlockNames) {
            bgBlocks.add(bgTile(bgBlockName, bgBlockDims[0], bgBlockDims[1]));
        }
        for (String bgStripName : bgStripNames) {
            bgStrips.add(bgTile(bgStripName, bgStripDims[0], bgStripDims[1]));
        }

	}
	private static TextureRegion[] bgTile(String name, int rows, int cols) {
		final TextureRegion[] texture = new TextureRegion[rows*cols];
		AtlasRegion textureAtlas = atlas.findRegion(name);
		TextureRegion[][] nestedTiles = textureAtlas.split(textureAtlas.getRegionWidth()/cols, textureAtlas.getRegionHeight()/rows);
		for (int i = 0; i < rows*cols; i++) {
			texture[i] = nestedTiles[i/cols][i%cols];
		}
		return texture;
	}
		
	public static void dispose() {
		atlas.dispose();
		latoLightGenerator.dispose();
		gridnikGenerator.dispose();
	}
	public static Vector2 model2pixel(Vector2 coords) {
		return coords.cpy().add(StarCycle.meterWidth/2, StarCycle.meterHeight/2).scl(StarCycle.pixelsPerMeter);
	}
	
	public static Vector2 model2pixel(float x, float y) {
		return model2pixel(new Vector2(x, y));
	}

    public static void resetFonts() {
        gridnikMedium = gridnikGenerator.generateFont(22);
        gridnikLarge = gridnikGenerator.generateFont(42);
        latoLightLarge = latoLightGenerator.generateFont(52);
    }
}
	

