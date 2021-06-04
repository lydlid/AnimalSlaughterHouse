package com.celirk.manifoldtravelers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.celirk.manifoldtravelers.ManifoldTravelers;

public class DesktopLauncher {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.resizable = false;// can't change window size mannully
        new LwjglApplication(new ManifoldTravelers(), config);
    }
}
