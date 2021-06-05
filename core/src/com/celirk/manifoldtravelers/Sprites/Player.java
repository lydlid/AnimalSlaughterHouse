package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class Player extends Sprite {
    public PlayScreen screen;
    public World world;
    public Body b2body;

    private int weapon_on_hand;

    private float hit_point;

    public Player(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        definePlayer();
        weapon_on_hand = 0;
        hit_point = 100;
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / ManifoldTravelers.PPM, 64 / ManifoldTravelers.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.linearDamping = 10;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / ManifoldTravelers.PPM);

        fdef.shape = shape;
        fdef.filter.maskBits = ManifoldTravelers.MASK_PLAYER;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_PLAYER;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void acquireItem(int id) {
        if(id == 0){
            weapon_on_hand = 0;
            this.b2body.applyLinearImpulse(new Vector2(0.1f,0),this.b2body.getWorldCenter(), true);
        }
    }
}
