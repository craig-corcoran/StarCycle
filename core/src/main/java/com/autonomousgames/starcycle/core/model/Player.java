package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class Player {

    public static class PlayerState {
        float ammo = 0f;
        int starsControlled = 0;
        int numActiveOrbs = 0;
        float pointerX = 0f;
        float pointerY = 0f;
        boolean[] buttonStates = new boolean[3];
    }

    //TODO safe to get rid of these?
    //public boolean win;
    //public final boolean ammoCap = false;
    //public final float maxAmmo = 80f * 5f - 1f; // TODO how is this max ammo determined? can players max out?
    //private float winOrbAngle = 0;
    //private boolean winner = false;
    //public boolean altWin = false;
    //public boolean showIncomeOrbs = true;
    static final float velScale = ModelSettings.getFloatSetting("velScale");
    static final float ammoRate = ModelSettings.getFloatSetting("ammoRate");
    static final float maxAmmo = ModelSettings.getFloatSetting("maxAmmo");
    static final float captureFrac = ModelSettings.getFloatSetting("captureFraction");

    final HashMap<Class,Float> orbCosts = new HashMap<Class,Float>(3);

    public final int number;
    public final BaseType basetype;
    public final Color[] colors;
    public final Base base;
    public final LaunchPad launchPad;
	public final boolean baseVisible;
	public final boolean launchpadVisible;

    public final PlayerState state = new PlayerState();
    public boolean frozen = false; // Prevents launching any orbs.

    final Model model;
    final float ammoDripRate;
    final float basePad = StarCycle.meterHeight * (1 / 4.2f);
	final Vector2[] baseOrigins = {
			new Vector2(StarCycle.meterWidth - basePad, basePad),
			new Vector2(basePad, StarCycle.meterHeight - basePad) };

    public Player(int num,
                  Model model,
                  Stage ui,
                  BaseType basetype,
                  Color[] colors,
                  boolean baseVisible,
                  boolean launchpadVisible) {

		number = num;
		ammoDripRate = ModelSettings.getFloatSetting("ammoDripRate");
        state.ammo = ModelSettings.getFloatSetting("initAmmo");
        orbCosts.put(ChargeOrb.class, ModelSettings.getFloatSetting("orbCost"));
        orbCosts.put(Void.class, ModelSettings.getFloatSetting("voidCosts"));
        orbCosts.put(Nova.class, ModelSettings.getFloatSetting("novaCosts"));

        this.model = model;
        this.basetype = basetype;
		this.colors = colors;
		this.baseVisible = baseVisible;
		this.launchpadVisible = launchpadVisible;

        base = new Base(this, baseOrigins[number], ui, baseVisible, false);
		launchPad = new LaunchPad(model, ui, this, launchpadVisible, false); // create ui buttons for player
	}
	
	public Player(int num,
                  Model model,
                  Stage ui,
                  BaseType basetype,
                  Color[] colors,
                  boolean UIVisible) {
		this(num, model, ui, basetype, colors,  UIVisible, UIVisible);
	}

    final Vector2 pos = new Vector2();
	public void update(Star[] stars) {

		state.starsControlled = 0;
        for (Star star : stars) {
            if (star.populations[number] > captureFrac * star.maxPop) {
                state.starsControlled += 1;
            }
        }

        state.ammo += ammoDripRate;
        state.ammo += state.numActiveOrbs * ammoRate;

        if (maxAmmo >= 0) {
            state.ammo = Math.min(state.ammo, maxAmmo);
        }

		if (baseVisible) {
			base.update(Model.dt);
		}

        pos.set(base.getPointer());
        pos.scl(velScale);
        state.pointerX = pos.x;
        state.pointerY = pos.y;

		launchPad.update(Model.dt);

	}

    //private float incOrbGravScale = ModelSettings.getFloatSetting("incOrbGravScale");
    public void draw(SpriteBatch batch) {

        // TODO may want to draw income orbs here
        //for (FakeOrb incomeOrb : incomeOrbs) {
        //    incomeOrb.draw(batch);
        //}

        // TODO should the launchpad be drawn here (instead of with ui) or base drawn with ui?
		base.draw(batch);
	}

    public void launch(Class cls) {
        float cost = orbCosts.get(cls);
        if (state.ammo > cost) {
            state.ammo -= cost;
            pos.set(state.pointerX, state.pointerY);
            pos.nor().scl(0.3f * base.baseDiams[base.level]);
            pos.add(base.origin);
            model.addOrb(number, cls, pos.x, pos.y, state.pointerX, state.pointerY);
        }
    }

	public void dispose() {
		// TODO need to dispose player elements? base, launchpad, incomeorbs, etc
	}

	public void stopDrip() {
		ammoDripRate = 0f;
	}
	
	public void startDrip() {
		ammoDripRate = ModelSettings.getFloatSetting("ammoDripRate");
	}

	public void disableIncome() {
		stopDrip();
		state.ammo = 0f;
	}

    public void setLevel(int i) {
        base.changeLvl(i); // plays levelup sound (as opposed to base.setLevel)
        launchPad.setLvl(i);
    }

    public void setState(PlayerState state) {
        this.state.ammo = state.ammo;
        this.state.starsControlled = state.starsControlled;
        this.state.numActiveOrbs = state.numActiveOrbs;
        this.state.pointerX = state.pointerX;
        this.state.pointerY = state.pointerY;
        this.state.buttonStates = state.buttonStates;
    }
}
