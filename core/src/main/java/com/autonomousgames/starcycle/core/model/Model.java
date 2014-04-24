package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import java.io.Serializable;
import java.util.*;

// TODO:
// voids and novas should only be able to be used when stars have been captured
// reimplement income orbs with pools
// fix drawing for stars
// fix win conditions

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
                if (p.state.starsControlled == stars.length) {
                    return p.number;
                }
            }
            return -1;
        }
    };

    public void checkStateConsistent(GameState state) {

        for (Player p: players) {
            int activePl = p.state.numActiveOrbs;
            int activeSt = 0;
            for (Star s: stars) {
                activeSt += s.state.numActiveOrbs[p.number];
            }
            assert (activePl == activeSt);
            assert (totalOrbs[p.number] == state.orbStates[p.number].size());
            assert (totalVoids[p.number] == state.voidStates[p.number].size());
            assert (totalNovas[p.number] == state.novaStates[p.number].size());
        }
    }

    public int[] totalOrbs = new int[numPlayers];
    public int[] totalVoids = new int[numPlayers];
    public int[] totalNovas = new int[numPlayers];

    static final float dt = 1/30f;
    static final float coolDown = ModelSettings.getFloatSetting("coolDown");
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

	public Model(LevelType lvl, ModelScreen screen) {

		world = new World(new Vector2(0, 0), true); // no absolute gravity,
													// sleep if able to
		ContactListener contactListener = new OrbContactListener();
		world.setContactListener(contactListener);


        players = initPlayers(screen);
		Level level = new Level(world, lvl, players);
        state = new GameState(level.numStars);
        stars = level.stars; // TODO clean up level / Model interface
        initState();
        setCosts();

        for (Player p: players){
            orbPools[p.number] = new OrbPool(p);
            voidPools[p.number] = new VoidPool(p);
            novaPools[p.number] = new NovaPool(p);

            orbs[p.number] = new LinkedHashMap<Integer, ChargeOrb>(500);
            voids[p.number] = new LinkedHashMap<Integer, Void>(50);
            novas[p.number] = new LinkedHashMap<Integer, Nova>(50);
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
        orbCosts.put(Void.class, ModelSettings.getFloatSetting("voidCost"));
        orbCosts.put(Nova.class, ModelSettings.getFloatSetting("novaCost"));
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
            return nova;
        }
    }

    void makeOrbsEquivalent(int playerNum, LinkedHashMap<Integer, ChargeOrb> orbMap, Set<Integer> newSet, Set<Integer> oldSet, GameState newState) {
        LinkedList<Integer> toRemove = makeSetEquivalent(playerNum, newSet, oldSet, orbMap.values().getClass());
        for (Integer ii: toRemove) {
            removeOrb(orbMap.get(ii));
        }

        for (Map.Entry<Integer, ChargeOrb> entry: orbMap.entrySet()) {
            entry.getValue().init(newState.orbStates[playerNum].get(entry.getKey()));
        }
    }

    void makeVoidsEquivalent(int playerNum, LinkedHashMap<Integer, Void> orbMap, Set<Integer> newSet, Set<Integer> oldSet, GameState newState) {
        LinkedList<Integer> toRemove = makeSetEquivalent(playerNum, newSet, oldSet, orbMap.values().getClass());
        for (Integer ii: toRemove) {
            removeOrb(orbMap.get(ii));
        }

        for (Map.Entry<Integer, Void> entry: orbMap.entrySet()) {
            entry.getValue().init(newState.voidStates[playerNum].get(entry.getKey()));
        }
    }

    void makeNovasEquivalent(int playerNum, LinkedHashMap<Integer, Nova> orbMap, Set<Integer> newSet, Set<Integer> oldSet, GameState newState) {
        LinkedList<Integer> toRemove = makeSetEquivalent(playerNum, newSet, oldSet, orbMap.values().getClass());
        for (Integer ii: toRemove) {
            removeOrb(orbMap.get(ii));
        }

        for (Map.Entry<Integer, Nova> entry: orbMap.entrySet()) {
            entry.getValue().init(newState.novaStates[playerNum].get(entry.getKey()));
        }
    }

    LinkedList<Integer> makeSetEquivalent(int playerNum, Set<Integer> newSet, Set<Integer> oldSet, Class cls) {
        HashSet<Integer> newOrbs = new HashSet<Integer>(newSet);
        HashSet<Integer> deadOrbs = new HashSet<Integer>(oldSet);

        newOrbs.removeAll(oldSet);
        deadOrbs.removeAll(newSet);

        LinkedList<Integer> toRemove = new LinkedList<Integer>();

        // add dead orbs to remove list
        Iterator<Integer> itDead = deadOrbs.iterator();
        while (itDead.hasNext()) {
            toRemove.add(itDead.next());
        }

        // add new ones
        Iterator<Integer> itNew = newOrbs.iterator();
        while (itNew.hasNext()) {
            Integer uid = itNew.next();
            addOrb(playerNum, cls, uid); // does not intialize other state vars than uidCounter
        }

        return toRemove;
    }

    public void setState(GameState state) {

        for (int i=0; i < numPlayers; i++) {
            makeOrbsEquivalent(i, this.orbs[i],
                    state.orbStates[i].keySet(),
                    this.state.orbStates[i].keySet(),
                    state);
            makeVoidsEquivalent(i, this.voids[i],
                    state.voidStates[i].keySet(),
                    this.state.voidStates[i].keySet(),
                    state);
            makeNovasEquivalent(i, this.novas[i],
                    state.novaStates[i].keySet(),
                    this.state.novaStates[i].keySet(),
                    state);
            players[i].setState(state.playerStates[i]);
        }

        for (Star.StarState starSt: state.starStates) {
            this.stars[starSt.index].setState(starSt);
        }

        this.state.frame = state.frame;
        this.state.orbID = state.orbID;
        Orb.uidCounter = state.orbID;

    }

    //Vector2 _pos = new Vector2();
    public void launch(Player player, Class cls) {
        float cost = orbCosts.get(cls);
        if (player.state.ammo >= cost) {
            player.state.ammo -= cost;
            Vector2 _pos = new Vector2(player.state.pointerX, player.state.pointerY);
            _pos.nor().scl(0.3f * player.base.baseDiams[player.base.level]);
            _pos.add(player.base.origin);
            addOrb(player.number, cls, _pos.x, _pos.y, player.state.pointerX, player.state.pointerY);
        }
    }

	public void update() {
		world.step(dt, 6, 2); // check for collisions
        removeOrbs(); // remove collided orbs

        for (Star star: stars) {
            star.update();
        }

        // update orbs and player state
        for (Player p: players) {

            for (Orb o: orbs[p.number].values()) {
                o.update(stars);
            }
            for (Void o: voids[p.number].values()) {
                o.update(stars);
            }
            for (Nova o: novas[p.number].values()) {
                o.update(stars);
            }

            p.update(stars);

            if ((p.state.buttonStates[0] == true) & (p.launchPad.sinceLastShot >= coolDown)) {
                launch(p, ChargeOrb.class);
                p.launchPad.sinceLastShot=0f;
            }
            if (p.state.buttonStates[1] == true) {
                launch(p, Void.class);
                p.state.buttonStates[1] = false;
            }
            if (p.state.buttonStates[2] == true) {
                launch(p, Nova.class);
                p.state.buttonStates[2] = false;
            }
        }


        state.orbID = Orb.uidCounter;
        state.frame++;

        setState(this.state); // TODO remove; testing
        checkStateConsistent(this.state);
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
            orb.state.uid = uid;
            orbs[playerNum].put(uid, orb);
            state.orbStates[playerNum].put(uid, (ChargeOrb.ChargeOrbState) orb.state);
            totalOrbs[playerNum]++;
        }
        else if (cls == Void.class) {
            Void vd = voidPools[playerNum].obtain();
            vd.state.uid = uid;
            voids[playerNum].put(uid, vd);
            state.voidStates[playerNum].put(uid, (ChargeOrb.ChargeOrbState) vd.state);
            totalVoids[playerNum]++;
        }
        else {
            Nova nova = novaPools[playerNum].obtain();
            nova.state.uid = uid;
            novas[playerNum].put(uid, nova);
            state.novaStates[playerNum].put(uid, nova.state);
            totalNovas[playerNum]++;
        }
    }

    /**
     * Adds an orb of the given class for the given player and sets the orbs position and velocity
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
            orb.init(x,y,v,w);
            orbs[playerNum].put(orb.state.uid, orb);
            this.state.orbStates[playerNum].put(orb.state.uid, (ChargeOrb.ChargeOrbState) orb.state);
            totalOrbs[playerNum]++;
            return orb;
        }
        else if (cls == Void.class) {
            Void vd = voidPools[playerNum].obtain();
            vd.init(x,y,v,w);
            voids[playerNum].put(vd.state.uid, vd);
            this.state.voidStates[playerNum].put(vd.state.uid, (ChargeOrb.ChargeOrbState) vd.state);
            totalVoids[playerNum]++;
            return vd;
        }
        else {
            Nova nova = novaPools[playerNum].obtain();
            nova.init(x,y,v,w);
            novas[playerNum].put(nova.state.uid, nova);
            this.state.novaStates[playerNum].put(nova.state.uid, nova.state);
            totalNovas[playerNum]++;
            return nova;
        }
    }

    void removeOrb(Orb o) {
        if (o.getClass() == ChargeOrb.class) {

            if (((ChargeOrb.ChargeOrbState) o.state).lockedOn) {
                stars[((ChargeOrb.ChargeOrbState) o.state).star].removeOrb((ChargeOrb) o);
            }

            state.orbStates[o.playerNum].remove(o.state.uid);
            orbs[o.playerNum].remove(o.state.uid);

            assert (!orbs[o.playerNum].containsValue(o));
            assert (!orbs[o.playerNum].containsKey(o.state.uid)); // TODO remove eventually, add to debug?

            orbPools[o.playerNum].free((ChargeOrb)o);
            totalOrbs[o.playerNum]--;
        }
        else if (o.getClass() == Nova.class) {
            state.novaStates[o.playerNum].remove(o.state.uid);
            novas[o.playerNum].remove(o.state.uid);
            novaPools[o.playerNum].free((Nova)o);
            totalNovas[o.playerNum]--;
        }
        else {
            state.voidStates[o.playerNum].remove(o.state.uid);
            voids[o.playerNum].remove(o.state.uid);
            voidPools[o.playerNum].free((Void)o);
            totalVoids[o.playerNum]--;
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