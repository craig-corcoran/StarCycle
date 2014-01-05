package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Bot extends Player {
	
	private ModelScreen screen;
	private int other;
	public Vector2[] starPositions;
	private ArrayList<Float> starDistances = new ArrayList<Float>();
	private Star targetStar;
	private Vector2 aimPos = new Vector2();
    private float retargetPeriod = 3f;
	private float retargetCounter = retargetPeriod;
    private float chargeRadius;
//	private float maxOrbSpeed;
//	private float travelTime;
	private float voidThreshold;
	private float novaThreshold;
	private float voidPeriod;
	private float novaPeriod;
	private float voidCounter = 0f;
	private float novaCounter = 0f;
	private boolean active = true;
	
	public Bot(int num, Base.BaseType basetype, Color[] colors, ModelScreen screen, Stage ui, boolean drawBase, boolean drawLaunchPad) {
		super(num, basetype, colors, screen, ui, drawBase, drawLaunchPad);
		this.screen = screen;
		other = (num == 0) ? 1 : 0;
		
//		maxOrbSpeed = Base.maxPointerLength*UserSettingz.getSetting("velScaleOrbFact");
		chargeRadius = UserSettingz.getFloatSetting("chargeRadius");
		launchPad.streamOrbs = true;
	}
	
	public void setBotType(BotType botType) {
		switch (botType) {
		case DEAD:
			active = false;
			launchPad.streamOrbs = false; 
			break;
		case EZ:
			voidThreshold = 30f;
			novaThreshold = 30f;
			novaPeriod = 100f;
			voidPeriod = 20f;
			break;
		case MEDIUM:
			voidThreshold = 15f;
			novaThreshold = 30f;
			novaPeriod = 30f;
			voidPeriod = 15f;
			break;
		case PWN:
			voidThreshold = 3f;
			novaThreshold = 0f;
			voidPeriod = 2f;
			novaPeriod = 15f;
			this.ammo = 400;
			break;
		}
	}

	public void initializeModel(Model model) { // This can't happen in the constructor, as the model has not been constructed.
		starPositions = model.starPositions;
		targetStar = model.stars[0];
        for (Vector2 starPosition : starPositions) {
            this.starDistances.add(baseOrigins[number].dst(starPosition));
        }
	}
	
	@Override
	public void update(float delta, Star[] stars, Vector2[] starPositions) {
		if (active) {
			if (retargetCounter >= retargetPeriod) {
				targetStar.targetted=false;
				targetStar = nearestStar(stars);
				targetStar.targetted=true;
				retargetCounter = 0f;
			}
			else {
				retargetCounter += delta;
			}
			
			aimAtStar(targetStar);
			
			voidCounter += delta;
			novaCounter += delta;
			
			//launch voids!
			if ((voidCounter >= voidPeriod) & 
					((targetStar.orbNum[other] >= voidThreshold) || (targetStar.orbNum[this.number] >= voidThreshold)) & 
					(this.starsCaptured >= UserSettingz.getFloatSetting("gravWellStars"))) {
				if (MathUtils.random() < .3f ){
					base.setPointer(new Vector2(MathUtils.random()*Base.maxPointerLength,(MathUtils.random()-0.5f)*Base.maxPointerLength*2));
				}
				screen.launch(Orb.OrbType.VOID, this);
				voidCounter = 0f;
			}
			
			// launch novas!
			if ((novaCounter >= novaPeriod) & (targetStar.orbNum[other] >= novaThreshold) & 
					(this.starsCaptured >= UserSettingz.getFloatSetting("nukeStars"))) {
				base.setPointer(new Vector2(MathUtils.random()*Base.maxPointerLength,(MathUtils.random()-0.5f)*Base.maxPointerLength*2));
				screen.launch(Orb.OrbType.NOVA, this);
				novaCounter = 0f;
			}
		}
		super.update(delta, stars, starPositions);
	}
	
	public void aimAtStar(Star target) {
		aimPos.set(target.position);
        float radiusScalar = 1.5f * (target.radius + chargeRadius);
		aimPos.sub(baseOrigins[number]);
		aimPos.rotate(MathUtils.radDeg*MathUtils.atan2(radiusScalar, aimPos.len()));
 //		travelTime = aimPos.dst(baseOrigins[number])/maxOrbSpeed;
//		aimPos.add(velo.x*travelTime, velo.y*travelTime);
//		base.setPointer(aimPos.sub(baseOrigins[number]));
		base.setPointer(aimPos);
	}

	public void vecAim (Vector2 targetVec) {
		aimPos.set(targetVec).sub(baseOrigins[number]);
		base.setPointer(aimPos);
	}
	
	private Star nearestStar(Star[] stars) {
		int nearest = 0;
		starDistances.set(0, baseOrigins[number].dst(starPositions[0]));
		for (int i=1; i < starDistances.size(); i++) {
			starDistances.set(i, baseOrigins[number].dst(starPositions[i]));
			if (starDistances.get(i) < starDistances.get(nearest)) {
				nearest = i;
			}
		}
		return stars[nearest];
	}
	
}
