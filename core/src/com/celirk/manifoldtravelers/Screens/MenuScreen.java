package com.celirk.manifoldtravelers.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import org.w3c.dom.Text;


public class MenuScreen implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 125;
    private static final int EXIT_BUTTON_HEIGHT = 50;
    private static final int PLAY_BUTTON_WIDTH = 125;
    private static final int PLAY_BUTTON_HEIGHT = 50;
    private static final int EXIT_BUTTON_Y = 275;
    private static final int PLAY_BUTTON_Y = 275;

    final ManifoldTravelers game;

    Texture background;
    Texture title;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;


    public MenuScreen(final ManifoldTravelers game){
        this.game  =  game;
        background = new Texture("background.png");
        playButtonActive   = new Texture("play_button_active.png");
        playButtonInactive   = new Texture("play_button_inactive.png");
        exitButtonActive   = new Texture("exit_button_active.png");
        exitButtonInactive   = new Texture("exit_button_inactive.png");
        title = new Texture("title_pink.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        //background
        game.batch.draw(background,0,0);

        //title
        game.batch.draw(title,80,450);

        //EXIT button
        int x = 1280 - ManifoldTravelers.V_WIDTH/2 - PLAY_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x){
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }else{
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }
        //PLAY button
        int x1 = 1280 - ManifoldTravelers.V_WIDTH;
        if(Gdx.input.getX() < x1 + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x1){
            game.batch.draw(playButtonActive, x1, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){
                game.setScreen(new PlayScreen(game));
            }
        }else{
            game.batch.draw(playButtonInactive, x1, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
