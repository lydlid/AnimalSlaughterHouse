package com.celirk.manifoldtravelers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.celirk.manifoldtravelers.Screens.PlayScreen;

public class ManifoldTravelers extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 225;
	// pixels per meter
	public static final float PPM = 100;

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
