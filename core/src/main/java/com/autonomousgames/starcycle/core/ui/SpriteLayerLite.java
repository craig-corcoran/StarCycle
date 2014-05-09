package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ccor on 3/18/14.
 */
public class SpriteLayerLite {

    Vector2 relPos = new Vector2();
    Vector2 size = new Vector2();
    public boolean activateable = false;
    Sprite image;

    public SpriteLayerLite(TextureRegion texture, Vector2 relPos, Vector2 size, Color tint) {
        this.size = size;
        this.relPos = relPos;
        image = new Sprite(texture);
        image.setSize(size.x, size.y);
        image.setOrigin(size.x/2f, size.y/2f);
        image.setColor(tint);
    }

    public void draw(SpriteBatch batch, float parentAlpha, float groupCenterX, float groupCenterY, float groupAngle, boolean active) {
        if ((!activateable) || (active)){ // if this layer doesnt care about activation, or if active, then draw
            image.setPosition(groupCenterX + relPos.x - size.x/2f, groupCenterY + relPos.y - size.y/2f);
            image.setRotation(groupAngle);
            image.draw(batch, parentAlpha);
        }
    }
}
