package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

/**
 * Created by AvA on 09.02.2017.
 */

public class PreStartMenu implements Screen {
    private MyGdxGame locGame;
    private Stage stage;
    private Table table;
    private TextButton btnPvP, btnPvAI;
    public BackgroundActor backgroundActor;
    private ButtonGroup btnGroupGameType;
    private TextField edPlayer1, edPlayer2;
    private Label lblPlayer1, lblPlayer2;

    public PreStartMenu(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        stage = new Stage (locGame.view);

        table = new Table ();
        table.setFillParent(true);
        table. pad(30);
        //table.debugAll();

        btnPvP = new CheckBox("Игрок против Игрока", locGame.skin, "default");
        btnPvAI = new CheckBox("Игрок против Андроида", locGame.skin, "default");
        btnGroupGameType = new ButtonGroup(btnPvP, btnPvAI);
        btnGroupGameType.setMaxCheckCount(1);
        btnGroupGameType.setMinCheckCount(1);
        btnGroupGameType.setUncheckLast(true);

        lblPlayer1 = new Label("Имя игрока 1", locGame.skin, "default");
        edPlayer1 = new TextField("Игрок 1", locGame.skin, "default" );
        edPlayer1.setMaxLength(15);
        lblPlayer2 = new Label("Имя игрока 2", locGame.skin, "default");
        edPlayer2 = new TextField("Игрок 2", locGame.skin, "default" );
        edPlayer2.setMaxLength(15);

        float cnPad = 5;
        table.add(btnPvP).pad(cnPad);
        table.add(btnPvAI).pad(cnPad);
        table.row();
        table.add(lblPlayer1).align(Align.right).pad(cnPad);
        table.add(edPlayer1).align(Align.left).pad(cnPad);
        table.row();
        table.add(lblPlayer2).align(Align.right).pad(cnPad);
        table.add(edPlayer2).align(Align.left).pad(cnPad);


        backgroundActor = new BackgroundActor(locGame);
        backgroundActor.setPosition(0, 0);

        //stage.addActor(backgroundActor);
        stage.addActor(table);
    }

    @Override
    public void show() {
            Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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

    @Override
    public void dispose() {

    }
}
