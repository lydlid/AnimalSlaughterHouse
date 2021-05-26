package com.celirk.manifoldtravelers.Utils;

import com.badlogic.gdx.physics.box2d.*;
import com.celirk.manifoldtravelers.ManifoldTravelers;
import com.celirk.manifoldtravelers.Sprites.Item;
import com.celirk.manifoldtravelers.Sprites.Player;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case ManifoldTravelers.CATEGORY_PLAYER | ManifoldTravelers.CATEGORY_DROP:
                if(fixA.getFilterData().categoryBits == ManifoldTravelers.CATEGORY_PLAYER) {
                    ((Player) fixA.getUserData()).acquireItem(((Item) fixB.getUserData()).getID());
                }
                else {
                    ((Player) fixB.getUserData()).acquireItem(((Item) fixA.getUserData()).getID());
                }

        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
