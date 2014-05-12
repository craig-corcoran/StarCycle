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
    LayeredButton swiper =  new LayeredButton(swipeCenter, swipeSize); // The drag element  at the top of the screen.
    float bw = sh * 0.05f; // Bar width for the swiper border.
    float yDown = 0f; // Drag initial position.
    float yPrev = 0f; // Last updated touchDrag.
    float dy = 0f; // Distance between current touchDrag and yPrev.
    float dragLen; // Length of the drag overall.
    float dragThreshold = sh*0.25f; // Threshold for changing to a new tutorial page.
    boolean dragging = false; // Moving via touch.
    boolean moving = false; // Moving to a page after touch.
    int moves = 15;
    int move = 0;
    float moveStep = 0f;
    ArrayList<LayeredButton> draggables = new ArrayList<LayeredButton>(); // UI elements that move with the drags.
    ArrayList<Boolean> pageDone = new ArrayList<Boolean>(); // Task completed?
    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
    float fakeOrbRad = ModelSettings.getFloatSetting("chargeOrbRadius")*StarCycle.pixelsPerMeter;

    float offset;
    int currentBorder = 0; // Page being viewed.
    ArrayList<LayeredButton> ellipses = new ArrayList<LayeredButton>(); // Indicators to move on.
    ScreenType prevScreen;
    public boolean startAtEnd = false; // For swiping backwards from Tutorial1 to Tutorial0.

    int[][] starClamp; // The stars don't move for drags within certain ranges.
    int[] orbClamp = new int[]{0, 0}; // The orbs don't move for drags within certain ranges.
    int pages;

    public Tutorial (Level.LevelType lvlType, ScreenType screenType, ScreenType nextScreen, ScreenType prevScreen, BaseType[] skins, Color[][] colors, StarCycle starcycle) {
        super(lvlType, screenType, skins, colors, starcycle);
        this.nextScreen = nextScreen;
        this.prevScreen = prevScreen;
        silentSwitch = true; // Don't play the screen switch sound.
        model.altWin = true; // Don't win on star capture.

        ((SpriteLayer) resumeButton.getLayer(0)).setSpriteAlpha(1f); // The pause menu blocks everything out.
        // Label the pause menu.
        resumeButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Resume", new Vector2(pauseButton.getDims().x*3f/8f, 0f)).rotateText(90f));
        mainMenuButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Main Menu", new Vector2(pauseButton.getDims().x*3f/8f, 0f)).rotateText(90f));
        backButton.addLayer(new TextLayer(StarCycle.tex.gridnikMedium, "Quit Tutorial", new Vector2(pauseButton.getDims().x*3f/8f, 0f)).rotateText(90f));

        starClamp = new int[model.stars.length][2];
        for (int i = 0; i < model.stars.length; i ++) {
            starClamp[i][0] = 0;
            starClamp[i][1] = 0;
        }

        swiper.addListener(new DragListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragging = true; // Begin dragging and note the initial position:
                yDown = y;
                yPrev = y;
                dragLen = 0f;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (!swiper.touchOn(x,y)) {
                    cancel(); // End when the drag leaves the touch area.
                }
                else {
                    dy = y - yPrev; // Distance dragged on this update.
                    yPrev = y;
                    dragLen = yPrev - yDown; // Distance dragged since the initial touchDown.
                    // Always move to the right; only move to the left after the starting page:
                    if (currentBorder > 0 || y < yDown) {
                        moveDraggables(dy); // Move appropriate elements.
                    }
                }
            }

            @Override
            public void cancel() {
                dragging = false;
                dragLen = yPrev - yDown; // Total drag length.
                // Advance one page if the threshold is exceeded:
                if (-dragLen > dragThreshold) {
                    currentBorder++;
                    sendDraggables(-sh-dragLen);
                }
                // Go back one page if the threshold is exceeded:
                else if (dragLen > dragThreshold && currentBorder > 0) {
                    currentBorder--;
                    sendDraggables(sh-dragLen);
                }
                // Recenter the page if the threshold isn't exceeded:
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
            moveDraggables(moveStep); // Move toward the new page.
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
            // Turn on ellipses once a page's condition is met:
            if (pageDone.get(i) && !ellipses.get(i).isActive()) {
                ui.addActor(ellipses.get(i));
                ellipses.get(i).activate();
            }
        }
    }

    @Override
    public void render(float delta) {
        // FakeOrbs must be drawn after the super call, because the tutorial UI elements will block them out otherwise.
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
            draggables.get(i).moveCenter(0f, y); // Draggable LayeredButtons.
        }
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();) {
            itr.next().move(0f, y); // Demo FakeOrbs.
        }
        for (int i = 0; i < model.stars.length; i ++) {
            // Stars move only outside of their individual stationary ranges:
            model.stars[i].moveStar(0f, moveClamped(starClamp[i][0], starClamp[i][1], y) / StarCycle.pixelsPerMeter);
        }
        for (int i = 0; i < Model.numPlayers; i ++) {
            // Orbs move only outside of their shared stationary range:
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

    // Send everything to a new destination, usually to the next page:
    public void sendDraggables(float y) {
        moving = true;
        moveStep = y/moves;
        swiper.deactivate(); // Prevent drags while moving to avoid misalignment.
    }

    // Each page has a swiper border with indicator ellipses:
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

    // Drag only when outside of a stationary range:
    float moveClamped(int startPage, int endPage, float y) {
        boolean clamped = false; // Assume not clamped, then check for certain cases.
        // Setting startPage == endPage will always result in a drag.
        if (startPage != endPage) {
            if (startPage < currentBorder && currentBorder < endPage) {
                clamped = true; // Within the stationary range.
            }
            if (currentBorder == startPage && moving && y > 0f) {
                clamped = true; // Advancing to the first stationary page.
            }
            if (currentBorder == startPage && !moving && dragLen <= 0f) {
                clamped = true; // Dragging backwards from the first stationary page.
            }
            if (currentBorder == endPage && moving && y < 0f) {
                clamped = true; // Returning to the final stationary page.
            }
            if (currentBorder == endPage && !moving && dragLen >= 0f) {
                clamped = true; // Dragging forwards from the final stationary page.
            }
        }
        return clamped? 0f : y;
    }

    // Add to ui and set as a draggable button.
    void add(LayeredButton button) {
        ui.addActor(button);
        draggables.add(button);
    }

    // Not being dragged and not moving after a drag:
    boolean still() {
        return !moving && !dragging;
    }
}
