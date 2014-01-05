package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Soundz;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class SettingsScreen extends MenuScreen {
	
	ToggleButton musIcon;
	UISlider musVolSlider;
	ToggleButton sfxIcon;
	UISlider sfxVolSlider;
	Vector2 touchSize = new Vector2(ui.getHeight()/7f, ui.getHeight()/7f);
	Vector2 iconSize = new Vector2(ui.getHeight()/13f, ui.getHeight()/13f);
	
	public long sfxCooldown = 700l; // Minimum time between playing sounds for the volume slider.
	public long sfxLastTime = 0l;
	
	public SettingsScreen() {
		StandardButton backButton = new StandardButton(backPosition, backSize, Texturez.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MAINMENU));

		musIcon = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/10f), touchSize);
		musIcon.addLayer(new SpriteLayer(Texturez.gradientRound, touchSize), LayerType.DOWN);
		musIcon.addLayer(new SpriteLayer(Texturez.musicIcon, iconSize).setSpriteColor(Texturez.cyan));
		musIcon.addLayer(new SpriteLayer(Texturez.noIcon, touchSize).setSpriteColor(Texturez.red), LayerType.TOGGLED);
		musIcon.setRotation(90f);
		musIcon.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (musIcon.toggled) {
					musVolSlider.setPercent(0f);
					Soundz.gameMusic.setVolume(0f);
					UserSettingz.setFloatSetting("musicVolume", 0f);
				}
				else {
					musVolSlider.setPercent(0.5f);
					Soundz.gameMusic.setVolume(0.5f);
					UserSettingz.setFloatSetting("musicVolume", 0.5f);
				}
			}
		});
		
		musVolSlider = new UISlider(new Vector2(ui.getWidth()/4f, ui.getHeight()*7f/12f), new Vector2(ui.getWidth()/20f, ui.getHeight()*2f/3f), false, Texturez.sliderIcon, new Vector2(ui.getWidth()/15f, ui.getWidth()/30f), 00f, Texturez.line,  new Vector2(ui.getHeight()*0.65f, ui.getWidth()/100f), 90f);
		musVolSlider.addListener(new DragListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (super.touchDown(event, x, y, pointer, button)) {
					musVolSlider.setSlider(x, y);
					Soundz.gameMusic.setVolume(musVolSlider.getPercent());
					UserSettingz.setFloatSetting("musicVolume", musVolSlider.getPercent());
                    musIcon.toggled = musVolSlider.getPercent() == 0f;
					return true;
				}
				else {
					return false;
				}
			}
			
			public void drag(InputEvent event, float x, float y, int pointer) {
				musVolSlider.setSlider(x, y);
				Soundz.gameMusic.setVolume(musVolSlider.getPercent());
				UserSettingz.setFloatSetting("musicVolume", musVolSlider.getPercent());
                musIcon.toggled = musVolSlider.getPercent() == 0f;
			}
		});
		
		musVolSlider.setPercent(UserSettingz.getFloatSetting("musicVolume"));

        musIcon.toggled = musVolSlider.getPercent() == 0f;
		
		sfxIcon = new ToggleButton(new Vector2(ui.getWidth()*3f/8f, ui.getHeight()/10f), touchSize);
		sfxIcon.addLayer(new SpriteLayer(Texturez.gradientRound, touchSize), LayerType.DOWN);
		sfxIcon.addLayer(new SpriteLayer(Texturez.soundIcon, iconSize).setSpriteColor(Texturez.yellow));
		sfxIcon.addLayer(new SpriteLayer(Texturez.noIcon, touchSize).setSpriteColor(Texturez.red), LayerType.TOGGLED);
		sfxIcon.setRotation(90f);
		sfxIcon.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (sfxIcon.toggled) {
					sfxVolSlider.setPercent(0f);
					Soundz.sfxVolume = 0f;
					UserSettingz.setFloatSetting("sfxVolume", 0f);
				}
				else {
					sfxVolSlider.setPercent(0.5f);
					Soundz.sfxVolume = 0.5f;
					UserSettingz.setFloatSetting("sfxVolume", 0.5f);
					if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
						Soundz.levelup1Sound.play(Soundz.sfxVolume);
						sfxLastTime = System.currentTimeMillis();
					}
				}
			}
		});
		
		sfxVolSlider = new UISlider(new Vector2(ui.getWidth()*3f/8f, ui.getHeight()*7f/12f), new Vector2(ui.getWidth()/20f, ui.getHeight()*2f/3f), false, Texturez.sliderIcon, new Vector2(ui.getWidth()/15f, ui.getWidth()/30f), 0f, Texturez.line,  new Vector2(ui.getHeight()*0.65f, ui.getWidth()/100f), 90f);
		sfxVolSlider.addListener(new DragListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (super.touchDown(event, x, y, pointer, button)) {
					sfxVolSlider.setSlider(x, y);
					Soundz.sfxVolume = sfxVolSlider.getPercent();
					UserSettingz.setFloatSetting("sfxVolume", sfxVolSlider.getPercent());
					if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
						Soundz.levelup1Sound.play(Soundz.sfxVolume);
						sfxLastTime = System.currentTimeMillis();
					}
                    sfxIcon.toggled = sfxVolSlider.getPercent() == 0f;
					return true;
				}
				else {
					return false;
				}
			}
			
			public void drag(InputEvent event, float x, float y, int pointer) {
				sfxVolSlider.setSlider(x, y);
				Soundz.sfxVolume = sfxVolSlider.getPercent();
				UserSettingz.setFloatSetting("sfxVolume", sfxVolSlider.getPercent());
                sfxIcon.toggled = sfxVolSlider.getPercent() == 0f;
			}
			
			public void dragStop (InputEvent event, float x, float y, int pointer) {
				if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
					Soundz.levelup1Sound.play(Soundz.sfxVolume);
					sfxLastTime = System.currentTimeMillis();
				}
			}
		});
		
		sfxVolSlider.setPercent(UserSettingz.getFloatSetting("sfxVolume"));

        sfxIcon.toggled = sfxVolSlider.getPercent() == 0f;
		
		ui.addActor(backButton);	
		ui.addActor(musIcon);
		ui.addActor(musVolSlider);
		ui.addActor(sfxIcon);
		ui.addActor(sfxVolSlider);
		}

	
	@Override
	public void render(float delta) {
		//super.render() is not called on purpose to skip out on fakeorb stuff
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.disableBlending();
		batch.begin();
		batch.draw(Texturez.mainMenuBG, 0f, 0f, StarCycle.screenWidth,StarCycle.screenHeight);
		batch.enableBlending();
		batch.end();
		ui.draw();
	}
	
	public String toString(){
		return "Settings";
	}
}