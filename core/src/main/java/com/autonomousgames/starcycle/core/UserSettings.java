package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserSettings {
	private static Preferences userPrefs;
	static {
		userPrefs = Gdx.app.getPreferences("StarCycle-user-settings");
		if (userPrefs.getFloat("musicVolume") == 0.0f) {
			Gdx.app.log("UserSettings", "rewriting settings file");
			userPrefs.putFloat("musicVolume", 0.5f);
			userPrefs.putFloat("musicVolume", 0.5f);
			userPrefs.putFloat("sfxVolume",0.5f);
			userPrefs.flush();
		}
	}

	public static float getFloatSetting(String istring) {
		return UserSettings.userPrefs.getFloat(istring);
	}

	public static void setFloatSetting(String istring, float ifloat){
		UserSettings.userPrefs.putFloat(istring, ifloat);
		UserSettings.userPrefs.flush();
	}
}
