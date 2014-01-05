package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.ListIterator;

public class ChargeOrb extends Orb implements Collidable {

	public static float orbitScal;

	public LinkedList<Star> activeStars = new LinkedList<Star>();
    public Star chargeStar = null;
	public boolean charging = false;
    public boolean lockedOn = false;

    private float dAngle = 0f;
    private float angleSum = 0f;

	LayeredButton chargeButton;

    private static final float angleThresh = UserSettingz.getFloatSetting("angleThresh");
	private static final float beamWidth = 0.1f*StarCycle.pixelsPerMeter;
	private static final float chargeRadius = UserSettingz.getFloatSetting("chargeRadius")*StarCycle.pixelsPerMeter;

	public ChargeOrb(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		super(player, position, velocity, model, type);

		orbitScal = UserSettingz.getFloatSetting("orbitScal");

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

		// only consider the current charge star
		else {
			force.x = 0;
			force.y = 0;
			oldVel.set(body.getLinearVelocity());

			// add component from regular gravity for the active star, ignoring others
			// because we assume the orbs dont affect stars, we can treat all objects as having mass 1.
            float dist = position.dst(chargeStar.position);
            float scal = chargeStar.mass * gravScalar / (dist * dist * dist);
			force.add(scal * (chargeStar.position.x - position.x), scal * (chargeStar.position.y - position.y));

			// adjust orbit toward stable velocity
			diffVec.set(chargeStar.position.x - position.x, chargeStar.position.y - position.y).nor(); // unit vector from star to orb
            Vector2 comp = diffVec.scl(oldVel.dot(diffVec));
			dir.set(oldVel.x - comp.x, oldVel.y - comp.y); // direction orth to star
			dir.nor().scl((float) Math.sqrt(chargeStar.mass * gravScalar) / (dist)); // target velocity
			force.add(dir.sub(oldVel).scl(orbitScal)); // adjust velocity toward target

			return force;
		}
	}

    void setPosition(float x, float y) {                           // TODO move to orb?
        //Gdx.app.log("setting charge orb position",  x + ", " + y);
        position.set(x, y);
        body.setTransform(x, y, 0f);
        orbButton.setPosition(x * StarCycle.pixelsPerMeter, y * StarCycle.pixelsPerMeter);
    }

    private Vector2 vec = new Vector2();
    private float measAngle = 0f;
    private float measAngOld = 0f;
	@Override
	public void update(float delta, Vector2[] starPositions) {

        if (lockedOn) {

            // when locked on, just age and rotate at constant rate around star
            age += delta;
            if ((age > lifeSpan) & (lifeSpan != -1f)) { // remove expired orbs
                removeSelf();
            }

            angle += rotVel;
            vec.rotate((float)(dAngle * 180f/Math.PI));
            setPosition(chargeStar.position.x + vec.x,
                        chargeStar.position.y + vec.y);


        }
        else {

            super.update(delta,starPositions); // delegates physics difference bt charging to getGravForce

            if (charging) { // if not locked on, but charging: check for lock-on conditions
                measAngle = measureAngle(chargeStar.position); // change in angle this frame
                dAngle = measAngOld - measAngle;
                if (Math.abs(dAngle) > MathUtils.PI) {
                    dAngle += -1 * Math.signum(dAngle) * MathUtils.PI2;
                }
                angleSum += dAngle;
                measAngOld = measAngle;

                if (Math.abs(angleSum) > angleThresh) {
                    lockedOn = true;
                    orbButton.activate();
                    chargeStar.addOrb(this);
                    // set vector to be rotated dAngle
                    vec.set(position.x - chargeStar.position.x, position.y - chargeStar.position.y);
                }
            }
        }
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (charging) {
			// Updating the beam angle can't occur in the update method, because it must occur after super.update.
			float beamAngle = activeStars.getFirst().getButtonCenter().sub(orbButton.getCenter()).angle();
			// Move to the orb's current position and point toward the star.
			chargeButton.setCenter(orbButton.getCenter());
			chargeButton.setRotation(beamAngle);
			chargeButton.draw(batch, 1f);
		}
		super.draw(batch);
	}

    private float measureAngle(Vector2 position) {
        return MathUtils.atan2(position.x - this.position.x, position.y - this.position.y) + MathUtils.PI;
    }

	@Override
	public void beginSensorContact(Collidable obj) {
		if (obj instanceof Star) {

            // activate charge beam and orb
            if (!charging) {
                chargeStar = (Star)obj;
                charging = true;
                chargeButton.activate();
                resetLockCounter();
            }
            // add the star to the orb's active set
            assert !activeStars.contains(obj); // TODO remove
            activeStars.add((Star)obj);
		}
    }

    private void resetLockCounter() {
        angleSum = 0f;
        //dAngle = 0f;
        measAngOld = measureAngle(chargeStar.position);
    }

	@Override
	public void endSensorContact(Collidable obj) {

        if (obj instanceof Star) {
            activeStars.remove(obj); // remove from the active set

            if (obj.equals(chargeStar)) {
                if (lockedOn) { // losing lock on
                    chargeStar.removeOrb(this);
                    lockedOn = false;
                    orbButton.deactivate();
                }

                if (activeStars.size() == 0) { // if no more active stars, stop charge beam
                    chargeStar = null;
                    charging = false;
                    chargeButton.deactivate();
                }
                else {
                    chargeStar = activeStars.getFirst(); // set to next in priority
                    resetLockCounter();
                }
            }
        }
	}

	@Override
	public void removeSelf() {
		if (lockedOn) {
            lockedOn = false;
			chargeStar.removeOrb(this);
		}
		super.removeSelf();
	}

}
