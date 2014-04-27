package com.m12y.gravitas;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

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

        for (float t = (quadrant-1) * MathUtils.PI/2; t < quadrant * MathUtils.PI/2; t += 0.05f/r) {
            vertices.add(new Vector2(a + r * MathUtils.cos(t), b + r * MathUtils.sin(t)));
        }

        return vertices;
    }

    public static ArrayList<Vector2> outerCorner(float x, float y, float r, int quadrant) {
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

        for (float t = quadrant * MathUtils.PI/2; t > (quadrant-1) * MathUtils.PI/2; t -= 0.05f) {
            vertices.add(new Vector2(a + r * MathUtils.cos(t), b + r * MathUtils.sin(t)));
        }

        return vertices;
    }

    public static ArrayList<Vector2> floor() {
        ArrayList<Vector2> vertices = new ArrayList<Vector2>();

        // Start
        vertices.add(new Vector2(0, 0));
        // Big stairs
        vertices.add(new Vector2(5, 0));
        vertices.add(new Vector2(5, 2));
        vertices.add(new Vector2(7, 2));
        vertices.add(new Vector2(7, 4));
        vertices.add(new Vector2(9, 4));
        // Pit
        vertices.add(new Vector2(9, -6));
        vertices.add(new Vector2(8, -6));
        // Underground corner
        vertices.addAll(corner(8, -4, 1, 1));
        vertices.add(new Vector2(5, -4));
        vertices.add(new Vector2(5, -10));
        vertices.add(new Vector2(11, -10));
        // Other side of pit
        vertices.add(new Vector2(11, 5));
        vertices.add(new Vector2(13, 5));
        vertices.add(new Vector2(13, 0));
        // First corner
        vertices.addAll(corner(17, 0, 1, 4));
        vertices.add(new Vector2(17, 4));
        vertices.add(new Vector2(15, 4));
        // Top right staircase
        vertices.add(new Vector2(15, 12));
        vertices.add(new Vector2(14, 12));
        vertices.add(new Vector2(14, 13));
        vertices.add(new Vector2(13, 13));
        vertices.add(new Vector2(13, 14));
        // Top blocker
        vertices.add(new Vector2(1, 14));
        vertices.add(new Vector2(1, 10));
        vertices.add(new Vector2(-4, 10));
        vertices.addAll(outerCorner(-4, 8, 1, 4));
        vertices.add(new Vector2(-9, 8));
        vertices.addAll(corner(-9, 0, 3, 3));
        // End area
        vertices.add(new Vector2(-1, 0));
        vertices.add(new Vector2(-1, 4));
        vertices.add(new Vector2(-6, 4));
        vertices.add(new Vector2(-6, 5));
        vertices.add(new Vector2(0, 5));

        return vertices;
    }

    public static ArrayList<Vector2> floater1() {
        ArrayList<Vector2> vertices = new ArrayList<Vector2>();

        vertices.add(new Vector2(11, 11));
        vertices.add(new Vector2(12, 11));
        vertices.add(new Vector2(12, 10));
        vertices.add(new Vector2(13, 10));
        vertices.add(new Vector2(13, 9));
        vertices.add(new Vector2(11, 9));

        return vertices;
    }

    public static ArrayList<Vector2> floater2() {
        ArrayList<Vector2> vertices = new ArrayList<Vector2>();

        vertices.add(new Vector2(5, 11));
        vertices.add(new Vector2(8, 11));
        vertices.add(new Vector2(8, 10));
        vertices.add(new Vector2(5, 10));

        return vertices;
    }

    public static void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Gravitas.WHITE);

        shapeRenderer.rect(0, 0, 5, 10);
        shapeRenderer.rect(5, 2, 2, 8);
        shapeRenderer.rect(7, 4, 4, 6);
        shapeRenderer.rect(11, 5, 4, 4);
        shapeRenderer.rect(13, 0, 2, 12);
        shapeRenderer.rect(15, 0, 1, 4);
        shapeRenderer.rect(16, 1, 1, 3);
        shapeRenderer.arc(16, 1, 1, 269, 91, 20);
        shapeRenderer.rect(9, -6, 2, 11);
        shapeRenderer.rect(5, -10, 6, 4);
        shapeRenderer.rect(5, -6, 2, 2);
        shapeRenderer.rect(7, -6, 1, 1);
        shapeRenderer.arc(7, -5, 1, -1, 92, 20);
        shapeRenderer.rect(12, 10, 2, 3);
        shapeRenderer.rect(1, 11, 12, 3);
        shapeRenderer.rect(1, 10, 4, 1);
        shapeRenderer.rect(8, 10, 3, 1);
        shapeRenderer.rect(-2, 5, 2, 5);
        shapeRenderer.rect(-4, 5, 2, 5);
        shapeRenderer.arc(-6, 3, 3, 180, 92, 20);
        shapeRenderer.rect(-9, 3, 2, 5);
        shapeRenderer.rect(-7, 3, 1, 2);
        shapeRenderer.rect(-7, 5, 3, 3);
        shapeRenderer.rect(-6, 0, 5, 4);
        shapeRenderer.rect(-5, 8, 1, 1);

        shapeRenderer.setColor(Gravitas.BLACK);
        shapeRenderer.arc(-5, 9, 1, 269, 92, 20);

        shapeRenderer.end();
    }
}
