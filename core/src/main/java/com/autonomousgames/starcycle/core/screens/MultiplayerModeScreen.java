package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.math.Vector2;

public class MultiplayerModeScreen extends MenuScreen {

    Vector2 touchSize = new Vector2(ui.getHeight()/2f, ui.getHeight()/5f);
    Vector2 iconSize = new Vector2(ui.getHeight()/6f, ui.getHeight()/6f);

	public MultiplayerModeScreen() {
		float padding = StarCycle.pixelsPerMeter/2f; // padding around text-only buttons

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.STARTMENU));

        LayeredButton network = new LayeredButton(new Vector2(ui.getWidth()/3f, ui.getHeight()/2f), touchSize);
//        network.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, touchSize), LayerType.DOWN);
        network.addLayer(new SpriteLayer(StarCycle.tex.globe, new Vector2(ui.getHeight()*0.125f, 0f), iconSize.cpy().scl(0.8f)).setSpriteColor(Colors.smoke));
        network.addLayer(new SpriteLayer(StarCycle.tex.soloIcon, new Vector2(-ui.getHeight()*0.125f, 0f), iconSize).setSpriteColor(Colors.smoke));
        network.addLayer(new SpriteLayer(StarCycle.tex.network, iconSize.cpy().scl(0.4f)).setSpriteColor(Colors.smoke));
        network.setRotation(90f);
//        network.addListener(new ScreenDoneClickListener(this, ScreenType.NETWORKEDPREGAME));

        LayeredButton local = new LayeredButton(new Vector2(ui.getWidth()*2f/3f, ui.getHeight()/2f), touchSize);
        local.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, touchSize), LayerType.DOWN);
        local.addLayer(new SpriteLayer(StarCycle.tex.soloIcon, new Vector2(ui.getHeight()*0.125f, 0f), iconSize));
        local.addLayer(new SpriteLayer(StarCycle.tex.soloIcon, new Vector2(-ui.getHeight()*0.125f, 0f), iconSize));
        local.addLayer(new SpriteLayer(StarCycle.tex.tablet, new Vector2(iconSize.x*3f/4f, iconSize.y).scl(0.5f)).rotateSprite(-90f).setSpriteColor(Colors.smoke));
        local.setRotation(90f);
        local.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERLEVELSELECT));

		ui.addActor(backButton);
        ui.addActor(network);
        ui.addActor(local);
	}

	public String toString(){
		return "MultiplayerModeScreen";
	}
}