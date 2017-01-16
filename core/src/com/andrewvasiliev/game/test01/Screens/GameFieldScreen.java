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
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.abgr8888ToColor;

/**
 * Created by ava on 23.12.16.
 */

public class GameFieldScreen implements Screen {
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
    //private Label plr1LabelName, plr2LabelName, plr1Score, plr2Score;
    private Label plrLabelName[], plrScore[];
    public int currentPlayer; //индекс в массиве locGame.plr[] текущего игрока
    public boolean hudEnabled = false; //если истина, то от интерфейса ожидается нажатие (на цвет, например)

    private Label lblFps;

    public GameFieldScreen(MyGdxGame myGdxGame) {
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
        //gamefield.GenerateField(16*2+8*1, Const.CellShape.RECTANGLE); //лучше чтобы кол-во столбцов было кратно 8

        infoTable = new Table();
        infoTable.align(Align.bottom);
        infoTable.setWidth(locWidthMeter);

        infoTable.background(new Image(new Texture(Gdx.files.internal("quad.png"))).getDrawable());

        infoTable.setPosition(0, hudHeight/2);
        //infoTable.setDebug(true);
        plrLabelName = new Label[locGame.maxPlr];
        plrScore = new Label[locGame.maxPlr];
        plrLabelName[0] = new Label("", locGame.skin, "default-font", Color.WHITE);
        plrScore[0] = new Label("", locGame.skin, "default-font", Color.WHITE);
        plrScore[0].setAlignment(Align.center);

        plrLabelName[1] = new Label("", locGame.skin, "default-font", Color.WHITE);
        plrLabelName[1].setAlignment(Align.right);
        plrScore[1] = new Label("", locGame.skin, "default-font", Color.WHITE);
        plrScore[1].setAlignment(Align.center);


        float _scoreWidth = 150;
        infoTable.add(plrLabelName[0]).width(locWidthMeter/2f-_scoreWidth);
        infoTable.add(plrScore[0]).width(_scoreWidth);
        infoTable.add(plrScore[1]).width(_scoreWidth);
        infoTable.add(plrLabelName[1]).width(locWidthMeter/2f-_scoreWidth);

        lblFps = new Label("FPS", locGame.skin, "default-font", Color.YELLOW);
        lblFps.setPosition(0, 0);



        //mainFieldStage.addActor(background);
        mainFieldStage.addActor(gamefield);
        mainFieldStage.addActor(hud);
        mainFieldStage.addActor(infoTable);

        mainFieldStage.addActor(lblFps);


        System.out.println("started");

    }

    public void GenerateField (int countColIn, Const.CellShape cellType) {
        gamefield.GenerateField(countColIn,cellType); //лучше чтобы кол-во столбцов было кратно 8
    }

    public void StartGame() {
        gamefield.GenerateField(16*2+8*1, Const.CellShape.HEX); //лучше чтобы кол-во столбцов было кратно 8

        for (int i=0; i<locGame.maxPlr; i++) {
            plrLabelName[i].setText(locGame.plr[i].name);
            plrScore[i].setText(Integer.toString(locGame.plr[i].score));
        }

        //решаем какой игрок ходит первым
        currentPlayer = new Random().nextInt(locGame.maxPlr);
        ColoringPlayers ();
        //разрешаем нажимать унопки
        hud.colorIdx = -1;
        hudEnabled = true;
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

    private void SetCurrentPlayerColor (Color colorIn) {
        plrLabelName[currentPlayer].setColor(colorIn);
        plrScore[currentPlayer].setColor(colorIn);
    }

    private void ColoringPlayers () {
        for (int i=0; i<locGame.maxPlr; i++) {
            if (i == currentPlayer) {
                plrLabelName[i].setColor(Color.WHITE);
                plrScore[i].setColor(Color.WHITE);
            } else {
                plrLabelName[i].setColor(Color.BLACK);
                plrScore[i].setColor(Color.BLACK);
            }
        }

    }

    @Override
    public void render(float delta) {
        if (hudEnabled) {
            if (hud.colorIdx != -1) { //нажата цветовая кнопка
                hudEnabled = false;
                //заливаем цветом для текущего игрока
                gamefield.PlayerMove(Const.colorArr[hud.colorIdx]);
                //меняем вол-во очков
                plrScore[0].setText(Integer.toString(locGame.plr[0].score));
                plrScore[1].setText(Integer.toString(locGame.plr[1].score));
                //передаем ход следующему игроку
                currentPlayer += 1;
                if (currentPlayer == locGame.maxPlr){currentPlayer = 0;}
                //отобразим цветом кто ходит следующим
                ColoringPlayers();
                //включаем ожидание нажатия цветовых кнопок
                hud.colorIdx = -1;
                hudEnabled = true;
                // !!! а что делать если следующий игрок - компьютер ????

            }
        }
        lblFps.setText("FPS:"+Integer.toString (Gdx.graphics.getFramesPerSecond()));

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
