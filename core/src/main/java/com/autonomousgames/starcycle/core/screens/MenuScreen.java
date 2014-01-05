package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.model.FakeOrb;
import com.autonomousgames.starcycle.core.model.MenuOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;

public class MenuScreen extends GameScreen {

	public ArrayList<MenuOrb> fakeOrbs = new ArrayList<MenuOrb>();
	
	public MenuScreen() {
		super();
		Gdx.input.setInputProcessor(new MenuController(this));
	}

    @Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.disableBlending();
		batch.begin();
		batch.draw(Texturez.mainMenuBG, 0f, 0f, StarCycle.screenWidth,StarCycle.screenHeight);
		batch.enableBlending();
        for (ListIterator<MenuOrb> itr = fakeOrbs.listIterator(); itr.hasNext();){
            FakeOrb o = itr.next();
            o.draw(batch);
            o.update(delta);
        }
		batch.end();
		
		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // TODO correct time?
		ui.draw();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		fakeOrbs.clear();
	}

    public void touchDragged(int x, int y, int pointer) {
        Vector2 vec = new Vector2(x, StarCycle.screenHeight - y);
        int textureSwitch = (Math.random() > .5) ? 1 : 0;
        boolean left = (textureSwitch == 1) ? true : false;
		fakeOrbs.add(new MenuOrb(Texturez.fakeorbTextures[textureSwitch], StarCycle.screenHeight/60f, vec,
                StarCycle.screenWidth, StarCycle.screenHeight, left));
	}
	public String toString(){
		return "MenuScreen";
	}
}
