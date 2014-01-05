package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BackgroundManager {
	private final int blockNum; // Number of large blocks making up the "panel".
    private static float blockDim = 10000f/StarCycle.screenHeight; // Side length of blocks in meters.
	private static float tileDim = blockDim/Texturez.bgBlockDims[0]; 
	private ArrayList<Integer> panelBlocks = new ArrayList<Integer>();
	private float  panelSpeed;
    private final ArrayList<Vector2> panelPos = new ArrayList<Vector2>(); // Position of the panel blocks.
	private final float panelLength;

    private final int shapeNum = 15; // Number of shapes floating on the screen.
	private final ArrayList<Vector2> shapePos;
	private final ArrayList<Vector2> shapeVel;
    private final ArrayList<Float> shapeAngle;
	private final ArrayList<Float> shapeAngleVel;
    private static float shapeDim = 15f/StarCycle.pixelsPerMeter; // Side length of shapes in meters.

	private final int numLayers = 3;
	private final int motesPerLayer = 20;
	private ArrayList<ArrayList<Vector2>> motePositions = new ArrayList<ArrayList<Vector2>>();
	private Vector2[] layerVel = new Vector2[numLayers];
    private static float moteDim = 5f/StarCycle.pixelsPerMeter; // Side length of motes in meters.
	private Vector2 pos = new Vector2();
	public boolean noDraw = false;
	
	public BackgroundManager() {
	
		// Randomize the background:
        int[] blockRange = new int[]{3, 3};
        blockNum = MathUtils.random(blockRange[0], blockRange[1]);
		panelBlocks = permutation(0, Texturez.bgBlocks.size()-1, blockNum);
        float panelDir = 1 - MathUtils.random(1) * 2;
        float[] panelSpRange = new float[]{0.0005f, 0.001f};
        panelSpeed = MathUtils.random(panelSpRange[0], panelSpRange[1])* panelDir;
		panelPos.add(new Vector2(MathUtils.random(-blockDim, 0f), MathUtils.random(-blockDim/2, StarCycle.meterHeight/2)));
        float[] panelPadRange = new float[]{3f, 6f};
        for (int i = 1; i < blockNum; i++) {
			panelPos.add(new Vector2(panelPos.get(i-1).x+blockDim+MathUtils.random(panelPadRange[0], panelPadRange[1]),  MathUtils.random(-blockDim/2, StarCycle.meterHeight/2)));
		}
		panelLength = panelPos.get(panelPos.size()-1).x-panelPos.get(0).x+blockDim+MathUtils.random(panelPadRange[0], panelPadRange[1]);
				
		shapePos = getRandomScreenPos(shapeNum);
        float[] shapeSpRange = new float[]{0.001f, 0.005f};
        shapeVel = getRandomVectors(shapeNum, shapeSpRange);
		shapeAngle = getRandomAngles(shapeNum, new float[]{0f, 360f});
        float[] shapeAngVelRange = new float[]{0.2f, 0.8f};
        shapeAngleVel = getRandomAngles(shapeNum, shapeAngVelRange);
		
		// set layer speeds of motes as multiples of 2 in the same direction
        float moteSpeed = 0.004f;
        Vector2 maxVel = getRandomVector(moteSpeed);
		for (int i = 0; i < numLayers; i++) {
			ArrayList<Vector2> posList = getRandomScreenPos(motesPerLayer);
			motePositions.add(posList);
			layerVel[i] = new Vector2((float) (maxVel.x / Math.pow(2f, i)), (float) (maxVel.y / Math.pow(2f, i)));
		}
	}

    public void update() {
		for (int i = 0; i < blockNum; i++) {
			panelPos.get(i).add(panelSpeed, 0f);
			if (panelPos.get(i).x+blockDim<0) {
				panelPos.get(i).add(panelLength, 0f);
			}
			if (panelPos.get(i).x+blockDim>panelLength) {
				panelPos.get(i).add(-panelLength, 0f);
			}
		}
		
		for (int i = 0; i < shapeNum; i++) {
			shapePos.get(i).add(shapeVel.get(i));
            float ang = shapeAngle.get(i) + shapeAngleVel.get(i);
			shapeAngle.set(i, ang);
		}
		wrapPositions(shapePos, shapeDim, shapeDim);
		
		for (int i=0; i < numLayers; i++) {
			for (int j=0; j < motesPerLayer; j++) {
				motePositions.get(i).get(j).add(layerVel[i]);
			}
			wrapPositions(motePositions.get(i), moteDim, moteDim);
		}
	}
	
	public void draw(SpriteBatch batch) {
		if (!noDraw) {
			for (int i = 0;  i< blockNum; i++) {
				drawTiled(batch, Texturez.bgBlocks.get(panelBlocks.get(i)), panelPos.get(i).x, panelPos.get(i).y, Texturez.bgBlockDims[0], Texturez.bgBlockDims[1], tileDim, 0f);
			}
			
			for (int i = 0; i < shapeNum; i++) {
				pos = shapePos.get(i);
				batch.draw(Texturez.bgPent, pos.x, pos.y, 
						shapeDim/2f, shapeDim/2f, shapeDim, shapeDim, 1f, 1f, shapeAngle.get(i));
			}
			
			for (int i=0; i < numLayers; i++) {
				for (int j=0; j < motesPerLayer; j++) {
					pos = motePositions.get(i).get(j);
					batch.draw(Texturez.bgMote, pos.x, pos.y, 
							moteDim/2f, moteDim/2f, moteDim, moteDim, 1f, 1f, 0f);
				}
			}
		}
	}
	
	void drawTiled(SpriteBatch batch, TextureRegion[] texture, float x, float y, int r, int c, float s, float angle) {
		for (int i=0; i < texture.length; i++) {
			float xt = x + s*(i%c);
			float yt = y + s*(r - 1 - i/c);
			if (xt < StarCycle.meterWidth & yt < StarCycle.meterHeight & 0 < xt + s & 0 < yt + s) {
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
