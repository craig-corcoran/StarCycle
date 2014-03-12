package com.autonomousgames.starcycle.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;

public class SpriteLayer extends Layer{

	public Sprite image;
	float rotationSpeed;
	float revolutionSpeed;
    public boolean revolving;
	Vector2 relPos = new Vector2();

	public SpriteLayer(TextureRegion texture, Vector2 relPos, Vector2 dims) {
		super(relPos, dims);
		image = new Sprite(texture);
		image.setBounds(getX(), getY(), size.x, size.y);
	}
	
	public SpriteLayer(TextureRegion texture, Vector2 dims) {
		this(texture, new Vector2(0f, 0f), dims);
	}
	
	public SpriteLayer(TextureRegion texture, Vector2 relPos, Vector2 dims, Color color, float angle) {
		this(texture, relPos, dims);
		setLayerColor(color);
		setRotation(angle);
	}

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        image.setSize(width, height);
    }
	
	// The sprite will rotate around it's own center:
	public SpriteLayer setRotationSpeed(float speed) {
		if (speed != 0f) {
			rotationSpeed = speed / 60f; // In degrees/second, assuming 60 FPS.
			Action rotateAction = new Action(){
				public boolean act (float delta) {
					rotate(rotationSpeed);
					return false;
				}
			};
			this.addAction(rotateAction);
		}
		return this;
	}
	
	// The sprite will revolve around the button center:
	public SpriteLayer setRevolutionSpeed(float speed) {
		if (speed != 0f) {
			revolutionSpeed = speed / 60f; // In degrees/second, assuming 60 FPS.
            revolving = true;
			Action revolveAction = new Action(){
				public boolean act (float delta) {
                    if (revolving) {
					    setCenter(center.rotate(revolutionSpeed));
                    }
					return false;
				}
			};
			this.addAction(revolveAction);
		}
		return this;
	}

    // The sprite will move relative to the button center, then go back to it's original position:
    public SpriteLayer slideAndReturn(Vector2 newPos, float time) {
        final Vector2 startPos = getCenter();
        final int steps = MathUtils.round(time*60f);
        float stepSize = newPos.dst(startPos)/(steps); // Time in seconds.
        final Vector2 stepVec = new Vector2(newPos).sub(startPos).nor().scl(stepSize);

        Action slideAction = new Action(){
            int step = 0;

            public boolean act(float delta) {
                if (step < steps) {
                    setCenter(getCenter().add(stepVec));
                    step++;
                }
                else {
                    step = 0;
                    setCenter(startPos);
                }
                return false;
            }
        };
        this.addAction(slideAction);
        return this;
    }

    public SpriteLayer blink(float on, float off) {
        final int onPeriod = MathUtils.round(on * 60f);
        final int offPeriod = MathUtils.round(off * 60f);

        Action blink = new Action(){
            int step = 0;
            int period = onPeriod + offPeriod;

            public boolean act(float delta) {
                if (step == onPeriod) {
                    specialDraw = false;
                }
                else if (step == 0) {
                    specialDraw = true;
                }
                step = (step + 1)%(period);
                return false;
            }
        };
        this.addAction(blink);
        return this;
    }

    public SpriteLayer blinkOn(float off, float on) {
        final int offPeriod = MathUtils.round(off * 60f);
        final int onPeriod = MathUtils.round(on * 60f);

        Action blink = new Action(){
            int step = 0;
            int period = offPeriod + onPeriod;

            public boolean act(float delta) {
                if (step == offPeriod) {
                    specialDraw = true;
                }
                else if (step == 0) {
                    specialDraw = false;
                }
                step = (step + 1)%(period);
                return false;
            }
        };
        this.addAction(blink);
        return this;
    }

    public SpriteLayer blink(float eachPeriod) {
        return blink(eachPeriod, eachPeriod);
    }
	
	@Override
	public void setLayerColor(Color tint) {
		image.setColor(tint);
	}
	
	@Override
	public Color getColor() {
		return image.getColor();
	}
	
	public SpriteLayer setSpriteColor(Color tint) {
		setLayerColor(tint);
		return this;
	}
	
	public SpriteLayer rotateSprite(float angle) {
		rotate(angle);
		return this;
	}

    public SpriteLayer flipSprite(boolean x, boolean y) {
        flip(x, y);
        return this;
    }
	
	@Override
	public void rotate(float angle) {
		super.rotate(angle);
		image.setOrigin(getOriginX(), getOriginY());
		image.rotate(angle);
	}

	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);
		image.setOrigin(getOriginX(), getOriginY());
		image.setRotation(angle);
	}
	
	public SpriteLayer setSpriteAlpha(float a) {
		Color color = image.getColor();
		image.setColor(color.r, color.g, color.b, a);
		return this;
	}
	
	// Each LayeredButton draws its own layers
	public void draw(SpriteBatch batch, float parentAlpha, Vector2 groupCenter, float groupAngle) {
		relPos.set(getCenter()); // Keep the original
		setCenter(getCenter().rotate(groupAngle)); // Rotate the center to match the group rotation
		image.setPosition(getX() + groupCenter.x, getY() + groupCenter.y); // Move the image to match the new center
		image.setOrigin(getOriginX(), getOriginY()); // Move the image origin as well
		image.setRotation(getRotation()+groupAngle); // Rotate the image with the layer and group angles
		image.draw(batch, parentAlpha); // Draw the sprite
		setCenter(relPos); // Reset the relative position
		/* I modify and revert the relative position because I seemed to be accumulating rounding errors.
		Eventually, the rotation of the relative position and the rotation of the sprite angle would desynchronize. */
	}
	
	public SpriteLayer setSpriteOrigin(Vector2 vector) {
		setOrigin(vector.x, vector.y);
		image.setOrigin(vector.x, vector.y);
		return this;
	}

	@Override
	public void setScaleX(float scaleX) {
		super.setScaleX(scaleX);
		image.setScale(scaleX, image.getScaleY());
	}
	
	@Override
	public void setScaleY(float scaleY) {
		super.setScaleY(scaleY);
		image.setScale(image.getScaleX(), scaleY);
	}
	
	public void setScale(Vector2 scale) {
		setScale(scale.x, scale.y);
	}

    public void setCenterAngle(float angle) {
        setCenter(getCenter().setAngle(angle));
    }
	
	@Override // Is this needed? Will the built-in clone do the same thing?
	public SpriteLayer clone() throws CloneNotSupportedException {
		SpriteLayer clone = new SpriteLayer(image, center, size, getColor(), 0f);
		clone.setOrigin(getOriginX(), getOriginY());
		clone.rotate(getRotation());
		Array<Action> actions = getActions();
		for (int i = 0; i < actions.size; i ++) {
			clone.addAction(actions.get(i));
		}
		Array<EventListener> eListeners = getListeners();
		for (int i = 0; i < eListeners.size; i ++) {
			clone.addListener(eListeners.get(i));
		}
		Array<EventListener> cListeners = getCaptureListeners();
		for (int i = 0; i < cListeners.size; i ++) {
			clone.addCaptureListener(cListeners.get(i));
		}
		return clone;
	}
	
	@Override
	public void flip(boolean x, boolean y) {
		image.flip(x, y);
	}
	
}
