package com.autonomousgames.starcycle.core.controllers;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.screens.MainMenu;
import com.autonomousgames.starcycle.core.screens.MenuScreen;
import com.autonomousgames.starcycle.core.screens.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuController extends LogController {
	private MenuScreen screen;
	Stage ui;
	
	public MenuController(MenuScreen inp_screen) {
		super(inp_screen);
		Gdx.input.setCatchBackKey(true);
		
		screen = inp_screen;
		ui = screen.ui;
	}

	public boolean keyDown(int keycode) {
		super.keyDown(keycode);
		if (keycode == Keys.BACK){
			if (screen instanceof MainMenu){
				StarCycle.sc.dispose();
        	}
			else{
				screen.nextScreen = ScreenType.MAINMENU;
        		screen.isDone = true;
			}
        }
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		ui.touchDown(x, y, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {	
		super.touchDragged(x, y, pointer);
		ui.touchDragged(x, y, pointer);
		screen.touchDragged(x,y,pointer);
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		super.mouseMoved(x, y);
		ui.mouseMoved(x,y);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		super.touchUp(x, y, pointer, button);
		ui.touchUp(x, y, pointer, button);
		return true;
	}

}
