package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez.TextureType;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool;

public class Orb implements Collidable, Pool.Poolable {

    static int uid = 0;

    public static class OrbState {
        public float x = 0f;
        public float y = 0f;
        public float v = 0f;
        public float w = 0f;
        public int age = 0;
        public int uid = Orb.uid++;
    }

    @Override
    public int hashCode() { return state.uid; }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) { return true; }
        if( o instanceof Orb ) { return this.state.uid == ((Orb)o).state.uid; }
        return false;
    }

    static int lifeSpan = (int) ModelSettings.getFloatSetting("orbLifeSpan");
    static final LayeredButton[] orbButtons = new LayeredButton[Model.numPlayers]; // XXX is it overkill to use LayeredButton to draw orbs?
    static final float radius = ModelSettings.getFloatSetting("OrbRadius");
    static final float gravScalar = ModelSettings.getFloatSetting("gravScalar");

    public final OrbState state = new OrbState();
    public final Body body; // Physics Box2D body
    final float rotVel;
    final int playerNum;

	public Orb(Player player, World world) {

        playerNum = player.number;
		Vector2 imageDims = new Vector2(radius, radius).scl(StarCycle.pixelsPerMeter);
        AtlasRegion[] textures = getTextures(player);

        orbButtons[player.number] = new LayeredButton(new Vector2().scl(StarCycle.pixelsPerMeter));
        orbButtons[player.number].addLayer(new SpriteLayer(StarCycle.tex.gradientRound, imageDims.cpy().scl(3f)).setSpriteColor(player.colors[0]), LayerType.ACTIVE);
        //orbButtons[player.number].deactivate();

        for (int i = 0; i < 2; i ++) {
            orbButtons[player.number].addLayer(new SpriteLayer(textures[i], imageDims).setSpriteColor(player.colors[i]));
        }

        // choose rotation velocity randomly
        int flip = (Math.random() > 0.5) ? 1 : 0;
        rotVel = (float) (Math.random() * 4f + 1) * (2 * flip - 1);

        BodyDef orbBodyDef = new BodyDef();
        orbBodyDef.type = BodyType.DynamicBody;
        orbBodyDef.position.set(state.x, state.y);
        body = world.createBody(orbBodyDef);

        CircleShape orbShape = new CircleShape();
        orbShape.setRadius(radius);
        FixtureDef orbFixtureDef = new FixtureDef();
        orbFixtureDef.shape = orbShape;

        orbFixtureDef.filter.categoryBits = Model.orbCategoryBits[player.number];
        orbFixtureDef.filter.maskBits = Model.orbMaskBits[player.number];
        body.createFixture(orbFixtureDef);
        body.setUserData(this); // add a pointer back to this object in the Body
        orbShape.dispose();
	}

    public static void setLifeSpan(int lifespan) {
        lifeSpan = lifespan;
    }

    static AtlasRegion[] getTextures(Player player) {
        return new AtlasRegion[]{
                        StarCycle.tex.skinMap.get(player.basetype).get(TextureType.ORB0),
                        StarCycle.tex.skinMap.get(player.basetype).get(TextureType.ORB1)};
    }
	
	public void draw(SpriteBatch batch) {
		// Only draw when visible
		if ((state.x > -radius*2f) & (state.y > -radius*2f)
				& (state.x < StarCycle.meterWidth + radius)
				& (state.y < StarCycle.meterHeight + radius)) {

            LayeredButton button = orbButtons[playerNum];
			button.setCenter(state.x, state.y);
            button.setRotation(rotVel * state.age); // XXX ok to move before each draw (is this part. expensive?)
            orbButtons[playerNum].draw(batch, 1f);
		}
	}

    public void update(Star[] stars) {
        state.age++;
        if ((state.age > lifeSpan) & (lifeSpan != -1f)) { // remove expired orbs
            Model.toRemove.add(this);
        }
        else {
            Vector2 pos = body.getPosition();
            state.x = pos.x;
            state.y = pos.y;

            Vector2 force = getGravForce(stars);
            state.v += force.x;
            state.w += force.y;
            body.setLinearVelocity(state.v, state.w);
        }
    }

    public Vector2 getGravForce(Star[] stars) {
        Vector2 force = new Vector2();
		for (Star star: stars) {
			// because we assume the orbs dont affect stars, we can treat all orbs as having mass 1.
            Star.StarState starState = star.state;
            float dist = (float) Math.sqrt(Math.pow(starState.x-state.x,2) + Math.pow(starState.y-state.y,2));
            float scal = star.mass * gravScalar / (dist * dist * dist);
			force.add(scal * (starState.x - state.x),
					  scal * (starState.y - state.y));
		}
		return force;
	}

	@Override
	public void beginSensorContact(Collidable obj) {
	}

	@Override
	public void endSensorContact(Collidable obj) {
	}

	@Override
	public void collision(Collidable obj) {
        Model.toRemove.add(this);
        if ((obj instanceof Star) & (getClass() == ChargeOrb.class)) { // play a sound if the orb collided with the star
            StarCycle.audio.orbCrash.play(StarCycle.audio.sfxVolume);
        }
	}

    @Override
    public void reset() {
        state.x = 0f;
        state.y = 0f;
        state.v = 0f;
        state.w = 0f;
        state.age = 0;
        body.setActive(false);
    }

    public void init(float x, float y, float v, float w) {
        state.x = x;
        state.y = y;
        state.v = v;
        state.w = w;
        body.setActive(true);
        body.setActive(true);
        body.setTransform(this.state.x, this.state.y, 0f);
        body.setLinearVelocity(this.state.v, this.state.w);
    }

    public void init(OrbState state) {
        init(state.x, state.y, state.v, state.w);
        this.state.age = state.age;
    }

    public void moveOrb(float x, float y) {
        this.state.x += x;
        this.state.y += y;
        body.setTransform(this.state.x, this.state.y, 0f);
    }

    public void removeIfOff() {
        if (state.x < -radius*1.5f || state.x > StarCycle.meterWidth + radius*1.5f || state.y < -radius*1.5f || state.y > StarCycle.meterHeight + radius*1.5f) {
            Model.toRemove.add(this);
        }
    }

}
