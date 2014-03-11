package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerSelect extends LevelSelectScreen{
	
	public BaseType skin0;
	public BaseType skin1;
	public Color[] colors0;
	public Color[] colors1;
	
	ToggleButton ready0;
	ToggleButton ready1;
	AllToggledGroup readyGroup = new AllToggledGroup();
	
	ToggleButton trefoilButton;
	ToggleButton doublebinaryButton;
	ToggleButton concentricButton;
	ToggleButton venndiagramButton;
	RadioGroup levelRadio = new RadioGroup();
	
	ToggleButton maluma0;
	ToggleButton takete0;
	ToggleButton derelict0;
	RadioGroup skinRadio0 = new RadioGroup();
	
	ToggleButton cool0;
	ToggleButton warm0;
	ToggleButton leafy0;
	ToggleButton floral0;
	RadioGroup colorRadio0 = new RadioGroup();
	
	ToggleButton skinSelect0;
	ToggleButton colorSelect0;
	RadioGroup selectRadio0 = new RadioGroup(false);

	ToggleButton maluma1;
	ToggleButton takete1;
	ToggleButton derelict1;
	RadioGroup skinRadio1 = new RadioGroup();
	
	ToggleButton cool1;
	ToggleButton warm1;
	ToggleButton leafy1;
	ToggleButton floral1;
	RadioGroup colorRadio1 = new RadioGroup();
	
	ToggleButton skinSelect1;
	ToggleButton colorSelect1;
	RadioGroup selectRadio1 = new RadioGroup(false);
	
	public MultiplayerSelect(LevelType lvl, BaseType[] skins, Color[][] colors) {
		
		super();

        backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYERMODESELECT));

		Vector2 buttonDims = new Vector2(ui.getHeight()/6f, ui.getHeight()/6f);
		
		ready0 = new ToggleButton(new Vector2(ui.getWidth()*8f/9f, ui.getHeight()/7f), buttonDims);
		ready0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		ready0.addLayer(new SpriteLayer(StarCycle.tex.readyIcon, buttonDims).setSpriteColor(Colors.smoke), LayerType.UNTOGGLED);
		ready0.addLayer(new SpriteLayer(StarCycle.tex.readyIcon, buttonDims).setSpriteColor(Colors.spinach), LayerType.TOGGLED);
		ready0.setRotation(90f);

		ready1 = new ToggleButton(new Vector2(ui.getWidth()/9f, ui.getHeight()*6f/7f), buttonDims);
		ready1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		ready1.addLayer(new SpriteLayer(StarCycle.tex.readyIcon, buttonDims).setSpriteColor(Colors.smoke), LayerType.UNTOGGLED);
		ready1.addLayer(new SpriteLayer(StarCycle.tex.readyIcon, buttonDims).setSpriteColor(Colors.spinach), LayerType.TOGGLED);
		ready1.setRotation(270f);
		
		readyGroup.addButton(ready0);
		readyGroup.addButton(ready1);
		readyGroup.setGroupListener(new ScreenDoneClickListener(this, ScreenType.MULTIPLAYER) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setSkins(skin0, skin1);
				setColors(colors0, colors1);
				super.clicked(event, x, y);
			}
		});
		readyGroup.addAll(ui);
		
		trefoilButton = new ToggleButton(new Vector2(ui.getWidth()/2f,ui.getHeight()/5f), buttonDims);
		trefoilButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		trefoilButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		trefoilButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOn[0], buttonDims), LayerType.TOGGLED);
		trefoilButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOff[0], buttonDims), LayerType.UNTOGGLED);
		trefoilButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.TREFOIL;
			}
		});
		
		doublebinaryButton = new ToggleButton(new Vector2(ui.getWidth()/2f,ui.getHeight()*2f/5f), buttonDims);
		doublebinaryButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		doublebinaryButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		doublebinaryButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOn[1], buttonDims), LayerType.TOGGLED);
		doublebinaryButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOff[1], buttonDims), LayerType.UNTOGGLED);
		doublebinaryButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.DOUBLEBINARY;
			}
		});
		
		concentricButton = new ToggleButton(new Vector2(ui.getWidth()/2f,ui.getHeight()*3f/5f), buttonDims);
		concentricButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		concentricButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		concentricButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOn[2], buttonDims), LayerType.TOGGLED);
		concentricButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOff[2], buttonDims), LayerType.UNTOGGLED);
		concentricButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.CONCENTRIC;
			}
		});
		
		venndiagramButton = new ToggleButton(new Vector2(ui.getWidth()/2f,ui.getHeight()*4f/5f), buttonDims);
		venndiagramButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.DOWN);
		venndiagramButton.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		venndiagramButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOn[3], buttonDims), LayerType.TOGGLED);
		venndiagramButton.addLayer(new SpriteLayer(StarCycle.tex.levelIconsOff[3], buttonDims), LayerType.UNTOGGLED);
		venndiagramButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.VENNDIAGRAM;
			}
		});
		
		levelRadio.addButton(venndiagramButton);
		levelRadio.addButton(concentricButton);
		levelRadio.addButton(doublebinaryButton);
		levelRadio.addButton(trefoilButton);
		levelRadio.addAll(ui);
		
		ToggleButton onButton = null;
		
		if (lvl == null) {
			nextLvlConfig = LevelType.TREFOIL;
			}
		else {
			nextLvlConfig = lvl;
		}
		switch (nextLvlConfig) {
		case TREFOIL:
			onButton = trefoilButton;
			break;
		case DOUBLEBINARY:
			onButton = doublebinaryButton;
			break;
		case CONCENTRIC:
			onButton = concentricButton;
			break;
		case VENNDIAGRAM:
			onButton = venndiagramButton;
			break;
		default:
			nextLvlConfig = LevelType.TREFOIL;
			onButton = trefoilButton;
			break;
		}
		onButton.toggled = true;
		onButton.deactivate();
		
		maluma0 = new ToggleButton(new Vector2(ui.getWidth()*3f/4f, ui.getHeight()*3f/10f), buttonDims);
		maluma0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		maluma0.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims).setRotationSpeed(30f));
		maluma0.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims).setRotationSpeed(-30f));
		maluma0.setRotation(90f);
		maluma0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin0 = BaseType.MALUMA;
			}
		});
		
		takete0 = new ToggleButton(new Vector2(ui.getWidth()*3f/4f, ui.getHeight()/2f), buttonDims);
		takete0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		takete0.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims).setRotationSpeed(30f));
		takete0.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims).setRotationSpeed(-30f));
		takete0.setRotation(90f);
		takete0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin0 = BaseType.TAKETE;
			}
		});
		
		derelict0 = new ToggleButton(new Vector2(ui.getWidth()*3f/4f, ui.getHeight()*7f/10f), buttonDims);
		derelict0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		derelict0.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0a, buttonDims).setRotationSpeed(30f));
		derelict0.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0b, buttonDims).setRotationSpeed(-30f));
		derelict0.setRotation(90f);
		derelict0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin0 = BaseType.DERELICT;
			}
		});
		
		skinRadio0.addButton(maluma0);
		skinRadio0.addButton(takete0);
		skinRadio0.addButton(derelict0);
		
		maluma1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()*7f/10f), buttonDims);
		maluma1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		maluma1.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims).setRotationSpeed(30f));
		maluma1.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims).setRotationSpeed(-30f));
		maluma1.setRotation(270f);
		maluma1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin1 = BaseType.MALUMA;
			}
		});
		
		takete1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/2f), buttonDims);
		takete1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		takete1.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims).setRotationSpeed(30f));
		takete1.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims).setRotationSpeed(-30f));
		takete1.setRotation(270f);
		takete1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin1 = BaseType.TAKETE;
			}
		});
		
		derelict1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()*3f/10f), buttonDims);
		derelict1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.TOGGLED);
		derelict1.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0a, buttonDims).setRotationSpeed(30f));
		derelict1.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0b, buttonDims).setRotationSpeed(-30f));
		derelict1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				skin1 = BaseType.DERELICT;
			}
		});
		
		skinRadio1.addButton(maluma1);
		skinRadio1.addButton(takete1);
		skinRadio1.addButton(derelict1);
		
		cool0 = new ToggleButton(new Vector2(ui.getWidth()*3/4f, ui.getHeight()/5f), buttonDims);
		cool0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		cool0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.cool[0]).setSpriteAlpha(0.6f));
		cool0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.cool[1]).setSpriteAlpha(0.6f));
		cool0.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(90f), LayerType.LOCKED);
		cool0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors0 = Colors.cool;
				colorRadio1.unlockAll();
				cool1.lock();
			}
		});
		
		warm0 = new ToggleButton(new Vector2(ui.getWidth()*3/4f, ui.getHeight()*2f/5f), buttonDims);
		warm0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		warm0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.warm[0]).setSpriteAlpha(0.6f));
		warm0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.warm[1]).setSpriteAlpha(0.6f));
		warm0.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(90f), LayerType.LOCKED);
		warm0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors0 = Colors.warm;
				colorRadio1.unlockAll();
				warm1.lock();
			}
		});
		
		leafy0 = new ToggleButton(new Vector2(ui.getWidth()*3/4f, ui.getHeight()*3f/5f), buttonDims);
		leafy0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		leafy0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.leafy[0]).setSpriteAlpha(0.6f));
		leafy0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.leafy[1]).setSpriteAlpha(0.6f));
		leafy0.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(90f), LayerType.LOCKED);
		leafy0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors0 = Colors.leafy;
				colorRadio1.unlockAll();
				leafy1.lock();
			}
		});
		
		floral0 = new ToggleButton(new Vector2(ui.getWidth()*3/4f, ui.getHeight()*4f/5f), buttonDims);
		floral0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		floral0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.floral[0]).setSpriteAlpha(0.6f));
		floral0.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.floral[1]).setSpriteAlpha(0.6f));
		floral0.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(90f), LayerType.LOCKED);
		floral0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors0 = Colors.floral;
				colorRadio1.unlockAll();
				floral1.lock();
			}
		});
		
		colorRadio0.addButton(cool0);
		colorRadio0.addButton(warm0);
		colorRadio0.addButton(leafy0);
		colorRadio0.addButton(floral0);
		
		cool1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()*4f/5f), buttonDims);
		cool1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		cool1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.cool[0]).setSpriteAlpha(0.6f));
		cool1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.cool[1]).setSpriteAlpha(0.6f));
		cool1.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(270f), LayerType.LOCKED);
		cool1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors1 = Colors.cool;
				colorRadio0.unlockAll();
				cool0.lock();
			}
		});
		
		warm1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()*3f/5f), buttonDims);
		warm1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		warm1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.warm[0]).setSpriteAlpha(0.6f));
		warm1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.warm[1]).setSpriteAlpha(0.6f));
		warm1.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(270f), LayerType.LOCKED);
		warm1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors1 = Colors.warm;
				colorRadio0.unlockAll();
				warm0.lock();
			}
		});
		
		leafy1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()*2f/5f), buttonDims);
		leafy1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		leafy1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.leafy[0]).setSpriteAlpha(0.6f));
		leafy1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.leafy[1]).setSpriteAlpha(0.6f));
		leafy1.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(270f), LayerType.LOCKED);
		leafy1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors1 = Colors.leafy;
				colorRadio0.unlockAll();
				leafy0.lock();
			}
		});
		
		floral1 = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/5f), buttonDims);
		floral1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.5f)), LayerType.TOGGLED);
		floral1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(-0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.floral[0]).setSpriteAlpha(0.6f));
		floral1.addLayer(new SpriteLayer(StarCycle.tex.block, buttonDims.cpy().scl(0.125f), buttonDims.cpy().scl(0.75f)).setSpriteColor(Colors.floral[1]).setSpriteAlpha(0.6f));
		floral1.addLayer(new SpriteLayer(StarCycle.tex.noIcon, buttonDims).setSpriteColor(Colors.charcoal).rotateSprite(270f), LayerType.LOCKED);
		floral1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				colors1 = Colors.floral;
				colorRadio0.unlockAll();
				floral0.lock();
			}
		});
		
		colorRadio1.addButton(cool1);
		colorRadio1.addButton(warm1);
		colorRadio1.addButton(leafy1);
		colorRadio1.addButton(floral1);
		
		skinSelect0 = new ToggleButton(new Vector2(ui.getWidth()*8f/9f, ui.getHeight()*2f/5f), buttonDims);
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.DOWN);
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f), LayerType.UNTOGGLED);
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f).setRotationSpeed(-15f), LayerType.TOGGLED);
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims.cpy().scl(1f / 4f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Colors.cyan));
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims.cpy().scl(1f / 4f).rotate(120f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Colors.yellow));
		skinSelect0.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0a, buttonDims.cpy().scl(1f / 4f).rotate(240f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Color.WHITE));
		skinSelect0.setRotation(90f);
		skinSelect0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!skinSelect0.toggled) {
					skinRadio0.removeAll();
				}
				else {
					skinRadio0.addAll(ui);
					skinRadio0.setLayerColor(colors0[0], 1);
					skinRadio0.setLayerColor(colors0[1], 2);
					colorRadio0.removeAll();
				}
			}
		});
		
		skinSelect1 = new ToggleButton(new Vector2(ui.getWidth()/9f, ui.getHeight()*3/5f), buttonDims);
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.DOWN);
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f), LayerType.UNTOGGLED);
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f).setRotationSpeed(-15f), LayerType.TOGGLED);
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.baseMaluma0, buttonDims.cpy().scl(1f / 4f).rotate(180f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Colors.cyan));
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.baseTakete0, buttonDims.cpy().scl(1f / 4f).rotate(60f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Colors.yellow));
		skinSelect1.addLayer(new SpriteLayer(StarCycle.tex.baseDerelict0a, buttonDims.cpy().scl(1f / 4f).rotate(300f), buttonDims.cpy().scl(1f / 2f)).setSpriteColor(Color.WHITE));
		skinSelect1.setRotation(270f);
		skinSelect1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!skinSelect1.toggled) {
					skinRadio1.removeAll();
				}
				else {
					skinRadio1.addAll(ui);
					skinRadio1.setLayerColor(colors1[0], 1);
					skinRadio1.setLayerColor(colors1[1], 2);
					colorRadio1.removeAll();
				}
			}
		});
		
		colorSelect0 = new ToggleButton(new Vector2(ui.getWidth()*8f/9f, ui.getHeight()*3f/5f), buttonDims);
		colorSelect0.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.DOWN);
		colorSelect0.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f), LayerType.UNTOGGLED);
		colorSelect0.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f).setRotationSpeed(-15f), LayerType.TOGGLED);
		colorSelect0.addLayer(new SpriteLayer(StarCycle.tex.swatchIcon, buttonDims.cpy().scl(0.75f)).rotateSprite(90f));
		colorSelect0.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!colorSelect0.toggled) {
					colorRadio0.removeAll();
				}
				else {
					colorRadio0.addAll(ui);
					skinRadio0.removeAll();
				}
			}
		});
		
		colorSelect1 = new ToggleButton(new Vector2(ui.getWidth()/9f, ui.getHeight()*2f/5f), buttonDims);
		colorSelect1.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, buttonDims.cpy().scl(1.25f)), LayerType.DOWN);
		colorSelect1.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f), LayerType.UNTOGGLED);
		colorSelect1.addLayer(new SpriteLayer(StarCycle.tex.swapIcon, buttonDims).setSpriteColor(Colors.smoke).setSpriteAlpha(0.4f).setRotationSpeed(-15f), LayerType.TOGGLED);
		colorSelect1.addLayer(new SpriteLayer(StarCycle.tex.swatchIcon, buttonDims.cpy().scl(0.75f)).rotateSprite(270f));
		colorSelect1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!colorSelect1.toggled){
					colorRadio1.removeAll();
				}
				else {
					colorRadio1.addAll(ui);
					skinRadio1.removeAll();
				}
			}
		});
		
		selectRadio0.addButton(skinSelect0);
		selectRadio0.addButton(colorSelect0);
		
		selectRadio1.addButton(skinSelect1);
		selectRadio1.addButton(colorSelect1);
		
		selectRadio0.addAll(ui);
		selectRadio1.addAll(ui);
		
		if (skins[0] == null) {
			skin0 = BaseType.MALUMA;
		}
		else {
			skin0 = skins[0];
		}
		switch (skin0) {
		case MALUMA:
			onButton = maluma0;
			break;
		case TAKETE:
			onButton = takete0;
			break;
		case DERELICT:
			onButton = derelict0;
			break;
		default:
			skin0 = BaseType.MALUMA;
			onButton = maluma0;
			break;
		}
		
		onButton.toggled = true;
		onButton.deactivate();
		
		if (skins[1] == null) {
			skin1 = BaseType.TAKETE;
		}
		else {
			skin1 = skins[1];
		}
		switch (skin1) {
		case MALUMA:
			onButton = maluma1;
			break;
		case TAKETE:
			onButton = takete1;
			break;
		case DERELICT:
			onButton = derelict1;
			break;
		default:
			skin1 = BaseType.TAKETE;
			onButton = takete1;
			break;
		}
		
		onButton.toggled = true;
		onButton.deactivate();
		
		ToggleButton offButton;
		
		if (colors[0][1] == null) {
			colors0 = Colors.cool;
		}
		else {
			colors0 = colors[0];
		}
		if (colors0 == Colors.cool) {
			onButton = cool0;
			offButton = cool1;
		}
		else if (colors0 == Colors.warm) {
			onButton = warm0;
			offButton = warm1;
		}
		else if (colors0 == Colors.leafy) {
			onButton = leafy0;
			offButton = leafy1;
		}
		else if (colors0 == Colors.floral) {
			onButton = floral0;
			offButton = floral1;
		}
		else {
			colors0 = Colors.cool;
			onButton = cool0;
			offButton = cool1;
		}
		
		onButton.toggled = true;
		onButton.deactivate();
		offButton.lock();
		
		if (colors[1][1] == null || colors0 == colors[1]) {
			colors1 = (colors0 != Colors.warm) ? Colors.warm : Colors.cool;
		}
		else {
			colors1 = colors[1]; 
		}
		if (colors1 == Colors.cool) {
			onButton = cool1;
			offButton = cool0;
		}
		else if (colors1 == Colors.warm) {
			onButton = warm1;
			offButton = warm0;
		}
		else if (colors1 == Colors.leafy) {
			onButton = leafy1;
			offButton = leafy0;
		}
		else if (colors1 == Colors.floral) {
			onButton = floral1;
			offButton = floral0;
		}
		else {
			colors1 = (colors0 != Colors.warm) ? Colors.warm : Colors.cool;
			onButton = (colors0 != Colors.warm) ? warm0 : cool0;
			offButton = (colors0 != Colors.warm) ? warm1 : cool1;
		}
		
		onButton.toggled = true;
		onButton.deactivate();
		offButton.lock();
		
	}
	
	public String toString(){
		return "MultiPlayerSelect";
	}
}
