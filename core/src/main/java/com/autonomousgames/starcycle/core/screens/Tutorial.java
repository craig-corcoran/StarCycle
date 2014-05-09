package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.*;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.autonomousgames.starcycle.core.ui.TextLayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Tutorial extends ModelScreen {

    float sw = StarCycle.screenWidth;
    float sh = StarCycle.screenHeight;
    Vector2 swipeSize = new Vector2(sh*0.5f, sh);
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
    ArrayList<Boolean> pageDone = new ArrayList<Boolean>();
    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
    float fakeOrbRad = ModelSettings.getFloatSetting("chargeOrbRadius")*StarCycle.pixelsPerMeter;

    float offset;
    int currentBorder = 0;
    ArrayList<LayeredButton> ellipses = new ArrayList<LayeredButton>();
    ScreenType prevScreen;
    public boolean startAtEnd = false;

    int[][] starClamp;
    int[] orbClamp = new int[]{0, 0};
    int pages;

    public Tutorial (Level.LevelType lvlType, ScreenType screenType, ScreenType nextScreen, ScreenType prevScreen, BaseType[] skins, Color[][] colors, StarCycle starcycle) {
        super(lvlType, screenType, skins, colors, starcycle);
        this.nextScreen = nextScreen;
        this.prevScreen = prevScreen;
        silentSwitch = true;
        model.altWin = true;

        ((SpriteLayer) resumeButton.getLayer(0)).setSpriteAlpha(1f);
        resumeButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Resume", new Vector2(pauseButton.getDims().x*3f/8f, 0f), resumeButton.getDims()).rotateText(90f));
        mainMenuButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Main Menu", new Vector2(pauseButton.getDims().x*3f/8f, 0f), resumeButton.getDims()).rotateText(90f));
        backButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Quit Tutorial", new Vector2(pauseButton.getDims().x*3f/8f, 0f), resumeButton.getDims()).rotateText(90f));

        starClamp = new int[model.stars.length][2];
        for (int i = 0; i < model.stars.length; i ++) {
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
                if (!swiper.touchOn(x,y)) {
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
                swiper.activate();
            }
        }

        if (!moving && currentBorder == pages) {
            isDone = true;
        }

        for (int i = 0; i < pages; i ++) {
            if (pageDone.get(i) && !ellipses.get(i).isActive()) {
                ui.addActor(ellipses.get(i));
                ellipses.get(i).activate();
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
        for (int i = 0; i < model.stars.length; i ++) {
            model.stars[i].moveStar(0f, moveClamped(starClamp[i][0], starClamp[i][1], y) / StarCycle.pixelsPerMeter);
        }
        for (int i = 0; i < Model.numPlayers; i ++) {

            for (ChargeOrb orb: model.orbs[i].values()) {
                orb.moveOrb(0f, moveClamped(orbClamp[0], orbClamp[1], y/StarCycle.pixelsPerMeter));
            }
            for (com.autonomousgames.starcycle.core.model.Void orb: model.voids[i].values()) {
                orb.moveOrb(0f, moveClamped(orbClamp[0], orbClamp[1], y/StarCycle.pixelsPerMeter));
            }
            for (Nova orb: model.novas[i].values()) {
                orb.moveOrb(0f, moveClamped(orbClamp[0], orbClamp[1], y/StarCycle.pixelsPerMeter));
            }
        }
    }

    public void sendDraggables(float y) {
        moving = true;
        moveStep = y/moves;
        swiper.deactivate();
    }

    void borders(int borderNum) {
        for (int i = 0; i < borderNum; i++) {
            LayeredButton border = new LayeredButton(swipeCenter, swipeSize);
            border.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, swipeSize.y/2f -bw/2f), new Vector2(swipeSize.x, bw), Colors.night, 0f));
            border.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(swipeSize.x/2f - bw/2f, -swipeSize.y*0.01f), new Vector2(bw, swipeSize.y*1.02f), Colors.night, 0f));
            border.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, -swipeSize.y/2f +bw/2f), new Vector2(swipeSize.x, bw), Colors.night, 0f));
            border.moveCenter(0f, sh*i);
            add(border);
            LayeredButton ellipsis = new LayeredButton(new Vector2(swipeCenter.x+swipeSize.x/2f - bw/2f, sh - bw*3.5f));
            ellipsis.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(bw/2f, bw/2f)).blinkOn(0.25f, 1f), LayerType.SPECIAL);
            ellipsis.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, bw), new Vector2(bw/2f, bw/2f)).blinkOn(0.5f, 0.75f), LayerType.SPECIAL);
            ellipsis.addLayer(new SpriteLayer(StarCycle.tex.block, new Vector2(0f, bw*2f), new Vector2(bw/2f, bw/2f)).blinkOn(0.75f, 0.5f), LayerType.SPECIAL);
            ellipsis.moveCenter(0f,sh*i);
            ellipsis.deactivate();
            draggables.add(ellipsis);
            ellipses.add(ellipsis);
            pageDone.add(false);
        }
    }

    void moveBase(int i, float y) {
        model.players[i].base.translateBase(0f ,y);
    }

    void moveLaunch(int i, float y) {
        model.players[i].launchPad.movePos(0f, y);
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

    void add(LayeredButton button) {
        ui.addActor(button);
        draggables.add(button);
    }

    boolean still() {
        return !moving && !dragging;
    }
}
