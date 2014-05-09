package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez.TextureType;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool;
import java.util.LinkedList;

public class Orb implements Collidable, Pool.Poolable {

    static int uidCounter = 0;
    public static boolean useLifeSpan = true;

    @Override
    public int hashCode() { return state.uid; }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) { return true; }
        if( o instanceof Orb ) { return this.state.uid == ((Orb)o).state.uid; }
        return false;
    }

    final int lifeSpan;
    static final LayeredSprite[] orbLayers = new LayeredSprite[Model.numPlayers];
    static final float radius = ModelSettings.getFloatSetting("orbRadius");
    static final float gravScalar = ModelSettings.getFloatSetting("gravScalar");

    final public OrbState state;
    public final Body body; // Physics Box2D body
    final float rotVel;
    float drawAngle = 0f;
    final int playerNum;
    // needed for network mode. Tells us whether this is a predicted orb on the client side
    public boolean predicted;
    public int playerActionMessageNumberWhenCreated;

	public Orb(Player player, World world, OrbState state, int lifeSpan) {

        playerNum = player.number;
        this.state = state;
        this.lifeSpan = lifeSpan;
        setTextures(player);

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

    AtlasRegion[] getTextures(Player player) {
            return new AtlasRegion[]{
                StarCycle.tex.skinMap.get(player.basetype).get(TextureType.ORB0),
                StarCycle.tex.skinMap.get(player.basetype).get(TextureType.ORB1)};
    }

    void setTextures(Player player) {

        Vector2 imageDims = new Vector2(2*radius * StarCycle.pixelsPerMeter, 2*radius * StarCycle.pixelsPerMeter);
        AtlasRegion[] textures = getTextures(player);

        LinkedList<SpriteLayerLite> spriteLayers = new LinkedList<SpriteLayerLite>();
        if (this instanceof ChargeOrb) {
            SpriteLayerLite activeGrad = new SpriteLayerLite(StarCycle.tex.gradientRound, new Vector2(), imageDims.cpy().scl(3f), player.colors[0]);
            activeGrad.activateable = true;
            spriteLayers.add(activeGrad);
        }

        for (int i = 0; i < textures.length; i ++) {
            spriteLayers.add(new SpriteLayerLite(textures[i], new Vector2(), imageDims, player.colors[i]));
        }
        orbLayers[player.number] = new LayeredSprite(spriteLayers);
    }

    public void draw(SpriteBatch batch, LayeredSprite[] layers) {
        drawAngle += rotVel; // This must happen here, because update doesn't get called for charging orbs.
        if (isOnScreen()) {

            boolean active = (this instanceof ChargeOrb) ? ((ChargeOrbState) state).lockedOn : true;
            layers[playerNum].draw(batch, 1f, state.x*StarCycle.pixelsPerMeter, state.y*StarCycle.pixelsPerMeter, drawAngle, active);
        }
    }

	public void draw(SpriteBatch batch) {
        draw(batch, orbLayers);
	}

    public void update(Star[] stars) {
        state.age++;
        if ((state.age > this.lifeSpan) & (this.lifeSpan != -1f) & useLifeSpan) { // remove expired orbs // TODO need -1 w/ bool?
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
            StarState starState = star.state;
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

    boolean isOnScreen() {
        return ((state.x > -radius*2f) & (state.y > -radius*2f)
                & (state.x < StarCycle.meterWidth + radius)
                & (state.y < StarCycle.meterHeight + radius));
    }

    public void removeIfOff() {
        if (!isOnScreen()) {
            Model.toRemove.add(this);
        }
    }

}
