package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.ui.LayeredSprite;
import com.autonomousgames.starcycle.core.ui.SpriteLayerLite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;

public class Nova extends Orb implements Collidable {

    static final LayeredSprite[] novaLayers = new LayeredSprite[Model.numPlayers];
    static final float radius = ModelSettings.getFloatSetting("novaRadius");

    public Nova(Player player, World world) {
        super(player, world, new OrbState(), (int) ModelSettings.getFloatSetting("powerupLifeSpan"));
        StarCycle.audio.launchnukeSound.play(StarCycle.audio.sfxVolume);
    }

    @Override
    TextureAtlas.AtlasRegion[] getTextures(Player player) {
        return new TextureAtlas.AtlasRegion[]{
                StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA0),
                StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA1)};
    }

    @Override
    void setTextures(Player player) {
        Vector2 imageDims = new Vector2(2*radius * StarCycle.pixelsPerMeter, 2*radius * StarCycle.pixelsPerMeter);
        TextureAtlas.AtlasRegion[] textures = getTextures(player);

        LinkedList<SpriteLayerLite> spriteLayers = new LinkedList<SpriteLayerLite>();
        for (int i = 0; i < textures.length; i ++) {
            spriteLayers.add(new SpriteLayerLite(textures[i], new Vector2(), imageDims, player.colors[i]));
        }
        novaLayers[player.number] = new LayeredSprite(spriteLayers);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch, novaLayers);
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
