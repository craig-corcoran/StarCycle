package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.math.Vector2;

public class Void extends ChargeOrb implements Collidable {

    public Void(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		super(player, position, velocity, model, type);

        float absorbRadius = ModelSettings.getFloatSetting("gravWellSensorRadius");
		Sensor.addSensor(this, body, absorbRadius);
		
		orbitScal = ModelSettings.getFloatSetting("orbitScalGravWell");
	}

	@Override
	public void beginSensorContact(Collidable obj) {
		if ((obj instanceof Orb)) {
			if ((obj instanceof Void)) {
                StarCycle.audio.gravkillgravSound.play(StarCycle.audio.sfxVolume);
			}
			obj.collision(this);
		} else {
			super.beginSensorContact(obj);
		}
	}

    @Override
    public void collision(Collidable obj){
        if (!(model.toDestroyList.contains(this))) {
            super.removeSelf();
        }
    }
}
