package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class UISlider extends Actor {

	Vector2 position;
	final Vector2 touchSize;
	boolean xDir;
	float slideMin;
	float slideMax;
	float slideLength;
	
	TextureRegion sliderImage;
	Vector2 sliderSize;
	float sliderAngle;
	Vector2 sliderPos;
	
	TextureRegion backgroundImage;
	Vector2 backgroundSize;
	float backgroundAngle;

	public UISlider(Vector2 position, Vector2 touchSize, boolean slideX) {
		this.position = position;
		this.touchSize = touchSize;
		xDir = slideX;
		this.setBounds(position.x - touchSize.x / 2f,
				position.y - touchSize.y / 2f, 
				touchSize.x, touchSize.y);
		this.setOrigin(touchSize.x / 2f, touchSize.y / 2f);
		slideMin = xDir ? this.getX() : this.getY();
		slideMax = xDir ? this.getX() + touchSize.x : this.getY() + touchSize.y;
		slideLength = slideMax-slideMin;
        addListener(new DragListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (super.touchDown(event, x, y, pointer, button)) {
                    setSlider(x, y);
                    return true;
                } else {
                    return false;
                }
            }

            public void drag(InputEvent event, float x, float y, int pointer) {
                setSlider(x, y);
            }
        });
	}
	
	public UISlider(Vector2 position, Vector2 touchSize, boolean slideX, TextureRegion sliderImage, Vector2 sliderSize) {
		this(position, touchSize, slideX, sliderImage, sliderSize, 0f);
	}
	
	public UISlider(Vector2 position, Vector2 touchSize, boolean slideX, TextureRegion sliderImage, Vector2 sliderSize, float sliderAngle) {
		this(position, touchSize, slideX);
		this.sliderImage = sliderImage;
		this.sliderSize = sliderSize;
		this.sliderAngle = sliderAngle;
		sliderPos = new Vector2(position.x, position.y);
		slideMin += xDir ? sliderSize.x/2f : sliderSize.y/2f;
		slideMax -= xDir ? sliderSize.x/2f : sliderSize.y/2f;
		slideLength = slideMax-slideMin;
	}

	public UISlider(Vector2 position, Vector2 touchSize, boolean slideX, TextureRegion sliderImage, Vector2 sliderSize, TextureRegion backgroundImage, Vector2 backgroundSize) {
		this(position, touchSize, slideX, sliderImage, sliderSize, 0f, backgroundImage, backgroundSize, 0f);
	}
	
	public UISlider(Vector2 position, Vector2 touchSize, boolean slideX, TextureRegion sliderImage, Vector2 sliderSize, float sliderAngle, TextureRegion backgroundImage, Vector2 backgroundSize, float backgroundAngle) {
		this(position, touchSize, slideX, sliderImage, sliderSize, sliderAngle);
		this.backgroundImage = backgroundImage;
		this.backgroundSize = backgroundSize;
		this.backgroundAngle = backgroundAngle;
	}
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (backgroundImage!=null) {
			batch.draw(backgroundImage, position.x-backgroundSize.x/2f, position.y-backgroundSize.y/2f, backgroundSize.x/2f, backgroundSize.y/2f, backgroundSize.x, backgroundSize.y, 1f, 1f, backgroundAngle);
		}
		if (sliderImage!=null) {
			batch.draw(sliderImage, sliderPos.x-sliderSize.x/2f, sliderPos.y-sliderSize.y/2f, sliderSize.x/2f, sliderSize.y/2f, sliderSize.x, sliderSize.y, 1f, 1f, sliderAngle);
		}
	}
	
	public void setSlider(float x, float y) {
		if (xDir) {
			sliderPos.set(MathUtils.clamp(slideMin+x, slideMin, slideMax), sliderPos.y);
		}
		else {
			sliderPos.set(sliderPos.x, MathUtils.clamp(slideMin+y, slideMin, slideMax));
		}
	}
	
//	public void moveSlider(float x, float y) {
//		if (xDir) {
//			sliderPos.add(x, 0f);
//		}
//		else {
//			Gdx.app.log("UISlider", "y="+y);
//			sliderPos.add(0f, y);
//		}
//	}
	
	public float getPercent() {
		return xDir ? (sliderPos.x - slideMin) / slideLength : (sliderPos.y - slideMin) / slideLength;
	}
	
	public void setPercent(float p) {
        // Set both x and y, because setSlider will determine which is appropriate.
        setSlider(p*slideLength, p*slideLength);
	}
}
