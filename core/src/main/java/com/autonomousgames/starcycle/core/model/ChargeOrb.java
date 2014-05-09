package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;

public class ChargeOrb extends Orb implements Collidable {
	//static final LayeredButton[] chargeButtons = new LayeredButton[Model.numPlayers];

    private static final float angleThresh = ModelSettings.getFloatSetting("angleThresh");
    private static final float chargeRadius = ModelSettings.getFloatSetting("chargeRadius")*StarCycle.pixelsPerMeter;
    private static final float orbitScal = ModelSettings.getFloatSetting("orbitScal");
	private static final float beamWidth = 0.1f*StarCycle.pixelsPerMeter;

    private float measAngle = 0f;
    private float measAngOld = 0f;
    private float angleSum = 0f;
    private float beamAngle = 0f;
    private LinkedList<Star> activeStars = new LinkedList<Star>();

	public ChargeOrb(Player player, World world, Class cls) {
		super(player, world, new ChargeOrbState(Orb.uidCounter++), (cls == Void.class) ?
                        (int) ModelSettings.getFloatSetting("powerupLifeSpan") :
                        (int) ModelSettings.getFloatSetting("orbLifeSpan"));
		//chargeButtons[playerNum] = new LayeredButton(orbButtons[playerNum].getCenter(),orbButtons[playerNum].getDims());
		//chargeButtons[playerNum].addLayer(new SpriteLayer(StarCycle.tex.chargeBeam, new Vector2(chargeRadius/2f, 0f), new Vector2(chargeRadius,beamWidth)).setSpriteColor(player.colors[1]),LayerType.ACTIVE);
		//chargeButtons[playerNum].deactivate();
	}

    public ChargeOrb(Player player, World world) {
        this(player, world, ChargeOrb.class);
    }


    final Vector2 delta = new Vector2();
    final Vector2 gamma = new Vector2();
    // TODO move local vars to final class members for optimization
	@Override
	public Vector2 getGravForce(Star[] stars) {
		if (((ChargeOrbState)state).star == -1) { // if not orbiting any star
			return super.getGravForce(stars);
		}

		// only consider the current charge star
		else {
		    Vector2 force = new Vector2();

			// add component from regular gravity for the active star, ignoring others
            Star chargeStar = stars[((ChargeOrbState)state).star];
            Vector2 starPos = new Vector2(chargeStar.state.x, chargeStar.state.y);

            float dist = (float) Math.sqrt(Math.pow(starPos.x-state.x, 2) + Math.pow(starPos.y-state.y, 2));
            float scal = stars[((ChargeOrbState)state).star].mass * gravScalar / (dist * dist);
            delta.set((starPos.x - state.x)/dist, (starPos.y - state.y)/dist); // normalized vector from orb to star
			force.add(scal * delta.x, scal * delta.y);

            // dprod is the components of velocity toward the star
            float radialComp = (delta.x*state.v+delta.y*state.w);

            // gamma is set to the normalized vector tangent to the circular orbit
            gamma.set(state.v - radialComp * delta.x, state.w - radialComp * delta.y).nor();
            gamma.scl(6f * (float)Math.sqrt(chargeStar.mass * gravScalar / dist)); // then scaled to target velocity
			force.add(gamma.sub(state.v, state.w).scl(orbitScal)); // adjust velocity toward target

            // harder alternative
//            force.add(-orbitScal*dprod*delta.x, -orbitScal*dprod*delta.y);

			return force;
		}
	}

    // TODO optimize starPos and local vars
    private Vector2 vec = new Vector2();
    private float dTheta;
	@Override
	public void update(Star[] stars) {

        if (((ChargeOrbState)state).lockedOn) {

            // when locked on, just age and rotate at constant rate around star
            state.age++;
            if ((state.age > lifeSpan) & (lifeSpan != -1f)) { // remove expired orbs
                Model.toRemove.add(this);
                return;
            }

            Vector2 starPos = new Vector2(stars[((ChargeOrbState)state).star].state.x, stars[((ChargeOrbState)state).star].state.y);
            vec.set(state.x - starPos.x, state.y - starPos.y);
            vec.rotate(((float)(state.v * 180f/Math.PI))); // v is used as rot vel when locked on
            state.x = starPos.x + vec.x;
            state.y = starPos.y + vec.y;
            body.setTransform(state.x, state.y, 0f);
        }
        else {

            super.update(stars); // delegates physics difference bt orbiting to getGravForce

            if (!(((ChargeOrbState)state).star == -1)) { // if not locked on, but orbiting: check for lock-on conditions
                measAngle = measureAngle(stars[((ChargeOrbState)state).star].state.x, stars[((ChargeOrbState)state).star].state.y); // change in angle this frame
                dTheta = measAngOld - measAngle;
                if (Math.abs(dTheta) > MathUtils.PI) {
                    dTheta += -1 * Math.signum(dTheta) * MathUtils.PI2;
                }
                angleSum += dTheta;
                measAngOld = measAngle;

                if (Math.abs(angleSum) > angleThresh) {
                    lockOn(stars[((ChargeOrbState)state).star]);
                }
            }
        }
        if (((ChargeOrbState)state).star >= 0) { // if locked on
            // TODO update beam angle
            //beamAngle = stars[((ChargeOrbState)state).star].getButtonCenter().sub(orbButtons[playerNum].getCenter()).angle();
        }
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!(((ChargeOrbState)state).star == -1)) { // if within range of a star
            // draw the charge beam
            // Move to the orb's current position and point toward the star.
            //chargeButtons[playerNum].setCenter(orbButtons[playerNum].getCenter());
            //chargeButtons[playerNum].setRotation(beamAngle);
            //chargeButtons[playerNum].draw(batch, 1f);
        }
        super.draw(batch);
	}

    private float measureAngle(float x, float y) {
        return MathUtils.atan2(x - state.x, y - state.y) + MathUtils.PI;
    }

    private void resetLockCounter(Star star) {
        angleSum = 0f;
        measAngOld = measureAngle(star.state.x, star.state.y);
    }

	@Override
	public void beginSensorContact(Collidable obj) {
		if (obj instanceof Star) {

            if (!((ChargeOrbState)state).lockedOn) {
                activeStars.addLast((Star)obj);
                if (((ChargeOrbState)state).star == -1) {
                    ((ChargeOrbState)state).star = ((Star) obj).state.index;
                    resetLockCounter((Star)obj);
                }
            }
		}
    }

	@Override
	public void endSensorContact(Collidable obj) {

        if (obj instanceof Star) {
            if (!((ChargeOrbState)state).lockedOn) {

                int idx = ((Star) obj).state.index;
                activeStars.remove(obj); // remove from the active set

                // if we just left range of the current charge star
                if (idx == ((ChargeOrbState)state).star) {
                    if (!activeStars.isEmpty()) {
                        Star star = activeStars.getFirst();
                        ((ChargeOrbState)state).star = star.state.index;
                        resetLockCounter(star);
                    }
                    else {
                        ((ChargeOrbState)state).star = -1;
                    }
                }
            }
        }
	}

    public void lockOn(Star star) {
        ((ChargeOrbState) state).star = star.state.index;
        ((ChargeOrbState) state).lockedOn = true;
        state.v = dTheta;
        if (getClass() == ChargeOrb.class) {
            star.addOrb(this);
        }
    }

    @Override
    public void reset() {

        // TODO remove orb from star lists here?
        super.reset();
        ((ChargeOrbState)state).lockedOn = false;
        ((ChargeOrbState)state).star = -1;
        ((ChargeOrbState)state).dAngle = 0f;
        activeStars.clear();
    }

    @Override
    public void init(OrbState state) {
        init(state.x, state.y, state.v, state.w);
        this.state.age = state.age;
        ((ChargeOrbState)this.state).lockedOn = ((ChargeOrbState)this.state).lockedOn;
        ((ChargeOrbState)this.state).star = ((ChargeOrbState)this.state).star;
        ((ChargeOrbState)this.state).dAngle = ((ChargeOrbState)this.state).dAngle;
    }
}
