package com.m12y.ld29;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Spikes {
    final Body body;

    public Spikes(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(5, -10);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{
                0, 0.0f,
                6, 0.0f,
                6, 0.5f,
                0, 0.5f
        });
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        Fixture mainFixture = body.createFixture(fixtureDef);
        mainFixture.setUserData("spikes");
        shape.dispose();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(LD29.BLACK);

        Vector2 pos = body.getPosition();

        for (int i = 0; i < 12; i++) {
            shapeRenderer.triangle(
                    pos.x + i * 0.5f, pos.y,
                    pos.x + i * 0.5f + 0.25f, pos.y + 0.5f,
                    pos.x + i * 0.5f + 0.5f, pos.y
            );
        }

        shapeRenderer.end();
    }
}
