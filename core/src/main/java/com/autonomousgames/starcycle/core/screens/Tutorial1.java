package com.autonomousgames.starcycle.core.screens;


import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base;
import com.autonomousgames.starcycle.core.model.Level;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.model.Star;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tutorial1 extends Tutorial {

    public Tutorial1() {
        super(Level.LevelType.DOUBLE, ScreenType.TUTORIAL1, ScreenType.TUTORIAL4, ScreenType.TUTORIAL0, new Base.BaseType[]{Base.BaseType.MALUMA, Base.BaseType.TAKETE}, new Color[][]{Texturez.cool, Texturez.warm});

        Gdx.input.setInputProcessor(new GameController(this, 1));

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
