package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.Soundz;
import com.badlogic.gdx.math.Vector2;

public class Nova extends Orb implements Collidable {

	public Nova(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		super(player, position, velocity, model, type);
		Soundz.launchnukeSound.play(Soundz.sfxVolume);
	}

	@Override
	public void collision(Collidable obj) {
		if (obj instanceof Star) {
			((Star) obj).addPop(((Star) obj).maxPop * 2f, player.number);
			Soundz.landnukeSound.play(Soundz.sfxVolume);
		}
		else {
			Soundz.gravkillnukeSound.play(Soundz.sfxVolume);
		}
		super.collision(obj);
	}
}
