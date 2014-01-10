package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.ListIterator;

public class Player {

	public final int number;
	public float ammo;
	public boolean win;
	public Base base;
	public boolean frozen = false; // Prevents launching any orbs.
	public ArrayList<Orb> orbs = new ArrayList<Orb>(); // each player has an orb list
	public ArrayList<Void> voids = new ArrayList<Void>(); // each player has a void list
	public ArrayList<Nova> novas = new ArrayList<Nova>(); // each player has a nova list
    public ArrayList<ImageOrb> incomeOrbs = new ArrayList<ImageOrb>();
	public ArrayList<WinOrb> winOrbs = new ArrayList<WinOrb>();
	public int starsCaptured;
	//public final BitmapFont font;
	public float initAmmo;
	public final boolean baseVisible;
	public final boolean launchpadVisible;
	public final boolean ammoCap = false;
	public final float maxAmmo = 80f * 5f - 1f; // TODO how is this max ammo determined? can players max out?
	public final LaunchPad launchPad;
	private float ammoDripRate;
    private float winOrbAngle = 0;
    private boolean winner = false;
    public boolean altWin = false;
    private final float basePad = StarCycle.meterHeight * (1 / 4.2f);
	Vector2[] baseOrigins = {
			new Vector2(StarCycle.meterWidth - basePad, basePad),
			new Vector2(basePad, StarCycle.meterHeight - basePad) };

	public BaseType basetype;
	public Color[] colors;

	public Player(int num, BaseType basetype, Color[] colors, ModelScreen screen, 
				Stage ui, boolean baseVisible, boolean launchpadVisible) {
		this.number = num;
        float baseRadius = UserSettingz.getFloatSetting("baseRadius");
		this.initAmmo = UserSettingz.getFloatSetting("initAmmo");
		this.ammoDripRate = UserSettingz.getFloatSetting("ammoDripRate");
		this.ammo = initAmmo;
		this.basetype = basetype;
		this.colors = colors;
		this.baseVisible = baseVisible;
		this.launchpadVisible = launchpadVisible;
		this.base = new Base(this, baseOrigins[number], baseRadius, ui, baseVisible);
		launchPad = new LaunchPad(screen, this, launchpadVisible); // create ui buttons for player
	}
	
	public Player(int num, BaseType basetype, Color[] colors, ModelScreen screen, 
			Stage ui, boolean UIVisible) {
		this(num, basetype, colors, screen, ui, UIVisible, UIVisible);
	}

    private Vector2 difference = new Vector2();
    private float minDiff = 80f;
	public void update(float delta, ArrayList<Star> stars, Vector2[] starPositions) {

        ammo += ammoDripRate;
		starsCaptured = 0;
        for (Star star : stars) {
            if (star.populations[number] > Star.captureRatio * star.maxPop) {
                starsCaptured += 1;
            }
        }
		// check if player has conquered the star cluster
        if (!altWin) {win = (starsCaptured == stars.size());}
		
		//update player orb positions and get income
        for (Orb o: orbs) {
            o.update(delta, starPositions);
        }

        for (Void v: voids) {
            v.update(delta, starPositions);
        }

        for (Orb n: novas) {
            n.update(delta, starPositions);
        }

        // add ammo from stars
        for (Star star: stars) {
            star.updateControl();
        }

		if (ammoCap) {
			ammo = Math.min(ammo, maxAmmo);
		}
		if (baseVisible) {
			base.update(delta);
		}
		launchPad.update(delta);
		
		// update income orb list to move toward base
        for (ListIterator<ImageOrb> itr = incomeOrbs.listIterator(); itr.hasNext();) {
            ImageOrb fakeOrb = itr.next();

            difference.set((base.buttonLoc.x - fakeOrb.position.x), (base.buttonLoc.y - fakeOrb.position.y));
            if ((difference.len() < minDiff) | (!fakeOrb.insideView())) {
                Gdx.app.log("player", "removing orb");
                itr.remove();
            }
            else {
                difference = difference.nor();
                fakeOrb.acceleration.set(0.8f * difference.x, 0.8f * difference.y);
                fakeOrb.update(delta);
            }
        }
	}

    public void updateWinOrbs(Float delta)	{
        // update win orblist
        for (ListIterator<WinOrb> itr = winOrbs.listIterator(); itr.hasNext();) {
            FakeOrb winOrb = itr.next();
            winOrb.update(delta);
            winOrb.age += 1;
            if (winOrb.age > 50){
                itr.remove();
            }
        }
        if (winner){
            winOrbAngle += .1;
            if (winOrbAngle > 2*MathUtils.PI) {
                winOrbAngle -= 2*MathUtils.PI;
            }
            Vector2 winOrbVector = new Vector2(MathUtils.cos(winOrbAngle) * MathUtils.PI/180f,
                    MathUtils.sin(winOrbAngle) * MathUtils.PI/180f).nor().scl(10f);
            winOrbs.add(new WinOrb(this, 8f, StarCycle.pixelScreenCenter,
                    StarCycle.screenWidth, StarCycle.screenHeight, winOrbVector.cpy(), new Vector2(0f, 0f).cpy()));
            winOrbs.add(new WinOrb(this, 8f, StarCycle.pixelScreenCenter,
                    StarCycle.screenWidth, StarCycle.screenHeight, winOrbVector.scl(-1f).cpy(), new Vector2(0f, 0f).cpy()));
        }
    }

    public void draw(SpriteBatch batch) {

        for (FakeOrb incomeOrb : incomeOrbs) {
            incomeOrb.draw(batch);
        }

        for (Orb orb : orbs) {
            orb.draw(batch);
        }

        for (Orb aVoid : voids) {
            aVoid.draw(batch);
        }

        for (Orb nova : novas) {
            nova.draw(batch);
        }
		
		base.draw(batch);

        for (ImageOrb incomeOrb: incomeOrbs) {
            incomeOrb.draw(batch);
        }

        for (FakeOrb winOrb : winOrbs) {
            winOrb.draw(batch);
        }


	}

	public void dispose() {
		orbs.clear();
        winOrbs.clear();
		// TODO need to dispose player elements?n launchpad, etc
	}

	public void stopDrip() {
		ammoDripRate = 0f;
	}
	
	public void startDrip() {
		UserSettingz.getFloatSetting("ammoDripRate");
	}
    public void setWinner() {
        winner = true;
    }
	
	public void disableIncome() {
		stopDrip();
		ammo = 0f;
//		launchPad.drawAmmo = false;
	}
}
