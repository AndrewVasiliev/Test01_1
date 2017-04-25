package com.andrewvasiliev.game.test01.Screens;

import com.andrewvasiliev.game.test01.Actors.BackgroundActor;
import com.andrewvasiliev.game.test01.Actors.GameField;
import com.andrewvasiliev.game.test01.Actors.Hud;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.MyGdxGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by ava on 23.12.16.
 */

public class GameFieldScreen implements Screen {
    public MyGdxGame locGame;
    //private float locWidthMeter, locHeightMeter;
    public ShapeRenderer shapeRenderer;
    private Stage mainFieldStage;
    //private BackgroundActor background;
    private GameField gamefield;
    private Hud hud;
    //private Table infoTable;
    private Label plrLabelName[], plrScore[];
    public int currentPlayer; //индекс в массиве locGame.plr[] текущего игрока
    public boolean hudEnabled = false; //если истина, то от интерфейса ожидается нажатие (на цвет, например)
    private Dialog NextPlayerMoveDialog, WinningDialog;
    private Label lblWinningDialog, lblWinningName;
    //private TextButton RetryButton, ToMenuButton;
    private Label lblNextPlayerDialog;
    private Random random;
    //private float nextPlayerDelay = 1.0f;
    private int prevFieldSize;
    private Const.CellShape prevCellShape;

    private boolean isGameEnded;
    //private Label popUpScores;
    private Pool<Label> poolPopUpLabel;


    private Label lblFps;

    public GameFieldScreen(MyGdxGame myGdxGame) {
        locGame = myGdxGame;
        float locWidthMeter = locGame.iWidthMeter;
        float locHeightMeter = locGame.iHeightMeter;

        NextPlayerMoveDialog = new Dialog("", locGame.skin);
        lblNextPlayerDialog = new Label("", locGame.skin, "popupMessage");
        NextPlayerMoveDialog.text(lblNextPlayerDialog);

        WinningDialog= new Dialog("Результаты:", locGame.skin, "result_window"){
            protected void result(Object object) {
                this.hide();
                System.out.println("Chosen: " + object);
                if (object.toString().equals("true")) {
                    StartGame(prevFieldSize, prevCellShape);
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

        //shapeRenderer = new ShapeRenderer();
        shapeRenderer = locGame.sr;

        mainFieldStage = new Stage(locGame.view);

        BackgroundActor background = new BackgroundActor(myGdxGame);
        background.setPosition(0, 0);

        float hudHeight = locHeightMeter/9f;
        hud = new Hud(this, 0, 0, locWidthMeter, hudHeight);
        hud.setPosition(0, 0);

        gamefield = new GameField(this, 0, hudHeight, locWidthMeter, locHeightMeter - hudHeight);
        gamefield.SetSolidMode(true);
        //gamefield.GenerateField(16*2+8*1, Const.CellShape.RECTANGLE); //лучше чтобы кол-во столбцов было кратно 8

        Table infoTable = new Table();
        infoTable.align(Align.bottom);
        infoTable.setWidth(locWidthMeter);

        //infoTable.background(new Image(new Texture(Gdx.files.internal("quad.png"))).getDrawable());

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
        lblFps.setPosition(0, locHeightMeter - lblFps.getHeight());

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
                return new Label("", locGame.skin, "default-font", Color.YELLOW);
            }
        };



        System.out.println("started");
        random = new Random();

        mainFieldStage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Gamefield touchDown " + x + ", " + y);
                return super.touchDown(event, x, y, pointer, button);
                //return false;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                //return super.keyDown(event, keycode);
                System.out.format("key down%n");
                if ((keycode == Input.Keys.BACK) || (keycode == Input.Keys.ESCAPE)) {
                    System.out.format("set mainscreen%n");
                    //сохраняем текущее состояние
                    SaveGameState();

                    //вызодим в главное меню
                    locGame.setScreen(locGame.mainMenu);
                }
                return false;
            }
        });
    }
/*
    public void GenerateField (int countColIn, Const.CellShape cellType) {
        gamefield.GenerateField(countColIn,cellType); //лучше чтобы кол-во столбцов было кратно 8
    }
*/
    private void SaveGameState () {
        Json json = new Json();
        Preferences prefs = Gdx.app.getPreferences(Const.PreferencesName);
        //--признак 1-сохраненная игра 0-пусто (после восстановления игры ставить 0)
        prefs.putBoolean("isGameSaved", true);
        //--информация о игроках
        for (int i=0; i<locGame.maxPlr; i++) {
            prefs.putString("PlayerName"+Integer.toString(i), locGame.plr[i].name);
            prefs.putInteger("PlayerScore"+Integer.toString(i), locGame.plr[i].score);
            prefs.putInteger("PlayerColorIdx"+Integer.toString(i), locGame.plr[i].colorIdx);
            prefs.putBoolean("isAndroid"+Integer.toString(i), locGame.plr[i].isAndroid);
            prefs.putInteger("deepLevel"+Integer.toString(i), locGame.plr[i].deepLevel);
        }
        //prefs.putString("PlayersInfo", json.toJson(locGame.plr));
        //--чей сейчас ход
        prefs.putInteger("CurrentPlayer", currentPlayer);
        //--тип ячеек
        prefs.putString("CellShape", prevCellShape.toString());
        //-- количество столбцов
        prefs.putInteger("ColumnCount", prevFieldSize);
        //--игровое поле
        prefs.putString("GameField", json.toJson(gamefield.cells));
        //System.out.println(json.toJson(gamefield.cells));
        //сохраним
        prefs.flush();
    }

    public void ResumeGame () {
        Json json = new Json();
        Preferences prefs = Gdx.app.getPreferences(Const.PreferencesName);

        prevFieldSize = prefs.getInteger("ColumnCount");
        prevCellShape = Const.CellShape.valueOf(prefs.getString("CellShape"));
        //--информация о игроках
        for (int i=0; i<locGame.maxPlr; i++) {
            locGame.plr[i].name = prefs.getString("PlayerName"+Integer.toString(i));
            plrLabelName[i].setText(locGame.plr[i].name);
            locGame.plr[i].score = prefs.getInteger("PlayerScore"+Integer.toString(i));
            locGame.plr[i].colorIdx = prefs.getInteger("PlayerColorIdx"+Integer.toString(i));
            locGame.plr[i].isAndroid = prefs.getBoolean("isAndroid"+Integer.toString(i));
            locGame.plr[i].deepLevel = prefs.getInteger("deepLevel"+Integer.toString(i));
        }
        //--чей сейчас ход
        currentPlayer = prefs.getInteger("CurrentPlayer");
        //--игровое поле
        gamefield.cells = json.fromJson(MyCell[].class, prefs.getString("GameField"));
        //отметим, что игра загружена (чтоб 2 раза не продолжать одну и ту же игру)
        //prefs.putBoolean("isGameSaved", false);
        //prefs.flush();

        gamefield.GenerateField(prevFieldSize, prevCellShape, true);
        StartGameFinalStep();
    }

    public void StartGame(int fieldSize, Const.CellShape inCellShape) {
        prevFieldSize = fieldSize;
        prevCellShape = inCellShape;
        gamefield.GenerateField(fieldSize, inCellShape, false); //лучше чтобы кол-во столбцов было кратно 8
        //GenerateField(16*2+8*1, Const.CellShape.TRIANGLE);

        for (int i=0; i<locGame.maxPlr; i++) {
            locGame.plr[i].score = 1;
            plrLabelName[i].setText(locGame.plr[i].name);
            plrScore[i].setText(Integer.toString(locGame.plr[i].score));
        }

        //решаем какой игрок ходит первым
        currentPlayer = new Random().nextInt(locGame.maxPlr);

        StartGameFinalStep();
    }

    private void StartGameFinalStep () {
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
        }, /*nextPlayerDelay*/ 1.0f);
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
            if (scores[maxIndex] < scores[i]) {
                maxIndex = i;
            } else if (scores[maxIndex] == scores[i]) {
                //если одинаковое кол-во очков, то случайным образом решим какой индекс выбрать. для разнообразия
                maxIndex = random.nextBoolean() ? maxIndex : i;
            }
        }
        if (deepLevel == (locGame.plr[plrIdx].deepLevel-1)) {
            //System.out.format("maxindex=%d%n", maxIndex);
            return maxIndex;
        } else {
            return scores[maxIndex];
        }
    }

    private void PopUpScores(int idx, int deltaScore) {
        float x, y;

        //System.out.format("poolPopUpLabel.getFree()=%d%n", poolPopUpLabel.getFree());
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
        //RemoveActorAction removeActor = new RemoveActorAction();
        SequenceAction mySequence = new SequenceAction(showAction, moveAction, hideAction, /*removeActor,*/ run(new Runnable() {
            public void run () {
                //System.out.println("Action complete!");
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
