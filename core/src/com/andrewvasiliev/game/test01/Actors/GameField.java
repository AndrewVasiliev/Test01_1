package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.BaseCell;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.andrewvasiliev.game.test01.Screens.GameFieldScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Queue;

import java.util.Random;


/**
 * Created by ava on 23.12.16.
 */

public class GameField extends BaseField {
    private GameFieldScreen locScreen;

    public GameField(GameFieldScreen gameFieldScreen, float x, float y, float width, float height) {
        super (gameFieldScreen.shapeRenderer, x, y, width, height, true);
        locScreen = gameFieldScreen;
        SetAnimationSpeed(0.5f);
    }

    @Override
    public void GenerateField (int countColIn, Const.CellShape cellShape, boolean isResumed) {
        super.GenerateField (countColIn, cellShape, isResumed);
        SetPlayerInfo(countCol, countRow);
    }

    private void SetPlayerInfo (int numCol, int numRow) { //!!!
        //установим стартовые позиции
        int firstPlrIdx;
        int secondPlrIdx;

        firstPlrIdx = GetIndex(0, numRow-1); //левый нижний угол
        cells[firstPlrIdx].owner = 0;
        locScreen.locGame.plr[0].colorIdx = cells[firstPlrIdx].colorIdx;

        secondPlrIdx = GetIndex(numCol-1, 0); //правый верхний угол
        cells[secondPlrIdx].owner = 1;
        locScreen.locGame.plr[1].colorIdx = cells[secondPlrIdx].colorIdx;

        //скорректируем цвета игроков, чтобы они не совпадали друг с другом
        if (cells[firstPlrIdx].colorIdx == cells[secondPlrIdx].colorIdx) {
            cells[secondPlrIdx].colorIdx = GetNextColor(cells[secondPlrIdx].colorIdx);
            cells[secondPlrIdx].colorIdxNext =  cells[secondPlrIdx].colorIdx;
            locScreen.locGame.plr[1].colorIdx = cells[secondPlrIdx].colorIdx;
        }
        //скорректируем цвета соседних ячеек, чтобы они не совпадали со стартовыми цветами игроков
        //для игрока 1
        FixNearbyCellsAroundPlayer(firstPlrIdx);
        //для игрока 2
        FixNearbyCellsAroundPlayer(secondPlrIdx);
    }

    private void FixNearbyCellsAroundPlayer (int plrIdx) { //!!!
        for (int k=0; k<maxNearby; k++) {
            if (cells[plrIdx].nearby[k] == -1) {continue;}
            int nearbyIdx = cells[plrIdx].nearby[k];
            if (cells[nearbyIdx].colorIdx == cells[plrIdx].colorIdx) {
                cells[nearbyIdx].colorIdx = GetNextColor(cells[nearbyIdx].colorIdx);
                cells[nearbyIdx].colorIdxNext =  cells[nearbyIdx].colorIdx;
            }
        }
    }

    private int GetNextColor (int inColor) { //!!!
        inColor++;
        if (inColor >= Const.ColorCount) {
            inColor = 0;
        }
        return inColor;
    }


    @Override
    public void draw(Batch batch, float alpha) {
        /*batch.end();
        locScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //нарисуем белый контур для области текущего игрока
        for (int i=0; i<countCol*countRow; i++)
            if ((cells[i].owner == locScreen.currentPlayer) && (!isInnerCell(i))) {
                locScreen.shapeRenderer.setColor(Color.WHITE);
                FillFullCell (i);
            }
        locScreen.shapeRenderer.end();

        //нарисуем черные линии по периметру, чтобы поле не сливалось с панелью
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.BLACK);
 //       shapeRenderer.rect(leftX+1, leftY+1, widthX-1, heightY-1);
 //       shapeRenderer.end();

        batch.begin();*/
        super.draw(batch, alpha);
    }

    public void dispose()   {
        //shapeRenderer.dispose();
    }

    public int CountScore(int playerIdx, MyCell[] locCells) { //!!!
        int score = 0;
        for (int i=0; i<countCol*countRow; i++)
            if (locCells[i].owner == playerIdx) {score++;}
        return score;
    }

    public void FillColor (int colorIn, int locPlayerIdx, MyCell[] locCells) { //!!!
        MyCell ce;
        Queue<MyCell> qe = new Queue();
        for (int i=0; i<countCol*countRow; i++)
            if (locCells[i].owner == locPlayerIdx) {
                locCells[i].colorIdxNext = colorIn; //устанавливаем новый цвет
                locCells[i].phaseIdx = 0; //устанавливаем начальную фазу анимации
                qe.addLast(locCells[i]);
            }
        while (qe.size > 0) {
            ce = qe.removeFirst();
            for (int k=0; k<maxNearby; k++) {
                if (ce.nearby[k] == -1) {continue;}
                if ((locCells[ce.nearby[k]].owner == NOBODYCELL) && (locCells[ce.nearby[k]].colorIdx == colorIn)) {
                    //если ячейка свободна и ее цвет совпадает с цветом "хода", то присвоим ее
                    locCells[ce.nearby[k]].owner = locPlayerIdx;
                    locCells[ce.nearby[k]].phaseIdx = 0;
                    qe.addLast(locCells[ce.nearby[k]]); //добавим ее в очередь, вдруг и у нее есть наши "соседи"
                }
            }
        }
    }

    public void PlayerMove(int colorIdx) { //!!!
        //ход очередного игрока цветом colorIn
        int playerIdx = locScreen.currentPlayer;

        locScreen.locGame.plr[playerIdx].colorIdx = colorIdx;
        FillColor(colorIdx, playerIdx, cells);

        locScreen.locGame.plr[playerIdx].score = CountScore(playerIdx, cells);

    }

    public boolean isPossibleMoves (int playerIdx) { //!!!
        for (int i=0; i<countCol*countRow; i++)
            if (cells[i].owner == playerIdx) {
                for (int k=0; k<maxNearby; k++) {
                    if (cells[i].nearby[k] == -1) {continue;}
                    if (cells[cells[i].nearby[k]].owner == NOBODYCELL) {
                        //у игрока еще есть возможность для расширения его владений
                        return true;
                    }
                }
            }
        //игрок уже захватил все, доступное ему пространство.
        return false;
    }
}
