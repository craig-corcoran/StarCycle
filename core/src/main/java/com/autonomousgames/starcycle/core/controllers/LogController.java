package com.autonomousgames.starcycle.core.controllers;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.screens.GameScreen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.JsonWriter;

public abstract class LogController implements InputProcessor {

	public LogController(GameScreen screen, int numActivePlayers){
		this(screen);
	}
	public LogController(GameScreen screen){
		StarCycle.json.setOutputType(JsonWriter.OutputType.json);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	//desktop hover
	public boolean mouseMoved(int screenX, int screenY) { 
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
}
