package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.model.*;
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


    Vector2 tileSize = new Vector2(sh*0.4f, sw*0.3f-sh*0.05f);


    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
    Vector2 fakeBasePos0 = new Vector2(tileSize.y*153f/420f, sh*0.1f+tileSize.x*270/420f + sh);
    Vector2 fakeBasePos1 = new Vector2(fakeBasePos0).add(0f, sh);
    Vector2 orbVel0 = new Vector2(-2f, 1.9f);
    Vector2 orbVel1 = new Vector2(-2.76f, 0f);
    Vector2 vel = new Vector2();
    float coolDown = UserSettingz.getFloatSetting("coolDown");
    float sinceLastShot;

    public Tutorial0() {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL0, ScreenType.TUTORIAL3, ScreenType.STARTMENU, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.MALUMA}, new Color[][]{Texturez.cool, Texturez.cool});

        for (int i = 0; i < model.stars.size(); i ++) {
            Star star = model.stars.get(i);
            star.mass = 0f;
            star.moveStar(0f, sh * 3f / StarCycle.pixelsPerMeter);
        }

        pauseButton.deactivate();

        borders(4);

        dragHand = new LayeredButton(new Vector2(swipeCenter.x, swipeCenter.y+sh*0.3f));
        Vector2 slideVec = new Vector2(0f, -sh*0.6f);
        dragHand.addLayer(new SpriteLayer(Texturez.fingerRight, new Vector2(swipeSize.x/4f, swipeSize.x/3f)).rotateSprite(90f).slideAndReturn(slideVec, 2f));
        ui.addActor(dragHand);
        draggables.add(dragHand);

        holdImage = new LayeredButton(new Vector2(sw*0.7f, sh*0.5f));
        float graphicSide = (sh < sw*0.6f) ? sh : sw*0.6f;
        holdImage.addLayer(new SpriteLayer(Texturez.tutorialImages[0], new Vector2(1f, 1f).scl(graphicSide)).rotateSprite(90f));
        ui.addActor(holdImage);
        draggables.add(holdImage);

        aim = new LayeredButton(new Vector2(swipeCenter.x-0.025f*sh, swipeCenter.y + sh));
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[1], new Vector2(0f, -sh*0.2f), tileSize));
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[2], new Vector2(0f, -sh*0.2f), tileSize).blink(0.75f),LayerType.SPECIAL);
        aim.addLayer(new SpriteLayer(Texturez.tutorialImages[3], new Vector2(0f, sh*0.2f), tileSize));
        aim.rotateLayers(90f);
        ui.addActor(aim);
        draggables.add(aim);

        shoot = new LayeredButton(new Vector2(swipeCenter.x-0.025f*sh, swipeCenter.y + 2f*sh));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[1], new Vector2(0f, -sh * 0.2f), tileSize));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[4], new Vector2(0f, sh * 0.2f), tileSize));
        shoot.addLayer(new SpriteLayer(Texturez.tutorialImages[5], new Vector2(0f, sh * 0.2f), tileSize).blink(1f), LayerType.SPECIAL);
        shoot.rotateLayers(90f);
        ui.addActor(shoot);
        draggables.add(shoot);

        ui.addActor(swiper);

        players[1].base.moveBase(players[0].base.origin);
        players[1].base.setPointer(-10f, 0f);
        players[0].launchPad.movePos(0f, sh * 2f);
        players[0].base.translateBase(0f, sh * 2f);
        players[1].base.translateBase(0f, sh);
        orbFactory.setCosts(0f, 0f, 0f);
        players[1].launchPad.streamOrbs = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!moving && currentBorder == 3) {
            isDone = true;
        };

        if (moving) {
            moveDraggables(moveStep);
            move++;
            if (move == moves) {
                moving = false;
                move = 0;
            }
        }

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

        if ((currentBorder != 1 && players[1].launchPad.streamOrbs) || (currentBorder == 1 && players[1].launchPad.streamOrbs == false)) {
            players[1].launchPad.streamOrbs = !(players[1].launchPad.streamOrbs);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();) {
            FakeOrb orb = itr.next();
            orb.draw(batch);
            orb.update(delta);
        }
        batch.end();
    }

    @Override
    void setPlayers() {
        numPlayers = 2;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i ++) {
            players[i] = new Player(i, skins[i], colors[i], this, ui, true, i == 0);
            players[i].altWin = true;
            players[i].launchPad.showMeter(false);
        }
    }

    @Override
    public void moveDraggables(float y) {
        players[0].launchPad.movePos(0f,y);
        for (int i = 0; i < numPlayers; i ++) {
            players[i].base.translateBase(0f,y);
            for (int j = 0; j < players[i].orbs.size(); j ++) {
                players[i].orbs.get(j).moveVisual(0f, y);
            }
        }
       for (int i = 0; i < draggables.size(); i++) {
           draggables.get(i).moveCenter(0f, y);
       }
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();) {
            itr.next().move(0f, y);
        }
        fakeBasePos0.add(0f,y);
        fakeBasePos1.add(0f,y);

        for (int i = 0; i < model.stars.size(); i ++) {
            model.stars.get(i).moveStar(0f, y/StarCycle.pixelsPerMeter);
        }
    }

    @Override
    public void sendDraggables(float y) {
        moving = true;
        moveStep = y/moves;
    }

}
