package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;

import java.io.Serializable;
import java.util.*;

public abstract class Model {

    public class GameState implements Serializable {

        // TODO mutex lock

        int frame = 0;
        int orbID = 0;
        final LinkedHashMap<Integer, ChargeOrb.ChargeOrbState>[] orbStates = new LinkedHashMap[numPlayers];
        final LinkedHashMap<Integer,ChargeOrb.ChargeOrbState>[] voidStates = new LinkedHashMap[numPlayers];
        final LinkedHashMap<Integer,Orb.OrbState>[] novaStates = new LinkedHashMap[numPlayers];
        final Player.PlayerState[] playerStates = new Player.PlayerState[numPlayers];
        final Star.StarState[] starStates;

        GameState(int numStars) {

            starStates = new Star.StarState[numStars];

            for (int i=0; i < numPlayers; i++) {
                orbStates[i] = new LinkedHashMap<Integer,ChargeOrb.ChargeOrbState>(500);
                voidStates[i] = new LinkedHashMap<Integer,ChargeOrb.ChargeOrbState>(50);
                novaStates[i] = new LinkedHashMap<Integer,Orb.OrbState>(50);
            }
        }
    }

    public abstract class WinCondition {
        public abstract int getWinner();
    }

    public final WinCondition winCondition = new WinCondition() {
        @Override
        public int getWinner() {
            for (Player p : players) {
                if (p.state.starsControlled == level.numStars) {
                    return p.number;
                }
            }
            return -1;
        }
    };

    public void checkStateConsistent(GameState state) {

    }

    public int[] totalOrbs = new int[numPlayers];
    public int[] totalVoids = new int[numPlayers];
    public int[] totalNovas = new int[numPlayers];

    static final float dt = 1/30f;
	//static final float gravityScalar = ModelSettings.getFloatSetting("gravScalar");
	static final short starCat = 0x0001;   		// 0000000000000001
	static final short p1orbCat = 0x0004;  		// 0000000000000100
	static final short p2orbCat = 0x0008;  		// 0000000000001000
	static final short p1voidSensorCat = 0x0010; // 0000000000010000
	static final short p2voidSensorCat = 0x0020; // 0000000000100000
	
	static final short starMask = p1orbCat | p2orbCat;
	static final short p1orbMask = starCat | p2voidSensorCat;
	static final short p2orbMask = starCat | p1voidSensorCat;
	static final short p1voidSensorMask = p2orbCat;
	static final short p2voidSensorMask = p1orbCat;
	
	static final short[] orbCategoryBits = new short[] {p1orbCat, p2orbCat};
	static final short[] voidCategoryBits = new short[] {p1voidSensorCat, p2voidSensorCat};

	static final short[] orbMaskBits = new short[] { p1orbMask, p2orbMask };
	static final short[] voidMaskBits = new short[] { p1voidSensorMask, p2voidSensorMask };

    static final HashMap<Class,Float> orbCosts = new HashMap<Class,Float>(3);

    public static final LinkedHashSet<Orb> toRemove = new LinkedHashSet<Orb>(); // add orbs to this to remove them
    public static final int numPlayers = 2;

    public final LinkedHashMap<Integer,ChargeOrb>[] orbs = new LinkedHashMap[numPlayers];
    public final LinkedHashMap<Integer,Void>[] voids = new LinkedHashMap[numPlayers];
    public final LinkedHashMap<Integer,Nova>[] novas = new LinkedHashMap[numPlayers];
	public final Player[] players;
    public final Star[] stars;

    final Pool<ChargeOrb>[] orbPools = new OrbPool[numPlayers];
    final Pool<Void>[] voidPools = new VoidPool[numPlayers];
    final Pool<Nova>[] novaPools = new NovaPool[numPlayers];

    public final GameState state;

	public final World world;
	public final Level level;

	public Model(LevelType lvl, ModelScreen screen) {

		world = new World(new Vector2(0, 0), true); // no absolute gravity,
													// sleep if able to
		ContactListener contactListener = new OrbContactListener();
		world.setContactListener(contactListener);


        players = initPlayers(screen);
		level = new Level(world, lvl, players);
        state = new GameState(level.numStars);
        stars = level.stars;
        //this.players = players;


        for (int i=0; i < Model.numPlayers; i++) {
            if (players[i] instanceof Bot) {
                ((Bot) players[i]).initializeModel(this);
            }
        }

        initState();
        setCosts();

        for (Player p: players){
            orbPools[p.number] = new OrbPool(p);
            voidPools[p.number] = new VoidPool(p);
            novaPools[p.number] = new NovaPool(p);

            orbs[p.number] = new LinkedHashMap<Integer,ChargeOrb>(500);
            voids[p.number] = new LinkedHashMap<Integer,Void>(50);
            novas[p.number] = new LinkedHashMap<Integer,Nova>(50);
        }
	}

    public abstract Player[] initPlayers(ModelScreen screen);

    void initState() {
        for (Star star: stars) {
            this.state.starStates[star.state.index] = star.state;
        }
        for (Player player: players) {
            this.state.playerStates[player.number] = player.state;
        }
    }

    public static void setCosts() {
        orbCosts.put(ChargeOrb.class, ModelSettings.getFloatSetting("orbCost"));
        orbCosts.put(Void.class, ModelSettings.getFloatSetting("voidCosts"));
        orbCosts.put(Nova.class, ModelSettings.getFloatSetting("novaCosts"));
    }

    public static void setCosts(float o, float v, float n) {
        orbCosts.put(ChargeOrb.class, o);
        orbCosts.put(Void.class, v);
        orbCosts.put(Nova.class, n);
    }

    public class OrbPool extends Pool {

        final Player player;
        public OrbPool(Player player) {
            this.player = player;
        }

        @Override
        protected ChargeOrb newObject() {
            ChargeOrb orb = new ChargeOrb(player, world);
            //orbs[player.number].put(orb.uid, orb);
            //state.orbStates[player.number].add(orb.state);
            return orb;
        }
    }

    public class VoidPool extends Pool {

        final Player player;
        public VoidPool(Player player) {
            this.player = player;
        }
        @Override
        protected Void newObject() {
            Void vd = new Void(player, world);
            voids[player.number].put(vd.uid, vd);
            state.voidStates[player.number].put(vd.uid, vd.state);
            return vd;
        }
    }

    public class NovaPool extends Pool {
        final Player player;
        public NovaPool(Player player) {
            this.player = player;
        }
        @Override
        protected Nova newObject() {
            Nova nova = new Nova(player, world);
            novas[player.number].put(nova.uid, nova);
            state.novaStates[player.number].put(nova.uid, nova.state);
            return nova;
        }
    }

    void makeEquivalent(int playerNum, LinkedHashMap orbMap, LinkedHashSet<Integer> newSet, LinkedHashSet<Integer> oldSet) {
        HashSet<Integer> newOrbs = new HashSet<Integer>(newSet);
        HashSet<Integer> deadOrbs = new HashSet<Integer>(oldSet);

        newOrbs.removeAll(oldSet);
        deadOrbs.removeAll(newOrbs);

        // remove dead orbs
        Iterator<Integer> itDead = deadOrbs.iterator();
        while (itDead.hasNext()) {
            Integer uid = itDead.next();
            removeOrb((Orb)orbMap.get(uid));
        }

        // add new ones
        Iterator<Integer> itNew = newOrbs.iterator();
        while (itNew.hasNext()) {
            Integer uid = itNew.next();
            addOrb(playerNum, orbMap.values().getClass(), uid); // does not intialize other state vars than uid
        }

        // make sure orbs are active and overwrite their state (init)
        Iterator<Integer> it = orbMap.keySet().iterator();
        while (it.hasNext()) {
            Integer uid = it.next();
            Orb.OrbState st = state.orbStates[playerNum].get(uid);
            ((Orb) orbMap.get(uid)).init(st); // XXX make init that takes state inf and/or make init in chargeorb
            // XXX does this affect state.orbStates?
        }
    }

    public void setState(GameState state) { // XXX

        for (int i=0; i < numPlayers; i++) {
            makeEquivalent(i, this.orbs[i],
                    (LinkedHashSet)state.orbStates[i].keySet(),
                    (LinkedHashSet)this.state.orbStates[i].keySet());
            makeEquivalent(i, this.voids[i],
                    (LinkedHashSet)state.novaStates[i].keySet(),
                    (LinkedHashSet)this.state.novaStates[i].keySet());
            makeEquivalent(i, this.novas[i],
                    (LinkedHashSet)state.novaStates[i].keySet(),
                    (LinkedHashSet)this.state.novaStates[i].keySet());
            players[i].setState(state.playerStates[i]);
        }

        for (Star.StarState starSt: state.starStates) {
            this.stars[starSt.index].setState(starSt);
        }

        this.state.frame = state.frame;
        this.state.orbID = state.orbID;
        Orb.uid = this.state.orbID;

    }

	public void update() {
		world.step(dt, 6, 2); // check for collisions
        removeOrbs(); // remove collided orbs

        for (Player p: players) {
            Iterator<ChargeOrb> orbIt = orbs[p.number].values().iterator();
            while (orbIt.hasNext()){
                orbIt.next().update(stars);
            }

            Iterator<Void> voidIt = voids[p.number].values().iterator();
            while (voidIt.hasNext()) {
                voidIt.next().update(stars);
            }

            Iterator<Nova> novaIt = novas[p.number].values().iterator();
            while (novaIt.hasNext()) {
                novaIt.next().update(stars);
            }

            p.update(stars); // XXX
        }
        level.updatePosition(dt); // XXX stars updated here?

        state.orbID = Orb.uid;
        state.frame++;
	}


    void incrementCounters(int playerNum, Class cls) {
        if (cls == ChargeOrb.class) {
            totalOrbs[playerNum]++;
        }
        if (cls == Void.class) {
            totalVoids[playerNum]++;
        }
        if (cls == Nova.class) {
            totalNovas[playerNum]++;
        }
    }

    /**
     * Adds an orb of the given class type with the given uid to appropriate player's lists. Does
     * not initialize the state of the orb.
     * @param playerNum 0-base integer uniquely identifying the player
     * @param cls class of created orb (ChargeOrb, nova, void)
     * @param uid unique id for the given orb, used as key in HashMaps
     */
    void addOrb(int playerNum, Class cls, int uid) {
        if (cls == ChargeOrb.class) {
            ChargeOrb orb = orbPools[playerNum].obtain();
            orb.uid = uid;
            orbs[playerNum].put(uid, orb);
            state.orbStates[playerNum].put(uid, orb.state);
        }
        else if (cls == Void.class) {
            Void vd = voidPools[playerNum].obtain();
            vd.uid = uid;
            voids[playerNum].put(uid, vd);
            state.voidStates[playerNum].put(uid, vd.state);
        }
        else {
            Nova nova = novaPools[playerNum].obtain();
            nova.uid = uid;
            novas[playerNum].put(uid, nova);
            state.novaStates[playerNum].put(uid, nova.state);
        }
        incrementCounters(playerNum, cls);
    }

    /**
     * Adds an orb for the given player of the given class and sets the orbs position and velocity
     * @param playerNum 0-base integer uniquely identifying the player
     * @param cls class of created orb (ChargeOrb, Nova, Void)
     * @param x pos.x
     * @param y pos.y
     * @param v vel.x
     * @param w vel.y
     */
    public Orb addOrb(int playerNum, Class cls, float x, float y, float v, float w) {
        incrementCounters(playerNum, cls);
        if (cls == ChargeOrb.class) {
            ChargeOrb orb = orbPools[playerNum].obtain();
            orb.init(x,y,v,w); // make sure its calling init w ChargeOrbState not OrbState
            orbs[playerNum].put(orb.uid, orb);
            this.state.orbStates[playerNum].put(orb.uid, orb.state);
            return orb;
        }
        else if (cls == Void.class) {
            Void vd = voidPools[playerNum].obtain();
            vd.init(x,y,v,w);
            voids[playerNum].put(vd.uid, vd);
            this.state.voidStates[playerNum].put(vd.uid, vd.state);
            return vd;
        }
        else {
            Nova nova = novaPools[playerNum].obtain();
            nova.init(x,y,v,w);
            novas[playerNum].put(nova.uid, nova);
            this.state.novaStates[playerNum].put(nova.uid, nova.state);
            return nova;
        }
    }

    /**
     * Adds an orb for the given player of the given class and copies the OrbState onto the state of the new orb object,
     * including uid, which is used as the key.
     * @param playerNum 0-base integer uniquely identifying the player
     * @param cls class of created orb (ChargeOrb, Nova, Void)
     * @param state state of the new orb
     */
    public void addOrb(int playerNum, Class cls, Orb.OrbState state) {
        if (cls == ChargeOrb.class) {
            ChargeOrb orb = orbPools[playerNum].obtain();
            orb.init((ChargeOrb.ChargeOrbState)state); // make sure its calling init w ChargeOrbState not OrbState
            orbs[playerNum].put(orb.uid, orb);
            this.state.orbStates[playerNum].put(orb.uid, orb.state);
        }
        else if (cls == Void.class) {
            Void vd = voidPools[playerNum].obtain();
            vd.init(state);
            voids[playerNum].put(vd.uid, vd);
            this.state.voidStates[playerNum].put(vd.uid, vd.state);
        }
        else {
            Nova nova = novaPools[playerNum].obtain();
            nova.init(state);
            novas[playerNum].put(nova.uid, nova);
            this.state.novaStates[playerNum].put(nova.uid, nova.state);
        }
        incrementCounters(playerNum, cls);
    }

    void removeOrb(Orb o) {
        if (o.getClass() == ChargeOrb.class) {

            if (((ChargeOrb)o).state.lockedOn) {
                stars[((ChargeOrb)o).state.star].removeOrb((ChargeOrb) o);
            }

            state.orbStates[o.playerNum].remove(o.state.uid);
            orbs[o.playerNum].remove(o.uid);
            orbPools[o.playerNum].free((ChargeOrb)o);
        }
        else if (o.getClass() == Nova.class) {
            state.novaStates[o.playerNum].remove(o.state.uid);
            novas[o.playerNum].remove(o.uid);
            novaPools[o.playerNum].free((Nova)o);
        }
        else {
            state.voidStates[o.playerNum].remove(o.state.uid);
            voids[o.playerNum].remove(o.uid);
            voidPools[o.playerNum].free((Void)o);
        }
    }

    public void removeOrbs() {
        for (Orb o: toRemove) {
            removeOrb(o);
        }
        toRemove.clear();
    }

	// TODO flesh out dispose, anything else?
	public void dispose() {
		world.dispose(); // need to dispose of stars?
        toRemove.clear();
	}
}