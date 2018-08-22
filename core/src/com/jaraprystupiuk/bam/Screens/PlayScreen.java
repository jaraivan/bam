package com.jaraprystupiuk.bam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jaraprystupiuk.bam.Bam;
import com.jaraprystupiuk.bam.Scenes.Hud;
import com.jaraprystupiuk.bam.Sprites.Fan;
import com.jaraprystupiuk.bam.Tools.B2WorldCreator;

public class PlayScreen implements Screen {

    private Bam game;
    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    Fan player;

    public PlayScreen(Bam game) {

        atlas = new TextureAtlas("C:/Users/Chocofortys/IdeaProjects/Bam/core/assets/FanAndEnemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Bam.V_WIDTH / Bam.PPM, Bam.V_HEIGHT / Bam.PPM, gameCam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("C:/Users/Chocofortys/IdeaProjects/Bam/core/assets/com.jaraprystupiuk.bam/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Bam.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, 0), true);


        b2dr = new Box2DDebugRenderer();
        b2dr.SHAPE_STATIC.set(1, 0, 0, 1);

        new B2WorldCreator(world, map);

        // crea el fan en tu mundo
        player = new Fan(world, this);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        //Esto lo frena cuando no esta apretando una tecla de movimiento
        player.b2body.setLinearVelocity(new Vector2(0, 0));

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //player.b2body.applyLinearImpulse(new Vector2(0, 0.05f), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(0, 1.2f));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //player.b2body.applyLinearImpulse(new Vector2(0, -0.1f), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(0, -1.2f));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            //player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(1.2f, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            //player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(-1.2f, 0));
        }


    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        player.update(dt);

        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.position.y = player.b2body.getPosition().y;

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
