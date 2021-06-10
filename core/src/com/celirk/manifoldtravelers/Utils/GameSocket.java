package com.celirk.manifoldtravelers.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.celirk.manifoldtravelers.ManifoldTravelers;
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

    private JSONObject buffer;
    private boolean needUpdate = false;

    public GameSocket(PlayScreen screen) {
        this.screen = screen;

        connectSocket();
        configSocketEvents();

        socket.emit("requestWorld");
    }

    public void connectSocket(){
        try{

            socket = IO.socket("http://localhost:5432");
            //socket = IO.socket("http://10.44.64.238:5432");
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
                //System.out.println(data);
                try {
                    isHost = data.getBoolean("isHost");
                    //System.out.println(isHost);
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
                System.out.println(data);
                try {
                    JSONObject players_box2d = data.getJSONObject("players_box2d");
                    JSONObject players_attribute = data.getJSONObject("players_attribute");
                    Iterator<String> keys = players_box2d.keys();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        System.out.println(key);
                        JSONObject player_box2d = players_box2d.getJSONObject(key);
                        JSONObject player_attribute = players_attribute.getJSONObject(key);
                        float x = (float) player_box2d.getDouble("x");
                        float y = (float) player_box2d.getDouble("y");
                        float velocity_x = (float) player_box2d.getDouble("velocity_x");
                        float velocity_y = (float) player_box2d.getDouble("velocity_y");
                        float hit_point = (float) player_attribute.getDouble("hit_point");
                        int weapon_on_hand = player_attribute.getInt("weapon_on_hand");


                        if(socket_id.equals(key)) {
                            Player player = new Player(screen, x, y);
                            player.setHitPoint(hit_point);
                            player.setVelocity(velocity_x, velocity_y);
                            player.setWeapon_on_hand(weapon_on_hand);
                            player.setId(key);
                            screen.setPlayer(player);
                        } else {
                            Player enemy = new Player(screen, x, y);
                            enemy.setHitPoint(hit_point);
                            enemy.setVelocity(velocity_x, velocity_y);
                            enemy.setWeapon_on_hand(weapon_on_hand);
                            enemy.setId(key);
                            screen.getEnemies().put(key, enemy);
                        }
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
                    for(int i = 0; i < projectiles.length(); i++){
                        JSONObject projectiles_ = projectiles.getJSONObject(i);
                        float x = (float) projectiles_.getDouble("x");
                        float y = (float) projectiles_.getDouble("y");
                        float velocity_x = (float) projectiles_.getDouble("velocity_x");
                        float velocity_y = (float) projectiles_.getDouble("velocity_y");
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
                    screen.setInitialized(true);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting world");
                    System.out.println(data);
                    System.out.println(e);
                }
            }
        }).on("hostUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(screen.isInitialized()) {
                    buffer = (JSONObject) args[0];
                    needUpdate = true;
                }
            }
        }).on("slaveUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(screen.isInitialized()) {
                    buffer = (JSONObject) args[0];
                    needUpdate = true;
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
                    String id = newPlayer.getString("id");

                    JSONObject player_attribute = newPlayer.getJSONObject("player_attribute");
                    float hit_point = (float) player_attribute.getDouble("hit_point");
                    int weapon_on_hand = player_attribute.getInt("weapon_on_hand");

                    Player enemy = new Player(screen, x, y);
                    enemy.setHitPoint(hit_point);
                    enemy.setVelocity(velocity_x, velocity_y);
                    enemy.setWeapon_on_hand(weapon_on_hand);
                    enemy.setId(id);
                    screen.getEnemies().put(id, enemy);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("newProjectile", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject projectile = (JSONObject) args[0];
                System.out.println(projectile);
                try {
                    float x = (float) projectile.getDouble("x");
                    float y = (float) projectile.getDouble("y");
                    float velocity_x = (float) projectile.getDouble("velocity_x");
                    float velocity_y = (float) projectile.getDouble("velocity_y");
                    int id = projectile.getInt("id");
                    Projectile projectile_;
                    switch (id) {
                        case 1:
                            projectile_ = new PistolBullet(screen, x, y, new Vector2(velocity_x, velocity_y));
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
                    screen.getEnemies().get(id).destroy();
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        });
    }

    public void pushHostUpdate(){
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject players_attribute = new JSONObject();
            players_attribute.put(socket_id, screen.getPlayer().getJsonAttribute());
            for (HashMap.Entry<String, Player> entry : screen.getEnemies().entrySet()) {
                players_attribute.put(entry.getKey(), entry.getValue().getJsonAttribute());
            }
            jsonObject.put("players_attribute", players_attribute);

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

            socket.emit("hostUpdate", jsonObject);
        }
        catch (JSONException e) {
            System.out.println(e);
        }
    }

    public void pushSlaveUpdate() {
        socket.emit("slaveUpdate", screen.getPlayer().getJsonBox2d());
    }

    public void newProjectile(JSONObject json){
        socket.emit("newProjectile", json);
    }

    public boolean isHost() {
        return isHost;
    }

    public void slaveUpdateFromBuffer() {
        try {
            JSONObject players_box2d = buffer.getJSONObject("players_box2d");
            JSONObject players_attribute = buffer.getJSONObject("players_attribute");
            Iterator<String> keys = players_box2d.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject player_box2d = players_box2d.getJSONObject(key);
                JSONObject player_attribute = players_attribute.getJSONObject(key);
                float x = (float) player_box2d.getDouble("x");
                float y = (float) player_box2d.getDouble("y");
                float velocity_x = (float) player_box2d.getDouble("velocity_x");
                float velocity_y = (float) player_box2d.getDouble("velocity_y");
                float hit_point = (float) player_attribute.getDouble("hit_point");
                int weapon_on_hand = player_attribute.getInt("weapon_on_hand");

                if (socket_id.equals(key)) {
                    Player player = screen.getPlayer();
                    player.setHitPoint(hit_point);
                    player.setWeapon_on_hand(weapon_on_hand);
                }
                else {
                    Player enemy = screen.getEnemies().get(key);
                    enemy.setPos(x, y);
                    enemy.setHitPoint(hit_point);
                    enemy.setVelocity(velocity_x, velocity_y);
                    enemy.setWeapon_on_hand(weapon_on_hand);
                }
            }

            JSONArray items = buffer.getJSONArray("items");
            int item_json_size = items.length();
            int item_game_size = screen.getItems().size;
            int i = 0;
            for (Item item : screen.getItems()) {
                if(i < item_json_size) {
                    JSONObject item_ = items.getJSONObject(i);
                    float x = (float) item_.getDouble("x");
                    float y = (float) item_.getDouble("y");
                    float velocity_x = (float) item_.getDouble("velocity_x");
                    float velocity_y = (float) item_.getDouble("velocity_y");
                    int id = item_.getInt("id");

                    item.setPos(x, y);
                    item.setVelocity(velocity_x, velocity_y);
                    item.setId(id);
                    i++;
                }
                else {
                    item.destroy();
                }
            }
            // need new items
            for(int iter = 0; iter < item_json_size - item_game_size; iter++) {
                JSONObject items_ = items.getJSONObject(i);
                float x = (float) items_.getDouble("x");
                float y = (float) items_.getDouble("y");
                float velocity_x = (float) items_.getDouble("velocity_x");
                float velocity_y = (float) items_.getDouble("velocity_y");
                int id = items_.getInt("id");
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


            JSONArray projectiles = buffer.getJSONArray("projectiles");
            int projectile_json_size = projectiles.length();
            int projectile_game_size = screen.getProjectiles().size;
            int j = 0;
            for (Projectile projectile : screen.getProjectiles()) {
                if(j < projectile_json_size) {
                    JSONObject projectiles_ = projectiles.getJSONObject(j);
                    float x = (float) projectiles_.getDouble("x");
                    float y = (float) projectiles_.getDouble("y");
                    float velocity_x = (float) projectiles_.getDouble("velocity_x");
                    float velocity_y = (float) projectiles_.getDouble("velocity_y");
                    int id = projectiles_.getInt("id");

                    projectile.setPos(x, y);
                    projectile.setVelocity(velocity_x, velocity_y);
                    projectile.setId(id);
                    j++;
                }
                else {
                    projectile.destroy();
                }
            }
            // need new projectiles
            for(int iter = 0; iter < projectile_json_size - projectile_game_size; iter++) {
                JSONObject projectiles_ = projectiles.getJSONObject(j);
                float x = (float) projectiles_.getDouble("x");
                float y = (float) projectiles_.getDouble("y");
                float velocity_x = (float) projectiles_.getDouble("velocity_x");
                float velocity_y = (float) projectiles_.getDouble("velocity_y");
                int id = projectiles_.getInt("id");
                Projectile projectile_;
                switch (id) {
                    case 1:
                        projectile_ = new PistolBullet(screen, x, y, new Vector2(velocity_x, velocity_y));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + id);
                }
                screen.appendProjectile(projectile_);
            }

        } catch (JSONException e) {
            Gdx.app.log("SocketIO", "Error updating slave");
            System.out.println(buffer);
            System.out.println(e);
        }
    }

    public void hostUpdateFromBuffer() {
        try {
            JSONObject players_box2d = buffer.getJSONObject("players_box2d");
            Iterator<String> keys = players_box2d.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject player_box2d = players_box2d.getJSONObject(key);
                float x = (float) player_box2d.getDouble("x");
                float y = (float) player_box2d.getDouble("y");
                float velocity_x = (float) player_box2d.getDouble("velocity_x");
                float velocity_y = (float) player_box2d.getDouble("velocity_y");

                if(socket_id.equals(key)) {
                    continue;
                }
                Player enemy = screen.getEnemies().get(key);
                enemy.setPos(x, y);
                enemy.setVelocity(velocity_x, velocity_y);
                screen.getEnemies().put(key, enemy);
            }
        }catch(JSONException e){
            Gdx.app.log("SocketIO", "Error updating host");
            System.out.println(buffer);
            System.out.println(e);
        }
    }

    public boolean needUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
