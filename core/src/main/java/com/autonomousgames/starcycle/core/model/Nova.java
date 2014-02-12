package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.math.Vector2;

public class Nova extends Orb implements Collidable {

	public Nova(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		super(player, position, velocity, model, type);
		StarCycle.audio.launchnukeSound.play(StarCycle.audio.sfxVolume);
	}

	@Override
	public void collision(Collidable obj) {
		if (obj instanceof Star) {
			((Star) obj).addPop(((Star) obj).maxPop * 2f, player.number);
			StarCycle.audio.landnukeSound.play(StarCycle.audio.sfxVolume);
            ((Star) obj).hitByNova = true;
		}
		else {
			StarCycle.audio.gravkillnukeSound.play(StarCycle.audio.sfxVolume);
		}
		super.collision(obj);
	}
}
