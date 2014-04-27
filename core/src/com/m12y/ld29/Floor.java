package com.m12y.ld29;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Arrays;

public class Floor {
    final Body body;

    public Floor(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(0, 0));
        body = world.createBody(bodyDef);

        float[] vertices = new float[]{
                1, 1,
                7, 1,
                8, 2,
                8, 8,
                1, 8,
                1, 1,
                7, 1,
                8, 2
        };

        for (int i = 2; i < vertices.length-4; i+=2) {
            ChainShape shape = new ChainShape();
            shape.createChain(Arrays.copyOfRange(vertices, i, i+4));
            shape.setPrevVertex(vertices[i-2], vertices[i-1]);
            shape.setNextVertex(vertices[i+4], vertices[i+5]);

            Fixture fixture = body.createFixture(shape, 0);
            fixture.setUserData("floor");

            shape.dispose();
        }
    }
}
