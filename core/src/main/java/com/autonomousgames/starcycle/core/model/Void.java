package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.LayeredSprite;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.autonomousgames.starcycle.core.ui.SpriteLayerLite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.autonomousgames.starcycle.core.Texturez.TextureType;

import java.util.LinkedList;


public class Void extends ChargeOrb implements Collidable {


    static final LayeredSprite[] voidLayers = new LayeredSprite[Model.numPlayers];
    static final float radius = ModelSettings.getFloatSetting("voidRadius");

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

    public void draw(SpriteBatch batch) {
        super.draw(batch, voidLayers);
    }

    @Override
    void setTextures(Player player) {
        Vector2 imageDims = new Vector2(2*radius * StarCycle.pixelsPerMeter, 2*radius * StarCycle.pixelsPerMeter);
        AtlasRegion[] textures = getTextures(player);

        LinkedList<SpriteLayerLite> spriteLayers = new LinkedList<SpriteLayerLite>();
        spriteLayers.add(new SpriteLayerLite(StarCycle.tex.voidRing, new Vector2(), voidRingDims.cpy().scl(StarCycle.pixelsPerMeter), player.colors[0]));
        SpriteLayerLite activeGrad = new SpriteLayerLite(StarCycle.tex.gradientRound, new Vector2(), imageDims.cpy().scl(3f), player.colors[0]);
        activeGrad.activateable = true;
        spriteLayers.add(activeGrad);

        for (int i = 0; i < textures.length; i ++) {
            spriteLayers.add(new SpriteLayerLite(textures[i], new Vector2(), imageDims, player.colors[i]));
        }
        voidLayers[player.number] = new LayeredSprite(spriteLayers);
    }

    @Override
    AtlasRegion[] getTextures(Player player) {
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

    @Override
    boolean isOnScreen() {
        return ((state.x > -voidRingDims.x*0.75f) & (state.y > -voidRingDims.y*0.75f)
                & (state.x < StarCycle.meterWidth + radius*0.75f)
                & (state.y < StarCycle.meterHeight + radius*0.75f));
    }
}
