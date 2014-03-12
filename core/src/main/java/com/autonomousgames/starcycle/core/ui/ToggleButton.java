package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ToggleButton extends LayeredButton{
	
	boolean toggled = false;
	public boolean sticksDown = false; // Radio button behavior

	public ToggleButton(Vector2 center, Vector2 touchSize) {
		super(center, touchSize);
		
		this.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				toggle();
				if (sticksDown) {
					active = !toggled && active;
				}
			}
		});
	}
	
	@Override
	boolean layerOn(Layer layer) {
		switch (layer.type) {
		case TOGGLED:
			return toggled;
		case UNTOGGLED:
			return !toggled;
		default:
			return super.layerOn(layer);
		}
	}

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        toggled = !toggled;
    }

    public void toggle(boolean value) {
        toggled = value;
    }
	
}
