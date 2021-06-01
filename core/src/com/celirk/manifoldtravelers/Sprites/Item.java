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

    protected boolean toDestroy;
    protected boolean destroyed;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();

        setPosition(x, y);
        setBounds(getX(), getY(), 5/ManifoldTravelers.PPM, 5/ManifoldTravelers.PPM);

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearDamping = 10;
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ManifoldTravelers.PPM);
        fdef.filter.maskBits = ManifoldTravelers.MASK_DROP;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_DROP;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

    }

    public void update(float dt) {

    }

    public int getID() {
        return id;
    }
}
