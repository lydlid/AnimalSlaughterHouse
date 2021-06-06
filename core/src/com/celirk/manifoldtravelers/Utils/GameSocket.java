package com.celirk.manifoldtravelers.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Item.Item;
import com.celirk.manifoldtravelers.Sprites.Item.Pistol;
import com.celirk.manifoldtravelers.Sprites.Player;
import com.celirk.manifoldtravelers.Sprites.Projectile.PistolBullet;
import com.celirk.manifoldtravelers.Sprites.Projectile.Projectile;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class GameSocket {
    private Socket socket;
    private PlayScreen screen;
    private boolean isHost;
    private String socket_id;

    public GameSocket(PlayScreen screen) {
        this.screen = screen;

        connectSocket();
        configSocketEvents();

        socket.emit("requestWorld");
    }

    public boolean isHost() {
        return isHost;
    }

    public void connectSocket(){
        try{
            socket = IO.socket("http://localhost:5432");
            socket.connect();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO","Connected");
            }
        }).on("isHost", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    isHost = data.getBoolean("isHost");
                    System.out.println(isHost);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error checking host");
                }
            }
        }).on("socketID", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    socket_id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID:" + socket_id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("fullWorld", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    JSONObject players_box2d = data.getJSONObject("players_box2d");
                    JSONObject players_attribute = data.getJSONObject("players_attribute");
                    Iterator<String> keys = players_box2d.keys();
                    //System.out.println(data);
                    while(keys.hasNext()) {
                        String key = keys.next();
                        JSONObject player_box2d = players_box2d.getJSONObject(key);
                        JSONObject player_attribute = players_attribute.getJSONObject(key);
                        float x = (float) player_box2d.getDouble("x");
                        float y = (float) player_box2d.getDouble("y");
                        float velocity_x = (float) player_box2d.getDouble("velocity_x");
                        float velocity_y = (float) player_box2d.getDouble("velocity_y");
                        float hit_point = (float) player_attribute.getDouble("hit_point");
                        int weapon_on_hand = player_attribute.getInt("weapon_on_hand");


                        if(socket_id.equals(key)) {
                            continue;
                        }
                        Player enemy = new Player(screen, x, y);
                        enemy.setHitPoint(hit_point);
                        enemy.setVelocity(velocity_x, velocity_y);
                        enemy.setWeapon_on_hand(weapon_on_hand);
                        screen.getEnemies().put(key, enemy);
                    }

                    JSONArray items = data.getJSONArray("items");
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item_ = items.getJSONObject(i);
                        float x = (float) item_.getDouble("x");
                        float y = (float) item_.getDouble("y");
                        float velocity_x = (float) item_.getDouble("velocity_x");
                        float velocity_y = (float) item_.getDouble("velocity_y");
                        int id = item_.getInt("id");
                        Item item;
                        switch (id) {
                            case 1:
                                item = new Pistol(screen, x, y);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + id);
                        }

                        item.setVelocity(velocity_x, velocity_y);
                        screen.appendItem(item);
                    }

                    JSONArray projectiles = data.getJSONArray("projectiles");
                    for(int i = 0; i < items.length(); i++){
                        JSONObject projectiles_ = projectiles.getJSONObject(i);
                        float x = (float) projectiles_.getDouble("x");
                        float y = (float) projectiles_.getDouble("y");
                        float velocity_x = (float) projectiles_.getDouble("velocity_x");
                        float velocity_y = (float) projectiles_.getDouble("velocity_y");
                        float attack = (float) projectiles_.getDouble("attack");
                        int id = projectiles_.getInt("id");
                        Projectile projectile;
                        switch (id) {
                            case 1:
                                projectile = new PistolBullet(screen, x, y, new Vector2(velocity_x,velocity_y));
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + id);
                        }
                        screen.appendProjectile(projectile);
                    }

                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting world");
                    System.out.println(data);
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject newPlayer = (JSONObject) args[0];
                try {
                    JSONObject player_box2d = newPlayer.getJSONObject("player_box2d");
                    float x = (float) player_box2d.getDouble("x");
                    float y = (float) player_box2d.getDouble("y");
                    float velocity_x = (float) player_box2d.getDouble("velocity_x");
                    float velocity_y = (float) player_box2d.getDouble("velocity_y");

                    JSONObject player_attribute = newPlayer.getJSONObject("player_attribute");
                    float hit_point = (float) player_attribute.getDouble("hit_point");
                    int weapon_on_hand = player_attribute.getInt("weapon_on_hand");

                    Player enemy = new Player(screen, x, y);
                    enemy.setHitPoint(hit_point);
                    enemy.setVelocity(velocity_x, velocity_y);
                    enemy.setWeapon_on_hand(weapon_on_hand);
                    screen.getEnemies().put(newPlayer.getString("id"), enemy);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("selfPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject newPlayer = (JSONObject) args[0];
                try {
                    JSONObject player_box2d = newPlayer.getJSONObject("player_box2d");
                    float x = (float) player_box2d.getDouble("x");
                    float y = (float) player_box2d.getDouble("y");
                    float velocity_x = (float) player_box2d.getDouble("velocity_x");
                    float velocity_y = (float) player_box2d.getDouble("velocity_y");

                    JSONObject player_attribute = newPlayer.getJSONObject("player_attribute");
                    float hit_point = (float) player_attribute.getDouble("hit_point");
                    int weapon_on_hand = player_attribute.getInt("weapon_on_hand");

                    Player player = new Player(screen, x, y);
                    player.setHitPoint(hit_point);
                    player.setVelocity(velocity_x, velocity_y);
                    player.setWeapon_on_hand(weapon_on_hand);
                    screen.setPlayer(player);

                    screen.setInitialized(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("newProjectile", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject projectile = (JSONObject) args[0];
                try {
                    float x = (float) projectile.getDouble("x");
                    float y = (float) projectile.getDouble("y");
                    float velocity_x = (float) projectile.getDouble("velocity_x");
                    float velocity_y = (float) projectile.getDouble("velocity_y");
                    float attack = (float) projectile.getDouble("attack");
                    int id = projectile.getInt("id");
                    Projectile projectile_;
                    switch (id) {
                        case 1:
                            projectile_ = new PistolBullet(screen, x, y, new Vector2(velocity_x,velocity_y));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + id);
                    }
                    screen.appendProjectile(projectile_);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    screen.getEnemies().remove(id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        });
    }

    public void hostUpdate(){
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject players_json = new JSONObject();
            players_json.put(socket_id, screen.getPlayer().getJsonAttribute());
            for (HashMap.Entry<String, Player> entry : screen.getEnemies().entrySet()) {
                players_json.put(entry.getKey(), entry.getValue().getJsonAttribute());
            }
            jsonObject.put("players", players_json);

            JSONArray items_json = new JSONArray();
            for (Item item : screen.getItems()) {
                items_json.put(item.getJson());
            }
            jsonObject.put("items", items_json);

            JSONArray projectiles_json = new JSONArray();
            for (Projectile projectile : screen.getProjectiles()) {
                projectiles_json.put(projectile.getJson());
            }
            jsonObject.put("projectiles", projectiles_json);

            socket.emit("pushUpdate", jsonObject);
        }
        catch (JSONException e) {
            System.out.println(e);
        }
    }

    public void slaveUpdate() {
        socket.emit("slaveUpdate", screen.getPlayer().getJsonAttribute());
    }

    public void newProjectile(JSONObject json){
        socket.emit("newProjectile", json);
    }

}
