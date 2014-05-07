package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.network.NodeUDPInfo;
import com.autonomousgames.starcycle.core.network.TCPClient;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.autonomousgames.starcycle.core.ui.StandardButton;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

public class NetworkedPregame extends MenuScreen {

	public NetworkedPregame(StarCycle.Mode mode, Vector<NodeUDPInfo> nodeVector) {
		Gdx.input.setInputProcessor(new MenuController(this));

        Vector2 iconSize = new Vector2(ui.getHeight()/6f, ui.getHeight()/6f);

		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERMODESELECT));

		Vector2 kubatkoVector = new Vector2((ui.getWidth()*4f)/8f, ui.getHeight()*1f/2f);
		LayeredButton lookingForGameText = new LayeredButton(kubatkoVector);
		lookingForGameText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge, "Looking for game...", kubatkoVector).rotateText(90f));

        StandardButton singlePlayerButton = new StandardButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/2f), iconSize, StarCycle.tex.soloIcon, padding);
        singlePlayerButton.setRotation(90f);
        singlePlayerButton.addListener(new ScreenDoneClickListener(this, ScreenType.CAMPAIGNSELECT));

        StandardButton multiPlayerButton = new StandardButton(new Vector2(ui.getWidth()/2f, ui.getHeight()/2f), new Vector2(iconSize.x*4f/3f, iconSize.y), StarCycle.tex.multiplayerIcon, padding);
        multiPlayerButton.setRotation(90f);
        multiPlayerButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERLEVELSELECT));

		ui.addActor(backButton);
        ui.addActor(multiPlayerButton);
        ui.addActor(singlePlayerButton);
		ui.addActor(lookingForGameText);
        // Assume client for now
        // TODO: no more hardcoding ports
        if (mode == StarCycle.Mode.kClient){
            TCPClient client = new TCPClient(nodeVector.get(0).getIp(), nodeVector.get(0).getPort());
            Vector2 lookingVector = new Vector2((ui.getWidth()*4f)/6f, ui.getHeight()*1f/2f );
            LayeredButton serverText = new LayeredButton(lookingVector);
            serverText.addLayer(new TextLayer(StarCycle.tex.latoLightLarge, "Connected to server on port " + client.getPort(), lookingVector).rotateText(90f));
            ui.addActor(serverText);
        }
    }
	
	public String toString(){
		return "NetworkedPregameScreen";
	}
}