package com.andrewvasiliev.game.test01.Actors;

import com.andrewvasiliev.game.test01.Classes.BaseCell;
import com.andrewvasiliev.game.test01.Classes.Const;
import com.andrewvasiliev.game.test01.Classes.MyCell;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Random;

/**
 * Created by Andrew on 12.03.2017.
 */

public class BaseField  extends Actor {
    protected float cellWidth, cellHeight;
    protected int countCol, countRow;
    protected int WASTECELL = -2; //лишняя ячейка. не отображается. присутствуют в гексах
    protected int maxNearby; //количество соседних ячеек. зависит от формы ячеек

    public  MyCell[] cells;
    public int NOBODYCELL = -1; //ячейка никому не принадлежит

    private ShapeRenderer sr;
    private float leftX, leftY, widthX, heightY;
    private int phaseCount;
    private float animationSpeed = 1.0f; //0.5f; //за сколько секунд должна закончиться анимация
    //private Const.CellShape cellShape;
    private BaseCell cell;
    private float innerR;
    private boolean isSolidField = false; // если true, то области с одним цветом сливаются
    private boolean isGameField = false; //если true, то поле для игрового экрана

    public BaseField(ShapeRenderer inSR, float x, float y, float width, float height, boolean inGameField) {
        leftX = x;
        leftY = y;
        widthX = width;
        heightY = height;
        countCol = 0;
        countRow = 0;
        isGameField = inGameField;
        sr = inSR;
        setBounds(x, y, width, height);
    }

    public void SetSolidMode (boolean inSolid) {
        isSolidField = inSolid;
    }

    public void SetAnimationSpeed (float inAnimSpeed) {
        animationSpeed = inAnimSpeed;
    }

    public void GenerateField (int countColIn, Const.CellShape cellShape, boolean isResumed) {
        countCol = countColIn;

        cellWidth = widthX/countCol;
        if (cellShape == Const.CellShape.HEX) {
            cellWidth = widthX/countCol/1.5f;
        }

        cell = new BaseCell(cellShape, cellWidth);
        cell.setScale(1.0f, 0.9f);
        phaseCount = cell.GetPhaseCount();
        cellHeight = (float)Math.floor(cell.GetHeight());

        switch (cellShape) {
            case RECTANGLE:
                maxNearby = 4;
                countRow = (int)(heightY / cellHeight);
                if (!isGameField) {
                    countRow += 1;
                }
                break;
            case TRIANGLE:
                //новый вариант расположения
                maxNearby = 3;
                countRow = (int)(heightY / cellHeight);
                countCol += countCol - 1;
                if (!isGameField) {
                    countRow += 2;
                    countCol += 2;
                }
                break;
            case RHOMBUS:
                maxNearby = 4;
                countRow = (int)(heightY / cellHeight) * 2 - 1;
                if (!isGameField) {
                    countRow += 4;
                    countCol += 1;
                }
                break;
            case HEX:
                maxNearby = 6;
                innerR = cellHeight / 2.0f; //внутренний радиус гекса
                countRow = (int)(heightY / cellHeight) * 2 - 1;
                if (!isGameField) {
                    countRow += 5;
                    countCol += 1;
                }
                break;
        }

        Random random = new Random();

        //если игра загружена из сохранения, то генерировать поле не надо. выходим
        if (isGameField && isResumed) {
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
                        //новый вариант расположения
                        cells[currIdx].invertY = (((j&1)^(i&1))==0) ? 1.0f : -1.0f;
                        _x = leftX + cellWidth/2.0f * (float)(j+1) - cellWidth/2.0f ;
                        _y = leftY + heightY - (float)i * cellHeight -
                                (float)Math.floor(cells[currIdx].invertY == -1.0f ? (float)Math.sqrt(3) * cellWidth / 6f : (float)Math.sqrt(3) * cellWidth / 3f);
                        if (isGameField) {
                            _x += cellWidth/2.0f;
                        }
                        //заполним индексы соседних ячеек
                        for (int k=0; k<maxNearby; k++) {
                            nx = j;
                            ny = i;
                            if (cells[currIdx].invertY == 1.0f) {
                                switch (k) {
                                    case 0: nx--; break;
                                    case 1: nx++; break;
                                    case 2: ny++; break;
                                }
                            } else {
                                switch (k) {
                                    case 0: nx--; break;
                                    case 1: nx++; break;
                                    case 2: ny--; break;
                                }
                            }
                            cells[currIdx].nearby[k] = (nx >= 0 && nx < countCol && ny >= 0 && ny < countRow) ? GetIndex(nx, ny) : -1;
                        }
                        break;
                    case RHOMBUS:
                        cells[currIdx].invertY = 1.0f;
                        _x = leftX + (float)j * cellWidth + cellWidth/2.0f +
                                (even ? 0 : cellWidth/2); //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * cellHeight/2.0f;
                        if (!isGameField) {
                            _x -= cellWidth / 2;
                        } else {
                            _y -= cellHeight/2.0f;
                        }
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
                        _x = leftX + (float)j * cellWidth/2.0f*3 +
                                (even ? 0 : cellWidth/2.0f*1.5f); //для нечетных рядов сдвигаем
                        _y = leftY + heightY - (float)i * innerR + innerR; //т.к. гекс по высоте меньше чем по ширине, то используем в расчетах внутренний радиус
                        if (isGameField) {
                            _x += cellWidth / 2.0f  * 1.5f;
                            _y -= innerR * 2.0f;
                        }
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
//для теста перемычек
                cells[currIdx].colorIdx = random.nextInt(Const.ColorCount);
                //cells[currIdx].colorIdx = 4;//random.nextInt(Const.ColorCount);
                cells[currIdx].colorIdxNext = cells[currIdx].colorIdx;
                cells[currIdx].phaseIdx = 0;
                cells[currIdx].animDuration = 0f;
                cells[currIdx].setPosition(_x,_y);
            }
        }
    }

    protected int GetIndex (int col, int row) {
        return row*countCol + col;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float locDelta = Gdx.graphics.getDeltaTime();
        int ni;
        int i;
        Color borderColor;
        //boolean even;
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        //sr.begin(ShapeRenderer.ShapeType.Line);
        for (int m=0; m<countRow; m++) {
            for (int j=0; j<countCol; j++) {
                //even = (m & 1) == 0; // четный ряд?
                i = GetIndex(j, m);
                //for (int i=0; i<cells.length; i++) {
                if (cells[i].owner == WASTECELL) {
                    continue;
                } //неотображаемые ячейки (лишние)

                if (cells[i].phaseIdx != -1) { //-1 состояние покоя и фаза 0
                    cells[i].animDuration += locDelta;
                    cells[i].phaseIdx = (int) (cells[i].animDuration / (animationSpeed / (float) phaseCount));
                    if (cells[i].phaseIdx >= phaseCount) {
                        cells[i].phaseIdx = -1;
                        cells[i].animDuration = 0.0f;
                        cells[i].colorIdx = cells[i].colorIdxNext;

                        //для теста бесконечного вращения (начало)
                        //cells[i].phaseIdx = 0;
                        //cells[i].animDuration -= animationSpeed;
                        //для теста (конец)
                    }
                }

/*  //как-то не очень хочется передавать сюда GameFieldScreen. надо подумать, как выделять захваченное текущим игроком
                if ((isGameField) && (locScreen.currentPlayer == cells[i].owner)) {
                    borderColor = Color.WHITE; // ячейка активного игрока
                } else {
                    borderColor = Const.borderColor;
                }
  */

                //для уменьшения количества треугольников, попробуем отрисовывать для ячеек, у которых все соседи того же цвета
                //и состояние покоя, просто квадрат, занимающий все место
                if (isInnerCell(i) && (cells[i].phaseIdx == -1)) {
                    //рисуем квадрат размером в ячейку
                    sr.setColor(Const.colorArr[cells[i].colorIdx]);
                    float dx = cellWidth /2.0f;
                    float dy = cellHeight / 2.0f;
                    float x1 = cells[i].x - dx;
                    float y1 = cells[i].y + dy;
                    float x3 = cells[i].x + dx;
                    float y3 = cells[i].y - dy;
                    sr.triangle(x1, y1,  x1, y3,  x3, y3);
                    sr.triangle(x1, y1,  x3, y1,  x3, y3);

                } else {
                    cell.draw(cells[i].x, cells[i].y, cells[i].invertY, cells[i].phaseIdx, cells[i].colorIdx, cells[i].colorIdxNext, sr, Const.borderColor);

                    //отрисовка перемычек для слияния одинаковых цветов в единое целое
                    if (isSolidField && (cells[i].phaseIdx == -1)) {
                        //если ячейка в состоянии покоя и опция solidField включена, то рисуем перемычки для одинаковых соседей
                        for (int k = 0; k < maxNearby; k++) {
                            ni = cells[i].nearby[k];
                            if (ni == -1) {
                                continue;
                            }
                            //если оставить этот кусочек условия, то рисовать по два раза одно и то же будет, но зато все выглядит красиво
                            //если убрать (как сейчас), то рисовать будет меньше, но появятся огрехи у "ушек перемычек"
                            if (/*(ni <= i) ||*/ (cells[i].colorIdx != cells[ni].colorIdx) || (cells[ni].phaseIdx != -1)) {
                                continue;
                            }
                            cell.drawbridge(
                                    sr,
                                    cells[i].x, cells[i].y, cells[i].invertY, k,
                                    cells[ni].x, cells[ni].y, cells[ni].invertY,
                                    isColorMatch(ni, cells[i].nearby[(k - 1) < 0 ? (maxNearby - 1) : (k - 1)]), isColorMatch(ni, cells[i].nearby[(k + 1) == maxNearby ? 0 : (k + 1)]));
                        }
                    }
                }
            }
        }
        sr.end();
        batch.begin();
    }

    private boolean isInnerCell (int idx) {
        //возвращает true если все соседи того же цвета
        int ni;
        boolean result = true;
        for (int k = 0; k < maxNearby; k++) {
            ni = cells[idx].nearby[k];
            if (ni == -1) {
                result = false;
            }
            result = result && (cells[idx].colorIdx == cells[ni].colorIdx) && (cells[ni].phaseIdx == -1);
            if (!result) {
                break;
            }
        }
        return result;
    }

    private boolean isColorMatch (int idx, int idxNearby) {
        /*if (idxNearby == -1) {
            return false;
        }
        return cells[idx].colorIdx == cells [idxNearby].colorIdx;*/

        return (idxNearby != -1) && (cells[idx].colorIdx == cells [idxNearby].colorIdx);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
        //return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }
}
