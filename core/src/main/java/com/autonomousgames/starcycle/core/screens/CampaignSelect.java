package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.autonomousgames.starcycle.core.model.BotType;
import com.autonomousgames.starcycle.core.model.Level.LevelType;
import com.autonomousgames.starcycle.core.ui.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CampaignSelect extends LevelSelectScreen {
	
	private final float edgeWidth = ui.getWidth() / 120f;
	
	public SinglePlayerLevel nextLvlSinglePlayer;
	public BotType botType;

    private ArrayList<String> accessibleLevels = new ArrayList<String>();

    public CampaignSelect() {
		
		super();

        backButton.addListener(new ScreenDoneClickListener(this, ScreenType.STARTMENU));

		// star positions
		HashMap<SinglePlayerLevel,	Vector2> posMap = new HashMap<SinglePlayerLevel,	Vector2>();  
		//posMap.put(SinglePlayerLevel.TUTORIAL,new Vector2(ui.getWidth()*7/8f, ui.getHeight()*3/16f));
		posMap.put(SinglePlayerLevel.LEVEL1,new Vector2(ui.getWidth()*7/8f, ui.getHeight()*3/16f));
		posMap.put(SinglePlayerLevel.LEVEL2,new Vector2(ui.getWidth()*6/16f, ui.getHeight()*1/4f));
		posMap.put(SinglePlayerLevel.LEVEL3,new Vector2(ui.getWidth()*10/16f, ui.getHeight()*3/4f));
		posMap.put(SinglePlayerLevel.LEVEL4,new Vector2(ui.getWidth()*1/8f, ui.getHeight()*13/16f));
		
		addEdges(posMap);
		
		Vector2 baseDims = new Vector2(1f, 1f).scl(ui.getHeight() * 0.15f);
		
		BaseButton level1 = new BaseButton(BaseType.TAKETE, Colors.warm, posMap.get(SinglePlayerLevel.LEVEL1), baseDims);
		level1.addBottomLayer(new SpriteLayer(StarCycle.tex.gradientRound, baseDims.cpy().scl(1.5f)), LayerType.DOWN);
		if (!StarCycle.progressHandler.getLevelComplete(SinglePlayerLevel.LEVEL1.toString()).equals("true")) {
			level1.addBottomLayer(new SpriteLayer(StarCycle.tex.voidRing, baseDims.cpy().scl(1.25f)).setSpriteAlpha(0.4f));
		}
		level1.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.TREFOIL;
				skins = new BaseType[] {BaseType.MALUMA, BaseType.TAKETE};
				colors = new Color[][] {Colors.cool, Colors.warm};
				nextLvlSinglePlayer = SinglePlayerLevel.LEVEL1;
				botType = BotType.EZ;
			}
		});
        ArrayList<LayeredButton> levelButtons = new ArrayList<LayeredButton>();
        levelButtons.add(level1);
		
		BaseButton level2 = new BaseButton(BaseType.TARGET, Colors.floral, posMap.get(SinglePlayerLevel.LEVEL2), baseDims);
		level2.addBottomLayer(new SpriteLayer(StarCycle.tex.gradientRound, baseDims.cpy().scl(1.5f)), LayerType.DOWN);
		if (!StarCycle.progressHandler.getLevelComplete(SinglePlayerLevel.LEVEL2.toString()).equals("true")) {
			level2.addBottomLayer(new SpriteLayer(StarCycle.tex.voidRing, baseDims.cpy().scl(1.25f)).setSpriteAlpha(0.4f));
		}
		level2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.CONCENTRIC;
				skins = new BaseType[] {BaseType.MALUMA, BaseType.TARGET};
				colors = new Color[][] {Colors.cool, Colors.floral};
				nextLvlSinglePlayer = SinglePlayerLevel.LEVEL2;
				botType = BotType.MEDIUM;
			}
		});
		levelButtons.add(level2);
		
		BaseButton level3 = new BaseButton(BaseType.DERELICT, Colors.neutral, posMap.get(SinglePlayerLevel.LEVEL3), baseDims);
		level3.addBottomLayer(new SpriteLayer(StarCycle.tex.gradientRound, baseDims.cpy().scl(1.5f)), LayerType.DOWN);
		if (!StarCycle.progressHandler.getLevelComplete(SinglePlayerLevel.LEVEL3.toString()).equals("true")) {
			level3.addBottomLayer(new SpriteLayer(StarCycle.tex.voidRing, baseDims.cpy().scl(1.25f)).setSpriteAlpha(0.4f));
		}
		level3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.DOUBLEBINARY;
				skins = new BaseType[] {BaseType.MALUMA, BaseType.DERELICT};
				colors = new Color[][] {Colors.cool, Colors.neutral};
				nextLvlSinglePlayer = SinglePlayerLevel.LEVEL3;
				botType = BotType.MEDIUM;
			}
		});
		levelButtons.add(level3);
		
		BaseButton level4 = new BaseButton(BaseType.CLOCKWORK, Colors.metallic, posMap.get(SinglePlayerLevel.LEVEL4), baseDims);
		level4.addBottomLayer(new SpriteLayer(StarCycle.tex.gradientRound, baseDims.cpy().scl(1.5f)), LayerType.DOWN);
		if (!StarCycle.progressHandler.getLevelComplete(SinglePlayerLevel.LEVEL4.toString()).equals("true")) {
			level4.addBottomLayer(new SpriteLayer(StarCycle.tex.voidRing, baseDims.cpy().scl(1.25f)).setSpriteAlpha(0.4f));
		}
		level4.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextLvlConfig = LevelType.TREFOIL;
				skins = new BaseType[] {BaseType.MALUMA, BaseType.CLOCKWORK};
				colors = new Color[][] {Colors.cool, Colors.metallic};
				nextLvlSinglePlayer = SinglePlayerLevel.LEVEL4;
				botType = BotType.PWN;
			}
		});
		levelButtons.add(level4);

        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.addButtonList(levelButtons);
		levelGroup.addInputListener(new ScreenDoneClickListener(this, ScreenType.SINGLEPLAYER));

		ui.addActor(level1);
		if (this.accessibleLevels.contains(SinglePlayerLevel.LEVEL2.toString())) {
			ui.addActor(level2);
		}
		if (this.accessibleLevels.contains(SinglePlayerLevel.LEVEL3.toString())) {
			ui.addActor(level3);
		}
		if (this.accessibleLevels.contains(SinglePlayerLevel.LEVEL4.toString())) {
			ui.addActor(level4);
		}
	}
	
	private void addEdges(HashMap<SinglePlayerLevel,	Vector2> posMap) {
		ArrayList<SinglePlayerLevel[]> edgeList = new ArrayList<SinglePlayerLevel[]>();
		edgeList.add(new SinglePlayerLevel[]{SinglePlayerLevel.LEVEL1,SinglePlayerLevel.LEVEL2});
		edgeList.add(new SinglePlayerLevel[]{SinglePlayerLevel.LEVEL1,SinglePlayerLevel.LEVEL3});
		edgeList.add(new SinglePlayerLevel[]{SinglePlayerLevel.LEVEL2,SinglePlayerLevel.LEVEL3});
		edgeList.add(new SinglePlayerLevel[]{SinglePlayerLevel.LEVEL2,SinglePlayerLevel.LEVEL4});
		edgeList.add(new SinglePlayerLevel[]{SinglePlayerLevel.LEVEL3,SinglePlayerLevel.LEVEL4});
        ArrayList<SinglePlayerLevel[]> validEdges = getAccessibleLevels(edgeList);
        for (SinglePlayerLevel[] edge : edgeList) {
            Vector2 pos1 = posMap.get(edge[0]);
            Vector2 pos2 = posMap.get(edge[1]);
            if (validEdges.contains(edge)) {
                Vector2 center = pos1.cpy().add(pos2).div(2f);
                Vector2 diff = pos1.cpy().sub(pos2);
                LayeredButton button = new LayeredButton(center);
                button.addLayer(new SpriteLayer(StarCycle.tex.line, new Vector2(), new Vector2(0.6f * diff.len(), edgeWidth / 2f)));
                float angle = diff.angle();
                button.rotate(angle);
                ui.addActor(button);
            }
        }
	}
	
	public String toString() {
		return "CampaignSelect";
	}
	public void setSinglePlayerGameType(SinglePlayerLevel nextLvlSinglePlayer){
		this.nextLvlSinglePlayer = nextLvlSinglePlayer;
	}
	public ArrayList<String> getCompletedLevels(){
		ArrayList<String> completedLevels = new ArrayList<String>();
		for (int n=0;n<SinglePlayerLevel.values().length;n++){
			String lvl = SinglePlayerLevel.values()[n].toString();
			if (StarCycle.progressHandler.getLevelComplete(lvl).equals("true")) {
				completedLevels.add(SinglePlayerLevel.values()[n].toString());
			}
		}
		return completedLevels;
	}
	public ArrayList<SinglePlayerLevel[]> getAccessibleLevels(ArrayList<SinglePlayerLevel[]> edgeList){
        ArrayList<String> completedLevels = getCompletedLevels();
		ArrayList<SinglePlayerLevel[]> validEdges = new ArrayList<SinglePlayerLevel[]>();
        for (SinglePlayerLevel[] edge : edgeList) {
            String vertexOne = edge[0].toString();
            String vertexTwo = edge[1].toString();
            if (completedLevels != null) {
                if (completedLevels.contains(vertexOne) || completedLevels.contains(vertexTwo)) {
                    validEdges.add(edge);
                    if (!this.accessibleLevels.contains(vertexOne)) {
                        this.accessibleLevels.add(vertexOne);
                    }
                    if (!this.accessibleLevels.contains(vertexTwo)) {
                        this.accessibleLevels.add(vertexTwo);
                    }
                }
            }
        }
		return validEdges;
	}
}