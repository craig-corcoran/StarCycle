package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Texturez {
    public enum TextureType {
        ORB0, ORB1, VOID0, VOID1, NOVA0, NOVA1
    }

    private final TextureAtlas atlas;
    public final AtlasRegion fingerRight;

    private AtlasRegion tutorial0;
    private AtlasRegion tutorial1;
    private AtlasRegion tutorial2;
    private AtlasRegion tutorial3;
    private AtlasRegion tutorial4;
    private AtlasRegion tutorial5;
    private AtlasRegion tutorial6;
    public AtlasRegion[] tutorialImages = new AtlasRegion[] {tutorial0, tutorial1, tutorial2, tutorial3, tutorial4, tutorial5, tutorial6};
    public TextureRegion fingerLeft; // = atlas.findRegion("icon-finger"); // 3:4

    // ORBS N STUFF
    private AtlasRegion orbMaluma0;
    private AtlasRegion orbMaluma1;
    private AtlasRegion orbTakete0;
    private AtlasRegion orbTakete1;
    private AtlasRegion orbTarget0;
    private AtlasRegion orbTarget1;
    private AtlasRegion orbDerelict0;
    private AtlasRegion orbDerelict1;
    private AtlasRegion orbClockwork0;
    private AtlasRegion orbClockwork1;
    private AtlasRegion voidMaluma0;
    private AtlasRegion voidMaluma1;
    private AtlasRegion voidTakete0;
    private AtlasRegion voidTakete1;
    private AtlasRegion voidTarget0;
    private AtlasRegion voidTarget1;
    private AtlasRegion voidDerelict0;
    private AtlasRegion voidDerelict1;
    private AtlasRegion voidClockwork0;
    private AtlasRegion voidClockwork1;
    private AtlasRegion novaMaluma0;
    private AtlasRegion novaMaluma1;
    private AtlasRegion novaTakete0;
    private AtlasRegion novaTakete1;
    private AtlasRegion novaTarget0;
    private AtlasRegion novaTarget1;
    private AtlasRegion novaDerelict0;
    private AtlasRegion novaDerelict1;
    private AtlasRegion novaClockwork0;
    private AtlasRegion novaClockwork1;
    private AtlasRegion fakeredOrbTexture;
    private AtlasRegion fakeblueOrbTexture;
    public AtlasRegion chargeBeam;
    public AtlasRegion voidRing;

    public AtlasRegion[] fakeorbTextures;

    // LAUNCHPAD (NEW)
    private AtlasRegion launchBackground;
    private AtlasRegion launchFill;
    private AtlasRegion launchMeter;
    private AtlasRegion launchButton1;
    private AtlasRegion launchButton2;
    private AtlasRegion launchTriangle;
    public AtlasRegion[] launchTextures;
    // AIMING UI (UPDATED)
    private AtlasRegion handle;
    private AtlasRegion chevron0;
    private AtlasRegion chevron1;
    public AtlasRegion[] aimer;

    // BACKGROUND (NEW)
    private String[] bgBlockNames;
    public int[] bgBlockDims;
    public ArrayList<TextureRegion[]> bgBlocks;
    private String[] bgStripNames;
    public int[] bgStripDims;
    public ArrayList<TextureRegion[]> bgStrips;
    public int[] bgStarsDims;
    public TextureRegion[] bgStars;

    // BACKGROUND
    public AtlasRegion bgMote;

    // FONTS
    public BitmapFont font;

    // MENU UI
    public AtlasRegion splashTexture;
    public AtlasRegion genericLogo;
    public AtlasRegion questionIcon;
    public AtlasRegion settingsIcon;
    public AtlasRegion soloIcon;
    public AtlasRegion multiplayerIcon; // 4:3
    public AtlasRegion musicIcon;
    public AtlasRegion soundIcon;
    public AtlasRegion noIcon;
    public AtlasRegion infoIcon;
    public AtlasRegion swapIcon;
    public AtlasRegion swatchIcon;
    public AtlasRegion sliderIcon; // 2:1
    public AtlasRegion backIcon; // 4:3
    public AtlasRegion readyIcon;
    public AtlasRegion rightIcon;
    public AtlasRegion wrongIcon;
    private AtlasRegion trefoilIcon;
    private AtlasRegion doublebinaryIcon;
    private AtlasRegion concentricIcon;
    private AtlasRegion venndiagramIcon;
    private AtlasRegion trefoilIconOn;
    private AtlasRegion doublebinaryIconOn;
    private AtlasRegion concentricIconOn;
    private AtlasRegion venndiagramIconOn;
    public AtlasRegion[] levelIconsOff;
    public AtlasRegion[] levelIconsOn;

    // BASIC UI
    public AtlasRegion line;
    public AtlasRegion block;
    public AtlasRegion circle;
    public AtlasRegion gradientRound;

   // IN-GAME PAUSE UI
    public AtlasRegion menuIcon;
    public AtlasRegion pauseIcon; // 3:4
    public AtlasRegion arrowsIcon;
    public AtlasRegion[] pauseUI;
    public AtlasRegion trophy;
    public AtlasRegion thumbsDown;

    // STARS
    public AtlasRegion hexStar;

    // BASE SHAPES
    public AtlasRegion baseMaluma0;
    public AtlasRegion baseMaluma1;

    public AtlasRegion baseTakete0;
    public AtlasRegion baseTakete1;

    public AtlasRegion baseTarget0;
    public AtlasRegion baseTarget1;
    public AtlasRegion baseTarget2;
    public AtlasRegion baseTarget3;

    public AtlasRegion baseDerelict0a;
    public AtlasRegion baseDerelict0b;
    public AtlasRegion baseDerelict1a;
    public AtlasRegion baseDerelict1b;
    public AtlasRegion baseDerelict2a;
    public AtlasRegion baseDerelict2b;

    public AtlasRegion baseClockwork0a;
    public AtlasRegion baseClockwork0b;
    public AtlasRegion baseClockwork1a;
    public AtlasRegion baseClockwork1b;
    public AtlasRegion baseClockwork2a;
    public AtlasRegion baseClockwork2b;
    public AtlasRegion baseClockwork2c;

    public HashMap<BaseType, HashMap<TextureType,AtlasRegion>> skinMap;
    public HashMap<TextureType,AtlasRegion> malumaMap;
    public HashMap<TextureType,AtlasRegion> taketeMap;
    public HashMap<TextureType,AtlasRegion> targetMap;
    public HashMap<TextureType,AtlasRegion> derelictMap;
    public HashMap<TextureType,AtlasRegion> clockworkMap;

    public BitmapFont gridnikMedium;
    public BitmapFont gridnikLarge;
    public BitmapFont latoLightLarge;
    private FreeTypeFontGenerator gridnikGenerator;
    private FreeTypeFontGenerator latoLightGenerator;

    public Texturez() {
        atlas = StarCycle.assetManager.get("packed-images/packed-textures.atlas", TextureAtlas.class);
        fingerRight = atlas.findRegion("icon-finger"); // 3:4

        tutorial0 = atlas.findRegion("holdinghands");
        tutorial1 = atlas.findRegion("aim1");
        tutorial2 = atlas.findRegion("aim2");
        tutorial3 = atlas.findRegion("autofire");
        tutorial4 = atlas.findRegion("shoot1");
        tutorial5 = atlas.findRegion("shoot2");
        tutorial6 = atlas.findRegion("dontcrash");
        tutorialImages = new AtlasRegion[] {tutorial0, tutorial1, tutorial2, tutorial3, tutorial4, tutorial5, tutorial6};
        fingerLeft = atlas.findRegion("icon-finger"); // 3:4

        // ORBS N STUFF
        orbMaluma0 = atlas.findRegion("orb-mal0");
        orbMaluma1 = atlas.findRegion("orb-mal1");
        orbTakete0 = atlas.findRegion("orb-tak0");
        orbTakete1 = atlas.findRegion("orb-tak1");
        orbTarget0 = atlas.findRegion("orb-tar0");
        orbTarget1 = atlas.findRegion("orb-tar1");
        orbDerelict0 = atlas.findRegion("orb-der0");
        orbDerelict1 = atlas.findRegion("orb-der1");
        orbClockwork0 = atlas.findRegion("orb-clo0");
        orbClockwork1 = atlas.findRegion("orb-clo1");
        voidMaluma0 = atlas.findRegion("void-mal0");
        voidMaluma1 = atlas.findRegion("void-mal1");
        voidTakete0 = atlas.findRegion("void-tak0");
        voidTakete1 = atlas.findRegion("void-tak1");
        voidTarget0 = atlas.findRegion("void-tar0");
        voidTarget1 = atlas.findRegion("void-tar1");
        voidDerelict0 = atlas.findRegion("void-der0");
        voidDerelict1 = atlas.findRegion("void-der1");
        voidClockwork0 = atlas.findRegion("void-clo0");
        voidClockwork1 = atlas.findRegion("void-clo1");
        novaMaluma0 = atlas.findRegion("nova-mal0");
        novaMaluma1 = atlas.findRegion("nova-mal1");
        novaTakete0 = atlas.findRegion("nova-tak0");
        novaTakete1 = atlas.findRegion("nova-tak1");
        novaTarget0 = atlas.findRegion("nova-tar0");
        novaTarget1 = atlas.findRegion("nova-tar1");
        novaDerelict0 = atlas.findRegion("nova-der0");
        novaDerelict1 = atlas.findRegion("nova-der1");
        novaClockwork0 = atlas.findRegion("nova-clo0");
        novaClockwork1 = atlas.findRegion("nova-clo1");
        fakeredOrbTexture = atlas.findRegion("fake-orb-red");
        fakeblueOrbTexture = atlas.findRegion("fake-orb-blue");
        chargeBeam = atlas.findRegion("charge");
        voidRing = atlas.findRegion("voidring");

        fakeorbTextures = new AtlasRegion[] {fakeblueOrbTexture, fakeredOrbTexture};

        // LAUNCHPAD (NEW)
        launchBackground = atlas.findRegion("launchpad-bg");
        launchFill = atlas.findRegion("launchpad-fillmeter");
        launchMeter = atlas.findRegion("launchpad-bordermeter");
        launchButton1 = atlas.findRegion("launchpad-orb");
        launchButton2 = atlas.findRegion("launchpad-powerup");
        launchTriangle = atlas.findRegion("launchpad-mark");
        launchTextures = new AtlasRegion[]{launchBackground, launchFill, launchMeter, launchButton1, launchButton2, launchTriangle};

        // AIMING UI (UPDATED)
        handle = atlas.findRegion("handle");
        chevron0 = atlas.findRegion("chevron0");
        chevron1 = atlas.findRegion("chevron1");
        aimer = new  AtlasRegion[]{handle, chevron0, chevron1};

        // BACKGROUND (NEW)
        bgBlockNames = new String[]{"disco", "nebula", "hexnut"};
        bgBlockDims = new int[]{6, 6};
        bgBlocks = new ArrayList<TextureRegion[]>();
        bgStripNames = new String[]{"bubbles", "bacon", "lobe", "ribbon", "triangles"};
        bgStripDims = new int[]{6, 2};
        bgStrips = new ArrayList<TextureRegion[]>();
        bgStarsDims = new int[]{8, 13};
        bgStars = bgTile("stardust", bgStarsDims[0], bgStarsDims[1]);

        // BACKGROUND
        bgMote = atlas.findRegion("mote");

        // FONTS
        font = new BitmapFont();

        // MENU UI
        splashTexture = atlas.findRegion("ag-logo");
        genericLogo = atlas.findRegion("generic-starcycle-logo");
        questionIcon = atlas.findRegion("icon-question");
        settingsIcon = atlas.findRegion("icon-tools");
        soloIcon = atlas.findRegion("icon-solo");
        multiplayerIcon = atlas.findRegion("icon-multi"); // 4:3
        musicIcon = atlas.findRegion("icon-music");
        soundIcon = atlas.findRegion("icon-bell");
        noIcon = atlas.findRegion("icon-no");
        infoIcon = atlas.findRegion("icon-info");
        swapIcon = atlas.findRegion("icon-swap");
        swatchIcon = atlas.findRegion("icon-swatches");
        sliderIcon = atlas.findRegion("icon-slider"); // 2:1
        backIcon = atlas.findRegion("icon-back"); // 4:3
        readyIcon = atlas.findRegion("icon-check");
        rightIcon = atlas.findRegion("icon-altcheck");
        wrongIcon = atlas.findRegion("icon-x");
        trefoilIcon = atlas.findRegion("trefoilselect");
        doublebinaryIcon = atlas.findRegion("doublebinaryselect");
        concentricIcon = atlas.findRegion("concentricselect");
        venndiagramIcon = atlas.findRegion("venndiagramselect");
        trefoilIconOn = atlas.findRegion("trefoilselectOn");
        doublebinaryIconOn = atlas.findRegion("doublebinaryselectOn");
        concentricIconOn = atlas.findRegion("concentricselectOn");
        venndiagramIconOn = atlas.findRegion("venndiagramselectOn");
        levelIconsOff = new AtlasRegion[] {trefoilIcon, doublebinaryIcon, concentricIcon, venndiagramIcon};
        levelIconsOn = new AtlasRegion[] {trefoilIconOn, doublebinaryIconOn, concentricIconOn, venndiagramIconOn};

        // BASIC UI
        line = atlas.findRegion("line");
        block = atlas.findRegion("block");
        circle = atlas.findRegion("solidcircle");
        gradientRound = atlas.findRegion("gradient-round");

        // IN-GAME PAUSE UI
        menuIcon = atlas.findRegion("icon-menu");
        pauseIcon = atlas.findRegion("icon-pause"); // 3:4
        arrowsIcon = atlas.findRegion("icon-cycle");
        pauseUI = new AtlasRegion[]{menuIcon,pauseIcon,arrowsIcon,backIcon};
        trophy = atlas.findRegion("icon-trophy");
        thumbsDown = atlas.findRegion("icon-thumb");

        // STARS
        hexStar = atlas.findRegion("starquarter");

        // BASE SHAPES
        baseMaluma0 = atlas.findRegion("base-maluma0");
        baseMaluma1 = atlas.findRegion("base-maluma1");

        baseTakete0 = atlas.findRegion("base-takete0");
        baseTakete1 = atlas.findRegion("base-takete1");

        baseTarget0 = atlas.findRegion("base-target0");
        baseTarget1 = atlas.findRegion("base-target1");
        baseTarget2 = atlas.findRegion("base-target2");
        baseTarget3 = atlas.findRegion("base-target3");

        baseDerelict0a = atlas.findRegion("base-derelict0a");
        baseDerelict0b = atlas.findRegion("base-derelict0b");
        baseDerelict1a = atlas.findRegion("base-derelict1a");
        baseDerelict1b = atlas.findRegion("base-derelict1b");
        baseDerelict2a = atlas.findRegion("base-derelict2a");
        baseDerelict2b = atlas.findRegion("base-derelict2b");

        baseClockwork0a = atlas.findRegion("base-clockwork0a");
        baseClockwork0b = atlas.findRegion("base-clockwork0b");
        baseClockwork1a = atlas.findRegion("base-clockwork1a");
        baseClockwork1b = atlas.findRegion("base-clockwork1b");
        baseClockwork2a = atlas.findRegion("base-clockwork2a");
        baseClockwork2b = atlas.findRegion("base-clockwork2b");
        baseClockwork2c = atlas.findRegion("base-clockwork2c");


        //SKIN HASHMAPS
        skinMap = new HashMap<BaseType, HashMap<TextureType,AtlasRegion>>();
        malumaMap = new HashMap<TextureType,AtlasRegion>();
        taketeMap = new HashMap<TextureType,AtlasRegion>();
        targetMap = new HashMap<TextureType,AtlasRegion>();
        derelictMap = new HashMap<TextureType,AtlasRegion>();
        clockworkMap = new HashMap<TextureType,AtlasRegion>();

        gridnikGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Gridnik.ttf"));
        latoLightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Lato-Light.ttf"));
        gridnikMedium = gridnikGenerator.generateFont(22);
        gridnikLarge = gridnikGenerator.generateFont(42);
        latoLightLarge = latoLightGenerator.generateFont(52);
        fingerLeft = new TextureRegion(fingerRight);
        fingerLeft.flip(true, false);


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

        //TAKETE MAP
        skinMap.get(BaseType.TAKETE).put(TextureType.ORB0, orbTakete0);
        skinMap.get(BaseType.TAKETE).put(TextureType.ORB1, orbTakete1);
        skinMap.get(BaseType.TAKETE).put(TextureType.VOID0, voidTakete0);
        skinMap.get(BaseType.TAKETE).put(TextureType.VOID1, voidTakete1);
        skinMap.get(BaseType.TAKETE).put(TextureType.NOVA0, novaTakete0);
        skinMap.get(BaseType.TAKETE).put(TextureType.NOVA1, novaTakete1);

        //TARGET MAP
        skinMap.get(BaseType.TARGET).put(TextureType.ORB0, orbTarget0);
        skinMap.get(BaseType.TARGET).put(TextureType.ORB1, orbTarget1);
        skinMap.get(BaseType.TARGET).put(TextureType.VOID0, voidTarget0);
        skinMap.get(BaseType.TARGET).put(TextureType.VOID1, voidTarget1);
        skinMap.get(BaseType.TARGET).put(TextureType.NOVA0, novaTarget0);
        skinMap.get(BaseType.TARGET).put(TextureType.NOVA1, novaTarget1);

        //DERELICT MAP
        skinMap.get(BaseType.DERELICT).put(TextureType.ORB0, orbDerelict0);
        skinMap.get(BaseType.DERELICT).put(TextureType.ORB1, orbDerelict1);
        skinMap.get(BaseType.DERELICT).put(TextureType.VOID0, voidDerelict0);
        skinMap.get(BaseType.DERELICT).put(TextureType.VOID1, voidDerelict1);
        skinMap.get(BaseType.DERELICT).put(TextureType.NOVA0, novaDerelict0);
        skinMap.get(BaseType.DERELICT).put(TextureType.NOVA1, novaDerelict1);

        //CLOCKWORK MAP
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.ORB0, orbClockwork0);
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.ORB1, orbClockwork1);
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.VOID0, voidClockwork0);
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.VOID1, voidClockwork1);
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.NOVA0, novaClockwork0);
        skinMap.get(BaseType.CLOCKWORK).put(TextureType.NOVA1, novaClockwork1);

//		// PARSE BACKGROUND TILES
        for (String bgBlockName : bgBlockNames) {
            bgBlocks.add(bgTile(bgBlockName, bgBlockDims[0], bgBlockDims[1]));
        }
        for (String bgStripName : bgStripNames) {
            bgStrips.add(bgTile(bgStripName, bgStripDims[0], bgStripDims[1]));
        }

    }

	private TextureRegion[] bgTile(String name, int rows, int cols) {
		final TextureRegion[] texture = new TextureRegion[rows*cols];
		AtlasRegion textureAtlas = atlas.findRegion(name);
		TextureRegion[][] nestedTiles = textureAtlas.split(textureAtlas.getRegionWidth()/cols, textureAtlas.getRegionHeight()/rows);
		for (int i = 0; i < rows*cols; i++) {
			texture[i] = nestedTiles[i/cols][i%cols];
		}
		return texture;
	}
		
	public void dispose() {
		latoLightGenerator.dispose();
		gridnikGenerator.dispose();
	}

    public void resetFonts() {
        gridnikMedium = gridnikGenerator.generateFont(22);
        gridnikLarge = gridnikGenerator.generateFont(42);
        latoLightLarge = latoLightGenerator.generateFont(52);
    }
}
