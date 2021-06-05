package com.celirk.manifoldtravelers.Sprites.Indicator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class Indicator extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected FixtureDef fdef;

    public Indicator(float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();

        setPosition(x, y);
        setBounds(getX(), getY(), 5/ ManifoldTravelers.PPM, 5/ManifoldTravelers.PPM);

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearDamping = 10;
        body = world.createBody(bdef);

        fdef = new FixtureDef();

        fdef.filter.maskBits = 0;
        fdef.filter.categoryBits = 0;

        body.createFixture(fdef);
    }

    public static Texture getTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();


        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
