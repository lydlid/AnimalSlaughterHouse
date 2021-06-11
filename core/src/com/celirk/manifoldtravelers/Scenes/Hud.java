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
import com.celirk.manifoldtravelers.Screens.PlayScreen;

import static java.lang.Math.round;

public class Hud implements Disposable {
    private final PlayScreen screen;
    public Stage stage;
    private final Viewport viewport;
    private final Integer worldTimer;
    Label FPSLabel;
    Label hostLabel;
    Label HitPointLabel;

    public Hud(SpriteBatch sb, PlayScreen screen) {
        this.screen = screen;
        worldTimer = 300;

        viewport = new FitViewport(ManifoldTravelers.V_WIDTH, ManifoldTravelers.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // just an example

        // maybe we dont need too many labels here.
        // if needed just add some labels and u know.
        // this is more of an example here? maybe
        // table.row();
        // to start a new row
        FPSLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.CHARTREUSE));
        HitPointLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.MAROON));
        hostLabel = new Label("false", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(FPSLabel).expandX().padTop(10);
        table.add(HitPointLabel).expandX().padTop(10);
        table.add(hostLabel).expandX().padTop(10);
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void update(float dt){
        FPSLabel.setText("FPS: " + round(1 / dt));

        hostLabel.setText("Host:" + screen.getSocket().isHost());

        HitPointLabel.setText("HP: " + screen.getPlayer().getHit_point());

    }
}