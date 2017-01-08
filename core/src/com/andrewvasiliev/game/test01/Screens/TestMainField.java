package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Actors.GameField;
import com.andrewvasiliev.game.test01.Actors.Hud;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.Color.RED;

/**
 * Created by ava on 23.12.16.
 */

public class TestMainField implements Screen {
    public MyGdxGame locGame;
    //Texture img;
    //SpriteBatch batch;
//    OrthographicCamera camera;
//    Viewport view;
    private float locWidthMeter, locHeightMeter;
    //Sprite sprite;
    public ShapeRenderer shapeRenderer;
    Stage mainFieldStage;
    private BackgroundActor background;
    private GameField gamefield;
    private Hud hud;
    private Table infoTable;
    private Label plr1LabelName, plr2LabelName, plr1Score, plr2Score;

    public TestMainField(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        locWidthMeter = locGame.iWidthMeter;
        locHeightMeter = locGame.iHeightMeter;

        //img = new Texture(Gdx.files.internal("badlogic.jpg"), true);
        //img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //batch = new SpriteBatch();

//        camera = new OrthographicCamera(locWidthMeter, locHeightMeter);
//        view = new FitViewport(locWidthMeter, locHeightMeter, camera);


        //sprite = new Sprite(new Texture(Gdx.files.internal("quad.png")));
        //sprite.setBounds(0, 0, 4, 4);

        shapeRenderer = new ShapeRenderer();

        mainFieldStage = new Stage(locGame.view);

        background = new BackgroundActor(myGdxGame);
        background.setPosition(0, 0);

        float hudHeight = locHeightMeter/9f;
        hud = new Hud(this, 0, 0, locWidthMeter, hudHeight);
        hud.setPosition(0, 0);

        gamefield = new GameField(this, 0, hudHeight, locWidthMeter, locHeightMeter - hudHeight);
        gamefield.GenerateField(16*2+8*1, Const.CellShape.HEX); //лучше чтобы кол-во столбцов было кратно 8

        infoTable = new Table();
        infoTable.align(Align.bottom);
        infoTable.setWidth(locWidthMeter);

        infoTable.background(new Image(new Texture(Gdx.files.internal("quad.png"))).getDrawable());

        infoTable.setPosition(0, hudHeight/2);
        infoTable.setDebug(true);
        plr1LabelName = new Label("Игрок 1", locGame.skin, "default-font", Color.GREEN);
        plr1Score = new Label("1", locGame.skin, "default-font", Color.WHITE);
        plr1Score.setAlignment(Align.center);
        plr2LabelName = new Label("Android", locGame.skin, "default-font", Color.GREEN);
        plr2Score = new Label("100", locGame.skin, "default-font", Color.WHITE);
        plr2Score.setAlignment(Align.center);
        infoTable.add(plr1LabelName).expandX().left();
        infoTable.add(plr1Score).width(150).right();
        infoTable.add(plr2Score).width(150).left();
        infoTable.add(plr2LabelName).expandX().right();

        //mainFieldStage.addActor(background);
        mainFieldStage.addActor(gamefield);
        mainFieldStage.addActor(hud);
        mainFieldStage.addActor(infoTable);


        System.out.println("started");

    }

    public void GenerateField (int countColIn, Const.CellShape cellType) {
        gamefield.GenerateField(countColIn,cellType); //лучше чтобы кол-во столбцов было кратно 8
    }

    @Override
    public void dispose() {
        gamefield.dispose();
        hud.dispose();
        mainFieldStage.dispose();
        shapeRenderer.dispose();
        //batch.dispose();
        //img.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainFieldStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        locGame.camera.update();
        shapeRenderer.setProjectionMatrix(locGame.camera.combined);

        mainFieldStage.act(delta);
        mainFieldStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        locGame.view.update(width, height, true);
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

}
