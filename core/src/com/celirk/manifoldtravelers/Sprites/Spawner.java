package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public abstract class Spawner extends InteractiveTileObject {
    public Spawner(PlayScreen screen, MapObject object) {
        super(screen, object);
        fdef.filter.maskBits = ManifoldTravelers.MASK_SPAWNER;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_SPAWNER;
        body.createFixture(fdef);
    }
    public abstract void update(float dt);
}
