package com.celirk.manifoldtravelers.Sprites.Tile.Spawner;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Item.Pistol;

public class WeaponSpawner extends Spawner {
    public WeaponSpawner(PlayScreen screen, MapObject object) {
        super(screen, object);
        time_segment = 1;
    }

    @Override
    protected void Spawn() {
        screen.appendItem(new Pistol(screen, body.getPosition().x * ManifoldTravelers.PPM, body.getPosition().y * ManifoldTravelers.PPM));
    }

}
