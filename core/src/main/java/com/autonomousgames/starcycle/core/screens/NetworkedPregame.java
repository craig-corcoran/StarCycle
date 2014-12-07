package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.ui.*;
import com.autonomousgames.starcycle.core.network.NodeUDPInfo;
import com.autonomousgames.starcycle.core.network.TCPClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

public class NetworkedPregame extends MenuScreen {

    ToggleButton netMode;
    LayeredButton stats;
    LayeredButton search;
    MultiText statusText;
    LayeredButton levelSelect;
    LayeredButton skinSelect;
    StandardButton multiPlayerButton;

    int winNum = 0;
    CharSequence winStr;
    Color winColor;

    public NetworkedPregame(StarCycle.Mode mode, Vector<NodeUDPInfo> nodeVector) {
        Gdx.input.setInputProcessor(new MenuController(this));

        Vector2 iconSize = new Vector2(ui.getHeight()/6f, ui.getHeight()/6f);
        
        StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
        backButton.setRotation(90f);
        backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERMODESELECT));

        Vector2 netTouch = new Vector2(ui.getHeight()/3f, ui.getWidth()/8f);
        Vector2 netSize = new Vector2(1f, 1f).scl(ui.getWidth()/18f);
        Vector2 netVec = new Vector2(ui.getHeight()/12f, ui.getWidth()/36f);
        netMode = new ToggleButton(new Vector2(ui.getWidth()/16f, ui.getHeight()*5f/6f), netTouch);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.block, netTouch).setSpriteAlpha(0.5f), LayerType.DOWN);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.wifi, new Vector2(-netVec.x, netVec.y), new Vector2(netSize.x, netSize.y*3f/4f)).setSpriteColor(Colors.yellow), LayerType.UNTOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.wifi, new Vector2(-netVec.x, netVec.y), new Vector2(netSize.x, netSize.y*3f/4f)).setSpriteColor(Colors.charcoal), LayerType.TOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.globe, new Vector2(netVec.x, netVec.y), netSize).setSpriteColor(Colors.charcoal), LayerType.UNTOGGLED);
        netMode.addLayer(new SpriteLayer(StarCycle.tex.globe, new Vector2(netVec.x, netVec.y), netSize).setSpriteColor(Colors.cyan), LayerType.TOGGLED);
        netMode.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "LOCAL", new Vector2(netVec.y, 0f), Colors.yellow, 90f), LayerType.UNTOGGLED);
        netMode.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "GLOBAL", new Vector2(netVec.y, 0f), Colors.cyan, 90f), LayerType.TOGGLED);
        netMode.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
            if (netMode.isToggled()){
                search.deactivate();
                statusText.switchTo(1);
                multiPlayerButton.remove();

            }
            else {
                search.activate();
                statusText.switchTo(0);
                ui.addActor(multiPlayerButton);
            }
            }
        });
        netMode.setRotation(90f);

        Vector2 statsTouch = new Vector2(netTouch.x, netTouch.y/2f);
        winStr = winNum < 0 ? "- "+-1*winNum : "+ "+winNum;
        winColor = winNum < 0 ? Colors.red :  Colors.spinach;
        stats = new LayeredButton(new Vector2(ui.getWidth()/32f, ui.getHeight()/6f), statsTouch);
        stats.addLayer(new SpriteLayer(StarCycle.tex.block, statsTouch).setSpriteAlpha(0.5f), LayerType.DOWN);
        stats.addLayer(new SpriteLayer(StarCycle.tex.stats, new Vector2(-ui.getHeight() / 16f, 0f), new Vector2(1f, 1f).scl(ui.getWidth() / 20f)).setSpriteColor(Colors.indigo));
        stats.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, winStr, new Vector2(0f, ui.getHeight()/18f), winColor, 90f));
        stats.setRotation(90f);

        statusText = new MultiText(new Vector2(ui.getWidth()/4f, ui.getHeight()/2f), StarCycle.tex.gridnikMedium);
        statusText.addText("Searching for players on local network...");
        statusText.addText("Only local networks supported at this time.");
        statusText.textRotation(90f);
        statusText.switchTo(0);

        search = new LayeredButton(new Vector2(ui.getWidth()*7f/16f, ui.getHeight()/2f));
        search.addLayer(new SpriteLayer(StarCycle.tex.search, new Vector2(0f, ui.getHeight()/10f), new Vector2(1f, 1f).scl(ui.getHeight()/10f)).setRevolutionSpeed(180f), LayerType.ACTIVE);
        search.setRotation(90f);

        ui.addActor(backButton);
        ui.addActor(netMode);
        ui.addActor(stats);
        ui.addActor(statusText);
        ui.addActor(search);

        multiPlayerButton = new StandardButton(new Vector2(ui.getWidth()*3f/4f, ui.getHeight()/2f), new Vector2(iconSize.x*4f/3f, iconSize.y), StarCycle.tex.multiplayerIcon, padding);
        multiPlayerButton.setRotation(90f);
        multiPlayerButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERLEVELSELECT));

        ui.addActor(multiPlayerButton);
        // Assume client for now
        // TODO: no more hardcoding ports
        if (mode == StarCycle.Mode.kClient){
            TCPClient client = new TCPClient(nodeVector.get(0).getIp(), nodeVector.get(0).getPort());
        }
    }

	public String toString(){
		return "NetworkedPregameScreen";
	}
}