package com.m12y.ld29;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashSet;
import java.util.Set;

public class Player {
    final Body body;
    float angle;
    private Set<Fixture> floorsTouching;
    boolean won;
    boolean dead;

    public Player(World world) {
        angle = 0;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(2.5f, 0);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{
                -0.48f, 0.5f,
                0.48f, 0.5f,
                0.48f, 1.7f,
                -0.48f, 1.7f
        });
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        fixtureDef.density = 1f;
        Fixture topFixture = body.createFixture(fixtureDef);
        topFixture.setUserData("player");
        shape.dispose();

        CircleShape shape3 = new CircleShape();
        shape3.setRadius(0.5f);
        shape3.setPosition(new Vector2(0, 0.51f));
        fixtureDef.shape = shape3;
        Fixture bottomFixture = body.createFixture(fixtureDef);
        bottomFixture.setUserData("player");
        shape3.dispose();

        PolygonShape shape2 = new PolygonShape();
        shape2.set(new float[]{
                -0.01f, -0.25f,
                0.01f, -0.25f,
                0.01f, 0f,
                -0.01f, 0f
        });
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = shape2;
        fixtureDef2.friction = 0;
        fixtureDef2.isSensor = true;
        fixtureDef2.density = 1;
        Fixture footFixture = body.createFixture(fixtureDef2);
        footFixture.setUserData("foot");
        shape2.dispose();

        floorsTouching = new HashSet<Fixture>();
    }

    public void update() {
        body.setTransform(body.getPosition(), angle);

        Vector2 delta = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            delta.add(-4, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F) ||
                Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            delta.add(4, 0);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.E) ||
                Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.UP)) && isOnGround()) {
            delta.add(0, 1.6f);
        }

        delta.add(0, body.getLinearVelocity().rotateRad(-angle).y);

        body.setLinearVelocity(delta.rotateRad(angle));

        body.applyForceToCenter(new Vector2(0, -25).rotateRad(angle), true);
    }

    public boolean isOnGround() {
        return !floorsTouching.isEmpty();
    }

    public void startedTouchingFloor(Fixture floor) {
        float angleDifference = Math.abs(floorAngle(floor) - angle);
        if (angleDifference > 1 && angleDifference < MathUtils.PI2-1) return;
        floorsTouching.add(floor);
        calculateAngle();
    }

    public void stoppedTouchingFloor(Fixture floor) {
        floorsTouching.remove(floor);
        calculateAngle();
    }

    public void calculateAngle() {
        if (floorsTouching.isEmpty()) return;
        float sum = 0;

        boolean anglesNear2Pi = false;

        for (Fixture fixture : floorsTouching) {
            if (floorAngle(fixture) > MathUtils.PI2 - 1) {
                anglesNear2Pi = true;
                break;
            }
        }

        for (Fixture fixture : floorsTouching) {
            float fAngle = floorAngle(fixture);
            if (anglesNear2Pi && fAngle < 1) {
                fAngle += MathUtils.PI2;
            }
            sum += fAngle;
        }

        angle = sum / floorsTouching.size();
    }

    static float floorAngle(Fixture fixture) {
        ChainShape floorShape = (ChainShape) fixture.getShape();
        Vector2 point1 = new Vector2();
        floorShape.getVertex(0, point1);
        Vector2 point2 = new Vector2();
        floorShape.getVertex(1, point2);
        point2.sub(point1);
        float angle = MathUtils.atan2(point2.y, point2.x);
        return (angle + MathUtils.PI2) % MathUtils.PI2;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(LD29.GRAY);

        Vector2 pos = body.getPosition();

        Vector2 bottomLeft = new Vector2(-0.5f, 0);
        bottomLeft.rotateRad(angle);
        bottomLeft.add(pos);

        Vector2 shape = new Vector2(1, 1.7f);

        shapeRenderer.rect(
                bottomLeft.x,
                bottomLeft.y,
                shape.x,
                shape.y,
                0,
                0,
                MathUtils.radiansToDegrees * angle
        );

        shapeRenderer.end();
    }

    public void stop() {
        Vector2 delta = new Vector2(0, body.getLinearVelocity().rotateRad(-angle).y);

        body.setLinearVelocity(delta.rotateRad(angle));
    }

    public void win() {
        won = true;
        stop();
    }

    public void die() {
        dead = true;
        stop();
    }
}
