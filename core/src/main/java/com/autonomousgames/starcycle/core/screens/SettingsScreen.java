package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.log.UserSettings;
import com.autonomousgames.starcycle.core.ui.*;
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
		StandardButton backButton = new StandardButton(backPosition, backSize, StarCycle.tex.backIcon, padding);
		backButton.setRotation(90f);
		backButton.addListener(new ScreenDoneClickListener(this, ScreenType.MAINMENU));

		musIcon = new ToggleButton(new Vector2(ui.getWidth()/4f, ui.getHeight()/10f), touchSize);
		musIcon.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, touchSize), LayerType.DOWN);
		musIcon.addLayer(new SpriteLayer(StarCycle.tex.musicIcon, iconSize).setSpriteColor(Colors.cyan));
		musIcon.addLayer(new SpriteLayer(StarCycle.tex.noIcon, touchSize).setSpriteColor(Colors.red), LayerType.TOGGLED);
		musIcon.setRotation(90f);
		musIcon.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
            if (musIcon.toggled) {
                musVolSlider.setPercent(0f);
                StarCycle.audio.gameMusic.setVolume(0f);
                UserSettings.setFloatSetting("musicVolume", 0f);
            }
            else {
                musVolSlider.setPercent(0.5f);
                StarCycle.audio.gameMusic.setVolume(0.5f);
                UserSettings.setFloatSetting("musicVolume", 0.5f);
            }
			}
		});
		
		musVolSlider = new UISlider(new Vector2(ui.getWidth()/4f, ui.getHeight()*7f/12f), new Vector2(ui.getWidth()/20f, ui.getHeight()*2f/3f), false, StarCycle.tex.sliderIcon, new Vector2(ui.getWidth()/15f, ui.getWidth()/30f), 00f, StarCycle.tex.line,  new Vector2(ui.getHeight()*0.65f, ui.getWidth()/100f), 90f);
		musVolSlider.addListener(new DragListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (super.touchDown(event, x, y, pointer, button)) {
					musVolSlider.setSlider(x, y);
					StarCycle.audio.gameMusic.setVolume(musVolSlider.getPercent());
					UserSettings.setFloatSetting("musicVolume", musVolSlider.getPercent());
                    musIcon.toggled = musVolSlider.getPercent() == 0f;
					return true;
				}
				else {
					return false;
				}
			}
			
			public void drag(InputEvent event, float x, float y, int pointer) {
				musVolSlider.setSlider(x, y);
				StarCycle.audio.gameMusic.setVolume(musVolSlider.getPercent());
				UserSettings.setFloatSetting("musicVolume", musVolSlider.getPercent());
                musIcon.toggled = musVolSlider.getPercent() == 0f;
			}
		});
		
		musVolSlider.setPercent(UserSettings.getFloatSetting("musicVolume"));

        musIcon.toggled = musVolSlider.getPercent() == 0f;
		
		sfxIcon = new ToggleButton(new Vector2(ui.getWidth()*3f/8f, ui.getHeight()/10f), touchSize);
		sfxIcon.addLayer(new SpriteLayer(StarCycle.tex.gradientRound, touchSize), LayerType.DOWN);
		sfxIcon.addLayer(new SpriteLayer(StarCycle.tex.soundIcon, iconSize).setSpriteColor(Colors.yellow));
		sfxIcon.addLayer(new SpriteLayer(StarCycle.tex.noIcon, touchSize).setSpriteColor(Colors.red), LayerType.TOGGLED);
		sfxIcon.setRotation(90f);
		sfxIcon.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
            if (sfxIcon.toggled) {
                sfxVolSlider.setPercent(0f);
                StarCycle.audio.sfxVolume = 0f;
                UserSettings.setFloatSetting("sfxVolume", 0f);
            }
            else {
                sfxVolSlider.setPercent(0.5f);
                StarCycle.audio.sfxVolume = 0.5f;
                UserSettings.setFloatSetting("sfxVolume", 0.5f);
                if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
                    StarCycle.audio.levelup1Sound.play(StarCycle.audio.sfxVolume);
                    sfxLastTime = System.currentTimeMillis();
                }
            }
			}
		});
		
		sfxVolSlider = new UISlider(new Vector2(ui.getWidth()*3f/8f, ui.getHeight()*7f/12f), new Vector2(ui.getWidth()/20f, ui.getHeight()*2f/3f), false, StarCycle.tex.sliderIcon, new Vector2(ui.getWidth()/15f, ui.getWidth()/30f), 0f, StarCycle.tex.line,  new Vector2(ui.getHeight()*0.65f, ui.getWidth()/100f), 90f);
		sfxVolSlider.addListener(new DragListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (super.touchDown(event, x, y, pointer, button)) {
					sfxVolSlider.setSlider(x, y);
					StarCycle.audio.sfxVolume = sfxVolSlider.getPercent();
					UserSettings.setFloatSetting("sfxVolume", sfxVolSlider.getPercent());
					if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
						StarCycle.audio.levelup1Sound.play(StarCycle.audio.sfxVolume);
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
				StarCycle.audio.sfxVolume = sfxVolSlider.getPercent();
				UserSettings.setFloatSetting("sfxVolume", sfxVolSlider.getPercent());
                sfxIcon.toggled = sfxVolSlider.getPercent() == 0f;
			}
			
			public void dragStop (InputEvent event, float x, float y, int pointer) {
				if (sfxLastTime + sfxCooldown < System.currentTimeMillis()) {
					StarCycle.audio.levelup1Sound.play(StarCycle.audio.sfxVolume);
					sfxLastTime = System.currentTimeMillis();
				}
			}
		});
		
		sfxVolSlider.setPercent(UserSettings.getFloatSetting("sfxVolume"));
        sfxIcon.toggled = sfxVolSlider.getPercent() == 0f;
		
		ui.addActor(backButton);	
		ui.addActor(musIcon);
		ui.addActor(musVolSlider);
		ui.addActor(sfxIcon);
		ui.addActor(sfxVolSlider);

        drawFakeOrbs = false;
	}


	public String toString(){
		return "Settings";
	}
}