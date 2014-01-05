package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RadioGroup extends ButtonGroup {
	
	private ToggleButton pressedButton;
	private boolean trueRadio; // All buttons stick down until another button is pressed. This doesn't work for trueRadio = false. Yet.
	
	public RadioGroup() {
		this(true);
	}
	
	public RadioGroup(boolean trueRadio) {
		super();
		this.trueRadio = trueRadio;
		addInputListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {					
				pressedButton = (ToggleButton) event.getTarget();
				untoggleOthers(pressedButton);
			}
		});
	}
	
	@Override
	public void addButton(LayeredButton button) {
		assert (button instanceof ToggleButton) : "RadioGroup accepts only ToggleButton.";
		if (trueRadio) {((ToggleButton) button).sticksDown = true;}
		super.addButton(button);
	}
	
	void untoggleOthers(ToggleButton pressedButton) {
        for (LayeredButton aButtonList : buttonList) {
            button = aButtonList;
            if (!(button == pressedButton)) {
                ((ToggleButton) button).toggled = false;
                button.active = true;
            }
        }
	}
	
}
