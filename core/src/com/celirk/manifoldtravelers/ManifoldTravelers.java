package com.celirk.manifoldtravelers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Screens.MenuScreen;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;


public class ManifoldTravelers extends Game {

    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 225;
    // pixels per meter
    public static final float PPM = 100;
    // collision filters
    public static final int CATEGORY_PORTALABLE = 0x0001;  //   0000000000000001
    public static final int CATEGORY_PORTAL = 0x0002; //        0000000000000010
    public static final int CATEGORY_PLAYER = 0x0004; //        0000000000000100
    public static final int CATEGORY_GROUND = 0x0008; //        0000000000001000
    public static final int CATEGORY_SPAWNER = 0x0010; //       0000000000010000
    public static final int CATEGORY_ITEM = 0x0020; //          0000000000100000
    public static final int CATEGORY_PROJECTILE = 0x0040; //    0000000001000000

    public static final int MASK_PORTALABLE = CATEGORY_PORTAL;
    public static final int MASK_PORTAL = CATEGORY_PORTALABLE | CATEGORY_PLAYER | CATEGORY_GROUND;
    public static final int MASK_PLAYER = CATEGORY_PORTAL | CATEGORY_GROUND | CATEGORY_ITEM | CATEGORY_PROJECTILE | CATEGORY_PLAYER;
    public static final int MASK_GROUND = CATEGORY_PORTAL | CATEGORY_PLAYER | CATEGORY_ITEM | CATEGORY_PROJECTILE;
    public static final int MASK_SPAWNER = CATEGORY_ITEM;
    public static final int MASK_ITEM = CATEGORY_PLAYER | CATEGORY_ITEM | CATEGORY_GROUND | CATEGORY_SPAWNER;
    public static final int MASK_PROJECTILE = CATEGORY_GROUND | CATEGORY_PLAYER;

    public static String ip;

    public SpriteBatch batch;


    public static AssetManager manager;

    public ManifoldTravelers (String args[]) {
        super();
        ip = args[0];

    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/bgm.ogg", Music.class);
        manager.load("audio/sounds/pistol_shoot.wav", Sound.class);
        manager.load("audio/sounds/pistol_reload.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/getHit.wav", Sound.class);
        manager.load("audio/sounds/die.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.finishLoading();

        this.setScreen(new MenuScreen(this));
}

    @Override
    public void render() {
        super.render();
    }

	@Override
	public void dispose () {
		batch.dispose();
	}
}
