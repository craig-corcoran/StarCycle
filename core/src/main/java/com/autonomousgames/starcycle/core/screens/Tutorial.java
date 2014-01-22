package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.FakeOrb;
import com.autonomousgames.starcycle.core.model.ImageOrb;
import com.autonomousgames.starcycle.core.model.Level;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Tutorial extends ModelScreen {

    float sw = StarCycle.screenWidth;
    float sh = StarCycle.screenHeight;
    Vector2 swipeSize = new Vector2(sh*0.45f, sh*0.9f);
    Vector2 swipeCenter = new Vector2(swipeSize.x/2f, sh*0.5f);
    LayeredButton swiper =  new LayeredButton(swipeCenter, swipeSize);
    float bw = sh * 0.05f;
    float yDown = 0f;
    float yPrev = 0f;
    float dy = 0f;
    float dragLen;
    float dragThreshold = sh*0.25f;
    boolean dragging = false;
    boolean moving = false;
    int moves = 15;
    int move = 0;
    float moveStep = 0f;
    ArrayList<LayeredButton> draggables = new ArrayList<LayeredButton>();
    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
    float fakeOrbRad = UserSettingz.getFloatSetting("chargeOrbRadius")*StarCycle.pixelsPerMeter;

    float offset;
    int currentBorder = 0;
    ScreenType prevScreen;
    public boolean startAtEnd = false;

    int[][] starClamp;

    public Tutorial (Level.LevelType lvlType, ScreenType screenType, ScreenType nextScreen, ScreenType prevScreen, BaseType[] skins, Color[][] colors) {
        super(lvlType, screenType, skins, colors);
        this.nextScreen = nextScreen;
        this.prevScreen = prevScreen;
        silentSwitch = true;

        starClamp = new int[model.stars.size()][2];
        for (int i = 0; i < model.stars.size(); i ++) {
            starClamp[i][0] = 0;
            starClamp[i][1] = 0;
        }

        swiper.addListener(new DragListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragging = true;
                yDown = y;
                yPrev = y;
                dragLen = 0f;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (swiper.touchOn(x,y) == false) {
                    cancel();
                }
                else {
                    dy = y - yPrev;
                    yPrev = y;
                    dragLen = yPrev - yDown;
                    if (currentBorder > 0 || y < yDown) {
                        moveDraggables(dy);
                    }
                }
            }

            @Override
            public void cancel() {
                dragging = false;
                dragLen = yPrev - yDown;
                if (-dragLen > dragThreshold) {
                    currentBorder++;
                    sendDraggables(-sh-dragLen);
                }
                else if (dragLen > dragThreshold && currentBorder > 0) {
                    currentBorder--;
                    sendDraggables(sh-dragLen);
                }
                else if (dragLen < 0f || currentBorder > 0) {
                    sendDraggables(-dragLen);
                }
                super.cancel();
            }

        });

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (moving) {
            moveDraggables(moveStep);
            move++;
            if (move == moves) {
                moving = false;
                move = 0;
            }
        }
        for (int i = 0; i < numPlayers; i ++) {
            for (int j = 0; j < players[i].orbs.size(); j ++) {
                players[i].orbs.get(j).removeIfOff();
            }
            for (int j = 0; j < players[i].voids.size(); j ++) {
                players[i].voids.get(j).removeIfOff();
            }
            for (int j = 0; j < players[i].novas.size(); j ++) {
                players[i].novas.get(j).removeIfOff();
            }
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

    public void moveDraggables(float y) {
        for (int i = 0; i < draggables.size(); i++) {
            draggables.get(i).moveCenter(0f, y);
        }
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();) {
            itr.next().move(0f, y);
        }
        for (int i = 0; i < model.stars.size(); i ++) {
            model.stars.get(i).moveStar(0f, moveClamped(starClamp[i][0], starClamp[i][1], y) / StarCycle.pixelsPerMeter);
        }
        for (int i = 0; i < numPlayers; i ++) {
            for (int j = 0; j < players[i].orbs.size(); j ++) {
                players[i].orbs.get(j).moveOrb(0f, y/StarCycle.pixelsPerMeter);
            }
            for (int j = 0; j < players[i].voids.size(); j ++) {
                players[i].voids.get(j).moveOrb(0f, y/StarCycle.pixelsPerMeter);
            }
            for (int j = 0; j < players[i].novas.size(); j ++) {
                players[i].novas.get(j).moveOrb(0f, y/StarCycle.pixelsPerMeter);
            }
        }
    }

    public void sendDraggables(float y) {
        moving = true;
        moveStep = y/moves;
    }

    void borders(int borderNum) {
        for (int i = 0; i < borderNum; i++) {
            LayeredButton button = new LayeredButton(swipeCenter, swipeSize);
            button.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, sh * 0.425f), new Vector2(swipeSize.x, bw), Colors.night, 0f));
            button.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(swipeCenter.x - bw / 2f, 0f), new Vector2(bw, sh * 0.9f), Colors.night, 0f));
            button.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, -sh * 0.425f), new Vector2(swipeSize.x, bw), Colors.night, 0f));
            button.moveCenter(0f, sh*i);
            draggables.add(button);
            ui.addActor(button);
        }
    }

    void moveBase(int i, float y) {
        players[i].base.translateBase(0f ,y);
    }

    void moveLaunch(int i, float y) {
        players[i].launchPad.movePos(0f, y);
    }

    float moveClamped(int startPage, int endPage, float y) {
        boolean clamped = false;
        if (startPage != endPage) {
            if (startPage < currentBorder && currentBorder < endPage) {
                clamped = true;
            }
            if (currentBorder == startPage && moving && y > 0f) {
                clamped = true;
            }
            if (currentBorder == startPage && !moving && dragLen <= 0f) {
                clamped = true;
            }
            if (currentBorder == endPage && moving && y < 0f) {
                clamped = true;
            }
            if (currentBorder == endPage && !moving && dragLen >= 0f) {
                clamped = true;
            }
        }
        return clamped? 0f : y;
    }
}
