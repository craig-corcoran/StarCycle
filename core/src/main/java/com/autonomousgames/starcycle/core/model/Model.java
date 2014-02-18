package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.ModelSettings;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Model {

	public static float gravityScalar = ModelSettings.getFloatSetting("gravScalar");
	
	public static final short starCat = 0x0001;   		// 0000000000000001
	public static final short chargeCat = 0x0002; 		// 0000000000000010
	public static final short p1orbCat = 0x0004;  		// 0000000000000100
	public static final short p2orbCat = 0x0008;  		// 0000000000001000
	public static final short p1voidSensorCat = 0x0010; // 0000000000010000
	public static final short p2voidSensorCat = 0x0020; // 0000000000100000
	
	public static final short starMask = p1orbCat | p2orbCat;
	public static final short chargeMask = p1orbCat | p2orbCat;
	private static final short p1orbMask = starCat | p2voidSensorCat; 
	private static final short p2orbMask = starCat | p1voidSensorCat;
	private static final short p1voidSensorMask = p2orbCat;
	private static final short p2voidSensorMask = p1orbCat;
	
	public static final short[] orbCategoryBits = new short[] {p1orbCat, p2orbCat};
	public static final short[] voidCategoryBits = new short[] {p1voidSensorCat, p2voidSensorCat};
	
	public static final short[] orbMaskBits = new short[] { p1orbMask, p2orbMask };
	public static final short[] voidMaskBits = new short[] { p1voidSensorMask, p2voidSensorMask };
	
	public ArrayList<Orb> toDestroyList = new ArrayList<Orb>(); // and a to destroy list
	public ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	public Player[] players;
	public final ArrayList<Star> stars;
	public final World world;
	public final Level level;
	public Vector2[] starPositions; // keep track of star positions centrally
										// for fast orb updates

	public Model(LevelType lvl, Player[] players) {
		this.players = players;
		world = new World(new Vector2(0, 0), true); // no absolute gravity,
													// sleep if able to
		ContactListener contactListener = new OrbContactListener();
		world.setContactListener(contactListener);

		level = new Level(world, lvl, players);
		stars = level.stars;

		starPositions = new Vector2[stars.size()];
		for (int i = 0; i < starPositions.length; i++) {
			starPositions[i] = stars.get(i).position;
		}
	}

	public void update(float delta) {
		world.step(delta, 6, 2);
		destroyCollided(); // remove collided orbs

		level.updatePosition(delta);
		for (int i = 0; i < stars.size(); i++) {
			starPositions[i].set(stars.get(i).position);
		}
	}

    public void destroyCollided() {
        for (int i=0; i < toDestroyList.size(); i++) {
            Orb orb = toDestroyList.get(i);
            world.destroyBody(orb.body);

            if (orb.type == Orb.OrbType.ORB) {
                orb.player.orbs.remove(orb);
            }
            if (orb.type == Orb.OrbType.NOVA) {
                orb.player.novas.remove(orb);
            }

            if (orb.type == Orb.OrbType.VOID) {
                orb.player.voids.remove(orb);
            }
        }
        toDestroyList.clear();
    }

	// TODO flesh out dispose, anything else?
	public void dispose() {
		world.dispose(); // need to dispose of stars? 
	}
}