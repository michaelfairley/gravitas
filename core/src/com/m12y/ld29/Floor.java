package com.m12y.ld29;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Floor {
    final Body body;

    public Floor(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(0, 0));
        body = world.createBody(bodyDef);

        ArrayList<Vector2> vertices = new ArrayList<Vector2>();
        vertices.add(new Vector2(1, 1));
        vertices.add(new Vector2(7, 1));
        vertices.add(new Vector2(8, 2));
        vertices.add(new Vector2(8, 8));
        vertices.add(new Vector2(1, 8));

        for (int i = 0; i < vertices.size(); i++) {
            ChainShape shape = new ChainShape();
            shape.createChain(new Vector2[]{vertices.get(i), vertices.get((i+1)%vertices.size())});
            shape.setPrevVertex(vertices.get((i-1+vertices.size())%vertices.size()));
            shape.setNextVertex(vertices.get((i+2)%vertices.size()));

            Fixture fixture = body.createFixture(shape, 0);
            fixture.setUserData("floor");

            shape.dispose();
        }
    }
}
