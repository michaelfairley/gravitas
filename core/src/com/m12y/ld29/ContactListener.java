package com.m12y.ld29;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ("foot".equals(fixtureA.getUserData()) && "floor".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureA.getBody().getUserData();
            player.startedTouchingFloor(fixtureB);
        } else if ("floor".equals(fixtureA.getUserData()) && "foot".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureB.getBody().getUserData();
            player.startedTouchingFloor(fixtureA);

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ("foot".equals(fixtureA.getUserData()) && "floor".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureA.getBody().getUserData();
            player.stoppedTouchingFloor(fixtureB);
        } else if ("floor".equals(fixtureA.getUserData()) && "foot".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureB.getBody().getUserData();
            player.stoppedTouchingFloor(fixtureA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
