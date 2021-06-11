package com.celirk.manifoldtravelers.Utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Tile.Ground;
import com.celirk.manifoldtravelers.Sprites.Tile.Portalable;
import com.celirk.manifoldtravelers.Sprites.Tile.Spawner.Spawner;
import com.celirk.manifoldtravelers.Sprites.Tile.Spawner.WeaponSpawner;

public class B2WorldCreator {
    private final Array<Spawner> spawners;

    public B2WorldCreator(PlayScreen screen) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        TiledMap map = screen.getMap();

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

            new Ground(screen, object);
        }

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Portalable(screen, object);
        }

        spawners = new Array<Spawner>();

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            spawners.add(new WeaponSpawner(screen, object));
        }
    }

    public Array<Spawner> getSpawners() {
        return spawners;
    }
}
