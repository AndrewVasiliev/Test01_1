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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.Random;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.abgr8888ToColor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

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
    private Dialog NextPlayerMoveDialog, WinningDialog;
    private Label lblWinningDialog, lblWinningName;
    private TextButton RetryButton, ToMenuButton;
    private Label lblNextPlayerDialog;
    private Random random;
    private float nextPlayerDelay = 1.0f;

    private boolean isGameEnded;
    //private Label popUpScores;
    Pool<Label> poolPopUpLabel;


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

        NextPlayerMoveDialog = new Dialog("", locGame.skin);
        lblNextPlayerDialog = new Label("", locGame.skin, "popupMessage");
        NextPlayerMoveDialog.text(lblNextPlayerDialog);

        WinningDialog= new Dialog("Результаты:", locGame.skin, "result_window"){
            protected void result(Object object) {
                this.hide();
                System.out.println("Chosen: " + object);
                if (object.toString().equals("true")) {
                    StartGame();
                } else {
                    locGame.setScreen(locGame.mainMenu);
                }
            }
        };
        WinningDialog.padTop(30).padBottom(30);
        lblWinningDialog = new Label("Победил", locGame.skin, "default");
        lblWinningName = new Label("тут имя", locGame.skin, "default");
        WinningDialog.getContentTable().add(lblWinningDialog).row();
        WinningDialog.getContentTable().add(lblWinningName).row();
        WinningDialog.getButtonTable().padTop(25);
        WinningDialog.button("Сыграть еще", true).row();
        WinningDialog.button("Вернуться в меню", false).row();

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

        //всплывающее кол-во очков на очередном ходе
        //popUpScores = new Label("xxx", locGame.skin, "menu-font", Color.YELLOW);
        //popUpScores.setVisible(false);
        //mainFieldStage.addActor(popUpScores);
        poolPopUpLabel = new Pool<Label>() {
            protected Label newObject () {
                return new Label("", locGame.skin, "menu-font", Color.YELLOW);
            }
        };



        System.out.println("started");
        random = new Random();
    }
/*
    public void GenerateField (int countColIn, Const.CellShape cellType) {
        gamefield.GenerateField(countColIn,cellType); //лучше чтобы кол-во столбцов было кратно 8
    }
*/
    public void StartGame() {
        gamefield.GenerateField(24, Const.CellShape.HEX); //лучше чтобы кол-во столбцов было кратно 8
        //GenerateField(16*2+8*1, Const.CellShape.TRIANGLE);

        for (int i=0; i<locGame.maxPlr; i++) {
            locGame.plr[i].score = 1;
            plrLabelName[i].setText(locGame.plr[i].name);
            plrScore[i].setText(Integer.toString(locGame.plr[i].score));
        }

        //решаем какой игрок ходит первым
        currentPlayer = new Random().nextInt(locGame.maxPlr);
        ColoringPlayers ();
        //разрешаем нажимать кнопки
        hud.colorIdx = -1;
        hudEnabled = false;
        PrintNextPlayerName();
        isGameEnded = false;
        if (!locGame.plr[currentPlayer].isAndroid) {
            //ход игрока
            hudEnabled = true;
        } else {
            // ход делает андроид
            RunAiThread();
        }
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

    private void PrintNextPlayerName() {
        lblNextPlayerDialog.setText(" Ходит " + locGame.plr[currentPlayer].name+" ");
        NextPlayerMoveDialog.show(mainFieldStage);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                NextPlayerMoveDialog.hide();
                Timer.instance().clear();
            }
        }, nextPlayerDelay);
    }

    private void RunAiThread() {
        //запускаем в потоке расчет следующего хода AI
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int AndroidAiResult = AndroidAI(currentPlayer, locGame.plr[currentPlayer].deepLevel, gamefield.cells);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        hud.colorIdx = AndroidAiResult;
                        hudEnabled = true;
                    }
                });
            }
        }).start();
    }

    private int AndroidAI (int plrIdx, int deepLevel, MyCell[] inCells) {
        int[] scores = new int[Const.ColorCount];
        int i, prevScore;
        MyCell[] locCells;

        deepLevel--;
        //инициализируем массив нулями
        for (i = 0; i < scores.length; i++) {
            scores[i] = 0;
        }
        locCells = new MyCell[inCells.length];
        for (int j = 0; j < locCells.length; j++) {
            locCells[j] = new MyCell();
        }
        //для каждого цвета сделаем ход и сохраним полученные очки
        boolean skipColor;
        for (i = 0; i < scores.length; i++) {

            if (deepLevel == (locGame.plr[plrIdx].deepLevel-1)) {
                //пропускаем цвет если он совпадает с текущим цветом, кого-то из игроков
                skipColor = false;
                for (int j = 0; j < locGame.maxPlr; j++) {
                    if (locGame.plr[j].colorIdx == i) {
                        skipColor = true;
                        break;
                    }
                }
                if (skipColor) {
                    continue;
                }
            }

            for (int j = 0; j < locCells.length; j++) {
                locCells[j].owner = inCells[j].owner;
                locCells[j].colorIdx = inCells[j].colorIdx;
                locCells[j].nearby = inCells[j].nearby.clone();
            }
            //кол-во очков до хода очередным цветом
            prevScore = gamefield.CountScore(plrIdx, locCells);
            //ход очередным цветом
            gamefield.FillColor(i, plrIdx, locCells);
            //кол-во очков после хода очередным цветом
            scores[i] = gamefield.CountScore(plrIdx, locCells);
            //если кол-во очков увеличилось и еще не достигнуто дно рекурсии (сложность/глубина AI), то вызываем рекурсию
            if ((prevScore < scores[i]) && (deepLevel > 0)) {
                scores[i] += AndroidAI(plrIdx, deepLevel, locCells);
            }
        }

        int maxIndex = 0;
        for (i = 0; i < scores.length; i++) {
            if (deepLevel == (locGame.plr[plrIdx].deepLevel-1)) {
                System.out.format("scores[%d]=%d%n", i, scores[i]);
            }
            if (scores[maxIndex] < scores[i]) {
                maxIndex = i;
            } else if (scores[maxIndex] == scores[i]) {
                //если одинаковое кол-во очков, то случайным образом решим какой индекс выбрать. для разнообразия
                maxIndex = random.nextBoolean() ? maxIndex : i;
            }
        }
        if (deepLevel == (locGame.plr[plrIdx].deepLevel-1)) {
            System.out.format("maxindex=%d%n", maxIndex);
            return maxIndex;
        } else {
            return scores[maxIndex];
        }
    }

    private void PopUpScores(int idx, int deltaScore) {
        float x, y;

        //Label popUpScores = new Label("xxx", locGame.skin, "menu-font", Color.YELLOW);
        System.out.format("poolPopUpLabel.getFree()=%d%n", poolPopUpLabel.getFree());
        final Label popUpScores = poolPopUpLabel.obtain();

        popUpScores.setVisible(false);
        mainFieldStage.addActor(popUpScores);

        x = hud.colorButton[idx].x + hud.diametrH/2.0f;
        y = hud.colorButton[idx].y;
        popUpScores.setText("+" + Integer.toString(deltaScore));
        popUpScores.setPosition(x, y);

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(x, y+150);
        moveAction.setDuration(2.0f);
        VisibleAction showAction = new VisibleAction();
        showAction.setVisible(true);
        VisibleAction hideAction = new VisibleAction();
        hideAction.setVisible(false);
        RemoveActorAction removeActor = new RemoveActorAction();
        SequenceAction mySequence = new SequenceAction(showAction, moveAction, hideAction, /*removeActor,*/ run(new Runnable() {
            public void run () {
                System.out.println("Action complete!");
                poolPopUpLabel.free(popUpScores);
            }
            }));
        popUpScores.addAction(mySequence);
    }

    @Override
    public void render(float delta) {
        if (!isGameEnded) {
            if (hudEnabled) {
                if (hud.colorIdx != -1) { //нажата цветовая кнопка
                    hudEnabled = false;
                    //тут будет заработанное кол-во очков на этом ходе
                    int deltaScore = gamefield.CountScore(currentPlayer, gamefield.cells);
                    //заливаем цветом для текущего игрока
                    gamefield.PlayerMove(hud.colorIdx);
                    deltaScore = gamefield.CountScore(currentPlayer, gamefield.cells) - deltaScore;
                    //всплывающее кол-во очков над нажатой кнопкой
                    PopUpScores(hud.colorIdx, deltaScore);
                    //меняем вол-во очков
                    plrScore[0].setText(Integer.toString(locGame.plr[0].score));
                    plrScore[1].setText(Integer.toString(locGame.plr[1].score));
                    //передаем ход следующему игроку
                    int prevIdx = currentPlayer; //если следующему игроку уже некуда будет ходить, то текущий игрок атоматически забирает остальное пространство, чтобы не тянуть время
                    currentPlayer += 1;
                    if (currentPlayer == locGame.maxPlr) {
                        currentPlayer = 0;
                    }
                    //отобразим цветом кто ходит следующим
                    ColoringPlayers();
                    //проверим есть ли еще возможность для хода у игрока
                    //если нет, то заканчиваем игру и пишем кто выиграл
                    if (!gamefield.isPossibleMoves(currentPlayer)) {
                        //возможных ходов больше нет.
                        isGameEnded = true;
                        //расширим владения соперника до конца
                        for (int j = 0; j < gamefield.cells.length; j++) {
                            if (gamefield.cells[j].owner == gamefield.NOBODYCELL) {
                                gamefield.cells[j].owner = prevIdx;
                                gamefield.cells[j].phaseIdx = 0;
                                gamefield.cells[j].colorIdxNext = locGame.plr[prevIdx].colorIdx;
                            }
                        }
                        locGame.plr[0].score = gamefield.CountScore(0, gamefield.cells);
                        locGame.plr[1].score = gamefield.CountScore(1, gamefield.cells);
                        plrScore[0].setText(Integer.toString(locGame.plr[0].score));
                        plrScore[1].setText(Integer.toString(locGame.plr[1].score));
                        //выведем сообщение о победителе и спросим "продолжать или выйти в меню"
                        if (locGame.plr[0].score == locGame.plr[1].score) {
                            lblWinningDialog.setText("Победила");
                            lblWinningName.setText("ДРУЖБА !!!");
                        } else {
                            lblWinningDialog.setText("Победил(а)");
                            if (locGame.plr[0].score > locGame.plr[1].score) {
                                lblWinningName.setText(locGame.plr[0].name);
                            } else {
                                lblWinningName.setText(locGame.plr[1].name);
                            }
                        }
                        WinningDialog.show(mainFieldStage);
                    } else {
                        //выводим на экран сообщение о том кто следующий ходит
                        PrintNextPlayerName();
                    }

                    //включаем ожидание нажатия цветовых кнопок
                    hud.colorIdx = -1;
                    if (!locGame.plr[currentPlayer].isAndroid) {
                        hudEnabled = true;
                    } else {
                        // ход делает андроид
                        RunAiThread();
                    }
                }
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
