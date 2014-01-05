package com.autonomousgames.starcycle.html;

import com.autonomousgames.starcycle.core.StarCycle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class StarCycleHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new StarCycle();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
