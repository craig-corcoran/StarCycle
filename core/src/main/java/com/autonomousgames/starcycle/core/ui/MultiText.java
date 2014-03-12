package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MultiText extends LayeredButton{

    BitmapFont defaultFont;
    ArrayList<Layer> onLayers = new ArrayList<Layer>();

    public MultiText(Vector2 center) {
        this(center, new Vector2(0f, 0f));
    }

    public MultiText(Vector2 center, Vector2 dims) {
        super(center, dims);
        
    }

    public MultiText(Vector2 center, BitmapFont font) {
        this(center, new Vector2(0f,0f), font);
    }

    public MultiText(Vector2 center, Vector2 dims, BitmapFont font) {
        super(center, dims);
        defaultFont = font;
    }
    
    public TextLayer addText(java.lang.CharSequence text, Vector2 relPos, BitmapFont font) {
        TextLayer layer = new TextLayer(font, text, relPos);
        layers.add(layer);
        return layer;
    }
    
    public TextLayer addText(java.lang.CharSequence text, BitmapFont font) {
        return addText(text, new Vector2(0f, 0f), font);
    }
    
    public TextLayer addText(java.lang.CharSequence text, Vector2 relPos) {
        return addText(text, relPos, defaultFont);
    }
    
    public TextLayer addText(java.lang.CharSequence text) {
        return addText(text, defaultFont);
    }

    public MultiText turnOn(int i) {
        if (!onLayers.contains(layers.get(i))) {
            onLayers.add(layers.get(i));
        }
        return this;
    }

    public MultiText turnOff(int i) {
        if (onLayers.contains(layers.get(i))) {
            onLayers.remove(layers.get(i));
        }
        return this;
    }

    public MultiText allOff() {
        onLayers.clear();
        return this;
    }

    public MultiText switchTo(int i) {
        return allOff().turnOn(i);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        parentAlpha *= getAlpha();
        for (int i = 0; i < onLayers.size(); i ++) {
            onLayers.get(i).draw(batch, parentAlpha, center, getRotation());
        }
    }

    public void textRotation(float angle) {
        for (int i = 0; i < layers.size(); i ++) {
            ((TextLayer) layers.get(i)).rotateText(angle);
        }
    }

}
