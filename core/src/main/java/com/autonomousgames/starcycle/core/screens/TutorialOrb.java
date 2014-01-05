//package com.autonomousgames.starcycle.core.screens;
//
//import java.util.ArrayList;
//
//import com.autonomousgames.starcycle.core.StarCycle;
//import com.autonomousgames.starcycle.core.Texturez;
//import Base.BaseType;
//import Level.LevelType;
//import Orb.OrbType;
//import Player;
//import com.autonomousgames.starcycle.core.ui.LayeredButton;
//import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
//import com.autonomousgames.starcycle.core.ui.SpriteLayer;
//import com.autonomousgames.starcycle.core.ui.UIButton;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//
//public class TutorialOrb extends Tutorial {
//	
//	public Vector2 handleFingerStartPos;
//	
//	public TutorialOrb(){	
//		super(LevelType.SINGLE, ScreenType.TUTORIAL_ORB, ScreenType.TUTORIAL_VOID);
//		
//		Vector2 pointer = student.base.origin.cpy().sub(student.base.pointer.cpy().nor().scl(student.base.pointer.len()+handleOffset));
//		
//		handleFinger = new LayeredButton(pointer.scl(StarCycle.pixelsPerMeter));
//		handleFinger.addLayer(new SpriteLayer(Texturez.gradientRound, new Vector2(), fingerSize.cpy().scl(2f)));
//		handleFinger.addLayer(new SpriteLayer(Texturez.fingerLeft, new Vector2(), fingerSize));
//		handleFinger.setRotation(pointer.angle() - 90f);
//		
//		Action action = new Action(){
//			public boolean act(float delta) {
//				Vector2 pointer = student.base.pointer.cpy().nor();
//				Vector2 origin = student.base.origin.cpy();
//				origin.sub(pointer.scl(student.base.pointer.len()+handleOffset));
//				handleFinger.setPosition(origin.x * StarCycle.pixelsPerMeter, origin.y * StarCycle.pixelsPerMeter);
//				handleFinger.setRotation(pointer.angle() - 90f);
//				
//				if (stringCounter == 3) {
//					if (handleFingerStartPos.dst(student.base.pointer) >= handleOffset/2f) {
//						tutorialButton.removeLayer();
//						tutorialButton.addLayer(Texturez.latoLightLarge,Texturez.cyan, getNextString(stringCounter), textPosCenter, wrapWidth);
//						stringCounter += 1;
//						guideFinger.setPosition(student.launchPad.corner.x-fingerSize.x*0.6f, student.launchPad.corner.y-fingerSize.x*0.6f);
//						guideFinger.setRotation(120f);
//						handleFinger.remove();
//						ui.addActor(guideFinger);
//						ui.addActor(launchButton);
//					}
//				}
//				return false;
//			}
//		};
//		
//		handleFinger.addAction(action);
//		
//		launchButton.addListener(new ClickListener() {
//			public void clicked (InputEvent event, float x, float y) {
//				tutorialButton.removeLayer();
//				tutorialButton.addLayer(Texturez.moreIcon,
//						new Vector2(textPosCenter.x + ui.getWidth()*4/64f,textPosCenter.y + ui.getHeight()*5/16f),
//						//new Vector2(500f,500f));
//						new Vector2(moreWidth, moreWidth / 4f));
//				tutorialButton.addLayer(Texturez.latoLightLarge, Texturez.cyan,getNextString(stringCounter), textPosCenter, wrapWidth);
//				stringCounter += 1;
//				tutorialButtonSpecial.active = true;
//				student.frozen = false;
//				student.base.setPointer(new Vector2(-10f, 0f));
//				orbFactory.launch(OrbType.ORB, student);
//				guideFinger.remove();
//				launchButton.remove();
//			}
//		});
//		
//		tutorialButtonSpecial = new UIButton(textPosCenter, tutorialButtonSize, StarCycle.padding+5f);
//		tutorialButtonSpecial.addListener(new ClickListener() {
//			public void clicked (InputEvent event, float x, float y) {
//				switch (stringCounter) {
//				case 1:
//					Gdx.app.log("tutorial orb", "base pointer: " + student.base.pointer);
//					guideFinger.setPosition(student.base.origin.cpy().add(student.base.pointer.cpy().rotate(-45f)).scl(StarCycle.pixelsPerMeter));
//					guideFinger.setRotation(225f);
//					ui.addActor(guideFinger);
//					break;
//				case 2:
//					guideFinger.remove();
//					tutorialButton.removeLayer(); // Implicitly removes "more" icon because another layer gets removed later.
//					ui.addActor(handleFinger);
//					handleFingerStartPos = new Vector2 (student.base.pointer.x, student.base.pointer.y);
//					tutorialButtonSpecial.active = false;
//					break;
//				case 6:
//					tutorialButton.removeLayer();
//					ui.addActor(guideFinger);
//					ui.addActor(handleFinger);
//					tutorialButtonSpecial.remove();
//				}
//				if (stringCounter != 3 & stringCounter != 4) {
//					tutorialButton.removeLayer();
//					tutorialButton.addLayer(Texturez.latoLightLarge, Texturez.cyan,getNextString(stringCounter), textPosCenter, wrapWidth);
//					stringCounter += 1;
//				}
//			}});
//		tutorialButton.active= false;
//		ui.addActor(tutorialButtonSpecial);
//		
//	}
//
//	@Override
//	void setPlayers() {
//		numPlayers = 1;
//		players = new Player[numPlayers];
//		players[0] = new Player(0, BaseType.MALUMA, Texturez.cool, this, ui, true);
//	}
//	
//
//	@Override
//	ArrayList<String> getStringList() {
//		final ArrayList<String> stringList = new ArrayList<String>();
//		stringList.add("Welcome to StarCycle! Touch to continue.");
//		stringList.add("In StarCycle, each player has a BASE.");
//		stringList.add("Aim by pulling behind your base. Use your left thumb. Aim to continue.");
//		stringList.add("The launchpad is on your right. Use your right thumb. Try it!");
//		stringList.add("Orbs must orbit. Orbiting gives you ammo and control.");
//		stringList.add("You need ammo to launch more orbs. You need control to win stars.");
//		stringList.add("Win all stars to win the game. Control this star.");
//		return stringList;
//	}
//
//	@Override
//	public void addWinBanner(Player winner) {
//		// TODO Auto-generated method stub
//		super.addWinBanner(winner);
//		winButton.addListener(new ScreenDoneClickListener(this, ScreenType.TUTORIAL_VOID));
//		winButton.setRotation(90f);
//		ui.addActor(winButton);
//	}
//
//	@Override
//	void setInitialConditions() {
//	}
//	
//	public String toString(){
//		return "TutorialOrb";
//	}
//	
//}
