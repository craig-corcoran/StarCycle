package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial0 extends Tutorial {

    LayeredButton dragHand;
    LayeredButton holdImage;
    LayeredButton aim;
    LayeredButton shoot;
    LayeredButton orbit;

    int[] basePages;
    int[] launchPages;
    int[] taketePages;
    int pages = 5;

    Vector2 tileSize = new Vector2(swipeSize.y/2f-bw, swipeSize.x-bw);

    Vector2 fakeBasePos0 = new Vector2(tileSize.y*153f/420f, sh*0.1f+tileSize.x*270/420f);
    Vector2 fakeBasePos1 = new Vector2(fakeBasePos0);
    Vector2 orbVel0 = new Vector2(-2f, 1.9f);
    Vector2 orbVel1 = new Vector2(-2.76f, 0f);
    Vector2 vel = new Vector2();
    float coolDown = UserSettingz.getFloatSetting("coolDown");
    float sinceLastShot;

    int orbKillPage = 4;
    boolean starsMass = false;

    public Tutorial0(boolean startAtEnd) {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL0, ScreenType.TUTORIAL5, ScreenType.STARTMENU, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Texturez.cool, Texturez.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));

        // Zeroth page
        // Pausing and swiping:
        offset = 0f;

        dragHand = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y+sh*0.3f));
        Vector2 slideVec = new Vector2(0f, -sh*0.6f);
        dragHand.addLayer(new SpriteLayer(StarCycle.tex.fingerRight, new Vector2(swipeSize.x/4f, swipeSize.x/3f)).rotateSprite(90f).slideAndReturn(slideVec, 2f));
        ui.addActor(dragHand);
        draggables.add(dragHand);

        // Fist page
        // Holding:
        offset = sh;

        holdImage = new LayeredButton(new Vector2(sw*0.7f, sh*0.5f+offset));
        float graphicSide = (sh < sw*0.6f) ? sh : sw*0.6f;
        holdImage.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[0], new Vector2(1f, 1f).scl(graphicSide)).rotateSprite(90f));
        ui.addActor(holdImage);
        draggables.add(holdImage);

        // Second page
        // Aiming:
        offset = 2*sh;
        basePages = new int[]{2, pages-1};

        players[0].base.translateBase(0f, offset);
        fakeBasePos0.add(0f, offset);

        aim = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[2], new Vector2(0f, -tileSize.x/2f), tileSize).blink(0.75f),LayerType.SPECIAL);
        aim.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[3], new Vector2(0f, tileSize.x/2f), tileSize));
        aim.rotateLayers(90f);
        ui.addActor(aim);
        draggables.add(aim);

        // Third page
        // Shooting:
        offset = 3*sh;
        launchPages = new int[]{3, pages-1};

        players[0].launchPad.movePos(0f, offset);
        fakeBasePos1.add(0f, offset);

        shoot = new LayeredButton(new Vector2(swipeCenter.x-bw/2f, swipeCenter.y + offset));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[1], new Vector2(0f, -tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[4], new Vector2(0f, tileSize.x/2f), tileSize));
        shoot.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[5], new Vector2(0f, tileSize.x/2f), tileSize).blink(1f), LayerType.SPECIAL);
        shoot.rotateLayers(90f);
        ui.addActor(shoot);
        draggables.add(shoot);

        // Fourth page
        // Orbiting:
        offset = 4*sh;
        taketePages = new int[]{4, pages-1};

        players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f-1f, StarCycle.meterHeight*2f/3f+1f));
        players[1].base.translateBase(0f, offset);
        players[1].base.setPointer(2f, 2f);


        for (int i = 0; i < model.stars.size(); i ++) {
            Star star = model.stars.get(i);
            star.mass = 0f;
            star.moveStar(2f, -1f + i*2f + offset / StarCycle.pixelsPerMeter);
        }

        orbit = new LayeredButton(new Vector2(swipeCenter.x - bw, swipeCenter.y + offset));
        orbit.addLayer(new SpriteLayer(StarCycle.tex.tutorialImages[6], new Vector2(swipeSize.y, swipeSize.x)).rotateSprite(90f));
        ui.addActor(orbit);
        draggables.add(orbit);

        // Transition:
        offset = 5*sh;

        borders(pages + 1);

        pauseButton.deactivate();

        ui.addActor(swiper);

        orbFactory.setCosts(0f, 0f, 0f);

        if (startAtEnd) {
            for (int i = 0; i < pages-1; i ++) {
                moveDraggables(-sh);
                currentBorder++;
            }
        }
        startAtEnd = false;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!moving && currentBorder == pages) {
            isDone = true;
        };

        sinceLastShot = Math.min(sinceLastShot + delta, coolDown);
        if (sinceLastShot >= coolDown) {
            if (aim.getLayer(1).drawCondition()) {
                vel.set(orbVel1);
            }
            else {
                vel.set(orbVel0);
            }
            if (0f < fakeBasePos0.y && fakeBasePos0.y < sh) {
                fakeOrbs.add(new ImageOrb(StarCycle.tex.fakeorbTextures[0], 10f, fakeBasePos0, StarCycle.screenWidth,
                        StarCycle.screenHeight, vel, new Vector2(0, 0)));
            }
            if (shoot.getLayer(2).drawCondition() && 0f < fakeBasePos1.y && fakeBasePos1.y < sh) {
                fakeOrbs.add(new ImageOrb(StarCycle.tex.fakeorbTextures[0], 10f, fakeBasePos1, StarCycle.screenWidth,
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

        if (currentBorder >= orbKillPage && (dragging || moving)) {
            for (int i = 0; i < numPlayers; i ++) {
                for (int j = 0; j < players[i].orbs.size(); j ++) {
                    players[i].orbs.get(j).removeSelf();
                }
                for (int j = 0; j < players[i].voids.size(); j ++) {
                    players[i].voids.get(j).removeSelf();
                }
                for (int j = 0; j < players[i].novas.size(); j ++) {
                    players[i].novas.get(j).removeSelf();
                }
            }
        }

        if (currentBorder >= 4 && !starsMass) {
            for (int i = 0; i < model.stars.size(); i ++) {
                Star star = model.stars.get(i);
                star.mass = star.radius*star.radius;
            }
            starsMass = true;
        }
        if (currentBorder < 4 && !moving && starsMass) {
            for (int i = 0; i < model.stars.size(); i ++) {
                model.stars.get(i).mass = 0f;
            }
            starsMass = false;
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
            players[i].launchPad.manualLvl = true;
            players[i].base.manualLvl = true;
        }
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