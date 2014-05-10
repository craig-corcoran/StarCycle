package com.autonomousgames.starcycle.core.screens;


import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial1 extends Tutorial {

    LayeredButton transStar0;
    LayeredButton transStar1;
    LayeredButton fakeLaunch;
    LayeredButton fakeMaluma;
    LayeredButton fakeAim0;
    LayeredButton fakeTakete;
    LayeredButton fakeAim1;
    LayeredButton orbit;

    LayeredButton controlText;
    LayeredButton topStar0;
    LayeredButton topStar1;
    LayeredButton topStar2;
    LayeredButton topStar3;
    LayeredButton topStar4;
    LayeredButton topControl0;
    LayeredButton topControl1;
    LayeredButton topControl2;
    LayeredButton lvl0Star;
    LayeredButton lvl1Star;
    LayeredButton lvl2Star;
    LayeredButton lvl0Control;
    LayeredButton lvl1Control;
    LayeredButton lvl2Control;
    LayeredButton lvlNums;
    LayeredButton orbNames;
    LayeredButton launch0;
    LayeredButton launch1;
    LayeredButton launch1b;
    LayeredButton launch2;
    LayeredButton launch2b;
    LayeredButton launch2c;

    LayeredButton voidText;
    ChargeOrb orb;
    LayeredButton voidHint;

    LayeredButton novaText;
    LayeredButton novaHint;

    LayeredButton ammoText;

    LayeredButton winText;
    LayeredButton outroText;

    LayeredButton fakeBack;
    LayeredButton fakeSolo;
    LayeredButton fakeMulti;
    LayeredButton fakeTutorial;

    boolean page2orbLaunched = false;
    boolean gravityOn = false;
    boolean ammoStarOnly = false;

    float starRadius = 1.5f * ModelSettings.getFloatSetting("starRadius");

    int[] basePages;
    int[] launchPages;

    public Tutorial1(StarCycle starCycle) {
        super(Level.LevelType.QUAD2, ScreenType.TUTORIAL1, ScreenType.STARTMENU, ScreenType.TUTORIAL0, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Colors.cool, Colors.warm}, starCycle);

        Gdx.input.setInputProcessor(new GameController(this, 1));

        pages = 6;

        // Zeroth page:
        // Return transition:
        offset = 0f;

        transStar0 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh/3f-StarCycle.pixelsPerMeter + offset), starRadius);
        transStar1 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh*2f/3f+StarCycle.pixelsPerMeter + offset), starRadius);
        add(transStar0);
        add(transStar1);

        fakeLaunch = model.players[0].launchPad.getOrbButton(new Vector2(sw,sh + offset), 180f, model.players[0].colors, false);
        add(fakeLaunch);

        Vector2 malumaPos = new Vector2(sw-sh/4.2f, sh/4.2f + offset);
        fakeMaluma = new BaseButton(Base.BaseType.MALUMA, model.players[0].colors, malumaPos, new Vector2(1f,1f).scl(ModelSettings.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        add(fakeMaluma);

        fakeAim0 = model.players[0].base.getAimer(malumaPos, new Vector2(), model.players[0].colors);
        fakeAim0.rotate(90f);
        add(fakeAim0);

        Vector2 taketePos = new Vector2(sw/2f-StarCycle.pixelsPerMeter, sh*2f/3f+StarCycle.pixelsPerMeter + offset);
        fakeTakete = new BaseButton(Base.BaseType.TAKETE, model.players[1].colors, taketePos, new Vector2(1f,1f).scl(ModelSettings.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        add(fakeTakete);

        fakeAim1 = model.players[1].base.getAimer(taketePos, new Vector2(), model.players[1].colors);
        fakeAim1.rotate(-45f);
        for (int i = 0; i < fakeAim0.getLayerNum(); i ++) {
            fakeAim1.getLayer(i).setRelPosLen(Base.maxPointerLength * StarCycle.pixelsPerMeter);
        }
        add(fakeAim1);

        orbit = new LayeredButton(new Vector2(swipeCenter.x - bw, swipeCenter.y + offset));
        orbit.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[6], new Vector2(swipeSize.y, swipeSize.x)).rotateSprite(90f));
        add(orbit);

        // First page
        // Control/leveling overview:
        offset = sh;

        controlText = new LayeredButton(new Vector2(swipeSize.x/4f, sh/2f + offset));
        controlText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Capture stars to gain abilities", swipeSize).rotateText(90f));
        add(controlText);

        float topRow = swipeCenter.x + swipeSize.x/6f;

        topStar0 = Star.getButton(new Vector2(topRow, sh/6f + offset), starRadius*0.70f);
        add(topStar0);

        topStar1 = Star.getButton(new Vector2(topRow, sh*2f/6f + offset), starRadius*0.70f);
        add(topStar1);

        topControl0 = Star.getControlButton(topStar1.getCenter(), starRadius*0.70f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.25f);
        add(topControl0);

        topStar2 = Star.getButton(new Vector2(topRow, sh*3f/6f + offset), starRadius*0.90f);
        topStar2.setLayerColor(Colors.cyan, 1);
        add(topStar2);

        topControl1 = Star.getControlButton(topStar2.getCenter(), starRadius*0.90f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.5f);
        add(topControl1);

        topStar3 = Star.getButton(new Vector2(topRow, sh*4f/6f + offset), starRadius*0.70f);
        topStar3.setLayerColor(Colors.cyan, 1);
        add(topStar3);

        topControl2 = Star.getControlButton(topStar3.getCenter(), starRadius*0.70f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.75f);
        add(topControl2);

        topStar4 = Star.getButton(new Vector2(topRow, sh*5f/6f + offset), starRadius*0.70f);
        for (int i = 2; i < topStar4.getLayerNum(); i ++) {
            topStar4.setLayerColor(Colors.cyan, i);
        }
        add(topStar4);

        float starh = sh/4f + offset;
        float lph = sh * 9f/10f + offset;
        float row0 = (sw - swipeSize.x)/6f + swipeSize.x;
        float row1 = (sw - swipeSize.x)/2f + swipeSize.x;
        float row2 = (sw - swipeSize.x)*5f/6f + swipeSize.x;

        lvl0Star = Star.getButton(new Vector2(row0, starh), starRadius*0.9f);
        lvl0Star.setLayerColor(Colors.cyan, 1);
        add(lvl0Star);

        lvl0Control = Star.getControlButton(lvl0Star.getCenter(), starRadius*0.9f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.5f);
        add(lvl0Control);

        lvl1Star = Star.getButton(new Vector2(row1, starh), starRadius*0.9f);
        lvl1Star.setLayerColor(Colors.cyan, 1);
        add(lvl1Star);

        lvl1Control = Star.getControlButton(lvl1Star.getCenter(), starRadius*0.9f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.5f);
        add(lvl1Control);

        lvl2Star = Star.getButton(new Vector2(row2, starh), starRadius*0.9f);
        lvl2Star.setLayerColor(Colors.cyan, 1);
        add(lvl2Star);

        lvl2Control = Star.getControlButton(lvl2Star.getCenter(), starRadius*0.9f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.5f);
        add(lvl2Control);

        lvlNums = new LayeredButton(new Vector2(row0, sh/8f + offset));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "0 x").rotateText(90f));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "1 x", new Vector2(row1-row0, 0f)).rotateText(90f));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "2 x", new Vector2(row2 - row0, 0f)).rotateText(90f));
        add(lvlNums);

        orbNames = new LayeredButton(new Vector2(row0, sh*7f/16f + offset));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Orbs").rotateText(90f));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Voids", new Vector2(row1-row0, 0f)).rotateText(90f));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Novas", new Vector2(row2 - row0, 0f)).rotateText(90f));
        add(orbNames);

        launch0 = model.players[0].launchPad.getOrbButton(new Vector2(row0+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        launch0.getLayer(1).toggleSpecial();
        add(launch0);

        launch1 = model.players[0].launchPad.getOrbButton(new Vector2(row1+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        add(launch1);

        launch1b = model.players[0].launchPad.getPw1Button(new Vector2(row1+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        launch1b.activate();
        launch1b.getLayer(1).toggleSpecial();
        add(launch1b);

        launch2 = model.players[0].launchPad.getOrbButton(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        add(launch2);

        launch2b = model.players[0].launchPad.getPw1Button(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        launch2b.activate();
        add(launch2b);

        launch2c = model.players[0].launchPad.getPw2Button(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, model.players[0].colors, false);
        launch2c.activate();
        launch2c.getLayer(1).toggleSpecial();
        add(launch2c);

        // Second page
        // Using voids:
        offset = 2f*sh;

        voidText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        voidText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Capture a star to unlock voids", new Vector2(-swipeSize.x/8f, 0f)).rotateText(90f));
        voidText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Voids can destroy enemy orbs", new Vector2(swipeSize.x/8f, 0f)).rotateText(90f));
        add(voidText);

        model.players[0].base.translateBase(0f, offset);
        model.players[0].launchPad.movePos(0f, offset);
        basePages = new int[]{2, pages-2};
        launchPages = new int[]{2, pages-2};

        for (int i = 0; i < 2; i ++) {
            Star star = model.stars[i];
            star.moveStar(2f, -1f + i * 2f + offset / StarCycle.pixelsPerMeter);
            star.gravityOff();
            starClamp[i][0] = 2;
            starClamp[i][1] = 3;
        }
        Vector2 orbDist = new Vector2(ModelSettings.getFloatSetting("chargeRadius"), 0f).scl(2f);
        Vector2 starPos = new Vector2(model.stars[0].state.x, model.stars[0].state.y);
        for (int i = 0; i < Model.numPlayers; i ++) {
            model.stars[0].setControlPercent(i, 0.3f + i * 0.4f);
            for (int j = 0; j < 7; j ++) {
                orbDist.rotate(22.5f);

                // create locked orb
                ChargeOrb orb = (ChargeOrb) model.addOrb(model.players[i].number,
                        ChargeOrb.class,
                        starPos.x+orbDist.x,
                        starPos.y+orbDist.y,
                        -3f/60f, 0f, true);
                model.stars[0].addOrb(orb);
            }
            orbDist.rotate(22.5f);
        }
        model.stars[1].setControlPercent(0, 0.49f);
        orbClamp[0] = 2;
        orbClamp[1] = 3;
        Vector2 toStar = model.stars[1].getPosition().sub(model.players[0].base.origin);
        Vector2 aimVec = toStar.cpy().rotate(-90f).nor().scl(model.stars[0].radius+ ModelSettings.getFloatSetting("chargeRadius")*0.95f);
        aimVec.add(toStar);
        model.players[0].base.setPointer(aimVec);

        voidHint = new LayeredButton(new Vector2(sw-sh/12f, sh*7f/12f + offset));
        voidHint.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x/6f, swipeSize.x/4.5f)), LayerType.ACTIVE);
        voidHint.addLayer(new TextLayer(StarCycle.tex.gridnikSmall, "Launch a void!", new Vector2(-sh/12f, 0f)).rotateText(90f), LayerType.ACTIVE);
        voidHint.deactivate();
        add(voidHint);

        // Third Page
        // Using novas:
        offset = 3f*sh;

        novaText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        novaText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Capture two stars to unlock novas", new Vector2(-swipeSize.x/8f, 0f)).rotateText(90f));
        novaText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Novas can instantly capture a star", new Vector2(swipeSize.x/8f, 0f)).rotateText(90f));
        add(novaText);

        model.stars[2].moveStar(1f, offset / StarCycle.pixelsPerMeter);
        model.stars[2].gravityOff();
        starClamp[2][0] = 3;
        starClamp[2][1] = 3;

        novaHint = new LayeredButton(new Vector2(sw - sh/3f, sh*8f/9f + offset));
        novaHint.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x/8f, swipeSize.x/6f)).rotateSprite(-60f), LayerType.ACTIVE);
        novaHint.addLayer(new TextLayer(StarCycle.tex.gridnikSmall, "Launch a nova!", new Vector2(-sh/12f, -sh/12f)).rotateText(90f), LayerType.ACTIVE);
        novaHint.deactivate();
        add(novaHint);

        // Fourth Page
        // Ammo and income:
        offset = 4f*sh;

        ammoText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        ammoText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Orbs gather energy from stars.", new Vector2(-swipeSize.x/4f, 0f)).rotateText(90f));
        ammoText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Launching each orb requires energy.", new Vector2(-swipeSize.x/16f, 0f)).rotateText(90f));
        ammoText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Voids need more energy than orbs.", new Vector2(swipeSize.x/16f, 0f)).rotateText(90f));
        ammoText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Novas use lots of energy, so aim carefully!", new Vector2(swipeSize.x*3f/16f, 0f)).rotateText(90f));
        add(ammoText);

        model.stars[3].moveStar(-2f, offset / StarCycle.pixelsPerMeter);
        model.stars[3].gravityOff();

        // Fifth Page
        // Win condition and outro:
        offset = 5f*sh;

        winText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        winText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Capture all stars to win!", swipeSize).rotateText(90f));
        add(winText);

        outroText = new LayeredButton(new Vector2(sw/2f, sh/2f + offset));
        outroText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "This concludes the tutorial.", swipeSize).rotateText(90f));
        outroText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Now you are ready to play StarCycle!", new Vector2(bw*1.5f, 0f)).rotateText(90f));
        outroText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Jump into the solo campaign,", new Vector2(bw*4f, 0f)).rotateText(90f));
        outroText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "or challenge a friend in multiplayer!", new Vector2(bw*5.5f, 0f)).rotateText(90f));
        StarCycle.virgin = false;
        add(outroText);

        // Sixth Page
        // Transition:
        offset = 6*sh;

        Vector2 fakeIconsize = new Vector2(sh/5f, sh/5f);

        fakeBack = new LayeredButton(new Vector2(backPosition.x, backPosition.y + offset));
        fakeBack.addLayer(new SpriteLayer(StarCycle.tex.backIcon, backSize).rotateSprite(90f));
        add(fakeBack);

        fakeSolo = new LayeredButton(new Vector2(sw/4f, sh/2f + offset));
        fakeSolo.addLayer(new SpriteLayer(StarCycle.tex.soloIcon, fakeIconsize).rotateSprite(90f));
        add(fakeSolo);

        fakeMulti = new LayeredButton(new Vector2(sw/2f, sh/2f + offset));
        fakeMulti.addLayer(new SpriteLayer(StarCycle.tex.multiplayerIcon, new Vector2(fakeIconsize.x*4f/3f, fakeIconsize.y)).rotateSprite(90f));
        add(fakeMulti);

        fakeTutorial = new LayeredButton(new Vector2(sw*3f/4f, sh/2f + offset));
        fakeTutorial.addLayer(new SpriteLayer(StarCycle.tex.questionIcon, fakeIconsize.cpy().div(2f)).rotateSprite(90f));
        add(fakeTutorial);

        borders(pages);
        pageDone.set(1,true);

        ui.addActor(swiper);

        if (startAtEnd) {
            for (int i = 0; i < pages-1; i ++) {
                moveDraggables(-sh);
                currentBorder++;
            }
            startAtEnd = false;
        }
        else {
            moveDraggables(-sh);
            currentBorder = 1;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!moving && currentBorder == 0) {
            nextScreen = prevScreen;
            startAtEnd = true;
            isDone = true;
        };

        if (currentBorder == 2 && !moving && !page2orbLaunched) {
            Model.setCosts(0f,0f,0f);
            Vector2 vel = model.players[0].base.getPointer().scl(ModelSettings.getFloatSetting("velScaleOrbFact"));
            orb = (ChargeOrb) model.addOrb(0, // TODO create locked orb method in Model?
                    ChargeOrb.class,
                    model.players[0].base.origin.x,
                    model.players[0].base.origin.y,
                    vel.x, vel.y, false);
            page2orbLaunched = true;
        }
        if (page2orbLaunched && !gravityOn) {
            if (((ChargeOrbState)orb.state).star >= 0) { // if orbiting
                for (int i = 0; i < 3; i ++) {
                    Star star = model.stars[i];
                    star.gravityOn();
                }
                gravityOn = true;
                orb.lockOn(model.stars[1]); // XXX removed angle here..
            }
        }
        if (page2orbLaunched && model.players[0].base.level == 0) {
            if (model.stars[1].state.possession[0] >= 0.5f) {
                model.players[0].setLevel(1);
                model.players[0].base.setPointerPolar(2f, 150f);
                model.players[0].base.manualLvl = false;
                model.players[0].launchPad.manualLvl = false;
                voidHint.activate();

            }
        }

        if (currentBorder == 2 && !pageDone.get(2)) {
            if (model.stars[0].state.numActiveOrbs[1] == 0) {
                pageDone.set(2, true);

            }
        }
        if (currentBorder == 3 && !pageDone.get(3)) {
            if (model.stars[2].hitByNova) {
                pageDone.set(3, true);
            }
        }
        if (model.players[0].base.level == 2 && !novaHint.isActive()) {
            novaHint.activate();
        }

        if (currentBorder == 4 && !moving && !model.players[0].showIncomeOrbs) {

            Model.setCosts();
            for (int i = 0; i < Model.numPlayers; i ++) {
                for (Orb orb: model.orbs[i].values()) {
                    orb.removeIfOff();
                }
                for (Orb orb: model.voids[i].values()) {
                    orb.removeIfOff();
                }
                for (Orb orb: model.novas[i].values()) {
                    orb.removeIfOff();
                }
            }
            model.players[0].state.ammo = ModelSettings.getFloatSetting("nukeCost");
            model.players[0].showIncomeOrbs = true;
            model.players[0].launchPad.showMeter(true);
            Vector2 starPos = new Vector2(model.stars[3].state.x, model.stars[3].state.y);
            Vector2 orbDist = new Vector2(ModelSettings.getFloatSetting("chargeRadius"), 0f).scl(2f);
            for (int i = 0; i < 3; i ++) {
                orbDist.rotate(120f);
                ChargeOrb orb = (ChargeOrb) model.addOrb(model.players[0].number, // TODO create locked orb method in Model?
                                                        ChargeOrb.class,
                                                        starPos.x+orbDist.x,
                                                        starPos.y+orbDist.y,
                                                        -5/60f, 0f, false);
                model.stars[3].addOrb(orb);
            }
            pageDone.set(4, true);
        }

        if (currentBorder == 4 && !moving && !ammoStarOnly) {
            for (int i = 0; i < 3; i ++) {
                model.stars[i].gravityOff();
            }
            model.stars[3].gravityOn();
            ammoStarOnly = true;
        }
        else if (currentBorder != 4 && ammoStarOnly) {
            for (int i = 0; i < 3; i ++) {
                model.stars[i].gravityOn();
            }
            model.stars[3].gravityOff();
            ammoStarOnly = false;
        }
    }

    @Override
    public Model initModel(Level.LevelType lvl, ModelScreen screen) {
        // anonymous Model class implementing abstract initPlayers method
        return new Model(lvl, screen) {

            @Override
            public Player[] initPlayers(ModelScreen screen) {
                Player[] players = new Player[numPlayers];
                for (int i=0; i < numPlayers; i++){
                    players[i] = new Player(i, ui, skins[i], colors[i], i == 0);
                    players[i].launchPad.showMeter(false);
                    players[i].showIncomeOrbs = false;
                    players[i].launchPad.manualLvl = true;
                    players[i].base.manualLvl = true;

                }
                return players;
            }
        };
    }

    @Override
    public void moveDraggables(float y) {
        super.moveDraggables(y);
        moveBase(0, moveClamped(basePages[0], basePages[1], y));
        moveLaunch(0, moveClamped(launchPages[0], basePages[1], y));
    }
}
