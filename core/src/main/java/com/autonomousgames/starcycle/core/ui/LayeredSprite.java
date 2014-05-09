package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ccor on 3/18/14.
 */
public class LayeredSprite {

    public final LinkedList<SpriteLayerLite> layers;

    public LayeredSprite(LinkedList<SpriteLayerLite> layers) {
        this.layers = layers;
    }

    public void draw(SpriteBatch batch, float alpha, float centerX, float centerY, float angle, boolean active) {
        for (SpriteLayerLite layer: layers) {
            layer.draw(batch, alpha, centerX, centerY, angle, active);
        }
    }
}
