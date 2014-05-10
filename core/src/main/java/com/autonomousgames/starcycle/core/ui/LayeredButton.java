package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class LayeredButton extends Group {
    public boolean pressedDown = false; // Is the button being touched right now?
    boolean active = true; // Is the button able to be pressed?
    boolean locked = false;

    Vector2 touchSize;
    Vector2 center = new Vector2(); // position of the center of the button/textures

    ArrayList<Layer> layers = new ArrayList<Layer>();

    float alpha = 1f;

    public LayeredButton(Vector2 center, Vector2 touchSize) {
        this.touchSize = touchSize;
        this.setCenter(center.x, center.y);
        this.setSize(touchSize.x, touchSize.y);
        this.setOrigin(touchSize.x / 2f, touchSize.y / 2f);

        this.addListener(new ClickListener(){
            int pointerNum;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressedDown = true;
                pointerNum = pointer;
                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointer == pointerNum) {
                    pressedDown = touchOn(x, y);
                }
                super.touchDragged(event, x, y, pointer);
            }

            public void clicked(InputEvent event, float x, float y) {
                pressedDown = false;
            }

        });
    }

    // Constructor without touchSize (probably a non-interactive UI element.
    public LayeredButton(Vector2 center) {
        this(center, new Vector2());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        parentAlpha *= getAlpha();
        for (int i = 0; i < layers.size(); i++) {
            // Check to see if each layer should be drawn (i.e. pressed layers should only be drawn on touchDown)
            if (layerOn(layers.get(i))) {
                // Pass the position and angle of the button to each layer for drawing.
                layers.get(i).draw(batch, parentAlpha, center, getRotation());
            }
        }
    }

    boolean layerOn(Layer layer) {
        switch (layer.type) {
            case UP: // Not pressed
                return !pressedDown;
            case DOWN: // Pressed or dragged
                return pressedDown;
            case ACTIVE: // Able to be pressed
                return active;
            case ACTIVEUP: // Active but not pressed
                return active && !pressedDown;
            case INACTIVE: // Currently unavailable
                return !active;
            case LOCKED: // Potentially able to be pressed, but blocked by something
                return locked;
            case UNLOCKED: // Not being blocked
                return !locked;
            case FREE: // Active and unlocked
                return active && !locked;
            case SPECIAL: // Specified by the layer
                return layer.drawCondition();
            default:
                return true;
        }
    }

    // setCenter is probably the preferred method.

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.center.set(x + touchSize.x/2f, y + touchSize.y/2f);
    }

    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    public void setCenter(float x, float y) {
        super.setPosition(x - touchSize.x/2f, y - touchSize.y/2f);
        this.center.set(x , y);
    }

    public void setCenter(Vector2 pos) {
        setCenter(pos.x, pos.y);
    }

    // Add a layer that will always be drawn.
    public LayeredButton addLayer(Layer layer) {
        return this.addLayer(layer, LayerType.ALWAYS);
    }

    // Add a layer with a certain draw condition.
    public LayeredButton addLayer(Layer layer, LayerType type) {
        layers.add(layer);
        layer.type = type;
        return this;
    }

    // Actions that apply to only the indicated layer:
    public void setLayerCenter(float x, float y, int index) {
        layers.get(index).setCenter(x, y);
    }

    public void setLayerPosition(float x, float y, int index) {
        layers.get(index).setPosition(x, y);
    }

    public void addLayerAction(Action action, int index) {
        layers.get(index).addAction(action);
    }

    public LayeredButton setLayerColor(Color tint, int index) {
        layers.get(index).setLayerColor(tint);
        return this;
    }

    // Actions that apply to all layers:
    @Override
    public void setColor (Color tint) {
        assert layers.size() == 1 : "Use setLayerColor(Color tint, int i) or setAllColors(Color tint).";
        layers.get(0).setLayerColor(tint);
    }

    public void setAlpha (float a) {
        alpha = a;
    }

    public void setAllColors(Color tint) {
        for (Layer layer : layers) {
            layer.setColor(tint);
        }
    }

    public void moveCenter (float dx, float dy) {
        setCenter(getCenter().x + dx, getCenter().y + dy);
    }

    public void moveCenter (Vector2 dPos) {
        moveCenter(dPos.x, dPos.y);
    }

    // Rotate all layers around their own center, not around the button center.
    public LayeredButton rotateLayers(float angle){
        for (Layer layer : layers) {
            layer.rotate(angle);
        }
        return this;
    }

    @Override
    public Color getColor() {
        assert false : "Use getLayerColor(int i) or getAllColors().";
        return null; // Layers have color, not LayeredButton
    }

    // The button's center position
    public Vector2 getCenter() {
        return center.cpy();
    }

    //The button's touchSize
    public Vector2 getDims() {
        return touchSize.cpy();
    }

    // The button's alpha:
    public float getAlpha() {
        return alpha;
    }

    public int getLayerNum() {
        return layers.size();
    }

    public Layer getLayer(int i) {
        return layers.get(i);
    }

    public Layer getLast() {
        return layers.get(layers.size()-1);
    }

    public Color getLayerColor(int index) {
        return layers.get(index).getColor();
    }

    public Color[] getAllColors() {
        Color[] colors = new Color[layers.size()];
        for (int i = 0; i < layers.size(); i ++) {
            colors[i] = getLayerColor(i);
        }
        return colors;
    }

    public void removeLayer(int i) {
        layers.remove(i);
    }

    public void flip(boolean x, boolean y) {
        for (Layer layer : layers) {
            layer.flip(x, y);
        }
    }

    @Override
    public void act(float delta) {
        for (Layer layer : layers) {
            layer.act(delta);
        }
        super.act(delta);
    }

    public boolean touchOn(float x, float y) {
        return (0 <= x && x <= touchSize.x && 0 <= y && y <= touchSize.y);
    }

    public boolean isDown() {
        return pressedDown;
    }

    // Enable the button.
    public void activate() {
        active  = true;
    }

    // Disable the button.
    public void deactivate() {
        pressedDown = false;
        active = false;
        cancel();
    }

    public boolean isActive() {
        return active;
    }

    // Prevent the button from being pressed, even if it's active
    public void lock() {
        pressedDown = false;
        locked = true;
        cancel();
    }

    // Allow the button to be pressed, as long as it's active
    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean fire (Event event) {
        return (!(active & !locked)) || super.fire(event);
    }

    private void cancel() {
        Array<EventListener> eventListeners = getListeners();
        for (int i = 0; i < eventListeners.size; i ++) {
            if (eventListeners.get(i) instanceof ClickListener) {
                ((ClickListener) eventListeners.get(i)).cancel();
            }
        }
    }

    public void switchActive() {
        if (active) {
            deactivate();
        }
        else {
            activate();
        }
    }

    public void switchLocked() {
        if (locked) {
            unlock();
        }
        else {
            lock();
        }
    }

}
