package com.celirk.manifoldtravelers.Sprites.Tile;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class Ground extends InteractiveTileObject {
    public Ground(PlayScreen screen, MapObject object) {
        super(screen, object);

        fdef.filter.maskBits = ManifoldTravelers.MASK_GROUND;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_GROUND;

        body.createFixture(fdef);
    }
}
