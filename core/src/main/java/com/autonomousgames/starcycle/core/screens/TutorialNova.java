//package com.autonomousgames.starcycle.core.screens;
//
//import java.util.ArrayList;
//
//import com.autonomousgames.starcycle.core.StarCycle;
//import com.autonomousgames.starcycle.core.Texturez;
//import com.autonomousgames.starcycle.core.ModelSettings;
//import Base.BaseType;
//import Level.LevelType;
//import Orb.OrbType;
//import Player;
//import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
//import com.autonomousgames.starcycle.core.ui.UIButton;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//
//public class TutorialNova extends Tutorial {
//	
//	public TutorialNova() {
//		super(LevelType.QUAD, ScreenType.TUTORIAL_NOVA, ScreenType.STARTMENU);
//		student.frozen = true;
//		
//		tutorialButtonSpecial = new UIButton(textPosCenter, tutorialButtonSize, StarCycle.padding+5f);
//		tutorialButtonSpecial.addListener(new ClickListener() {
//			public void clicked (InputEvent event, float x, float y) {
//				switch (stringCounter) {
//				case 1://TODO for some reason this clicklistener doesn't work very well. I think the click region is bad.
//					guideFinger.setPosition(student.launchPad.getNovaButtonPos().x - fingerSize.x/2f, student.launchPad.getNovaButtonPos().y - fingerSize.y/2f);
//					guideFinger.setRotation(300f);
//					launchButton.setPosition(student.launchPad.getNovaButtonPos().x - student.launchPad.getOrbButtonSize().x/2f, student.launchPad.getNovaButtonPos().y - student.launchPad.getOrbButtonSize().y/2f);
//					launchButtonListener = new ClickListener() {
//						public void clicked (InputEvent event, float x, float y) {
//							Gdx.app.log("case 2--launchButtonListener", stringCounter + "");
//							tutorialButton.removeLayer();
//							tutorialButton.addLayer(StarCycle.tex.latoLightLarge,StarCycle.tex.cyan, getNextString(stringCounter), textPosCenter, wrapWidth);
//							student.frozen = false;
//							student.base.setPointer(new Vector2(-10f, 0f));
//							orbFactory.launch(OrbType.NOVA, student);
//							guideFinger.remove();
//							tutorialButtonSpecial.remove();
//							launchButton.remove();
//						}};
//					Gdx.app.log("case 2", stringCounter + "");
//					launchButton.addListener(launchButtonListener);
//					ui.addActor(launchButton);
//					ui.addActor(guideFinger);
//					tutorialButton.removeLayer();
//					break;
//				}
//				Gdx.app.log("every_case", stringCounter + "");
//				if (stringCounter != 4 && stringCounter < stringList.size()){
//					tutorialButton.removeLayer();
//					tutorialButton.addLayer(Texturez.latoLightLarge,Texturez.cyan, getNextString(stringCounter), textPosCenter, wrapWidth);
//					stringCounter++;
//				}
//				}
//			}
//		);
//		tutorialButton.active = false;
//		ui.addActor(tutorialButtonSpecial);
//	}
//
//	@Override
//	ArrayList<String> getStringList() {
//		final ArrayList<String> stringList = new ArrayList<String>();
//		stringList.add("The last type of shot is the NOVA");
//		stringList.add("This button launches the nova. Try it!");
//		stringList.add("WOW! It gives instant control but it's expensive. And it doesn't give ammo.");
//		stringList.add("Control all four stars to win.");
//		return stringList;
//	}
//	@Override
//	public void addWinBanner(Player winner) {
//		// TODO Auto-generated method stub
//		super.addWinBanner(winner);
//		winButton.addListener(new ScreenDoneClickListener(this, ScreenType.CAMPAIGNSELECT));
//		winButton.setRotation(90f);
//		ui.addActor(winButton);
//	}
//	@Override
//	void setInitialConditions() {
//		
//		// Player 0 owns two stars; player 1 owns one star.
//		for (int s = 0; s < model.stars.length - 1; s++) {
//			model.stars[s].populations[s%2] = model.stars[s].maxPop;
//		} 
//		
//		Vector2 lowerRight = new Vector2((1 / 2f) * StarCycle.meterWidth,
//				(2 / 3f) * StarCycle.meterHeight);
//		Vector2 upperFarRight = new Vector2((1 / 3f) * StarCycle.meterWidth, (5f / 6f) * StarCycle.meterHeight);
//		Vector2 upperCenter = new Vector2((1 / 3f) * StarCycle.meterWidth, (1f / 2f) * StarCycle.meterHeight);
//		
//		// player 0 starts with immortal harvesters orbiting two stars
//		int numInitOrbs = 16;
//		Vector2 pos = new Vector2();
//		Vector2 offset = new Vector2();
//		float deg = 0f;
//		for (int p = 0; p < 2; p++) {
//			for (int i = 0; i <  numInitOrbs; i++) {
//				pos = p==0 ? lowerRight.cpy() : upperFarRight.cpy();
//				deg = 360f * ((float) i) / numInitOrbs;
//				offset.set(ModelSettings.getSetting("chargeRadius") * MathUtils.cosDeg(deg),
//						ModelSettings.getSetting("chargeRadius") * MathUtils.sinDeg(deg));
//	
//				pos.set(pos.x + 2f * offset.x, 
//						pos.y + 2f * offset.y);
//				offset.rotate(90f);
//				offset.nor();
//				orbFactory.createOrb(OrbType.ORB, student, pos, offset, -1f);
//			}
//		}
//		
//		//player 1 has immortal harvesters orbiting one star
//		for (int i = 0; i <  numInitOrbs; i++) {
//			pos = upperCenter.cpy();
//			deg = 360f * ((float) i) / numInitOrbs;
//			offset.set(ModelSettings.getSetting("chargeRadius") * MathUtils.cosDeg(deg),
//					   ModelSettings.getSetting("chargeRadius") * MathUtils.sinDeg(deg));
//			
//			pos.set(pos.x + 2f * offset.x, 
//					pos.y + 2f * offset.y);
//			offset.rotate(90f);
//			offset.nor();
//			orbFactory.createOrb(OrbType.ORB, players[1], pos, offset, -1f);
//		}
//		
//		// novas are free, orbs are prohibited
//		orbFactory.setCosts(-1f, 0f, 0f);
//	}
//
//	@Override
//	void setPlayers() {
//		numPlayers = 1; // we want the controller to treat it as one player, though we create another for opponent orbs
//		players = new Player[2];
//		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true);
//		players[1] = new Player(1, BaseType.TAKETE, Texturez.warm, this, ui, false);
//	}
//	public String toString(){
//		return "TutorialNova";
//	}
//	
//	@Override
//	public void dispose() {
//		//ModelSettings.setSetting(SinglePlayerLevel.TUTORIAL.toString(), 1f);
//	}
//	
//}
