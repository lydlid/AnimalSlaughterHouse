package com.celirk.manifoldtravelers.Sprites.Projectile;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class PistolBullet extends Projectile {
    private TextureRegion bulletPic;
    public PistolBullet(PlayScreen screen, float x, float y, Vector2 velocity){
        super(screen, x, y, velocity);
        bulletPic = new TextureRegion(screen.getAtlas().findRegion("bullet"),0,0,16,16);
        attack = 10;

        setBounds(x,y,16,16);
        setRegion(bulletPic);
    }

    @Override
    public void update(float dt){
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            screen.removeProjectile(this);
        }
        //System.out.println("I'm here!!!!!");
        setPosition((float) (body.getPosition().x+(11.5)*getWidth()/2),body.getPosition().y + 6*getHeight()/2);
        setRegion(bulletPic);
        id = 1;
    }
}
