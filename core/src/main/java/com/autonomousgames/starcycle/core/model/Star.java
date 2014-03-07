package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.Layer;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Star extends Orbitable implements Collidable {

    public static class StarState {
        public int index = -1;
        public float x = 0f;
        public float y = 0f;
        public final float[] possession = new float[Model.numPlayers];
        public final int[] numActiveOrbs = new int[Model.numPlayers];
    }
    // TODO need index as part of state?

    public static final StarState state = new StarState();

    static final float popRate = ModelSettings.getFloatSetting("popRate");
    static final float sideScale = 2f * 0.57735f; // The second value is tand(30deg), but MathUtils doesn't have tangent.
    static final float scaleRange = (432f - 143f) / 143f; // Scale percent, for maximum side length, over 100% of original size.

    public final float maxPop;
    public final float radius;
    public final LinkedList<ChargeOrb> activeOrbs = new LinkedList<ChargeOrb>();

    final Body body;
    final float maxOrbs;
    final float orbitSpeed;
    final float[] populations;
    final float rotateSpeed = MathUtils.random(0.2f, 0.4f)*(1-2*MathUtils.random(1)); // This is purely visual.
    final LayeredButton starButton;
    final Color[][] playerColors = new Color[Model.numPlayers][];

    public float mass;
    public boolean hitByNova = false;

    float startPercent;
    PathType pathMap;
    ArrayList<LayeredButton> controlButtons = new ArrayList<LayeredButton>();

    // XXX change all position arguments to state.x and state.y
    //public boolean targetted = false;

	// Drawing stuff
    // TODO do we need to store all of these for each star? can some be local or static?
	final float minSideLen; // Smallest hexagon side length.
	final Vector2 sideDims; // Dimensions of hexagon side length.
    final int quadLayer0; // Starting layer of star image quadrants.
    final Vector2 hexPos;
	final Vector2 imageDims;

	// constructor for single stars. Constructed in Level.java
	public Star(float radius,
                Vector2 position,
			    Player[] players,
                World world,
                int index,
                float orbitSpeed) {

        this.radius = radius;
        this.state.x = position.x;
        this.state.y = position.y;
        this.orbitSpeed = orbitSpeed;
        state.index = index;
        maxOrbs = ModelSettings.getFloatSetting("maxOrbs");
		mass = this.radius * this.radius;
		maxPop = 100 * radius;
		populations = new float[Model.numPlayers]; // populations are initialized to zero
        startPercent = 0;


		// create the box2d star body
        BodyDef starBodyDef = new BodyDef();
		starBodyDef.type = BodyType.StaticBody;
		starBodyDef.position.set(state.x, state.y);
		body = world.createBody(starBodyDef);

		CircleShape starShape = new CircleShape();
		starShape.setRadius(radius);

		FixtureDef starFixtureDef = new FixtureDef();
		starFixtureDef.shape = starShape;
		starFixtureDef.filter.categoryBits = Model.starCat;
		starFixtureDef.filter.maskBits = Model.starMask;
		body.createFixture(starFixtureDef);
		body.setUserData(this); // add a pointer back to this object in the Body
		starShape.dispose();

        // add charge sensor
		float chargeRadius = ModelSettings.getFloatSetting("chargeRadius") + this.radius;
		Sensor.addSensor(this, body, chargeRadius);

		// Create star visual
		imageDims = new Vector2(radius, radius).scl(StarCycle.pixelsPerMeter);
		starButton = getButton(position.cpy().scl(StarCycle.pixelsPerMeter), radius);
        quadLayer0 = starButton.getLayerNum()-4;
		minSideLen = imageDims.x * 142f/512f * sideScale; // This is the minimum hexagon side length.
		hexPos = new Vector2(imageDims.x*142f/512f, 0f); // Starting position (minimum).
		sideDims = new Vector2(imageDims.x/24f, minSideLen); // Minimum dimensions per hexagon side.
        for (int i = 0; i < Model.numPlayers; i ++) {
            // Each player gets a "button."
            controlButtons.add(getControlButton(starButton.getCenter(), imageDims.x, players[i].colors[0], i));
            playerColors[i] = players[i].colors;
        }
	}

	public Star(float radius,
			Player[] players,
            Vector2 center,
            World world,
            int index,
            PathType pathMap,
			float startPercent,
            float rotSpeed) {

		this(radius, new Vector2(), players, world, index, rotSpeed); // TODO why was center not getting passed through here?
		this.pathMap = pathMap;
		this.startPercent = startPercent;
	}

    public void setState(StarState state) {
        this.state.index = state.index;
        this.state.x = state.x;
        this.state.y = state.y;

        for (int i=0; i < Model.numPlayers; i++) {
            this.state.possession[i] = state.possession[i];
            this.state.numActiveOrbs[i] = state.numActiveOrbs[i];
        }
    }

    public Vector2 getPosition() {
        return new Vector2(this.state.x, this.state.y);
    }

	public void draw(SpriteBatch batch) {

		for (int i = 0; i < Model.numPlayers; i ++) {
			// The new control percent is calculated and stored for later.
			float newPercent = populations[i] / maxPop;
			// If the button has become active:
			if (0f < newPercent && newPercent < 1f && !(controlButtons.get(i).isActive())) {
				controlButtons.get(i).activate();
			}
			// Else if the button has become inactive:
			else if ((newPercent == 0f || newPercent == 1f) && controlButtons.get(i).isActive()) {
				controlButtons.get(i).deactivate();
			}
			// If the the star has become fully controlled:
			if (newPercent >= 0.99f && state.possession[i] < 0.99f) {
				// Tint the star image.
				for (int j = quadLayer0; j < quadLayer0 + 4; j ++) {
					starButton.getLayer(j).setColor(playerColors[i][0]);
				}
				starButton.activate();
			}
			// If the player has control: 
			if (Player.captureFrac <= newPercent && state.possession[i]< Player.captureFrac) {
				// Tint the inner portion.
                starButton.getLayer(1).setColor(playerColors[i][0]);
//				starButton.getLayer(2).setColor(players[i].colors[0]);
				starButton.lock();
			}
			// Update the control percent with the stored value.
            state.possession[i] = newPercent;
		}
		// If neither player has full control (the 0.99 threshold adds some debounce):
		if (state.possession[0] < 0.99f && state.possession[1] < 0.99f && starButton.isActive()) {
			starButton.deactivate();
			for (int i = quadLayer0; i < quadLayer0 + 4; i ++) {
				starButton.getLayer(i).setColor(Colors.night);
			}
            for (int i = 0; i < Model.numPlayers; i ++) {
                if (Player.captureFrac <= state.possession[i]) {
                    starButton.getLayer(1).setColor(playerColors[i][0]);
                }
            }
		}
		// If either player has full control, do not draw hexes:
		if (starButton.isActive() && (0.99f <= state.possession[0] || 0.99f <= state.possession[1])) {
			for (int i = 0; i < Model.numPlayers; i ++) {
				controlButtons.get(i).deactivate();
			}
            starButton.getLayer(1).setColor(Color.BLACK);
		}
		// If neither player has control, reset the inner portion:
		if (state.possession[0] < Player.captureFrac && state.possession[1] < Player.captureFrac && starButton.isLocked()) {
			starButton.unlock();
            starButton.getLayer(1).setColor(Color.BLACK);
//			starButton.getLayer(2).setColor(Color.BLACK);
		}

		// Update the button position and angle, then draw:
		starButton.setCenter(state.x * StarCycle.pixelsPerMeter, state.y * StarCycle.pixelsPerMeter);
		starButton.rotate(rotateSpeed);
		starButton.draw(batch, 1f);
		// Update the control hexagons:
		for (int i = 0; i < Model.numPlayers; i ++) {
			LayeredButton button = controlButtons.get(i);
			button.setCenter(starButton.getCenter()); // Update position.
			button.rotate(rotateSpeed); // Update angle/
			// Calculate the inner radius of the hexagon:
			float apothem = imageDims.x * (142f + (432f - 142f)*state.possession[i]) / 512f;
			for (int j = 0; j < 6; j ++) {
				Layer layer = button.getLayer(j);
				layer.setCenter(layer.getCenter().nor().scl(apothem)); // Scale the image radius.
				layer.setScaleY(state.possession[i]*scaleRange + 1f); // Scale the side length (not width).
			}
			button.draw(batch,1f);
		}
	}

	public void addPop(float popUp, int playerNum) {
		populations[playerNum] += popUp;

		float popSum = 0f;
		for (float pop : populations){
			popSum += pop;
		}
		popSum += popUp;
		if (popSum > maxPop) {
			float excess = popSum - maxPop;

			if (Model.numPlayers == 2) {
				if (populations[1 - playerNum] < excess / 2f) {
					populations[1 - playerNum] = 0;
					populations[playerNum] = maxPop;
				} else {
					populations[playerNum] -= excess / 2f;
					populations[1 - playerNum] -= excess / 2f;
				}
			} else if (Model.numPlayers == 1) {
				populations[playerNum] -= excess;
			}
			//popSum = maxPop;
		}
	}

    // TODO add income orbs
    //private float[] nonlinearIncome = {0f, 0f};
    public void updateControl() {
        for (int i = 0; i < Model.numPlayers; i++) {

            addPop(popRate*state.numActiveOrbs[i], i);

            //nonlinearIncome[i] += ammoRate * ((float) Math.pow(((double) state.numActiveOrbs[i]), ((double)2/3)));

            //while(showIncomeOrbs && (nonlinearIncome[i] > incAmmoThresh)) {
            //    // emit fake income orb
            //    nonlinearIncome[i] -= incAmmoThresh;
            //    Color color = (MathUtils.random(1f) < 0.3f) ? player.colors[0] : player.colors[1];

            //    vel = new Vector2(MathUtils.random(-initVelScale,initVelScale), MathUtils.random(-initVelScale,initVelScale));
            //    pos = this.getButtonCenter();
            //    nor = vel.len();
            //    pos = pos.add(this.radius*StarCycle.pixelsPerMeter*vel.x/nor, this.radius*StarCycle.pixelsPerMeter*vel.y/nor);
            //    player.incomeOrbs.add(new ImageOrb(StarCycle.tex.bgMote, incOrbSize * StarCycle.screenHeight, pos,
            //            StarCycle.screenWidth, StarCycle.screenHeight, vel, new Vector2()).tint(color).set_alpha(incOrbAlpha));
            //}
        }
    }


    // TODO move more of this to level?

	void updatePosition(float delta) {

		if (pathMap != null) {
		    Vector2 pos = pathMap.getPosition(t, startPercent);
            state.x = pos.x;
            state.y = pos.y;
			body.setTransform(pos, 0f);
			t += orbitSpeed * delta;
		}
	}

	// stars unaffected by contact
	// TODO are these Overrides necessary? Collidable doesn't have anything to override? Do Stars need to implement Collidable at all?
    // Do they need Colliadable so that (star instanceof Collidable) returns true elsewhere?
    // If that's the case, do they need the Overrides so that these methods can be called, even though they do nothing?
	@Override
	public void beginSensorContact(Collidable obj) {
	}

	@Override
	public void endSensorContact(Collidable obj) {
	}

	@Override
	public void collision(Collidable obj) {  }

	public void addOrb(ChargeOrb orb) {
        state.numActiveOrbs[orb.playerNum]++;
        if (activeOrbs.size() > maxOrbs) {
            Model.toRemove.add(activeOrbs.getFirst()); // if above capacity remove oldest (fifo linked list)
        }
	}

	public void removeOrb(ChargeOrb orb) {
        state.numActiveOrbs[orb.playerNum]--;
        activeOrbs.remove(orb);
	}

	// Return the starButton center (in pixels):
	public Vector2 getButtonCenter() {
		return starButton.getCenter();
	}

    public void moveStar(float x, float y) {
        state.x += x;
        state.y += y;
        body.setTransform(state.x, state.y, 0f);
    }

    public static LayeredButton getButton(Vector2 position, float radius) {
        Vector2 dims = new Vector2(radius, radius).scl(StarCycle.pixelsPerMeter);
        LayeredButton button = new LayeredButton(position);
        Vector2 quadPos = dims.cpy().div(2f);
        button.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, dims.cpy().scl(2.75f)));
        button.addLayer(new SpriteLayer(StarCycle.tex.circle, dims.cpy().scl(0.8f)).setSpriteColor(Color.BLACK));
        // The main star visual is drawn as four quadrants.
        for (int i = 0; i < 4; i++) {
            button.addLayer(new SpriteLayer(StarCycle.tex.hexStar, quadPos.cpy().rotate(90f*i), dims, Colors.night, (90f*i)));
        }
        button.deactivate(); // This is a hackish way of noting whether either player has 100% control.
        button.unlock(); // This is a hackish way of noting whether either player has 50% control.
        return button;
    }

    public static LayeredButton getControlButton(Vector2 pos, float dims, Color color, int player, float percent) {
        LayeredButton button = new LayeredButton(pos);
        Vector2 hexPos = new Vector2(dims*142f/512f, 0f); // Starting position (minimum).
        float minSideLen = dims * 142f/512f * sideScale; // This is the minimum hexagon side length.
        Vector2 sideDims = new Vector2(dims/24f, minSideLen); // Minimum dimensions per hexagon side.
        for (int i = 0; i < 6; i ++) {
            // The buttons have six layers, one for each side of the hexagon.
            float sideAngle = 60f*i+30f*player;
            button.addLayer(new SpriteLayer(StarCycle.tex.block, hexPos.cpy().rotate(sideAngle), sideDims.cpy(), color, sideAngle), LayerType.ACTIVE);
        }
        if (percent == 0f) {
            button.deactivate(); // Start off. Active means the hexagons are drawn.
        }
        else {
            float apothem = dims * (142f + (432f - 142f)*percent) / 512f;
            for (int i = 0; i < 6; i ++) {
                Layer layer = button.getLayer(i);
                layer.setCenter(layer.getCenter().nor().scl(apothem)); // Scale the image radius.
                layer.setScaleY(percent*scaleRange + 1f); // Scale the side length (not width).
            }
        }
        return button;
    }

    public static LayeredButton getControlButton(Vector2 pos, float dims, Color color, int player) {
        return getControlButton(pos, dims, color, player, 0f);
    }

    public void setControlPercent(int player, float percent) {
        populations[player] = maxPop*percent;
    }

    public void gravityOff() {
        mass = 0f;
    }

    public void gravityOn() {
        mass = radius * radius;
    }
}
