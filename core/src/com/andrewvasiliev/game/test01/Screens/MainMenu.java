package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundField;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by ava on 06.01.17.
 */

public class MainMenu implements Screen {
    private MyGdxGame locGame;
    private Stage stage;
    //private Table table;
    private TextButton /*startButton, quitButton,*/ resumeGame;
    //public BackgroundActor backgroundActor;
    //private BackgroundField bf;
    private Label lblFps;
    private StringBuilder sb;

    public MainMenu(MyGdxGame myGdxGame) {
        sb = new StringBuilder();
        locGame = myGdxGame;

        stage = new Stage (locGame.view);

        Table table = new Table ();
        table.setFillParent(true);
        //table.setDebug(true);
        //table.setWidth(stage.getWidth());
        //table.align(Align.center|Align.top);
        //table.setPosition(0, stage.getHeight());

        resumeGame = new TextButton(locGame.StrRes.get("ResumeGame")/*"Продолжить игру"*/, locGame.skin, "menuStyle");
        String StartGameStr = locGame.StrRes.get("StartGame");
        TextButton startButton = new TextButton(StartGameStr/*"Начать игру"*/, locGame.skin, "menuStyle");
        String ExitGameStr = locGame.StrRes.get("ExitGame");
        TextButton quitButton = new TextButton(ExitGameStr/*"Выйти из игры"*/, locGame.skin, "menuStyle");

        table.padTop(30);
        table.add(resumeGame).padBottom(30);
        table.row();
        table.add(startButton).padBottom(30);
        table.row();
        table.add(quitButton);

        //final Dialog dialog = new Dialog("Click message", locGame.skin);

        resumeGame.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    locGame.gameScreen.ResumeGame();
                    locGame.setScreen(locGame.gameScreen);
                }
            }
        );
        startButton.addListener(new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                   locGame.setScreen(locGame.preStartMenu);
               }
           }
        );
        quitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.exit();
                }
            }
        );

        lblFps = new Label("FPS", locGame.skin, "default-font", Color.YELLOW);
        lblFps.setPosition(0, locGame.iHeightMeter - lblFps.getHeight());

        //backgroundActor = new BackgroundActor(locGame);
        //backgroundActor.setPosition(0, 0);
        BackgroundField bf = new BackgroundField(locGame, 0, 0, locGame.view.getScreenWidth(), locGame.view.getScreenHeight());
        bf.GenerateField(12, Const.CellShape.HEX, false);

        //stage.addActor(backgroundActor);
        stage.addActor(bf);
        stage.addActor(table); //кнопки меню
        stage.addActor(lblFps);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                //return super.keyDown(event, keycode);
                System.out.format("key down%n");
                if ((keycode == Input.Keys.BACK) || (keycode == Input.Keys.ESCAPE)) {
                    System.out.format("exit app%n");
                    Gdx.app.exit();
                }
                return false;
            }
        });
    }

    private boolean isGameSaved () {
        Preferences prefs = Gdx.app.getPreferences(Const.PreferencesName);
        //--признак 1-сохраненная игра 0-пусто (после восстановления игры ставить 0)
        return prefs.getBoolean("isGameSaved", false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        resumeGame.setVisible(isGameSaved());
        //table.setPosition(table.getX(), table.getY());
    }

    @Override
    public void render(float delta) {

        sb.setLength(0);
        lblFps.setText(sb.append("FPS:").append(Gdx.graphics.getFramesPerSecond()));

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
