package com.autonomousgames.starcycle.core.ui;

import com.autonomousgames.starcycle.core.Colors;
import com.autonomousgames.starcycle.core.StarCycle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class PopUp extends LayeredButton {

    Stage ui;
    BitmapFont font;
    ArrayList<SpriteLayer> background = new ArrayList<SpriteLayer>();
    Vector2 boxDims;
    Vector2 buttonSize;
    float edge;
    Vector2 iconSize;
    TextLayer text;
    LayeredButton buttonOne;
    LayeredButton buttonTwo;
    boolean twoButtons = false;
    boolean mask = true;
    boolean icon = false;
    int result = 0;

    public PopUp (Stage ui, BitmapFont font, CharSequence message, CharSequence label) {
        super(new Vector2(ui.getWidth() / 2f, ui.getHeight() / 2f), new Vector2(ui.getWidth(), ui.getHeight()));
        this.ui = ui;
        this.font = font;

        boxDims = new Vector2(ui.getWidth()/4f, ui.getHeight()*5f/6f);
        edge = ui.getHeight()/48f;
        iconSize = new Vector2(1f, 1f).scl(boxDims.x/2f);

        background.add(new SpriteLayer(StarCycle.tex.block, touchSize).setSpriteColor(Color.BLACK));
        background.add(new SpriteLayer(StarCycle.tex.block, boxDims).setSpriteColor(Colors.smoke));
        background.add(new SpriteLayer(StarCycle.tex.block, boxDims.cpy().sub(edge*2f, edge*2f)).setSpriteColor(Colors.night));

        text = new TextLayer(font, message).rotateText(90f);

        buttonSize = new Vector2(1f/16f, 1f/8f).scl(ui.getHeight());
        buttonOne = new LayeredButton(boxDims.cpy().sub(buttonSize).scl(0.5f).sub(2f * edge, 2f * edge).add(center), buttonSize.cpy().scl(1.2f));
        buttonOne.addLayer(new SpriteLayer(StarCycle.tex.block, buttonSize).setSpriteColor(Colors.cyan), LayerType.DOWN);
        buttonOne.addLayer(new SpriteLayer(StarCycle.tex.block, buttonSize).setSpriteColor(Colors.navy), LayerType.UP);
        buttonOne.addLayer(new TextLayer(font, label).rotateText(90f));
        buttonOne.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                result = 1;
                clickOne();
                remove();
            }
        });

    }

    public PopUp (Stage ui, BitmapFont font, CharSequence message, CharSequence labelOne, CharSequence labelTwo) {
        this(ui, font, message, labelOne);
        makeButtonTwo(labelTwo);
    }

    public PopUp (Stage ui, BitmapFont font, CharSequence message) {
        this(ui, font, message, "OK");
    }

    public PopUp yesNo() {
        buttonOne.removeLayer(2);
        buttonOne.addLayer(new TextLayer(font, "YES").rotateText(90f));
        buttonOneColors(Colors.spinach, Colors.matcha);
        makeButtonTwo("NO");
        buttonTwoColors(Colors.red, Colors.yellow);
        return this;
    }

    public PopUp buttonOneColors(Color up, Color down) {
        ((SpriteLayer) buttonOne.getLayer(0)).setSpriteColor(down);
        ((SpriteLayer) buttonOne.getLayer(1)).setSpriteColor(up);
        return this;
    }

    public PopUp buttonTwoColors(Color up, Color down) {
        ((SpriteLayer) buttonTwo.getLayer(0)).setSpriteColor(down);
        ((SpriteLayer) buttonTwo.getLayer(1)).setSpriteColor(up);
        return this;
    }

    void makeButtonTwo (CharSequence label) {
        twoButtons = true;
        buttonTwo = new LayeredButton(boxDims.cpy().sub(buttonSize).scl(0.5f).sub(2f*edge, 2f*edge).scl(1f, -1f).add(center), buttonSize.cpy().scl(1.2f));
        buttonTwo.addLayer(new SpriteLayer(StarCycle.tex.block, buttonSize).setSpriteColor(Colors.smoke), LayerType.DOWN);
        buttonTwo.addLayer(new SpriteLayer(StarCycle.tex.block, buttonSize).setSpriteColor(Colors.charcoal), LayerType.UP);
        buttonTwo.addLayer(new TextLayer(font, label).rotateText(90f));
        buttonTwo.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                result = 2;
                clickTwo();
                remove();
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        parentAlpha *= getAlpha();
        for (int i = 0; i < background.size(); i ++) {
            background.get(i).draw(batch, parentAlpha, center, getRotation());
        }
        text.draw(batch, parentAlpha, center, getRotation());
        // buttonOne and buttonTwo will be drawn directly by the ui.
    }

    public PopUp info() {
        if (!icon) {
            background.add(new SpriteLayer(StarCycle.tex.infoIcon, iconSize).setSpriteColor(Color.BLACK).rotateSprite(90f));
            icon = true;
        }
        else {
            Gdx.app.log("PopUp", "Icon already added.");
        }
        return this;
    }

    public PopUp alert() {
        if (!icon) {
            background.add(new SpriteLayer(StarCycle.tex.warningIcon, iconSize).setSpriteColor(Color.BLACK).rotateSprite(90f));
            icon = true;
        }
        else {
            Gdx.app.log("PopUp", "Icon already added.");
        }
        return this;
    }

    public PopUp question() {
        if (!icon) {
            background.add(new SpriteLayer(StarCycle.tex.questionIcon, iconSize).setSpriteColor(Color.BLACK).rotateSprite(90f));
            icon = true;
        }
        else {
            Gdx.app.log("PopUp", "Icon already added.");
        }
        return this;
    }

    public PopUp noMask() {
        if (mask) {
            background.remove(0);
            mask = false;
        }
        else {
            Gdx.app.log("PopUp", "Mask already removed.");
        }
        return this;
    }

    public PopUp maskAlpha(float alpha) {
        if (mask) {
            background.get(0).setSpriteAlpha(alpha);
        }
        else {
            Gdx.app.log("PopUp", "Mask already removed.");
        }
        return this;
    }

    public void go() {
        ui.addActor(this);
        ui.addActor(buttonOne);
        if (twoButtons) {
            ui.addActor(buttonTwo);
        }
    }

    @Override
    public boolean remove() {
        buttonOne.remove();
        if (twoButtons) {
            buttonTwo.remove();
        }
        return super.remove();
    }

    public int result() {
        return result;
    }

    public void clickOne() {}

    public void clickTwo() {}

}
