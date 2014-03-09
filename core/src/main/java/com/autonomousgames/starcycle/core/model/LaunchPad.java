package com.autonomousgames.starcycle.core.model;

import com.autonomousgames.starcycle.core.log.ModelSettings;
import com.autonomousgames.starcycle.core.StarCycle;
import com.autonomousgames.starcycle.core.Texturez;
import com.autonomousgames.starcycle.core.screens.ModelScreen;
import com.autonomousgames.starcycle.core.ui.ArcButton;
import com.autonomousgames.starcycle.core.ui.LayerType;
import com.autonomousgames.starcycle.core.ui.LayeredButton;
import com.autonomousgames.starcycle.core.ui.SpriteLayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LaunchPad {


    static final float coolDown = ModelSettings.getFloatSetting("coolDown");
    static final int voidStars = (int) ModelSettings.getFloatSetting("voidStars");
    static final int novaStars = (int) ModelSettings.getFloatSetting("novaStars");
    static final int orbCost = (int) ModelSettings.getFloatSetting("chargeOrbCost");
    static final int voidCost = (int) ModelSettings.getFloatSetting("voidCost");
    static final int novaCost = (int) ModelSettings.getFloatSetting("novaCost");

    // TODO move intermediate variables (not used later) into local vars in a static block
    static final float radi = StarCycle.screenHeight / 3f;
    static final float[] topAngs = new float[]{11.851f, 26.358f, 38.692f, 48.269f, 55.560f, 61.137f, 65.457f, 68.857f};
    static final float[] botAngs = new float[]{34.896f, 57.584f, 68.857f};
    static final Vector2 triDim = new Vector2(20f, 32f).scl(radi/512f);
    static final Vector2 triPos = new Vector2(0f, 502f/512f).scl(radi);
    static final Vector2 metDim = new Vector2(748f, 192f).scl(radi/512f);
    static final Vector2 metPos = new Vector2(528f - 96f, 0f).scl(radi / 512f).rotate(45f);
    static final Vector2 orbTch = new Vector2(0f,radi/2f*0.95f);
    static final Vector2 orbAng = new Vector2(0f, 90f);
    static final Vector2 orbDim = new Vector2(1f,1f).scl(radi * 29f / 64f);
    static final Vector2 orbPos = new Vector2(1f,1f).scl(radi/4f);
    static final Vector2 txtDim = new Vector2(1f, 1f).scl(radi/10f);
    static final Vector2 orbTxtPos = orbPos.cpy().scl(0.75f);
    static final Vector2 powTch = new Vector2(radi/2f, radi*0.92f);
    static final Vector2 pw1Ang = new Vector2(45f, 90f);
    static final Vector2 pw2Ang = new Vector2(0f, 45f);
    static final Vector2 powDim = new Vector2(320f, 288f).scl(radi/512f);
    static final Vector2 pw1Pos = new Vector2(168f, 328f).scl(radi/512f);
    static final Vector2 pw2Pos = pw1Pos.cpy().rotate(90f - 2f*pw1Pos.angle());
    static final Vector2 pw2TxtDim = txtDim.cpy().scl(2f);
    static final Vector2 pw1TxtPos = new Vector2(radi*0.7f,0f).rotate(67.5f);
    static final Vector2 pw2TxtPos = pw1TxtPos.cpy().rotate(-45f);
    static final float trs = 60f; // Texture rotation spe    

    final Player player;
    final LayeredButton bgButton;
    final SpriteLayer meter;
    final ArcButton orbButton;
    final ArcButton pw1Button;
    final ArcButton pw2Button;
    final Vector2 position;
    final boolean visible;
    final AtlasRegion[] orbTxt;
    final AtlasRegion[] pw1Txt;
    final AtlasRegion[] pw2Txt;

    public boolean manualLvl; // TODO remove?

    // public boolean streamOrbs = false;
    float sinceLastShot = 0f;
    float angle;
    float meterAngle;
    ArrayList<LayeredButton> buttons = new ArrayList<LayeredButton>();

    public LaunchPad(Stage ui, Player plyr, boolean visible, boolean manualLvl){

        this.player = plyr;
        this.visible = visible;
        this.manualLvl = manualLvl;
        orbTxt = new AtlasRegion[]{StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.ORB0), StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.ORB1)};
        pw1Txt = new AtlasRegion[]{StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.VOID0), StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.VOID1)};
        pw2Txt = new AtlasRegion[]{StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA0), StarCycle.tex.skinMap.get(player.basetype).get(Texturez.TextureType.NOVA1)};

        position = (player.number ==0 ) ? new Vector2(StarCycle.screenWidth, StarCycle.screenHeight) : new Vector2(0f, 0f);
        angle =  (player.number == 0) ? 180f : 0f;

        bgButton = new LayeredButton(position);
        bgButton.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[1], metPos, metDim, Color.BLACK, -45f), LayerType.ACTIVE);
        bgButton.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[1], metPos, metDim, player.colors[0], -45f), LayerType.ACTIVE);
        for (int i = 0; i < 8; i ++) {
            bgButton.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[5], triPos.cpy().rotate(-topAngs[i]), triDim, player.colors[1], -topAngs[i]), LayerType.ACTIVE);
        }
        for (int i = 0; i < 3; i ++) {
            bgButton.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[5], triPos.cpy().rotate(-botAngs[i]), triDim, player.colors[1], -botAngs[i] + 180f), LayerType.ACTIVE);
        }
        bgButton.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[2], metPos, metDim, player.colors[1], -45f), LayerType.ACTIVE);
        bgButton.rotate(angle);
        meter = ((SpriteLayer) bgButton.getLayer(1));

        orbButton = getOrbButton(position, angle, player.colors, !(player instanceof Bot));
        if (!(player instanceof Bot)) {
            orbButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    player.state.buttonStates[0]  = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    player.state.buttonStates[0] = false;
                }
            });
        }

        pw1Button = getPw1Button(position, angle, player.colors, !(player instanceof Bot));
        if (!(player instanceof Bot)) {
            pw1Button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float  y) {
                    player.state.buttonStates[1] = true;
                }
            });
        }

        pw2Button = getPw2Button(position, angle, player.colors, !(player instanceof Bot));
        if (!(player instanceof Bot)) {
            pw2Button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    player.state.buttonStates[2] = true;
                }
            });
        }

        buttons.add(bgButton);
        buttons.add(orbButton);
        buttons.add(pw1Button);
        buttons.add(pw2Button);

        if (visible) {
            ui.addActor(bgButton);
            ui.addActor(orbButton);
            ui.addActor(pw1Button);
            ui.addActor(pw2Button);
            updateMeter();
        }

    }

    public void update(float delta) {
        sinceLastShot = Math.min(sinceLastShot + delta, coolDown);

        if (!manualLvl) {
            if (player.state.starsControlled >= voidStars && !(pw1Button.isActive())) {
                pw1Button.activate();
            }
            else if (player.state.starsControlled < voidStars && pw1Button.isActive()) {
                pw1Button.deactivate();
            }

            if (player.state.starsControlled >= novaStars && !(pw2Button.isActive())) {
                pw2Button.activate();
            }
            else if (player.state.starsControlled < novaStars && pw2Button.isActive()) {
                pw2Button.deactivate();
            }
        }

        if (player.state.ammo >= orbCost && orbButton.isLocked()) {
            orbButton.unlock();
        }
        else if (player.state.ammo < orbCost && !(orbButton.isLocked())) {
            orbButton.lock();
        }

        if (player.state.ammo >= voidCost && pw1Button.isLocked()) {
            pw1Button.unlock();
        }
        else if (player.state.ammo < voidCost && !(pw1Button.isLocked())) {
            pw1Button.lock();
        }

        if (player.state.ammo >= novaCost && pw2Button.isLocked()) {
            pw2Button.unlock();
        }
        else if (player.state.ammo < novaCost && !(pw2Button.isLocked())) {
            pw2Button.lock();
        }

        updateMeter();
    }

    public void updateMeter() {
        meterAngle = (float)(45f+89.28f/(Math.pow(player.state.ammo,1.5f)/1153.7f + 1f));
        meter.setCenterAngle(meterAngle);
        meter.setRotation(meterAngle-90f);
    }

    public Vector2 getPos() {
        return position.cpy();
    }

    public void setPos(float x, float y) {
        position.set(x,y);
        for (int i = 0; i < buttons.size(); i ++) {
            buttons.get(i).setCenter(position);
        }
    }

    public void setPos(Vector2 pos) {
        setPos(pos.x, pos.y);
    }

    public void movePos(Vector2 pos) {
        movePos(pos.x, pos.y);
    }

    public void movePos(float x, float y) {
        setPos(position.x + x, position.y + y);
    }

    public void showMeter(boolean visible) {
        if (visible) {
            bgButton.activate();
        }
        else {
            bgButton.deactivate();
        }
    }

    public ArcButton getOrbButton(Vector2 position, float angle, Color[] colors, boolean touchable) {
        ArcButton button = new ArcButton(position, orbAng, orbTch);
        if (touchable) {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[3], orbPos, orbDim, colors[1], 0f), LayerType.UP);
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[3], orbPos, orbDim, colors[0], 0f), LayerType.DOWN);
        }
        else {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[3], orbPos, orbDim, colors[1], 0f));
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[3], orbPos, orbDim, colors[0], 0f), LayerType.SPECIAL);
            button.getLayer(1).toggleSpecial();
        }
        button.addLayer(new SpriteLayer(orbTxt[0], orbTxtPos, txtDim, colors[0], 0f).setRotationSpeed(trs), LayerType.UNLOCKED);
        button.addLayer(new SpriteLayer(orbTxt[1], orbTxtPos, txtDim, colors[1], 0f).setRotationSpeed(trs), LayerType.UNLOCKED);
        button.rotate(angle);
        return button;
    }

    public ArcButton getPw1Button(Vector2 position, float angle, Color[] colors, boolean touchable) {
        ArcButton button = new ArcButton(position, pw1Ang, powTch);
        if (touchable) {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw1Pos, powDim, colors[1], 0f), LayerType.ACTIVEUP);
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw1Pos, powDim, colors[0], 0f), LayerType.DOWN);
        }
        else {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw1Pos, powDim, colors[1], 0f), LayerType.ACTIVE);
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw1Pos, powDim, colors[0], 0f), LayerType.SPECIAL);
            button.getLayer(1).toggleSpecial();
        }
        button.addLayer(new SpriteLayer(StarCycle.tex.voidRing, pw1TxtPos, txtDim.cpy().scl(2.5f), colors[0], 0f), LayerType.FREE);
        button.addLayer(new SpriteLayer(pw1Txt[0], pw1TxtPos, txtDim, colors[0], 0f).setRotationSpeed(trs), LayerType.FREE);
        button.addLayer(new SpriteLayer(pw1Txt[1], pw1TxtPos, txtDim, colors[1], 0f).setRotationSpeed(trs), LayerType.FREE);
        button.rotate(angle);
        button.deactivate();
        return button;
    }

    public ArcButton getPw2Button(Vector2 position, float angle, Color[] colors, boolean touchable) {
        ArcButton button = new ArcButton(position, pw2Ang, powTch);
        if (touchable) {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw2Pos, powDim, colors[1], 0f).flipSprite(true, false).rotateSprite(-90f), LayerType.ACTIVEUP);
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw2Pos, powDim, colors[0], 0f).flipSprite(true, false).rotateSprite(-90f), LayerType.DOWN);
        }
        else {
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw2Pos, powDim, colors[1], 0f).flipSprite(true, false).rotateSprite(-90f), LayerType.ACTIVE);
            button.addLayer(new SpriteLayer(StarCycle.tex.launchTextures[4], pw2Pos, powDim, colors[0], 0f).flipSprite(true, false).rotateSprite(-90f), LayerType.SPECIAL);
            button.getLayer(1).toggleSpecial();
        }
        button.addLayer(new SpriteLayer(pw2Txt[0], pw2TxtPos, pw2TxtDim, colors[0], 0f).setRotationSpeed(trs), LayerType.FREE);
        button.addLayer(new SpriteLayer(pw2Txt[1], pw2TxtPos, pw2TxtDim, colors[1], 0f).setRotationSpeed(trs), LayerType.FREE);
        button.rotate(angle);
        button.deactivate();
        return button;
    }

    public void setLvl(int i) {
        switch (i) {
            case 0:
                pw1Button.deactivate();
                pw2Button.deactivate();
                break;
            case 1:
                pw1Button.activate();
                pw2Button.deactivate();
                break;
            case 2:
                pw1Button.activate();
                pw2Button.activate();
                break;
        }

    }
}