package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.Texturez.TextureType;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.ArrayList;
import java.util.ListIterator;

public class Orb implements Collidable {

	float age = 0f;
	float angle = 0f;
	float lifeSpan; // can be reset using setLifeSpan

	public final OrbType type;
	final Player player;
	final Body body; // Physics 2D body
	float radius;
	Vector2 dimensions;
	final float maxRadius;
	final float rotVel;

	Vector2 position;
	Vector2 force = new Vector2();

	ArrayList<Orb> orbList;
	Star[] starList;
	World world;
	Model model;
	float gravScalar;
	
	AtlasRegion[] textures;
	Vector2 imageDims;
	LayeredButton orbButton;

	public static enum OrbType {
		ORB, VOID, NOVA
	}

	public Orb(Player player, Vector2 position,
			Vector2 velocity, Model model, OrbType type) {
		lifeSpan = UserSettingz.getFloatSetting("lifeSpan");
		this.player = player;
		this.position = position;
		maxRadius = OrbFactory.orbRadii[type.ordinal()];
		radius = maxRadius;
		dimensions = new Vector2(radius, radius).scl(2f);
		imageDims = new Vector2(dimensions).scl(StarCycle.pixelsPerMeter);
		this.model = model;
		this.type = type;

		orbButton = new LayeredButton(position.cpy().scl(StarCycle.pixelsPerMeter));
		
		switch (type) {
		case ORB:
			orbList = player.orbs;
			orbButton.addLayer(new SpriteLayer(Texturez.gradientRound, imageDims.cpy().scl(3f)).setSpriteColor(player.colors[0]), LayerType.ACTIVE);
			orbButton.deactivate();
			textures = new AtlasRegion[]{Texturez.skinMap.get(player.basetype).get(TextureType.ORB0), Texturez.skinMap.get(player.basetype).get(TextureType.ORB1)};
			break;
		case VOID:
			orbList = player.voids;
			Vector2 voidRingDims = new Vector2(UserSettingz.getFloatSetting("gravWellSensorRadius"), UserSettingz.getFloatSetting("gravWellSensorRadius")).scl(2f);
			orbButton.addLayer(new SpriteLayer(Texturez.voidRing, voidRingDims.cpy().scl(StarCycle.pixelsPerMeter)).setSpriteColor(player.colors[0]));
			textures = new AtlasRegion[]{Texturez.skinMap.get(player.basetype).get(TextureType.VOID0), Texturez.skinMap.get(player.basetype).get(TextureType.VOID1)};
			break;
		case NOVA:
			orbList = player.novas;
			textures = new AtlasRegion[]{Texturez.skinMap.get(player.basetype).get(TextureType.NOVA0), Texturez.skinMap.get(player.basetype).get(TextureType.NOVA1)};
			break;
		}
		
		starList = model.stars;
		world = model.world;
		gravScalar = Model.gravityScalar;

		// choose rotation velocity randomly
		int flip = (Math.random() > 0.5) ? 1 : 0;
		rotVel = (float) (Math.random() * 4f + 1) * (2 * flip - 1); 

		BodyDef orbBodyDef = new BodyDef();
		orbBodyDef.type = BodyType.DynamicBody;
		orbBodyDef.position.set(position);
		orbBodyDef.linearVelocity.set(velocity);
		body = world.createBody(orbBodyDef);

		CircleShape orbShape = new CircleShape(); // TODO make final, static?
		orbShape.setRadius(OrbFactory.orbRadii[type.ordinal()]);
		FixtureDef orbFixtureDef = new FixtureDef();
		orbFixtureDef.shape = orbShape;
		
		orbFixtureDef.filter.categoryBits = Model.orbCategoryBits[this.player.number];
		orbFixtureDef.filter.maskBits = Model.orbMaskBits[this.player.number];
		body.createFixture(orbFixtureDef);
		body.setUserData(this); // add a pointer back to this object in the Body
								// (box2d returns bodies that collide)
		orbShape.dispose();
		
		for (int i = 0; i < 2; i ++) {
			orbButton.addLayer(new SpriteLayer(textures[i], imageDims).setSpriteColor(player.colors[i]));
		}
	}
	
	public void draw(SpriteBatch batch) {
		// Only draw when visible
		if ((position.x > -radius*2f) & (position.y > -radius*2f)
				& (position.x < StarCycle.meterWidth + radius)
				& (position.y < StarCycle.meterHeight + radius)) {
			orbButton.draw(batch, 1f);
		}
	}

    public void update(float delta, Vector2[] starPositions, ListIterator<Orb> itr) {
		age += delta;

		if ((age > lifeSpan) & (lifeSpan != -1f)) { // remove expired orbs
			removeSelf(itr);
			world.destroyBody(body);
		} else {
			angle += rotVel;
			position = body.getPosition();
			force = getGravForce(starPositions);
            Vector2 vel = body.getLinearVelocity();
			force.scl(delta);
			body.setLinearVelocity(vel.x + force.x, vel.y + force.y);
			orbButton.setCenter(position.cpy().scl(StarCycle.pixelsPerMeter));
			orbButton.setRotation(angle);
			// body.applyForceToCenter(force); // .scl(body.getMass()));
		}
		// Fade out over the last second. This assumes 60 fps and delta = 1/30f.
//		if (lifeSpan < age + 2f && lifeSpan != -1f) {
//			radius = maxRadius * (lifeSpan-age) / 2f;
//			
//		}
	}

    public Vector2 getGravForce(Vector2[] starPositions) {
		force.x = 0;
		force.y = 0;

		for (int i = 0; i < starPositions.length; i++) {

            Vector2 starpos = starPositions[i];
            Star star = starList[i];
			// because we assume the orbs dont affect stars, we can treat all orbs as having mass 1.
			// be careful using the vector2 methods as they change vectors in place
            float dist = position.dst(starpos);
            float scal = star.mass * gravScalar / (dist * dist * dist);
			force.add(scal * (starpos.x - position.x), 
					  scal * (starpos.y - position.y));
		}

		return force;
	}

	public void setLifeSpan(float lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	@Override
	public void beginSensorContact(Collidable obj) {
	}

	@Override
	public void endSensorContact(Collidable obj) {
	}

	@Override
	public void collision(Collidable obj) {
        if (!(model.toDestroyList.contains(this))) {
            model.toDestroyList.add(this);
            removeSelf();
        } else {
            Gdx.app.log("ORB collision", " ORB added to destroy list twice");
        }
        removeSelf();
	}

    public void removeSelf() {
        orbList.remove(this);
    }

    public void removeSelf(ListIterator<Orb> itr) {
        itr.remove();
	}
}
