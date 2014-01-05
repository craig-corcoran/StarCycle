package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Soundz {

	public static final Music gameMusic;
	public static final Sound screenswitchSound;
	//public static final Sound bootupSound;
	public static final Sound launchnukeSound;
	public static final Sound gravkillgravSound;
	public static final Sound gravkillnukeSound;
	public static final Sound landnukeSound;
	public static final Sound levelup1Sound;
	public static final Sound levelup2Sound;
	public static final Sound levelup3Sound;
	public static final Sound leveldownSound;
	public static final Sound winSound;
	public static final Sound orbCrash;
	public static float musicVolume;
	public static float sfxVolume;
	
	static {
		musicVolume = UserSettingz.getFloatSetting("musicVolume");
		sfxVolume = UserSettingz.getFloatSetting("sfxVolume");
		
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/all_music1.mp3"));
		screenswitchSound = Gdx.audio.newSound(Gdx.files.internal("audio/screenchange.wav"));
		//bootupSound = Gdx.audio.newSound(Gdx.files.internal("audio/poweron.mp3"));
		launchnukeSound = Gdx.audio.newSound(Gdx.files.internal("audio/launchnuke.mp3"));
		gravkillgravSound = Gdx.audio.newSound(Gdx.files.internal("audio/grav_annihilate.mp3"));
		gravkillnukeSound = Gdx.audio.newSound(Gdx.files.internal("audio/killnuke.mp3"));
		landnukeSound = Gdx.audio.newSound(Gdx.files.internal("audio/landnuke.mp3"));
		levelup1Sound =  Gdx.audio.newSound(Gdx.files.internal("audio/levelup.wav"));
		levelup2Sound =  Gdx.audio.newSound(Gdx.files.internal("audio/levelup2.wav"));
		levelup3Sound =  Gdx.audio.newSound(Gdx.files.internal("audio/levelup3.wav"));
		leveldownSound = Gdx.audio.newSound(Gdx.files.internal("audio/leveldown.mp3"));
		winSound = Gdx.audio.newSound(Gdx.files.internal("audio/win.wav"));
		orbCrash = Gdx.audio.newSound(Gdx.files.internal("audio/orbcrash1.wav"));
	}
	
	private Soundz() { } //constructor never called 
	
	public static void dispose(){
		gameMusic.dispose();
		screenswitchSound.dispose();
	}
}
