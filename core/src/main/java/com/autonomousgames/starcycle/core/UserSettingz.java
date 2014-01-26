package com.autonomousgames.starcycle.core;

import com.autonomousgames.starcycle.core.screens.SinglePlayerLevel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class UserSettingz {
	private static Preferences userPrefs;
	private static Preferences sessionPrefs;
	static {
		userPrefs = Gdx.app.getPreferences("StarCycle-user-preferences");
		sessionPrefs = Gdx.app.getPreferences("StarCycle-session-preferences");
		int clientSettingsVersion = userPrefs.getInteger("settingsVersion");
		int currentSettingsVersion = 23;
		if (clientSettingsVersion != currentSettingsVersion) {
			Gdx.app.log("UserSettingz", "rewriting settings file");
			userPrefs.putInteger("settingsVersion", currentSettingsVersion);
			userPrefs.putLong("uid", MathUtils.random.nextLong()); //MathUtils.random(1f,1000*1000*1000*1000f*1000f)
			userPrefs.putFloat("musicVolume", 0.5f);
			userPrefs.putFloat("musicVolume", 0.5f);       // Soundz
			userPrefs.putFloat("sfxVolume",0.5f);          // Soundz

            userPrefs.putFloat("maxOrbs", 32f); // Star parameters
            userPrefs.putFloat("angleThresh", MathUtils.PI2); // charge orb lock on threshold
			
			// padding in units of multiples of pixels per meter: padding = StarCycle.pixelsPerMeter * paddingMeters; 
			userPrefs.putFloat("paddingMeters",0.5f);      // UI
			
			userPrefs.putFloat("initAmmo", 80.0f);         // Player
			userPrefs.putFloat("ammoDripRate", .04f);      // Player
			userPrefs.putFloat("baseRadius", 1.75f);          // Player

			userPrefs.putFloat("gravScalar", 10f);     		// Level
			userPrefs.putFloat("orbitSpeed", 0.02f);    	// Level
			userPrefs.putFloat("starRadius", 0.45f);        // Level
			
			userPrefs.putFloat("explosionRadius",0.6f);     // explosions
			
			userPrefs.putFloat("chargeRadius",0.5f);       // ChargeOrb
			userPrefs.putFloat("orbitScal", 3f);           // ChargeOrb
			userPrefs.putFloat("orbitScalGravWell", 3f);   // GravityWell
			
			userPrefs.putFloat("lifeSpan", 80f);           // Orb
            userPrefs.putFloat("incOrbLifeSpan", 500f);           // Orb
			
			userPrefs.putFloat("baseRotationSpeed", 12f);	// Base
			userPrefs.putFloat("minSpeed", 0.75f);          // Base - changed from 1f
			userPrefs.putFloat("velScale", 0.5f);          	// Base - changed from 0.4f
			
			userPrefs.putFloat("angleDelta", 0.5f);        // Star
			userPrefs.putFloat("captureRatio", 0.5f);      // Star

			userPrefs.putFloat("gravWellRadius", 0.12f);   // OrbFactory
			userPrefs.putFloat("gravWellSensorRadius", 0.6f);   // GravWell
			
			userPrefs.putFloat("chargeOrbCost", 4f);       // OrbFactory
			userPrefs.putFloat("gravWellCost", 30f);       // OrbFactory
			userPrefs.putFloat("nukeCost", 80f);           // OrbFactory
			userPrefs.putFloat("chargeOrbRadius", 0.115f); // OrbFactory
			
			userPrefs.putFloat("nukeRadius", 0.28f);       // OrbFactory
			userPrefs.putFloat("gravWellStars", 1f);        // LaunchPad
			userPrefs.putFloat("nukeStars", 2f);			   // LaunchPad
			
			userPrefs.putFloat("coolDown",0.7f);           // OrbFactory
			userPrefs.putFloat("orbLifeSpan", 100f);       // OrbFactory
			userPrefs.putFloat("powerupLifeSpan",30f);     // OrbFactory
			userPrefs.putFloat("velScaleOrbFact",0.6f);    // OrbFactory
			userPrefs.putFloat("ammoRate",0.008f);         // OrbFactory
			userPrefs.putFloat("popRate",0.005f);          // OrbFactory

            userPrefs.putFloat("incAmmoThresh", 0.4f);
            userPrefs.putFloat("initVelScale", 0.2f); // income orbs
            userPrefs.putFloat("incOrbGravScale", 2000f);
            userPrefs.putFloat("incOrbSize", 0.006f);
            userPrefs.putFloat("incOrbAlpha", 0.65f);

			userPrefs.putFloat(SinglePlayerLevel.LEVEL1.toString(), 0f);  //LevelSelect
			userPrefs.putFloat(SinglePlayerLevel.LEVEL2.toString(), 0f); //LevelSelect
			userPrefs.putFloat(SinglePlayerLevel.LEVEL3.toString(), 0f);  //LevelSelect
			userPrefs.putFloat(SinglePlayerLevel.LEVEL4.toString(), 0f); //LevelSelect

			
			userPrefs.flush();
		}
	}
	
	public static float getFloatSetting(String istring) {
		return UserSettingz.userPrefs.getFloat(istring);
	}
	
	public static long getLongSetting(String istring) {
		return UserSettingz.userPrefs.getLong(istring);
	}
	
	public static void setFloatSetting(String istring, float ifloat){
		UserSettingz.userPrefs.putFloat(istring, ifloat);
		UserSettingz.userPrefs.flush();
	}
	
	public static void setSessionSetting(String istring, float ifloat){
		UserSettingz.sessionPrefs.putFloat(istring, ifloat);
	}
	
	
}
