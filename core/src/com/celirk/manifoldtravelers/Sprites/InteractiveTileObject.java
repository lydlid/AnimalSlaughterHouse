package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;

public class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected FixtureDef fdef;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.fdef = new FixtureDef();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / ManifoldTravelers.PPM, (bounds.getY() + bounds.getHeight() / 2) / ManifoldTravelers.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / ManifoldTravelers.PPM, bounds.getHeight() / 2 / ManifoldTravelers.PPM);
        fdef.shape = shape;
    }
}
