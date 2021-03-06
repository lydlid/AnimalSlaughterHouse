package com.celirk.manifoldtravelers.Sprites.Projectile;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class PistolBullet extends Projectile {
    private final TextureRegion bulletPic;
    public PistolBullet(PlayScreen screen, float x, float y, Vector2 velocity){
        super(screen, x, y, velocity);
        bulletPic = new TextureRegion(screen.getAtlas().findRegion("bullet"),0,0,16,16);
        attack = 10;
        id = 1;
        setBounds(x,y,16,16);
        setRegion(bulletPic);
        ManifoldTravelers.manager.get("audio/sounds/pistol_shoot.wav", Sound.class).play(
                1 - screen.getPlayer().body.getPosition().dst(body.getPosition())/100,
                1,
                (body.getPosition().x - screen.getPlayer().body.getPosition().x)/100
        );
    }

    @Override
    public void update(float dt){
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            screen.removeProjectile(this);
        }

        //setPosition((float) (body.getPosition().x+(11.5)*getWidth()/2)*3,(body.getPosition().y + 6*getHeight()/2)*3);
        setPosition((body.getPosition().x-screen.getPlayer().body.getPosition().x)*ManifoldTravelers.PPM + getWidth()/2 + ManifoldTravelers.V_WIDTH/2 - screen.getPlayer().getWidth()/2,
                (body.getPosition().y-screen.getPlayer().body.getPosition().y)*ManifoldTravelers.PPM + getHeight()/2 + ManifoldTravelers.V_HEIGHT/2 - screen.getPlayer().getHeight()/2);


        setRegion(bulletPic);

    }
}
