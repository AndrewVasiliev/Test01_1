package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Classes.AniButton;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by AvA on 09.02.2017.
 */

public class PreStartMenu implements Screen {
    private MyGdxGame locGame;
    private Stage stage;
    private Table table, tblGameType, tblDifficulty, tblFieldType, tblFieldSize;
    public BackgroundActor backgroundActor;
    private ButtonGroup btnGroupGameType;
    private TextField edPlayer1, edPlayer2;
    private Label lblPlayer1, lblPlayer2;
    private Label lblDifficulty, lblFieldType, lblFieldSize;
    private ButtonGroup btnGroupDifficulty, btnGroupFielType, btnGroupFielSize;
    private Button btnEasy, btnMid, btnHard;
    private Label lblGameType;
    //private TextButton btnPvP, btnPvAI;
    private Button btnPvP, btnPvAI, btnSmall, btnMiddle, btnLarge, btnXLarge;
    private TextButton btnStartGame, btnCancel;
    private AniButton btnRectangle, btnTriangle, btnRhombus, btnHex;


    public PreStartMenu(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        stage = new Stage (locGame.view);

        table = new Table ();
        table.setFillParent(true);
        //table.pad(30);
        table.debugAll();
        float cnPad = 5;

        lblGameType = new Label("Игра против", locGame.skin, "default");
        btnPvP = new Button(locGame.skin, "toggleButton");
        btnPvP.add(new Label("Игрока", locGame.skin, "default"));
        btnPvAI = new Button(locGame.skin, "toggleButton");
        btnPvAI.add(new Label("Андроида", locGame.skin, "default"));
        tblGameType = new Table();
        tblGameType.add(btnPvP).pad(cnPad);
        tblGameType.add(btnPvAI).pad(cnPad);

        btnGroupGameType = new ButtonGroup(btnPvP, btnPvAI);
        btnGroupGameType.setMaxCheckCount(1);
        btnGroupGameType.setMinCheckCount(1);
        btnGroupGameType.setUncheckLast(true);

        lblPlayer1 = new Label("Имя игрока 1", locGame.skin, "default");
        edPlayer1 = new TextField("Игрок 1", locGame.skin, "default" );
        edPlayer1.setMaxLength(15);
        edPlayer1.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {
                //Gdx.input.setOnscreenKeyboardVisible(true);
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        edPlayer1.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, lblPlayer1.getText().toString(), edPlayer1.getText(), "");
            }
        });

        lblPlayer2 = new Label("Имя игрока 2", locGame.skin, "default");
        edPlayer2 = new TextField("Игрок 2", locGame.skin, "default" );
        edPlayer2.setMaxLength(15);
        edPlayer2.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {
                //Gdx.input.setOnscreenKeyboardVisible(true);
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        edPlayer2.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, lblPlayer2.getText().toString(), edPlayer2.getText(), "");
            }
        });

        lblDifficulty = new Label("Сложность Андроида", locGame.skin, "default");
        btnEasy = new Button(locGame.skin, "toggleButton");
        btnEasy.add(new Label("Легкий", locGame.skin, "default"));
        btnMid = new Button(locGame.skin, "toggleButton");
        btnMid.add(new Label("Средний", locGame.skin, "default"));
        btnHard= new Button(locGame.skin, "toggleButton");
        btnHard.add(new Label("Тяжелый", locGame.skin, "default"));
        btnGroupDifficulty = new ButtonGroup(btnEasy, btnMid, btnHard);
        btnGroupDifficulty.setMaxCheckCount(1);
        btnGroupDifficulty.setMinCheckCount(1);
        btnGroupDifficulty.setUncheckLast(true);
        tblDifficulty = new Table();
        tblDifficulty.add(btnEasy).align(Align.left).pad(cnPad);
        tblDifficulty.add(btnMid).align(Align.left).pad(cnPad);
        tblDifficulty.add(btnHard).align(Align.left).pad(cnPad);

        //-- тип ячеек
        lblFieldType = new Label("Тип ячеек", locGame.skin, "default");
        btnRectangle = new AniButton(locGame.skin, "toggleButton", Const.CellShape.RECTANGLE);
        btnTriangle = new AniButton(locGame.skin, "toggleButton", Const.CellShape.TRIANGLE);
        btnRhombus = new AniButton(locGame.skin, "toggleButton", Const.CellShape.RHOMBUS);
        btnHex = new AniButton(locGame.skin, "toggleButton", Const.CellShape.HEX);
        btnGroupFielType = new ButtonGroup(btnRectangle, btnTriangle, btnRhombus, btnHex);
        btnGroupFielType.setMaxCheckCount(1);
        btnGroupFielType.setMinCheckCount(1);
        btnGroupFielType.setUncheckLast(true);

        tblFieldType = new Table();
        tblFieldType.add(btnRectangle).align(Align.left).pad(cnPad);
        tblFieldType.add(btnTriangle).align(Align.left).pad(cnPad);
        tblFieldType.add(btnRhombus).align(Align.left).pad(cnPad);
        tblFieldType.add(btnHex).align(Align.left).pad(cnPad);

        //-- размер поля
        lblFieldSize = new Label("Размер поля", locGame.skin, "default");
        btnSmall = new Button(locGame.skin, "toggleButton");
        btnSmall.add(new Label("S", locGame.skin, "default"));
        btnMiddle = new Button(locGame.skin, "toggleButton");
        btnMiddle.add(new Label("M", locGame.skin, "default"));
        btnLarge = new Button(locGame.skin, "toggleButton");
        btnLarge.add(new Label("L", locGame.skin, "default"));
        btnXLarge = new Button(locGame.skin, "toggleButton");
        btnXLarge.add(new Label("XL", locGame.skin, "default"));

        btnGroupFielSize = new ButtonGroup(btnSmall, btnMiddle, btnLarge, btnXLarge);
        btnGroupFielSize.setMaxCheckCount(1);
        btnGroupFielSize.setMinCheckCount(1);
        btnGroupFielSize.setUncheckLast(true);

        tblFieldSize = new Table();
        tblFieldSize.add(btnSmall).align(Align.left).pad(cnPad);
        tblFieldSize.add(btnMiddle).align(Align.left).pad(cnPad);
        tblFieldSize.add(btnLarge).align(Align.left).pad(cnPad);
        tblFieldSize.add(btnXLarge).align(Align.left).pad(cnPad);

        btnStartGame = new TextButton("Начать игру", locGame.skin, "default");
        btnCancel = new TextButton("Назад", locGame.skin, "default");

        btnCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                locGame.setScreen(locGame.mainMenu);
                }
            }
        );

        btnStartGame.addListener(new ClickListener() {
              @Override
              public void clicked(InputEvent event, float x, float y) {
              locGame.plr[0].SetPlayer(edPlayer1.getText(), 1, false, 0);
              if (btnPvP.isChecked()) {
                  //игра против игрока
                  locGame.plr[1].SetPlayer(edPlayer2.getText(), 1, false, 0);
              } else {
                  //игра против андроида
                  locGame.plr[1].SetPlayer("Android-"+String.valueOf(btnGroupDifficulty.getCheckedIndex()+1), 1, true, btnGroupDifficulty.getCheckedIndex()+1);
              }

              Const.CellShape startCellShape = Const.CellShape.RECTANGLE;
              switch (btnGroupFielType.getCheckedIndex()) {
                  case 0:   startCellShape = Const.CellShape.RECTANGLE; break;
                  case 1:   startCellShape = Const.CellShape.TRIANGLE; break;
                  case 2:   startCellShape = Const.CellShape.RHOMBUS; break;
                  case 3:   startCellShape = Const.CellShape.HEX; break;
              }

              int startFieldSize = 8;
              switch (btnGroupFielSize.getCheckedIndex()) {
                  case 0:   startFieldSize = 8; break;
                  case 1:   startFieldSize = 16; break;
                  case 2:   startFieldSize = 24; break;
                  case 3:   startFieldSize = 32; break;
              }

              locGame.gameScreen.StartGame(startFieldSize, startCellShape);
              locGame.setScreen(locGame.gameScreen);
              }
          }
        );





        //тип игры: PvP или PvAI
        table.add(lblGameType).align(Align.right).pad(cnPad);
        table.add(tblGameType).align(Align.left);
        table.row();
        //имя игрока 1
        table.add(lblPlayer1).align(Align.right).pad(cnPad);
        table.add(edPlayer1).align(Align.left).pad(cnPad);
        table.row();
        //имя игрока 2
        table.add(lblPlayer2).align(Align.right).pad(cnPad);
        table.add(edPlayer2).align(Align.left).pad(cnPad);
        table.row();
        //сложность Андроида
        table.add(lblDifficulty).align(Align.right).pad(cnPad);
        table.add(tblDifficulty).align(Align.left);
        table.row();
        //тип ячеек
        //table.row().minHeight(btnPvP.getHeight());
        table.add(lblFieldType).align(Align.right).pad(cnPad);
        table.add(tblFieldType);
        table.row();
        //размер поля
        table.add(lblFieldSize).align(Align.right).pad(cnPad);
        table.add(tblFieldSize);
        table.row();


        float maxWidth = (btnCancel.getWidth() > btnStartGame.getWidth()) ? btnCancel.getWidth() : btnStartGame.getWidth();
        //кнопка Назад
        table.add(btnCancel).width(maxWidth).align(Align.left).padTop(20);
        //кнопка начать игру
        table.add(btnStartGame).width(maxWidth).align(Align.right).padTop(20);


        btnPvP.setChecked(true);
        btnEasy.setChecked(true);
        btnRectangle.setChecked(true);
        btnSmall.setChecked(true);

        backgroundActor = new BackgroundActor(locGame);
        backgroundActor.setPosition(0, 0);

        stage.addActor(backgroundActor);
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
