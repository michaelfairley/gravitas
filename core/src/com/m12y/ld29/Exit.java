package com.m12y.ld29;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Exit {
    final Body body;
    private static final float WIDTH = 0.7f;

    public Exit(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(-3, 0);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{
                -WIDTH, 0.0f,
                WIDTH, 0.0f,
                WIDTH, 1.7f,
                -WIDTH, 1.7f
        });
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        Fixture mainFixture = body.createFixture(fixtureDef);
        mainFixture.setUserData("exit");
        shape.dispose();

        CircleShape shape2 = new CircleShape();
        shape2.setRadius(WIDTH);
        shape2.setPosition(new Vector2(0, 1.7f));
        fixtureDef.shape = shape2;
        Fixture topFixture = body.createFixture(fixtureDef);
        topFixture.setUserData("exit");
        shape2.dispose();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(LD29.BLACK);

        Vector2 pos = body.getPosition();

        shapeRenderer.rect(pos.x - WIDTH, pos.y, WIDTH*2, 1.7f);
        shapeRenderer.circle(pos.x, pos.y + 1.7f, WIDTH, 20);

        shapeRenderer.end();
    }
}
