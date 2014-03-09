package com.autonomousgames.starcycle.core.controllers;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.autonomousgames.starcycle.core.screens.MultiPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameController extends LogController{

	private float controlRadius = 1.1f* Base.maxPointerLength;
	private float sqrdControlRadius = controlRadius*controlRadius;
	private ModelScreen screen;
	
	private int[] activePointer;
	private Vector2[] origins;
	private Vector2 vec = new Vector2();
	private int numActivePlayers;
	
	public GameController(ModelScreen screen, int numActivePlayers){
		super(screen, numActivePlayers);
		Gdx.input.setCatchBackKey(true);
		
		this.screen = screen;
		this.numActivePlayers = numActivePlayers;
		origins = new Vector2[this.numActivePlayers];
		activePointer = new int[this.numActivePlayers];
		
		// creating a region for displaying the control region
		// TODO is the world sensor region needed if not using debug renderer?
		final CircleShape starShape = new CircleShape();
		starShape.setRadius(controlRadius);
		
		for (int i=0; i < this.numActivePlayers; i++) {
			origins[i] = screen.model.players[i].base.origin; // keep
			activePointer[i] = -1;
			
			if (screen instanceof MultiPlayer) {
				if (((MultiPlayer)screen).dbRender) {
			
					// TODO remove
					final BodyDef starBodyDef = new BodyDef();
					starBodyDef.type = BodyType.StaticBody;
					starBodyDef.position.set(origins[i]);
					Body body = screen.model.world.createBody(starBodyDef);
					FixtureDef starFixtureDef = new FixtureDef();
					starFixtureDef.shape = starShape;
					starFixtureDef.isSensor = true;
					body.createFixture(starFixtureDef);
				}
			}
		}	
		starShape.dispose(); // remove to here		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		super.keyDown(keycode);
        if (keycode == Keys.Q) {
        	screen.model.players[0].state.buttonStates[0] = true;
        }
        if (keycode == Keys.W) {
            screen.model.launch(screen.model.players[0], com.autonomousgames.starcycle.core.model.Void.class);
        }
        if (keycode == Keys.E) {
            screen.model.launch(screen.model.players[0], Nova.class);
        }
        if (keycode == Keys.BACK){
        	screen.addPauseBanner();
        	return true;
        }
        return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.Q) {
        	screen.model.players[0].state.buttonStates[0] = false;
        }
		        return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		super.touchDown(x, y, pointer, button);
		
		for (int i=0; i < this.numActivePlayers; i++){
			if (activePointer[i] == -1){ // if there is currently no active pointer
				vec.set((float)x/StarCycle.pixelsPerMeter,(float)y/StarCycle.pixelsPerMeter);
				vec.sub(origins[i].x, StarCycle.meterHeight-origins[i].y);
				if (vec.len2() < sqrdControlRadius){
					activePointer[i] = pointer;
					vec.x = -vec.x; // y is already negative bc of inverted screen axis
					screen.model.players[i].base.setPointer(vec);
					return true;
				}
			}
		}

		screen.ui.touchDown(x,y,pointer,button);
		
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		super.touchDragged(x, y, pointer);
		for (int i=0; i < this.numActivePlayers; i++){
			vec.set((float)x/StarCycle.pixelsPerMeter,(float)y/StarCycle.pixelsPerMeter);
			vec.sub(origins[i].x, StarCycle.meterHeight-origins[i].y);
			if ((activePointer[i] == pointer) | ((activePointer[i] == -1) & (vec.len2() < sqrdControlRadius))){
				activePointer[i] = pointer;
				vec.x = -vec.x; // y is already negative bc of inverted screen axis
				screen.model.players[i].base.setPointer(vec);
				return true;
			}
		}
		screen.ui.touchDragged(x,y,pointer);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		super.touchUp(x, y, pointer, button);
		for (int i=0; i < this.numActivePlayers; i++){
			if (activePointer[i] == pointer){
				activePointer[i] = -1;
				return true; // this line needed?
			}
		}
		screen.ui.touchUp(x,y,pointer,button);
		return true;
	}

}
