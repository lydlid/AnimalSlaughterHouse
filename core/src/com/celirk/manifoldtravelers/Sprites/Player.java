package com.celirk.manifoldtravelers.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Screens.PlayScreen;
import com.celirk.manifoldtravelers.Sprites.Indicator.Indicator;
import com.celirk.manifoldtravelers.Sprites.Projectile.PistolBullet;
import com.celirk.manifoldtravelers.Sprites.Projectile.Projectile;

public class Player extends Sprite {
    private TextureRegion playerMove;
    public enum State { UP, DOWN, LEFT, RIGHT};
    public State currentState;
    public State previousState;
    private Animation playerUp;
    private Animation playerDown;
    private Animation playerLeft;
    private Animation playerRight;
    private float stateTimer;
    //private Integer playerDirection;//{ 0:DOWN , 1:LEFT , 2:UP , 3:RIGHT}


    public PlayScreen screen;
    public World world;
    public Body b2body;

    private int weapon_on_hand;

    private float hit_point;
    //private Indicator hp_indicator;

    private float attack_time = -1e10F;
    private float attack_time_segment = 1e10F;

    public Player(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.DOWN;
        previousState  = State.DOWN;
        stateTimer = 0;
        //playerDirection = 0;
        setPosition(x / ManifoldTravelers.PPM, y / ManifoldTravelers.PPM);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("dog"), i*32,0,32, 32));
        }
        playerDown = new Animation(0.1f, frames);
        frames.clear();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("dog"), i*32,32,32, 32));
        }
        playerLeft = new Animation(0.1f, frames);
        frames.clear();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("dog"), i*32,64,32, 32));
        }
        playerRight = new Animation(0.1f, frames);
        frames.clear();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("dog"), i*32,96,32, 32));
        }
        playerUp = new Animation(0.1f, frames);

        playerMove = new TextureRegion(screen.getAtlas().findRegion("dog"), 0,0,32,32);

        definePlayer();
        defineUtils();

        setBounds(0,0,32,32);
        setRegion(playerMove);

    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.linearDamping = 10;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();//circle shape
        shape.setRadius(5 / ManifoldTravelers.PPM);

        fdef.shape = shape;
        fdef.filter.maskBits = ManifoldTravelers.MASK_PLAYER;
        fdef.filter.categoryBits = ManifoldTravelers.CATEGORY_PLAYER;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void defineUtils() {
        weapon_on_hand = 0;
        hit_point = 100;
        //hp_indicator = new Indicator((int) getX(), (int) getY());
    }

    public void update(float dt) {
        attack_time += dt;
        setPosition((float) (b2body.getPosition().x+(11.5)*getWidth()/2), b2body.getPosition().y + 6*getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DOWN:
                region = (TextureRegion) playerDown.getKeyFrame(stateTimer, true);
                break;
            case UP:
                region = (TextureRegion) playerUp.getKeyFrame(stateTimer, true);
                break;
            case LEFT:
                region = (TextureRegion) playerLeft.getKeyFrame(stateTimer, true);
                break;
            case RIGHT:
                region = (TextureRegion) playerRight.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) playerDown.getKeyFrame(stateTimer);
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {

        if(b2body.getLinearVelocity().x > 0)
            return State.RIGHT;
        else if(b2body.getLinearVelocity().x < 0)
            return State.LEFT;
        else
            if(b2body.getLinearVelocity().y > 0)
                return State.UP;
            else if(b2body.getLinearVelocity().y < 0)
                return State.DOWN;
            else
                // default: down
                return State.DOWN;
    }
    public void acquireItem(int id) {
        switch (id){
            case 1:
                attack_time = 1;
                attack_time_segment = 0.5F;
        }
        weapon_on_hand = id;
    }

    public void hit(float delta_hp) {
        hit_point -= delta_hp;

    }

    public void shoot(float x, float y, float dt) {
        if(weapon_on_hand == 0) return;

        if(attack_time > 0) {
            Vector2 direction = new Vector2(x,y);
            direction.nor();
            direction.scl(7 / ManifoldTravelers.PPM);

            Vector2 velocity = new Vector2(x,y);
            velocity.nor();
            velocity.scl(5);

            screen.appendProjectile(new PistolBullet(screen,
                    b2body.getPosition().x + direction.x,
                    b2body.getPosition().y + direction.y,
                    velocity.add(b2body.getLinearVelocity())));
            attack_time = -attack_time_segment;

            b2body.applyLinearImpulse(direction.scl(-2), b2body.getWorldCenter(), true);
        }
    }
}
