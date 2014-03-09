package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.controllers.GameController;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.Bot;
import com.autonomousgames.starcycle.core.model.BotType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.model.Model;
import com.autonomousgames.starcycle.core.model.Player;
import com.autonomousgames.starcycle.core.ui.ScreenDoneClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class SinglePlayer extends ModelScreen {

	private SinglePlayerLevel leveltype;
	
	public SinglePlayer(LevelType lvl, BaseType[] skins, Color[][] colors, SinglePlayerLevel leveltype, BotType botType) {
		super(lvl, ScreenType.SINGLEPLAYER, skins, colors);
		Gdx.input.setInputProcessor(new GameController(this, 1)); // only one active touch interface
		nextScreen = ScreenType.CAMPAIGNSELECT;
		this.leveltype = leveltype;
		((Bot)model.players[1]).setBotType(botType);
	}

    class SinglePlayerModel extends Model {

        public SinglePlayerModel(LevelType lvl, ModelScreen screen) {
            super(lvl, screen);
        }

        @Override
        public Player[] initPlayers(ModelScreen screen) {
            Player[] players = new Player[numPlayers];
            for (int i=0; i < numPlayers; i++){
                if (i==0){
                    players[i] = new Player(i, ui, skins[i],colors[i], true, true);
                }
                else{
                    players[i] = new Bot(i, skins[i], colors[i], screen, ui, true, true);
                }
            }
            return players;
        }
    }

    @Override
    public Model initModel(LevelType lvl, ModelScreen screen) {
        return new SinglePlayerModel(lvl, screen);
    }
	
	void pushLevelCompletion(){
		StarCycle.progressHandler.setLevelComplete(this.leveltype.toString(), "true");
	}
	
	// TODO clean up, you win, you lose
	@Override
	public void addWinBanner(final Player winner) {
		super.addWinBanner(winner);
		winButton.addListener(new ScreenDoneClickListener(this, backScreen));
		ui.addActor(winButton);
		if (winner.number == 0) {
            pushLevelCompletion();
        }
	}
	
	public String toString(){
		return "SinglePlayer";
	}
}
