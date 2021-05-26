package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected MapObject object;
    protected PlayScreen screen;
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected FixtureDef fdef;

    public InteractiveTileObject(PlayScreen screen, MapObject object) {
        this.screen = screen;
        this.object = object;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
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
