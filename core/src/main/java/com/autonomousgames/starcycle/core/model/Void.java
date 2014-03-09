package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.autonomousgames.starcycle.core.Texturez.TextureType;



public class Void extends ChargeOrb implements Collidable {


    public Void(Player player, World world) {
        super(player, world, Void.class);
        float voidRadius = ModelSettings.getFloatSetting("voidSensorRadius");
        Sensor.addSensor(this, body, voidRadius);

        Vector2 voidRingDims = new Vector2(ModelSettings.getFloatSetting("voidSensorRadius"), ModelSettings.getFloatSetting("voidSensorRadius")).scl(2f);
        orbButtons[playerNum].addLayer(new SpriteLayer(StarCycle.tex.voidRing, voidRingDims.cpy().scl(StarCycle.pixelsPerMeter)).setSpriteColor(player.colors[0]));
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
