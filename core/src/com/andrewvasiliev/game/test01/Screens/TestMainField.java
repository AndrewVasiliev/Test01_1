package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Actors.GameField;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by ava on 23.12.16.
 */

public class TestMainField implements Screen {
    private MyGdxGame locGame;
    Texture img;
    SpriteBatch batch;
    OrthographicCamera camera;
    private float locWidthMeter, locHeightMeter;
    Sprite sprite;
    public ShapeRenderer shapeRenderer;
    Stage mainFieldStage;
    Viewport view;
    private BackgroundActor background;
    private GameField gamefield;

    public TestMainField(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        locWidthMeter = locGame.iWidthMeter;
        locHeightMeter = locGame.iHeightMeter;

        img = new Texture(Gdx.files.internal("badlogic.jpg"), true);
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        batch = new SpriteBatch();

        camera = new OrthographicCamera(locWidthMeter, locHeightMeter);
        view = new FitViewport(locWidthMeter, locHeightMeter, camera);
        //camera.position.set(new Vector3(/*locWidthMeter/2.0f, locHeightMeter/2.0f*/ 0,0, 0));


        sprite = new Sprite(new Texture(Gdx.files.internal("quad.png")));
        sprite.setBounds(0, 0, 4, 4);

        shapeRenderer = new ShapeRenderer();

        mainFieldStage = new Stage(view);

        background = new BackgroundActor(myGdxGame);
        background.setPosition(0, 0);

        gamefield = new GameField(this, 0, locHeightMeter/9f, locWidthMeter, locHeightMeter - locHeightMeter/9f, 16*5, 8*5);

        //mainFieldStage.addActor(background);
        mainFieldStage.addActor(gamefield);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);


        mainFieldStage.act(delta);
        mainFieldStage.draw();


/*
        batch.begin();

        batch.draw(img, 0, 0, locWidthMeter/2, locHeightMeter/2);
        batch.draw(img, locWidthMeter/2, locHeightMeter/2, locWidthMeter/2, locHeightMeter/2);
        sprite.draw(batch);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Заливаем задний фон
        shapeRenderer.setColor(0 / 255.0f, 255 / 255.0f, 255 / 255.0f, 1);
        //shapeRenderer.rect(0, 0, 2, 2);
        //shapeRenderer.polygon (new float[]{0f,0f, 1f,2f, 1.5f,1.0f, 0.7f,0.3f});
        shapeRenderer.triangle(0f,0f, 1f,2f, 1.5f,1.0f);
        shapeRenderer.point(2.5f, 2.5f, 0f);
        shapeRenderer.point(-2.5f, -2.5f, 0f);

        // Рисуем Grass
        //shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
        //shapeRenderer.rect(0,  66, 136, 11);

        // Рисуем Dirt
        //shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
        //shapeRenderer.rect(0, 77, 136, 52);

        shapeRenderer.end();
*/

    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height, true);
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

    }
}
