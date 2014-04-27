package com.m12y.ld29;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class LD29 extends ApplicationAdapter {
    public static final Color WHITE = new Color(0.95f, 0.95f, 0.95f, 1);
    public static final Color BLACK = new Color(0.05f, 0.05f, 0.05f, 1);

	World world;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    Player player;
    ShapeRenderer shapeRenderer;

    @Override
	public void create () {
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        setup();
	}

    private void setup() {
        world = new World(Vector2.Zero, true);
        player = new Player(world);
        world.setContactListener(new ContactListener());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 64f, Gdx.graphics.getHeight() / 64f);
        new Floor(world, Floor.floor());
        new Floor(world, Floor.floater1());
        new Floor(world, Floor.floater2());
    }

    @Override
	public void render () {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) setup();

        player.update();
        world.step(1/60f, 6, 2);

        Vector2 playerPos = player.body.getPosition();
        Vector2 centering = new Vector2(0, 3).rotateRad(player.angle).add(playerPos);

        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.up.rotateRad(camera.direction, -player.angle);
        camera.direction.rotateRad(camera.direction, -player.angle);
        camera.translate(centering.x - camera.position.x, centering.y - camera.position.y);

        camera.update();

		Gdx.gl.glClearColor(BLACK.r, BLACK.g, BLACK.b, BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        Floor.draw(shapeRenderer);
        player.draw(shapeRenderer);

//        debugRenderer.render(world, camera.combined);
	}

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 64f, height / 64f);
    }
}
