package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.math.Vector2;

public class Tutorial1 extends TutorialGraphic {
	// "How to hold the tablet" tutorial screen.
	
	
	
	public Tutorial1() {
		super(ScreenType.TUTORIAL2, ScreenType.STARTMENU);
		infoGraphic.addLayer(new SpriteLayer(Texturez.tutorialImages[0], 
				new Vector2(StarCycle.screenHeight, StarCycle.screenHeight)).rotateSprite(90f));
		
	}
	


}
