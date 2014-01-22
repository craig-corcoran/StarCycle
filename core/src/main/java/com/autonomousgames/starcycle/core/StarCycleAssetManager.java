package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class StarCycleAssetManager extends AssetManager {
    public StarCycleAssetManager(){
        super();
        this.load("packed-images/packed-textures.atlas", TextureAtlas.class);
        this.load("audio/screenchange.wav", Sound.class);
        this.load("audio/launchnuke.mp3", Sound.class);
        this.load("audio/grav_annihilate.mp3", Sound.class);
        this.load("audio/killnuke.mp3", Sound.class);
        this.load("audio/landnuke.mp3", Sound.class);
        this.load("audio/levelup.wav", Sound.class);
        this.load("audio/levelup2.wav", Sound.class);
        this.load("audio/levelup3.wav", Sound.class);
        this.load("audio/leveldown.mp3", Sound.class);
        this.load("audio/win.wav", Sound.class);
        this.load("audio/orbcrash1.wav", Sound.class);
        this.load("audio/all_music1.mp3", Music.class);
        this.finishLoading();
    }
}
