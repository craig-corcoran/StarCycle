package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

// TODO make actor group
public class ButtonGroup{
	
	ArrayList<LayeredButton> buttonList = new ArrayList<LayeredButton>();
	ArrayList<InputListener> listenerList = new ArrayList<InputListener>();
	LayeredButton button;
	
	public ButtonGroup() {}
	
	public void addButtonList(ArrayList<LayeredButton> buttonList){
        for (LayeredButton aButtonList : buttonList) {
            this.buttonList.add(aButtonList); // This allows different behavior in extensions via overrides to addButton.
        }
	}
	
	public void addButton(LayeredButton newButton) {
		button = newButton;
		if (listenerList.size() > 0) {
            for (InputListener aListenerList : listenerList) {
                button.addListener(aListenerList);
            }
		}
		buttonList.add(button);
	}
	
	public void addInputListener(InputListener listener){
		listenerList.add(listener);
		if (buttonList.size() > 0) {
            for (LayeredButton aButtonList : buttonList) {
                aButtonList.addListener(listener);
            }
		}
	}
	
	public void addAll(Stage ui) {
        for (LayeredButton aButtonList : buttonList) {
            ui.addActor(aButtonList);
        }
	}
	
	public void removeAll() {
        for (LayeredButton aButtonList : buttonList) {
            aButtonList.remove();
        }
	}
	
	public void setLayerColor(Color tint, int index) {
        for (LayeredButton aButtonList : buttonList) {
            aButtonList.setLayerColor(tint, index);
        }
	}
	
	public void unlockAll() {
        for (LayeredButton aButtonList : buttonList) {
            aButtonList.unlock();
        }
	}
}
