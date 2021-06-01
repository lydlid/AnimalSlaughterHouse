package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class WeaponSpawner extends Spawner {
    private float time;
    public WeaponSpawner(PlayScreen screen, MapObject object) {
        super(screen, object);
        time = -1;
    }

    public void update(float dt) {
        time += dt;
        if(time>=0){
            if(Spawn() == true){
                time = -1;

            }
        }
    }

    private boolean Spawn() {
        // TODO
        // if collides with an item, return false
        // else, spawn a new item, and return true
        screen.appendItem(new Pistol(screen, body.getPosition().x, body.getPosition().y));
        return true;
    }

}
