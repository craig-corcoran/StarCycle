//package com.autonomousgames.starcycle.core.screens;
//
//import java.util.ArrayList;
//
//import com.autonomousgames.starcycle.core.StarCycle;
//import com.autonomousgames.starcycle.core.Texturez;
//import com.autonomousgames.starcycle.core.UserSettingz;
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
//public class TutorialVoid extends Tutorial {
//	
//	// TODO single player controller, visible opponent?
//	public TutorialVoid() {
//		super(LevelType.DOUBLE, ScreenType.TUTORIAL_VOID, ScreenType.TUTORIAL_NOVA);
//		
//		tutorialButtonSpecial = new UIButton(textPosCenter, tutorialButtonSize, StarCycle.padding+5f);
//		tutorialButtonSpecial.addListener(new ClickListener() {
//			public void clicked (InputEvent event, float x, float y) {
//				switch (stringCounter) {
//				case 1:
//					orbFactory.createOrb(OrbType.VOID, players[1], new Vector2(StarCycle.meterWidth/2f + 1f, 0f), new Vector2(0f, 1f), -1f);
//					Gdx.app.log("case 1", stringCounter + "");
//					break;
//				case 2:
//					guideFinger.setPosition(student.launchPad.getVoidButtonPos().x - fingerSize.x/2f, student.launchPad.getVoidButtonPos().y - fingerSize.y/2f);
//					guideFinger.setRotation(-30f);
//					launchButton.setPosition(student.launchPad.getVoidButtonPos().x - student.launchPad.getOrbButtonSize().x/2f, student.launchPad.getVoidButtonPos().y - student.launchPad.getOrbButtonSize().y/2f);
//					launchButtonListener = new ClickListener() {
//						public void clicked (InputEvent event, float x, float y) {
//							Gdx.app.log("case 2--launchButtonListener", stringCounter + "");
//							tutorialButton.removeLayer();
//							tutorialButton.addLayer(Texturez.latoLightLarge,Texturez.cyan, getNextString(stringCounter), textPosCenter, wrapWidth);
//							student.frozen = false;
//							student.base.setPointer(new Vector2(-10f, -2f));
//							orbFactory.launch(OrbType.VOID, student);
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
//				if (stringCounter < stringList.size() & stringCounter !=4) {
//					tutorialButton.removeLayer();
//					tutorialButton.addLayer(Texturez.latoLightLarge,Texturez.cyan, getNextString(stringCounter), textPosCenter, wrapWidth);
//					stringCounter += 1;
//				}
//			}});
//		tutorialButton.active= false;
//		ui.addActor(tutorialButtonSpecial);
//	}
//
//	@Override
//	void setPlayers() {
//		numPlayers = 1; // we want the controller to treat it as one player, though we create another for opponent orbs
//		players = new Player[2];
//		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true);
//		players[1] = new Player(1, BaseType.TAKETE, Texturez.warm, this, ui, false);
//		players[0].ammo += 180;
//	}
//
//	@Override
//	ArrayList<String> getStringList() {
//		final ArrayList<String> stringList = new ArrayList<String>();
//		stringList.add("You can fire three types of shots.");
//		stringList.add("The VOID is the most powerful.");
//		stringList.add("This button launches voids. Launch a void to continue.");
//		stringList.add("Voids do not give you control. Only orbs give you control. Control the yellow star.");
//		return stringList;
//	}
//	
//	@Override
//	public void addWinBanner(Player winner) {
//		// TODO Auto-generated method stub
//		super.addWinBanner(winner);
//		winButton.addListener(new ScreenDoneClickListener(this, ScreenType.TUTORIAL_NOVA));
//		winButton.setRotation(90f);
//		ui.addActor(winButton);
//	}
//	
//	@Override
//	void setInitialConditions() {
//		
//		for (int p = 0; p < players.length; p++) {
//			model.stars[p].populations[p] = model.stars[p].maxPop;
//		}
//		
//		// both players start with immortal harvesters orbiting one star
//		Vector2 centerRight = new Vector2((1 / 2f) * StarCycle.meterWidth,
//				(1 / 3f) * StarCycle.meterHeight);
//		Vector2 centerLeft = new Vector2((1 / 2f) * StarCycle.meterWidth,
//				(2 / 3f) * StarCycle.meterHeight);
//		int numInitOrbs = 16;
//		Vector2 pos = new Vector2();
//		Vector2 offset = new Vector2();
//		float deg = 0f;
//		for (int p = 0; p < players.length; p++) {
//			for (int i = 0; i <  numInitOrbs; i++) {
//				pos = (p == 0) ? centerRight.cpy() : centerLeft.cpy();
//				deg = 360f * ((float) i) / numInitOrbs;
//				offset.set(UserSettingz.getSetting("chargeRadius") * MathUtils.cosDeg(deg), 
//						   UserSettingz.getSetting("chargeRadius") * MathUtils.sinDeg(deg));
//				
//				pos.set(pos.x + 2f * offset.x, 
//						pos.y + 2f * offset.y);
//				offset.rotate(90f);
//				offset.nor();
//				orbFactory.createOrb(OrbType.ORB, players[p], pos, offset, -1f);
//			}
//		}
//		
//	// and the opponent has immortal voids!
//		int numInitVoids = 2;
//		for (int i = 0; i <  numInitVoids; i++) {
//			deg = 360f * ((float) i) / numInitVoids;
//			offset.set(UserSettingz.getSetting("chargeRadius") * MathUtils.cosDeg(deg), 
//					   UserSettingz.getSetting("chargeRadius") * MathUtils.sinDeg(deg));
//			
//			pos.set(centerLeft.x + 2f * offset.x, 
//					centerLeft.y + 2f * offset.y);
//			offset.rotate(90f);
//			offset.nor();
//			orbFactory.createOrb(OrbType.VOID, players[1], pos, offset, -1f);
//		}
//	}
//	
//	public String toString(){
//		return "TutorialVoid";
//	}
//}
