package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.model.Void;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial0 extends Tutorial {

    LayeredButton welcomeText;
    LayeredButton pauseArea;
    LayeredButton dragHand;
    LayeredButton holdText;
    LayeredButton holdImage;
    LayeredButton aim;
    LayeredButton target0;
    LayeredButton target1;
    LayeredButton target2;
    LayeredButton shoot;
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

    // Some elements drag onto the screen, then stay put, and then eventually drag away.
    // These variables note when the element stops dragging and resumes dragging.
    int[] basePages;
    int[] launchPages;
    int[] taketePages;

    Vector2 tileSize = new Vector2(swipeSize.y/2f-bw, swipeSize.x-bw); // For split images.

    float starRadius = 1.5f * ModelSettings.getFloatSetting("starRadius"); // To avoid repeated calls of getFloatSettings.
    // Where fakeOrbs come out in the demos:
    Vector2 fakeBasePos0 = new Vector2(tileSize.y*153f/420f, bw+tileSize.x*270/420f);
    Vector2 fakeBasePos1 = new Vector2(fakeBasePos0);
    Vector2 orbVel0 = new Vector2(-2f, 1.9f);
    Vector2 orbVel1 = new Vector2(-2.76f, 0f);
    Vector2 vel = new Vector2();
    float coolDown = ModelSettings.getFloatSetting("coolDown");
    float sinceLastShot;

    int orbKillPage = 4; // Existing orbs are cleared at this point.
    boolean gravityOn = false; // Track changes to gravity.

    public Tutorial0(boolean startAtEnd, StarCycle starcycle) {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL0, ScreenType.TUTORIAL1, ScreenType.STARTMENU, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Colors.cool, Colors.warm}, starcycle);

        Gdx.input.setInputProcessor(new GameController(this, 1));

        pages = 5;

        // Zeroth page
        // Welcome, pausing, and swiping:
        offset = 0f;

        // Starting page text:
        CharSequence text00 = "Welcome to StarCycle!";
        CharSequence text01 = "Swipe within the upper area";
        CharSequence text02 = "to progress through this tutorial.";
        CharSequence text03 = "Access the pause menu by tapping";
        CharSequence text04 = "within the blue rectangular area.";
        welcomeText = new LayeredButton(new Vector2(sw/2f - pauseButton.getDims().x/4f, sh/2f + offset));
        welcomeText.addLayer(new TextLayer(StarCycle.tex.gridnikJumbo, text00).rotateText(90f));
        welcomeText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, text01, new Vector2(1.5f * StarCycle.pixelsPerMeter, 0f)).rotateText(90f));
        welcomeText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, text02, new Vector2(2.25f * StarCycle.pixelsPerMeter, 0f)).rotateText(90f));
        welcomeText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, text03, new Vector2(6.75f * StarCycle.pixelsPerMeter, 0f)).rotateText(90f));
        welcomeText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, text04, new Vector2(7.25f * StarCycle.pixelsPerMeter, 0f)).rotateText(90f));
        add(welcomeText);

        // Indicator of pause touch area:
        pauseArea = new LayeredButton(pauseButton.getCenter());
        pauseArea.addLayer(new SpriteLayer(StarCycle.tex.block, pauseButton.getDims()).setSpriteColor(Colors.navy).setSpriteAlpha(0.25f));
        add(pauseArea);

        // Drag hint:
        dragHand = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y+sh*0.3f));
        Vector2 slideVec = new Vector2(0f, -sh*0.6f);
        dragHand.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x / 4f, swipeSize.x / 3f)).rotateSprite(90f).slideAndReturn(slideVec, 2f));
        add(dragHand);

        // Fist page
        // Holding:
        offset = sh;

        CharSequence text10 = "Use two hands to play";
        CharSequence text11 = "The dots indicate a good time to go on,";
        CharSequence text12 = "but feel free to move at your own pace.";
        holdText = new LayeredButton(new Vector2(swipeCenter.x-bw*2.5f, swipeCenter.y + offset));
        holdText.addLayer(new TextLayer(StarCycle.tex.gridnikJumbo, text10).rotateText(90f));
        holdText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, text11, new Vector2(bw*3.5f, 0f)).rotateText(90f));
        holdText.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, text12, new Vector2(bw*5f, 0f)).rotateText(90f));
        add(holdText);

        holdImage = new LayeredButton(new Vector2(sw*0.7f, sh*0.5f+offset));
        float graphicSide = (sh < sw*0.6f) ? sh : sw*0.6f;
        holdImage.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[0], new Vector2(1f, 1f).scl(graphicSide)).rotateSprite(90f));
        add(holdImage);

        // Second page
        // Aiming:
        offset = 2f*sh;
        basePages = new int[]{2, pages-1}; // Maluma base stationary from this point until the transition page.

        model.players[0].base.translateBase(0f, offset); // Start Maluma on this page.
        fakeBasePos0.add(0f, offset); // First FakeOrb origin.

        aim = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[2], new Vector2(0f, -tileSize.x/2f), tileSize).blink(1.5f),LayerType.SPECIAL);
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[3], new Vector2(0f, tileSize.x/2f), tileSize));
        aim.rotateLayers(90f);
        add(aim);

        // Targets for demonstrating the touch behind to aim forwards concept:
        Vector2 targetDims = new Vector2(0.5f,0.5f).scl(StarCycle.pixelsPerMeter);
        Vector2 bo = model.players[0].base.baseButton.getCenter();
        Vector2 targetVec = new Vector2(-Base.maxPointerLength * StarCycle.pixelsPerMeter, 0f).scl(1.75f).rotate(-25f);
        target0 = new LayeredButton(new Vector2(bo.x + targetVec.x, bo.y + targetVec.y));
        target0.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.red).setSpriteAlpha(0.5f), LayerType.INACTIVE);
        target0.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.spinach).setSpriteAlpha(0.5f), LayerType.ACTIVE);
        target0.deactivate();
        targetVec.rotate(-20f);
        target1 = new LayeredButton(new Vector2(bo.x + targetVec.x, bo.y + targetVec.y));
        target1.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.red).setSpriteAlpha(0.5f), LayerType.INACTIVE);
        target1.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.spinach).setSpriteAlpha(0.5f), LayerType.ACTIVE);
        target1.deactivate();
        targetVec.rotate(-20f);
        target2 = new LayeredButton(new Vector2(bo.x + targetVec.x, bo.y + targetVec.y));
        target2.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.red).setSpriteAlpha(0.5f), LayerType.INACTIVE);
        target2.addLayer(new SpriteLayer(StarCycle.tex.circle, targetDims).setSpriteColor(Colors.spinach).setSpriteAlpha(0.5f), LayerType.ACTIVE);
        target2.deactivate();
        add(target0);
        add(target1);
        add(target2);

        // Third page
        // Shooting:
        offset = 3f*sh;
        launchPages = new int[]{3, pages-1}; // Maluma LaunchPad stationary from this point until the transition page.

        model.players[0].launchPad.movePos(0f, offset); // Start the LaunchPad here.
        fakeBasePos1.add(0f, offset); // Second FakeOrb origin.

        shoot = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[4], new Vector2(0f, tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[5], new Vector2(0f, tileSize.x/2f), tileSize).blink(1.5f), LayerType.SPECIAL);
        shoot.rotateLayers(90f);
        add(shoot);

        // Fourth page
        // Orbiting:
        offset = 4f*sh;
        taketePages = new int[]{4, pages-1}; // Takete base stationary from this point until the transition page.

        // Start Takete on this page, below the swiper, and aiming toward the demo star:
        model.players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f-1f, StarCycle.meterHeight*2f/3f+1f));
        model.players[1].base.translateBase(0f, offset);
        model.players[1].base.setPointer(2f, 2f);

        // Gravity starts off; stars start on this page:
        for (int i = 0; i < model.stars.length; i ++) {
            Star star = model.stars[i];
            star.gravityOff();
            star.moveStar(2f, -1f + i*2f + offset / StarCycle.pixelsPerMeter);
        }

        orbit = new LayeredButton(new Vector2(swipeCenter.x - bw, swipeCenter.y + offset));
        orbit.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[6], new Vector2(swipeSize.y, swipeSize.x)).rotateSprite(90f));
        add(orbit);

        // Fifth page
        // Transition:
        offset = 5f*sh;

        controlText = new LayeredButton(new Vector2(swipeSize.x/4f, sh/2f + offset));
        controlText.addLayer(new TextLayer(StarCycle.tex.gridnikLarge, "Capture stars to gain abilities").rotateText(90f));
        add(controlText);

        // Lots of fake stars, fake launchpads, and text:
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

        borders(pages + 1); // Generate the borders.
        pageDone.set(1, true); // Turn on ellipsis on this page.

        ui.addActor(swiper);

        Model.setCosts(0f, 0f, 0f);
        Orb.useLifeSpan = false;

        // When swiping back from Tutorial1 to Tutorial0, move to the final page:
        if (startAtEnd) {
            // Do not replace this loop with a single command!
            for (int i = 0; i < pages-1; i ++) {
                moveDraggables(-sh);
                currentBorder++;
            }
        }
        startAtEnd = false; // The warning is a lie. StarCycle.java will check the value before disposing of the screen.
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Cooldown for FakeOrbs:
        sinceLastShot = sinceLastShot + delta;
        if (sinceLastShot >= coolDown) {
            // Choose where to aim:
            if (aim.getLayer(1).drawCondition()) {
                vel.set(orbVel1); // Angled.
            }
            else {
                vel.set(orbVel0); // Straight up.
            }
            // If the "aim" fake base is visible:
            if (0f < fakeBasePos0.y && fakeBasePos0.y < sh) {
                fakeOrbs.add(new ImageOrb(StarCycle.tex.fakeorbTextures[0], fakeOrbRad, fakeBasePos0, StarCycle.screenWidth,
                        StarCycle.screenHeight, vel, new Vector2(0, 0)));
            }
            // If the "shoot" fake base is visible and launching:
            if (shoot.getLayer(2).drawCondition() && 0f < fakeBasePos1.y && fakeBasePos1.y < sh) {
                fakeOrbs.add(new ImageOrb(StarCycle.tex.fakeorbTextures[0], fakeOrbRad, fakeBasePos1, StarCycle.screenWidth,
                        StarCycle.screenHeight, orbVel0, new Vector2(0, 0)));
            }
            sinceLastShot = 0f; //Reset.
        }

        // Maluma streams automatically on the "aim" page only:
        if ((!still() && model.players[0].state.buttonStates[0]) || (currentBorder == 2 && still() && !model.players[0].state.buttonStates[0])) {
            model.players[0].state.buttonStates[0] = !(model.players[0].state.buttonStates[0]);
        }

        // Takete starts streaming on the "orbit" page or if he has fewer than 10 orbs locked on:
        if (currentBorder == 4 && still() && model.stars[1].state.numActiveOrbs[1] < 10 && !model.players[1].state.buttonStates[0]) {
            model.players[1].state.buttonStates[0] = true;
        }

        // Takete stops streaming once 10 orbs lock onto the star or when off the screen:
        if (((currentBorder != 4 && still()) || model.stars[1].state.numActiveOrbs[1] >= 10) && model.players[1].state.buttonStates[0]) {
            model.players[1].state.buttonStates[0] = false;
        }

        // Clean up existing orbs:
        if (currentBorder >= orbKillPage && !still()) {
            for (int i = 0; i < model.numPlayers; i ++) {

                for (ChargeOrb orb: model.orbs[i].values()) {
                    model.toRemove.add(orb);
                }
                for (Void orb: model.voids[i].values()) {
                    model.toRemove.add(orb);
                }
                for (Nova orb: model.novas[i].values()) {
                    model.toRemove.add(orb);
                }
            }
        }

        // Turn on gravity for the "orbit" page:
        if (currentBorder >= 4 && !gravityOn) {
            for (int i = 0; i < model.stars.length; i ++) {
                Star star = model.stars[i];
                star.gravityOn();
            }
            gravityOn = true;
        }

        // Turn off gravity when moving back from the "orbit" page:
        if (currentBorder < 4 && !moving && gravityOn) {
            for (int i = 0; i < model.stars.length; i ++) {
                model.stars[i].gravityOff();
            }
            gravityOn = false;
        }


        // Always remove off-screen orbs:
        for (int i = 0; i < model.numPlayers; i ++) {

            for (ChargeOrb orb: model.orbs[i].values()) {
                orb.removeIfOff();
            }
            for (Void orb: model.voids[i].values()) {
                orb.removeIfOff();
            }
            for (Nova orb: model.novas[i].values()) {
                orb.removeIfOff();
            }
        }

        // Targets turn green after Maluma aims at them:
        if (currentBorder == 2 && !pageDone.get(2)) {
            float aimAng = model.players[0].base.getPointer().angle();
            if (152.5f < aimAng && aimAng < 157.5f && !target0.isActive()) {
                target0.activate();
            }
            if (132.5f < aimAng && aimAng < 137.5f && !target1.isActive()) {
                target1.activate();
            }
            if (112.5f < aimAng && aimAng < 117.5f && !target2.isActive()) {
                target2.activate();
            }
            // Aiming at all three targets completes the page:
            if (target0.isActive() && target1.isActive() && target2.isActive()) {
                pageDone.set(2, true);
            }
        }

        // Touching the orb button completes the page:
        if (currentBorder == 3 && !pageDone.get(3)) {
            if (model.players[0].state.buttonStates[0]) {
                pageDone.set(3, true);
            }
        }

        // Getting five orbs to charge completes the page:
        if (currentBorder == 4 && !pageDone.get(4)) {
            if (model.stars[0].state.numActiveOrbs[0] >= 5) { // getOrbCount(Orb.OrbType.ORB) >= 5) {
                pageDone.set(4, true);
            }
        }

    }

    @Override
    public Model initModel(Level.LevelType lvl, ModelScreen screen) {

        return new Model(lvl, screen) {
            @Override
            public Player[] initPlayers(ModelScreen screen) {
                Player[] players = new Player[numPlayers];
                for (int i=0; i < numPlayers; i++){

                    players[i] = new Player(i, ui, skins[i], colors[i], true, i == 0);
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
        fakeBasePos0.add(0f,y);
        fakeBasePos1.add(0f,y);
        moveBase(0, moveClamped(basePages[0], basePages[1], y));
        moveLaunch(0, moveClamped(launchPages[0], basePages[1], y));
        moveBase(1,moveClamped(taketePages[0], taketePages[1], y));
    }
}