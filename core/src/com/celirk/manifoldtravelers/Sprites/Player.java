package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Indicator.Indicator;
import com.celirk.manifoldtravelers.Sprites.Projectile.PistolBullet;
import com.celirk.manifoldtravelers.Sprites.Projectile.Projectile;

public class Player extends Sprite {
    public PlayScreen screen;
    public World world;
    public Body b2body;

    private int weapon_on_hand;

    private float hit_point;
    //private Indicator hp_indicator;

    private float attack_time = -1e10F;
    private float attack_time_segment = 1e10F;

    public Player(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        definePlayer();
        defineUtils();
    }

    private void definePlayer() {
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

    private void defineUtils() {
        weapon_on_hand = 0;
        hit_point = 100;
        //hp_indicator = new Indicator((int) getX(), (int) getY());
    }

    public void update(float dt) {
        attack_time += dt;
    }

    public void acquireItem(int id) {
        switch (id){
            case 1:
                attack_time = 1;
                attack_time_segment = 0.5F;
        }
        weapon_on_hand = id;
    }

    public void hit(float delta_hp) {
        hit_point -= delta_hp;

    }

    public void shoot(float x, float y, float dt) {
        if(weapon_on_hand == 0) return;

        if(attack_time > 0) {
            Vector2 direction = new Vector2(x,y);
            direction.nor();
            direction.scl(7 / ManifoldTravelers.PPM);

            Vector2 velocity = new Vector2(x,y);
            velocity.nor();
            velocity.scl(5);

            screen.appendProjectile(new PistolBullet(screen,
                    b2body.getPosition().x + direction.x,
                    b2body.getPosition().y + direction.y,
                    velocity.add(b2body.getLinearVelocity())));
            attack_time = -attack_time_segment;

            b2body.applyLinearImpulse(direction.scl(-2), b2body.getWorldCenter(), true);
        }
    }
}
