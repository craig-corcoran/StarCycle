package com.autonomousgames.starcycle.core.screens;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.controllers.MenuController;
import com.autonomousgames.starcycle.core.model.FakeOrb;
import com.autonomousgames.starcycle.core.model.ImageOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.ListIterator;

public class MenuScreen extends GameScreen {

	public ArrayList<ImageOrb> fakeOrbs = new ArrayList<ImageOrb>();
    boolean drawFakeOrbs = true;
	
	public MenuScreen() {
		super();
        background = StarCycle.sc.getBackground();
		Gdx.input.setInputProcessor(new MenuController(this));
	}

    @Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
        background.update();
        background.draw(batch);
        for (ListIterator<ImageOrb> itr = fakeOrbs.listIterator(); itr.hasNext();){
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
        if (drawFakeOrbs) {
		    fakeOrbs.add(new ImageOrb(Texturez.fakeorbTextures[textureSwitch], StarCycle.screenHeight/60f, vec,
                    StarCycle.screenWidth, StarCycle.screenHeight, left));
        }
	}
	public String toString(){
		return "MenuScreen";
	}
}
