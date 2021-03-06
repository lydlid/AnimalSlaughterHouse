package com.celirk.manifoldtravelers.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.celirk.manifoldtravelers.Sprites.Item.Item;
import com.celirk.manifoldtravelers.Sprites.Item.Pistol;
import com.celirk.manifoldtravelers.Sprites.Player;
import com.celirk.manifoldtravelers.Sprites.Projectile.Projectile;
import com.celirk.manifoldtravelers.Sprites.Tile.Spawner.Spawner;
import com.celirk.manifoldtravelers.Utils.B2WorldCreator;
import com.celirk.manifoldtravelers.Utils.GameSocket;
import com.celirk.manifoldtravelers.Utils.WorldContactListener;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;


import java.util.ArrayList;
import java.util.HashMap;

public class PlayScreen implements Screen {
    private final TextureAtlas atlas;
    private final TextureAtlas particleAtlas;

    private final TextureAtlas gunPack;

    private final GameSocket socket;

    private final ManifoldTravelers game;

    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    private final Hud hud;

    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;

    private final B2WorldCreator creator;

    private Player player;

    private final HashMap<String, Player> enemies;
    private final ArrayList<String> enemies_to_destroy;

    private final Array<Item> items;

    private final Array<Projectile> projectiles;

    private final int update_every_n_frames = 5;
    private final int n_frames_without_update = update_every_n_frames;

    private boolean isInitialized;

    private final Music music;

    private final Array<ParticleEffect> particle_effects;


    public PlayScreen(ManifoldTravelers game) {
        isInitialized = false;

        // definition at same place
        atlas = new TextureAtlas("animalWithBullet.pack");
        gunPack = new TextureAtlas("weapon/weapon.pack");
        particleAtlas = new TextureAtlas("ParticleEffects/particle.pack");

        particle_effects = new Array<ParticleEffect>();

        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(ManifoldTravelers.V_WIDTH / ManifoldTravelers.PPM, ManifoldTravelers.V_HEIGHT / ManifoldTravelers.PPM, gamecam);

        hud = new Hud(game.batch, this);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tmp1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ManifoldTravelers.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());
        // temp player
        // player = new Player(this, 128f, 128f);

        enemies = new HashMap<>();
        enemies_to_destroy = new ArrayList<>();

        items = new Array<Item>(false, 128);

        projectiles = new Array<Projectile>(false, 128);

        socket = new GameSocket(this);

        music = ManifoldTravelers.manager.get("audio/music/bgm.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas() {
        return atlas;
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
        game.batch.begin();
        if (isInitialized) {
            player.draw(game.batch);
            for (HashMap.Entry<String, Player> entry : enemies.entrySet()) {
                if (entry.getValue().getHit_point() > 0)
                    entry.getValue().draw(game.batch);
            }
        }
        //draw weapon pack
        for (Item item : items) {
            item.draw(game.batch);
        }
        //draw bullet
        for (Projectile projectile : projectiles) {
            projectile.draw(game.batch);
        }
        // draw particles
        for (ParticleEffect effect : particle_effects) {
            effect.draw(game.batch, delta);

        }


        game.batch.end();

        hud.stage.draw();

        if (isInitialized && gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }


    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && player.body.getLinearVelocity().y <= 2)
            player.body.applyLinearImpulse(new Vector2(0, 0.2f), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.S) && player.body.getLinearVelocity().y >= -2)
            player.body.applyLinearImpulse(new Vector2(0, -0.2f), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.body.getLinearVelocity().x <= 2)
            player.body.applyLinearImpulse(new Vector2(0.2f, 0), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -2)
            player.body.applyLinearImpulse(new Vector2(-0.2f, 0), player.body.getWorldCenter(), true);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            player.shoot(Gdx.input.getX() - gamePort.getScreenWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - gamePort.getScreenHeight() / 2,
                    delta);
        }
    }

    public void update(float dt) {
        if (isInitialized) {

            if (socket.needUpdate()) {
                if (socket.isHost()) {

                    socket.hostUpdateFromBuffer();
                } else {
                    socket.slaveUpdateFromBuffer();
                }
                socket.setNeedUpdate(false);
            }

            handleInput(dt);
            player.update(dt);

            for (HashMap.Entry<String, Player> entry : enemies.entrySet()) {
                entry.getValue().update(dt);
            }
            for (String id : enemies_to_destroy) {
                enemies.remove(id);
            }

            for (Projectile projectile : projectiles) {
                projectile.update(dt);
            }

            for (Item item : items) {
                item.update(dt);
            }

            if (socket.isHost()) {
                for (Spawner spawner : creator.getSpawners()) {
                    spawner.update(dt);
                }
                socket.pushHostUpdate();
                socket.pushSlaveUpdate();
            } else {
                socket.pushSlaveUpdate();
            }

            hud.update(dt);
            gamecam.position.x = player.body.getPosition().x;
            gamecam.position.y = player.body.getPosition().y;
        }

        world.step(dt, 6, 2);


        //n_frames_without_update++;
        // update with server

        gamecam.update();
        renderer.setView(gamecam);
    }

    public boolean gameOver() {
        return player.currentState == Player.State.DEAD && player.getStateTimer() > 3;
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

    public void removeItem(Item item) {
        items.removeValue(item, true);
    }

    public void appendProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void removeProjectile(Projectile projectile) {
        projectiles.removeValue(projectile, true);
    }

    public HashMap<String, Player> getEnemies() {
        return enemies;
    }

    public Array<Item> getItems() {
        return items;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public GameSocket getSocket() {
        return socket;
    }

    public void appendParticleEffect(ParticleEffect effect) {
        particle_effects.add(effect);
    }

    public void removeEnemy(String id) {
        enemies_to_destroy.add(id);
    }

    public TextureAtlas getGunPack() {
        return gunPack;
    }

    public TextureAtlas getParticleAtlas() {
        return particleAtlas;
    }
}
