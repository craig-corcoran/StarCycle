package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.autonomousgames.starcycle.core.Texturez.TextureType;



public class Void extends ChargeOrb implements Collidable {


    static final Vector2 voidRingDims = new Vector2(ModelSettings.getFloatSetting("voidSensorRadius"), ModelSettings.getFloatSetting("voidSensorRadius")).scl(2f);
    public Void(Player player, World world) {
        super(player, world, Void.class);
        float voidRadius = ModelSettings.getFloatSetting("voidSensorRadius");

        CircleShape sensShape = new CircleShape();
        sensShape.setRadius(voidRadius);
        FixtureDef sensFixtureDef = new FixtureDef();
        sensFixtureDef.shape = sensShape;

        sensFixtureDef.filter.categoryBits = Model.voidCategoryBits[playerNum];
        sensFixtureDef.filter.maskBits = Model.voidMaskBits[playerNum];

        Fixture sensor = body.createFixture(sensFixtureDef);
        sensor.setSensor(true);
        sensor.setUserData(this);

        sensShape.dispose();

    }

    static AtlasRegion[] getTextures(Player player) {
        return new TextureAtlas.AtlasRegion[]{
                        StarCycle.tex.skinMap.get(player.basetype).get(TextureType.VOID0),
                        StarCycle.tex.skinMap.get(player.basetype).get(TextureType.VOID1)};
    }

	@Override
	public void beginSensorContact(Collidable obj) {
		if ((obj instanceof Orb)) {
			if ((obj instanceof Void)) {
                StarCycle.audio.gravkillgravSound.play(StarCycle.audio.sfxVolume);
			}
			obj.collision(this);
		}
        else {
			super.beginSensorContact(obj);
		}
	}
}
