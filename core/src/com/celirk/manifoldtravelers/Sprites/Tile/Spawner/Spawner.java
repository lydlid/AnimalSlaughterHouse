package com.celirk.manifoldtravelers.Sprites.Tile.Spawner;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Tile.InteractiveTileObject;

public abstract class Spawner extends InteractiveTileObject {
    protected boolean isOccupied;

    private float time;
    protected float time_segment;

    public Spawner(PlayScreen screen, MapObject object) {
        super(screen, object);
        fdef.filter.maskBits = ManifoldTravelers.MASK_SPAWNER;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_SPAWNER;

        fdef.isSensor = true;

        body.createFixture(fdef).setUserData(this);

        isOccupied = false;
    }

    public void update(float dt) {
        if(isOccupied) {
            time = -time_segment;
        }
        else {
            time += dt;
            if (time >= 0) {
                Spawn();
                time = -time_segment;
            }
        }
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    protected abstract void Spawn();
}
