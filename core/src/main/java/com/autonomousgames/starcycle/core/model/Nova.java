package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Nova extends Orb implements Collidable {

    public Nova(Player player, World world) {
        super(player, world, new OrbState(), (int) ModelSettings.getFloatSetting("powerupLifeSpan"));
        StarCycle.audio.launchnukeSound.play(StarCycle.audio.sfxVolume);
    }

    static TextureAtlas.AtlasRegion[] getTextures(Player player) {


        return new TextureAtlas.AtlasRegion[]{
                        StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA0),
                        StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA1)};
    }

	@Override
	public void collision(Collidable obj) {
		if (obj instanceof Star) {
			((Star) obj).addPop(((Star) obj).maxPop * 2f, playerNum);
			StarCycle.audio.landnukeSound.play(StarCycle.audio.sfxVolume);
            //((Star) obj).hitByNova = true;
		}
		else {
			StarCycle.audio.gravkillnukeSound.play(StarCycle.audio.sfxVolume);
		}
		super.collision(obj);
	}
}
