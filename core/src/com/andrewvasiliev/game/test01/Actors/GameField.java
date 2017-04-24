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

public class GameField extends Actor {
    private float leftX, leftY, widthX, heightY;
    private int countCol, countRow;
    private GameFieldScreen locScreen;
    public MyCell[] cells;
    private BaseCell cell;
    private float innerR;
    private ShapeRenderer sr;
    public int NOBODYCELL = -1; //ячейка никому не принадлежит
    private int WASTECELL = -2; //лишняя ячейка. не отображается. присутствуют в гексах
    private int maxNearby; //количество соседних ячеек. зависит от формы ячеек

    //private Const.CellShape cellShape;

    private int phaseCount;
    private float animationSpeed = 0.5f; //за сколько секунд должна закончиться анимация

    public GameField(GameFieldScreen gameFieldScreen, float x, float y, float width, float height) {
        locScreen = gameFieldScreen;
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = 0;
        countRow = 0;
        //shapeRenderer = new ShapeRenderer();
        sr = locScreen.shapeRenderer;
        phaseCount = 0;
    }

    public void GenerateField (int countColIn, Const.CellShape cellShape, boolean isResumed) {
        countCol = countColIn;

        float cellWidth = widthX/countCol;
        float cellHeight;

        if (cellShape == Const.CellShape.HEX) {
            cellWidth = widthX/countCol/1.5f;
        }

        cell = new BaseCell(cellShape, cellWidth/*, cellHeight*/);
        cell.setScale(1.0f, 0.9f);
        phaseCount = cell.GetPhaseCount();
        cellHeight = (float)Math.floor(cell.GetHeight());

        switch (cellShape) {
            case RECTANGLE:
                maxNearby = 4;
                countRow = (int)(heightY / cellHeight);
                break;
            case TRIANGLE:
                maxNearby = 4;
                countRow = (int)(heightY / cellHeight) * 2;
                break;
            case RHOMBUS:
                maxNearby = 4;
                countRow = (int)(heightY / cellHeight) * 2 - 1;
                break;
            case HEX:
                maxNearby = 6;
                innerR = cellHeight / 2.0f; //внутренний радиус гекса
                //попробуем вычислять кол-во рядов исходя из размеров фигуры
                countRow = (int)(heightY / cellHeight) * 2 - 1;
                //leftX += cellWidth / 4; //сдвинем на половину радиуса, т.к. справа получается пустота шириной в радиус
                break;
        }


        Random random = new Random();

//для теста
        //countCol = 3;
        //countRow = 1;
//для теста
        //если игра загружена из сохранения, то генерировать поле не надо. выходим
        if (isResumed) {
            return;
        }
        cells = new MyCell[countCol * countRow];
        for (int i=0; i<cells.length; i++) {
            cells[i] = new MyCell();
        }

        float _x = 0.0f;
        float _y = 0.0f;
        boolean even; //четный ряд
        int currIdx;
        int nx,ny;  //координаты возможных соседей для ячейки
        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                even = (i & 1) == 0; // четный ряд?
                currIdx = GetIndex(j, i);
                cells[currIdx].owner = NOBODYCELL; // никому не принадлежит

                cells[currIdx].nearby = new int[maxNearby]; //массив индексов соседних ячеек

                switch (cellShape) {
                    case RECTANGLE:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f;
                        _y = leftY + heightY - (float)i * cellHeight - cellHeight/2.0f;
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            switch (k) {
                                case 0: nx--; break;
                                case 1: ny--; break;
                                case 2: nx++; break;
                                case 3: ny++; break;
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case TRIANGLE:
                        cells[currIdx].invertY = even ? -1.0f : 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2); //для четных рядов сдвигаем
                        _y = leftY + heightY - (float)Math.floor(even ? (float)Math.sqrt(3) * cellWidth / 6f : (float)Math.sqrt(3) * cellWidth / 3f) -
                                (float)(i/2) * cellHeight;
                        if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка. перевернутая ячейка
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: ny++; break;
                                    case 2: ny--; break;
                                    case 3: ny--; nx--; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny--; break;
                                    case 1: nx++; ny--; break;
                                    case 2: nx++; ny++; break;
                                    case 3: ny++; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case RHOMBUS:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2); //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка.
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: nx--; ny--; break;
                                    case 2: ny--; break;
                                    case 3: ny++; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny++; break;
                                    case 1: ny--; break;
                                    case 2: nx++; ny--; break;
                                    case 3: nx++; ny++; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case HEX:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth * 1.5f +
                                (even ? 0 : cellWidth/2.0f*1.5f) + //для нечетных рядов сдвигаем
                                cellWidth / 2.0f * 1.5f ;
                        //_y = leftY + heightY - (float)i * cellHeight/2.0f - cellHeight/2.0f;
                        _y = leftY + heightY - (float)i * innerR - innerR; //т.к. гекс по высоте меньше чем по ширине, то используем в расчетах внутренний радиус
                        if (!even && j==(countCol-1)) {
                            cells[currIdx].owner = WASTECELL; //метим лишние(выходят за пределы экрана) ячейки
                        }
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (even) { //четная строка.
                                switch (k) {
                                    case 0: nx--; ny++; break;
                                    case 1: nx--; ny--; break;
                                    case 2: ny-=2; break;
                                    case 3: ny--; break;
                                    case 4: ny++; break;
                                    case 5: ny+=2; break;
                                }
                            } else { //нечетная строка.
                                switch (k) {
                                    case 0: ny++; break;
                                    case 1: ny--; break;
                                    case 2: ny-=2; break;
                                    case 3: nx++; ny--; break;
                                    case 4: nx++; ny++; break;
                                    case 5: ny+=2; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx>=0 && nx <countCol && ny>=0 && ny<countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                }

                cells[currIdx].colorIdx = random.nextInt(Const.ColorCount);
                cells[currIdx].colorIdxNext = cells[currIdx].colorIdx;
                cells[currIdx].phaseIdx = 0;
                cells[currIdx].animDuration = 0f;
                cells[currIdx].setPosition(_x,_y);
            }
        }

        SetPlayerInfo(countCol, countRow);
    }

    private void SetPlayerInfo (int numCol, int numRow) {
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

    private void FixNearbyCellsAroundPlayer (int plrIdx) {
        for (int k=0; k<maxNearby; k++) {
            if (cells[plrIdx].nearby[k] == -1) {continue;}
            int nearbyIdx = cells[plrIdx].nearby[k];
            if (cells[nearbyIdx].colorIdx == cells[plrIdx].colorIdx) {
                cells[nearbyIdx].colorIdx = GetNextColor(cells[nearbyIdx].colorIdx);
                cells[nearbyIdx].colorIdxNext =  cells[nearbyIdx].colorIdx;
            }
        }
    }

    private int GetNextColor (int inColor) {
        inColor++;
        if (inColor >= Const.ColorCount) {
            inColor = 0;
        }
        return inColor;
    }

    private int GetIndex (int col, int row) {
        return row*countCol + col;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.end();
        float deltaTime = Gdx.graphics.getDeltaTime();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        Color borderColor;
        int currIdx;

        for (int i=0; i<countRow; i++) {
            for (int j=0; j<countCol; j++) {
                currIdx = GetIndex(j, i);
                if (cells[currIdx].owner == WASTECELL) {continue;} //неотображаемые ячейки (лишние)


                if (cells[currIdx].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                    cells[currIdx].animDuration += deltaTime;
                    cells[currIdx].phaseIdx = (int)(cells[currIdx].animDuration/(animationSpeed/(float)phaseCount));
                    if (cells[currIdx].phaseIdx >= phaseCount) {
                        cells[currIdx].phaseIdx = -1;
                        cells[currIdx].animDuration = 0.0f;
                        cells[currIdx].colorIdx = cells[currIdx].colorIdxNext;
                    }
                }

                if (locScreen.currentPlayer == cells[currIdx].owner) {
                    borderColor = Color.WHITE; // ячейка активного игрока
                } else {
                    borderColor = Const.borderColor;
                }
                cell.draw(cells[currIdx].x, cells[currIdx].y, cells[currIdx].invertY, cells[currIdx].phaseIdx, cells[currIdx].colorIdx, cells[currIdx].colorIdxNext, sr, borderColor);
            }
        }
        sr.end();

        //нарисуем черные линии по периметру, чтобы поле не сливалось с панелью
/*        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(leftX+1, leftY+1, widthX-1, heightY-1);
        shapeRenderer.end();
*/
        batch.begin();
        super.draw(batch, alpha);
    }

    public void dispose()   {
        //shapeRenderer.dispose();
    }

    public int CountScore(int playerIdx, MyCell[] locCells) {
        int score = 0;
        for (int i=0; i<countCol*countRow; i++)
            if (locCells[i].owner == playerIdx) {score++;}
        return score;
    }

    public void FillColor (int colorIn, int locPlayerIdx, MyCell[] locCells) {
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

    public void PlayerMove(int colorIdx) {
        //ход очередного игрока цветом colorIn
        int playerIdx = locScreen.currentPlayer;

        locScreen.locGame.plr[playerIdx].colorIdx = colorIdx;
        FillColor(colorIdx, playerIdx, cells);

        locScreen.locGame.plr[playerIdx].score = CountScore(playerIdx, cells);

    }

    public boolean isPossibleMoves (int playerIdx) {
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
