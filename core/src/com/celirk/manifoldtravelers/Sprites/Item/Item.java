package com.celirk.manifoldtravelers.Sprites.Item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected FixtureDef fdef;

    protected int id;

    protected boolean toDestroy;
    protected boolean destroyed;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();

        setPosition(x, y);
        setBounds(getX(), getY(), 5/ManifoldTravelers.PPM, 5/ManifoldTravelers.PPM);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / ManifoldTravelers.PPM, y / ManifoldTravelers.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearDamping = 10;
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ManifoldTravelers.PPM);
        fdef.filter.maskBits = ManifoldTravelers.MASK_ITEM;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_ITEM;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        toDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            screen.removeItem(this);
        }
        setPosition((body.getPosition().x-screen.getPlayer().body.getPosition().x)*ManifoldTravelers.PPM + getWidth()/2 + ManifoldTravelers.V_WIDTH/2 - screen.getPlayer().getWidth()/2,
                (body.getPosition().y-screen.getPlayer().body.getPosition().y)*ManifoldTravelers.PPM + getHeight()/2 + ManifoldTravelers.V_HEIGHT/2 - screen.getPlayer().getHeight()/2);
    }

    public int getID() {
        return id;
    }

    public void destroy(){
        toDestroy = true;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x", body.getPosition().x * ManifoldTravelers.PPM);
            jsonObject.put("y", body.getPosition().y * ManifoldTravelers.PPM);
            jsonObject.put("velocity_x", body.getLinearVelocity().x);
            jsonObject.put("velocity_y", body.getLinearVelocity().y);
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }

    public void setPos(float x, float y) {
        body.setTransform(x / ManifoldTravelers.PPM,y / ManifoldTravelers.PPM,0);
    }

    public void setId(int id) {
        this.id = id;
    }
}
