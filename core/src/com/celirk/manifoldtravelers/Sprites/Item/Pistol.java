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
        System.out.println("lq 0000");
        // 这一句不行
        gunPack = new TextureAtlas("weapon/weapon.pack");
        System.out.println("lq 1111");
        gunPic = new TextureRegion(gunPack.findRegion("Enfield"),
                0,0,32,19);
        System.out.println("lq 2222");
        setBounds(x-getWidth()*100,y-getHeight()*100,16,9.5F);
        setRegion(gunPic);
    }

    @Override
    public void update(float dt) {
        setRegion(gunPic);
        super.update(dt);
    }

}
