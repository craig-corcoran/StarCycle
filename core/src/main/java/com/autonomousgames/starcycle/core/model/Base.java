package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.ui.BaseButton;
import com.autonomousgames.starcycle.core.ui.Layer;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Base {

    static final float maxPointerLength = 2f;
    static final float minPointerLength = 1.2f;
    static final Vector2 handleImDims = new Vector2(1f, 1f).scl(0.54f * StarCycle.pixelsPerMeter);
    static final Vector2 chevronImDims = new Vector2(1f, 1f).scl(0.45f * StarCycle.pixelsPerMeter);
    static final float baseRotationSpeed = ModelSettings.getFloatSetting("baseRotationSpeed");
    static final Vector2 baseDims = new Vector2(1f, 1f).scl(StarCycle.pixelsPerMeter *
                                                            ModelSettings.getFloatSetting("baseRadius"));

    static final float[] baseDiams = new float[] { 2f, 2.4f, 2.8f };
    final Player player;
	final Vector2 origin;
    final LayeredButton aimer;
	final Vector2 pointer = new Vector2(); // vector pointing from origin to the tip of the rings

    final boolean UIVisible;
    final float UIScaleFactor;
	final BaseButton baseButton;
	final Vector2 buttonLoc;
    final boolean manualLvl;

    int level;
    float pointerScale;
	float angle;
	float angleOfBaseRotation = 0;

    public enum BaseType {
		MALUMA, TAKETE, TARGET, DERELICT, CLOCKWORK
	}
    //constructor with optional ui visible parameter
	public Base(Player player, Vector2 origin, Stage ui, boolean UIVisible, boolean manualLvl) {
		
		this.origin = new Vector2(origin.x, origin.y);
		this.player = player;
		this.UIVisible = UIVisible;
        this.manualLvl = manualLvl;
		UIScaleFactor = ui.getHeight()/10f; // TODO isn't this (effectively) stored in StarCycle.java?
		buttonLoc = new Vector2(origin.x, origin.y).scl(UIScaleFactor);
		
		if (UIVisible) {
			baseButton = new BaseButton(player.basetype, player.colors, buttonLoc, baseDims);
			ui.addActor(baseButton);
		}
        else { baseButton = null; }

		if (player.number == 1) {
			setPointer((maxPointerLength + minPointerLength) / 2f, 0f); // vector to tip of rings
		} else if (player.number == 0) {
			setPointer((-maxPointerLength - minPointerLength) / 2f, 0f); // vector to tip of rings
		}
		
		aimer = getAimer(buttonLoc, baseDims, player.colors);
	}
    // basic constructor, assumes UI is visible and we are setting the level manually
	public Base(Player player, Vector2 origin, float baseRadius, Stage ui) {
		this(player, origin, ui, true, false);
	}

	// Called in Player.java
	public void draw(SpriteBatch batch) {
		if (UIVisible) {
			aimer.setRotation(angle);
			for (int i = 0; i < 3; i ++) {
				Layer layer = aimer.getLayer(i);
				layer.setRelPosLen(maxPointerLength*StarCycle.pixelsPerMeter * pointerScale);
			}
				aimer.draw(batch, 1f);
		}
	}

	public void update(float delta) {
		
		angleOfBaseRotation += delta * baseRotationSpeed; 
		if (angleOfBaseRotation > 360f) {
			angleOfBaseRotation -= 360;
		}

        if (!manualLvl) {
            int num = Math.min(2, player.state.starsControlled);
            if (num != level) {
                changeLvl(num);
            }
        }
	}

	public void setPointer(float x, float y) {
		setPointer(new Vector2(x,y));
	}
	
	public void setPointerPolar(float r, float theta) {
		setPointer(new Vector2(MathUtils.cosDeg(theta),MathUtils.sinDeg(theta)).scl(r));
	}
	
	public void setPointer(Vector2 pntr) {
		pointer.set(pntr);
		if (pointer.len() > maxPointerLength) {
			pointer.nor().scl(maxPointerLength);
		} else if (pointer.len() < minPointerLength) {
			pointer.nor().scl(minPointerLength);
		}
		angle = (MathUtils.atan2(pointer.y, pointer.x) - MathUtils.PI / 2f) * 180f / MathUtils.PI;
		pointerScale = pointer.len() / maxPointerLength;
	}
	
	public void rotatePointer(float theta) {
		setPointer(getPointer().rotate(theta));
	}
	
	public Vector2 getPointer() {
		return pointer.cpy();
	}
	
	public Vector2 getBaseSize() {
		return baseDims.cpy();
	}

    // This takes meters.
	public void moveBase(Vector2 newPos) {
		origin.set(newPos.x, newPos.y);
		baseButton.setCenter(newPos.cpy().scl(UIScaleFactor));
		aimer.setCenter(newPos.cpy().scl(UIScaleFactor));
	}

    // This takes pixels.
    public void translateBase(float x, float y) {
        x = x/UIScaleFactor;
        y = y/UIScaleFactor;
        moveBase(new Vector2(origin.x + x, origin.y + y));
    }

    public LayeredButton getAimer(Vector2 pos, Vector2 dims, Color[] colors) {
        LayeredButton button = new LayeredButton(pos);
        button.addLayer(new SpriteLayer(StarCycle.tex.aimer[0], new Vector2(0f, -1f).scl((maxPointerLength + minPointerLength)*StarCycle.pixelsPerMeter / 2f), handleImDims).setSpriteColor(colors[1]));
        for (int i = 0; i < 2; i ++) {
            button.addLayer(new SpriteLayer(StarCycle.tex.aimer[i + 1], new Vector2(0f, 1f).scl((maxPointerLength + minPointerLength)*StarCycle.pixelsPerMeter / 2f), chevronImDims).setSpriteColor(colors[i]));
        }
        return button;
    }

    // just sets vars, no sound effects
    public void setLevel(int i) {
        baseButton.setLevel(i);
        level = i;
    }

    // plays sound effect
    public void changeLvl(int i) {
        if (i > level) {
            if (i == 1){
                StarCycle.audio.levelup1Sound.play(StarCycle.audio.sfxVolume);
            }
            if (i == 2) {
                StarCycle.audio.levelup2Sound.play(StarCycle.audio.sfxVolume);
            }
        }
        else if (i < level) {
            StarCycle.audio.leveldownSound.play(StarCycle.audio.sfxVolume);
        }
        setLevel(i);
    }
}
