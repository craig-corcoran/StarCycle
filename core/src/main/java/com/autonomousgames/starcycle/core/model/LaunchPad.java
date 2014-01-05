package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.Texturez.TextureType;
import com.autonomousgames.starcycle.core.UserSettingz;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.autonomousgames.starcycle.core.ui.UIArcButton;
import com.autonomousgames.starcycle.core.ui.UIButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LaunchPad extends Actor {
	
	private final Player player;
	private final ModelScreen screen;
	private static float coolDown;										
	private float sinceLastShot = 0f;
	public boolean streamOrbs = false;
	private boolean drawUI;
	public boolean drawAmmo = true;
	
	public final Vector2 corner;
    private final float arcMin = 1.15f;
	private final float arcMax = 35.7f;
	private final float arcRange = arcMax-arcMin;
	private final int[] divNum = new int[]{7, 2};
	private final int arcNum = 3;
//	private Vector2 arcAngles = new Vector2(arcRange, -arcRange);
	private float[] arcAngles = new float[]{arcRange, arcRange, arcRange};
	
	private final float[] powerupCosts = new float[]{UserSettingz.getFloatSetting("gravWellCost"), UserSettingz.getFloatSetting("nukeCost")};
	private final float ammoDispCap = (divNum[0]+1)*powerupCosts[0];
//	private float[] ammoFraction = new float[]{0f, 0f, 0f};
//	private Vector2 powerupCounts = new Vector2();
	
	private final Vector2 radii = new Vector2(StarCycle.screenHeight/6f, StarCycle.screenHeight/3f);
	private final float textureAngle;
	private final Vector2 orbButtonPos = new Vector2(radii.x/2f, radii.x/2f);
	private final Vector2 orbButtonSize = new Vector2(radii.x, radii.x);
    private final float voidTextureAngle = -(arcRange / 2f + arcMin);
	private final float novaTextureAngle = arcRange / 2f + arcMin - 90f;
	private Vector2 voidButtonPos = new Vector2(0f, 250f/384f * radii.y).rotate(voidTextureAngle);
	private Vector2 novaButtonPos = new Vector2(voidButtonPos.x, voidButtonPos.y).rotate(novaTextureAngle-voidTextureAngle);
	private final float powerupTextureSize = radii.y / 8f;
	//private final Vector2 orbTextureSize = new Vector2(radii.y, radii.y);
	
	private final float minIncome = UserSettingz.getFloatSetting("ammoDripRate");
	private final float maxIncome = (UserSettingz.getFloatSetting("ammoRate")*UserSettingz.getFloatSetting("orbLifeSpan")/UserSettingz.getFloatSetting("coolDown"));


    private final Vector2 blipStartCenter = new Vector2(247f/256f, 247/256f).scl(radii.y).div((float) Math.sqrt(2));
	private final Vector2 blipMeanDir;
	private final float blipPathLen = 129f/256f*radii.y;
	private Vector2 blipDims = new Vector2(10f/256f, 10f/256f).scl(radii.y);
	private float blipWaitTime = 0f;
    private ArrayList<Vector2> blipPos = new ArrayList<Vector2>();
	private ArrayList<Vector2> blipDir = new ArrayList<Vector2>();
	private ArrayList<Vector2> blipStarts = new ArrayList<Vector2>();
	private final int maxBlipNum = 60;
	private int lastBlip = maxBlipNum;
	private boolean[] blipActive = new boolean[maxBlipNum];

    public LaunchPad (ModelScreen screen, Stage ui, Player iplayer, boolean drawLaunchPad) {
		this.screen = screen;
		player = iplayer;
		drawUI = drawLaunchPad;
		coolDown = UserSettingz.getFloatSetting("coolDown");
        float blipSpeed = blipPathLen / 60f;
        blipMeanDir = new Vector2(-1f,-1f).nor().scl(blipSpeed);
		java.util.Arrays.fill(blipActive, false);

		corner = (player.number==0) ? new Vector2(StarCycle.screenWidth, StarCycle.screenHeight) : new Vector2(0f, 0f);
        Vector2 voidAngles = new Vector2(55f, 90f);
        Vector2 nukeAngles = new Vector2(0f, 35f);
        if (player.number==0) {
			textureAngle = 180f;
			nukeAngles.add(180f, 180f);
			voidAngles.add(180f, 180f);
			changeCorner(orbButtonPos);
            Vector2 orbTexturePos = new Vector2(radii.x, radii.x);
            changeCorner(orbTexturePos);
			changeCorner(voidButtonPos);
			changeCorner(novaButtonPos);
			blipStartCenter.rotate(180f);
			blipMeanDir.rotate(180f);
		}
		else {
			textureAngle = 0f;
		}
		
		voidButtonPos=voidButtonPos.sub(powerupTextureSize/2f, powerupTextureSize/2f);
		novaButtonPos=novaButtonPos.sub(powerupTextureSize/2f, powerupTextureSize/2f);
		
		for (int i = 0; i < maxBlipNum; i++) {
			blipPos.add(corner.cpy().add(blipStartCenter.cpy()));
			blipDir.add(blipMeanDir.cpy());
			blipStarts.add(blipPos.get(i));
		}
		newBlip();
		
		if (drawUI) {
			ui.addActor(this);
			if (!(player instanceof Bot)) {
				UIButton orbButton = new UIButton(orbButtonPos, orbButtonSize, 0f);
				if (player.number==0) {
					orbButton.rotate(180f);
				}
				orbButton.addListener(new InputListener(){
					@Override 
				    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						streamOrbs = true;
						return true;
				     }
					@Override
				     public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						streamOrbs = false;
				     }
				});
				
				UIArcButton voidButton = new UIArcButton(corner, voidAngles, radii);
				voidButton.addListener(new ClickListener() {
					public void clicked (InputEvent event, float x, float y) {
						if (player.starsCaptured >= UserSettingz.getFloatSetting("gravWellStars")) {
							launch(Orb.OrbType.VOID);
						}
					}
				});
				
				UIArcButton nukeButton = new UIArcButton(corner, nukeAngles, radii);
				nukeButton.addListener(new ClickListener() {
					public void clicked (InputEvent event, float x, float y) {
						if (player.starsCaptured >= UserSettingz.getFloatSetting("nukeStars")) {
							launch(Orb.OrbType.NOVA);
						}
					}
				});
				ui.addActor(orbButton);
				ui.addActor(voidButton);
				ui.addActor(nukeButton);
			}
		}
				
	}
	
	public void update (float delta) {
		sinceLastShot = Math.min(sinceLastShot + delta, coolDown);
		if (streamOrbs) {
			if (sinceLastShot >= coolDown) {
				launch(Orb.OrbType.ORB);
				sinceLastShot = 0f;
			}
		}
		
		if (drawUI) {
			for (int i=0; i < arcNum; i++) {
				if (player.ammo < ammoDispCap * (i+1)) {
					if (player.ammo < ammoDispCap*i) {
						arcAngles[i] = 45f;
					}
					else {
						arcAngles[i] = arcRange * ( 1 - ((player.ammo - ammoDispCap*i) / ammoDispCap) );
					}
				}
				else {
					arcAngles[i] = 0f;
				}
			}

            float incomeFraction = (player.income - minIncome) / (maxIncome - minIncome);
			if (player.number==0) {
			}
			for (int i=0; i < maxBlipNum; i++) {
				updateBlips(i, delta);
			}
			blipWaitTime += delta;
			
			// Nonlinear scaling of income meter to give more feedback at lower incomes. Thanks, Obama!
//			if (incomeFraction < 0.125f) {
//				blipCutoff = blipMaxWait * (1f - 4f * incomeFraction); 
//			}
//			else if (incomeFraction < 0.5f) {
//				blipCutoff = blipMaxWait * (0.625f - incomeFraction);
//			}
//			else if (incomeFraction < 1f) {
//				blipCutoff = blipMaxWait * (0.25f - incomeFraction / 4f);
//			}
//			else {
//				blipCutoff = 0f;
//			}
            float blipCutoff = (float) Math.exp(-10f * incomeFraction);
			if (blipWaitTime >= blipCutoff) {
				newBlip();
				blipWaitTime = 0f;
			}
		}
	}
	
	private void updateBlips (int i, float delta) {
		if (blipActive[i]) {
			blipPos.get(i).add(blipDir.get(i));
			if (blipPos.get(i).dst(blipStarts.get(i)) >= blipPathLen) {
				blipActive[i] = false;
			}
		}
		
	}
	
	private void newBlip() {
		lastBlip = (lastBlip + 1) % maxBlipNum;
        float rotationRange = 10f;
        float randomAngle = MathUtils.random(rotationRange) - rotationRange / 2f;
		blipPos.set(lastBlip, corner.cpy().add(blipStartCenter.cpy().rotate(randomAngle)));
		blipDir.set(lastBlip, blipMeanDir.cpy().rotate(randomAngle));
		blipStarts.set(lastBlip, blipPos.get(lastBlip).cpy());
		blipActive[lastBlip] = true;
	}
	
	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		if (drawUI) {
			drawMain(batch, Texturez.launchBackground);
			batch.setColor(player.colors[0]);
			if (drawAmmo) {
				for (int i=0; i < arcNum; i++) {
					batch.draw(Texturez.launchArcs[i], corner.x, corner.y, 0f, 0f, radii.y, radii.y, 1f, 1f, textureAngle + arcAngles[i]);
					batch.draw(Texturez.launchArcs[i+3], corner.x, corner.y, 0f, 0f, radii.y, radii.y, 1f, 1f, textureAngle - arcAngles[i]);
				}
				for (int i=0; i < maxBlipNum; i++) {
					if (blipActive[i]) {
						batch.draw(Texturez.launchBlip, blipPos.get(i).x-blipDims.x/2f, blipPos.get(i).y-blipDims.y/2f, blipDims.x/2f, blipDims.y/2f, blipDims.x, blipDims.y, 1f, 1f, 0f);
					}
				}
			}
			batch.setColor(Color.WHITE);
			for (int i=0; i < Texturez.launchTextures.length; i++) {
				drawMain(batch, Texturez.launchTextures[i]);
			}
			if (player.starsCaptured > 0) {
				batch.setColor(player.colors[0]);
				batch.draw(Texturez.skinMap.get(player.basetype).get(TextureType.VOID0), voidButtonPos.x, voidButtonPos.y, powerupTextureSize/2f, powerupTextureSize/2f, powerupTextureSize, powerupTextureSize, 1f, 1f, voidTextureAngle);
				if (player.starsCaptured > 1) {
					batch.draw(Texturez.skinMap.get(player.basetype).get(TextureType.NOVA0), novaButtonPos.x, novaButtonPos.y, powerupTextureSize/2f, powerupTextureSize/2f, powerupTextureSize, powerupTextureSize, 1f, 1f, novaTextureAngle);
				}
			}
			batch.setColor(player.colors[1]);
			if (player.starsCaptured > 0) {
				batch.draw(Texturez.skinMap.get(player.basetype).get(TextureType.VOID1), voidButtonPos.x, voidButtonPos.y, powerupTextureSize/2f, powerupTextureSize/2f, powerupTextureSize, powerupTextureSize, 1f, 1f, voidTextureAngle);
				if (player.starsCaptured > 1) {
					batch.draw(Texturez.skinMap.get(player.basetype).get(TextureType.NOVA1), novaButtonPos.x, novaButtonPos.y, powerupTextureSize/2f, powerupTextureSize/2f, powerupTextureSize, powerupTextureSize, 1f, 1f, novaTextureAngle);
				}
			}
			drawMain(batch, Texturez.launchBezel);
			batch.setColor(player.colors[0]);
			batch.draw(Texturez.launchButton, corner.x, corner.y, 0f, 0f, radii.x, radii.x, 1f, 1f, textureAngle);
			batch.setColor(Color.WHITE);
		}
	}
	
	private void drawMain (SpriteBatch batch, TextureRegion texture) {
		batch.draw(texture, corner.x, corner.y, 0f, 0f, radii.y, radii.y, 1f, 1f, textureAngle);
	}
	
	public void launch (Orb.OrbType orbType) {
		screen.launch(orbType, player);
	}
	
	private void changeCorner (Vector2 vector) {
		vector.scl(-1f).add(StarCycle.screenWidth, StarCycle.screenHeight);
	}
	
	public Vector2 getOrbButtonPos() {
		return orbButtonPos.cpy();
	}
	
	public Vector2 getVoidButtonPos() {
		return voidButtonPos.cpy();
	}
	
	public Vector2 getNovaButtonPos() {
		return novaButtonPos.cpy();
	}
	
	public Vector2 getOrbButtonSize() {
		return orbButtonSize.cpy();
	}
	
	@Override
	public float getWidth() {
		return radii.y; // radii.y is the larger radius, not a vector component
	}
	
	@Override
	public float getHeight() {
		return radii.y; // radii.y is the larger radius, not a vector component
	}
}
