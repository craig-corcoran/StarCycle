package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.FakeOrb;
import com.autonomousgames.starcycle.core.model.ImageOrb;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;

public class Tutorial2 extends TutorialSandbox {
	// This is the aim/shoot tutorial.
	
	private long[] timeouts = new long[]{1200, 1000}; // For switching aim/shoot textures.
	private long[] lastchange = new long[]{0,0};
	private long timeNow;
    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
	private Vector2 vec = new Vector2(-1.5f, 1.5f).div(1.41f); // Velocity vector for FakeOrbs
    private Vector2 fakePos = new Vector2(StarCycle.screenHeight*5f/14f*0.5f, StarCycle.screenHeight*9f/14f*0.5f);
    private long lastOrb = 0;
	
	public Tutorial2() {
		super(LevelType.NOSTARS, ScreenType.TUTORIAL2, ScreenType.TUTORIAL3, ScreenType.TUTORIAL1);

		// Two aim textures and two shoot textures alternate periodically. 
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[1], new Vector2(0f, -StarCycle.screenHeight/4f), new Vector2(StarCycle.screenHeight/2f, StarCycle.screenHeight/2f)));
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[3], new Vector2(0f, StarCycle.screenHeight/4f), new Vector2(StarCycle.screenHeight/2f, StarCycle.screenHeight/2f)));
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[2], new Vector2(0f, -StarCycle.screenHeight/4f), new Vector2(StarCycle.screenHeight/2f, StarCycle.screenHeight/2f)),
				LayerType.INACTIVE); // Layer types are used for texture switching. This will not be an actual button.
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[4], new Vector2(0f, StarCycle.screenHeight/4f), new Vector2(StarCycle.screenHeight/2f, StarCycle.screenHeight/2f)),
				LayerType.LOCKED); // Layer types are used for texture switching. This will not be an actual button.
		infoGraphic.rotateLayers(90f);
		
		nextPage.activate(); // Don't wait for an objective.
		
		// Initialization.
		timeNow = System.currentTimeMillis();
		lastchange[0] = timeNow;
		lastchange[1] = timeNow;
		lastOrb = timeNow;
		
	}

	@Override
	public void update(float delta) {
		// Alternate textures.
		timeNow = System.currentTimeMillis();
		// Switch between aim conditions.
		if (timeNow - lastchange[0] >= timeouts[0]) {
			infoGraphic.switchActive();
			// Change the FakeOrb velocity.
			if (infoGraphic.isActive()) {
				vec.set(-1.5f, 1.5f).div(1.41f);
			}
			else {
				vec.set(-1.5f,0f);
			}
			lastchange[0] = timeNow;
		}
		// Switch between shoot conditions.
		if (timeNow - lastchange[1] >= timeouts[1]) {
			infoGraphic.switchLocked();
//			players[1].launchPad.streamOrbs = !players[1].launchPad.streamOrbs;
			lastchange[1] = timeNow;
		}
		// Shoot orbs when the "button" is being "pressed".
        long orbCooldown = 400;
        if (infoGraphic.isLocked() && timeNow - lastOrb >= orbCooldown) {
            float fakeRadius = 10f;
            fakeOrbs.add(new ImageOrb(Texturez.fakeorbTextures[0], fakeRadius, fakePos, StarCycle.screenWidth,
                    StarCycle.screenHeight, vec, new Vector2(0, 0)));
			lastOrb = timeNow;
		}
		super.update(delta);
	}
	
	@Override
	void setInitialConditions() {
		orbFactory.setCosts(0f,-1f,-1f);
		orbFactory.setLife(15f, 15f);
		// Basic launchpad with no ammo or income.
		players[0].disableIncome();
	}

	@Override
	boolean checkWin() {
		return false; // There is nothing to win.
	}
	
	@Override
	public void dispose() {
		super.dispose();
		fakeOrbs.clear();
	}
	
	@Override
	void setPlayers() {
		numPlayers = 1;
		players = new Player[numPlayers];
		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true, true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		// FakeOrbs must get drawn last to appear over infoGraphic.
		batch.begin();
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();) {
            FakeOrb orb = itr.next();
            orb.draw(batch);
            orb.update(delta);
        }
		batch.end();
	}
	
}
