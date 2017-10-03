package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by AvA on 02.10.2017.
 */

public class OptionsScreen implements Screen {
    private MyGdxGame locGame;
    private Stage stage;
    final String[] langCaption = {"Русский", "English", "Deutsch", "Español"};
    final String[] langCode = {"ru", "en", "de", "es"};

    private BackgroundActor backgroundActor;

    public OptionsScreen(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        stage = new Stage (locGame.view);

        Table table = new Table ();
        table.setFillParent(true);
        float cnPad = 5;

        Label lblLanguage = new Label(locGame.StrRes.get("lblLanguage"), locGame.skin, "default");
        final SelectBox sbLanguage = new SelectBox(locGame.skin, "default");
        sbLanguage.setItems(langCaption);
        String locale = locGame.StrRes.getLocale().getLanguage();

        for (int i = 0; i < langCode.length; i++) {
            if (langCode[i].equals(locale)) {
                sbLanguage.setSelectedIndex(i);
                break;
            }
        }

        sbLanguage.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                String selectedLanguage = sbLanguage.getSelected().toString();
                //System.out.println("selected language " + selectedLanguage);
                //System.out.println("langCaption.length " + langCaption.length);
                for (int i = 0; i < langCaption.length; i++) {
                    if (langCaption[i].equals(selectedLanguage)) {
                        //System.out.println("saving langcode " + langCode[i]);
                        locGame.Settings.putString("Language", langCode[i]);
                        locGame.Settings.flush();;
                        break;
                    }
                }
            }
        });

        TextButton btnBack = new TextButton(locGame.StrRes.get("Back"), locGame.skin, "default");
        btnBack.addListener(new ClickListener() {
                                  @Override
                                  public void clicked(InputEvent event, float x, float y) {
                                      locGame.setScreen(locGame.mainMenu);
                                  }
                              }
        );


        table.add(lblLanguage).align(Align.right).pad(cnPad);
        table.add(sbLanguage).align(Align.left);
        table.row();
        //кнопка Назад
        table.add(btnBack).width(btnBack.getWidth()).align(Align.left).padTop(20);

        backgroundActor = new BackgroundActor(locGame);
        backgroundActor.setPosition(0, 0);

        stage.addActor(backgroundActor);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                //return super.keyDown(event, keycode);
                //System.out.format("key down%n");
                if ((keycode == Input.Keys.BACK) || (keycode == Input.Keys.ESCAPE)) {
                    //System.out.format("set mainscreen%n");
                    locGame.setScreen(locGame.mainMenu);
                }
                return false;
            }
        });
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
