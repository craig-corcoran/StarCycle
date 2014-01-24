package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.UserSettingz;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class OrbFactory {

	// constants for building orbs
	static float chargeOrbCost;
	static float gravWellCost;
	static float nukeCost;
	private static float[] orbCosts;

	static float chargeOrbRadius;
	static float gravWellRadius;
	static float nukeRadius;
	public static float[] orbRadii;

	static float ammoRate;
	static float popRate;
	public int p1orbs;
	public int p1voids;
	public int p1novas;
	public int p2orbs;
	public int p2voids;
	public int p2novas;
	
	private float orbLifeSpan;
	private float powerupLifeSpan;
	private static float velScaleOrbFact; // scaling of velocity with base pointer length
	private Model model;

	public OrbFactory(Model model) {
		p1orbs = 0;
		p1voids = 0;
		p1novas = 0;
		p2orbs = 0;
		p2voids = 0;
		p2novas = 0;
		
		chargeOrbCost = UserSettingz.getFloatSetting("chargeOrbCost");
		gravWellCost = UserSettingz.getFloatSetting("gravWellCost");
		nukeCost = UserSettingz.getFloatSetting("nukeCost");
		orbCosts = new float[] { chargeOrbCost, gravWellCost, nukeCost };

		chargeOrbRadius = UserSettingz.getFloatSetting("chargeOrbRadius");
		gravWellRadius = UserSettingz.getFloatSetting("gravWellRadius");
		nukeRadius = UserSettingz.getFloatSetting("nukeRadius");
		orbRadii = new float[] { chargeOrbRadius, gravWellRadius, nukeRadius };

		orbLifeSpan = UserSettingz.getFloatSetting("orbLifeSpan");
		powerupLifeSpan = UserSettingz.getFloatSetting("powerupLifeSpan");
		velScaleOrbFact = UserSettingz.getFloatSetting("velScaleOrbFact");
		ammoRate = UserSettingz.getFloatSetting("ammoRate");
		popRate = UserSettingz.getFloatSetting("popRate");

		this.model = model;
	}
	
	public void setCosts(float chargeOrbCost, float gravWellCost, float nukeCost) {
		OrbFactory.chargeOrbCost = chargeOrbCost;
		OrbFactory.gravWellCost = gravWellCost;
		OrbFactory.nukeCost = nukeCost;
		OrbFactory.orbCosts = new float[] { chargeOrbCost, gravWellCost, nukeCost };
	}
	
	public void setLife(float orbLifeSpan, float powerupLifeSpan) {
		this.orbLifeSpan = orbLifeSpan;
		this.powerupLifeSpan = powerupLifeSpan;
	}

    public void launch(Orb.OrbType type, Player player) {
        float orbCost = orbCosts[type.ordinal()];
		// if sufficient ammo. -1 indicates not shootable/infinite ammo cost
		if ((player.ammo >= orbCost) & (orbCost != -1) & !player.frozen) {
			player.ammo -= orbCost;
            Vector2 vec = player.base.getPointer();
			vec.scl(velScaleOrbFact);
            Vector2 position = vec.cpy().nor().scl(0.3f * player.base.baseDiams[player.base.level]);
			position.add(player.base.origin);

			switch (type) {
			case ORB:
				createOrb(type, player, position, vec, orbLifeSpan);
				break;

			case VOID:
				createOrb(type, player, position, vec, powerupLifeSpan);
				break;

			case NOVA:
				createOrb(type, player, position, vec, powerupLifeSpan);
				break;
			}
		}
	}
	
	public void createOrb(Orb.OrbType type, Player player, Vector2 pos, Vector2 vel, float lifespan) {
		switch (type) {
		case ORB:
			if (player.number == 0) {
				p1orbs++;
			}
			else if (player.number == 1){
				p2orbs++;
			}

            ChargeOrb orb = new ChargeOrb(player, pos, vel, model, type);
			orb.setLifeSpan(lifespan);
			player.orbs.add(orb);
			break;

		case VOID:
			if (player.number == 0) {
				p1voids++;
			}
			else if (player.number == 1){
				p2voids++;
			}
            Void orbVoid = new Void(player, pos, vel, model, type);
			orbVoid.setLifeSpan(lifespan);
			player.voids.add(orbVoid);
			break;

		case NOVA:
			if (player.number == 0) {
				p1novas++;
			}
			else if (player.number == 1){
				p2novas++;
			}
            Nova nova = new Nova(player, pos, vel, model, type);
			nova.setLifeSpan(lifespan);
			player.novas.add(nova);
			break;
		}
	}

	public void createOrb(Orb.OrbType type, Player player, Vector2 pos, Vector2 vel) {
		float lifespan = -1f;
		switch (type) {
		case ORB:
			lifespan = orbLifeSpan;
			break;
		case VOID:
			lifespan = powerupLifeSpan;
			break;
		case NOVA:
			lifespan = powerupLifeSpan;
			break;
		}
		createOrb(type, player, pos, vel, lifespan);
	}
}
