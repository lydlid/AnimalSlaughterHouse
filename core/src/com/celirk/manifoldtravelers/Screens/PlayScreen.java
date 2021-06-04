package com.celirk.manifoldtravelers.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Scenes.Hud;
import com.celirk.manifoldtravelers.Sprites.Item;
import com.celirk.manifoldtravelers.Sprites.Pistol;
import com.celirk.manifoldtravelers.Sprites.Player;
import com.celirk.manifoldtravelers.Sprites.Spawner;
import com.celirk.manifoldtravelers.Utils.B2WorldCreator;
import com.celirk.manifoldtravelers.Utils.WorldContactListener;

public class PlayScreen implements Screen {
    private ManifoldTravelers game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;

    private B2WorldCreator creator;

    private Array<Item> items;

    public PlayScreen(ManifoldTravelers game) {
        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(ManifoldTravelers.V_WIDTH / ManifoldTravelers.PPM, ManifoldTravelers.V_HEIGHT / ManifoldTravelers.PPM, gamecam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("l1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ManifoldTravelers.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        player = new Player(this);

        items = new Array<Item>();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && player.b2body.getLinearVelocity().y <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0, 0.2f), player.b2body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.S) && player.b2body.getLinearVelocity().y >= -2)
            player.b2body.applyLinearImpulse(new Vector2(0, -0.2f), player.b2body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);


    }

    public void update(float dt) {
        // dt := delta time
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        for(Item item : items){
            item.update(dt);
        }

        for(Spawner spawner : creator.getSpawners()) {
            spawner.update(dt);
        }

        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public void appendItem(Item item) {
        items.add(item);
    }
}
