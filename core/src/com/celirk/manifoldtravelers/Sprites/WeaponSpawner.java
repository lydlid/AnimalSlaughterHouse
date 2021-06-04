package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class WeaponSpawner extends Spawner {
    public WeaponSpawner(PlayScreen screen, MapObject object) {
        super(screen, object);
        time_segment = 1;
    }

    @Override
    protected void Spawn() {
        screen.appendItem(new Pistol(screen, body.getPosition().x, body.getPosition().y));
    }

}
