package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.Soundz;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.ui.BaseButton;
import com.autonomousgames.starcycle.core.ui.Layer;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Base {
	public final Player player;
	public Vector2 origin;
	public Vector2 pointer = new Vector2(); // vector pointing from origin to the tip of the rings
	private float pointerScale;

	// TODO should these be passed into the constructor or just set statically?
	LayeredButton aimer;
	public Vector2 handleImDims;
	public Vector2 chevronImDims;
    public Vector2 baseDims;
	public float[] baseDiams;
	public BaseButton baseButton;
	Vector2 buttonLoc;
	public float angle;
	public float angleDeg;
	private float angleOfBaseRotation = 0;
	public static float maxPointerLength;
	public static float minPointerLength;
	float maxAimerLength;
	//public static float minSpeed;
	public static float velScale = (Float) UserSettingz.getFloatSetting("velScale");
	private boolean UIVisible = true;
	private float UIScaleFactor;

	private static float baseRotationSpeed;
	
	public int level;
    int maxLevel = 2;
    public boolean manualLvl = false;

    public enum BaseType {
		MALUMA, TAKETE, TARGET, DERELICT, CLOCKWORK
	}
    //constructor with optional ui visible parameter
	public Base(Player player, Vector2 origin, float baseRadius, Stage ui, boolean UIVisible) {
		
		baseRotationSpeed = UserSettingz.getFloatSetting("baseRotationSpeed");
		this.origin = new Vector2(origin.x, origin.y);
		this.player = player;
		this.UIVisible = UIVisible;
		baseDiams = new float[] { 2f, 2.4f, 2.8f };
		baseDims = new Vector2(1f, 1f).scl(StarCycle.pixelsPerMeter * baseRadius);
		UIScaleFactor = ui.getHeight()/10f;
		buttonLoc = new Vector2(origin.x, origin.y).scl(UIScaleFactor);
		
		if (UIVisible) {
			baseButton = new BaseButton(player.basetype, player.colors, buttonLoc, baseDims);
			ui.addActor(baseButton);
		}

		maxPointerLength = 2f;
		minPointerLength = 1.2f;
		if (player.number == 1) {
			setPointer((maxPointerLength + minPointerLength) / 2f, 0f); // vector to tip of rings
		} else if (player.number == 0) {
			setPointer((-maxPointerLength - minPointerLength) / 2f, 0f); // vector to tip of rings
		}
		maxAimerLength = maxPointerLength*StarCycle.pixelsPerMeter;

        float handleSize = 0.54f;
		// TODO if we keep these square, get rid of separate width and height
        float handleWidth = handleSize;
        float handleHeight = handleSize;
        float chevronSize = 0.45f;
        float chevronWidth = chevronSize;
        float chevronHeight = chevronSize;
		handleImDims = new Vector2(handleWidth, handleHeight).scl(StarCycle.pixelsPerMeter);
		chevronImDims = new Vector2(chevronWidth, chevronHeight).scl(StarCycle.pixelsPerMeter);
		
		aimer = new LayeredButton(buttonLoc, baseDims);
		aimer.addLayer(new SpriteLayer(Texturez.aimer[0], new Vector2(0f, -1f), handleImDims).setSpriteColor(player.colors[1]));
		for (int i = 0; i < 2; i ++) {
			aimer.addLayer(new SpriteLayer(Texturez.aimer[i+1], new Vector2(0f, 1f), chevronImDims).setSpriteColor(player.colors[i]));
		}
	}
    // basic constructor, assumes UI is visible
	public Base(Player player, Vector2 origin, float baseRadius, Stage ui) {
		this(player, origin, baseRadius, ui, true);
	}

	// Called in Player.java
	public void draw(SpriteBatch batch) {
		if (UIVisible) {
			aimer.setRotation(angleDeg);
			for (int i = 0; i < 3; i ++) {
				Layer layer = aimer.getLayer(i);
				layer.setCenter(layer.getCenter().nor().scl(maxAimerLength * pointerScale));
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
            if (Math.min(maxLevel, player.starsCaptured) != level) {
                if (Math.min(maxLevel, player.starsCaptured) > level) {
                    if (player.starsCaptured == 1){
                        Soundz.levelup1Sound.play(UserSettingz.getFloatSetting("sfxVolume"));
                    }
                    if (player.starsCaptured == 2) {
                        Soundz.levelup2Sound.play(UserSettingz.getFloatSetting("sfxVolume"));
                    }

                } else {
                    Soundz.leveldownSound.play(UserSettingz.getFloatSetting("sfxVolume"));
                }
                level = Math.min(maxLevel, player.starsCaptured);
                baseButton.setLevel(level);
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
		pointer = pointer.set(pntr);
		if (pointer.len() > maxPointerLength) {
			pointer.nor().scl(maxPointerLength);
		} else if (pointer.len() < minPointerLength) {
			pointer.nor().scl(minPointerLength);
		}
		angle = MathUtils.atan2(pointer.y, pointer.x) - MathUtils.PI / 2f;
		angleDeg = angle * 180f / MathUtils.PI;
		pointerScale = pointer.len() / maxPointerLength;
		//Gdx.app.log(angle + "", pointer.angle().toString());
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
}
