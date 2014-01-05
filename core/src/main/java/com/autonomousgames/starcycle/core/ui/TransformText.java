package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TransformText extends BitmapFontCache {

	private float[] newVertices = new float[0];
	private int idx2;
	private float angle;
	
	Texture texture;
	
	public TransformText(BitmapFont font) {
		this(font, font.usesIntegerPositions());
	}
	
	public TransformText(BitmapFont font, boolean integer) {
		super(font, integer);
		texture = font.getRegion().getTexture();
	}
	
	public void rotate(float theta) {
		
		ArrayList<Vector2> vectors = new ArrayList<Vector2>();
		for (int i = 0; i < 4; i ++) {
			vectors.add(new Vector2());
		}
		Vector2 vc = new Vector2();
		
		updateVertices();
		
		for (int i = 0, n = idx2; i < n; i += 20) {
			vc.set( (newVertices[i] + newVertices[i+15]) / 2f, (newVertices[i+1] + newVertices[i+16]) / 2f);
			for (int j = 0; j < 4; j ++){
				vectors.get(j).set(newVertices[i+j*5], newVertices[i+1+j*5]).rotate(theta);
				newVertices[i+j*5] = vectors.get(j).x;
				newVertices[i+1+j*5] = vectors.get(j).y;
			}
		}
		
		this.angle = theta;
		
	}
	
	@Override
	public void translate(float xAmount, float yAmount) {
		super.translate(xAmount, yAmount);
		updateVertices();
	}
	
	@Override
	public TextBounds addText (CharSequence str, float x, float y, int start, int end) {
		super.addText(str, x, y, start, end);
		updateVertices();
		return getBounds();
	}
	
	@Override
	public TextBounds addMultiLineText (CharSequence str, float x, float y, float alignmentWidth, HAlignment alignment) {
		super.addMultiLineText(str, x, y, alignmentWidth, alignment);
		updateVertices();
		return getBounds();
	}
	
	@Override
	public TextBounds addWrappedText (CharSequence str, float x, float y, float wrapWidth, HAlignment alignment) {
		super.addWrappedText(str, x, y, wrapWidth, alignment);
		updateVertices();
		return getBounds();
	}
	
	@Override
	public void setColor (float color) {
		super.setColor(color);
		updateVertices();
	}

	@Override
	public void setColor (Color tint) {
		super.setColor(tint);
		updateVertices();
	}
	
	@Override
	public void setColor (float r, float g, float b, float a) {
		super.setColor(r, g, b, a);
		updateVertices();
	}
	
	@Override
	public void setColor (Color tint, int start, int end) {
		super.setColor(tint, start, end);
		updateVertices();
	}
	
	public void updateVertices() {
		newVertices = getVertices();
		idx2 = newVertices.length;
	}

	@Override
	public void draw (SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, newVertices, 0, idx2);
	}
	
	public float getRotation() {
		return angle;
	}
	
}
