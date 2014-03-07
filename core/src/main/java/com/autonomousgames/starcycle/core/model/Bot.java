package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Bot extends Player {
	
	private ModelScreen screen;
	private int other;
	public Star[] stars;
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

        super(num, screen.model, ui, basetype, colors, true, drawLaunchPad);
		this.screen = screen;
		other = (num == 0) ? 1 : 0;
		
//		maxOrbSpeed = Base.maxPointerLength*ModelSettings.getSetting("velScaleOrbFact");
		chargeRadius = ModelSettings.getFloatSetting("chargeRadius");
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
			this.state.ammo = 400;
			break;
		}
	}

	public void initializeModel(Model model) { // This can't happen in the constructor, as the model has not been constructed.
		stars = model.stars;
		targetStar = model.stars[0];
        for (Star st : stars) {
            this.starDistances.add(baseOrigins[number].dst(st.state.x, st.state.y));
        }
	}
	
	@Override
	public void update(Star[] stars) {
		if (active) {
			if (retargetCounter >= retargetPeriod) {
				//targetStar.targetted=false;
				targetStar = nearestStar(stars);
				//targetStar.targetted=true;
				retargetCounter = 0f;
			}
			else {
				retargetCounter += Model.dt;
			}
			
			aimAtStar(targetStar);
			
			voidCounter += Model.dt;
			novaCounter += Model.dt;
			
			//launch voids!
			if ((voidCounter >= voidPeriod) & 
					((targetStar.state.numActiveOrbs[other] >= voidThreshold) || (targetStar.state.numActiveOrbs[this.number] >= voidThreshold)) &
					(this.state.starsControlled >= ModelSettings.getFloatSetting("gravWellStars"))) {
				if (MathUtils.random() < .3f ){
					base.setPointer(new Vector2(MathUtils.random()*Base.maxPointerLength,(MathUtils.random()-0.5f)*Base.maxPointerLength*2));
				}
				launch(Void.class);
				voidCounter = 0f;
			}
			
			// launch novas!
			if ((novaCounter >= novaPeriod) & (targetStar.state.numActiveOrbs[other] >= novaThreshold) &
					(this.state.starsControlled >= ModelSettings.getFloatSetting("nukeStars"))) {
				base.setPointer(new Vector2(MathUtils.random()*Base.maxPointerLength,(MathUtils.random()-0.5f)*Base.maxPointerLength*2));
				launch(Nova.class);
				novaCounter = 0f;
			}
		}
		super.update(stars);
	}
	
	public void aimAtStar(Star target) {
		aimPos.set(target.state.x, target.state.y);
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
		starDistances.set(0, baseOrigins[number].dst(stars[0].state.x, stars[0].state.y));
		for (int i=1; i < starDistances.size(); i++) {
			starDistances.set(i, baseOrigins[number].dst(stars[i].state.x, stars[i].state.y));
			if (starDistances.get(i) < starDistances.get(nearest)) {
				nearest = i;
			}
		}
		return stars[nearest];
	}
	
}
