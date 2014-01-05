package com.autonomousgames.starcycle.java;

import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class StarCycleDesktop {
    public static void main (String[] args) {
      	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
   		cfg.title = "Starcycle";
   		cfg.useGL20 = false;
   		cfg.width = 1280;
   		cfg.height = 800;
        		
        new LwjglApplication(new StarCycle(), cfg);
    }
}