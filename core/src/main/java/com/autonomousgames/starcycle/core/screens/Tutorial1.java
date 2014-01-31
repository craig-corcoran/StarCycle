package com.autonomousgames.starcycle.core.screens;


import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.UserSettingz;
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

    boolean page2orbLaunched = false;
    boolean gravityOn = false;

    float starRadius = 1.5f * UserSettingz.getFloatSetting("starRadius");

    int[] basePages;
    int[] launchPages;
    int[] taketePages;

    public Tutorial1() {
        super(Level.LevelType.TRIPLE, ScreenType.TUTORIAL1, ScreenType.TUTORIAL7, ScreenType.TUTORIAL0, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Colors.cool, Colors.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));

        pages = 5;

        // Zeroth page:
        // Return transition:
        offset = 0f;

        transStar0 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh/3f-StarCycle.pixelsPerMeter + offset), starRadius);
        transStar1 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh*2f/3f+StarCycle.pixelsPerMeter + offset), starRadius);
        add(transStar0);
        add(transStar1);

        fakeLaunch = players[0].launchPad.getOrbButton(new Vector2(sw,sh + offset), 180f, players[0].colors, false);
        add(fakeLaunch);

        Vector2 malumaPos = new Vector2(sw-sh/4.2f, sh/4.2f + offset);
        fakeMaluma = new BaseButton(Base.BaseType.MALUMA, players[0].colors, malumaPos, new Vector2(1f,1f).scl(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        add(fakeMaluma);

        fakeAim0 = players[0].base.getAimer(malumaPos, new Vector2(), players[0].colors);
        fakeAim0.rotate(90f);
        add(fakeAim0);

        Vector2 taketePos = new Vector2(sw/2f-StarCycle.pixelsPerMeter, sh*2f/3f+StarCycle.pixelsPerMeter + offset);
        fakeTakete = new BaseButton(Base.BaseType.TAKETE, players[1].colors, taketePos, new Vector2(1f,1f).scl(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        add(fakeTakete);

        fakeAim1 = players[1].base.getAimer(taketePos, new Vector2(), players[1].colors);
        fakeAim1.rotate(-45f);
        for (int i = 0; i < fakeAim0.getLayerNum(); i ++) {
            fakeAim1.getLayer(i).setRelPosLen(Base.maxAimerLength);
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

        Vector2 numberSize = new Vector2(2f, 1f).scl(StarCycle.pixelsPerMeter);
        lvlNums = new LayeredButton(new Vector2(row0, sh/8f + offset));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "0 x", numberSize).rotateText(90f));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "1 x", new Vector2(row1-row0, 0f), numberSize).rotateText(90f));
        lvlNums.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "2 x", new Vector2(row2 - row0, 0f), numberSize).rotateText(90f));
        add(lvlNums);

        orbNames = new LayeredButton(new Vector2(row0, sh*7f/16f + offset));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Orbs", numberSize).rotateText(90f));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Voids", new Vector2(row1-row0, 0f), numberSize).rotateText(90f));
        orbNames.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Novas", new Vector2(row2 - row0, 0f), numberSize).rotateText(90f));
        add(orbNames);

        launch0 = players[0].launchPad.getOrbButton(new Vector2(row0+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        launch0.getLayer(1).toggleSpecial();
        add(launch0);

        launch1 = players[0].launchPad.getOrbButton(new Vector2(row1+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        add(launch1);

        launch1b = players[0].launchPad.getPw1Button(new Vector2(row1+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        launch1b.activate();
        launch1b.getLayer(1).toggleSpecial();
        add(launch1b);

        launch2 = players[0].launchPad.getOrbButton(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        add(launch2);

        launch2b = players[0].launchPad.getPw1Button(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        launch2b.activate();
        add(launch2b);

        launch2c = players[0].launchPad.getPw2Button(new Vector2(row2+StarCycle.pixelsPerMeter, lph), 180f, players[0].colors, false);
        launch2c.activate();
        launch2c.getLayer(1).toggleSpecial();
        add(launch2c);

        // Second page
        // Using voids:
        offset = 2f*sh;

        voidText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        voidText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Capture a star to unlock voids", new Vector2(-swipeSize.x/8f, 0f), swipeSize).rotateText(90f));
        voidText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Voids can destroy enemy orbs", new Vector2(swipeSize.x/8f, 0f), swipeSize).rotateText(90f));
        add(voidText);

        players[0].base.translateBase(0f, offset);
        players[0].launchPad.movePos(0f, offset);
        basePages = new int[]{2, pages-1};
        launchPages = new int[]{2, pages-1};

        for (int i = 0; i < 2; i ++) {
            Star star = model.stars.get(i);
            star.moveStar(2f, -1f + i * 2f + offset / StarCycle.pixelsPerMeter);
            star.mass = 0f;
            starClamp[i][0] = 2;
            starClamp[i][1] = pages-1;
        }
        Vector2 orbDist = new Vector2(UserSettingz.getFloatSetting("chargeRadius"), 0f).scl(2f);
        Vector2 starPos = new Vector2(model.stars.get(0).position);
        for (int i = 0; i < numPlayers; i ++) {
            model.stars.get(0).setControlPercent(i, 0.2f + i*0.6f);
            for (int j = 0; j < 5; j ++) {
                orbDist.rotate(30f);
                orbFactory.createLockedOrb(players[i], new Vector2(starPos.x + orbDist.x, starPos.y + orbDist.y), -1f, model.stars.get(0), -120f);
            }
            orbDist.rotate(30f);
        }
        model.stars.get(1).setControlPercent(0, 0.49f);
        orbClamp[0] = 2;
        orbClamp[1] = pages-1;
        Vector2 toStar = model.stars.get(1).position.cpy().sub(players[0].base.origin);
        Vector2 aimVec = toStar.cpy().rotate(-90f).nor().scl(model.stars.get(0).radius+UserSettingz.getFloatSetting("chargeRadius")*0.95f);
        aimVec.add(toStar);
        players[0].base.setPointer(aimVec);

        voidHint = new LayeredButton(new Vector2(sw-sh/12f, sh*7f/12f + offset));
        voidHint.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x/6f, swipeSize.x/4.5f)), LayerType.ACTIVE);
        voidHint.addLayer(new TextLayer(StarCycle.tex.gridnikSmall, "Launch a void!", new Vector2(-sh/12f, 0f), swipeSize).rotateText(90f), LayerType.ACTIVE);
        voidHint.deactivate();
        add(voidHint);

        // Third Page
        // Using novas:
        offset = 3f*sh;

        novaText = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y + offset));
        novaText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Capture two stars to unlock novas", new Vector2(-swipeSize.x/8f, 0f), swipeSize).rotateText(90f));
        novaText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Novas can instantly capture a star", new Vector2(swipeSize.x/8f, 0f), swipeSize).rotateText(90f));
        add(novaText);

        taketePages= new int[]{3, pages-1};

        model.stars.get(2).moveStar(1f, offset / StarCycle.pixelsPerMeter);
        model.stars.get(2).mass = 0f;
        starClamp[2][0] = 3;
        starClamp[2][1] = pages-1;

        novaHint = new LayeredButton(new Vector2(sw - sh/3f, sh*8f/9f + offset));
        novaHint.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x/8f, swipeSize.x/6f)).rotateSprite(-60f), LayerType.ACTIVE);
        novaHint.addLayer(new TextLayer(StarCycle.tex.gridnikSmall, "Launch a nova!", new Vector2(-sh/12f, -sh/12f), swipeSize).rotateText(90f), LayerType.ACTIVE);
        novaHint.deactivate();
        add(novaHint);

        // Fourth Page
        //
        offset = 4f*sh;

        players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f-1f, StarCycle.meterHeight*2f/3f+1f));
        players[1].base.translateBase(0f, offset);

        borders(pages + 1);

        ui.addActor(swiper);

        moveDraggables(-sh);
        currentBorder = 1;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!moving && currentBorder == 0) {
            nextScreen = prevScreen;
            startAtEnd = true;
            isDone = true;
        };

        if (currentBorder == 2 && !moving) {
            if (!page2orbLaunched) {
                orbFactory.setCosts(0f, 0f, 0f);
                orb = orbFactory.createChargeOrb(players[0], players[0].base.origin, players[0].base.getPointer().scl(UserSettingz.getFloatSetting("velScaleOrbFact")), -1f);
                page2orbLaunched = true;
            }
        }
        if (page2orbLaunched && !gravityOn) {
            if (orb.orbiting) {
                for (int i = 0; i < model.stars.size(); i ++) {
                    Star star = model.stars.get(i);
                    star.mass = star.radius*star.radius;
                }
                gravityOn = true;
                orb.lockOn(model.stars.get(1), 180f/60f);
            }
        }
        if (page2orbLaunched && players[0].base.level == 0) {
            if (model.stars.get(1).controlPercents[0] >= 0.5f) {
                players[0].setLevel(1);
                players[0].base.setPointerPolar(2f, 150f);
                voidHint.activate();
            }
        }

        if (currentBorder == 2 && !moving && voidHint.isActive()) {
            if (model.stars.get(0).controlPercents[0] >= 0.5f && players[0].base.level == 1) {
                sendDraggables(-sh);
                currentBorder++;
            }
        }
        if (currentBorder == 3 && !moving && players[0].base.level == 1) {
            orbFactory.setCosts(1000000f, 1000000f, 0f);
            players[0].base.manualLvl = false;
            players[0].launchPad.manualLvl = false;
        }
        if (players[0].base.level == 2 && !novaHint.isActive()) {
            novaHint.activate();
        }
    }

    @Override
    void setPlayers() {
        numPlayers = 2;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i ++) {
            players[i] = new Player(i, skins[i], colors[i], this, ui, true, i ==0);
            players[i].altWin = true;
            players[i].launchPad.showMeter(false);
            players[i].showIncomeOrbs = false;
            players[i].launchPad.manualLvl = true;
            players[i].base.manualLvl = true;
        }
    }

    @Override
    public void moveDraggables(float y) {
        super.moveDraggables(y);
        moveBase(0, moveClamped(basePages[0], basePages[1], y));
        moveLaunch(0, moveClamped(launchPages[0], basePages[1], y));
        moveBase(1,moveClamped(taketePages[0], taketePages[1], y));
    }
}
