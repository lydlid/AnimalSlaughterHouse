package com.celirk.manifoldtravelers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Screens.MenuScreen;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;


public class ManifoldTravelers extends Game {
    //socket
    private Socket socket;


    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 225;
    // pixels per meter
    public static final float PPM = 100;
    // collision filters
    public static final int CATEGORY_PORTALABLE = 0x0001;  //   0000000000000001
    public static final int CATEGORY_PORTAL = 0x0002; //        0000000000000010
    public static final int CATEGORY_PLAYER = 0x0004; //        0000000000000100
    public static final int CATEGORY_GROUND = 0x0008; //        0000000000001000
    public static final int CATEGORY_SPAWNER = 0x0010; //       0000000000010000
    public static final int CATEGORY_ITEM = 0x0020; //          0000000000100000
    public static final int CATEGORY_PROJECTILE = 0x0040; //    0000000001000000

    public static final int MASK_PORTALABLE = CATEGORY_PORTAL;
    public static final int MASK_PORTAL = CATEGORY_PORTALABLE | CATEGORY_PLAYER | CATEGORY_GROUND;
    public static final int MASK_PLAYER = CATEGORY_PORTAL | CATEGORY_GROUND | CATEGORY_ITEM | CATEGORY_PROJECTILE;
    public static final int MASK_GROUND = CATEGORY_PORTAL | CATEGORY_PLAYER | CATEGORY_ITEM | CATEGORY_PROJECTILE;
    public static final int MASK_SPAWNER = CATEGORY_ITEM;
    public static final int MASK_ITEM = CATEGORY_PLAYER | CATEGORY_ITEM | CATEGORY_GROUND | CATEGORY_SPAWNER;
    public static final int MASK_PROJECTILE = CATEGORY_GROUND | CATEGORY_PLAYER;

    public SpriteBatch batch;


    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
        connectSocket();
        configSocketEvents();
}

    @Override
    public void render() {
        super.render();
    }

//	@Override
//	public void dispose () {
//		batch.dispose();
//	}

    public void connectSocket(){
        try{
            System.out.println("I'm here.");
            socket = IO.socket("http://localhost:5432");
            System.out.println(socket.connect());
            System.out.println("I'm here too.");
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
        }).on("socketID", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID:" + id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connected:" + id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        });
    }
}
