package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.model.Base.BaseType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.ArrayList;

public class BaseButton extends ToggleButton {
	
	ArrayList<Integer> levels = new ArrayList<Integer>();
	ArrayList<Layer> baseLayers = new ArrayList<Layer>(); // The layers of the main base graphic.
	ArrayList<Color> layerColors = new ArrayList<Color>();
	
	ArrayList<Layer> bottomLayers = new ArrayList<Layer>(); // Layers beneath the base, usually for UI features.
	ArrayList<Layer> topLayers = new ArrayList<Layer>(); // Layers above the base, usually for UI features.
	
	ArrayList<Vector2> sizes = new ArrayList<Vector2>();
	float[] speeds = new float[]{25f, 30f, 35f};
	
	Vector2 mid = new Vector2(0f, 0f);
	
	BaseType skin;
	Color[] colors;
	
	int level = 0;
	
	public BaseButton(BaseType baseType, Color[] colors, Vector2 center, Vector2 baseDims) {
		this(baseType, colors, center, baseDims, 0);
	}
		
	public BaseButton(BaseType baseType, Color[] colors, Vector2 center, Vector2 baseDims, int level) {
		super(center, baseDims);
		skin = baseType;
		this.colors = colors;
		this.level = level;
		sizes.add(baseDims.cpy().scl(.9f));
		sizes.add(baseDims.cpy().scl(1.2f));
		sizes.add(baseDims.cpy().scl(1.5f));

		switch (skin) {
		case MALUMA:
			addBaseLayer(Texturez.baseMaluma0, sizes.get(0), speeds[0], 0, 0);
			addBaseLayer(Texturez.baseMaluma0, sizes.get(0), -speeds[0], 0, 1);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(1), speeds[1], 1, 0);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(1), -speeds[1], 1, 1);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(2), speeds[0], 2, 0);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(2), -speeds[0], 2, 0);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(1), speeds[2], 2, 1);
			addBaseLayer(Texturez.baseMaluma1, sizes.get(1), -speeds[2], 2, 1);
			break;
		case TAKETE:
			addBaseLayer(Texturez.baseTakete0, sizes.get(0), speeds[0], 0, 0);
			addBaseLayer(Texturez.baseTakete0, sizes.get(0), -speeds[0], 0, 1);
			addBaseLayer(Texturez.baseTakete1, sizes.get(1), speeds[1], 1, 0);
			addBaseLayer(Texturez.baseTakete1, sizes.get(1), -speeds[1], 1, 1);
			addBaseLayer(Texturez.baseTakete1, sizes.get(2), speeds[0], 2, 0);
			addBaseLayer(Texturez.baseTakete1, sizes.get(2), -speeds[0], 2, 0);
			addBaseLayer(Texturez.baseTakete1, sizes.get(1), speeds[2], 2, 1);
			addBaseLayer(Texturez.baseTakete1, sizes.get(1), -speeds[2], 2, 1);
			break;
		case TARGET:
			ArrayList<Vector2> size0 = new ArrayList<Vector2>();
			ArrayList<Vector2> size1 = new ArrayList<Vector2>();
			ArrayList<Vector2> size2 = new ArrayList<Vector2>();
			for (int i = 0; i < 3; i ++) {
				size0.add(sizes.get(i).cpy().scl(64f / 256f));
				size1.add(sizes.get(i).cpy().scl(92f / 256f));
				size2.add(sizes.get(i).cpy().scl(0.5f));
			}
			SpriteLayer layer00 = new SpriteLayer(Texturez.baseTarget0, size0.get(0).cpy().scl(0.5f), size0.get(0), colors[0], 0f);
			layer00.setOrigin(0f, 0f);
			SpriteLayer layer01 = new SpriteLayer(Texturez.baseTarget0, size0.get(1).cpy().scl(0.5f), size0.get(1), colors[0], 0f);
			layer01.setOrigin(0f, 0f);
			SpriteLayer layer02 = new SpriteLayer(Texturez.baseTarget0, size0.get(2).cpy().scl(0.5f), size0.get(2), colors[0], 0f);
			layer02.setOrigin(0f, 0f);
			SpriteLayer layer10 = new SpriteLayer(Texturez.baseTarget1, size1.get(0).cpy().scl(0.5f), size1.get(0), colors[0], 0f);
			layer10.setOrigin(0f, 0f);
			SpriteLayer layer11 = new SpriteLayer(Texturez.baseTarget1, size1.get(1).cpy().scl(0.5f), size1.get(1), colors[0], 0f);
			layer11.setOrigin(0f, 0f);
			SpriteLayer layer12 = new SpriteLayer(Texturez.baseTarget1, size1.get(2).cpy().scl(0.5f), size1.get(2), colors[0], 0f);
			layer12.setOrigin(0f, 0f);
			SpriteLayer layer20 = new SpriteLayer(Texturez.baseTarget2, size2.get(0).cpy().scl(0.5f), size2.get(0), colors[0], 0f);
			layer20.setOrigin(0f, 0f);
			SpriteLayer layer21 = new SpriteLayer(Texturez.baseTarget2, size2.get(1).cpy().scl(0.5f), size2.get(1), colors[0], 0f);
			layer21.setOrigin(0f, 0f);
			SpriteLayer layer22 = new SpriteLayer(Texturez.baseTarget2, size2.get(2).cpy().scl(0.5f), size2.get(2), colors[0], 0f);
			layer22.setOrigin(0f, 0f);
			SpriteLayer layer30 = new SpriteLayer(Texturez.baseTarget3, size2.get(0).cpy().scl(0.5f), size2.get(0), colors[0], 0f);
			layer30.setOrigin(0f, 0f);
			SpriteLayer layer31 = new SpriteLayer(Texturez.baseTarget3, size2.get(1).cpy().scl(0.5f), size2.get(1), colors[0], 0f);
			layer31.setOrigin(0f, 0f);
			SpriteLayer layer32 = new SpriteLayer(Texturez.baseTarget3, size2.get(2).cpy().scl(0.5f), size2.get(2), colors[0], 0f);
			layer32.setOrigin(0f, 0f);
			
			modAndAddBaseLayer(layer10, speeds[0], 0f, 0f, 0, 0);
			modAndAddBaseLayer(layer10, speeds[0], 120f, 0f, 0, 0);
			modAndAddBaseLayer(layer10, speeds[0], 240f, 0f, 0, 0);
			modAndAddBaseLayer(layer30, speeds[0], 0f, 0f, 0, 0);
			modAndAddBaseLayer(layer30, speeds[0], 120f, 0f, 0, 0);
			modAndAddBaseLayer(layer30, speeds[0], 240f, 0f, 0, 0);
//			modAndAddBaseLayer(layer20, -speeds[0]*.5f, 0f, 0f, 0, 0);
			modAndAddBaseLayer(layer10, -speeds[0], 60f, 0f, 0, 1);
			modAndAddBaseLayer(layer10, -speeds[0], 180f, 0f, 0, 1);
			modAndAddBaseLayer(layer10, -speeds[0], 300f, 0f, 0, 1);
			modAndAddBaseLayer(layer30, -speeds[0], 0f, 0f, 0, 1);
			modAndAddBaseLayer(layer30, -speeds[0], 120f, 0f, 0, 1);
			modAndAddBaseLayer(layer30, -speeds[0], 240f, 0f, 0, 1);
//			modAndAddBaseLayer(layer00, speeds[0]*.5f, 180f, 0f, 0, 1);
			
			modAndAddBaseLayer(layer11, speeds[1], 0f, 0f, 1, 0);
			modAndAddBaseLayer(layer11, speeds[1], 90f, 0f, 1, 0);
			modAndAddBaseLayer(layer11, speeds[1], 180f, 0f, 1, 0);
			modAndAddBaseLayer(layer11, speeds[1], 270f, 0f, 1, 0);
			modAndAddBaseLayer(layer31, speeds[1], 0f, 0f, 1, 0);
			modAndAddBaseLayer(layer31, speeds[1], 90f, 0f, 1, 0);
			modAndAddBaseLayer(layer31, speeds[1], 180f, 0f, 1, 0);
			modAndAddBaseLayer(layer31, speeds[1], 270f, 0f, 1, 0);
			modAndAddBaseLayer(layer01, -speeds[1]*2f, 0f, 0f, 1, 0);
			modAndAddBaseLayer(layer01, -speeds[1]*2f, 180f, 0f, 1, 0);
			modAndAddBaseLayer(layer11, -speeds[1], 45f, 0f, 1, 1);
			modAndAddBaseLayer(layer11, -speeds[1], 135f, 0f, 1, 1);
			modAndAddBaseLayer(layer11, -speeds[1], 225f, 0f, 1, 1);
			modAndAddBaseLayer(layer11, -speeds[1], 315f, 0f, 1, 1);
			modAndAddBaseLayer(layer31, -speeds[1], 0f, 0f, 1, 1);
			modAndAddBaseLayer(layer31, -speeds[1], 90f, 0f, 1, 1);
			modAndAddBaseLayer(layer31, -speeds[1], 180f, 0f, 1, 1);
			modAndAddBaseLayer(layer31, -speeds[1], 270f, 0f, 1, 1);
			modAndAddBaseLayer(layer01, speeds[1]*2f, 90f, 0f, 1, 1);
			modAndAddBaseLayer(layer01, speeds[1]*2f, 270f, 0f, 1, 1);
			
			modAndAddBaseLayer(layer12, speeds[2], 0f, 0f, 2, 0);
			modAndAddBaseLayer(layer12, speeds[2], 90f, 0f, 2, 0);
			modAndAddBaseLayer(layer12, speeds[2], 180f, 0f, 2, 0);
			modAndAddBaseLayer(layer12, speeds[2], 270f, 0f, 2, 0);
			modAndAddBaseLayer(layer32, speeds[2], 0f, 0f, 2, 0);
			modAndAddBaseLayer(layer32, speeds[2], 90f, 0f, 2, 0);
			modAndAddBaseLayer(layer32, speeds[2], 180f, 0f, 2, 0);
			modAndAddBaseLayer(layer32, speeds[2], 270f, 0f, 2, 0);
			modAndAddBaseLayer(layer22, -speeds[2]*2f, 0f, 0f, 2, 0);
			modAndAddBaseLayer(layer22, -speeds[2]*2f, 90f, 0f, 2, 0);
			modAndAddBaseLayer(layer22, -speeds[2]*2f, 180f, 0f, 2, 0);
			modAndAddBaseLayer(layer22, -speeds[2]*2f, 270f, 0f, 2, 0);
			modAndAddBaseLayer(layer02, -speeds[2]*2f, 45f, 0f, 2, 0);
			modAndAddBaseLayer(layer02, -speeds[2]*2f, 225f, 0f, 2, 0);
			modAndAddBaseLayer(layer12, -speeds[2], 22.5f, 0f, 2, 1);
			modAndAddBaseLayer(layer12, -speeds[2], 112.5f, 0f, 2, 1);
			modAndAddBaseLayer(layer12, -speeds[2], 202.5f, 0f, 2, 1);
			modAndAddBaseLayer(layer12, -speeds[2], 292.5f, 0f, 2, 1);
			modAndAddBaseLayer(layer32, -speeds[2], 0f, 0f, 2, 1);
			modAndAddBaseLayer(layer32, -speeds[2], 90f, 0f, 2, 1);
			modAndAddBaseLayer(layer32, -speeds[2], 180f, 0f, 2, 1);
			modAndAddBaseLayer(layer32, -speeds[2], 270f, 0f, 2, 1);
			modAndAddBaseLayer(layer02, speeds[2]*2f, 0f, 0f, 2, 1);
			modAndAddBaseLayer(layer02, speeds[2]*2f, 90f, 0f, 2, 1);
			modAndAddBaseLayer(layer02, speeds[2]*2f, 180f, 0f, 2, 1);
			modAndAddBaseLayer(layer02, speeds[2]*2f, 270f, 0f, 2, 1);
			modAndAddBaseLayer(layer22, speeds[2]*2f, 0f, 0f, 2, 1);
			modAndAddBaseLayer(layer22, speeds[2]*2f, 180f, 0f, 2, 1);
			break;
		case DERELICT:
			addBaseLayer(Texturez.baseDerelict0a, sizes.get(0), speeds[0], 0, 0);
			addBaseLayer(Texturez.baseDerelict0b, sizes.get(0), -speeds[0], 0, 1);
			addBaseLayer(Texturez.baseDerelict1a, sizes.get(0), speeds[1], 1, 0);
			addBaseLayer(Texturez.baseDerelict1b, sizes.get(0), -speeds[1], 1, 1);
			addBaseLayer(Texturez.baseDerelict2a, sizes.get(1), speeds[2], 2, 0);
			addBaseLayer(Texturez.baseDerelict2b, sizes.get(1), -speeds[2], 2, 1);
			break;
		case CLOCKWORK:
			addBaseLayer(Texturez.baseClockwork0a, sizes.get(0), speeds[0], 0, 0);
			addBaseLayer(Texturez.baseClockwork0b, sizes.get(0), -speeds[0], 0, 1);
			addBaseLayer(Texturez.baseClockwork1a, sizes.get(1), speeds[1], 1, 0);
			addBaseLayer(Texturez.baseClockwork1b, sizes.get(1), -speeds[1], 1, 1);
			addBaseLayer(Texturez.baseClockwork2a, sizes.get(2), speeds[2], 2, 0);
			addBaseLayer(Texturez.baseClockwork2b, sizes.get(2).cpy().scl(0.25f), -speeds[2]*3.75f*2f-speeds[2], 2, 0);
			addBaseLayer(Texturez.baseClockwork2c, sizes.get(2), -speeds[2], 2, 1);
			addBaseLayer(Texturez.baseClockwork2b, new Vector2(0, sizes.get(2).y * 0.22f), sizes.get(2).cpy().scl(0.25f), speeds[2]*3.75f*2f-speeds[2], 360f/16f, -speeds[2], 2, 1);
			addBaseLayer(Texturez.baseClockwork2b, new Vector2(0, sizes.get(2).y * -0.22f), sizes.get(2).cpy().scl(0.25f), speeds[2]*3.75f*2f-speeds[2], 360f/16f, -speeds[2], 2, 1);
			break;
		}
		
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		layers = bottomLayers;
		super.draw(batch, parentAlpha);
		
		for (int i = 0; i < baseLayers.size(); i++) {
			if (levels.get(i) == level) {
				baseLayers.get(i).draw(batch, parentAlpha, center, getRotation());
			}
		}
		
		layers = topLayers;
		super.draw(batch, parentAlpha);
	}
	
	public void setColors(Color[] newColors) {
		for (int i = 0; i < baseLayers.size(); i ++) {
			if (layerColors.get(i) == colors[0]) {
				baseLayers.get(i).setColor(newColors[0]);
			}
			else {
				baseLayers.get(i).setColor(newColors[1]);
			}
		}
		colors = newColors;
	}
	
	@Override
	public LayeredButton addLayer(Layer layer, LayerType type) {
		assert false : "Use addTopLayer or addBottomLayer.";
		return null;
	}
	
	private void addBaseLayer(TextureRegion texture, Vector2 dims, float rotSpeed, int level, int color) {
		addBaseLayer(texture, new Vector2(0f, 0f), dims, rotSpeed, 0f, 0f, level, color);
	}
	
	private void addBaseLayer(SpriteLayer layer, int level, int color){
		baseLayers.add(layer);
		levels.add(level);
		layerColors.add(colors[color]);
	}
	
	private void addBaseLayer(TextureRegion texture, Vector2 pos, Vector2 dims, float rotSpeed, float theta0, float revSpeed, int level, int color) {
		SpriteLayer layer = new SpriteLayer(texture, pos, dims, colors[color], theta0);
		layer.setRotationSpeed(rotSpeed).setRevolutionSpeed(revSpeed);
		addBaseLayer(layer, level, color);
	}

	private void modAndAddBaseLayer(SpriteLayer layer, float rotSpeed, float theta0, float revSpeed, int level, int color) {
        SpriteLayer layerCopy = null;
        try {
            layerCopy = layer.clone();
        } catch (CloneNotSupportedException e) {
            Gdx.app.log("BaseButton", e.fillInStackTrace() + "");
        }
        layerCopy.setRotationSpeed(rotSpeed).setRevolutionSpeed(revSpeed).setSpriteColor(colors[color]);
		layerCopy.setRotation(theta0);
		addBaseLayer(layerCopy, level, color);
	}
	
	public BaseButton addBottomLayer(Layer layer) {
		addBottomLayer(layer, LayerType.ALWAYS);
		return this;
	}
	
	public BaseButton addTopLayer(Layer layer) {
		addTopLayer(layer, LayerType.ALWAYS);
		return this;
	}
	
	public BaseButton addBottomLayer(Layer layer, LayerType type) {
		bottomLayers.add(layer);
		layer.type = type;
		return this;
	}

	public BaseButton addTopLayer(Layer layer, LayerType type) {
		topLayers.add(layer);
		layer.type = type;
		return this;
	}
	
	@Override
	public void setLayerCenter(float x, float y, int index) {
		assert false : "This doesn't apply to BaseButtons.";
	}

	@Override
	public void setLayerPosition(float x, float y, int index) {
		assert false : "This doesn't apply to BaseButtons.";
	}

	@Override
	public void addLayerAction(Action action, int index) {
		assert false : "This doesn't apply to BaseButtons.";
	}

	@Override
	public LayeredButton setLayerColor(Color tint, int index) {
		assert false : "Use setColors(Color[] colors).";
		return null;
	}
	
	@Override
	public void setColor (Color tint) {
		assert false : "Use setColors(Color[] colors).";
	}
	
	@Override
	public void setAllColors (Color tint) {
		assert false : "Use setColors(Color[] colors).";
	}
	
	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);
        for (Layer bottomLayer : bottomLayers) {
            bottomLayer.setRotation(angle);
        }
        for (Layer baseLayer : baseLayers) {
            baseLayer.setRotation(angle);
        }
        for (Layer topLayer : topLayers) {
            topLayer.setRotation(angle);
        }
	}
	
	@Override
	public LayeredButton rotateLayers(float angle){
        for (Layer bottomLayer : bottomLayers) {
            bottomLayer.rotate(angle);
        }
        for (Layer baseLayer : baseLayers) {
            baseLayer.rotate(angle);
        }
        for (Layer topLayer : topLayers) {
            topLayer.rotate(angle);
        }
		return this;
	}
	
	@Override
	public Color getLayerColor(int index) {
		assert false : "Use getAllColors().";
		return null;
	}

	@Override
	public Color[] getAllColors() {
		return colors;
	}
	
	@Override
	public void act(float delta) {
		layers = topLayers;
		super.act(delta);
		layers = baseLayers;
		super.act(delta);
		layers = bottomLayers;
		super.act(delta);
	}
	
}

