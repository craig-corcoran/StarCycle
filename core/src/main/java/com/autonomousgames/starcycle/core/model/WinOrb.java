package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class WinOrb extends FakeOrb{

    Vector2 dimensions;
    Vector2 imageDims;
    private final LayeredButton orbButton;

	public WinOrb(Player player, Float radius, Vector2 position, float vpWidth, float vpHeight, Vector2 velocity, Vector2 acceleration) {
	    super(radius, position, velocity, acceleration, vpWidth, vpHeight);
        orbButton = new LayeredButton(position.cpy());
        dimensions = new Vector2(radius, radius).scl(2f);
        imageDims = new Vector2(dimensions);
        orbButton.deactivate();
        TextureAtlas.AtlasRegion[] textures = new TextureAtlas.AtlasRegion[]{StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.ORB0), StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.ORB1)};
        for (int i = 0; i < 2; i ++) {
            orbButton.addLayer(new SpriteLayer(textures[i], imageDims).setSpriteColor(player.colors[i]));
        }
	}

	public void draw(SpriteBatch batch) {
        orbButton.draw(batch, 1f);
        orbButton.setCenter(this.position);
	}

    @Override
    public void update(float delta){
        super.update(delta);
    }
}
