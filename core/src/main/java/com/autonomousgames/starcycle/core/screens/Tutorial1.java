package com.autonomousgames.starcycle.core.screens;


import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.ui.BaseButton;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial1 extends Tutorial {

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
    LayeredButton transStar0;
    LayeredButton transStar1;
    LayeredButton fakeLaunch;
    LayeredButton fakeMaluma;
    LayeredButton fakeAim0;
    LayeredButton fakeTakete;
    LayeredButton fakeAim1;
    LayeredButton orbit;

    float starRadius = 1.5f * UserSettingz.getFloatSetting("starRadius");

    int[] basePages;
    int[] launchPages;
    int[] taketePages;

    public Tutorial1() {
        super(Level.LevelType.TRIPLE, ScreenType.TUTORIAL1, ScreenType.TUTORIAL7, ScreenType.TUTORIAL0, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Colors.cool, Colors.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));

        pages = 3;

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

        topStar0 = Star.getButton(new Vector2(topRow, sh/6f + offset), starRadius*0.75f);
        add(topStar0);

        topStar1 = Star.getButton(new Vector2(topRow, sh*2f/6f + offset), starRadius*0.75f);
        add(topStar1);

        topControl0 = Star.getControlButton(topStar1.getCenter(), starRadius*0.75f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.25f);
        add(topControl0);

        topStar2 = Star.getButton(new Vector2(topRow, sh*3f/6f + offset), starRadius*0.75f);
        topStar2.setLayerColor(Colors.cyan, 1);
        add(topStar2);

        topControl1 = Star.getControlButton(topStar2.getCenter(), starRadius*0.75f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.5f);
        add(topControl1);

        topStar3 = Star.getButton(new Vector2(topRow, sh*4f/6f + offset), starRadius*0.75f);
        topStar3.setLayerColor(Colors.cyan, 1);
        add(topStar3);

        topControl2 = Star.getControlButton(topStar3.getCenter(), starRadius*0.75f*StarCycle.pixelsPerMeter, Colors.cyan, 0, 0.75f);
        add(topControl2);

        topStar4 = Star.getButton(new Vector2(topRow, sh*5f/6f + offset), starRadius*0.75f);
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

        players[0].base.translateBase(0f, offset);
        players[0].launchPad.movePos(0f, offset);
        players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f-1f, StarCycle.meterHeight*2f/3f+1f));
        players[1].base.translateBase(0f, offset);
        basePages = new int[]{2, pages-1};
        launchPages = new int[]{2, pages-1};
        taketePages= new int[]{2, pages-1};

        for (int i = 0; i < model.stars.size(); i ++) {
            float vShift = (i != 2) ? -1f + i*2f : 0f;
            model.stars.get(i).moveStar(2f, vShift + offset / StarCycle.pixelsPerMeter);
        }

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
