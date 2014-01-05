package com.autonomousgames.starcycle.android;

import android.os.Bundle;
import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class StarCycleActivity extends AndroidApplication {
		@Override
        public void onCreate(android.os.Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
            
            AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
            cfg.useGL20 = false;
            
            initialize(new StarCycle(), cfg);
        }
}