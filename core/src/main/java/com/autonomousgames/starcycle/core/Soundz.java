package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Soundz {

    public final Music gameMusic;
	public final Sound launchnukeSound;
	public final Sound gravkillgravSound;
	public final Sound gravkillnukeSound;
	public final Sound landnukeSound;
	public final Sound levelup1Sound;
	public final Sound levelup2Sound;
	public final Sound levelup3Sound;
	public final Sound leveldownSound;
	public final Sound winSound;
    public final Sound orbCrash;
	public final Sound screenswitchSound;
	public float musicVolume;
	public float sfxVolume;

	public Soundz() {
        musicVolume = UserSettings.getFloatSetting("musicVolume");
        sfxVolume = UserSettings.getFloatSetting("sfxVolume");
        launchnukeSound = StarCycle.assetManager.get("audio/launchnuke.mp3", Sound.class);
        gravkillgravSound = StarCycle.assetManager.get("audio/grav_annihilate.mp3", Sound.class);
        gravkillnukeSound = StarCycle.assetManager.get("audio/killnuke.mp3", Sound.class);
        landnukeSound = StarCycle.assetManager.get("audio/landnuke.mp3", Sound.class);
        levelup1Sound = StarCycle.assetManager.get("audio/levelup.wav", Sound.class);
        levelup2Sound = StarCycle.assetManager.get("audio/levelup2.wav", Sound.class);
        levelup3Sound = StarCycle.assetManager.get("audio/levelup3.wav", Sound.class);
        leveldownSound = StarCycle.assetManager.get("audio/leveldown.mp3", Sound.class);
        winSound = StarCycle.assetManager.get("audio/win.wav", Sound.class);
        orbCrash = StarCycle.assetManager.get("audio/orbcrash1.wav", Sound.class);
        screenswitchSound = StarCycle.assetManager.get("audio/screenchange.wav", Sound.class);
        gameMusic = StarCycle.assetManager.get("audio/all_music1.mp3", Music.class);
    }
}
