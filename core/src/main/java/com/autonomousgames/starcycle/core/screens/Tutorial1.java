package com.autonomousgames.starcycle.core.screens;


import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base;
import com.autonomousgames.starcycle.core.model.Level;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.model.Star;
import com.autonomousgames.starcycle.core.ui.BaseButton;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial1 extends Tutorial {

    LayeredButton fakeStar0;
    LayeredButton fakeStar1;
    LayeredButton fakeLaunch;
    LayeredButton fakeMaluma;
    LayeredButton fakeAim0;
    LayeredButton fakeTakete;
    LayeredButton fakeAim1;
    LayeredButton orbit;

    float starRadius = 1.5f * UserSettingz.getFloatSetting("starRadius");

    public Tutorial1() {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL1, ScreenType.TUTORIAL4, ScreenType.TUTORIAL0, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Texturez.cool, Texturez.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));

        // Zeroth page:
        // Return transition:
        offset = 0f;

        fakeStar0 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh/3f-StarCycle.pixelsPerMeter + offset), starRadius);
        fakeStar1 = Star.getButton(new Vector2(sw*0.5f + StarCycle.pixelsPerMeter*2f, sh*2f/3f+StarCycle.pixelsPerMeter + offset), starRadius);
        ui.addActor(fakeStar0);
        draggables.add(fakeStar0);
        ui.addActor(fakeStar1);
        draggables.add(fakeStar1);

        fakeLaunch = players[0].launchPad.getOrbButton(new Vector2(sw,sh + offset), 180f, players[0].colors, false);
        ui.addActor(fakeLaunch);
        draggables.add(fakeLaunch);

        Vector2 malumaPos = new Vector2(sw-sh/4.2f, sh/4.2f + offset);
        fakeMaluma = new BaseButton(Base.BaseType.MALUMA, players[0].colors, malumaPos, new Vector2(1f,1f).scl(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        ui.addActor(fakeMaluma);
        draggables.add(fakeMaluma);

        fakeAim0 = players[0].base.getAimer(malumaPos, new Vector2(), players[0].colors);
        fakeAim0.rotate(90f);
        ui.addActor(fakeAim0);
        draggables.add(fakeAim0);

        Vector2 taketePos = new Vector2(sw/2f-StarCycle.pixelsPerMeter, sh*2f/3f+StarCycle.pixelsPerMeter + offset);
        fakeTakete = new BaseButton(Base.BaseType.TAKETE, players[1].colors, taketePos, new Vector2(1f,1f).scl(UserSettingz.getFloatSetting("baseRadius")*StarCycle.pixelsPerMeter));
        ui.addActor(fakeTakete);
        draggables.add(fakeTakete);

        fakeAim1 = players[1].base.getAimer(taketePos, new Vector2(), players[1].colors);
        fakeAim1.rotate(-45f);
        ui.addActor(fakeAim1);
        draggables.add(fakeAim1);

        orbit = new LayeredButton(new Vector2(swipeCenter.x - bw, swipeCenter.y + offset));
        orbit.addLayer(new SpriteLayer(Texturez.tutorialImages[6], new Vector2(swipeSize.y, swipeSize.x)).rotateSprite(90f));
        ui.addActor(orbit);
        draggables.add(orbit);

        // First page
        //

        for (int i = 0; i < model.stars.size(); i ++) {
            model.stars.get(i).moveStar(sw*0.25f/StarCycle.pixelsPerMeter, sh / StarCycle.pixelsPerMeter);
        }

        borders(3);

        ui.addActor(swiper);

        players[0].launchPad.movePos(0f, sh * 2f);
        players[0].base.translateBase(0f, sh * 2f);
        players[1].launchPad.movePos(0f, sh);
        players[1].base.moveBase(new Vector2(StarCycle.meterWidth/2f, StarCycle.meterHeight/2f));
        players[1].base.translateBase(0f, sh);

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
        }
    }

}
