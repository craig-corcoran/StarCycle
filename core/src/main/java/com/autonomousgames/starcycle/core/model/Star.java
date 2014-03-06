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

    public class StarState {
        int num = -1;
        float x = 0f;
        float y = 0f;
        float[] possession = new float[numPlayers];
        int[] numActiveOrbs = new int[numPlayers];
    }

    public static final StarState state;

	public static float captureRatio;
	public final float radius;
	public float mass;
	public final float maxPop;
    public boolean targetted;
	public Body body;
    private int numPlayers;
	public float[] populations;
	public float[] controlPercents = new float[]{0f, 0f};
	public float popSum = 0;
    public Float rotSpeed;

    public int[] numOrbs = new int[]{0, 0};
    public int[] numVoids = new int[]{0, 0};

    private final float ammoRate = ModelSettings.getFloatSetting("ammoRate");
    private final float popRate = ModelSettings.getFloatSetting("popRate");

    private LinkedList<ChargeOrb> activeOrbs = new LinkedList<ChargeOrb>();
	private HashSet<Void> activeVoids = new HashSet<Void>();

	public final int index;
	private Player[] players;
	private PathType pathMap = null;
	private float startPercent;
    public boolean hitByNova = false;

	// Drawing stuff
	LayeredButton starButton;
//	TextureRegion starImage = StarCycle.tex.hexStar;
	private Vector2 imageDims;
	private float rotateSpeed = MathUtils.random(0.2f, 0.4f)*(1-2*MathUtils.random(1)); // This is purely visual.
	ArrayList<LayeredButton> controlButtons = new ArrayList<LayeredButton>();
//	TextureRegion conIm = StarCycle.tex.block; // Image for the sides of the control hexes.
	float apothem; // Inner radius of hexagon.
	// Ratio of apothem to side length:
	static float sideScale = 2f * 0.57735f; // The second value is tand(30deg), but MathUtils doesn't have tangent.
	float minSideLen; // Smallest hexagon side length.
	static float scaleRange = (432f - 143f) / 143f;; // Scale percent, for maximum side length, over 100% of original size.
	Vector2 sideDims; // Dimensions of hexagon side length.
	int quadLayer0; // Starting layer of star image quadrants.


    final float maxOrbs;

	// constructor for single stars. Constructed in Level.java
	public Star(float radius, Vector2 position,
			Player[] players, World world, int index, float rotSpeed) {
		captureRatio = ModelSettings.getFloatSetting("captureRatio");
        maxOrbs = ModelSettings.getFloatSetting("maxOrbs");
		this.radius = radius;
		this.mass = this.radius * this.radius;
		this.maxPop = 100 * radius;
		this.position = position;
		this.index = index;
		this.numPlayers = players.length;
		this.players = players;
		this.rotSpeed = rotSpeed;

		populations = new float[numPlayers]; // populations are initialized to
		// zero


		// create the box2d star body
        BodyDef starBodyDef = new BodyDef();
		starBodyDef.type = BodyType.StaticBody;
		starBodyDef.position.set(this.position);
		body = world.createBody(starBodyDef);

		CircleShape starShape = new CircleShape();
		starShape.setRadius(radius);

		FixtureDef starFixtureDef = new FixtureDef();
		starFixtureDef.shape = starShape;
		starFixtureDef.filter.categoryBits = Model.starCat;
		starFixtureDef.filter.maskBits = Model.starMask;
		body.createFixture(starFixtureDef);
		body.setUserData(this); // add a pointer back to this object in the Body
		// (box2d returns bodies that collide)
		starShape.dispose();

		float chargeRadius = ModelSettings.getFloatSetting("chargeRadius") + this.radius;

		// add charge sensor 
		Sensor.addSensor(this, body, chargeRadius);

		// Create star visual
		imageDims = new Vector2(radius, radius).scl(StarCycle.pixelsPerMeter);
		starButton = getButton(position.cpy().scl(StarCycle.pixelsPerMeter), radius);
        quadLayer0 = starButton.getLayerNum()-4;
		minSideLen = imageDims.x * 142f/512f * sideScale; // This is the minimum hexagon side length.
		Vector2 hexPos = new Vector2(imageDims.x*142f/512f, 0f); // Starting position (minimum).
		sideDims = new Vector2(imageDims.x/24f, minSideLen); // Minimum dimensions per hexagon side.
        for (int i = 0; i < players.length; i ++) {
            // Each player gets a "button."
            controlButtons.add(getControlButton(starButton.getCenter(), imageDims.x, players[i].colors[0], i));
        }
	}

	public Star(float radius, Vector2 center,
			Player[] players, World world, int index, PathType pathMap,
			float startPercent, float rotSpeed) {
		this(radius, new Vector2(), players, world, index, rotSpeed);
		this.pathMap = pathMap;
		this.startPercent = startPercent;
	}

	public void draw(SpriteBatch batch) {

		for (int i = 0; i < players.length; i ++) {
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
			if (newPercent >= 0.99f && controlPercents[i] < 0.99f) {
				// Tint the star image.
				for (int j = quadLayer0; j < quadLayer0 + 4; j ++) {
					starButton.getLayer(j).setColor(players[i].colors[0]);
				}
				starButton.activate();
			}
			// If the player has control: 
			if (captureRatio <= newPercent && controlPercents[i] < captureRatio) {
				// Tint the inner portion.
                starButton.getLayer(1).setColor(players[i].colors[0]);
//				starButton.getLayer(2).setColor(players[i].colors[0]);
				starButton.lock();
			}
			// Update the control percent with the stored value.
			controlPercents[i] = newPercent;
		}
		// If neither player has full control (the 0.99 threshold adds some debounce):
		if (controlPercents[0] < 0.99f && controlPercents[1] < 0.99f && starButton.isActive()) {
			starButton.deactivate();
			for (int i = quadLayer0; i < quadLayer0 + 4; i ++) {
				starButton.getLayer(i).setColor(Colors.night);
			}
            for (int i = 0; i < players.length; i ++) {
                if (captureRatio <= controlPercents[i]) {
                    starButton.getLayer(1).setColor(players[i].colors[0]);
                }
            }
		}
		// If either player has full control, do not draw hexes:
		if (starButton.isActive() && (0.99f <= controlPercents[0] || 0.99f <= controlPercents[1])) {
			for (int i = 0; i < players.length; i ++) {
				controlButtons.get(i).deactivate();
			}
            starButton.getLayer(1).setColor(Color.BLACK);
		}
		// If neither player has control, reset the inner portion:
		if (controlPercents[0] < captureRatio && controlPercents[1] < captureRatio && starButton.isLocked()) {
			starButton.unlock();
            starButton.getLayer(1).setColor(Color.BLACK);
//			starButton.getLayer(2).setColor(Color.BLACK);
		}
		// Update the button position and angle, then draw:
		starButton.setCenter(position.cpy().scl(StarCycle.pixelsPerMeter));
		starButton.rotate(rotateSpeed);
		starButton.draw(batch, 1f);
		// Update the control hexagons:
		for (int i = 0; i < players.length; i ++) {
			LayeredButton button = controlButtons.get(i);
			button.setCenter(starButton.getCenter()); // Update position.
			button.rotate(rotateSpeed); // Update angle/
			// Calculate the inner radius of the hexagon:
			apothem = imageDims.x * (142f + (432f - 142f)*controlPercents[i]) / 512f;
			for (int j = 0; j < 6; j ++) {
				Layer layer = button.getLayer(j);
				layer.setCenter(layer.getCenter().nor().scl(apothem)); // Scale the image radius.
				layer.setScaleY(controlPercents[i]*scaleRange + 1f); // Scale the side length (not width).
			}
			button.draw(batch,1f);
		}
	}

	public void addPop(float popUp, int playerNum) {
		populations[playerNum] += popUp;

		popSum = 0f;
		for (float pop : populations){
			popSum += pop;
		}
		popSum += popUp;
		if (popSum > maxPop) {
			float excess = popSum - maxPop;

			if (numPlayers == 2) {
				if (populations[1 - playerNum] < excess / 2f) {
					populations[1 - playerNum] = 0;
					populations[playerNum] = maxPop;
				} else {
					populations[playerNum] -= excess / 2f;
					populations[1 - playerNum] -= excess / 2f;
				}
			} else if (numPlayers == 1) {
				populations[playerNum] -= excess;
			}
			popSum = maxPop;
		}
	}

    private Player player;
    private float rate;
    private float incAmmoThresh = ModelSettings.getFloatSetting("incAmmoThresh");
    private float initVelScale = ModelSettings.getFloatSetting("initVelScale");
    private float incOrbSize = ModelSettings.getFloatSetting("incOrbSize");
    private float incOrbAlpha = ModelSettings.getFloatSetting("incOrbAlpha");
    private Vector2 pos = new Vector2();
    private Vector2 vel = new Vector2();
    private float nor = 0;
    private float[] playerIncome = {0f, 0f};
    private float[] nonlinearIncome = {0f, 0f};
    public void updateControl() {
        for (int i = 0; i < players.length; i++) { // TODO iterator
            player = players[i];
            addPop(popRate*numOrbs[i], i);

            rate = ammoRate * numOrbs[i];
            player.ammo += rate;
            playerIncome[i] += rate;
            nonlinearIncome[i] += ammoRate * ((float) Math.pow(((double) numOrbs[i]), ((double)2/3)));

            while(player.showIncomeOrbs && (nonlinearIncome[i] > incAmmoThresh)) {
                // emit fake income orb
                nonlinearIncome[i] -= incAmmoThresh;
                Color color = (MathUtils.random(1f) < 0.3f) ? player.colors[0] : player.colors[1];

                vel = new Vector2(MathUtils.random(-initVelScale,initVelScale), MathUtils.random(-initVelScale,initVelScale));
                pos = this.getButtonCenter();
                nor = vel.len();
                pos = pos.add(this.radius*StarCycle.pixelsPerMeter*vel.x/nor, this.radius*StarCycle.pixelsPerMeter*vel.y/nor);
                player.incomeOrbs.add(new ImageOrb(StarCycle.tex.bgMote, incOrbSize * StarCycle.screenHeight, pos,
                        StarCycle.screenWidth, StarCycle.screenHeight, vel, new Vector2()).tint(color).set_alpha(incOrbAlpha));
            }
        }

//
// TODO sometimes emit (fake) income orb here
    }

	void updatePosition(float delta) {

		if (pathMap != null) {
			position = pathMap.getPosition(t, startPercent);
			body.setTransform(position, 0f);

			t += this.rotSpeed * delta;
		}
		else {
			position = body.getPosition();
			body.setTransform(position.x, position.y, 0f);
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

	public void addOrb(Orb orb) {
		if (orb.type == Orb.OrbType.ORB) {
			activeOrbs.add(((ChargeOrb) orb));
            if (activeOrbs.size() > maxOrbs) {
                activeOrbs.getFirst().removeSelf(); // if above capacity remove oldest (fifo linked list)
            }
            numOrbs[orb.player.number]++;
		}
		else if (orb.type == Orb.OrbType.VOID) {
			activeVoids.add((Void) orb);
            numVoids[orb.player.number]++; // keep player counts for easy access by bot
		}
        assert ((numVoids[0] + numVoids[1]) == getOrbCount(Orb.OrbType.VOID)) : "Wrong value for numVoids after adding.";
        assert ((numOrbs[0] + numOrbs[1]) == getOrbCount(Orb.OrbType.ORB)) : "Wrong value for numOrbs after adding.";
	}

	public void removeOrb(Orb orb) {
		if (orb.type == Orb.OrbType.ORB) {
            numOrbs[orb.player.number]--;
			activeOrbs.remove(orb);
		}
		else if (orb.type == Orb.OrbType.VOID) {
            numVoids[orb.player.number]--;
			activeVoids.remove(orb);
		}
        assert ((numVoids[0] + numVoids[1]) == getOrbCount(Orb.OrbType.VOID)) : "Wrong value for numVoids after removing.";
        assert ((numOrbs[0] + numOrbs[1]) == getOrbCount(Orb.OrbType.ORB)) : "Wrong value for numOrbs after removing.";
	}

	public int getOrbCount(Orb.OrbType type) {
		if (type == Orb.OrbType.ORB) {
			return activeOrbs.size();
		}
		else if (type == Orb.OrbType.VOID) {
			return activeVoids.size();
		}
		else {
			return 0;
		}
	}
	
	// Return the starButton center (in pixels):
	public Vector2 getButtonCenter() {
		return starButton.getCenter();
	}

    public void moveStar(float x, float y) {
        position = body.getPosition();
        body.setTransform(position.x + x, position.y + y, 0f);
        position = body.getPosition();
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
//        controlPercents[player] = percent;
        populations[player] = maxPop*percent;
    }

    public int getPlayerOrbs(int player) {
        return numOrbs[player];
    }

    public void gravityOff() {
        mass = 0f;
    }

    public void gravityOn() {
        mass = radius * radius;
    }
}
