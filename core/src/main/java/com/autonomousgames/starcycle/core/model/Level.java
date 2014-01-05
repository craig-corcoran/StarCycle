package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

public class Level {

	public int numStars;
    private ArrayList<Orbitable> pathedObjList = new ArrayList<Orbitable>();

	public float totalEnergy;
	private final World world;
	public ArrayList<Star> stars = new ArrayList<Star>();
	//private Player[] players;

	public static enum LevelType {
		DOUBLEBINARY, TREFOIL, VENNDIAGRAM, CONCENTRIC, SINGLE, DOUBLE, TRIPLE, QUAD, NOSTARS
	}

	public Level(World world, LevelType lvl, Player[] players) {
        float orbitSpeed = UserSettingz.getFloatSetting("orbitSpeed");
        float starRadius = UserSettingz.getFloatSetting("starRadius");
		this.world = world;
		
		Vector2 center = new Vector2((1 / 2f) * StarCycle.meterWidth,
				(1 / 2f) * StarCycle.meterHeight);
		Vector2 centerRight = new Vector2((1 / 2f) * StarCycle.meterWidth,
				(1 / 3f) * StarCycle.meterHeight);
		Vector2 centerLeft = new Vector2((1 / 2f) * StarCycle.meterWidth,
				(2 / 3f) * StarCycle.meterHeight);
		
		EllipticalPath ellipsePath = new EllipticalPath(3f, 4f, center);
		
		switch (lvl) {
		case SINGLE:
			numStars = 1;
			stars = new ArrayList<Star>(numStars);
			addStaticStar(players, 0, center, 1.5f * starRadius);
			break;
		case DOUBLE:
			numStars = 2;
			stars = new ArrayList<Star>(numStars);
			addStaticStar(players, 0, centerRight, 1.5f * starRadius);
			addStaticStar(players, 1, centerLeft, 1.5f * starRadius);
			break;
		case TRIPLE:
			numStars = 3;
			stars = new ArrayList<Star>(numStars);
			Vector2 farCenter = new Vector2((1 / 3f) * StarCycle.meterWidth, (1 / 2f) * StarCycle.meterHeight);
			
			addStaticStar(players, 0, centerRight, 1.5f * starRadius);
			addStaticStar(players, 1, centerLeft, 1.5f * starRadius);
			addStaticStar(players, 2, farCenter, 1.5f * starRadius);
			break;
		case QUAD:
			numStars = 4;
			stars = new ArrayList<Star>(numStars);
			Vector2 lowerRight = new Vector2((1 / 2f) * StarCycle.meterWidth,
					(2 / 3f) * StarCycle.meterHeight);
			Vector2 upperFarRight = new Vector2((1 / 3f) * StarCycle.meterWidth, (5f / 6f) * StarCycle.meterHeight);
			Vector2 upperCenter = new Vector2((1 / 3f) * StarCycle.meterWidth, (1f / 2f) * StarCycle.meterHeight);
			Vector2 lowerLeft = new Vector2((1 / 2f) * StarCycle.meterWidth,
					(1f / 3f) * StarCycle.meterHeight);
			
			addStaticStar(players, 0, lowerRight, 1.5f * starRadius);
			addStaticStar(players, 1, upperCenter, 1.5f * starRadius);
			addStaticStar(players, 2, upperFarRight, 1.5f * starRadius);
			addStaticStar(players, 3, lowerLeft, 1.5f * starRadius);
			break;
		case DOUBLEBINARY:
			numStars = 4;
			stars = new ArrayList<Star>(numStars);
			
			DoubleEllipticalPath binaryPath1 = new DoubleEllipticalPath(2.3f,
					3.1f, center, 1.15f, 1.15f, 0f, -3f);
			DoubleEllipticalPath binaryPath2 = new DoubleEllipticalPath(2.3f,
					3.1f, center, 1.15f, 1.15f, MathUtils.PI, -3f);

			addStarGroup(players, 0, 2, center, starRadius, binaryPath1, 0f,
                    orbitSpeed);
			addStarGroup(players, 2, 2, center, starRadius, binaryPath2, 0f,
                    orbitSpeed);
			break;
		case TREFOIL:
			numStars = 4;
			stars = new ArrayList<Star>(numStars);
			
			addStaticStar(players, 0, center, 1.5f * starRadius);
			addStarGroup(players, 1, 3, center, starRadius, ellipsePath, 0f, orbitSpeed);
			break;
		case VENNDIAGRAM:
			numStars = 4;
			stars = new ArrayList<Star>(numStars);
			
			Vector2 lowCenter = new Vector2((0.55f) * StarCycle.meterWidth,
					(0.6f) * StarCycle.meterHeight);
			Vector2 highCenter = new Vector2((0.45f) * StarCycle.meterWidth,
					(0.4f) * StarCycle.meterHeight);
			EllipticalPath lowEllipse = new EllipticalPath(2.3f, 2.3f,
					lowCenter);
			EllipticalPath highEllipse = new EllipticalPath(2.3f, 2.3f,
					highCenter);
			
			addStarGroup(players, 0, 2, lowCenter, starRadius, lowEllipse, 0.0f, orbitSpeed);
			addStarGroup(players, 2, 2, highCenter, starRadius, highEllipse, 0.0f, orbitSpeed);
			break;
		case CONCENTRIC:
			numStars = 4;
			stars = new ArrayList<Star>(numStars);
			
			EllipticalPath innerEllipse = new EllipticalPath(1.15f, 1.25f, center);
			EllipticalPath outerEllipse = new EllipticalPath(3.5f, 4f, center);

			addStarGroup(players, 0, 2, center, starRadius * 0.9f, innerEllipse, 0.0f, orbitSpeed);
			addStarGroup(players, 2, 2, center, starRadius * 1.2f, outerEllipse, 0.25f, -1f * orbitSpeed);
			break;
		case NOSTARS:
			numStars = 1;
			stars = new ArrayList<Star>(numStars);
			addStaticStar(players, 0, players[0].baseOrigins[0], 0f);
			break;
		} 
	}

	private void addStaticStar(Player[] players, int starIndex, Vector2 pos, float radius) {
		stars.add(starIndex, new Star(Texturez.hexStar, radius, pos, players,
				world, starIndex, 0f));
	}

	private void addPathedStar(Player[] players, int starIndex, Vector2 center, float radius, 
								PathType pathMap, float startPercent, float rotSpeed) {
		
		stars.add(starIndex, new Star(Texturez.hexStar, radius, center, players,
											world, starIndex, pathMap, startPercent, rotSpeed));
		pathedObjList.add(stars.get(starIndex));
	}

	private void addStarGroup(Player[] players, int startingIndex, int groupSize, Vector2 center, float radius, 
														PathType pathMap, float startPercent, float rotSpeed) {
		for (int i = 0; i < groupSize; i++) {
			int indx = startingIndex + i;
			float offset = ((float) i) / ((float) groupSize);
			addPathedStar(players, indx, center, radius, pathMap, offset + startPercent, rotSpeed);
		}
	}

	public void updatePosition(float delta) {
        for (Orbitable aPathedObjList : pathedObjList) {
            aPathedObjList.updatePosition(delta);
        }
	}
}
