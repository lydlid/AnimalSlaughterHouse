package com.celirk.manifoldtravelers.Sprites.Item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Item.Item;

public class Pistol extends Item {

    private TextureRegion gunPic;
    private TextureAtlas gunPack;

    public Pistol(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        id = 1;
        this.gunPack = screen.getGunPack();
        gunPic = new TextureRegion(gunPack.findRegion("Enfield"),
                0,0,32,19);
        setBounds(x-getWidth()*100,y-getHeight()*100,16,9.5F);
        setRegion(gunPic);
    }

    @Override
    public void update(float dt) {
        //setPosition();
        setRegion(gunPic);
        super.update(dt);
    }

}
