package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;
import java.util.Vector;

public class ChargeOrb extends Orb implements Collidable {

    public static class ChargeOrbState extends OrbState {
        float dAngle = 0f;
        int star = -1;
        boolean lockedOn = false;
    }

    public final ChargeOrbState state = new ChargeOrbState();
	static final LayeredButton[] chargeButtons = new LayeredButton[Model.numPlayers];

    private static final float angleThresh = ModelSettings.getFloatSetting("angleThresh");
    private static final float chargeRadius = ModelSettings.getFloatSetting("chargeRadius")*StarCycle.pixelsPerMeter;
    private static final float orbitScal = ModelSettings.getFloatSetting("orbitScal");
	private static final float beamWidth = 0.1f*StarCycle.pixelsPerMeter;

    private float measAngle = 0f;
    private float measAngOld = 0f;
    private float angleSum = 0f;
    private float beamAngle;
    private LinkedList<Star> activeStars = new LinkedList<Star>();

	public ChargeOrb(Player player, World world) {
		super(player, world);
		chargeButtons[playerNum] = new LayeredButton(orbButtons[playerNum].getCenter(),orbButtons[playerNum].getDims());
		chargeButtons[playerNum].addLayer(new SpriteLayer(StarCycle.tex.chargeBeam, new Vector2(chargeRadius/2f, 0f), new Vector2(chargeRadius,beamWidth)).setSpriteColor(player.colors[1]),LayerType.ACTIVE);
		chargeButtons[playerNum].deactivate();
	}


	@Override
	public Vector2 getGravForce(Star[] stars) {
		if (state.star == -1) { // if not orbiting any star
			return super.getGravForce(stars);
		}

		// only consider the current charge star
		else {
		    Vector2 force = new Vector2();

			// add component from regular gravity for the active star, ignoring others
			// because we assume the orbs dont affect stars, we can treat all objects as having mass 1.
            Star chargeStar = stars[state.star];
            Vector2 starPos = chargeStar.position;

            float dist = (float) Math.sqrt(Math.pow(starPos.x-state.x,2) + Math.pow(starPos.y-state.y,2));
            float scal = stars[state.star].mass * gravScalar / (dist * dist * dist);
			force.add(scal * (starPos.x - state.x), scal * (starPos.y - state.y));

            // TODO more efficient to not create vector 2's here?
			// adjust orbit toward stable velocity
			Vector2 diffVec = new Vector2(starPos.x - state.x, starPos.y - state.y).nor(); // unit vector from star to orb
            diffVec.scl(diffVec.x * state.v + diffVec.y * state.w); // dot(diffVec, state.vel)
            Vector2 dir = new Vector2(state.v - diffVec.x, state.w - diffVec.y); // direction orth to star
			dir.nor().scl((float) Math.sqrt(chargeStar.mass * gravScalar) / (dist)); // target velocity
			force.add(dir.sub(state.x, state.y).scl(orbitScal)); // adjust velocity toward target

			return force;
		}
	}

    private Vector2 vec = new Vector2();
    private float dTheta;
	@Override
	public void update(Star[] stars) {

        if (state.lockedOn) {

            // when locked on, just age and rotate at constant rate around star
            state.age++;
            if ((state.age > lifeSpan) & (lifeSpan != -1f)) { // remove expired orbs
                Model.toRemove.add(this);
                return;
            }

            Vector2 starPos = stars[state.star].position;
            vec.set(state.x - starPos.x, state.y - starPos.y);
            vec.rotate(state.v); // v is used as rot vel when locked on
            state.x = starPos.x + vec.x;
            state.y = starPos.y + vec.y;
        }
        else {

            super.update(stars); // delegates physics difference bt orbiting to getGravForce

            if (!(state.star == -1)) { // if not locked on, but orbiting: check for lock-on conditions
                measAngle = measureAngle(stars[state.star].position); // change in angle this frame
                dTheta = measAngOld - measAngle;
                if (Math.abs(dTheta) > MathUtils.PI) {
                    dTheta += -1 * Math.signum(dTheta) * MathUtils.PI2;
                }
                angleSum += dTheta;
                measAngOld = measAngle;

                if (Math.abs(angleSum) > angleThresh) {
                    lockOn(stars[state.star], (float)(dTheta * 180f/Math.PI));
                }
            }
        }
        beamAngle = stars[state.star].getButtonCenter().sub(orbButtons[playerNum].getCenter()).angle();
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!(state.star == -1)) {

			// Move to the orb's current position and point toward the star.
			chargeButtons[playerNum].setCenter(orbButtons[playerNum].getCenter());
			chargeButtons[playerNum].setRotation(beamAngle);
			chargeButtons[playerNum].draw(batch, 1f);
		}
		super.draw(batch);
	}

    private float measureAngle(Vector2 position) {
        return MathUtils.atan2(position.x - state.x, position.y - state.y) + MathUtils.PI;
    }

    private void resetLockCounter(Star star) {
        angleSum = 0f;
        measAngOld = measureAngle(star.position);
    }

	@Override
	public void beginSensorContact(Collidable obj) {
		if (obj instanceof Star) {
            activeStars.addLast((Star)obj);
            // activate charge beam and set star
            if (state.star == -1) {
                state.star = ((Star) obj).index;
                chargeButtons[playerNum].activate();
                resetLockCounter((Star)obj);
            }
		}
    }

	@Override
	public void endSensorContact(Collidable obj) {

        if (obj instanceof Star) {

            int idx = ((Star) obj).index;
            activeStars.remove(obj); // remove from the active set

            // if we just left range of the current charge star
            if (idx == state.star) {
                if (!activeStars.isEmpty()) {
                    Star star = activeStars.getFirst();
                    state.star = star.index;
                    resetLockCounter(star);
                }
                else {
                    state.star = -1;
                    chargeButtons[playerNum].deactivate();
                }
            }
        }
	}

    public void lockOn(Star star, float angle) {
        state.lockedOn = true;
        orbButtons[playerNum].activate();
        state.v = dTheta;
        // TODO change star state here?
    }

    @Override
    public void reset() {
        super.reset();
        this.state.lockedOn = false;
        this.state.star = -1;
        this.state.dAngle = 0f;
        this.activeStars.clear();
    }

    public void init(ChargeOrbState state) {
        init(state.x, state.y, state.v, state.w);
        this.state.age = state.age;
        this.state.lockedOn = state.lockedOn;
        this.state.star = state.star;
        this.state.dAngle = state.dAngle;
    }

}
