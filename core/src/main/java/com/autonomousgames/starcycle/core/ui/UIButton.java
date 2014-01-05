package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.HashMap;


public class UIButton extends Actor {
	
	boolean pressed = false;
	Vector2 touchSize;
	Vector2 position; // position of the center of the texture
	float padding;
	float wrapWidth = StarCycle.screenHeight;
	
	ArrayList<Object> layerListUp = new ArrayList<Object>(); // list of texture regions or sprite caches
	ArrayList<Object> layerListDown = new ArrayList<Object>();
	ArrayList<Object> layerList = new ArrayList<Object>();
	ArrayList<Vector2> positionListUp = new ArrayList<Vector2>();
	ArrayList<Vector2> positionListDown = new ArrayList<Vector2>();
	ArrayList<Vector2> positionList = new ArrayList<Vector2>();
	ArrayList<Vector2> sizeListUp = new ArrayList<Vector2>();
	ArrayList<Vector2> sizeListDown = new ArrayList<Vector2>();
	ArrayList<Vector2> sizeList = new ArrayList<Vector2>();
	private HashMap<Integer,java.lang.CharSequence> stringMap = new HashMap<Integer,java.lang.CharSequence>();
    private final Vector3 z_axis = new Vector3(0f, 0f, 1f);
    private Vector3 pos0; // position used for drawing font
    public boolean active = true;
	
	// TODO need a constructor for wrapped text?
	// A button region with no images. Use for blank buttons and for initializing complex buttons.
	/**
	 * @param position  position of the CENTER of the button
	 * @param touchSize size of valid touch region in pixels
	 */
	public UIButton(Vector2 position, Vector2 touchSize, float padding) {
		this.position=position;
		this.touchSize=touchSize;
		this.padding = padding;
		this.setBounds(position.x - touchSize.x / 2f,
					   position.y - touchSize.y / 2f, 
					   touchSize.x, touchSize.y);
		this.setOrigin(touchSize.x / 2f, touchSize.y / 2f);
	}

	// A button region with a single image (no up/down differentiation).
	/**
	 * @param image     base image to display for button
	 * @param position  position of the CENTER of the button
	 * @param touchSize touchSize size of valid touch region in pixels
	 */
	public UIButton(TextureRegion image, Vector2 position, Vector2 imageSize, Vector2 touchSize, float padding) {
		this(position, touchSize, padding);
		this.layerListUp.add(image);
		this.layerListDown.add(image);
		
		Vector2 imPos = new Vector2(position.x - imageSize.x/2f, position.y - imageSize.y/2f);
		this.positionListUp.add(imPos);
		this.positionListDown.add(imPos);
		this.sizeListUp.add(imageSize);
		this.sizeListDown.add(imageSize);
	}
	
	public UIButton(TextureRegion image, Vector2 position, Vector2 size, float padding) {
		this(image, position, size, size, padding);
	}
	
	public UIButton(BitmapFont font,Color color, java.lang.CharSequence text, Vector2 position, float padding) {
		BitmapFontCache layer = new BitmapFontCache(font);
		layer.setText(text, 0, 0);
		float height = layer.getBounds().height;
		float width = layer.getBounds().width;
		layer.setPosition(position.x - width/2f, position.y + height/2f); // origin for bitmapfont layer is top left

		this.touchSize = new Vector2(width, height);
		this.padding = padding;
		addLayer(layer, color, position, text);
		this.setOrigin(width / 2f, height / 2f);
		this.setBounds(position.x - touchSize.x / 2f,
				   	   position.y - touchSize.y / 2f, 
				   	   touchSize.x, touchSize.y);
	}
	
	public void removeLayer() {
		layerListUp.remove(layerListUp.size()-1);
		layerListDown.remove(layerListDown.size()-1);
		positionListUp.remove(positionListUp.size()-1);
		positionListDown.remove(positionListDown.size()-1);
		sizeListUp.remove(sizeListUp.size()-1);
		sizeListDown.remove(sizeListDown.size()-1);
	}

	// Add a texture region to both the up and down lists.
	public void addLayer(TextureRegion image, Vector2 position, Vector2 size){
		addUpLayer(image, position, size);
		addDownLayer(image, position, size);
	}

	// Add a layer to only the up list.
	public void addUpLayer(TextureRegion image, Vector2 position, Vector2 size){
		layerListUp.add(image);
		positionListUp.add(new Vector2(position.x - size.x/2f, position.y - size.y/2f));
		sizeListUp.add(size);
	}

	// Add a layer to only the down list.
	public void addDownLayer(TextureRegion image, Vector2 position, Vector2 size){
		layerListDown.add(image);
		positionListDown.add(new Vector2(position.x - size.x/2f, position.y - size.y/2f));
		sizeListDown.add(size);
	}
	
	public void addLayer(BitmapFont font, Color color, java.lang.CharSequence text, Vector2 position, float wrapWidth){ 
		this.wrapWidth = wrapWidth; // currently only one global wrap width, different layers cannot have different wrap widths
		BitmapFontCache layer = new BitmapFontCache(font);
		TextBounds bounds = layer.setWrappedText(text, 0, 0, this.wrapWidth);
		
		float height = bounds.height; //layer.getBounds().height;
		float width = bounds.width; //layer.getBounds().width;
		layer.setPosition(position.x - width/2f, position.y + height/2f); // origin for bitmapfont layer is top left
		addLayer(layer, color, position, text);
	}
	
	public void addLayer(BitmapFont font, Color color, java.lang.CharSequence text, Vector2 position){ 
		BitmapFontCache layer = new BitmapFontCache(font);
		layer.setText(text, 0, 0);
		layer.setColor(color);
		float height = layer.getBounds().height;
		float width = layer.getBounds().width;
		layer.setPosition(position.x - width/2f, position.y + height/2f); // origin for bitmapfont layer is top left
		addLayer(layer, color, position, text);
	}
	
	private void addLayer(BitmapFontCache layer,Color color, Vector2 position, java.lang.CharSequence text) {
		layerListUp.add(layer);
		layerListDown.add(layer);
		layer.setColor(color);
		stringMap.put(layerListUp.size()-1, text);

		Vector2 size = new Vector2(layer.getBounds().width, layer.getBounds().height );
		sizeListUp.add(size); // size is determined by bitmapfont
		sizeListDown.add(size); // size is determined by bitmapfont
		positionListUp.add(new Vector2(position.x - size.x/2f, position.y - size.y/2f));
		positionListDown.add(new Vector2(position.x - size.x/2f, position.y - size.y/2f));
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		layerList = pressed ? layerListDown : layerListUp; 
		positionList = pressed ? positionListDown : positionListUp;
		sizeList = pressed ? sizeListDown : sizeListUp;
		drawLayers(batch, layerList, positionList, sizeList);
	}
	
	void drawLayers(SpriteBatch batch, ArrayList<Object> layerList, ArrayList<Vector2> positionList, ArrayList<Vector2> sizeList) {
		for (int i = 0 ; i < layerList.size() ; i++) {
            Object layer = layerList.get(i);
            Vector2 size;
            if (layer instanceof TextureRegion) {
				position = positionList.get(i);
				size = sizeList.get(i);
				batch.draw((TextureRegion) layer, position.x, position.y, size.x/2f, size.y/2f, size.x, size.y, 1f,1f, this.getRotation());
			}
			else if (layer instanceof BitmapFontCache) {
				if (this.getRotation() != 0f) {
					position = positionList.get(i);
					size = sizeList.get(i);
					batch.setTransformMatrix( new Matrix4().rotate(z_axis, this.getRotation())); //.translate(width / 2f, height/2f, 0f));					
					
					if (this.getRotation() == 90f) {
						pos0 = new Vector3(position.x + size.x / 2f - size.y /2f, position.y - size.x / 2f + size.y /2f, 0f); 
					}
					else if (this.getRotation() == -90f) {
						pos0 = new Vector3(position.x + size.x / 2f + size.y /2f, position.y + size.x / 2f + size.y /2f, 0f); 
					}
					
					pos0.mul(batch.getTransformMatrix().tra());
					
					((BitmapFontCache) layer).setPosition(pos0.x, pos0.y);
					((BitmapFontCache) layer).draw(batch);
					batch.setTransformMatrix(new Matrix4().idt());
				}
				
				else {
					((BitmapFontCache) layer).draw(batch);	
				}
			}
		}
		
	}

	// Checks for a hit including possible padding. (Relative to the object's position.)
	public Actor isHit(float x, float y){
		return (x > -padding && y > -padding && x < touchSize.x + padding && y < touchSize.y + padding) ? this : null;
	}
}
