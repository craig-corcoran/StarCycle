package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.ui.BaseButton;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;

public class Tutorial0 extends Tutorial {

    LayeredButton dragHand;
    LayeredButton holdImage;
    LayeredButton aim;
    LayeredButton shoot;
    LayeredButton orbit;

    int basePage;
    int launchPage;
    float offset;

    Vector2 tileSize = new Vector2(swipeSize.y/2f-bw, swipeSize.x-bw);

    Vector2 fakeBasePos0 = new Vector2(tileSize.y*153f/420f, sh*0.1f+tileSize.x*270/420f);
    Vector2 fakeBasePos1 = new Vector2(fakeBasePos0);
    Vector2 orbVel0 = new Vector2(-2f, 1.9f);
    Vector2 orbVel1 = new Vector2(-2.76f, 0f);
    Vector2 vel = new Vector2();
    float coolDown = UserSettingz.getFloatSetting("coolDown");
    float sinceLastShot;

    boolean starMass = false;

    public Tutorial0() {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL0, ScreenType.TUTORIAL1, ScreenType.STARTMENU, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Texturez.cool, Texturez.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));
        Gdx.app.log("Tutorial0","swipeSize: "+swipeSize);

        // Zeroth page
        // Pausing and swiping:
        offset = 0f;

        dragHand = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y+sh*0.3f));
        Vector2 slideVec = new Vector2(0f, -sh*0.6f);
        dragHand.addLayer(new SpriteLayer(Texturez.fingerRight, new Vector2(swipeSize.x/4f, swipeSize.x/3f)).rotateSprite(90f).slideAndReturn(slideVec, 2f));
        ui.addActor(dragHand);
        draggables.add(dragHand);

        // Fist page
        // Holding:
        offset = sh;

        holdImage = new LayeredButton(new Vector2(sw*0.7f, sh*0.5f+offset));
        float graphicSide = (sh < sw*0.6f) ? sh : sw*0.6f;
        holdImage.addLayer(new SpriteLayer(Texturez.tutorialImages[0], new Vector2(1f, 1f).scl(graphicSide)).rotateSprite(90f));
        ui.addActor(holdImage);
        draggables.add(holdImage);

        // Second page
        // Aiming:
        offset = 2*sh;
        basePage = 2;

        players[0].base.translateBase(0f, offset);
        fakeBasePos0.add(0f, offset);

        aim = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[2], new Vector2(0f, -tileSize.x/2f), tileSize).blink(0.75f),LayerType.SPECIAL);
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[3], new Vector2(0f, tileSize.x/2f), tileSize));
        aim.rotateLayers(90f);
        ui.addActor(aim);
        draggables.add(aim);

        // Third page
        // Shooting:
        offset = 3*sh;
        launchPage = 3;

        players[0].launchPad.movePos(0f, offset);
        fakeBasePos1.add(0f, offset);

        shoot = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[4], new Vector2(0f, tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[5], new Vector2(0f, tileSize.x/2f), tileSize).blink(1f), LayerType.SPECIAL);
        shoot.rotateLayers(90f);
        ui.addActor(shoot);
        draggables.add(shoot);

        // Fourth page
        // Orbiting:
        offset = 4*sh;

        players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f-1f, StarCycle.meterHeight*2f/3f+1f));
        players[1].base.translateBase(0f, offset);
        players[1].base.setPointer(2f, 2f);

        for (int i = 0; i < model.stars.size(); i ++) {
            Star star = model.stars.get(i);
            star.mass = 0f;
            star.moveStar(i + 1f, i + offset / StarCycle.pixelsPerMeter);
        }

        orbit = new LayeredButton(new Vector2(swipeCenter.x - bw, swipeCenter.y + offset));
        orbit.addLayer(new SpriteLayer(Texturez.tutorialImages[6], new Vector2(swipeSize.y, swipeSize.x)).rotateSprite(90f));
        ui.addActor(orbit);
        draggables.add(orbit);

        pauseButton.deactivate();

        borders(5);

//        BaseButton fakeTakete = new BaseButton(Base.BaseType.TAKETE, Texturez.warm, new Vector2(sw*0.5f, sh*3.5f), new Vector2(1f, 1f).scl(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
//        ui.addActor(fakeTakete);
//        draggables.add(fakeTakete);

//        fakeAim = new LayeredButton(fakeTakete.getCenter());
//        fakeAim.addLayer(new SpriteLayer(Texturez.aimer[0], new Vector2(-UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter*0.92f, 0f), players[0].base.handleImDims,Texturez.warm[1], -90f));
//        for (int i = 0; i < 2; i ++) {
//            fakeAim.addLayer(new SpriteLayer(Texturez.aimer[i+1], new Vector2(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter*0.92f, 0f), players[0].base.chevronImDims,Texturez.warm[i], -90f));
//        }
//        ui.addActor(fakeAim);
//        draggables.add(fakeAim);

        ui.addActor(swiper);

        orbFactory.setCosts(0f, 0f, 0f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

//        if (!moving && currentBorder == 3) {
//            isDone = true;
//        };

        sinceLastShot = Math.min(sinceLastShot + delta, coolDown);
        if (sinceLastShot >= coolDown) {
            if (aim.getLayer(1).drawCondition()) {
                vel.set(orbVel1);
            }
            else {
                vel.set(orbVel0);
            }
            if (0f < fakeBasePos0.y && fakeBasePos0.y < sh) {
                fakeOrbs.add(new ImageOrb(Texturez.fakeorbTextures[0], 10f, fakeBasePos0, StarCycle.screenWidth,
                        StarCycle.screenHeight, vel, new Vector2(0, 0)));
            }
            if (shoot.getLayer(2).drawCondition() && 0f < fakeBasePos1.y && fakeBasePos1.y < sh) {
                fakeOrbs.add(new ImageOrb(Texturez.fakeorbTextures[0], 10f, fakeBasePos1, StarCycle.screenWidth,
                        StarCycle.screenHeight, orbVel0, new Vector2(0, 0)));
            }
            sinceLastShot = 0f;
        }

        if (((moving || dragging) && players[0].launchPad.streamOrbs) || (currentBorder == 2 && !moving && !dragging && players[0].launchPad.streamOrbs == false)) {
            players[0].launchPad.streamOrbs = !(players[0].launchPad.streamOrbs);
        }

        if (((moving || dragging || model.stars.get(1).getOrbCount(Orb.OrbType.ORB) >= 15) && players[1].launchPad.streamOrbs) || (currentBorder == 4 && !moving && !dragging && model.stars.get(1).getOrbCount(Orb.OrbType.ORB) < 15 && players[1].launchPad.streamOrbs == false)) {
            players[1].launchPad.streamOrbs = !(players[1].launchPad.streamOrbs);
        }

        if (currentBorder == 4 && !starMass) {
            for (int i = 0; i < model.stars.size(); i ++) {
                Star star = model.stars.get(i);
                star.mass = star.radius*star.radius;
            }
            starMass = true;
        }
        if (currentBorder != 4 && !moving && starMass) {
            for (int i = 0; i < model.stars.size(); i ++) {
                model.stars.get(i).mass = 0f;
            }
            starMass = false;
        }
    }

    @Override
    void setPlayers() {
        numPlayers = 2;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i ++) {
            players[i] = new Player(i, skins[i], colors[i], this, ui, true, i == 0);
            players[i].altWin = true;
            players[i].launchPad.showMeter(false);
            players[i].showIncomeOrbs = false;
        }
    }

    @Override
    public void moveDraggables(float y) {
        super.moveDraggables(y);
        fakeBasePos0.add(0f,y);
        fakeBasePos1.add(0f,y);
        moveBase(0, moveClamped(basePage, y));
        moveLaunch(0, moveClamped(launchPage, y));
        moveBase(1,y);
    }
}