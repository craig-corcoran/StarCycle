package com.autonomousgames.starcycle.core.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class ModelSettings {
	private static Preferences userPrefs;
	static {
		userPrefs = Gdx.app.getPreferences("StarCycle-model-settings");
		int clientSettingsVersion = userPrefs.getInteger("settingsVersion");
		int currentSettingsVersion = 24;
		if (clientSettingsVersion != currentSettingsVersion) {
			Gdx.app.log("ModelSettings", "rewriting settings file");
			userPrefs.putInteger("settingsVersion", currentSettingsVersion);
            userPrefs.putFloat("maxOrbs", 32f); // Star parameters
            userPrefs.putFloat("angleThresh", MathUtils.PI2); // charge orb lock on threshold

			// padding in units of multiples of pixels per meter: padding = StarCycle.pixelsPerMeter * paddingMeters;
			userPrefs.putFloat("paddingMeters",0.5f);      // UI

			userPrefs.putFloat("initAmmo", 80.0f);         // Player
			userPrefs.putFloat("ammoDripRate", .04f);      // Player
            userPrefs.putFloat("ammoRate", 0.008f);         // OrbFactory
            userPrefs.putFloat("maxAmmo", -1f);         // neg means no max ammo
			userPrefs.putFloat("baseRadius", 1.75f);          // Player

			userPrefs.putFloat("gravScalar", 10f);     		// Level
			userPrefs.putFloat("orbitSpeed", 0.02f);    	// Level
			userPrefs.putFloat("starRadius", 0.45f);        // Level

			userPrefs.putFloat("explosionRadius",0.6f);     // explosions

			userPrefs.putFloat("chargeRadius",0.5f);       // ChargeOrb
			userPrefs.putFloat("orbitScal", 3f);           // ChargeOrb

			userPrefs.putFloat("lifeSpan", 80f);           // Orb
            userPrefs.putFloat("incOrbLifeSpan", 500f);           // Orb

			userPrefs.putFloat("baseRotationSpeed", 12f);	// Base
			userPrefs.putFloat("minSpeed", 0.75f);          // Base
			userPrefs.putFloat("velScale", 0.5f);          	// Base

			userPrefs.putFloat("angleDelta", 0.5f);        // Star
			userPrefs.putFloat("captureFraction", 0.5f);      // Star

			userPrefs.putFloat("voidRadius", 0.12f);   // OrbFactory
			userPrefs.putFloat("voidSensorRadius", 0.6f);   // GravWell

			userPrefs.putFloat("orbCost", 4f);       // OrbFactory
			userPrefs.putFloat("voidCost", 30f);       // OrbFactory
			userPrefs.putFloat("novaCost", 80f);           // OrbFactory
			userPrefs.putFloat("orbRadius", 0.115f); // OrbFactory

			userPrefs.putFloat("novaRadius", 0.28f);       // OrbFactory
			userPrefs.putFloat("voidStars", 1f);        // LaunchPad
			userPrefs.putFloat("novaStars", 2f);			   // LaunchPad

			userPrefs.putFloat("coolDown",0.7f);
			userPrefs.putFloat("orbLifeSpan", 100f);
			userPrefs.putFloat("powerupLifeSpan",30f);

			userPrefs.putFloat("popRate",0.005f);

            userPrefs.putFloat("incAmmoThresh", 0.3f);
            userPrefs.putFloat("initVelScale", 0.4f); // income orbs
            userPrefs.putFloat("incOrbGravScale", 8000f);
            userPrefs.putFloat("incOrbSize", 0.006f);
            userPrefs.putFloat("incOrbAlpha", 0.65f);

			userPrefs.flush();
		}
	}

	public static float getFloatSetting(String istring) {
		return ModelSettings.userPrefs.getFloat(istring);
	}

	public static long getLongSetting(String istring) {
		return ModelSettings.userPrefs.getLong(istring);
	}
}
