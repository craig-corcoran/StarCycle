package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BackgroundManager {
    private int num;
    int[] stripRange = new int[]{1, 3};
	private final int blockNum; // Number of large blocks.
    ArrayList<Integer> blockInds = new ArrayList<Integer>();
    private final int stripNum; // Number of thinner strips.
    ArrayList<Integer> stripInds = new ArrayList<Integer>();
    int[] blockRange = new int[]{2, 3};
    ArrayList<Integer> typeInds = new ArrayList<Integer>();
    int[] textureTypes;
    int[] textureInds;
    float[] dims;
    ArrayList<TextureRegion[]> textures = new ArrayList<TextureRegion[]>();
    int r = Texturez.bgBlockDims[0];
    int c;
    ArrayList<Color> colors = new ArrayList<Color>();
    int cNum = 7;
    float[] texturePadRange = new float[]{StarCycle.pixelsPerMeter, StarCycle.pixelsPerMeter * 3f};
    ArrayList<LayeredButton> buttons = new ArrayList<LayeredButton>();
    private static float blockDim = StarCycle.screenHeight;
    private static float stripDim = StarCycle.screenHeight/3f;
	private static float tileDim = blockDim/Texturez.bgBlockDims[0];
    private Vector2 tdv = new Vector2(1f, 1f).scl(tileDim); // Tile dim vector.
	private ArrayList<Integer> panelBlocks = new ArrayList<Integer>();
    float bgDir = 1 - MathUtils.random(1) * 2;
    float[] bgSpRange = new float[]{10f / 60f, 10f / 60f};
	private float bgSpeed = MathUtils.random(bgSpRange[0], bgSpRange[1]) * bgDir;
    private final ArrayList<Vector2> panelPos = new ArrayList<Vector2>(); // Position of the panel blocks.
	private final float bgLength;
    private static Vector2 starTileDim = new Vector2(StarCycle.screenWidth/(Texturez.bgStarsDims[1]-1), StarCycle.screenHeight/Texturez.bgStarsDims[0]);
    private int starTileNum = Texturez.bgStars.length;
    ArrayList<LayeredButton> starButtons = new ArrayList<LayeredButton>();

    private static float moteDim = 5f/StarCycle.pixelsPerMeter; // Side length of motes in meters.
	private Vector2 pos = new Vector2();
	public boolean noDraw = false;
	
	public BackgroundManager() {

        colors.add(Texturez.cyan);
        colors.add(Texturez.navy);
        colors.add(Texturez.yellow);
        colors.add(Texturez.red);
        colors.add(Texturez.matcha);
        colors.add(Texturez.spinach);
        colors.add(Texturez.magenta);
        colors.add(Texturez.indigo);

		// Randomize the background:
        blockNum = MathUtils.random(blockRange[0], blockRange[1]);
        blockInds = permutation(0, Texturez.bgBlocks.size() - 1, blockNum);
        stripNum = MathUtils.random(stripRange[0], stripRange[1]);
        stripInds = permutation(0, Texturez.bgStrips.size() - 1, stripNum);
        num = blockNum + stripNum;
        typeInds = permutation(0, num - 1);
        textureTypes = new int[num];
        textureInds = new int[num];
        dims = new float[num];
        for (int i = 0; i < num; i++) {
            int j = typeInds.get(i);
            textureTypes[j] = i < blockNum ? 0 : 1;
            textureInds[j] = i < blockNum ? blockInds.get(i) : stripInds.get(i-blockNum);
            dims[j] = i < blockNum ? blockDim : stripDim;
        }

        buttons.add(getButton(MathUtils.random(-dims[0] + tileDim/2f, tileDim/2f), 0));

        for (int i = 1; i < num; i++) {
            float lastX = buttons.get(i-1).getCenter().x + dims[i-1] + MathUtils.random(texturePadRange[0], texturePadRange[1]);
            buttons.add(getButton(lastX, i));
        }

		bgLength = buttons.get(buttons.size()-1).getCenter().x-buttons.get(0).getCenter().x+dims[num-1]+MathUtils.random(texturePadRange[0], texturePadRange[1]);

        Vector2 starPos = new Vector2(starTileDim.x/2f, StarCycle.screenHeight - starTileDim.y/2f);
        for (int i =0; i < starTileNum; i ++) {
            LayeredButton button = new LayeredButton(new Vector2(starPos.x + starTileDim.x*(i%Texturez.bgStarsDims[1]), starPos.y-starTileDim.y*(i/Texturez.bgStarsDims[1])));
            button.addLayer(new SpriteLayer(Texturez.bgStars[i], starTileDim));
            starButtons.add(button);
        }
				
	}

    public void update() {
		for (int i = 0; i < num; i++) {
            buttons.get(i).moveCenter(bgSpeed, 0f);
			if (buttons.get(i).getCenter().x+blockDim -tileDim/2f < 0f) {
				buttons.get(i).moveCenter(bgLength, 0f);
			}
			if (buttons.get(i).getCenter().x+blockDim -tileDim/2f > bgLength) {
				buttons.get(i).moveCenter(-bgLength, 0f);
			}
		}
        for (int i = 0; i < starButtons.size(); i ++) {
            starButtons.get(i).moveCenter(bgSpeed/2f, 0f);
            if (starButtons.get(i).getCenter().x + starTileDim.x / 2f < 0f) {
                starButtons.get(i).moveCenter(StarCycle.screenWidth + starTileDim.x, 0f);
            }
            if (starButtons.get(i).getCenter().x  - tileDim / 2f > StarCycle.screenWidth) {
                starButtons.get(i).moveCenter(-StarCycle.screenWidth - starTileDim.x, 0f);
            }
        }
	}
	
	public void draw(SpriteBatch batch) {
		if (!noDraw) {
            batch.disableBlending();
			for (int i = 0;  i < num; i++) {
                buttons.get(i).draw(batch, 1f);
			}
            batch.enableBlending();
            for (int i = 0; i < starButtons.size(); i ++) {
                starButtons.get(i).draw(batch, 1f);
            }
		}
	}
	
	void drawTiled(SpriteBatch batch, TextureRegion[] texture, float x, float y, int r, int c, float s, float angle) {
		for (int i=0; i < texture.length; i++) {
			float xt = x + s*(i%c);
			float yt = y + s*(r - 1 - i/c);
			if (xt < StarCycle.screenWidth & yt < StarCycle.screenHeight & 0 < xt + s & 0 < yt + s) {
				batch.draw(texture[i], xt, yt, s/2, s/2, s, s, 1f, 1f, angle);
			}
		}
	}
	
	private void wrapPositions(ArrayList<Vector2> positions, float padWidth, float padHeight) {
        for (Vector2 position : positions) {
            pos = position;
            if (pos.x < 0 - padWidth) {
                pos.x = pos.x + StarCycle.meterWidth + padWidth;
            }
            if (pos.y < 0 - padHeight) {
                pos.y = pos.y + StarCycle.meterHeight + padHeight;
            }
            pos.set(pos.x % (StarCycle.meterWidth + moteDim), pos.y % (StarCycle.meterHeight + moteDim)); // should operate in place
        }
	}

    private LayeredButton getButton(float lastX, int i) {
        textures = (textureTypes[i] == 0) ? Texturez.bgBlocks : Texturez.bgStrips;
        c = (textureTypes[i] == 0) ? Texturez.bgBlockDims[1] : Texturez.bgStripDims[1];
        float yOffset = (textureTypes[i] == 0) ? MathUtils.random(-StarCycle.screenHeight/2f, StarCycle.screenHeight/2f) : 0f;
        Vector2 pos = new Vector2(lastX, StarCycle.screenHeight - tileDim/2f + yOffset);
        LayeredButton button = new LayeredButton(pos);
        Color color = colors.get(MathUtils.random(cNum));
        for (int j = 0; j < textures.get(textureInds[i]).length; j ++) {
            button.addLayer(new SpriteLayer(textures.get(textureInds[i])[j], new Vector2((j % c) * tileDim, -(j / c) * tileDim), tdv, color, 0f) {
                @Override
                public void draw(SpriteBatch batch, float parentAlpha, Vector2 groupCenter, float groupAngle) {
                    if (groupCenter.x + getCenter().x - tileDim <= StarCycle.screenWidth && groupCenter.x + getCenter().x + tileDim / 2f >= 0f) {
                        super.draw(batch, parentAlpha, groupCenter, groupAngle);
                    }
                }
            });
        }
        return button;
    }

	private Vector2 getRandomVector(float magnitude) {
		float theta = MathUtils.random(2*MathUtils.PI);
		return new Vector2(magnitude*MathUtils.cos(theta), magnitude*MathUtils.sin(theta));
	}
	
	private ArrayList<Vector2> getRandomVectors(int num, float[] range) {
		ArrayList<Vector2> vecs = new ArrayList<Vector2>();
		for (int i=0; i < num; i++) {
			float speed = MathUtils.random(range[0], range[1]);
			vecs.add(getRandomVector(speed));
		}
		return vecs;
	}
	
	private ArrayList<Vector2> getRandomScreenPos(int num) {
		ArrayList<Vector2> positions = new ArrayList<Vector2>();
		for (int i=0; i < num; i++) {
			positions.add(new Vector2(MathUtils.random(StarCycle.meterWidth), MathUtils.random(StarCycle.meterHeight)));
		}
		return positions;
	}
	
	private ArrayList<Float> getRandomAngles(int num, float[] range) {
		ArrayList<Float> angles = new ArrayList<Float>();
		for (int i=0; i < num; i++) {
			angles.add(MathUtils.random(range[0], range[1]));
		}
		return angles;
	}

    public ArrayList<Integer> permutation(int low, int high) {
        return permutation(low, high, high - low +1);
    }
	
	public ArrayList<Integer> permutation(int low, int high, int num) {
		ArrayList<Integer> perm = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = low; i <= high; i++) {
			list.add(i);
		}
		for (int i = 0; i < num; i++) {
			int p = MathUtils.random(list.size()-1);
			perm.add(list.get(p));
			list.remove(p);
		}
		return perm;
	}
}
