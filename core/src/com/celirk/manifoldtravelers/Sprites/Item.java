package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected FixtureDef fdef;

    public int id;


    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();

        setPosition(x, y);
        setBounds(getX(), getY(), 8/ManifoldTravelers.PPM, 8/ManifoldTravelers.PPM);

        fdef.filter.maskBits = ManifoldTravelers.MASK_DROP;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_DROP;
    }

    public abstract void update(float dt);

    public int getID() {
        return id;
    }
}
