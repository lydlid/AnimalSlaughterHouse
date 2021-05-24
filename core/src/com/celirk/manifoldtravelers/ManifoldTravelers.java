package com.celirk.manifoldtravelers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class ManifoldTravelers extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 225;
	// pixels per meter
	public static final float PPM = 100;
	// collision filters
	public static final short CATEGORY_PORTALABLE = 0x01;  // 0000000000000001
	public static final short CATEGORY_PORTAL = 0x02; // 0000000000000010
	public static final short CATEGORY_PLAYER = 0x04; // 0000000000000100
	public static final short CATEGORY_GROUND = 0x08; // 0000000000001000

	public static final short MASK_PORTALABLE = CATEGORY_PORTAL;
	public static final short MASK_PORTAL = CATEGORY_PORTALABLE | CATEGORY_PLAYER | CATEGORY_GROUND;
	public static final short MASK_PLAYER = CATEGORY_PORTAL | CATEGORY_GROUND;
	public static final short MASK_GROUND = CATEGORY_PORTAL | CATEGORY_PLAYER;

	public SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
//	@Override
//	public void dispose () {
//		batch.dispose();
//	}
}
