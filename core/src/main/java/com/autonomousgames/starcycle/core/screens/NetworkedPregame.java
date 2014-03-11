package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class NetworkedPregame extends MenuScreen {

    ToggleButton netMode;
    LayeredButton stats;
    ToggleButton searchBar;
    LayeredButton options;

	public NetworkedPregame() {
		Gdx.input.setInputProcessor(new MenuController(this));

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERMODESELECT));

        Vector2 netPos = new Vector2(ui.getWidth()/16f, ui.getHeight()*5f/6f);
        Vector2 netTouch = new Vector2(ui.getHeight()/3f, ui.getWidth()/8f);
        Vector2 netSize = new Vector2(1f, 1f).scl(ui.getWidth()/18f);
        Vector2 netVec = new Vector2(ui.getHeight()/12f, ui.getWidth()/36f);
        netMode = new ToggleButton(netPos, netTouch);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.block, netTouch).setSpriteAlpha(0.5f), LayerType.DOWN);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.wifi, new Vector2(-netVec.x, netVec.y), new Vector2(netSize.x, netSize.y*3f/4f)).setSpriteColor(Colors.yellow), LayerType.UNTOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.wifi, new Vector2(-netVec.x, netVec.y), new Vector2(netSize.x, netSize.y*3f/4f)).setSpriteColor(Colors.charcoal), LayerType.TOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.globe, new Vector2(netVec.x, netVec.y), netSize).setSpriteColor(Colors.charcoal), LayerType.UNTOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.globe, new Vector2(netVec.x, netVec.y), netSize).setSpriteColor(Colors.cyan), LayerType.TOGGLED);
        netMode.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "LOCAL", new Vector2(netVec.y, 0f), netSize, Colors.yellow, 90f), LayerType.UNTOGGLED);
        netMode.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "GLOBAL", new Vector2(netVec.y, 0f), netSize, Colors.cyan, 90f), LayerType.TOGGLED);
        netMode.setRotation(90f);

		ui.addActor(backButton);
        ui.addActor(netMode);
	}
	
	public String toString(){
		return "AboutScreen";
	}
}