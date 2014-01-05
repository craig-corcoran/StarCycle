package com.autonomousgames.starcycle.core.controllers;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.screens.GameScreen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;

public abstract class LogController implements InputProcessor {
	
	private GameScreen screen;
	
	public LogController(GameScreen screen, int numActivePlayers){
		this(screen);
	}
	public LogController(GameScreen screen){
		this.screen = screen;
		StarCycle.json.setOutputType(JsonWriter.OutputType.json);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		HashMap<String,Object> logMap = new HashMap<String,Object>();
		logMap.put("time", System.currentTimeMillis());
		logMap.put("sessionTime", StarCycle.startTime);
		logMap.put("x", screenX);
		logMap.put("y", screenY);
		logMap.put("type","down");
		logMap.put("screen", screen.toString());
		StarCycle.logHandler.logTouch(StarCycle.json.toJson(logMap));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		HashMap<String,Object> logMap = new HashMap<String,Object>();
		logMap.put("time", System.currentTimeMillis());
		logMap.put("sessionTime", StarCycle.startTime);
		logMap.put("x", screenX);
		logMap.put("y", screenY);
		logMap.put("type","up");
		logMap.put("screen", screen.toString());
		StarCycle.logHandler.logTouch(StarCycle.json.toJson(logMap));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		HashMap<String,Object> logMap = new HashMap<String,Object>();
		logMap.put("time", System.currentTimeMillis());
		logMap.put("sessionTime", StarCycle.startTime);
		logMap.put("x", screenX);
		logMap.put("y", screenY);
		logMap.put("type","drag");
		logMap.put("screen", screen.toString());
		StarCycle.logHandler.logTouch(StarCycle.json.toJson(logMap));
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
