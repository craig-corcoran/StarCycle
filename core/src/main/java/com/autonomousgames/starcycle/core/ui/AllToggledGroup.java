package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AllToggledGroup extends ButtonGroup {

	private ClickListener groupListener = null;

    public AllToggledGroup() {
		super();
		addInputListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				checkOthers(e,x,y);
			}
		});
	}
	
	public AllToggledGroup(ClickListener groupListener) {
		this();
		setGroupListener(groupListener);
	}
	
	public void setGroupListener(ClickListener groupListener) {
		this.groupListener = groupListener;
	}
	
	@Override
	public void addButton(LayeredButton button) {
		assert (button instanceof ToggleButton) : "AllToggledGroup accepts only ToggleButton.";
		super.addButton(button);
	}

	@Override
	public void addAll(Stage ui) {
		assert groupListener != null : "This group does nothing.";
		super.addAll(ui);
	}
	
	boolean checkOthers(InputEvent event, float x, float y) {
        boolean allToggled = true;
        for (LayeredButton aButtonList : buttonList) {
            if (!((ToggleButton) aButtonList).toggled) {
                allToggled = false;
                break; // No use checking the rest once we find an untoggled button.
            }
        }
		if (allToggled) {
			groupListener.clicked(event, x, y);
		}
		return true;
	}
}
