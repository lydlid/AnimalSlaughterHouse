package com.celirk.manifoldtravelers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.celirk.manifoldtravelers.ManifoldTravelers;

public class DesktopLauncher {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.resizable = true;// can't change window size manually
        config.vSyncEnabled = false;
        config.foregroundFPS = 60;
        new LwjglApplication(new ManifoldTravelers(args), config);
    }
}
