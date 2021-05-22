package com.celirk.manifoldtravelers.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.celirk.manifoldtravelers.ManifoldTravelers;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
//    Label scoreLabel;
//    Label timeLabel;
//    Label levelLabel;
//    Label worldLabel;
//    Label playerLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(ManifoldTravelers.V_WIDTH,ManifoldTravelers.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.CHARTREUSE));
        // maybe we dont need too many labels here.
        // if needed just add some labels and u know.
        // this is more of an example here? maybe
        table.add(countdownLabel).expandX().padTop(10);
        // table.row();
        // to start a new row

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
