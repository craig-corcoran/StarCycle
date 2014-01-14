package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.FakeOrb;
import com.autonomousgames.starcycle.core.model.ImageOrb;
import com.autonomousgames.starcycle.core.model.Level;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Tutorial extends ModelScreen {

    float sw = StarCycle.screenWidth;
    float sh = StarCycle.screenHeight;
    Vector2 swipeCenter = new Vector2(sw*0.15f, sh*0.5f);
    Vector2 swipeSize = new Vector2(sw*0.3f, sh*0.9f);
    LayeredButton swiper =  new LayeredButton(swipeCenter, swipeSize);
    float yDown = 0f;
    float yPrev = 0f;
    float dy = 0f;
    float dragThreshold = sh*0.25f;
    boolean moving = false;
    int moves = 15;
    int move = 0;
    float moveStep = 0f;
    ArrayList<LayeredButton> draggables = new ArrayList<LayeredButton>();
    public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();

    int currentBorder = 0;
    ScreenType prevScreen;

    public Tutorial (Level.LevelType lvlType, ScreenType screenType, ScreenType nextScreen, ScreenType prevScreen, BaseType[] skins, Color[][] colors) {
        super(lvlType, screenType, skins, colors);
        this.nextScreen = nextScreen;
        this.prevScreen = prevScreen;

        swiper.addListener(new DragListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                yDown = y;
                yPrev = y;
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
                    if (currentBorder > 0 || y < yDown) {
                        moveDraggables(dy);
                    }
                }
            }

            @Override
            public void cancel() {
                float dragLen = yPrev - yDown;
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

//    @Override
//    void setPlayers() {
//        numPlayers = 1;
//        players = new Player[numPlayers];
//        players[0] = new Player(0, skins[0], colors[0], this, ui, true, true);
//        players[0].altWin = true;
//    }

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
            model.stars.get(i).moveStar(0f, y/StarCycle.pixelsPerMeter);
        }
        for (int i = 0; i < numPlayers; i ++) {
            players[i].base.translateBase(0f,y);
            players[i].launchPad.movePos(0f,y);
            for (int j = 0; j < players[i].orbs.size(); j ++) {
                players[i].orbs.get(j).moveVisual(0f, y);
            }
        }
    }

    public void sendDraggables(float y) {
        moving = true;
        moveStep = y/moves;
    }

    void borders(int borderNum) {
        float bw = sh * 0.05f;
        for (int i = 0; i < borderNum; i++) {
            LayeredButton button = new LayeredButton(swipeCenter, swipeSize);
            button.addLayer(new SpriteLayer(Texturez.block, new Vector2(0f, sh * 0.425f), new Vector2(sw * 0.3f, bw), Texturez.night, 0f));
            button.addLayer(new SpriteLayer(Texturez.block, new Vector2(sw * 0.15f - bw / 2f, 0f), new Vector2(bw, sh * 0.9f), Texturez.night, 0f));
            button.addLayer(new SpriteLayer(Texturez.block, new Vector2(0f, -sh * 0.425f), new Vector2(sw * 0.3f, bw), Texturez.night, 0f));
            button.moveCenter(0f, sh*i);
            draggables.add(button);
            ui.addActor(button);
        }
    }

}
