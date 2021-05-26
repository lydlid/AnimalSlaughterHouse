package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class Pistol extends Item {
    public Pistol(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ManifoldTravelers.PPM);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void update(float ft) {

    }
}
