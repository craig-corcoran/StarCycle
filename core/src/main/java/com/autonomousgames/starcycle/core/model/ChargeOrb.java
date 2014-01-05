package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ListIterator;

public class ChargeOrb extends Orb implements Collidable {

	public static float orbitScal;

	public final float ammoRate;
	public final float popRate;
	
	public Star activeStar = null;
	public boolean charging = false;
	
	LayeredButton chargeButton;
	private static final float beamWidth = 0.1f*StarCycle.pixelsPerMeter;
	private static final float chargeRadius = UserSettingz.getFloatSetting("chargeRadius")*StarCycle.pixelsPerMeter;

	public ChargeOrb(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		super(player, position, velocity, model, type);

		orbitScal = UserSettingz.getFloatSetting("orbitScal");

		this.ammoRate = OrbFactory.ammoRate;
		this.popRate = OrbFactory.popRate;
		
		chargeButton = new LayeredButton(orbButton.getCenter(),orbButton.getDims());
		chargeButton.addLayer(new SpriteLayer(Texturez.chargeBeam, new Vector2(chargeRadius/2f, 0f), new Vector2(chargeRadius,beamWidth)).setSpriteColor(player.colors[1]),LayerType.ACTIVE);
		chargeButton.deactivate();
	}

    private Vector2 diffVec = new Vector2();
	private Vector2 oldVel = new Vector2();
    private Vector2 dir = new Vector2();

	@Override
	public Vector2 getGravForce(Vector2[] starPositions) {
		if (!charging) {
			return super.getGravForce(starPositions);
		}

		// only consider active stars, if any
		else {
			force.x = 0;
			force.y = 0;
            Vector2 starpos = activeStar.position;
			oldVel.set(body.getLinearVelocity());
			
			// add component from regular gravity for the active star, ignoring others
			// because we assume the orbs dont affect stars, we can treat all objects as having mass 1.
            float dist = position.dst(starpos);
            float scal = activeStar.mass * gravScalar / (dist * dist * dist);
			force.add(scal * (starpos.x - position.x), scal * (starpos.y - position.y));
			
			// adjust orbit toward stable velocity
			diffVec.set(starpos.x - position.x, starpos.y - position.y).nor(); // unit vector from star to orb
            Vector2 comp = diffVec.scl(oldVel.dot(diffVec));
			dir.set(oldVel.x - comp.x, oldVel.y - comp.y); // direction orth to star
			dir.nor().scl((float) Math.sqrt(activeStar.mass * gravScalar) / (dist)); // target velocity
			force.add(dir.sub(oldVel).scl(orbitScal)); // adjust velocity toward target

			return force;
		}
	}

	
	int incomeOrbDelay = 50;
	int incomeOrbCounter = incomeOrbDelay;
	@Override
	public void update(float delta, Vector2[] starPositions, ListIterator<Orb> itr) {

		// add population to charged star if a regular charge orb
		if (charging) {
			if (type == OrbType.ORB) {
				player.income += ammoRate;

				activeStar.addPop(popRate, player.number); // TODO do population / ammo calcs in star?
			}
		}
		super.update(delta, starPositions, itr);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (charging) {
			// Updating the beam angle can't occur in the update method, because it must occur after super.update.
			float beamAngle = activeStar.getButtonCenter().sub(orbButton.getCenter()).angle();
			// Move to the orb's current position and point toward the star.
			chargeButton.setCenter(orbButton.getCenter());
			chargeButton.setRotation(beamAngle);
			chargeButton.draw(batch, 1f);
		}
		super.draw(batch);
	}
	
	// TODO allow active star to switch?
	@Override
	public void beginSensorContact(Collidable obj) {
		if ((obj instanceof Star) & (!charging)) {
			charging = true;
			chargeButton.activate();
			orbButton.activate();
			activeStar = (Star) obj;
			activeStar.addOrb(this);
		}
	}

	@Override
	public void endSensorContact(Collidable obj) {
		if (obj.equals(activeStar)) {
			activeStar.removeOrb(this);
			activeStar = null;
			charging = false;
			chargeButton.deactivate();
			orbButton.deactivate();
		}
	}
	
	@Override
	public void removeSelf() {
		if (activeStar != null) {
			activeStar.removeOrb(this);
			activeStar = null;
			charging = false;
		}
		super.removeSelf();
	}
	
}
