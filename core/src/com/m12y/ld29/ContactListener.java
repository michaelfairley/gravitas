package com.m12y.ld29;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        System.out.println(fixtureA.getUserData());
        System.out.println(fixtureB.getUserData());
        System.out.println("=====");

        if ("foot".equals(fixtureA.getUserData()) && "floor".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureA.getBody().getUserData();
            setPlayerAngle(fixtureA, fixtureB);
            player.floorsTouching++;
        } else if ("floor".equals(fixtureA.getUserData()) && "foot".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureB.getBody().getUserData();
            setPlayerAngle(fixtureB, fixtureA);
            player.floorsTouching++;

        }
    }

    private void setPlayerAngle(Fixture footFixture, Fixture floorFixture) {
        System.out.println("hello");
        Body playerBody = footFixture.getBody();
        ChainShape floorShape = (ChainShape) floorFixture.getShape();
        Vector2 point1 = new Vector2();
        floorShape.getVertex(0, point1);
        Vector2 point2 = new Vector2();
        floorShape.getVertex(1, point2);
        point2.sub(point1);
        float angle = MathUtils.atan2(point2.y, point2.x);
        Player player = (Player) playerBody.getUserData();
        player.angle = angle;
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ("foot".equals(fixtureA.getUserData()) && "floor".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureA.getBody().getUserData();
            player.floorsTouching--;
        } else if ("floor".equals(fixtureA.getUserData()) && "foot".equals(fixtureB.getUserData())) {
            Player player = (Player) fixtureB.getBody().getUserData();
            player.floorsTouching--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
