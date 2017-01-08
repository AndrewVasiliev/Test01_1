package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by ava on 06.01.17.
 */

public class MainMenu implements Screen {
    private MyGdxGame locGame;
    private Stage stage;
    private Table table;
    private TextButton startButton, quitButton;
    public BackgroundActor backgroundActor;
    private Label testLabel;


    public MainMenu(MyGdxGame myGdxGame) {
        locGame = myGdxGame;

        stage = new Stage (locGame.view);

        table = new Table ();
        table.setFillParent(true);
        //table.setDebug(true);
        //table.setWidth(stage.getWidth());
        //table.align(Align.center|Align.top);
        //table.setPosition(0, stage.getHeight());

        startButton = new TextButton("Начать игру", locGame.skin, "menuStyle");
        quitButton = new TextButton("Выйти из игры", locGame.skin, "menuStyle");

        table.padTop(30);
        table.add(startButton).padBottom(30);
        table.row();
        table.add(quitButton);

        testLabel = new Label("Игрок 1", locGame.skin, "default-font", Color.WHITE);
        testLabel.setPosition(100,100);

        final Dialog dialog = new Dialog("Click message", locGame.skin);

        startButton.addListener(new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                   locGame.plr[0].SetPlayer("Влад", 1, false, 0);
                   locGame.plr[1].SetPlayer("Android", 1, true, 1);

                   locGame.gameScreen.GenerateField(16*2+8*1, Const.CellShape.TRIANGLE);
                   locGame.setScreen(locGame.gameScreen);
               }
           }
        );
        quitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                       @Override
                       public void run() {
                           dialog.hide();
                       }
                    }, 0.5f);
                }
            }
        );

        backgroundActor = new BackgroundActor(locGame);
        backgroundActor.setPosition(0, 0);

        stage.addActor(backgroundActor);
        stage.addActor(table);
        stage.addActor(testLabel);
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
