package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class Portalable extends InteractiveTileObject {
    public Portalable(PlayScreen screen, MapObject object) {
        super(screen, object);

        fdef.filter.maskBits = ManifoldTravelers.MASK_PORTALABLE;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_PORTALABLE;

        body.createFixture(fdef);
    }
}
