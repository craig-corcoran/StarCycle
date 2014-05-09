package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import java.util.*;

// TODO:
// voids and novas should only be able to be used when stars have been captured
// reimplement income orbs with pools
// fix win conditions

public abstract class Model {

    public boolean altWin = false;

    public abstract class WinCondition {
        public abstract int getWinner();
    }

    public final WinCondition winCondition = new WinCondition() {
        @Override
        public int getWinner() {
            if (!altWin) {
                for (Player p : players) {
                    if (p.state.starsControlled == stars.length) {
                        return p.number;
                    }
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

        for (Star st: stars) {
            assert (st.activeOrbs.size() <= st.maxOrbs);
            assert ((st.state.numActiveOrbs[0] + st.state.numActiveOrbs[1]) == st.activeOrbs.size());
        }
    }

    public static final LinkedHashSet<Orb> toRemove = new LinkedHashSet<Orb>(); // add orbs to this to remove them
    public static final int numPlayers = 2;
    public static final int voidStars = (int) ModelSettings.getFloatSetting("voidStars");
    public static final int novaStars = (int) ModelSettings.getFloatSetting("novaStars");

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

    // PlayerActionMessage buffer
    // Whenever we receive state from the server
    // The remaining actions in this buffer after
    // removing what was sent from the server is
    // applied and re-simulated
    public TreeMap<Integer, PlayerState> predictedActionsMap = new TreeMap<Integer, PlayerState>();

    // STATE LOCK
    public Object stateLock = null;

    public Model(LevelType lvl, ModelScreen screen) {

        world = new World(new Vector2(0, 0), true); // no absolute gravity,
        // sleep if able to
        ContactListener contactListener = new OrbContactListener();
        world.setContactListener(contactListener);

        // set up state lock
        this.stateLock = new Object();
        players = initPlayers(screen);
        level = new Level(world, lvl, players);
        state = new GameState(level.numStars, numPlayers);
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
            return new ChargeOrb(player, world);
        }
    }

    public class VoidPool extends Pool {

        final Player player;
        public VoidPool(Player player) {
            this.player = player;
        }
        @Override
        protected Void newObject() {
            return new Void(player, world);
        }
    }

    public class NovaPool extends Pool {
        final Player player;
        public NovaPool(Player player) {
            this.player = player;
        }
        @Override
        protected Nova newObject() {
            return new Nova(player, world);
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

//    final Vector<Integer> objectsForRemoval = new Vector<Integer>();
//    public void setState(GameState state) {
//
//        synchronized (stateLock) {
//
//            for (int i=0; i < numPlayers; i++) {
//                makeOrbsEquivalent(i, this.orbs[i],
//                        state.orbStates[i].keySet(),
//                        this.state.orbStates[i].keySet(),
//                        state);
//                makeVoidsEquivalent(i, this.voids[i],
//                        state.voidStates[i].keySet(),
//                        this.state.voidStates[i].keySet(),
//                        state);
//                makeNovasEquivalent(i, this.novas[i],
//                        state.novaStates[i].keySet(),
//                        this.state.novaStates[i].keySet(),
//                        state);
//            }
//
//
//            // Now in the predictedActionsMap go through and delete the actions that have been seen by the server
//            // Then resimulate by calling update multiple times for each of the user actions left
//            for (Map.Entry<Integer, PlayerState> entry: this.predictedActionsMap.entrySet())
//            {
//                if (entry.getKey() <= state.playerStates[StarCycle.playerNum].playerActionMessageNumber)
//                    objectsForRemoval.add(entry.getKey());
//            }
//            Iterator<Integer> it = objectsForRemoval.iterator();
//            while (it.hasNext())
//            {
//                this.predictedActionsMap.remove(it.next());
//            }
//            objectsForRemoval.clear();
//
//            // now call update on the world for the remaining user actions
//            for (Map.Entry<Integer, PlayerState> entry: this.predictedActionsMap.entrySet())
//            {
//                setPlayerState(entry.getValue());
//                update(true);
//                Gdx.app.log ("StarCycle", "Calling update to fast forward");
//            }
//
//            this.players[StarCycle.playerNum].setMyState(state.playerStates[StarCycle.playerNum]);
//            this.players[(StarCycle.playerNum + 1) % 2].setOpponentState(state.playerStates[(StarCycle.playerNum + 1) % 2]);
//            // Now for the stars. Set the state of the stars received
//            for (int i = 0; i < state.starStates.length; i++)
//            {
//                this.stars[i].setState(state.starStates[i]);
//            }
//
//            this.state.starStates = state.starStates;
//            this.state.frame = state.frame;
//            this.state.orbID = state.orbID;
//            Orb.uidCounter = state.orbID;
//        }
//    }

    public void setPlayerState (PlayerState state)
    {
        synchronized (stateLock)
        {
            for (int i=0; i < this.state.playerStates[state.playerNum].buttonStates.length; i++) {
                this.state.playerStates[state.playerNum].buttonStates[i] = state.buttonStates[i];
            }
            this.players[state.playerNum].base.setPointer(new Vector2(state.pointerX, state.pointerY));
            this.state.playerStates[state.playerNum].numActiveOrbs = state.numActiveOrbs;
            this.state.playerStates[state.playerNum].setPlayerActionMessageNumber(state.playerActionMessageNumber);
        }
    }

    public void setState (GameState state)
    {
        synchronized (stateLock)
        {
            // Debugging
            int differenceInPlayerActionCount = this.state.playerStates[StarCycle.playerNum].playerActionMessageNumber - state.playerStates[StarCycle.playerNum].playerActionMessageNumber;
            Gdx.app.log("StarCycle", "The difference is " + differenceInPlayerActionCount);
            Gdx.app.log("StarCycle", "The server frame number is " + state.frame);
            Gdx.app.log("StarCycle", "The client frame number is " + this.state.frame);
            // if an orb, nova or void already exists, we update it
            // if there is a new one, we need to create it
            // if we realize an orb is dead we remove it
            // Now the other way around remove Orbs that are in the old state but not in the new state
            // This means that those orbs are dead, and need to be removed.
            Vector<Integer> objectsForRemoval = new Vector<Integer>();
            for (int i = 0; i< numPlayers; i++) {
                for (Map.Entry<Integer, ChargeOrb> entry: this.orbs[i].entrySet())
                {
                    ChargeOrbState cos = state.orbStates[i].get(entry.getKey());
                    if ((cos==null) || ((entry.getValue().predicted==true)&&(entry.getValue().playerActionMessageNumberWhenCreated < state.playerStates[StarCycle.playerNum].playerActionMessageNumber + 2)))
                    {
                        //this.orbs[i].remove(entry.getKey());
                        // note all the objects for removal. We need to come up with a more
                        // optimal method
                        objectsForRemoval.add(entry.getKey());
                    }
                }

                // Now iterate through the vector and remove the objects from the map
                Iterator<Integer> it = objectsForRemoval.iterator();
                while (it.hasNext())
                {
                    this.orbs[i].remove(it.next());
                }

                // Done with one type of objects. Clear the vector
                objectsForRemoval.clear();

                for (Map.Entry<Integer, Nova> entry: this.novas[i].entrySet())
                {
                    OrbState os = state.novaStates[i].get(entry.getKey());
                    if ((os==null) ||((entry.getValue().predicted==true)&& (entry.getValue().playerActionMessageNumberWhenCreated < state.playerStates[StarCycle.playerNum].playerActionMessageNumber + 2)))
                    {
                        //this.novas[i].remove(entry.getKey());
                        objectsForRemoval.add(entry.getKey());
                    }
                }

                // Now iterate through the vector and remove the objects from the map
                it = objectsForRemoval.iterator();
                while (it.hasNext())
                {
                    this.novas[i].remove(it.next());
                }

                // Done with one type of objects. Clear the vector
                objectsForRemoval.clear();

                for (Map.Entry<Integer, Void> entry: this.voids[i].entrySet())
                {
                    ChargeOrbState cos = state.voidStates[i].get(entry.getKey());
                    if ((cos==null) || ((entry.getValue().predicted==true)&& (entry.getValue().playerActionMessageNumberWhenCreated < state.playerStates[StarCycle.playerNum].playerActionMessageNumber + 2)))
                    {
                        //this.voids[i].remove(entry.getKey());
                        objectsForRemoval.add(entry.getKey());
                    }
                }

                // Now iterate through the vector and remove the objects from the map
                it = objectsForRemoval.iterator();
                while (it.hasNext())
                {
                    this.voids[i].remove(it.next());
                }

                // Done!
                // Done with one type of objects. Clear the vector
                objectsForRemoval.clear();

                for (Map.Entry<Integer, ChargeOrbState> entry: state.orbStates[i].entrySet())
                {
                    ChargeOrb orb = orbs[i].get(entry.getKey());
                    if (orb!=null)
                    {
                        orb.init(entry.getValue());
                    }
                    else
                    {
                        addOrb(i, ChargeOrb.class, entry.getValue().x, entry.getValue().y, entry.getValue().v, entry.getValue().w, entry.getValue().uid, false);
                        // TODO: See what needs to happen here
                    }
                }

                for (Map.Entry<Integer, OrbState> entry: state.novaStates[i].entrySet())
                {
                    Nova nova = novas[i].get(entry.getKey());
                    if (nova!=null)
                    {
                        nova.init(entry.getValue());
                    }
                    else
                    {
                        addOrb(i, Nova.class, entry.getValue().x, entry.getValue().y, entry.getValue().v, entry.getValue().w, entry.getValue().uid, false);
                    }
                }

                for (Map.Entry<Integer, ChargeOrbState> entry: state.voidStates[i].entrySet())
                {
                    Void orb = voids[i].get(entry.getKey());
                    if (orb!=null)
                    {
                        orb.init(entry.getValue());
                    }
                    else
                    {
                        addOrb(i, Void.class, entry.getValue().x, entry.getValue().y, entry.getValue().v, entry.getValue().w, entry.getValue().uid, false);
                    }
                }
            }
            this.players[StarCycle.playerNum].setMyState(state.playerStates[StarCycle.playerNum]);
            this.players[(StarCycle.playerNum + 1) % 2].setOpponentState(state.playerStates[(StarCycle.playerNum + 1) % 2]);
            // Now for the stars. Set the state of the stars received
            for (int i = 0; i < state.starStates.length; i++)
            {
                this.stars[i].setState(state.starStates[i]);
            }

            this.state.orbStates = state.orbStates;
            this.state.novaStates = state.novaStates;
            this.state.voidStates = state.voidStates;
            this.state.frame = state.frame;
            this.state.orbID = state.orbID;
            this.state.starStates = state.starStates;

            // Now in the predictedActionsMap go through and delete the actions that have been seen by the server
            // Then resimulate by calling update multiple times for each of the user actions left
            for (Map.Entry<Integer, PlayerState> entry: this.predictedActionsMap.entrySet())
            {
                if (entry.getKey() <= state.playerStates[StarCycle.playerNum].playerActionMessageNumber)
                    objectsForRemoval.add(entry.getKey());
            }
            Iterator<Integer> it = objectsForRemoval.iterator();
            while (it.hasNext())
            {
                this.predictedActionsMap.remove(it.next());
            }
            objectsForRemoval.clear();

            // now call update on the world for the remaining user actions
            for (Map.Entry<Integer, PlayerState> entry: this.predictedActionsMap.entrySet())
            {
                setPlayerState(entry.getValue());
                update(true);
                Gdx.app.log ("StarCycle", "Calling update to fast forward");
            }
        }
    }

    public void launch(Player player, Class cls, boolean predicted) {
        synchronized (stateLock)
        {
            float cost = orbCosts.get(cls);
            if (player.state.ammo >= cost) {
                player.state.ammo -= cost;
                Vector2 _pos = new Vector2(player.state.pointerX, player.state.pointerY);
                _pos.nor().scl(0.3f * player.base.baseDiams[player.base.level]);
                _pos.add(player.base.origin);
                addOrb(player.number, cls, _pos.x, _pos.y, player.state.pointerX, player.state.pointerY, predicted);
            }
        }
    }

    public void update(boolean orbsPredicted) {
        synchronized (stateLock) {
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

                if (p.state.buttonStates[0] && p.launchPad.sinceLastShot >= coolDown) {
                    launch(p, ChargeOrb.class, orbsPredicted);
                    p.launchPad.sinceLastShot=0f;
                }
                if (p.state.buttonStates[1] && p.launchPad.sinceLastShot >= coolDown/3f) {
                    launch(p, Void.class, orbsPredicted);
                    p.launchPad.sinceLastShot=0f;
                }
                if (p.state.buttonStates[2] && p.launchPad.sinceLastShot >= coolDown/3f) {
                    launch(p, Nova.class, orbsPredicted);
                    p.launchPad.sinceLastShot=0f;
                }
            }
            level.updatePosition(dt); // XXX stars updated here?
            state.orbID = Orb.uidCounter;
            state.frame++;
//            setState(this.state); // TODO remove; testing
//            checkStateConsistent(this.state);
        }
    }

    void incrementCounters(int playerNum, Class cls) {
        synchronized (stateLock)
        {
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
    }

    /**
     * Adds an orb of the given class type with the given uid to appropriate player's lists. Does
     * not initialize the state of the orb.
     * @param playerNum 0-base integer uniquely identifying the player
     * @param cls class of created orb (ChargeOrb, nova, void)
     * @param uid unique id for the given orb, used as key in HashMaps
     */
    void addOrb(int playerNum, Class cls, int uid) {
        synchronized (stateLock)
        {
            if (cls == ChargeOrb.class) {
                ChargeOrb orb = orbPools[playerNum].obtain();
                orb.state.uid = uid;
                orbs[playerNum].put(uid, orb);
                state.orbStates[playerNum].put(uid, (ChargeOrbState) orb.state);
                totalOrbs[playerNum]++;
            }
            else if (cls == Void.class) {
                Void vd = voidPools[playerNum].obtain();
                vd.state.uid = uid;
                voids[playerNum].put(uid, vd);
                state.voidStates[playerNum].put(uid, (ChargeOrbState) vd.state);
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
//    public Orb addOrb(int playerNum, Class cls, float x, float y, float v, float w) {
//        incrementCounters(playerNum, cls);
//        if (cls == ChargeOrb.class) {
//            ChargeOrb orb = orbPools[playerNum].obtain();
//            orb.init(x,y,v,w);
//            orbs[playerNum].put(orb.state.uid, orb);
//            this.state.orbStates[playerNum].put(orb.state.uid, (ChargeOrb.ChargeOrbState) orb.state);
//            totalOrbs[playerNum]++;
//            return orb;
//        }
//        else if (cls == Void.class) {
//            Void vd = voidPools[playerNum].obtain();
//            vd.init(x,y,v,w);
//            voids[playerNum].put(vd.state.uid, vd);
//            this.state.voidStates[playerNum].put(vd.state.uid, (ChargeOrb.ChargeOrbState) vd.state);
//            totalVoids[playerNum]++;
//            return vd;
    public Orb addOrb(int playerNum, Class cls, float x, float y, float v, float w, boolean predicted) {
        synchronized (stateLock)
        {
            incrementCounters(playerNum, cls);
            if (cls == ChargeOrb.class) {
                ChargeOrb orb = orbPools[playerNum].obtain();
                orb.init(x,y,v,w); // make sure its calling init w ChargeOrbState not OrbState
                orb.predicted = predicted;
                orb.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                orbs[playerNum].put(orb.state.uid, orb);
                this.state.orbStates[playerNum].put(orb.state.uid, (ChargeOrbState) orb.state);
                totalOrbs[playerNum]++;
                return orb;
            }
            else if (cls == Void.class) {
                Void vd = voidPools[playerNum].obtain();
                vd.init(x,y,v,w);
                vd.predicted = predicted;
                vd.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                voids[playerNum].put(vd.state.uid, vd);
                this.state.voidStates[playerNum].put(vd.state.uid, (ChargeOrbState) vd.state);
                totalVoids[playerNum]++;
                return vd;
            }
            else {
                Nova nova = novaPools[playerNum].obtain();
                nova.init(x,y,v,w);
                nova.predicted = predicted;
                novas[playerNum].put(nova.state.uid, nova);
                this.state.novaStates[playerNum].put(nova.state.uid, nova.state);
                nova.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                totalNovas[playerNum]++;
                return nova;
            }
        }
    }

    public Orb addOrb(int playerNum, Class cls, float x, float y, float v, float w, int uid, boolean predicted) {
        synchronized (stateLock)
        {
            incrementCounters(playerNum, cls);
            if (cls == ChargeOrb.class) {
                ChargeOrb orb = orbPools[playerNum].obtain();
                orb.init(x,y,v,w); // make sure its calling init w ChargeOrbState not OrbState
                orb.state.uid = uid;
                orb.predicted = predicted;
                orb.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                orbs[playerNum].put(orb.state.uid, orb);
                this.state.orbStates[playerNum].put(orb.state.uid, (ChargeOrbState) orb.state);
                totalOrbs[playerNum]++;
                return orb;
            }
            else if (cls == Void.class) {
                Void vd = voidPools[playerNum].obtain();
                vd.init(x,y,v,w);
                vd.state.uid = uid;
                vd.predicted = predicted;
                vd.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                voids[playerNum].put(vd.state.uid, vd);
                this.state.voidStates[playerNum].put(vd.state.uid, (ChargeOrbState) vd.state);
                totalVoids[playerNum]++;
                return vd;
            }
            else {
                Nova nova = novaPools[playerNum].obtain();
                nova.init(x,y,v,w);
                nova.state.uid = uid;
                nova.predicted = predicted;
                nova.playerActionMessageNumberWhenCreated = this.state.playerStates[playerNum].playerActionMessageNumber;
                novas[playerNum].put(nova.state.uid, nova);
                this.state.novaStates[playerNum].put(nova.state.uid, nova.state);
                totalNovas[playerNum]++;
                return nova;
            }
        }
    }

    void removeOrb(Orb o) {
        synchronized (stateLock)
        {
            if (o.getClass() == ChargeOrb.class) {

                if (((ChargeOrbState) o.state).lockedOn) {
                    stars[((ChargeOrbState) o.state).star].removeOrb((ChargeOrb) o);
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
    }

    public void removeOrbs() {
        synchronized (stateLock)
        {
            for (Orb o: toRemove) {
                removeOrb(o);
            }
            toRemove.clear();
        }
    }

    // TODO flesh out dispose, anything else?
    public void dispose() {
        synchronized (stateLock)
        {
            world.dispose(); // need to dispose of stars?
            toRemove.clear();
        }
    }
}