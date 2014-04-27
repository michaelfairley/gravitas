package com.m12y.ld29;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Floor {
    final Body body;

    public Floor(World world, ArrayList<Vector2> vertices) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(0, 0));
        body = world.createBody(bodyDef);

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

    public static ArrayList<Vector2> corner(float x, float y, float r, int quadrant) {
        ArrayList<Vector2> vertices = new ArrayList<Vector2>();

        float a;
        float b;

        switch (quadrant) {
            case 1:
                a = x-r;
                b = y-r;
                break;
            case 2:
                a = x+r;
                b = y-r;
                break;
            case 3:
                a = x+r;
                b = y+r;
                break;
            case 4:
                a = x-r;
                b = y+r;
                break;
            default:
                throw new RuntimeException("that ain't a quadrant i've heard of");
        }

        for (float t = (quadrant-1) * MathUtils.PI/2; t < quadrant * MathUtils.PI/2; t += 0.05f) {
            vertices.add(new Vector2(a + r * MathUtils.cos(t), b + r * MathUtils.sin(t)));
        }

        return vertices;
    }

    public static ArrayList<Vector2> floor1() {
        ArrayList<Vector2> vertices = new ArrayList<Vector2>();

        vertices.addAll(corner(0, 0, 1, 3));
        vertices.add(new Vector2(5, 0));
        vertices.add(new Vector2(5, 2));
        vertices.add(new Vector2(7, 2));
        vertices.add(new Vector2(7, 4));
        vertices.addAll(corner(9, 4, 1, 4));

        vertices.addAll(corner(9, 10, 1, 1));
        vertices.addAll(corner(0, 10, 1, 2));


        return vertices;
    }
}
