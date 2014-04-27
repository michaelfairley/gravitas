package com.m12y.ld29;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class LD29 extends ApplicationAdapter {
    public static final Color WHITE = new Color(0.95f, 0.95f, 0.95f, 1);
    public static final Color BLACK = new Color(0.05f, 0.05f, 0.05f, 1);
    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1);

	World world;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    OrthographicCamera textCamera;
    Player player;
    ShapeRenderer shapeRenderer;
    Exit exit;
    BitmapFont font;
    SpriteBatch spriteBatch;
    Spikes spikes;

    @Override
	public void create () {
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        FileHandle fontFile = Gdx.files.internal("verdana.fnt");
        FileHandle fontImageFile = Gdx.files.internal("verdana.png");
        font = new BitmapFont(fontFile, fontImageFile, false);
        setup();
	}

    private void setup() {
        world = new World(Vector2.Zero, true);
        player = new Player(world);
        world.setContactListener(new ContactListener());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 64f, Gdx.graphics.getHeight() / 64f);
        textCamera = new OrthographicCamera();
        textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        new Floor(world, Floor.floor());
        new Floor(world, Floor.floater1());
        new Floor(world, Floor.floater2());
        exit = new Exit(world);
        spikes = new Spikes(world);
    }

    @Override
	public void render () {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) setup();

        if (!player.won && !player.dead) {
            player.update();
        }
        world.step(1/60f, 6, 2);

        Vector2 playerPos = player.body.getPosition();
        Vector2 centering = new Vector2(0, 3).rotateRad(player.angle).add(playerPos);

        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.up.rotateRad(camera.direction, -player.angle);
        camera.direction.rotateRad(camera.direction, -player.angle);
        camera.translate(centering.x - camera.position.x, centering.y - camera.position.y);
        camera.update();

        centering.scl(64);
        textCamera.up.set(0, 1, 0);
        textCamera.direction.set(0, 0, -1);
        textCamera.up.rotateRad(textCamera.direction, -player.angle);
        textCamera.direction.rotateRad(textCamera.direction, -player.angle);
        textCamera.translate(centering.x - textCamera.position.x, centering.y - textCamera.position.y);
        textCamera.update();

		Gdx.gl.glClearColor(BLACK.r, BLACK.g, BLACK.b, BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(textCamera.combined);
        Floor.draw(shapeRenderer);
        drawExitText();
        drawHelpText();
        if (player.dead) drawGameOverText();
        exit.draw(shapeRenderer);
        player.draw(shapeRenderer);
        spikes.draw(shapeRenderer);

//        debugRenderer.render(world, camera.combined);
	}

    private void drawGameOverText() {
        spriteBatch.begin();

        font.setColor(LD29.BLACK);
        font.draw(spriteBatch, "Game over", 400, -460);

        font.setColor(LD29.WHITE);
        font.draw(spriteBatch, "Press R to try again", 312, -680);

        spriteBatch.end();
    }

    private void drawHelpText() {
        spriteBatch.begin();
        font.setColor(Color.BLACK);

        font.draw(spriteBatch, "A", 62, 80);
        font.draw(spriteBatch, "W", 138, 180);
        font.draw(spriteBatch, "D", 220, 80);

        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(textCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect(70, 100, 20, 6);
        shapeRenderer.triangle(60, 103, 70, 112, 70, 94);
        shapeRenderer.rect(223, 100, 20, 6);
        shapeRenderer.triangle(252, 103, 243, 112, 243, 94);
        shapeRenderer.rect(155, 194, 6, 20);
        shapeRenderer.triangle(158, 224, 149, 214, 167, 214);

        shapeRenderer.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    private void drawExitText() {
        spriteBatch.begin();
        if (player.won) {
            font.setColor(LD29.BLACK);
            font.draw(spriteBatch, "Thanks for playing", -500, 220);

            font.setColor(LD29.WHITE);
            font.draw(spriteBatch, "Press R to play again", -500, -30);
        } else {
            font.setColor(LD29.BLACK);
            font.draw(spriteBatch, "EXIT", -242, 220);
        }

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 64f, height / 64f);
        textCamera.setToOrtho(false, width, height);
    }
}
