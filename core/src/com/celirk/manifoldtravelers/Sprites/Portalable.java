package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.celirk.manifoldtravelers.ManifoldTravelers;

public class Portalable extends InteractiveTileObject {
    public Portalable(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

        fdef.filter.maskBits = ManifoldTravelers.MASK_PORTALABLE;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_PORTALABLE;

        body.createFixture(fdef);
    }
}
