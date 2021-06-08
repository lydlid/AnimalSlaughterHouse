package com.celirk.manifoldtravelers.Sprites.Projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Projectile extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected FixtureDef fdef;

    protected boolean toDestroy;
    protected boolean destroyed;

    protected float attack;

    protected int id;

    public Projectile(PlayScreen screen, float x, float y, Vector2 velocity) {
        this.screen = screen;
        this.world = screen.getWorld();

        setPosition(x, y);
        setBounds(getX(), getY(), 1/ ManifoldTravelers.PPM, 1/ManifoldTravelers.PPM);

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        body.setBullet(true);
        body.applyLinearImpulse(velocity, body.getWorldCenter(), true);


        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / ManifoldTravelers.PPM);
        fdef.filter.maskBits = ManifoldTravelers.MASK_PROJECTILE;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_PROJECTILE;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        toDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            screen.removeProjectile(this);
        }
    }

    public void destroy(){
        toDestroy = true;
    }

    public float getAttack() {
        return attack;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x", getX()*ManifoldTravelers.PPM);

            jsonObject.put("y", getY()*ManifoldTravelers.PPM);
            jsonObject.put("velocity_x", body.getLinearVelocity().x);
            jsonObject.put("velocity_y", body.getLinearVelocity().y);
            jsonObject.put("attack", attack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setPos(float x, float y) {
        body.setTransform(x / ManifoldTravelers.PPM,y / ManifoldTravelers.PPM,0);
    }

    public void setVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }
}
