package com.celirk.manifoldtravelers.Sprites.Projectile;


import com.badlogic.gdx.math.Vector2;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class PistolBullet extends Projectile {
    public PistolBullet(PlayScreen screen, float x, float y, Vector2 velocity){
        super(screen, x, y, velocity);
        attack = 10;
    }
}
