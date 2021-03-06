package com.celirk.manifoldtravelers.Utils;

import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Sprites.Item.Item;
import com.celirk.manifoldtravelers.Sprites.Player;
import com.celirk.manifoldtravelers.Sprites.Projectile.Projectile;
import com.celirk.manifoldtravelers.Sprites.Tile.Spawner.Spawner;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case ManifoldTravelers.CATEGORY_PLAYER | ManifoldTravelers.CATEGORY_ITEM:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_PLAYER) {
                    ((Player) fixA.getUserData()).acquireItem(((Item) fixB.getUserData()).getID());
                    ((Item) fixB.getUserData()).destroy();
                }
                else {
                    ((Player) fixB.getUserData()).acquireItem(((Item) fixA.getUserData()).getID());
                    ((Item) fixA.getUserData()).destroy();
                }
                break;
            case ManifoldTravelers.CATEGORY_SPAWNER | ManifoldTravelers.CATEGORY_ITEM:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_SPAWNER) {
                    ((Spawner) fixA.getUserData()).setOccupied(true);
                }
                else {
                    ((Spawner) fixB.getUserData()).setOccupied(true);
                }
                break;
            case  ManifoldTravelers.CATEGORY_PROJECTILE | ManifoldTravelers.CATEGORY_PLAYER:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_PLAYER) {
                    ((Player) fixA.getUserData()).hit(((Projectile) fixB.getUserData()).getAttack());
                    ((Projectile) fixB.getUserData()).destroy();
                }
                else {
                    ((Player) fixB.getUserData()).hit(((Projectile) fixA.getUserData()).getAttack());
                    ((Projectile) fixA.getUserData()).destroy();
                }
                break;
            case  ManifoldTravelers.CATEGORY_PROJECTILE | ManifoldTravelers.CATEGORY_GROUND:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_PROJECTILE) {
                    ((Projectile) fixA.getUserData()).destroy();
                }
                else {
                    ((Projectile) fixB.getUserData()).destroy();
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case ManifoldTravelers.CATEGORY_SPAWNER | ManifoldTravelers.CATEGORY_ITEM:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_SPAWNER) {
                    ((Spawner) fixA.getUserData()).setOccupied(false);
                }
                else {
                    ((Spawner) fixB.getUserData()).setOccupied(false);
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
